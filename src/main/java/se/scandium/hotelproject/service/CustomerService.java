package se.scandium.hotelproject.service;

import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.exception.RecordNotFoundException;

import java.util.List;

public interface CustomerService {

    CustomerDto saveOrUpdate(CustomerDto customerDto) throws RecordNotFoundException;

    List<CustomerDto> getAll();

    CustomerDto findById(int customerId) throws RecordNotFoundException;

    void deleteById(int customerId) throws RecordNotFoundException;
}