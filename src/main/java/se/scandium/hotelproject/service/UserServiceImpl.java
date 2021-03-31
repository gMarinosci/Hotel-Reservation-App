package se.scandium.hotelproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authentication(String username, String password) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCaseAndPassword(username, password);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("Username and Password is not valid.");
        }
    }
}
