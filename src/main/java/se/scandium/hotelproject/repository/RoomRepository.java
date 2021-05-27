package se.scandium.hotelproject.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.entity.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends CrudRepository<Room,Integer> {
    Optional<Room> findByNameIgnoreCase(String name);

    @Modifying(clearAutomatically = true)
    @Query("select distinct r from Room r where r.type = :roomType")
    List<Room> findAllByRoomType(@Param("roomType") RoomType roomType);

    @Modifying(clearAutomatically = true)
    @Query("select r.name from Room r")
    List<String> findAllRoomNames();
}
