package se.scandium.hotelproject.repository;

import org.springframework.data.repository.CrudRepository;
import se.scandium.hotelproject.entity.Hotel;

public interface HotelRepository extends CrudRepository<Hotel,Integer> {
}
