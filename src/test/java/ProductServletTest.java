import org.example.DTO.ProductDTO;
import org.example.Servise.ProductService;
import org.example.Servlets.ProductServlet;
import org.junit.jupiter.api.Test;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductServletTest {

    @Test
    void testDoGetAllProducts() throws Exception {
        ProductService mockService = mock(ProductService.class);
        when(mockService.getAllProducts()).thenReturn(
                java.util.List.of(new ProductDTO(1, "Ноутбук", 75000.00, 1))
        );

        ProductServlet servlet = new ProductServlet(mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.service(request, response);

        verify(response).setContentType("application/json");
        String jsonOutput = sw.toString();
        assertTrue(jsonOutput.contains("Ноутбук"));
    }

    @Test
    void testDoPost() throws Exception {
        ProductService mockService = mock(ProductService.class);
        ProductServlet servlet = new ProductServlet(mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String productJson = "{\"id\":0,\"name\":\"Смартфон\",\"price\":30000.00,\"customerId\":1}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(productJson)));
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(mockService, times(1)).createProduct(any(ProductDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPut() throws Exception {
        ProductService mockService = mock(ProductService.class);
        ProductServlet servlet = new ProductServlet(mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String productJson = "{\"id\":5,\"name\":\"Мышь Pro\",\"price\":1000.00,\"customerId\":2}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(productJson)));
        when(request.getMethod()).thenReturn("PUT");

        servlet.service(request, response);

        verify(mockService, times(1)).updateProduct(any(ProductDTO.class));
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDelete() throws Exception {
        ProductService mockService = mock(ProductService.class);
        ProductServlet servlet = new ProductServlet(mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/10");
        when(request.getMethod()).thenReturn("DELETE");

        servlet.service(request, response);

        verify(mockService, times(1)).deleteProduct(10);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoGetProductNotFound() throws Exception {
        ProductService mockService = mock(ProductService.class);
        when(mockService.getProductById(999)).thenThrow(new RuntimeException("Product not found"));

        ProductServlet servlet = new ProductServlet(mockService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/999");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND), eq("Product not found"));
    }

}
