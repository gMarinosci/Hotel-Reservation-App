package se.scandium.hotelproject.converter;

import se.scandium.hotelproject.dto.AddressDto;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.entity.Address;
import se.scandium.hotelproject.entity.Customer;

public interface AddressConverter {

    AddressDto convertEntityToDto(Address address);

    Address convertDtoToEntity (AddressDto addressDto);

}
