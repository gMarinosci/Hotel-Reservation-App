package se.scandium.hotelproject.converter;

import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.UserInfoDto;
import se.scandium.hotelproject.entity.UserInfo;

@Component
public class UserInfoConverterImpl implements UserInfoConverter {

    @Override
    public UserInfoDto convertUserInfoToDto(UserInfo userInfo) {
        UserInfoDto userInfoDto = null;
        if (userInfo != null) {
            userInfoDto = new UserInfoDto();
            userInfoDto.setId(userInfo.getId());
            userInfoDto.setFirstName(userInfo.getFirstName());
            userInfoDto.setLastName(userInfo.getLastName());
            userInfoDto.setUserType(userInfo.getUserType());
            userInfoDto.setCreateDate(userInfo.getCreateDate());
            userInfoDto.setScreenTitle(userInfo.getScreenTitle());
            userInfoDto.setStatus(userInfo.isStatus());
        }
        return userInfoDto;
    }

    @Override
    public UserInfo convertDtoToUserInfo(UserInfoDto userInfoDto) {
        UserInfo userInfo= null;
        if (userInfoDto !=null){
            userInfo = new UserInfo();
            userInfo.setId(userInfoDto.getId());
            userInfo.setFirstName(userInfoDto.getFirstName());
            userInfo.setLastName(userInfoDto.getLastName());
            userInfo.setUserType(userInfoDto.getUserType());
            userInfo.setCreateDate(userInfoDto.getCreateDate());
            userInfo.setScreenTitle(userInfoDto.getScreenTitle());
            userInfo.setStatus(userInfoDto.isStatus());
        }
        return userInfo;
    }
}
