package se.scandium.hotelproject.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.scandium.hotelproject.entity.RoomType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private int id;
    private String name;
    private double price;
    private RoomType type;
    private int size;
    private HotelDto hotelDto;
    private boolean status;

    private String location;
    private boolean beds;
    private int numberOfBeds;
    private String description;
}
