package se.scandium.hotelproject.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return id == userInfo.id && status == userInfo.status && Objects.equals(firstName, userInfo.firstName) && Objects.equals(lastName, userInfo.lastName) && userType == userInfo.userType && Objects.equals(createDate, userInfo.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, userType, createDate, status);
    }
}
