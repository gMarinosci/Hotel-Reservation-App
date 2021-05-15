package se.scandium.hotelproject.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Data
public class BookingDto {

    private int id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private boolean breakfast;
    private boolean lunch;
    private boolean pay;
    private int numberOfPersons;
    private double fullPrice;
    private CustomerDto customer;
    private RoomDto room;
    private boolean status;
    private List<LocalDate> bookingDays;

}
