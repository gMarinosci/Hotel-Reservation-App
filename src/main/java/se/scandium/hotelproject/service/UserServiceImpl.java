package se.scandium.hotelproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.exception.ArgumentInvalidException;
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

    @Override
    public User saveOrUpdate(User user) throws UserNotFoundException {
        // When the user ID is present, the update operation is executed, otherwise the save is executed

        if (user == null) throw new ArgumentInvalidException("User should not be null");
        if (user.getId() != 0) {
            Optional<User> optionalUser = userRepository.findById(user.getId());
            if (optionalUser.isEmpty()) throw new UserNotFoundException("User not found");
        }
        if (user.getUsername() == null) throw new ArgumentInvalidException("User not found");

        return userRepository.save(user);
    }

}
