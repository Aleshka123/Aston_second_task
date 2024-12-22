package org.example.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Config.DatabaseConnectionManager;
import org.example.DTO.CustomerDTO;
import org.example.Repository.CustomerRepository;
import org.example.Repository.CustomerRepositoryImpl;
import org.example.Servise.CustomerService;
import org.example.Servise.CustomerServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {

    private final CustomerService customerService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomerServlet() {
        try {
            // Получаем DataSource из DatabaseConnectionManager
            DataSource dataSource = DatabaseConnectionManager.getDataSource();

            // Используем реализацию CustomerRepository
            CustomerRepository customerRepository = new CustomerRepositoryImpl(dataSource);

            // Создаём CustomerService с репозиторием
            this.customerService = new CustomerServiceImpl(customerRepository);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize CustomerServlet", e);
        }
    }


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<CustomerDTO> customers = customerService.getAllCustomers();
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(customers));
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                CustomerDTO customer = customerService.getCustomerById(id);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(customer));
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid customer ID");
            } catch (RuntimeException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CustomerDTO customerDTO = objectMapper.readValue(req.getReader(), CustomerDTO.class);
            customerService.createCustomer(customerDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data");
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CustomerDTO customerDTO = objectMapper.readValue(req.getReader(), CustomerDTO.class);
            customerService.updateCustomer(customerDTO);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            customerService.deleteCustomer(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid customer ID");
        }
    }
}
