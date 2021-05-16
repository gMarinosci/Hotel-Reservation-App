package se.scandium.hotelproject.repository;

import org.springframework.data.repository.CrudRepository;
import se.scandium.hotelproject.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>  {
}
