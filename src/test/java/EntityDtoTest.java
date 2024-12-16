import org.example.DTO.CustomerDTO;
import org.example.DTO.ProductDTO;
import org.junit.jupiter.api.Test;
import org.example.Entity.Product;
import org.example.Entity.Customer;

import static org.junit.jupiter.api.Assertions.*;

public class EntityDtoTest {

    @Test
    void testCustomerEntity() {
        Customer c = new Customer();
        c.setId(1);
        c.setName("Test Customer");
        c.setEmail("test@example.com");

        assertEquals(1, c.getId());
        assertEquals("Test Customer", c.getName());
        assertEquals("test@example.com", c.getEmail());

        Product p = new Product(0, "Product1", 100.0);
        c.addProduct(p);
        assertEquals(1, c.getProducts().size());
        assertEquals(c, p.getCustomer());

        c.removeProduct(p);
        assertTrue(c.getProducts().isEmpty());
        assertNull(p.getCustomer());
    }

    @Test
    void testProductEntity() {
        Product p = new Product();
        p.setId(10);
        p.setName("Test Product");
        p.setPrice(999.99);

        assertEquals(10, p.getId());
        assertEquals("Test Product", p.getName());
        assertEquals(999.99, p.getPrice());
    }

    @Test
    void testCustomerDTO() {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(2);
        dto.setName("DTO Customer");
        dto.setEmail("dto@example.com");
        dto.setProductIds(java.util.List.of(1,2,3));

        assertEquals(2, dto.getId());
        assertEquals("DTO Customer", dto.getName());
        assertEquals("dto@example.com", dto.getEmail());
        assertEquals(3, dto.getProductIds().size());
    }

    @Test
    void testProductDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setId(5);
        dto.setName("DTO Product");
        dto.setPrice(500.0);
        dto.setCustomerId(1);

        assertEquals(5, dto.getId());
        assertEquals("DTO Product", dto.getName());
        assertEquals(500.0, dto.getPrice());
        assertEquals(1, dto.getCustomerId());
    }
}
