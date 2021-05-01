package se.scandium.hotelproject.controller.fxml.view;

import lombok.Data;

import java.util.List;

@Data
public class UserView {

    private String username;
    private boolean active;
    private List<Integer> authorities;
    private String firstName;
    private String lastName;
    private int userType;
    private String screenTitle;


}
