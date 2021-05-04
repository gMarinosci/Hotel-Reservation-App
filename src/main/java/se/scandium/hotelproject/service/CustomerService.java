package se.scandium.hotelproject.service;

import se.scandium.hotelproject.dto.CustomerDto;

public interface CustomerService {

    CustomerDto saveOrUpdate(CustomerDto customerDto);
}