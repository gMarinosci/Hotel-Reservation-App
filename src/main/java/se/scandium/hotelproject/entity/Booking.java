package se.scandium.hotelproject.entity;

import lombok.Data;
import se.scandium.hotelproject.exception.ArgumentInvalidException;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private boolean breakfast;
    private boolean lunch;
    private boolean pay;
    private int numberOfPersons;
    private double fullPrice;
    private boolean status; // true = delete , false = data is not deleted

    @Transient
    private List<LocalDate> bookingDays;

    @ManyToOne(fetch = FetchType.EAGER,  cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "room_id")
    private Room room;


    public double calcFullPrice() {
        long noOfDaysBetween;
        if (fromDate.isAfter(toDate) || toDate.isBefore(fromDate) || fromDate.isEqual(toDate))
            throw new ArgumentInvalidException("Dates are not valid");
        else {
            noOfDaysBetween = ChronoUnit.DAYS.between(fromDate, toDate);
        }
        return (room.getPrice() * numberOfPersons) * noOfDaysBetween;
    }


    public List<LocalDate> createBookingDays() {
        long noOfDaysBetween = ChronoUnit.DAYS.between(fromDate, toDate);
        List<LocalDate> dates =  Stream.iterate(fromDate, date -> date.plusDays(1))
                .limit(noOfDaysBetween)
                .collect(Collectors.toList());
        return dates;
    }

}
