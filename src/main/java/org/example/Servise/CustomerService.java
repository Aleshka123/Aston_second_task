package org.example.Servise;

import org.example.DTO.CustomerDTO;
import java.util.List;

public interface CustomerService {
    void createCustomer(CustomerDTO customerDTO);

    void updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(int id);

    CustomerDTO getCustomerById(int id);

    List<CustomerDTO> getAllCustomers();
}
