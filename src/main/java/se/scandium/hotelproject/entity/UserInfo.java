package se.scandium.hotelproject.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 255)
    private String firstName;
    @Column(nullable = false, length = 255)
    private String lastName;
    @Column(nullable = false, length = 50)
    private UserType userType;
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private LocalDateTime createDate;
    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean status;
    private String screenTitle;

    public UserInfo() {
    }

    public UserInfo(String firstName, String lastName, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }

    public UserInfo(String firstName, String lastName, UserType userType, LocalDateTime createDate, boolean status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.createDate = createDate;
        this.status = status;
    }

    public UserInfo(int id, String firstName, String lastName, UserType userType, LocalDateTime createDate, boolean status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.createDate = createDate;
        this.status = status;
    }

}
