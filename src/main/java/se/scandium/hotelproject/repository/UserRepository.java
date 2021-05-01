package se.scandium.hotelproject.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import se.scandium.hotelproject.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    /*no need to implement basic CRUD operations*/
    @Modifying(clearAutomatically = true)
    @Query("UPDATE  User u SET u.status = :status WHERE u.username = :username")
    void updateStatusByUsername(@Param("username") String username, @Param("status") boolean status);

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByUsernameIgnoreCaseAndPassword(String username, String password);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE  User u SET u.password = :newPassword WHERE u.username = :username")
    void resetPassword(@Param("username") String username, @Param("newPassword") String newPassword);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE  User u SET u.active = :active WHERE u.username = :username")
    void updateActiveByUsername(@Param("username") String username, @Param("active") boolean active);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserInfo ui SET ui.screenTitle = :screenTitle WHERE ui.id = (SELECT u.id from User u where u.username = :username) ")
    void updateScreenTitleByUsername(@Param("username") String username, @Param("screenTitle") String screenTitle);

}
