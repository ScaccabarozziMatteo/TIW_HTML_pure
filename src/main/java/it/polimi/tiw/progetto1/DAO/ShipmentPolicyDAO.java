package it.polimi.tiw.progetto1.DAO;

import it.polimi.tiw.progetto1.ShipmentPolicy;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShipmentPolicyDAO {
    private Connection connection;

    public ShipmentPolicyDAO(Connection connection) {
        this.connection = connection;
    }

    public List<ShipmentPolicy> shipmentPolicyList(String supplier) throws SQLException {
        List<ShipmentPolicy> shipmentPolicies = new ArrayList<>();

        String SQLQuery = "SELECT * FROM dbtest.shipment_policy WHERE supplier LIKE ? ORDER BY min_articles";

        try (PreparedStatement statement = connection.prepareStatement(SQLQuery)) {
            statement.setString(1, supplier);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ShipmentPolicy shipmentPolicy = new ShipmentPolicy(resultSet.getInt("id"), resultSet.getInt("min_articles"), resultSet.getInt("max_articles"), resultSet.getString("supplier"), resultSet.getInt("cost_shipment"), resultSet.getInt("free_shipment"));
                shipmentPolicies.add(shipmentPolicy);
            }
        }

        return shipmentPolicies;
    }


    public void createShipPolicy(int min_articles, int max_articles, String supplier, float costShipment) throws SQLException {
        String query = "INSERT into dbtest.shipment_policy (min_articles, max_articles, supplier, cost_shipment, free_shipment) VALUES(?, ?, ?, ?, 0)";
        try (PreparedStatement pstatement = connection.prepareStatement(query);) {

            pstatement.setInt(1, min_articles);
            pstatement.setInt(2, max_articles);
            pstatement.setString(3, supplier);
            pstatement.setFloat(4, costShipment);

            pstatement.executeUpdate();
        }
    }

    public void createShipPolicy(String supplier, int freeShipment) throws SQLException {
        String query = "INSERT into dbtest.shipment_policy (supplier, free_shipment) VALUES(?, ?)";
        try (PreparedStatement pstatement = connection.prepareStatement(query);) {

            pstatement.setString(1, supplier);
            pstatement.setInt(2, freeShipment);

            pstatement.executeUpdate();
        }
    }

    public boolean checkIfExistPolicy(String supplier, int min_articles, int max_articles) throws SQLException {
        String query = "SELECT * FROM shipment_policy WHERE supplier LIKE ? AND ((min_articles <= ? AND max_articles >= ?) OR (min_articles >= ? AND max_articles <= ?) OR (min_articles <= ? AND max_articles <= ? AND max_articles >= ?) OR (min_articles >= ? AND max_articles >= ? AND min_articles <= ?) && (free_shipment <= ?))";
        ResultSet resultSet;
        boolean returnValue;
        try (PreparedStatement pstatement = connection.prepareStatement(query);) {

            pstatement.setString(1, supplier);
            pstatement.setInt(2, min_articles);
            pstatement.setInt(3, max_articles);
            pstatement.setInt(4, min_articles);
            pstatement.setInt(5, max_articles);
            pstatement.setInt(6, min_articles);
            pstatement.setInt(7, max_articles);
            pstatement.setInt(8, min_articles);
            pstatement.setInt(9, min_articles);
            pstatement.setInt(10, max_articles);
            pstatement.setInt(11, max_articles);
            pstatement.setInt(12, max_articles);

            resultSet = pstatement.executeQuery();
            returnValue = resultSet.next();
        }
        return returnValue;
    }

    public boolean checkIfExistPolicy(String supplier, int quantityFreeShipment) throws SQLException {
        String query = "SELECT * FROM shipment_policy WHERE supplier LIKE ? AND (max_articles >= ? OR NOT(free_shipment LIKE NULL))";
        ResultSet resultSet;
        boolean returnValue;
        try (PreparedStatement pstatement = connection.prepareStatement(query);) {

            pstatement.setString(1, supplier);
            pstatement.setInt(2, quantityFreeShipment);

            resultSet = pstatement.executeQuery();
            returnValue = resultSet.next();
        }
        return returnValue;
    }


}
