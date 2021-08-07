package it.polimi.tiw.progetto1.DAO;

import it.polimi.tiw.progetto1.Product;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public void createProduct(int code, String name, String description, String category, InputStream photo, int quantity, String supplier) throws SQLException {
        String query = "INSERT into dbtest.products (code, name, description, category, photo) VALUES(?, ?, ?, ?, ?)";
        String query2 = "INSERT into dbtest.supplier_catalogue (product, supplier, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement pstatement = connection.prepareStatement(query); PreparedStatement pstatement2 = connection.prepareStatement(query2)) {
            pstatement.setInt(1, code);
            pstatement.setString(2, name);
            pstatement.setString(3, description);
            pstatement.setString(4, category);
            pstatement.setBinaryStream(5, photo);
            pstatement.executeUpdate();

            pstatement2.setInt(1, code);
            pstatement2.setString(2, supplier);
            pstatement2.setInt(3, quantity);
            pstatement2.executeUpdate();


        }
    }

    public void addQuantityProduct(int codeProd, int quantity, String supplier) throws SQLException {

        String query = "SELECT quantity FROM dbtest.supplier_catalogue WHERE supplier LIKE ? AND product LIKE ?";
        String query2 = "UPDATE dbtest.supplier_catalogue SET quantity = ? + ? WHERE supplier LIKE ? AND product LIKE ?";
        int existingQuantity = 0;

        try (PreparedStatement pstatement = connection.prepareStatement(query); PreparedStatement pstatement2 = connection.prepareStatement(query2)) {
            pstatement.setString(1, supplier);
            pstatement.setInt(2, codeProd);
            ResultSet resultSet = pstatement.executeQuery();

            while (resultSet.next()) {
                existingQuantity = resultSet.getInt("quantity");
            }

            pstatement2.setInt(1, quantity);
            pstatement2.setInt(2, existingQuantity);
            pstatement2.setString(3, supplier);
            pstatement2.setInt(4, codeProd);
            pstatement2.executeUpdate();

        }
    }

}
