package se.scandium.hotelproject.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.scandium.hotelproject.controller.fxml.view.UserView;
import se.scandium.hotelproject.dto.AuthorityDto;
import se.scandium.hotelproject.dto.UserDto;
import se.scandium.hotelproject.dto.UserInfoDto;
import se.scandium.hotelproject.entity.UserType;
import se.scandium.hotelproject.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService testObject;

    UserDto userDto;

    @BeforeEach
    public void setup() throws UserNotFoundException {
        userDto = new UserDto();
        userDto.setUsername("test1");
        List<AuthorityDto> authorityDtoListForTest1 = new ArrayList<>();
        AuthorityDto authorityDtoTest1 = new AuthorityDto();
        authorityDtoTest1.setId(1);
        authorityDtoTest1.setName("user_management");

        authorityDtoListForTest1.add(authorityDtoTest1);
        userDto.setAuthoritiesList(authorityDtoListForTest1);

        UserInfoDto userTest1InfoDto = new UserInfoDto();
        userTest1InfoDto.setFirstName("test");
        userTest1InfoDto.setLastName("testsson");
        userTest1InfoDto.setUserType(UserType.ADMINISTRATOR);
        userTest1InfoDto.setScreenTitle("SCREEN TITLE TEST 1");
        userDto.setUserInfo(userTest1InfoDto);

        UserDto actual = testObject.saveOrUpdate(userDto);
        userDto = actual;
        assertEquals("test1", actual.getUsername());
        assertEquals("test", actual.getUserInfo().getFirstName());
        assertEquals("testsson", actual.getUserInfo().getLastName());

    }


    @Test
    public void test_login_success() throws UserNotFoundException {
        UserView actual = testObject.authentication(userDto.getUsername(), userDto.getPassword());

        assertNotNull(actual);
        assertEquals("test", actual.getFirstName());
        assertEquals("testsson", actual.getLastName());
    }


    @Test
    public void test_update_user() throws UserNotFoundException {
        userDto.getUserInfo().setFirstName("TEST");
        userDto.getUserInfo().setLastName("TESTSSON");
        userDto.getUserInfo().setScreenTitle("TEST TEST");

        UserDto actual = testObject.saveOrUpdate(userDto);
        System.out.println("actual =>>>>>>>>>>>>>>>>>>>>>>> " + actual);
        assertNotNull(actual);
        assertEquals("TEST", actual.getUserInfo().getFirstName());
        assertEquals("TESTSSON", actual.getUserInfo().getLastName());

    }

}
