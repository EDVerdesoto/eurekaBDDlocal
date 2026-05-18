package ec.edu.monster.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccesoDB {

    private AccesoDB() {
    }

    public static Connection getConnection() throws SQLException {
        Connection cn = null;

        try {
            String driver = "org.mariadb.jdbc.Driver";
            String url = "jdbc:mariadb://localhost:3306/eurekabank_rest_java";
            String user = "eureka";
            String pass = "admin";

            Class.forName(driver);
            cn = DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException e) {
            throw new SQLException("ERROR: no se encuentra el driver JDBC.");
        } catch (SQLException e) {
            throw new SQLException("ERROR al conectar: " + e.getMessage());
        }

        return cn;
    }

    public static Connection getConnectionModern() throws SQLException {
        Connection cn = null;

        try {

            cn = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/eurekabank_rest_java",
                    "eureka",
                    "admin"
            );

        } catch (SQLException e) {
            throw new SQLException("ERROR al conectar: " + e.getMessage());
        }

        return cn;
    }

}
