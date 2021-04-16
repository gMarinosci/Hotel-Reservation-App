package se.scandium.hotelproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.converter.UserConverter;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.exception.ArgumentInvalidException;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserConverter userConverter;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public UserView authentication(String username, String password) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCaseAndPassword(username, password);
        if (userOptional.isPresent()) {
            return userConverter.convertUserToUserView(userOptional.get());
        } else {
            throw new UserNotFoundException("Username and Password is not valid.");
        }
    }

    @Transactional
    @Override
    public UserView saveOrUpdate(UserView user) throws UserNotFoundException {
        return null;
    }

    @Transactional
    @Override
    public boolean resetPasswordUpdateScreen(String username, String password, String newPassword,String screenTitle) throws UserNotFoundException {
        if (resetPassword(username,password,newPassword)){
            userRepository.updateScreenTitleByUsername(username, screenTitle);
            userRepository.updateActiveByUsername(username, true);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean resetPassword(String username, String password, String newPassword) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        if (userOptional.isEmpty()) throw new UserNotFoundException("User not found");
        if (userOptional.get().getPassword().equals(newPassword))
            throw new ArgumentInvalidException("new password and old password must nut be same");

        userRepository.resetPassword(username, newPassword);
        return true;
    }


}
