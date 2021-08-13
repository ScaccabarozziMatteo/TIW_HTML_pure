package it.polimi.tiw.progetto1.Beans;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

public class Product implements Serializable {
    private static final long serialVersionUID = 5467675345346L;

    private int code;
    private int quantity;
    private String name;
    private String description;
    private String category;
    private String image;
    private float price;
    private boolean viewed;

    public Product(int code, String name, String description, String category, String image) throws IOException {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
    }

    public Product(int code, int quantity, String name, String description, String category, String image, float price) {
        this.code = code;
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.price = price;
    }
    public Product(int code, String name, String description, String category, String image, float price) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.price = price;
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

    public int getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }

    public float getPrice() {
        return price;
    }

    public boolean getViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

}
