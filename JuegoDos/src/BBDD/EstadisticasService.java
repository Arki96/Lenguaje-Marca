package BBDD;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para generar estadísticas del juego.
 */
public class EstadisticasService {
    
    /**
     * Obtiene las victorias por tipo de personaje.
     */
    public Map<String, Integer> obtenerVictoriasPorPersonaje() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        String query = "SELECT p.nombre, COUNT(c.id) as victorias " +
                      "FROM combates c " +
                      "JOIN personajes p ON c.id_personaje = p.id " +
                      "WHERE c.resultado = 'victoria' " +
                      "GROUP BY p.nombre";
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                stats.put(rs.getString("nombre"), rs.getInt("victorias"));
            }
        }
        
        return stats;
    }
    
    /**
     * Obtiene los últimos 10 combates registrados.
     */
    public List<Map<String, Object>> obtenerTopCombates() throws SQLException {
        List<Map<String, Object>> combates = new ArrayList<>();
        String query = "SELECT p.nombre as personaje, e.nombre as enemigo, " +
                      "c.resultado, c.rondas, c.danio_total, c.fecha " +
                      "FROM combates c " +
                      "JOIN personajes p ON c.id_personaje = p.id " +
                      "JOIN enemigos e ON c.id_enemigo = e.id " +
                      "ORDER BY c.fecha DESC LIMIT 10";
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Map<String, Object> combate = new HashMap<>();
                combate.put("personaje", rs.getString("personaje"));
                combate.put("enemigo", rs.getString("enemigo"));
                combate.put("resultado", rs.getString("resultado"));
                combate.put("rondas", rs.getInt("rondas"));
                combate.put("danio_total", rs.getInt("danio_total"));
                combate.put("fecha", rs.getTimestamp("fecha"));
                combates.add(combate);
            }
        }
        
        return combates;
    }
    
    /**
     * Obtiene el número total de combates.
     */
    public int obtenerTotalCombates() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM combates";
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        
        return 0;
    }
}
