package se.scandium.hotelproject.converter;

import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.entity.Customer;

public interface CustomerConverter {

    CustomerDto convertEntityToDto (Customer entity);

    Customer convertDtoToEntity (CustomerDto dto);
}
