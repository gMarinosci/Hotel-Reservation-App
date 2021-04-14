package se.scandium.hotelproject.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
//@Table(name = "_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true, length = 100)
    private String username;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean active;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "users_authotities"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<Authority> authorities;
    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean status;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserInfo userInfo;

    public User() {
    }

    public User(int id, String username, String password, UserInfo userInfo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userInfo = userInfo;
    }

    public User(String username, String password, UserInfo userInfo) {
        this.username = username;
        this.password = password;
        this.userInfo = userInfo;
    }

    public User(int id, String username, String password, boolean active, List<Authority> authorities, boolean status, UserInfo userInfo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
        this.status = status;
        this.userInfo = userInfo;
    }

    public User(String username, String password, boolean active, List<Authority> authorities, boolean status, UserInfo userInfo) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
        this.status = status;
        this.userInfo = userInfo;
    }
    public User(int id,String username, String password, boolean active, boolean status, UserInfo userInfo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.status = status;
        this.userInfo = userInfo;
    }
    public User(String username, String password, boolean active, boolean status, UserInfo userInfo) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.status = status;
        this.userInfo = userInfo;
    }

    public void addAuthority(Authority authority) {
        if (authorities == null) authorities = new ArrayList<>();
        if (authority != null) authorities.add(authority);
    }

    public void removeAuthority(Authority authority) {
        if (authorities == null) authorities = new ArrayList<>();
        if (authority != null) authorities.remove(authority);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && active == user.active && status == user.status && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(authorities, user.authorities) && Objects.equals(userInfo, user.userInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, active, authorities, status, userInfo);
    }
}
