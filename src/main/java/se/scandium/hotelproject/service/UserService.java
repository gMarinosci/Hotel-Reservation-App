package se.scandium.hotelproject.service;

import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.exception.UserNotFoundException;

public interface UserService {

    UserView authentication(String username, String password) throws UserNotFoundException;

    UserView saveOrUpdate(UserView user) throws UserNotFoundException;

    boolean resetPassword(String username, String password, String newPassword) throws UserNotFoundException;

    boolean resetPasswordUpdateScreen(String username, String password, String newPassword, String screenTitle) throws UserNotFoundException;

}
