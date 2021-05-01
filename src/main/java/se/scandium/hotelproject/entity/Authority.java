package se.scandium.hotelproject.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
@Data
@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 100)
    private String name;

    public Authority() {
    }

    public Authority(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Authority(String name) {
        this.name = name;
    }


}
