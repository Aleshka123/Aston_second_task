package org.example.Servise;

import org.example.DTO.ProductDTO;
import java.util.List;

public interface ProductService {
    void createProduct(ProductDTO productDTO);

    void updateProduct(ProductDTO productDTO);

    void deleteProduct(int id);

    ProductDTO getProductById(int id);

    List<ProductDTO> getAllProducts();
}
