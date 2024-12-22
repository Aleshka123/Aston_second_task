package org.example.repository;

import org.example.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    void save(Customer customer);
    void update(Customer customer);
    void delete(int id);
    Optional<Customer> findById(int id);
    List<Customer> findAll();
}
