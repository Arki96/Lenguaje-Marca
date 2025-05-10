package com.mycompany.crudos;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CRUDOperations {
    private DatabaseConnection dbConnection;

    public CRUDOperations(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void cargar(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);
        String query = "SELECT * FROM estadisticas";
        try (Statement stmt = dbConnection.getConnection().createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id"),
                    rs.getString("personaje"),
                    rs.getString("rol"),
                    rs.getInt("vida"),
                    rs.getString("ataque"),
                    rs.getString("defensa")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar las estadísticas: " + ex.getMessage());
        }
    }

    public void agregar(String personaje, String rol, int vida, String ataque, String defensa) throws SQLException {
        String sql = "INSERT INTO estadisticas(personaje, rol, vida, ataque, defensa) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, personaje);
            pstmt.setString(2, rol);
            pstmt.setInt(3, vida);
            pstmt.setString(4, ataque);
            pstmt.setString(5, defensa);
            pstmt.executeUpdate();
        }
    }

    public void actualizar(int id, String personaje, String rol, int vida, String ataque, String defensa) throws SQLException {
        String sql = "UPDATE estadisticas SET personaje=?, rol=?, vida=?, ataque=?, defensa=? WHERE id=?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, personaje);
            pstmt.setString(2, rol);
            pstmt.setInt(3, vida);
            pstmt.setString(4, ataque);
            pstmt.setString(5, defensa);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM estadisticas WHERE id=?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
