package se.scandium.hotelproject.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private double price;
    private RoomType type;
    private int size;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    private boolean status;
    @Embedded
    private RoomDetails roomDetails;

    //@OneToMany(fetch = FetchType.LAZY,
      //      cascade = CascadeType.ALL,
        //    mappedBy = "room",
       //// )
    //private List<Booking> bookings;



}
