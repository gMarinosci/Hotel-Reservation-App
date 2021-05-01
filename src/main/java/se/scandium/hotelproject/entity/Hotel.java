package se.scandium.hotelproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int star;
    @Embedded
    private Address address;
    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "hotel",
            orphanRemoval = true
    )    private List<Room> rooms;

    public Hotel(int id, String name, int star, Address address) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.address = address;
    }

    public Hotel(String name, int star, Address address) {
        this.name = name;
        this.star = star;
        this.address = address;
    }
}
