package org.example.Repository;

import org.example.Entity.Product;
import org.example.Entity.Customer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

    private final DataSource dataSource;

    public ProductRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Product product) {
        String sql = "INSERT INTO products (name, price, customer_id) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setObject(3, product.getCustomer() != null ? product.getCustomer().getId() : null, Types.INTEGER);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    product.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving product", e);
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, customer_id = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setObject(3, product.getCustomer() != null ? product.getCustomer().getId() : null, Types.INTEGER);
            stmt.setInt(4, product.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    @Override
    public Optional<Product> findById(int id) {
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.price AS product_price, " +
                "c.id AS customer_id, c.name AS customer_name, c.email AS customer_email " +
                "FROM products p LEFT JOIN customers c ON p.customer_id = c.id WHERE p.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("product_price")
                    );

                    int customerId = rs.getInt("customer_id");
                    if (!rs.wasNull()) {
                        Customer customer = new Customer(
                                customerId,
                                rs.getString("customer_name"),
                                rs.getString("customer_email")
                        );
                        product.setCustomer(customer); // Устанавливаем связь
                    }

                    return Optional.of(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by ID", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.price AS product_price, " +
                "c.id AS customer_id, c.name AS customer_name, c.email AS customer_email " +
                "FROM products p LEFT JOIN customers c ON p.customer_id = c.id";

        List<Product> products = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("product_price")
                );

                int customerId = rs.getInt("customer_id");
                if (!rs.wasNull()) {
                    Customer customer = new Customer(
                            customerId,
                            rs.getString("customer_name"),
                            rs.getString("customer_email")
                    );
                    product.setCustomer(customer);
                }

                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all products", e);
        }

        return products;
    }
}
