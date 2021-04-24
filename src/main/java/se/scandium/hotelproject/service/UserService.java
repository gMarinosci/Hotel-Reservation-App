package se.scandium.hotelproject.service;

import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.dto.UserDto;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.exception.UserNotFoundException;

public interface UserService {

    UserView authentication(String username, String password) throws UserNotFoundException;

    UserDto saveOrUpdate(UserDto userDto) throws UserNotFoundException;

    boolean resetPassword(String username, String password, String newPassword) throws UserNotFoundException;

    boolean resetPasswordUpdateScreen(String username, String password, String newPassword, String screenTitle) throws UserNotFoundException;

}
