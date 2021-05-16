package se.scandium.hotelproject.converter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.dto.AddressDto;
import se.scandium.hotelproject.entity.Address;
@Component
public class AddressConverterImpl implements  AddressConverter{

    @Override
    public AddressDto convertEntityToDto(Address address) {
        AddressDto addressDto = null;
        if (address != null) {
            addressDto = new AddressDto();
            addressDto.setCity(address.getCity());
            addressDto.setCountry(address.getCountry());
            addressDto.setStreet(address.getStreet());
            addressDto.setZipCode(address.getZipCode());
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
            address.setZipCode(addressDto.getZipCode());
        }
        return address;
    }
}
