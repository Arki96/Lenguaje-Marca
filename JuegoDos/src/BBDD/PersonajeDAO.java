package BBDD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import logica.*;

/**
 * Clase Data Access Object para manejar operaciones de base de datos relacionadas con personajes.
 */
public class PersonajeDAO {
    
    /**
     * Inserta un nuevo personaje en la base de datos.
     */
    public void insertarPersonaje(Personaje personaje) throws SQLException {
        String query = "INSERT INTO personajes (nombre, vida, ataque, defensa, velocidad, " +
                      "habilidadEspecial, nivel, estaVivo, energia, vidaMaxima) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, personaje.nombre);
            ps.setInt(2, personaje.vida);
            ps.setInt(3, personaje.ataque);
            ps.setInt(4, personaje.defensa);
            ps.setInt(5, personaje.velocidad);
            ps.setInt(6, personaje.habilidadEspecial);
            ps.setInt(7, personaje.nivel);
            ps.setBoolean(8, personaje.estaVivo);
            ps.setInt(9, personaje.energia);
            ps.setInt(10, personaje.vidaMaxima);
            
            ps.executeUpdate();
            
            // Obtener el ID generado
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    personaje.id = generatedKeys.getInt(1);
                }
            }
        }
    }
    
    /**
     * Inserta un nuevo enemigo en la base de datos.
     */
    public void insertarEnemigo(Personaje enemigo) throws SQLException {
        String query = "INSERT INTO enemigos (nombre, vida, ataque, defensa, velocidad, " +
                      "habilidadEspecial, nivel, estaVivo, energia, vidaMaxima) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, enemigo.nombre);
            ps.setInt(2, enemigo.vida);
            ps.setInt(3, enemigo.ataque);
            ps.setInt(4, enemigo.defensa);
            ps.setInt(5, enemigo.velocidad);
            ps.setInt(6, enemigo.habilidadEspecial);
            ps.setInt(7, enemigo.nivel);
            ps.setBoolean(8, enemigo.estaVivo);
            ps.setInt(9, enemigo.energia);
            ps.setInt(10, enemigo.vidaMaxima);
            
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    enemigo.id = generatedKeys.getInt(1);
                }
            }
        }
    }
    
    /**
     * Registra un combate en la base de datos.
     */
    public void registrarCombate(Personaje jugador, Personaje enemigo, boolean victoria, 
                               int rondas, int danioTotal) throws SQLException {
        String query = "INSERT INTO combates (id_personaje, id_enemigo, resultado, rondas, danio_total) " +
                      "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, jugador.id);
            ps.setInt(2, enemigo.id);
            ps.setString(3, victoria ? "victoria" : "derrota");
            ps.setInt(4, rondas);
            ps.setInt(5, danioTotal);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * Obtiene todos los personajes guardados en la base de datos.
     */
    public List<Personaje> obtenerTodosPersonajes() throws SQLException {
        List<Personaje> personajes = new ArrayList<>();
        String query = "SELECT * FROM personajes ORDER BY fecha_creacion DESC";
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Personaje p = crearPersonajeDesdeResultSet(rs);
                personajes.add(p);
            }
        }
        
        return personajes;
    }
    
    /**
     * Obtiene un personaje por su ID.
     */
    public Personaje obtenerPersonaje(int id) throws SQLException {
        String query = "SELECT * FROM personajes WHERE id = ?";
        Personaje personaje = null;
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    personaje = crearPersonajeDesdeResultSet(rs);
                }
            }
        }
        
        return personaje;
    }
    
    /**
     * Método auxiliar para crear un personaje desde un ResultSet.
     */
    private Personaje crearPersonajeDesdeResultSet(ResultSet rs) throws SQLException {
        String tipo = rs.getString("nombre").toLowerCase();
        Personaje p;
        
        switch(tipo) {
            case "guerrero":
                p = new Guerrero();
                break;
            case "ninja":
                p = new Ninja();
                break;
            case "chaman":
                p = new Chaman();
                break;
            default:
                throw new SQLException("Tipo de personaje desconocido: " + tipo);
        }
        
        p.id = rs.getInt("id");
        p.nombre = rs.getString("nombre");
        p.vida = rs.getInt("vida");
        p.ataque = rs.getInt("ataque");
        p.defensa = rs.getInt("defensa");
        p.velocidad = rs.getInt("velocidad");
        p.habilidadEspecial = rs.getInt("habilidadEspecial");
        p.nivel = rs.getInt("nivel");
        p.estaVivo = rs.getBoolean("estaVivo");
        p.energia = rs.getInt("energia");
        p.vidaMaxima = rs.getInt("vidaMaxima");
        
        return p;
    }
    
    /**
     * Verifica si los enemigos básicos ya existen en la base de datos.
     */
    public boolean enemigosExistenEnDB() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM enemigos WHERE nombre IN ('LoboSalvaje', 'NoMuerto', 'GuerreroOscuro', 'ReyMorgan')";
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt("total") >= 4;
            }
        }
        
        return false;
    }
}