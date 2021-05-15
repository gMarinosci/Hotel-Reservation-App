package se.scandium.hotelproject.converter;

import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.AddressDto;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.entity.Address;
import se.scandium.hotelproject.entity.Customer;

@Component
public class CustomerConverterImpl implements CustomerConverter {

    @Override
    public CustomerDto convertCustomerToDto(Customer customer) {
        CustomerDto dto = null;
        if (customer != null) {
            dto = new CustomerDto();
            dto.setId(customer.getId());
            dto.setFirstName(customer.getFirstName());
            dto.setLastName(customer.getLastName());
            dto.setIdNumber(customer.getIdNumber());
            dto.setAge(customer.getAge());
            dto.setGender(customer.getGender());
            Address address = customer.getAddress();
            if (address != null)
                dto.setAddress(new AddressDto(address.getStreet(), address.getZipCode(), address.getCity(), address.getCountry()));
        }
        return dto;
    }

    @Override
    public Customer convertDtoToCustomer(CustomerDto dto) {
        Customer customer = null;
        if (dto != null) {
            customer = new Customer();
            customer.setId(dto.getId());
            customer.setFirstName(dto.getFirstName());
            customer.setLastName(dto.getLastName());
            customer.setIdNumber(dto.getIdNumber());
            customer.setAge(dto.getAge());
            customer.setGender(dto.getGender());
            AddressDto addressDto = dto.getAddress();
            if (addressDto != null)
                customer.setAddress(new Address(addressDto.getStreet(), addressDto.getZipCode(), addressDto.getCity(), addressDto.getCountry()));
        }
        return customer;
    }
}
