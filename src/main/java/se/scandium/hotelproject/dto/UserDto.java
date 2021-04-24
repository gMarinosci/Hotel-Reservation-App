package se.scandium.hotelproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private int id;
    private String username;
    private String password;
    private boolean active;
    private List<AuthorityDto> authoritiesDtoList;
    private boolean status;
    private UserInfoDto userInfoDto;

    public UserDto() {
    }

    public UserDto(int id, String username, String password, boolean active, List<AuthorityDto> authoritiesDtoList, boolean status, UserInfoDto userInfoDto) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.authoritiesDtoList = authoritiesDtoList;
        this.status = status;
        this.userInfoDto = userInfoDto;
    }


}
