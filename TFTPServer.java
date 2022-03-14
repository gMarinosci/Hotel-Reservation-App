package assignment3;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TFTPServer 
{
	public static final int TFTPPORT = 4970;
	public static final int BUFSIZE = 516;
	public static final String READDIR = "/Users/gabrielemarinosci/IdeaProjects/compnets_ass3/files/"; //custom address at your PC
	public static final String WRITEDIR = "/Users/gabrielemarinosci/IdeaProjects/compnets_ass3/files/"; //custom address at your PC
	// OP codes
	public static final int OP_RRQ = 1;
	public static final int OP_WRQ = 2;
	public static final int OP_DAT = 3;
	public static final int OP_ACK = 4;
	public static final int OP_ERR = 5;
	public static final int ERR_LOST = 0;
	public static final int ERR_FNF = 1;
	public static final int ERR_ACCESS = 2;
	public static final int ERR_BADOP = 4;
	public static final int ERR_EXISTS = 6;

	public static final String[] errorCodes = {"Not defined", "File not found.", "Access violation.",
					"Disk full or allocation exceeded.", "Illegal TFTP operation.",
					"Unknown transfer ID.", "File already exists.",
					"No such user."};

	public static void main(String[] args) {
		if (args.length > 0) 
		{
			System.err.printf("usage: java %s\n", TFTPServer.class.getCanonicalName());
			System.exit(1);
		}
		//Starting the server
		try 
		{
			TFTPServer server= new TFTPServer();
			server.start();
		}
		catch (SocketException e) 
			{e.printStackTrace();}
	}
	
	private void start() throws SocketException 
	{
		byte[] buf= new byte[BUFSIZE];
		
		// Create socket
		DatagramSocket socket= new DatagramSocket(null);
		
		// Create local bind point 
		SocketAddress localBindPoint= new InetSocketAddress(TFTPPORT);
		socket.bind(localBindPoint);

		System.out.printf("Listening at port %d for new requests\n", TFTPPORT);

		// Loop to handle client requests 
		while (true) 
		{        
			
			final InetSocketAddress clientAddress = receiveFrom(socket, buf);
			
			// If clientAddress is null, an error occurred in receiveFrom()
			if (clientAddress == null) 
				continue;

			final StringBuffer requestedFile= new StringBuffer();
			final int reqtype = ParseRQ(buf, requestedFile);

			new Thread(() -> {
				try
				{
					DatagramSocket sendSocket= new DatagramSocket(0);

					// Connect to client
					sendSocket.connect(clientAddress);

					System.out.printf("%s request for %s from %s using port %d\n",
							(reqtype == OP_RRQ)?"Read":"Write",
							clientAddress.getHostName(), clientAddress.getAddress(), clientAddress.getPort());

					// Read request
					if (reqtype == OP_RRQ)
					{
						requestedFile.insert(0, READDIR);
						HandleRQ(sendSocket, requestedFile.toString(), OP_RRQ);
					}

					// Write request
					else
					{
						requestedFile.insert(0, WRITEDIR);
						HandleRQ(sendSocket,requestedFile.toString(),OP_WRQ);
					}
					sendSocket.close();
				}
				catch (SocketException e)
					{e.printStackTrace();}
			}).start();
		}
	}
	
	/**
	 * Reads the first block of data, i.e., the request for an action (read or write).
	 * @param socket (socket to read from)
	 * @param buf (where to store the read data)
	 * @return socketAddress (the socket address of the client)
	 */
	private InetSocketAddress receiveFrom(DatagramSocket socket, byte[] buf) 
	{
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

		InetSocketAddress socketAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());
		return socketAddress;
	}

	/**
	 * Parses the request in buf to retrieve the type of request and requestedFile
	 * 
	 * @param buf (received request)
	 * @param requestedFile (name of file to read/write)
	 * @return opcode (request type: RRQ or WRQ)
	 */
	private int ParseRQ(byte[] buf, StringBuffer requestedFile) 
	{
		ByteBuffer wrap = ByteBuffer.wrap(buf);
		short opcode = wrap.getShort();
		int delimiter = -1;

		for (int i = 2; i < buf.length; i++) {
			if (buf[i] == 0) {
				delimiter = i;
				break;
			}
		}

		String fileName = new String(buf, 2, delimiter-2);
		requestedFile.append(fileName);
		return opcode;
	}

	/**
	 * Handles RRQ and WRQ requests 
	 * 
	 * @param sendSocket (socket used to send/receive packets)
	 * @param requestedFile (name of file to read/write)
	 * @param opcode (RRQ or WRQ)
	 */
	private void HandleRQ(DatagramSocket sendSocket, String requestedFile, int opcode)
	{
		File file = new File(requestedFile);
		byte[] buf = new byte[BUFSIZE-4];
		int dataLength = 0;

		if(opcode == OP_RRQ)
		{
			FileInputStream in = null;
			short blockNum = 1;
			boolean result;

			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				System.err.println("File not found.");
				send_ERR(sendSocket, (short) ERR_FNF, "File not found");
				return;
			}

			while (true) {

				try {
					dataLength = in.read(buf);
				} catch (IOException e) {
					e.printStackTrace();
				}

				DatagramPacket dataPacket = createDataPacket(blockNum, buf, dataLength);
				result = send_DATA_receive_ACK(sendSocket, dataPacket);


				if (dataLength < BUFSIZE - 4) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				blockNum++;
			}

			if (!result) {
				System.out.println("Closing request.");
			} else {
				System.out.println("Operation completed successfully\n");
			}
		}

		else if (opcode == OP_WRQ) 
		{

			boolean result = receive_DATA_send_ACK(sendSocket, file);

			if (!result) {
				System.err.println("Terminating request");
			} else {
				System.out.println("Operation completed successfully\n");
			}
		}
		else 
		{
			System.err.println("Invalid request. Sending an error packet.");
			send_ERR(sendSocket, (short) ERR_BADOP, "Bad Operation");
			return;
		}		
	}

	/**
	 * Handles RRQ
	 *
	 * @param socket socket used to send/receive packets
	 * @param packet packet for sending data
	 * @return false is something went wrong
	 */
	private boolean send_DATA_receive_ACK(DatagramSocket socket, DatagramPacket packet)  {

		byte[] buf = new byte[4];
		DatagramPacket ackPacket = new DatagramPacket(buf, buf.length);
		int attempt = 0;

		while (attempt < 10) {
			try {
				socket.send(packet);
				socket.setSoTimeout(5000);
				socket.receive(ackPacket);

				short ackNum = getBlockNum(ackPacket);

				if (ackNum == getBlockNum(packet)) {
					return true;
				} else if (ackNum == -1) {
					return false;
				} else {
					attempt++;
					throw new SocketTimeoutException();
				}
			} catch (SocketTimeoutException e) {
				attempt++;
				System.out.println("Request timed out. Sending packet again");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Handles WRQ
	 *
	 * @param socket socket used to send/receive packets
	 * @param file file to be written
	 * @return false if something went wrong
	 */
	private boolean receive_DATA_send_ACK(DatagramSocket socket, File file)
	{
		byte[] buf = new byte[BUFSIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		int attempt = 0;

		if (file.exists()) {
			System.err.println("File already exists");
			//Here I implemented response with code 6 but python script
			//expects code 4
			//send_ERR(socket, (short) ERR_EXISTS, "File already exists");
			send_ERR(socket, (short) ERR_BADOP, "File already exists");
			return false;
		}
		FileOutputStream out = null;
		short blockNum = 1;

		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			socket.send(createAckPacket((short) 0));
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {

			while (attempt < 10) {
				try {
					socket.send(createAckPacket(blockNum));
					socket.setSoTimeout(5000);
					socket.receive(packet);

					if (packet == null) {
						System.err.println("Connection lost.");
						send_ERR(socket, (short) ERR_LOST, "Connection lost.");
						System.out.println("Deleting corrupt file.");
						file.delete();
						out.close();
					} else {
						byte[] data = packet.getData();
						out.write(data, 4, packet.getLength() - 4);
					}

					short dataBlockNum = getBlockNum(packet);

					if (dataBlockNum == blockNum) {
						break;
					} else if (dataBlockNum == -1) {
						return false;
					} else {
						throw new SocketTimeoutException();
					}

				} catch (SocketTimeoutException e){
					attempt++;
					System.out.println("Request timed out. Sending packet again");
				} catch (IOException e) {
					System.err.println("File could not be written");
					send_ERR(socket, (short) ERR_ACCESS, "File could not be accessed");
					return false;
				}

			}
			if (packet.getLength() - 4 < BUFSIZE -4) {
				try {
					socket.send(createAckPacket(blockNum));
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}

			blockNum++;
		}

		return true;
	}

	/**
	 * Creates and sends error packet to the clients
	 *
	 * @param socket socket used to send/receive packets
	 * @param errCode reference code of the error as described in RFC1350
	 * @param errMsg Error message
	 */
	private void send_ERR(DatagramSocket socket, short errCode, String errMsg) {

		ByteBuffer buf = ByteBuffer.allocate(BUFSIZE);
		buf.putShort((short) OP_ERR);
		buf.putShort(errCode);
		buf.put(errMsg.getBytes(StandardCharsets.UTF_8));
		buf.put((byte) 0);

		DatagramPacket packet = new DatagramPacket(buf.array(), buf.array().length);

		try {
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("Error packet could not be sent.");
			e.printStackTrace();
		}
	}

	/**
	 * Reads the info obtained from the error packet
	 *
	 * @param buf buffer containing error info
	 */
	private void readError(ByteBuffer buf) {

		short errCode = buf.getShort();

		byte[] buffer = buf.array();
		for (int i = 4; i < buffer.length; i++) {
			if (buffer[i] == 0) {
				String msg = new String(buffer, 4, i-4);
				if (errCode > 7) errCode = 0;
				System.err.println(errorCodes[errCode] + ": " + msg);
			}
		}
	}

	/**
	 * Creates ack packet to be sent to the client
	 *
	 * @param blockNum block number of the current block
	 * @return ack packet
	 */
	private DatagramPacket createAckPacket(short blockNum) {

		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putShort((short) OP_ACK);
		buf.putShort(blockNum);

		return new DatagramPacket(buf.array(), buf.array().length);
	}

	/**
	 * Creates Data packet to be sent to the client
	 *
	 * @param blockNum block number of the current block
	 * @param data data read from the file
	 * @param len length of the data read from file
	 * @return data packet
	 */
	private DatagramPacket createDataPacket(short blockNum, byte[] data, int len) {

		ByteBuffer buf = ByteBuffer.allocate(BUFSIZE);
		buf.putShort((short) OP_DAT);
		buf.putShort(blockNum);
		buf.put(data, 0, len);

		return new DatagramPacket(buf.array(), 4 + len);
	}

	/**
	 * Returns the block number of the given packet
	 *
	 * @param packet
	 * @return -1 if the packet is an error packet
	 */
	private short getBlockNum(DatagramPacket packet) {

		ByteBuffer buf = ByteBuffer.wrap(packet.getData());
		short opcode = buf.getShort();

		if (opcode == OP_ERR) {
			System.err.println("Connection ended.");
			readError(buf);
			return -1;
		}

		return buf.getShort();
	}
}



