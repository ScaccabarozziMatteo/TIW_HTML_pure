package it.polimi.tiw.progetto1.DAO;

import it.polimi.tiw.progetto1.Customer;
import it.polimi.tiw.progetto1.Supplier;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                Supplier supplier = new Supplier(resultSet.getString("code"), resultSet.getString("name"), resultSet.getString("password"));
                suppliers.add(supplier);
            }
        }

        return suppliers;
    }

}