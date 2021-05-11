package se.scandium.hotelproject.service;

import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;

import java.util.List;

public interface CustomerService {

    CustomerDto saveOrUpdate(CustomerDto customerDto);

    List<CustomerDto> getAll();
}