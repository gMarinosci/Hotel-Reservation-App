package se.scandium.hotelproject.repository;

import org.springframework.data.repository.CrudRepository;
import se.scandium.hotelproject.entity.Authority;

public interface AuthorityRepository extends CrudRepository<Authority, Integer> {
    /*no need to implement basic CRUD operations*/

}
