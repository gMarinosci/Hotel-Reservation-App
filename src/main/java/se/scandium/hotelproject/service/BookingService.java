package se.scandium.hotelproject.service;

import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.entity.RoomType;
import se.scandium.hotelproject.exception.RecordNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {


    //As a reception staff, I can search for available free dates for rooms
    List<RoomDto> searchAvailableRooms(LocalDate fromDate, LocalDate toDate, RoomType roomType);

    //As a reception staff, I must be able to search for a booking
    BookingDto findById(int bookingId) throws RecordNotFoundException;

    //As a reception staff, I can mark a room booking as paid
    void updatePaymentStatus(int id, boolean pay) throws RecordNotFoundException;

    //As a reception staff, I will be able to change a booking
    BookingDto update(BookingDto bookingDto) throws RecordNotFoundException;

    //As a reception staff, I can see an overview of the bookings
    List<BookingDto> getAlBooking();

    //As a reception staff, I must be able to book a room for a specific date range
    BookingDto createBooking(BookingDto bookingDto) throws RecordNotFoundException;

    //As a reception staff, I can see all the bookings for a specific room
    List<BookingDto> getBookingListByRoomId(int roomId);

    //As a reception staff, I can see all the bookings for a specific day
    List<BookingDto> getBookingListBySpecificDay(LocalDate fromDate, LocalDate toDate);

    List<RoomDto> getAvailableRooms (LocalDate fromDate, LocalDate toDate, RoomType roomType);

    List<BookingDto> getListByRoomName(String name);

}
