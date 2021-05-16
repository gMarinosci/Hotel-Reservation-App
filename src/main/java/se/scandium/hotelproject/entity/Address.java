package se.scandium.hotelproject.entity;


import lombok.*;

import javax.persistence.Embeddable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    private String street;
    private String zipCode;
    private String city;
    private String country;

}



