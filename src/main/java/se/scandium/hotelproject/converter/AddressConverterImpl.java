package se.scandium.hotelproject.converter;

import se.scandium.hotelproject.dto.AddressDto;
import se.scandium.hotelproject.entity.Address;

public class AddressConverterImpl implements  AddressConverter{

    @Override
    public AddressDto convertEntityToDto(Address address) {
        AddressDto addressDto = null;
        if (address != null) {
            addressDto = new AddressDto();
            addressDto.setCity(address.getCity());
            addressDto.setCountry(addressDto.getCountry());
            addressDto.setStreet(addressDto.getStreet());
            addressDto.setZipCode(addressDto.getZipCode());
        }
        return addressDto;
    }

    @Override
    public Address convertDtoToEntity(AddressDto addressDto) {
        Address address = null;
        if (addressDto != null) {
            address = new Address();
            address.setCity(addressDto.getCity());
            address.setCountry(addressDto.getCity());
            address.setStreet(addressDto.getStreet());
            address.setZipCode(address.getZipCode());
        }
        return address;
    }
}
