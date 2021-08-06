package it.polimi.tiw.progetto1.DAO;

import it.polimi.tiw.progetto1.Supplier;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SupplierDAO {

    private Connection connection;

    public SupplierDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Supplier> list() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();

        String SQLQuery = "SELECT * FROM dbtest.suppliers";

        try (   PreparedStatement statement = connection.prepareStatement(SQLQuery);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                Supplier supplier = new Supplier(resultSet.getString("code"), resultSet.getString("name"), resultSet.getString("password"), resultSet.getInt("evaluation"));
                suppliers.add(supplier);
            }
        }

        return suppliers;
    }

    public Supplier getSupplier(String code, String password) throws SQLException {
        String Query = "SELECT * FROM dbtest.suppliers WHERE suppliers.code LIKE ? AND suppliers.password LIKE ?";
        Supplier supplier = null;

        try (PreparedStatement statement = connection.prepareStatement(Query);) {

            statement.setString(1, code);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                if (code.equals(resultSet.getString("code")) && password.equals(resultSet.getString("password")))
                    supplier = new Supplier(resultSet.getString("code"), resultSet.getString("name"), resultSet.getString("password"), resultSet.getInt("evaluation"));
        }
        return supplier;
    }

    public void createSupplier(String code, String name, String password) throws SQLException {
        String query = "INSERT into dbtest.suppliers (code, name, password, evaluation) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstatement = connection.prepareStatement(query);) {
            float evaluation;
            Random random = new Random();
            evaluation = random.nextInt(4) + 1;
            pstatement.setString(1, code);
            pstatement.setString(2, name);
            pstatement.setString(3, password);
            pstatement.setFloat(4, evaluation);

            pstatement.executeUpdate();
        }
    }

    public boolean findIfExistSupplier(String code) throws SQLException {
        String Query = "SELECT code FROM dbtest.suppliers WHERE suppliers.code LIKE ?";
        boolean returnValue = false;

        try (PreparedStatement statement = connection.prepareStatement(Query);) {

            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                if (code.equals(resultSet.getString("code")))
                    returnValue = true;
        }
        return returnValue;
    }

}