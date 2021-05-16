package se.scandium.hotelproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.scandium.hotelproject.entity.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private Gender gender;

    private String street;
    private String zipCode;
    private String city;
    private String country;
}
