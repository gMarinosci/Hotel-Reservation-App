package se.scandium.hotelproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import se.scandium.hotelproject.repository.CustomerRepository;

public class CustomerServiceImpl implements CustomerService{

    CustomerRepository customerRepository;

    @Autowired
    public void setCustomerRepository (CustomerRepository customerRepository) { this.customerRepository = customerRepository; }
}
