package org.example.DTO;


public class ProductDTO {
    private int id;
    private String name;
    private double price;
    private int customerId;


    public ProductDTO() {}

    public ProductDTO(int id, String name, double price, int customerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.customerId = customerId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
