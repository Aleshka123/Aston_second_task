package org.example.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Config.DatabaseConnectionManager;
import org.example.DTO.ProductDTO;
import org.example.Repository.ProductRepository;
import org.example.Repository.ProductRepositoryImpl;
import org.example.Servise.ProductService;
import org.example.Servise.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    private final ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductServlet() {
        try {
            // Получаем DataSource из DatabaseConnectionManager
            DataSource dataSource = DatabaseConnectionManager.getDataSource();

            // Используем реализацию ProductRepository
            ProductRepository productRepository = new ProductRepositoryImpl(dataSource);

            // Создаём ProductService с репозиторием
            this.productService = new ProductServiceImpl(productRepository);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ProductServlet", e);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<ProductDTO> products = productService.getAllProducts();
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(products));
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                ProductDTO product = productService.getProductById(id);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(product));
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
            } catch (RuntimeException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ProductDTO productDTO = objectMapper.readValue(req.getReader(), ProductDTO.class);
            productService.createProduct(productDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data");
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ProductDTO productDTO = objectMapper.readValue(req.getReader(), ProductDTO.class);
            productService.updateProduct(productDTO);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            productService.deleteProduct(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
        }
    }
}
