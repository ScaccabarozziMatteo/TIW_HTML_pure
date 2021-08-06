package it.polimi.tiw.progetto1.DAO;

import java.sql.Connection;

public class Ship_PolicyDAO {
    private Connection connection;

    public Ship_PolicyDAO(Connection connection) {
        this.connection = connection;
    }

}
