package se.scandium.hotelproject.repository;

import org.springframework.data.repository.CrudRepository;
import se.scandium.hotelproject.entity.Room;

import java.util.Optional;

public interface RoomRepository extends CrudRepository<Room,Integer> {
    Optional<Room> findByNameIgnoreCase(String name);


}
