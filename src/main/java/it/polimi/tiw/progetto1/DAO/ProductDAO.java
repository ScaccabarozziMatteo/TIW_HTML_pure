package it.polimi.tiw.progetto1.DAO;

import it.polimi.tiw.progetto1.Product;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Product> list() throws SQLException {
        List<Product> products = new ArrayList<>();

        String SQLQuery = "SELECT * FROM dbtest.products";

        try (PreparedStatement statement = connection.prepareStatement(SQLQuery);
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                Product product = new Product(resultSet.getInt("code"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getString("category"), resultSet.getBlob("photo"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public boolean ifExistsProduct(int code) throws SQLException {
        String Query = "SELECT code FROM dbtest.products WHERE products.code LIKE ?";
        boolean returnValue = false;

        try (PreparedStatement statement = connection.prepareStatement(Query);) {

            statement.setInt(1, code);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                if (code == resultSet.getInt("code"))
                    returnValue = true;
        }
        return returnValue;
    }

    public void createProduct(int code, String name, String description, String category, BufferedImage photo) throws SQLException {
        String query = "INSERT into dbtest.products (code, name, description, category, photo) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement pstatement = connection.prepareStatement(query);) {
            pstatement.setInt(1, code);
            pstatement.setString(2, name);
            pstatement.setString(3, description);
            pstatement.setString(4, category);
            pstatement.setBinaryStream(5, photo);
            pstatement.executeUpdate();
        }
    }
}
