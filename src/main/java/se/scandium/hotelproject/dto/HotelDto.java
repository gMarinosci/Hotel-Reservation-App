package se.scandium.hotelproject.dto;

import lombok.*;
import se.scandium.hotelproject.entity.Address;
import se.scandium.hotelproject.entity.Room;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {

    private int id;
    private String name;
    private int star;
    private AddressDto address;
    private List<RoomDto> rooms;

    public HotelDto(int id, String name, int star, AddressDto address) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.address = address;
    }
}
