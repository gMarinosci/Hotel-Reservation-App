package se.scandium.hotelproject.converter;

import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Room;

import java.util.List;

@Component
public interface RoomConverter {

    RoomDto convertEntityToDto(Room entity);

    List<RoomDto> convertEntityListToDtoList(List<Room> entity);

    Room convertDtoToEntity(RoomDto dto);

    List<Room> convertDtoToEntity(List<RoomDto> dto);

}
