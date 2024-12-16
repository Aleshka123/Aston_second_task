package org.example.Servlets;

import org.example.DTO.CustomerDTO;
import org.example.Servise.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {

    private final CustomerService customerService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomerServlet(CustomerService customerService) {
        this.customerService = customerService;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CustomerDTO customerDTO = objectMapper.readValue(req.getReader(), CustomerDTO.class);
            customerService.createCustomer(customerDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CustomerDTO customerDTO = objectMapper.readValue(req.getReader(), CustomerDTO.class);
            customerService.updateCustomer(customerDTO);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
