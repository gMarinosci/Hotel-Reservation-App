package se.scandium.hotelproject.config.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.scandium.hotelproject.entity.Authority;
import se.scandium.hotelproject.entity.User;
import se.scandium.hotelproject.entity.UserInfo;
import se.scandium.hotelproject.entity.UserType;
import se.scandium.hotelproject.repository.AuthorityRepository;
import se.scandium.hotelproject.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class InitData implements CommandLineRunner {

    UserRepository userRepository;
    AuthorityRepository authorityRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... args) {
        saveUsers();
    }

    @Transactional
    void saveUsers() {
        Authority userAuthority = null;
        Authority hotelAuthority = null;
        if (authorityRepository.findByNameIgnoreCase("user_management").isEmpty())
            userAuthority = authorityRepository.save(new Authority("user_management"));
        if (authorityRepository.findByNameIgnoreCase("hotel_management").isEmpty())
            hotelAuthority = authorityRepository.save(new Authority("hotel_management"));

        if (userRepository.findByUsernameIgnoreCase("superAdmin").isEmpty()) {
            UserInfo superAdminUserInfo = new UserInfo("SuperAdmin", "SuperAdminsson", UserType.ADMINISTRATOR, LocalDateTime.now(), false);
            User superAdminUser = new User("superAdmin", "superAdmin", superAdminUserInfo);
            superAdminUser.addAuthority(userAuthority);
            superAdminUser.addAuthority(hotelAuthority);
            userRepository.save(superAdminUser);
        }

        if (userRepository.findByUsernameIgnoreCase("admin").isEmpty()) {
            UserInfo adminUserInfo = new UserInfo("Admin", "Adminsson", UserType.ADMINISTRATOR, LocalDateTime.now(), false);
            adminUserInfo.setScreenTitle("ADMIN SYSTEM");
            User adminUser = new User("admin", "admin", adminUserInfo);
            adminUser.setActive(true);
            adminUser.addAuthority(userAuthority);
            userRepository.save(adminUser);
        }
        if (userRepository.findByUsernameIgnoreCase("user").isEmpty()) {
            UserInfo receptionUserInfo = new UserInfo("Test", "Testsson", UserType.RECEPTION, LocalDateTime.now(), false);
            User receptionUser = new User("user", "user", receptionUserInfo);
            receptionUser.addAuthority(hotelAuthority);
            userRepository.save(receptionUser);
        }

    }
}
