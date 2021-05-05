package se.scandium.hotelproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.scandium.hotelproject.converter.CustomerConverter;
import se.scandium.hotelproject.dto.CustomerDto;
import se.scandium.hotelproject.entity.Customer;
import se.scandium.hotelproject.exception.ArgumentInvalidException;
import se.scandium.hotelproject.repository.CustomerRepository;
@Service
public class CustomerServiceImpl implements CustomerService{

    CustomerRepository customerRepository;
    CustomerConverter customerConverter;

    @Autowired
    public void setCustomerRepository (CustomerRepository customerRepository) { this.customerRepository = customerRepository; }

    @Autowired
    public void setCustomerConverter (CustomerConverter customerConverter) { this.customerConverter = customerConverter; }

    @Override
    public CustomerDto saveOrUpdate(CustomerDto customerDto) {

        if (customerDto == null) throw new ArgumentInvalidException("CustomerDto should not be null");
        Customer customerEntity = customerConverter.convertDtoToEntity(customerDto);
        Customer resultEntity = customerRepository.save(customerEntity);
        return customerConverter.convertEntityToDto(resultEntity);
    }
}
