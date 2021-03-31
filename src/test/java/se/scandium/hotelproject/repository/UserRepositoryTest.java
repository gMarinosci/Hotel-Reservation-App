package se.scandium.hotelproject.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.scandium.hotelproject.entity.Authority;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.entity.UserInfo;
import se.scandium.hotelproject.entity.UserType;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository testObject;

    @Autowired
    AuthorityRepository authorityRepository;

    @BeforeEach
    public void setup() {
        UserInfo adminUserInfo = new UserInfo("Mehrdad", "Javan", UserType.ADMINISTRATOR, LocalDateTime.now(), false);
        User adminUser = new User("admin", "admin", adminUserInfo);

        Authority authorityRead = authorityRepository.save(new Authority("FULL_READ"));
        Authority authorityWrite = authorityRepository.save(new Authority("FULL_WRITE"));

        adminUser.addAuthority(authorityRead);
        adminUser.addAuthority(authorityWrite);

        testObject.save(adminUser);
    }

    @Test
    @DisplayName("test_find_by_username_ignore_case_result_true")
    public void test_find_by_username_ignore_case_result_true() {
        Optional<User> adminUser = testObject.findByUsernameIgnoreCase("ADMIN");
        assertTrue(adminUser.isPresent());
        assertNotNull(adminUser.get());
    }

    @Test
    @DisplayName("test_login_true")
    public void test_login_true() {
        Optional<User> adminUser = testObject.findByUsernameIgnoreCaseAndPassword("ADMIN", "admin");
        assertTrue(adminUser.isPresent());
        assertNotNull(adminUser.get());
    }

    @Test
    @DisplayName("test_login_false")
    public void test_login_false() {
        Optional<User> adminUser = testObject.findByUsernameIgnoreCaseAndPassword("ADMIN", "123456");
        assertFalse(adminUser.isPresent());
        assertNull(adminUser.orElse(null));
    }


    @Test
    @DisplayName("test_delete_username_with_change_status")
    public void test_delete_username_with_change_status() {
        Optional<User> optional = testObject.findByUsernameIgnoreCase("ADMIN");
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());

        //User admin = optional.get();
        //admin.setStatus(true);
        //testObject.save(admin);
        testObject.updateStatusByUsername("admin",true);

        Optional<User> adminUser = testObject.findByUsernameIgnoreCase("admin");
        assertTrue(adminUser.isPresent());
        assertNotNull(adminUser.get());

        assertTrue(adminUser.get().isStatus());
    }

}
