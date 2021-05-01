package se.scandium.hotelproject.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
//@Table(name = "_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false,updatable = false, unique = true, length = 100)
    private String username;
    @Column(nullable = false,updatable = false, length = 100)
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

    public User(int id, String username, String password, boolean active, boolean status, UserInfo userInfo) {
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

}
