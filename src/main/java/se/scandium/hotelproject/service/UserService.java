package se.scandium.hotelproject.service;

import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.exception.UserNotFoundException;

public interface UserService {

    User authentication(String username, String password) throws UserNotFoundException;
}
