package org.example.repository;

import org.example.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product);

    void update(Product product);

    void delete(int id);

    Optional<Product> findById(int id);

    List<Product> findAll();
}
