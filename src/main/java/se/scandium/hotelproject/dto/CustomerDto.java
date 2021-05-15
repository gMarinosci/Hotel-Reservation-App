package se.scandium.hotelproject.dto;

import lombok.Data;
import se.scandium.hotelproject.entity.Gender;

@Data
public class CustomerDto {

    private int id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private int age;
    private Gender gender;
    private AddressDto address;

}
