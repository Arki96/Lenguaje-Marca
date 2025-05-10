package com.mycompany.crudos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bdestadisticas?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    private Connection conexion;

    public DatabaseConnection() {
        conectarBaseDatos();
    }

    private void conectarBaseDatos() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException ex) {
            mostrarError("Controlador JDBC de MySQL no encontrado. Añádelo a las librerías.");
            System.exit(1);
        } catch (SQLException ex) {
            mostrarError("Error conectando a la base de datos: " + ex.getMessage());
            System.exit(1);
        }
    }

    public Connection getConnection() {
        return conexion;
    }

    private void mostrarError(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(null, mensaje, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}