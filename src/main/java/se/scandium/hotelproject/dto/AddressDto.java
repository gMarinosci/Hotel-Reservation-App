package se.scandium.hotelproject.dto;


import lombok.*;

import javax.persistence.Embeddable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private String street;
    private String zipCode;
    private String city;
    private String country;

}



