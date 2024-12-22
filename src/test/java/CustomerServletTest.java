import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DTO.CustomerDTO;
import org.example.Servise.CustomerService;
import org.example.Servlets.CustomerServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerServletTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private void injectMockService(CustomerServlet servlet, CustomerService mockService) throws Exception {
        Field field = CustomerServlet.class.getDeclaredField("customerService");
        field.setAccessible(true);
        field.set(servlet, mockService);
    }

    @Test
    void testDoGetAllCustomers() throws Exception {
        // Создаем мок сервиса
        CustomerService mockService = mock(CustomerService.class);
        when(mockService.getAllCustomers()).thenReturn(
                java.util.List.of(new CustomerDTO(1, "Иван Иванов", "ivan@example.com", null))
        );

        // Создаем экземпляр сервлета и подменяем сервис
        CustomerServlet servlet = new CustomerServlet();
        injectMockService(servlet, mockService);

        // Настраиваем моки для запроса и ответа
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        // Вызываем метод doGet
        servlet.doGet(request, response);

        // Проверяем результат
        verify(response).setContentType("application/json");
        String jsonOutput = sw.toString();
        assertTrue(jsonOutput.contains("Иван Иванов"));
    }

    @Test
    void testDoPost() throws Exception {
        // Создаем мок сервиса
        CustomerService mockService = mock(CustomerService.class);

        // Создаем экземпляр сервлета и подменяем сервис
        CustomerServlet servlet = new CustomerServlet();
        injectMockService(servlet, mockService);

        // Настраиваем моки для запроса и ответа
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        CustomerDTO customerDTO = new CustomerDTO(0, "Мария Петрова", "mariya@example.com", null);
        String customerJson = objectMapper.writeValueAsString(customerDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(customerJson)));

        // Вызываем метод doPost
        servlet.doPost(request, response);

        // Проверяем взаимодействие
        verify(mockService, times(1)).createCustomer(any(CustomerDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoGetCustomerNotFound() throws Exception {
        // Создаем мок сервиса
        CustomerService mockService = mock(CustomerService.class);
        when(mockService.getCustomerById(999)).thenThrow(new RuntimeException("Customer not found"));

        // Создаем экземпляр сервлета и подменяем сервис
        CustomerServlet servlet = new CustomerServlet();
        injectMockService(servlet, mockService);

        // Настраиваем моки для запроса и ответа
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/999");

        // Вызываем метод doGet
        servlet.doGet(request, response);

        // Проверяем результат
        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND), eq("Customer not found"));
    }
}
