package se.scandium.hotelproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    //@Column(updatable = false,nullable = false, unique = true)
    private String idNumber;
    private int age;
    private Gender gender;
    @Embedded
    private Address address;

}
