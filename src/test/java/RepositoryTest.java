import org.example.config.DatabaseConnectionManager;
import org.example.entity.Customer;
import org.example.entity.Product;
import org.example.repository.CustomerRepository;
import org.example.repository.CustomerRepositoryImpl;
import org.example.repository.ProductRepository;
import org.example.repository.ProductRepositoryImpl;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTest {

    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .waitingFor(org.testcontainers.containers.wait.strategy.Wait.forListeningPort());

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    @BeforeAll
    void setup() throws Exception {
        postgres.start();

        System.setProperty("db.url", postgres.getJdbcUrl());
        System.setProperty("db.username", postgres.getUsername());
        System.setProperty("db.password", postgres.getPassword());
        System.setProperty("db.driver", "org.postgresql.Driver");
        System.setProperty("db.pool.size", "30");

        try (Connection conn = DatabaseConnectionManager.getDataSource().getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS products CASCADE;");
            stmt.execute("DROP TABLE IF EXISTS customers CASCADE;");
            stmt.execute("CREATE TABLE customers (id SERIAL PRIMARY KEY, name VARCHAR(100), email VARCHAR(100));");
            stmt.execute("CREATE TABLE products (id SERIAL PRIMARY KEY, name VARCHAR(100), price NUMERIC(10,2), customer_id INT REFERENCES customers(id) ON DELETE CASCADE);");
        }

        this.customerRepository = new CustomerRepositoryImpl(DatabaseConnectionManager.getDataSource());
        this.productRepository = new ProductRepositoryImpl(DatabaseConnectionManager.getDataSource());
    }

    @AfterAll
    void teardown() {
        postgres.stop();
    }

    // Тесты для CustomerRepository
    @Test
    void testSaveAndFindCustomerById() throws Exception {
        Customer customer = new Customer(0, "Иван Иванов", "ivan@example.com");
        customerRepository.save(customer);
        assertThat(customer.getId()).isGreaterThan(0);

        var found = customerRepository.findById(customer.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Иван Иванов");
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = new Customer(0, "Петр Петров", "petr@example.com");
        customerRepository.save(customer);

        customer.setName("Петр Петрович");
        customerRepository.update(customer);

        var found = customerRepository.findById(customer.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Петр Петрович");
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = new Customer(0, "Мария Сидорова", "maria@example.com");
        customerRepository.save(customer);

        customerRepository.delete(customer.getId());

        var found = customerRepository.findById(customer.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void testFindAllCustomers() throws Exception {
        Customer customer1 = new Customer(0, "User1", "user1@example.com");
        Customer customer2 = new Customer(0, "User2", "user2@example.com");

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        var allCustomers = customerRepository.findAll();
        assertThat(allCustomers).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void testSaveAndFindProductById() throws Exception {
        Customer customer = new Customer(0, "Иван Иванов", "ivan@example.com");
        customerRepository.save(customer);

        Product product = new Product(0, "Ноутбук", 75000.00);
        product.setCustomer(customer);
        productRepository.save(product);

        assertThat(product.getId()).isGreaterThan(0);
        var found = productRepository.findById(product.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ноутбук");
    }

    @Test
    void testUpdateProduct() throws Exception {
        Customer customer = new Customer(0, "Мария Петрова", "maria@example.com");
        customerRepository.save(customer);

        Product product = new Product(0, "Смартфон", 30000.00);
        product.setCustomer(customer);
        productRepository.save(product);

        product.setName("Смартфон Pro");
        product.setPrice(35000.00);
        productRepository.update(product);

        var found = productRepository.findById(product.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Смартфон Pro");
        assertThat(found.get().getPrice()).isEqualTo(35000.00);
    }

    @Test
    void testDeleteProduct() throws Exception {
        Customer customer = new Customer(0, "Петр Сидоров", "petr@example.com");
        customerRepository.save(customer);

        Product product = new Product(0, "Наушники", 2000.00);
        product.setCustomer(customer);
        productRepository.save(product);

        productRepository.delete(product.getId());
        var found = productRepository.findById(product.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void testFindAllProducts() throws Exception {
        Customer customer = new Customer(0, "UserX", "userx@example.com");
        customerRepository.save(customer);

        Product product1 = new Product(0, "Клавиатура", 1500.00);
        product1.setCustomer(customer);
        Product product2 = new Product(0, "Мышь", 800.00);
        product2.setCustomer(customer);

        productRepository.save(product1);
        productRepository.save(product2);

        var allProducts = productRepository.findAll();
        assertThat(allProducts).hasSizeGreaterThanOrEqualTo(2);
    }
}
