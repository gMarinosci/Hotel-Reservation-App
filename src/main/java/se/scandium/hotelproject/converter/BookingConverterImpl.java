package se.scandium.hotelproject.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Booking;
import se.scandium.hotelproject.entity.Customer;
import se.scandium.hotelproject.entity.Room;

@Component
public class BookingConverterImpl implements BookingConverter {

    CustomerConverter customerConverter;
    RoomConverter roomConverter;

    @Autowired
    public void setCustomerConverter(CustomerConverter customerConverter) {
        this.customerConverter = customerConverter;
    }

    @Autowired
    public void setRoomConverter(RoomConverter roomConverter) {
        this.roomConverter = roomConverter;
    }

    @Override
    public BookingDto convertBookingToDto(Booking booking) {
        BookingDto dto = null;
        if (booking != null) {
            dto = new BookingDto();
            dto.setId(booking.getId());
            dto.setStatus(booking.isStatus());
            dto.setFromDate(booking.getFromDate());
            dto.setToDate(booking.getToDate());
            dto.setBreakfast(booking.isBreakfast());
            dto.setLunch(booking.isLunch());
            dto.setPay(booking.isPay());
            dto.setNumberOfPersons(booking.getNumberOfPersons());
            dto.setFullPrice(booking.getFullPrice());
            dto.setBookingDays(booking.getBookingDays());

            Customer customer = booking.getCustomer();
            if (customer != null) {
                dto.setCustomer(customerConverter.convertEntityToDto(customer));
            }

            Room room = booking.getRoom();
            if (room != null) {
                dto.setRoom(roomConverter.convertEntityToDto(room));
            }

        }
        return dto;
    }

    @Override
    public Booking convertDtoToBooking(BookingDto dto) {
        Booking booking = null;
        if (dto != null) {
            booking = new Booking();
            booking.setId(dto.getId());
            booking.setStatus(dto.isStatus());
            booking.setFromDate(dto.getFromDate());
            booking.setToDate(dto.getToDate());
            booking.setBreakfast(dto.isBreakfast());
            booking.setLunch(dto.isLunch());
            booking.setPay(dto.isPay());
            booking.setNumberOfPersons(dto.getNumberOfPersons());
            booking.setFullPrice(dto.getFullPrice());
            booking.setBookingDays(dto.getBookingDays());
            booking.setPayType(dto.getPayType());

            CustomerDto customerDto = dto.getCustomer();
            if (customerDto != null) {
                booking.setCustomer(customerConverter.convertDtoToEntity(customerDto));
            }

            RoomDto roomDto = dto.getRoom();
            if (roomDto != null) {
                booking.setRoom(roomConverter.convertDtoToEntity(roomDto));
            }

        }
        return booking;
    }
}
