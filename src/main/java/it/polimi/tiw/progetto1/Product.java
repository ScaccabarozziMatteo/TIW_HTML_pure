package it.polimi.tiw.progetto1;

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
    private String name;
    private String description;
    private String category;
    private BufferedImage image;

    public Product(int code, String name, String description, String category, Blob image) throws SQLException, IOException {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;

        InputStream in = image.getBinaryStream();
        this.image = ImageIO.read(in);
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

    public BufferedImage getImage() {
        return image;
    }

}
