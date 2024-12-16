package org.example.Servise;


import org.example.DTO.ProductDTO;
import org.example.Entity.Product;
import org.example.Repository.ProductRepository;
import org.example.mapper.ProductMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void createProduct(ProductDTO productDTO) {
        Product product = ProductMapper.INSTANCE.fromDTO(productDTO);
        productRepository.save(product);
    }

    @Override
    public void updateProduct(ProductDTO productDTO) {
        Product product = ProductMapper.INSTANCE.fromDTO(productDTO);
        productRepository.update(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.delete(id);
    }

    @Override
    public ProductDTO getProductById(int id) {
        return productRepository.findById(id)
                .map(ProductMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }
}
