import org.example.DTO.CustomerDTO;
import org.example.Servise.CustomerService;
import org.example.Servlets.CustomerServlet;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerServletTest {

    @Test
    void testDoGetAllCustomers() throws Exception {
        CustomerService mockService = mock(CustomerService.class);
        when(mockService.getAllCustomers()).thenReturn(
                java.util.List.of(new CustomerDTO(1, "Иван Иванов", "ivan@example.com", null))
        );

        CustomerServlet servlet = new CustomerServlet(mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getPathInfo()).thenReturn("/");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.service(request, response);

        verify(response).setContentType("application/json");
        String jsonOutput = sw.toString();
        assertTrue(jsonOutput.contains("Иван Иванов"));
    }

    @Test
    void testDoPost() throws Exception {
        CustomerService mockService = mock(CustomerService.class);
        CustomerServlet servlet = new CustomerServlet(mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String customerJson = "{\"id\":0,\"name\":\"Мария Петрова\",\"email\":\"mariya@example.com\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(customerJson)));

        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(mockService, times(1)).createCustomer(any(CustomerDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoGetCustomerNotFound() throws Exception {
        CustomerService mockService = mock(CustomerService.class);
        when(mockService.getCustomerById(999)).thenThrow(new RuntimeException("Customer not found"));

        CustomerServlet servlet = new CustomerServlet(mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getPathInfo()).thenReturn("/999");

        servlet.service(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND), eq("Customer not found"));
    }
}
