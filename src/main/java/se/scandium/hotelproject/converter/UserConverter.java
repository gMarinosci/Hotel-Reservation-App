package se.scandium.hotelproject.converter;

import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.dto.UserDto;
import se.scandium.hotelproject.entity.User;

public interface UserConverter {

    UserView convertUserToUserView(User user);

    UserDto convertUserToDto(User user);

    User convertDtoToUser(UserDto userDto);
}
