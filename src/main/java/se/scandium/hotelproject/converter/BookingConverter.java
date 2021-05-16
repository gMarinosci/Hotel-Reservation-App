package se.scandium.hotelproject.converter;

import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.entity.Booking;

public interface BookingConverter {

    BookingDto convertBookingToDto(Booking booking);

    Booking convertDtoToBooking(BookingDto bookingDto);
}
