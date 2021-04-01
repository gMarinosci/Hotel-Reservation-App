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
    void saveUsers(){

        Authority authority1 = authorityRepository.save(new Authority("user_management"));
        Authority authority2 = authorityRepository.save(new Authority("hotel_management"));


        UserInfo superAdminUserInfo = new UserInfo("SuperAdmin", "SuperAdminsson", UserType.ADMINISTRATOR, LocalDateTime.now(), false);
        User superAdminUser = new User("superAdmin", "superAdmin", superAdminUserInfo);
        superAdminUser.addAuthority(authority1);
        superAdminUser.addAuthority(authority2);
        userRepository.save(superAdminUser);

        UserInfo adminUserInfo = new UserInfo("Admin", "Adminsson", UserType.ADMINISTRATOR, LocalDateTime.now(), false);
        User adminUser = new User("admin", "admin", adminUserInfo);
        adminUser.addAuthority(authority1);
        userRepository.save(adminUser);

        UserInfo receptionUserInfo = new UserInfo("Test", "Testsson", UserType.RECEPTION, LocalDateTime.now(), false);
        User receptionUser = new User("user", "user", receptionUserInfo);
        receptionUser.addAuthority(authority2);
        userRepository.save(receptionUser);
    }

}
