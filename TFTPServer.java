package assignment3;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class TFTPServer 
{
	public static final int TFTPPORT = 4970;
	public static final int BUFSIZE = 516;
	public static final String READDIR = "/Users/gabrielemarinosci/IdeaProjects/compnets_ass3/out/production/compnets_ass3/assignment3/"; //custom address at your PC
	public static final String WRITEDIR = "/home/username/write/"; //custom address at your PC
	// OP codes
	public static final int OP_RRQ = 1;
	public static final int OP_WRQ = 2;
	public static final int OP_DAT = 3;
	public static final int OP_ACK = 4;
	public static final int OP_ERR = 5;

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

			new Thread()
			{
				public void run() 
				{
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
				}
			}.start();
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
		// Create datagram packet
		
		// Receive packet
		
		// Get client address and port from the packet
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
		// See "TFTP Formats" in TFTP specification for the RRQ/WRQ request contents
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
			// See "TFTP Formats" in TFTP specification for the DATA and ACK packet contents
			FileInputStream in = null;
			short blockNum = 1;

			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			while (true) {

				try {
					dataLength = in.read(buf);
				} catch (IOException e) {
					e.printStackTrace();
				}

				DatagramPacket dataPacket = createDataPacket((blockNum, buf, dataLength);
				boolean result = send_DATA_receive_ACK(sendSocket, dataPacket);

				if (!result) {
					System.err.println("Oh damn sorry dawg, something went wrong...");
				}

				if (dataLength < BUFSIZE - 4) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}


		}
		else if (opcode == OP_WRQ) 
		{
			FileOutputStream out = null;

			try {
				out = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			boolean result = receive_DATA_send_ACK(sendSocket, out);
		}
		else 
		{
			System.err.println("Invalid request. Sending an error packet.");
			// See "TFTP Formats" in TFTP specification for the ERROR packet contents
			//send_ERR(params);
			return;
		}		
	}
	
	/**
	To be implemented
	*/
	private boolean send_DATA_receive_ACK(DatagramSocket socket, DatagramPacket packet)  {

		byte[] buf = new byte[4];
		DatagramPacket ackPacket = new DatagramPacket(buf, buf.length);
		int attempt = 0;

		while (attempt < 6) {
			try {
				socket.send(packet);
				System.out.println("sent");
				socket.setSoTimeout(5000);
				socket.receive(ackPacket);
				System.out.println("received");

				short ackNum = getBlockNum(ackPacket);

				if (ackNum == getBlockNum(packet)) {
					return true;
				} else if (ackNum == -1) {
					return false;
				} else {
					throw new SocketTimeoutException();
				}
			} catch (SocketTimeoutException e) {
				System.out.println("Request timed out. Sending packet again");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}
	
	private boolean receive_DATA_send_ACK(DatagramSocket socket, FileOutputStream out)
	{
		byte[] buf = new byte[BUFSIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		try {
			socket.receive(packet);
			byte[] data = packet.getData();
			out.write(data, 4, packet.getLength() - 4);
			System.out.println("packet received");
			socket.send(createAckPacket((short) 0));
			System.out.println("ack sent");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("could not close stream");
		}

		return true;
	}
	
	//private void send_ERR(params)
	//{}

	private DatagramPacket createAckPacket(short blockNum) {

		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putShort((short) OP_ACK);
		buf.putShort(blockNum);

		return new DatagramPacket(buf.array(), buf.array().length);
	}


	private DatagramPacket createDataPacket(short blockNum, byte[] data, int len) {

		ByteBuffer buf = ByteBuffer.allocate(BUFSIZE);
		buf.putShort((short) OP_DAT);
		buf.putShort(blockNum);
		buf.put(data, 0, len);

		return new DatagramPacket(buf.array(), 4 + len);
	}


	private short getBlockNum(DatagramPacket packet) {

		ByteBuffer buf = ByteBuffer.wrap(packet.getData());
		short opcode = buf.getShort();

		if (opcode == OP_ERR) {
			return -1;
		}

		return buf.getShort();
	}
	//private DatagramPacket createErrorPacket()
}



