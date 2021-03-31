package se.scandium.hotelproject.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User testObject;
    LocalDateTime regDate = LocalDateTime.now();

    @BeforeEach
    public void setup() {
        UserInfo adminUserInfo = new UserInfo(1, "Mehrdad", "Javan", UserType.ADMINISTRATOR, regDate, false);
        testObject = new User(1, "admin", "admin", false, false, adminUserInfo);
        Authority authorityRead = new Authority(1, "FULL_READ");
        Authority authorityWrite = new Authority(1, "FULL_WRITE");
        testObject.addAuthority(authorityRead);
        testObject.addAuthority(authorityWrite);
    }

    @Test
    @DisplayName("test_successfully_created")
    void test_successfully_created() {
        assertEquals(1, testObject.getId());
        assertEquals("admin", testObject.getUsername());
        assertEquals("Mehrdad", testObject.getUserInfo().getFirstName());
        assertEquals("Javan", testObject.getUserInfo().getLastName());
        assertEquals(UserType.ADMINISTRATOR, testObject.getUserInfo().getUserType());
    }

    @Test
    @DisplayName("test_equals")
    void test_equals() {
        UserInfo adminUserInfo = new UserInfo(1, "Mehrdad", "Javan", UserType.ADMINISTRATOR, regDate, false);
        User expected = new User(1, "admin", "admin", false, false, adminUserInfo);
        Authority authorityRead = new Authority(1, "FULL_READ");
        Authority authorityWrite = new Authority(1, "FULL_WRITE");
        expected.addAuthority(authorityRead);
        expected.addAuthority(authorityWrite);

        assertEquals(expected, testObject);
    }

    @Test
    @DisplayName("test_hashCode")
    void test_hashCode() {
        UserInfo adminUserInfo = new UserInfo(1, "Mehrdad", "Javan", UserType.ADMINISTRATOR, regDate, false);
        User expected = new User(1, "admin", "admin", false, false, adminUserInfo);
        Authority authorityRead = new Authority(1, "FULL_READ");
        Authority authorityWrite = new Authority(1, "FULL_WRITE");
        expected.addAuthority(authorityRead);
        expected.addAuthority(authorityWrite);

        assertEquals(expected.hashCode(), testObject.hashCode());
    }

}
