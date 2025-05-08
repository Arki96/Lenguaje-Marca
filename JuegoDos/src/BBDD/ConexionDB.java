package BBDD;

import java.sql.*;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/ZaltorDB?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Sin espacio si la contraseña está vacía
    private static Connection connection = null;

    static {
        try {
            // Para Connector/J 9.3.0 + MySQL 8.3.0
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver JDBC: " + e.getMessage());
            e.printStackTrace(); // Para ver detalles del error
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
