package se.scandium.hotelproject.service;

import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.exception.UserNotFoundException;

public interface UserService {

    User authentication(String username, String password) throws UserNotFoundException;

    User saveOrUpdate(User user) throws UserNotFoundException;

    boolean resetPassword(String username, String password, String newPassword) throws UserNotFoundException;

}
