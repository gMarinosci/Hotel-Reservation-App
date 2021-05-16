package se.scandium.hotelproject.converter;

import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.AddressDto;
import se.scandium.hotelproject.dto.HotelDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Address;
import se.scandium.hotelproject.entity.Hotel;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.entity.RoomDetails;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomConverterImpl implements RoomConverter {

    @Override
    public RoomDto convertEntityToDto(Room entity) {
        RoomDto dto = null;
        if (entity != null) {

            HotelDto hotelDto = null;
            if (entity.getHotel() != null) {
                AddressDto addressDto = null;
                if (entity.getHotel().getAddress() != null)
                    addressDto = new AddressDto(entity.getHotel().getAddress().getStreet(), entity.getHotel().getAddress().getZipCode(), entity.getHotel().getAddress().getCity(), entity.getHotel().getAddress().getCountry());
                hotelDto = new HotelDto(entity.getHotel().getId(), entity.getHotel().getName(), entity.getHotel().getStar(), addressDto);
            }
            dto = new RoomDto(entity.getId(), entity.getName(), entity.getPrice(), entity.getType(), entity.getSize(), hotelDto, entity.isStatus(), entity.getRoomDetails().getLocation(), entity.getRoomDetails().isBeds(), entity.getRoomDetails().getNumberOfBeds(), entity.getRoomDetails().getDescription());
        }
        return dto;
    }

    @Override
    public List<RoomDto> convertEntityListToDtoList(List<Room> entityList) {
        List<RoomDto> roomDtoList = null;
        if (entityList != null) {
            roomDtoList = entityList.stream().map(this::convertEntityToDto).collect(Collectors.toList());
        }
        return roomDtoList;
    }

    @Override
    public Room convertDtoToEntity(RoomDto dto) {
        Room entity = null;
        if (dto != null) {
            Hotel hotel = null;
            if (dto.getHotelDto() != null) {
                Address address = null;
                if (dto.getHotelDto().getAddressDto() != null)
                    address = new Address(dto.getHotelDto().getAddressDto().getStreet(), dto.getHotelDto().getAddressDto().getZipCode(), dto.getHotelDto().getAddressDto().getCity(), dto.getHotelDto().getAddressDto().getCountry());
                hotel = new Hotel(dto.getHotelDto().getId(), dto.getHotelDto().getName(), dto.getHotelDto().getStar(), address);
            }
            RoomDetails roomDetails = new RoomDetails(dto.getLocation(), dto.isBeds(), dto.getNumberOfBeds(), dto.getDescription());
            entity = new Room(dto.getId(), dto.getName(), dto.getPrice(), dto.getType(), dto.getSize(), hotel, dto.isStatus(), roomDetails);
        }
        return entity;
    }

    @Override
    public List<Room> convertDtoToEntity(List<RoomDto> dtoList) {
        List<Room> roomList = null;
        if (dtoList != null) {
            roomList = dtoList.stream().map(this::convertDtoToEntity).collect(Collectors.toList());
        }
        return roomList;
    }
}
