package it.polimi.tiw.progetto1.DAO;

import it.polimi.tiw.progetto1.Customer;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Customer> list() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        String SQLQuery = "SELECT * FROM dbtest.customers";

        try (   PreparedStatement statement = connection.prepareStatement(SQLQuery);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                Customer customer = new Customer(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("email"), resultSet.getString("address"), resultSet.getString("password"), resultSet.getString("sex") );
                customers.add(customer);
            }
        }

        return customers;
    }

    public void createCustomer(String email, String name, String surname, String address, String password, String sex) throws SQLException {
        String query = "INSERT into dbtest.customers (email, name, surname, address, password, sex) VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstatement = connection.prepareStatement(query);) {
            pstatement.setString(1, email);
            pstatement.setString(2, name);
            pstatement.setString(3, surname);
            pstatement.setString(4, address);
            pstatement.setString(5, password);
            pstatement.setString(6, sex);
            pstatement.executeUpdate();
        }
    }

    public boolean findExistCustomer(String email, String password) throws SQLException {
        String Query = "SELECT email, password FROM dbtest.customers";

        try (
                PreparedStatement statement = connection.prepareStatement(Query);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
               if (email.equals(resultSet.getString("email")) && password.equals("password"))
                   return true;
            }
        }
        return false;
    }

}