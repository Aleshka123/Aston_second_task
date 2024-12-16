package org.example.Servise;

import org.example.DTO.CustomerDTO;
import org.example.Entity.Customer;
import org.example.Repository.CustomerRepository;
import org.example.mapper.CustomerMapper;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void createCustomer(CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.INSTANCE.fromDTO(customerDTO);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.INSTANCE.fromDTO(customerDTO);
        customerRepository.update(customer);
    }

    @Override
    public void deleteCustomer(int id) {
        customerRepository.delete(id);
    }

    @Override
    public CustomerDTO getCustomerById(int id) {
        return customerRepository.findById(id)
                .map(CustomerMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }
}
