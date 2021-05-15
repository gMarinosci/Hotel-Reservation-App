package se.scandium.hotelproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.converter.BookingConverter;
import se.scandium.hotelproject.converter.RoomConverter;
import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Booking;
import se.scandium.hotelproject.entity.Customer;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.exception.ArgumentInvalidException;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.repository.BookingRepository;
import se.scandium.hotelproject.repository.CustomerRepository;
import se.scandium.hotelproject.repository.RoomRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {


    BookingConverter bookingConverter;
    RoomConverter roomConverter;
    BookingRepository bookingRepository;
    CustomerRepository customerRepository;
    RoomRepository roomRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, RoomConverter roomConverter, BookingConverter bookingConverter, CustomerRepository customerRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingConverter = bookingConverter;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.roomConverter = roomConverter;
    }


    @Override
    public List<RoomDto> searchAvailableFreeDates(LocalDate date) {
        List<Room> roomList = new ArrayList<>();
        roomRepository.findAll().forEach(roomList::add);
        List<Room> unAvailableRooms = bookingRepository.findAllByStatusFalseAndToDateGreaterThan(date).stream().map(Booking::getRoom).collect(Collectors.toList());
        roomList.removeAll(unAvailableRooms);
        System.out.println("roomList =###########  " + roomList);
        return roomList.stream().map(room -> roomConverter.convertEntityToDto(room)).collect(Collectors.toList());
    }

    @Override
    public BookingDto findById(int bookingId) throws RecordNotFoundException {
        if (bookingId == 0) throw new ArgumentInvalidException("bookingId is not valid");
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) throw new RecordNotFoundException("data not found");
        return bookingConverter.convertBookingToDto(bookingOptional.get());
    }

    @Override
    public void updatePaymentStatus(int bookingId, boolean pay) throws RecordNotFoundException {
        if (bookingId == 0) throw new ArgumentInvalidException("bookingId is not valid");
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) throw new RecordNotFoundException("data not found");
        bookingRepository.updatePaymentStatus(bookingOptional.get().getId(), pay);
    }

    @Override
    public BookingDto update(BookingDto bookingDto) throws RecordNotFoundException {
        if (bookingDto == null) throw new ArgumentInvalidException("booking should not be null");
        if (bookingDto.getId() == 0) throw new ArgumentInvalidException("bookingId should not be zero");
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingDto.getId());
        if (bookingOptional.isEmpty()) throw new RecordNotFoundException("booking id is not valid - data not found");

        if (bookingDto.getCustomer().getId() != 0) {
            Optional<Customer> customerOptional = customerRepository.findById(bookingDto.getCustomer().getId());
            if (customerOptional.isEmpty())
                throw new RecordNotFoundException("customer id is not valid - data not found");
        }

        if (bookingDto.getRoom() == null) throw new ArgumentInvalidException("room should not be null");
        Optional<Room> roomOptional = roomRepository.findById(bookingDto.getRoom().getId());
        if (roomOptional.isEmpty()) throw new RecordNotFoundException("customer id is not valid - data not found");

        Booking booking = bookingConverter.convertDtoToBooking(bookingDto);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingConverter.convertBookingToDto(savedBooking);
    }

    @Override
    public List<BookingDto> getAlBooking() {
        List<Booking> bookings = new ArrayList<>();
        bookingRepository.findAll().iterator().forEachRemaining(bookings::add);
        return bookings.stream().map(booking -> bookingConverter.convertBookingToDto(booking)).collect(Collectors.toList());
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto) throws RecordNotFoundException {
        if (bookingDto == null) throw new ArgumentInvalidException("booking should not be null");
        if (bookingDto.getId() != 0) throw new ArgumentInvalidException("bookingId should be zero or null");

        if (bookingDto.getCustomer() == null) throw new ArgumentInvalidException("customer should not be null");
        if (bookingDto.getCustomer().getId() != 0)
            throw new ArgumentInvalidException("customerId should be zero or null");

        if (bookingDto.getRoom() == null) throw new ArgumentInvalidException("room should not be null");
        if (bookingDto.getRoom().getId() == 0)
            throw new ArgumentInvalidException("room id should not be zero - room id is not valid");
        Optional<Room> roomOptional = roomRepository.findById(bookingDto.getRoom().getId());
        if (roomOptional.isEmpty()) throw new RecordNotFoundException("customer id is not valid - data not found");

        // date validation
        if (bookingDto.getFromDate().isEqual(bookingDto.getToDate())
                || bookingDto.getFromDate().isAfter(bookingDto.getToDate())
                || bookingDto.getToDate().isBefore(bookingDto.getFromDate())
        ) throw new ArgumentInvalidException("from date and to date are not valid");

        Booking booking = bookingConverter.convertDtoToBooking(bookingDto);

        // check booking dates
        List<Booking> result = bookingRepository.findAllByStatusFalseAndToDateGreaterThanAndRoomId(booking.getFromDate(), booking.getRoom().getId());
        if (result.size() != 0) throw new ArgumentInvalidException("booking date is not valid");
        booking.setBookingDays(booking.createBookingDays());
        booking.setBookingDays(booking.createBookingDays());
        booking.setFullPrice(booking.calcFullPrice());
        Booking savedBooking = bookingRepository.save(booking);
        return bookingConverter.convertBookingToDto(savedBooking);
    }

    @Override
    public List<BookingDto> getBookingListByRoomId(int roomId) {
        if (roomId == 0) throw new ArgumentInvalidException("room id is not valid");
        List<Booking> bookings = bookingRepository.findAllByRoom_IdAndStatusFalse(roomId);
        return bookings.stream().map(booking -> bookingConverter.convertBookingToDto(booking)).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingListBySpecificDay(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null && toDate == null) throw new ArgumentInvalidException("date is not valid");
        return bookingRepository.findAllByStatusFalseAndFromDateGreaterThanEqualAndToDateLessThan(fromDate, toDate).stream()
                .map(booking -> bookingConverter.convertBookingToDto(booking))
                .collect(Collectors.toList());
    }
}
