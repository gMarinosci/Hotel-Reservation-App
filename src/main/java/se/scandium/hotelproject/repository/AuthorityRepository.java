package se.scandium.hotelproject.repository;

import org.springframework.data.repository.CrudRepository;
import se.scandium.hotelproject.entity.Authority;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<Authority, Integer> {
    /*no need to implement basic CRUD operations*/

    Optional<Authority> findByNameIgnoreCase(String name);
}
