package se.scandium.hotelproject.service;

import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.exception.RecordNotFoundException;

import java.util.List;

public interface RoomService {

    RoomDto saveOrUpdate(RoomDto dto) throws RecordNotFoundException;
    List<RoomDto> getAll();
    RoomDto findById(int roomId) throws RecordNotFoundException;

    void deleteById(int roomId) throws RecordNotFoundException;

}
