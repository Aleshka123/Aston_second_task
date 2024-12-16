import org.example.DTO.ProductDTO;
import org.example.Entity.Product;
import org.example.Repository.ProductRepository;
import org.example.Servise.ProductService;
import org.example.Servise.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;



import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Test
    void testCreateProduct() {
        ProductRepository mockRepo = Mockito.mock(ProductRepository.class);
        ProductService productService = new ProductServiceImpl(mockRepo);

        ProductDTO dto = new ProductDTO(0, "Ноутбук", 75000.00, 1);
        productService.createProduct(dto);
        verify(mockRepo, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById() {
        ProductRepository mockRepo = mock(ProductRepository.class);
        ProductService productService = new ProductServiceImpl(mockRepo);

        Product p = new Product(1, "Смартфон", 30000.00);
        when(mockRepo.findById(1)).thenReturn(Optional.of(p));

        ProductDTO result = productService.getProductById(1);
        assertEquals("Смартфон", result.getName());
        verify(mockRepo, times(1)).findById(1);
    }

    @Test
    void testGetAllProducts() {
        ProductRepository mockRepo = mock(ProductRepository.class);
        Product p1 = new Product(1, "Наушники", 2000.00);
        Product p2 = new Product(2, "Клавиатура", 1500.00);
        when(mockRepo.findAll()).thenReturn(List.of(p1, p2));

        ProductService productService = new ProductServiceImpl(mockRepo);
        var allProducts = productService.getAllProducts();
        assertEquals(2, allProducts.size());
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void testGetProductByIdNotFound() {
        ProductRepository mockRepo = mock(ProductRepository.class);
        ProductService productService = new ProductServiceImpl(mockRepo);

        when(mockRepo.findById(999)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(999));
    }


    @Test
    void testUpdateProduct() {
        ProductRepository mockRepo = mock(ProductRepository.class);
        ProductService productService = new ProductServiceImpl(mockRepo);

        ProductDTO dto = new ProductDTO(1, "Мышь", 800.00, 1);
        productService.updateProduct(dto);
        verify(mockRepo, times(1)).update(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        ProductRepository mockRepo = mock(ProductRepository.class);
        ProductService productService = new ProductServiceImpl(mockRepo);

        productService.deleteProduct(10);
        verify(mockRepo, times(1)).delete(10);
    }
}
