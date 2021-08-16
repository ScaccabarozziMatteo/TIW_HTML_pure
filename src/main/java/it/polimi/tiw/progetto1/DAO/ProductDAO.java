package it.polimi.tiw.progetto1.DAO;

import it.polimi.tiw.progetto1.Beans.Product;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
                Product product = new Product(resultSet.getInt("code"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getString("category"), resultSet.getString("photo"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public void addProductInCatalogue(int codeProd, float price, String supplier) throws SQLException {
        String query = "INSERT into dbtest.supplier_catalogue (price, product, supplier) VALUES(?, ?, ?)";

        try (PreparedStatement pstatement = connection.prepareStatement(query)) {
            pstatement.setFloat(1, price);
            pstatement.setInt(2, codeProd);
            pstatement.setString(3, supplier);
            pstatement.executeUpdate();

        }
    }

    public boolean supplierHasProduct(String supplier, int prodCode) throws SQLException{
        String Query = "SELECT supplier FROM dbtest.supplier_catalogue WHERE supplier_catalogue.supplier LIKE ? AND product LIKE ?";
        boolean returnValue = false;

        try (PreparedStatement statement = connection.prepareStatement(Query);) {

            statement.setString(1, supplier);
            statement.setInt(2, prodCode);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                if (supplier.equals(resultSet.getString("supplier")))
                    returnValue = true;
        }
        return returnValue;
    }

    public List<Product> getProductsFromSearchTab(String nameProduct) throws SQLException, IOException {

        List <Product> products = new ArrayList<>();
        String SQLQuery = "SELECT code, MIN(price), name, description, photo, category, price FROM dbtest.products INNER JOIN dbtest.supplier_catalogue ON products.code = supplier_catalogue.product WHERE UPPER(name) LIKE ? OR UPPER(description) LIKE ? GROUP BY code ORDER BY price";
        try (PreparedStatement statement = connection.prepareStatement(SQLQuery);
        ){
            statement.setString(1, '%' + nameProduct.toUpperCase() + '%');
            statement.setString(2,'%'+ nameProduct.toUpperCase() + '%');
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Product product = new Product(resultSet.getInt("code"),resultSet.getString("name"),resultSet.getString("description"),resultSet.getString("category"),resultSet.getString("photo"),resultSet.getFloat("MIN(price)"));
                products.add(product);
            }
        }
        return products;
    }

    public Product getInfoProduct(int code) throws SQLException, IOException {
        Product product = null;

        String SQLQuery = "SELECT * FROM dbtest.products INNER JOIN supplier_catalogue sc on products.code = sc.product WHERE products.code LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(SQLQuery);
        ) {
            statement.setInt(1, code);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                product = new Product(resultSet.getInt("code"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getString("category"), resultSet.getString("photo"), resultSet.getFloat("price"));
            }
        }

        return product;
    }


    public List<Product> supplierProducts(String supplier) throws SQLException {
        List<Product> products = new ArrayList<>();

        String SQLQuery = "SELECT code, name, description, category, photo, price FROM dbtest.products INNER JOIN supplier_catalogue sc ON products.code = sc.product WHERE supplier LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(SQLQuery);
        ) {
            statement.setString(1, supplier);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(resultSet.getInt("code"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getString("category"), resultSet.getString("photo"), resultSet.getFloat("price"));
                products.add(product);
            }
        }

        if (products.isEmpty())
            return null;

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

    public void createProduct(int code, String name, String description, String category, String photoPath, String supplier, float price) throws SQLException {
        String query = "INSERT into dbtest.products (code, name, description, category, photo) VALUES(?, ?, ?, ?, ?)";
        String query2 = "INSERT into dbtest.supplier_catalogue (product, supplier, price) VALUES (?, ?, ?)";


        try (PreparedStatement pstatement = connection.prepareStatement(query); PreparedStatement pstatement2 = connection.prepareStatement(query2)) {
            pstatement.setInt(1, code);
            pstatement.setString(2, name);
            pstatement.setString(3, description);
            pstatement.setString(4, category);
            pstatement.setString(5, photoPath);
            pstatement.executeUpdate();

            pstatement2.setInt(1, code);
            pstatement2.setString(2, supplier);
            pstatement2.setFloat(3, price);
            pstatement2.executeUpdate();


        }
    }

}
