import org.example.DTO.CustomerDTO;
import org.example.Entity.Customer;
import org.example.Repository.CustomerRepository;
import org.example.Servise.CustomerService;
import org.example.Servise.CustomerServiceImpl;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CustomerServiceTest {

    @Test
    void testCreateCustomer() {
        CustomerRepository mockRepo = mock(CustomerRepository.class);
        CustomerService customerService = new CustomerServiceImpl(mockRepo);

        CustomerDTO dto = new CustomerDTO(0, "Иван Иванов", "ivan@example.com", null);
        customerService.createCustomer(dto);
        verify(mockRepo, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById() {
        CustomerRepository mockRepo = mock(CustomerRepository.class);
        CustomerService customerService = new CustomerServiceImpl(mockRepo);

        Customer c = new Customer(1, "Test Name", "test@example.com");
        when(mockRepo.findById(1)).thenReturn(Optional.of(c));

        CustomerDTO result = customerService.getCustomerById(1);
        assertEquals("Test Name", result.getName());
        verify(mockRepo, times(1)).findById(1);
    }

    @Test
    void testGetAllCustomers() {
        CustomerRepository mockRepo = mock(CustomerRepository.class);
        when(mockRepo.findAll()).thenReturn(List.of(
                new Customer(1, "Name1", "email1@example.com"),
                new Customer(2, "Name2", "email2@example.com")
        ));

        CustomerService customerService = new CustomerServiceImpl(mockRepo);
        var all = customerService.getAllCustomers();
        assertEquals(2, all.size());
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void testUpdateCustomer() {
        CustomerRepository mockRepo = mock(CustomerRepository.class);
        CustomerService customerService = new CustomerServiceImpl(mockRepo);

        CustomerDTO dto = new CustomerDTO(1, "Updated Name", "updated@example.com", null);
        customerService.updateCustomer(dto);
        verify(mockRepo, times(1)).update(any(Customer.class));
    }

    @Test
    void testGetCustomerByIdNotFound() {
        CustomerRepository mockRepo = mock(CustomerRepository.class);
        CustomerService customerService = new CustomerServiceImpl(mockRepo);

        when(mockRepo.findById(999)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.getCustomerById(999));
    }


    @Test
    void testDeleteCustomer() {
        CustomerRepository mockRepo = mock(CustomerRepository.class);
        CustomerService customerService = new CustomerServiceImpl(mockRepo);

        customerService.deleteCustomer(10);
        verify(mockRepo, times(1)).delete(10);
    }
}
