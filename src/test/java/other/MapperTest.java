package other;

import org.example.dto.CustomerDTO;
import org.example.dto.ProductDTO;
import org.example.mapper.CustomerMapper;
import org.example.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.example.entity.Product;
import org.example.entity.Customer;
import static org.junit.jupiter.api.Assertions.*;

public class MapperTest {

    @Test
    void testCustomerMapper() {
        Customer c = new Customer(1, "Иван", "ivan@example.com");
        Product p1 = new Product(0, "Ноутбук", 50000.00);
        Product p2 = new Product(0, "Мышь", 500.00);
        c.addProduct(p1);
        c.addProduct(p2);

        CustomerDTO dto = CustomerMapper.INSTANCE.toDTO(c);
        assertEquals(1, dto.getId());
        assertEquals("Иван", dto.getName());
        assertEquals("ivan@example.com", dto.getEmail());
        assertEquals(2, dto.getProductIds().size());

        Customer c2 = CustomerMapper.INSTANCE.fromDTO(dto);
        assertEquals(dto.getId(), c2.getId());
        assertEquals(dto.getName(), c2.getName());
        assertEquals(dto.getEmail(), c2.getEmail());
        assertTrue(c2.getProducts().isEmpty());
    }

    @Test
    void testProductMapper() {
        Product p = new Product(10, "Смартфон", 30000.00);
        Customer c = new Customer(2, "Мария", "maria@example.com");
        p.setCustomer(c);

        ProductDTO dto = ProductMapper.INSTANCE.toDTO(p);
        assertEquals(10, dto.getId());
        assertEquals("Смартфон", dto.getName());
        assertEquals(30000.00, dto.getPrice());
        assertEquals(2, dto.getCustomerId());

        Product p2 = ProductMapper.INSTANCE.fromDTO(dto);
        assertEquals(dto.getId(), p2.getId());
        assertEquals(dto.getName(), p2.getName());
        assertEquals(dto.getPrice(), p2.getPrice());
        assertNull(p2.getCustomer());
    }
}
