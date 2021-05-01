package se.scandium.hotelproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RoomDetails {
    private String location;
    private boolean beds;
    private int numberOfBeds;
    private String description;
}
