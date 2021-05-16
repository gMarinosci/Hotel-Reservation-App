package se.scandium.hotelproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.converter.CustomerConverter;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Customer;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.exception.ArgumentInvalidException;
import se.scandium.hotelproject.exception.RecordNotFoundException;
import se.scandium.hotelproject.exception.UserNotFoundException;
import se.scandium.hotelproject.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    CustomerRepository customerRepository;
    CustomerConverter customerConverter;

    @Autowired
    public void setCustomerRepository (CustomerRepository customerRepository) { this.customerRepository = customerRepository; }

    @Autowired
    public void setCustomerConverter (CustomerConverter customerConverter) { this.customerConverter = customerConverter; }

    @Override
    public CustomerDto saveOrUpdate(CustomerDto customerDto) throws RecordNotFoundException {
        if (customerDto == null) throw new ArgumentInvalidException("UserDto should not be null");
        if (customerDto.getId() != 0) {
            customerRepository.findById(customerDto.getId()).orElseThrow(() -> new RecordNotFoundException("UserDto Id is not valid"));
        }

        Customer customerEntity = customerConverter.convertDtoToEntity(customerDto);
        Customer resultEntity = customerRepository.save(customerEntity);
        return customerConverter.convertEntityToDto(resultEntity);
    }

    @Override
    public List<CustomerDto> getAll() {
        List<Customer> customerList = new ArrayList<>();
        customerRepository.findAll().iterator().forEachRemaining(customerList::add);
        return customerConverter.convertEntityListToDtoList(customerList);
    }

    @Override
    public CustomerDto findById(int customerId) throws RecordNotFoundException {
        if (customerId == 0) throw new ArgumentInvalidException("customer id should not be null or zero");
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent())
            return customerConverter.convertEntityToDto(optionalCustomer.get());
        else throw new RecordNotFoundException("record not found");
    }

    @Override
    public void deleteById(int customerId) throws RecordNotFoundException {
        if (customerId == 0) throw new ArgumentInvalidException("id should not be null or zero");
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent())
            customerRepository.deleteById(customerId);
        else throw new RecordNotFoundException("record not found");
    }
}
