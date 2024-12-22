package org.example.dto;
import java.util.List;

public class CustomerDTO {
    private int id;
    private String name;
    private String email;
    private List<Integer> productIds;

    public CustomerDTO() {}

    public CustomerDTO(int id, String name, String email, List<Integer> productIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.productIds = productIds;
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

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }
}
