package se.scandium.hotelproject.entity;

import java.util.Set;

public class Hotel {
    private Long id;
    private String name;
    private Address address;
    private int stars;
    private String email;
    private Set<Room> rooms;

}
