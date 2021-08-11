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

    public List<Product> getProductsFromSearchTab(String nameProduct) throws SQLException, IOException {

        List <Product> products = new ArrayList<>();
        String SQLQuery = "SELECT * FROM dbtest.products  INNER JOIN dbtest.supplier_catalogue on products.code = supplier_catalogue.product  WHERE UPPER(name) LIKE ? OR UPPER(description) LIKE ? ORDER BY price  ";
        try (PreparedStatement statement = connection.prepareStatement(SQLQuery);
        ){
            statement.setString(1, '%' + nameProduct.toUpperCase() + '%');
            statement.setString(2,'%'+ nameProduct.toUpperCase() + '%');
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Product product = new Product(resultSet.getInt("code"),resultSet.getString("name"),resultSet.getString("description"),resultSet.getString("category"),resultSet.getString("photo"),resultSet.getFloat("price"));
                products.add(product);
            }
        }
        return products;
    }


    public List<Product> supplierProducts(String supplier) throws SQLException {
        List<Product> products = new ArrayList<>();

        String SQLQuery = "SELECT sc.quantity, code, name, description, category, photo, price FROM dbtest.products INNER JOIN supplier_catalogue sc on products.code = sc.product WHERE supplier LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(SQLQuery);
        ) {
            statement.setString(1, supplier);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(resultSet.getInt("code"), resultSet.getInt("quantity"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getString("category"), resultSet.getString("photo"), resultSet.getFloat("price"));
                products.add(product);
            }
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

    public void createProduct(int code, String name, String description, String category, String photoPath, int quantity, String supplier, float price) throws SQLException {
        String query = "INSERT into dbtest.products (code, name, description, category, photo) VALUES(?, ?, ?, ?, ?)";
        String query2 = "INSERT into dbtest.supplier_catalogue (product, supplier, quantity, price) VALUES (?, ?, ?, ?)";


        try (PreparedStatement pstatement = connection.prepareStatement(query); PreparedStatement pstatement2 = connection.prepareStatement(query2)) {
            pstatement.setInt(1, code);
            pstatement.setString(2, name);
            pstatement.setString(3, description);
            pstatement.setString(4, category);
            pstatement.setString(5, photoPath);
            pstatement.executeUpdate();

            pstatement2.setInt(1, code);
            pstatement2.setString(2, supplier);
            pstatement2.setInt(3, quantity);
            pstatement2.setFloat(4, price);
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

    public List<Product> getObservedProducts(String supplier, HttpServletRequest request) {
        List<Product> products = null;

        Cookie[] cookies = request.getCookies();
        Cookie cookieProducts = null;
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("observedProducts"))
                cookie = cookieProducts;
        }

        return products;
    }

}
