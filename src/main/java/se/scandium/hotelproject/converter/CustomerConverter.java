package se.scandium.hotelproject.converter;

import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Customer;
import se.scandium.hotelproject.entity.Room;

import java.util.List;

public interface CustomerConverter {

    CustomerDto convertEntityToDto (Customer customer);

    Customer convertDtoToEntity (CustomerDto dto);

    List<CustomerDto> convertEntityListToDtoList(List<Customer> entity);
}
