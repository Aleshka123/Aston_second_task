import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ProductDTO;
import org.example.servise.ProductService;
import org.example.servlets.ProductServlet;
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

        ProductService mockService = mock(ProductService.class);
        when(mockService.getAllProducts()).thenReturn(
                java.util.List.of(new ProductDTO(1, "Ноутбук", 75000.00, 1))
        );

        ProductServlet servlet = new ProductServlet();
        injectMockService(servlet, mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        String jsonOutput = sw.toString();
        assertTrue(jsonOutput.contains("Ноутбук"));
    }

    @Test
    void testDoPost() throws Exception {
        ProductService mockService = mock(ProductService.class);

        ProductServlet servlet = new ProductServlet();
        injectMockService(servlet, mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ProductDTO productDTO = new ProductDTO(0, "Смартфон", 30000.00, 1);
        String productJson = objectMapper.writeValueAsString(productDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(productJson)));

        servlet.doPost(request, response);

        verify(mockService, times(1)).createProduct(any(ProductDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPut() throws Exception {
        ProductService mockService = mock(ProductService.class);

        ProductServlet servlet = new ProductServlet();
        injectMockService(servlet, mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ProductDTO productDTO = new ProductDTO(5, "Мышь Pro", 1000.00, 2);
        String productJson = objectMapper.writeValueAsString(productDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(productJson)));

        servlet.doPut(request, response);

        verify(mockService, times(1)).updateProduct(any(ProductDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDelete() throws Exception {
        ProductService mockService = mock(ProductService.class);

        ProductServlet servlet = new ProductServlet();
        injectMockService(servlet, mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/10");

        servlet.doDelete(request, response);

        verify(mockService, times(1)).deleteProduct(10);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoGetProductNotFound() throws Exception {
        ProductService mockService = mock(ProductService.class);
        when(mockService.getProductById(999)).thenThrow(new RuntimeException("Product not found"));

        ProductServlet servlet = new ProductServlet();
        injectMockService(servlet, mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/999");

        servlet.doGet(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND), eq("Product not found"));
    }
}
