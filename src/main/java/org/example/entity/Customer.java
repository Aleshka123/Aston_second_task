package org.example.entity;


import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String name;
    private String email;
    private List<Product> products = new ArrayList<>();


    public Customer() {}

    public Customer(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setCustomer(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.setCustomer(null);
    }
}
