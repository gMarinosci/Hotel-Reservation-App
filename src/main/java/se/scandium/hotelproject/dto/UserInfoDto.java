package se.scandium.hotelproject.dto;

import lombok.Data;
import se.scandium.hotelproject.entity.UserType;

import java.time.LocalDateTime;

@Data
public class UserInfoDto {
    private int id;
    private String firstName;
    private String lastName;
    private UserType userType;
    private LocalDateTime createDate;
    private boolean status;
    private String screenTitle;

}
