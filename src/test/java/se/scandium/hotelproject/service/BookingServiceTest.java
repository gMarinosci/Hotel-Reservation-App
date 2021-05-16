package se.scandium.hotelproject.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.scandium.hotelproject.dto.AddressDto;
import se.scandium.hotelproject.dto.BookingDto;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Address;
import se.scandium.hotelproject.entity.Gender;
import se.scandium.hotelproject.entity.Hotel;
import se.scandium.hotelproject.entity.RoomType;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.repository.HotelRepository;

import java.time.LocalDate;

@SpringBootTest
@Transactional
public class BookingServiceTest {

    @Autowired
    BookingService testObject;

    @Autowired
    RoomService roomService;

    @Autowired
    HotelRepository hotelRepository;

    BookingDto bookingDto1;
    BookingDto bookingDto2;

    RoomDto savedRoomDto1;
    RoomDto savedRoomDto2;
    BookingDto actual1;
    BookingDto actual2;

    @BeforeEach
    public void setup() throws RecordNotFoundException {
        // create hotel
        Hotel hotel = new Hotel();
        hotel.setName("TEST");
        hotel.setStar(4);
        hotel.setAddress(new Address("Teleborg", "35252", "VAXJO", "SWEDEN"));
        hotelRepository.save(hotel);

        // create room1
        RoomDto roomDto1 = new RoomDto();
        roomDto1.setName("Room1");
        roomDto1.setPrice(100);
        roomDto1.setType(RoomType.Single);
        roomDto1.setSize(10);
        roomDto1.setLocation("loc 1");
        roomDto1.setBeds(true);
        roomDto1.setNumberOfBeds(1);
        roomDto1.setDescription("dec 1");
        savedRoomDto1 = roomService.saveOrUpdate(roomDto1);

        // create room2
        RoomDto roomDto2 = new RoomDto();
        roomDto2.setName("Room2");
        roomDto2.setPrice(200);
        roomDto2.setType(RoomType.Double);
        roomDto2.setSize(20);
        roomDto2.setLocation("loc 2");
        roomDto2.setBeds(true);
        roomDto2.setNumberOfBeds(2);
        roomDto2.setDescription("dec 2");
        savedRoomDto2 = roomService.saveOrUpdate(roomDto2);




    }

    @Test
    public void test_save_booking() throws RecordNotFoundException {
        // instantiate booking
        bookingDto1 = new BookingDto();
        bookingDto1.setFromDate(LocalDate.parse("2021-01-01"));
        bookingDto1.setToDate(LocalDate.parse("2021-01-03"));
        bookingDto1.setBreakfast(true);
        bookingDto1.setLunch(true);
        bookingDto1.setPay(true);
        bookingDto1.setNumberOfPersons(2);
        bookingDto1.setStatus(false);

        CustomerDto customerDto1 = new CustomerDto();
        customerDto1.setFirstName("Test");
        customerDto1.setLastName("Testsson");
        customerDto1.setIdNumber("20001010-1234");
        customerDto1.setAge(30);
        customerDto1.setGender(Gender.MALE);
        AddressDto addressDto = new AddressDto("Teleborg", "35252", "VAXJO", "SWEDEN");
        customerDto1.setAddress(addressDto);

        bookingDto1.setCustomer(customerDto1);
        bookingDto1.setRoom(savedRoomDto1);

        actual1 = testObject.createBooking(bookingDto1);
        System.out.println("actual1 = " + actual1);
        Assertions.assertEquals("Test", actual1.getCustomer().getFirstName());
        Assertions.assertEquals(400.0, actual1.getFullPrice());
        Assertions.assertEquals(100.0, actual1.getRoom().getPrice());



        // instantiate booking
        bookingDto2 = new BookingDto();
        bookingDto2.setFromDate(LocalDate.parse("2021-01-04"));
        bookingDto2.setToDate(LocalDate.parse("2021-01-05"));
        bookingDto2.setBreakfast(true);
        bookingDto2.setLunch(true);
        bookingDto2.setPay(true);
        bookingDto2.setNumberOfPersons(3);
        bookingDto2.setStatus(false);

        CustomerDto customerDto2 = new CustomerDto();
        customerDto2.setFirstName("Test2");
        customerDto2.setLastName("Testsson");
        customerDto2.setIdNumber("20001111-2222");
        customerDto2.setAge(30);
        customerDto2.setGender(Gender.FEMALE);
        customerDto2.setAddress(addressDto);

        bookingDto2.setCustomer(customerDto2);
        bookingDto2.setRoom(savedRoomDto1);

        actual2 = testObject.createBooking(bookingDto2);
        System.out.println("actual2 = " + actual2);
        Assertions.assertEquals("Test2", actual2.getCustomer().getFirstName());
        Assertions.assertEquals(300.0, actual2.getFullPrice());
        Assertions.assertEquals(100.0, actual2.getRoom().getPrice());
    }


}
