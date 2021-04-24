package se.scandium.hotelproject.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.dto.AuthorityDto;
import se.scandium.hotelproject.dto.UserDto;
import se.scandium.hotelproject.dto.UserInfoDto;
import se.scandium.hotelproject.entity.Authority;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.entity.UserInfo;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverterImpl implements UserConverter {

    UserInfoConverter userInfoConverter;
    AuthorityConverter authorityConverter;

    @Autowired
    public UserConverterImpl(UserInfoConverter userInfoConverter, AuthorityConverter authorityConverter) {
        this.userInfoConverter = userInfoConverter;
        this.authorityConverter = authorityConverter;
    }

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

    @Override
    public UserDto convertUserToDto(User user) {
        UserDto userDto = null;
        if (user != null) {
            userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setPassword(user.getPassword());
            userDto.setActive(user.isActive());
            userDto.setStatus(user.isStatus());

            UserInfo userInfo = user.getUserInfo();
            if (userInfo != null) {
                UserInfoDto userInfoDto = userInfoConverter.convertUserInfoToDto(userInfo);
                userDto.setUserInfoDto(userInfoDto);
            }
            List<Authority> authorities = user.getAuthorities();
            if (authorities.size() != 0) {
                List<AuthorityDto> authorityDtoList = authorities.stream().map(authority -> authorityConverter.convertAuthorityToDto(authority)).collect(Collectors.toList());
                userDto.setAuthoritiesDtoList(authorityDtoList);
            }
        }
        return userDto;
    }

    @Override
    public User convertDtoToUser(UserDto userDto) {
        User user = null;
        if (userDto != null) {
            user = new User();
            user.setId(userDto.getId());
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setActive(userDto.isActive());
            user.setStatus(userDto.isStatus());

            UserInfoDto userInfoDto = userDto.getUserInfoDto();
            if (userInfoDto != null) {
                UserInfo userInfo = userInfoConverter.convertDtoToUserInfo(userInfoDto);
                user.setUserInfo(userInfo);
            }
            List<AuthorityDto> authorityDtoList = userDto.getAuthoritiesDtoList();
            if (authorityDtoList.size() != 0) {
                List<Authority> authorityList = authorityDtoList.stream().map(authorityDto -> authorityConverter.convertDtoToAuthority(authorityDto)).collect(Collectors.toList());
                user.setAuthorities(authorityList);
            }
        }
        return user;
    }


}
