package se.scandium.hotelproject.config.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.scandium.hotelproject.controller.fxml.singleton.HotelHolder;
import se.scandium.hotelproject.dto.AddressDto;
import se.scandium.hotelproject.dto.HotelDto;
import se.scandium.hotelproject.entity.*;
import se.scandium.hotelproject.repository.AuthorityRepository;
import se.scandium.hotelproject.repository.HotelRepository;
import se.scandium.hotelproject.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class InitData implements CommandLineRunner {

    UserRepository userRepository;
    AuthorityRepository authorityRepository;
    HotelRepository hotelRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setHotelRepository(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void run(String... args) {
        saveUsers();
        hotelInformation();
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

    @Transactional
    void hotelInformation() {
        Hotel hotel = hotelRepository.save(new Hotel("Test Hotel",5,new Address("VAXJO Street","35252","VAXJO","SWEDEN")));
        HotelHolder.getInstance().setHotelDto(new HotelDto(hotel.getId(),hotel.getName(),hotel.getStar(),new AddressDto(hotel.getAddress().getStreet(),hotel.getAddress().getZipCode(),hotel.getAddress().getCity(),hotel.getAddress().getCountry())));
    }
}
