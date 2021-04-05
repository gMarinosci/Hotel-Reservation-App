package se.scandium.hotelproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int numberOfDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;
    private boolean breakfast;
    private boolean lunch;
    private boolean pay;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Room room;

    public void calcPrice() {
        this.amount = room.getPrice() * numberOfDays;
    }

}
