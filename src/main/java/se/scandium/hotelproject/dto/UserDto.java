package se.scandium.hotelproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private int id;
    private String username;
    private String password;
    private boolean active;
    private List<AuthorityDto> authoritiesList;
    private boolean status;
    private UserInfoDto userInfo;

    public UserDto() {
    }

    public UserDto(int id, String username, String password, boolean active, List<AuthorityDto> authoritiesList, boolean status, UserInfoDto userInfo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.authoritiesList = authoritiesList;
        this.status = status;
        this.userInfo = userInfo;
    }


}
