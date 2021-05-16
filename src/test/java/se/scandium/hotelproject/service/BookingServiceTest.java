package se.scandium.hotelproject.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.scandium.hotelproject.dto.*;
import se.scandium.hotelproject.entity.Address;
import se.scandium.hotelproject.entity.Gender;
import se.scandium.hotelproject.entity.Hotel;
import se.scandium.hotelproject.entity.RoomType;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.repository.CustomerRepository;
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

    @Autowired
    CustomerService customerService;

    BookingDto bookingDto1;
    BookingDto bookingDto2;

    RoomDto savedRoomDto1;
    RoomDto savedRoomDto2;
    BookingDto actual1;
    BookingDto actual2;
    BookingDto actual3;
    CustomerDto savedCustomer1;
    CustomerDto savedCustomer2;
    Hotel savedHotel;

    @BeforeEach
    public void setup() throws RecordNotFoundException {
        // create hotel
        Hotel hotel = new Hotel();
        hotel.setName("TEST");
        hotel.setStar(4);
        hotel.setAddress(new Address("Teleborg", "35252", "VAXJO", "SWEDEN"));
        savedHotel = hotelRepository.save(hotel);

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


        AddressDto customerAddress = new AddressDto("Teleborg", "35252", "VAXJO", "SWEDEN");

        CustomerDto customerDto1 = new CustomerDto();
        customerDto1.setFirstName("Test");
        customerDto1.setLastName("Testsson");
        customerDto1.setAge(30);
        customerDto1.setGender(Gender.MALE);
        customerDto1.setCity(customerAddress.getCity());
        customerDto1.setCountry(customerAddress.getCountry());
        customerDto1.setStreet(customerAddress.getStreet());
        customerDto1.setZipCode(customerAddress.getZipCode());
        savedCustomer1 = customerService.saveOrUpdate(customerDto1);


        CustomerDto customerDto2 = new CustomerDto();
        customerDto2.setFirstName("Test2");
        customerDto2.setLastName("Testsson2");
        customerDto2.setAge(33);
        customerDto2.setGender(Gender.FEMALE);
        customerDto2.setCity(customerAddress.getCity());
        customerDto2.setCountry(customerAddress.getCountry());
        customerDto2.setStreet(customerAddress.getStreet());
        customerDto2.setZipCode(customerAddress.getZipCode());
        savedCustomer2 = customerService.saveOrUpdate(customerDto2);


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

        bookingDto1.setCustomer(customerService.findById(savedCustomer1.getId()));
        bookingDto1.setRoom(roomService.findById(savedRoomDto1.getId()));

        actual1 = testObject.createBooking(bookingDto1);
        System.out.println("actual1 = " + actual1);
        Assertions.assertEquals("Test", actual1.getCustomer().getFirstName());
        Assertions.assertEquals(400.0, actual1.getFullPrice());
        Assertions.assertEquals(100.0, actual1.getRoom().getPrice());


        // instantiate booking
        bookingDto2 = new BookingDto();
        bookingDto2.setFromDate(LocalDate.parse("2021-01-01"));
        bookingDto2.setToDate(LocalDate.parse("2021-01-05"));
        bookingDto2.setBreakfast(true);
        bookingDto2.setLunch(true);
        bookingDto2.setPay(true);
        bookingDto2.setNumberOfPersons(3);

        bookingDto2.setCustomer(customerService.findById(savedCustomer2.getId()));
        bookingDto2.setRoom(roomService.findById(savedRoomDto2.getId()));

        actual2 = testObject.createBooking(bookingDto2);
        System.out.println("#################### actual2 = " + actual2);
        Assertions.assertEquals("Test2", actual2.getCustomer().getFirstName());
        Assertions.assertEquals(2400.0, actual2.getFullPrice());
        Assertions.assertEquals(200.0, actual2.getRoom().getPrice());



        // update booking
        BookingDto bookingDto3 = testObject.findById(actual2.getId());
        bookingDto3.setFromDate(LocalDate.parse("2021-01-01"));
        bookingDto3.setToDate(LocalDate.parse("2021-01-07"));
        bookingDto3.setBreakfast(true);
        bookingDto3.setLunch(false);
        bookingDto3.setPay(false);
        bookingDto3.setNumberOfPersons(4);

        bookingDto3.setCustomer(customerService.findById(savedCustomer1.getId()));
        bookingDto3.setRoom(roomService.findById(savedRoomDto2.getId()));

        actual3 = testObject.update(bookingDto3);
        System.out.println("#################### actual2 = " + actual2);
        System.out.println("#################### actual3 = " + actual3);
        Assertions.assertEquals("Test", actual3.getCustomer().getFirstName());
        Assertions.assertEquals(4800.0, actual3.getFullPrice());
        Assertions.assertEquals(200.0, actual3.getRoom().getPrice());


    }


}
