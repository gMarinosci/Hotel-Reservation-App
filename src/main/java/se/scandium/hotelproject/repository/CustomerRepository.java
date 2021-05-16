package se.scandium.hotelproject.repository;

import org.springframework.data.repository.CrudRepository;
import se.scandium.hotelproject.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    Optional<Customer> findByIdNumber(String idNumber);
}
