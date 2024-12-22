package org.example.servise;

import org.example.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    void createCustomer(CustomerDTO customerDTO);

    void updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(int id);

    CustomerDTO getCustomerById(int id);

    List<CustomerDTO> getAllCustomers();
}
