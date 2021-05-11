package se.scandium.hotelproject.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.dto.AddressDto;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Address;
import se.scandium.hotelproject.entity.Customer;
import se.scandium.hotelproject.entity.Room;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerConverterImpl implements CustomerConverter {

    AddressConverter addressConverter;

    @Autowired
    public void setAddressConverter (AddressConverter addressConverter) { this.addressConverter = addressConverter; }

    @Override
    public CustomerDto convertEntityToDto(Customer customer) {
        CustomerDto customerDto = null;
        if (customer != null) {
            customerDto = new CustomerDto();
            customerDto.setId(customer.getId());
            customerDto.setFirstName(customer.getFirstName());
            customerDto.setLastName(customer.getLastName());
            customerDto.setAge(customer.getAge());
            customerDto.setGender(customer.getGender());

            Address address = customer.getAddress();
            if (address != null) {
                AddressDto addressDto = addressConverter.convertEntityToDto(address);
                customerDto.setAddressDto(addressDto);
            }
        }
        return customerDto;
    }

    @Override
    public Customer convertDtoToEntity(CustomerDto customerDto) {
        Customer customer = null;
        if (customerDto != null) {
            customer = new Customer();
            customer.setId(customerDto.getId());
            customer.setFirstName(customerDto.getFirstName());
            customer.setLastName(customerDto.getLastName());
            customer.setAge(customerDto.getAge());
            customer.setGender(customerDto.getGender());
        }

        AddressDto addressDto = customerDto.getAddressDto();
        if (addressDto != null) {
            Address address = addressConverter.convertDtoToEntity(addressDto);
            customer.setAddress(address);
        }
        return customer;
    }

    @Override
    public List<CustomerDto> convertEntityListToDtoList(List<Customer> entityList) {
        List<CustomerDto> customerDtoList = null;
        if (entityList != null) {
            customerDtoList = entityList.stream().map(this::convertEntityToDto).collect(Collectors.toList());
        }
        return customerDtoList;
    }
}
