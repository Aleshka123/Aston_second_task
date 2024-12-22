import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DTO.ProductDTO;
import org.example.Servise.ProductService;
import org.example.Servlets.ProductServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductServletTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private void injectMockService(ProductServlet servlet, ProductService mockService) throws Exception {
        Field field = ProductServlet.class.getDeclaredField("productService");
        field.setAccessible(true);
        field.set(servlet, mockService);
    }

    @Test
    void testDoGetAllProducts() throws Exception {
        // Создаем мок сервиса
        ProductService mockService = mock(ProductService.class);
        when(mockService.getAllProducts()).thenReturn(
                java.util.List.of(new ProductDTO(1, "Ноутбук", 75000.00, 1))
        );

        // Создаем экземпляр сервлета и подменяем сервис
        ProductServlet servlet = new ProductServlet();
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
        assertTrue(jsonOutput.contains("Ноутбук"));
    }

    @Test
    void testDoPost() throws Exception {
        // Создаем мок сервиса
        ProductService mockService = mock(ProductService.class);

        // Создаем экземпляр сервлета и подменяем сервис
        ProductServlet servlet = new ProductServlet();
        injectMockService(servlet, mockService);

        // Настраиваем моки для запроса и ответа
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ProductDTO productDTO = new ProductDTO(0, "Смартфон", 30000.00, 1);
        String productJson = objectMapper.writeValueAsString(productDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(productJson)));

        // Вызываем метод doPost
        servlet.doPost(request, response);

        // Проверяем взаимодействие
        verify(mockService, times(1)).createProduct(any(ProductDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPut() throws Exception {
        // Создаем мок сервиса
        ProductService mockService = mock(ProductService.class);

        // Создаем экземпляр сервлета и подменяем сервис
        ProductServlet servlet = new ProductServlet();
        injectMockService(servlet, mockService);

        // Настраиваем моки для запроса и ответа
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ProductDTO productDTO = new ProductDTO(5, "Мышь Pro", 1000.00, 2);
        String productJson = objectMapper.writeValueAsString(productDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(productJson)));

        // Вызываем метод doPut
        servlet.doPut(request, response);

        // Проверяем взаимодействие
        verify(mockService, times(1)).updateProduct(any(ProductDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDelete() throws Exception {
        // Создаем мок сервиса
        ProductService mockService = mock(ProductService.class);

        // Создаем экземпляр сервлета и подменяем сервис
        ProductServlet servlet = new ProductServlet();
        injectMockService(servlet, mockService);

        // Настраиваем моки для запроса и ответа
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/10");

        // Вызываем метод doDelete
        servlet.doDelete(request, response);

        // Проверяем взаимодействие
        verify(mockService, times(1)).deleteProduct(10);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoGetProductNotFound() throws Exception {
        // Создаем мок сервиса
        ProductService mockService = mock(ProductService.class);
        when(mockService.getProductById(999)).thenThrow(new RuntimeException("Product not found"));

        // Создаем экземпляр сервлета и подменяем сервис
        ProductServlet servlet = new ProductServlet();
        injectMockService(servlet, mockService);

        // Настраиваем моки для запроса и ответа
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/999");

        // Вызываем метод doGet
        servlet.doGet(request, response);

        // Проверяем результат
        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND), eq("Product not found"));
    }
}
