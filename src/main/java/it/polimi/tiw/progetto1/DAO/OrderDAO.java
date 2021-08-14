package it.polimi.tiw.progetto1.DAO;

import it.polimi.tiw.progetto1.Beans.Order;

import java.sql.Connection;
import java.util.List;

public class OrderDAO {

    private Connection connection;

    public OrderDAO(Connection connection) {
        this.connection = connection;
    }


}
