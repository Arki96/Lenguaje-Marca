/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package BBDD;

import java.sql.*;
/**
 *
 * @author Usuario
 * Para que el programa funcione es necesario crear una base de datos en MySQL llamada ZaltorDB que contenga los siguientes atributos:

-- Crear tabla personajes
CREATE TABLE personajes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    vida INT NOT NULL,
    ataque INT NOT NULL,
    defensa INT NOT NULL,
    velocidad INT NOT NULL,
    habilidadEspecial INT NOT NULL,
    nivel INT DEFAULT 0,
    estaVivo TINYINT(1) DEFAULT 1,
    energia INT DEFAULT 50,
    vidaMaxima INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Crear tabla enemigos
CREATE TABLE enemigos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    vida INT NOT NULL,
    ataque INT NOT NULL,
    defensa INT NOT NULL,
    velocidad INT NOT NULL,
    habilidadEspecial INT NOT NULL,
    nivel INT DEFAULT 0,
    estaVivo TINYINT(1) DEFAULT 1,
    energia INT DEFAULT 50,
    vidaMaxima INT NOT NULL
) ENGINE=InnoDB;

-- Crear tabla combates
CREATE TABLE combates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_personaje INT,
    id_enemigo INT,
    resultado VARCHAR(20) NOT NULL,
    rondas INT NOT NULL,
    danio_total INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_personaje) REFERENCES personajes(id),
    FOREIGN KEY (id_enemigo) REFERENCES enemigos(id)
) ENGINE=InnoDB;
 */
public class testConection {
    public static void main(String[] args) {
        try {
            Connection con = ConexionDB.getConnection();
            System.out.println("¡Conexión exitosa!");
            ConexionDB.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
        }
    }
}
