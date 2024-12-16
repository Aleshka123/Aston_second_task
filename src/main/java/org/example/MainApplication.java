//package org.example;
//
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
//import org.example.Config.DatabaseConnectionManager;
//import org.example.Repository.CustomerRepositoryImpl;
//import org.example.Repository.ProductRepositoryImpl;
//import org.example.Servise.CustomerServiceImpl;
//import org.example.Servise.ProductServiceImpl;
//import org.example.Servlets.CustomerServlet;
//import org.example.Servlets.ProductServlet;
//
//public class MainApplication {
//    public static void main(String[] args) throws Exception {
//
//        var dataSource = DatabaseConnectionManager.getDataSource();
//
//        var customerRepository = new CustomerRepositoryImpl(dataSource);
//        var productRepository = new ProductRepositoryImpl(dataSource);
//
//        var customerService = new CustomerServiceImpl(customerRepository);
//        var productService = new ProductServiceImpl(productRepository);
//
//        Server server = new Server(8080);
//
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        server.setHandler(context);
//
//        context.addServlet(new ServletHolder(new CustomerServlet(customerService)), "/customers/*");
//        context.addServlet(new ServletHolder(new ProductServlet(productService)), "/products/*");
//
//        server.start();
//        System.out.println("Сервер запущен: http://localhost:8080");
//        server.join();
//    }
//}
