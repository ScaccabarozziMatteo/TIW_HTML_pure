package it.polimi.tiw.progetto1.Beans;

import java.io.IOException;
import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 5467675345346L;

    private int code;
    private int quantity;
    private String name;
    private String description;
    private String category;
    private String image;
    private float price;
    private boolean sale;

    public Product(int code, String name, String description, String category, String image) throws IOException {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
    }

    public Product(int code, String name, String description, String category, String image, float price) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.price = price;
    }

    public Product(int code, int quantity) {
        this.code = code;
        this.quantity = quantity;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public float getPrice() {
        return price;
    }

    public boolean getSale() {
        return sale;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}