package se.scandium.hotelproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.converter.RoomConverter;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.exception.ArgumentInvalidException;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    RoomRepository roomRepository;
    RoomConverter roomConverter;

    @Autowired
    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Autowired
    public void setRoomConverter(RoomConverter roomConverter) {
        this.roomConverter = roomConverter;
    }

    @Override
    public RoomDto saveOrUpdate(RoomDto dto) throws RecordNotFoundException {
        System.out.println("dto = " + dto);
        if (dto == null) throw new ArgumentInvalidException("room object should not be null");
        if (dto.getId() != 0) { // call update
            // check id exist
            System.out.println("dto.getId() = " + dto.getId());
            Optional<Room> optionalRoom = roomRepository.findById(dto.getId());
            if (optionalRoom.isEmpty()) throw new RecordNotFoundException("record not found");
        }
        Room roomEntity = roomConverter.convertDtoToEntity(dto);
        Room resultEntity = roomRepository.save(roomEntity);
        return roomConverter.convertEntityToDto(resultEntity);
    }

    @Override
    public List<RoomDto> getAll() {
        List<Room> roomList = new ArrayList<>();
        roomRepository.findAll().iterator().forEachRemaining(roomList::add);
        return roomConverter.convertEntityListToDtoList(roomList);
    }

    @Override
    public RoomDto findById(int roomId) throws RecordNotFoundException {
        if (roomId == 0) throw new ArgumentInvalidException("room id should not be null or zero");
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent())
            return roomConverter.convertEntityToDto(optionalRoom.get());
        else throw new RecordNotFoundException("record not found");
    }

    @Override
    public void deleteById(int roomId) throws RecordNotFoundException {
        if (roomId == 0) throw new ArgumentInvalidException("room id should not be null or zero");
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent())
            roomRepository.deleteById(roomId);
        else throw new RecordNotFoundException("record not found");
    }

    @Override
    public List<String> getAllRoomNames() {
        return roomRepository.findAllRoomNames();
    }
}
