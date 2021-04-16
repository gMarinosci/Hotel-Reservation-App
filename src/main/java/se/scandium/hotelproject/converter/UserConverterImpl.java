package se.scandium.hotelproject.converter;

import org.springframework.stereotype.Component;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.entity.Authority;
import se.scandium.hotelproject.entity.User;

import java.util.stream.Collectors;

@Component
public class UserConverterImpl implements UserConverter {
    @Override
    public UserView convertUserToUserView(User user) {
        UserView userView = null;
        if (user != null) {
            userView = new UserView();
            userView.setUsername(user.getUsername());
            userView.setActive(user.isActive());
            userView.setScreenTitle(user.getUserInfo().getScreenTitle());
            userView.setFirstName(user.getUserInfo().getFirstName());
            userView.setLastName(user.getUserInfo().getLastName());
            userView.setUserType(user.getUserInfo().getUserType().getCode());
            userView.setAuthorities(user.getAuthorities().stream().map(Authority::getId).collect(Collectors.toList()));
        }
        return userView;
    }
}
