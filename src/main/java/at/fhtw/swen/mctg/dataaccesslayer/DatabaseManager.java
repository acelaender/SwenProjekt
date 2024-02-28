package at.fhtw.swen.mctg.dataaccesslayer;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DatabaseManager {
    INSTANCE;
    private String connectionString = "jdbc:postgresql://localhost:5432/mydb?user=swen&password=password";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            throw new DataAccessException("Datenbankverbindungsaufbau nicht erfolgreich", e);
        }
    }


}
