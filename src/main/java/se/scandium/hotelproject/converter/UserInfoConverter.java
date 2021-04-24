package se.scandium.hotelproject.converter;

import se.scandium.hotelproject.dto.UserInfoDto;
import se.scandium.hotelproject.entity.UserInfo;

public interface UserInfoConverter {

    UserInfoDto convertUserInfoToDto(UserInfo userInfo);

    UserInfo convertDtoToUserInfo(UserInfoDto userInfoDto);
}
