package se.scandium.hotelproject.service;

import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.exception.RecordNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {


    //Implement later = As a reception staff, I can search for available free dates for rooms
    List<RoomDto> searchAvailableFreeDates(LocalDate date);

    //As a reception staff, I must be able to search for a booking = findById
    BookingDto findById(int bookingId) throws RecordNotFoundException;

    //As a reception staff, I can mark a room booking as paid =
    void updatePaymentStatus(int id, boolean pay) throws RecordNotFoundException;

    //As a reception staff, I will be able to change a booking = update
    BookingDto update(BookingDto bookingDto) throws RecordNotFoundException;

    //As a reception staff, I can see an overview of the bookings = findAll
    List<BookingDto> getAlBooking();

    //DONE = As a reception staff, I must be able to book a room for a specific date range = SAVE
    BookingDto createBooking(BookingDto bookingDto) throws RecordNotFoundException;

    //DONE = As a reception staff, I can see all the bookings for a specific room = findAllByRoom_Id
    List<BookingDto> getBookingListByRoomId(int roomId);

    //DONE = As a reception staff, I can see all the bookings for a specific day
    List<BookingDto> getBookingListBySpecificDay(LocalDate fromDate, LocalDate toDate) ;


}
