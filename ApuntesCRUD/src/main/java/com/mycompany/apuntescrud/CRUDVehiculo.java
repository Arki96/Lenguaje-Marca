
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.apuntescrud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * CRUD de Vehículos usando Java Swing y MySQL
 */
/**
-- Base de datos
CREATE DATABASE IF NOT EXISTS bdvehiculos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bdvehiculos

-- Tabla vehiculos
CREATE TABLE IF NOT EXISTS vehiculos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    color VARCHAR(30) NOT NULL,
    matricula VARCHAR(20) NOT NULL UNIQUE
);

Pasos para usar:

1. Ejecuta el script SQL en tu servidor MySQL 8.3.0 para crear la base de datos y tabla.
2. Ajusta las credenciales DB_USER y DB_PASSWORD en AplicacionCRUDVehiculos.java.
3. Asegúrate de incluir el driver JDBC de MySQL en el proyecto de NetBeans.
4. Compila y ejecuta la aplicación.
5. Usa la interfaz para crear, leer, actualizar y eliminar vehículos.
*/
public class CRUDVehiculo extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bdvehiculos?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtId, txtMarca, txtModelo, txtAnio, txtColor, txtMatricula;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    private Connection conexion;

    public CRUDVehiculo() {
        inicializarInterfaz();
        conectarBaseDatos();
        cargarVehiculos();
    }

    private void inicializarInterfaz() {
        setTitle("Aplicación CRUD de Vehículos");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Detalles del Vehículo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField(5);
        txtId.setEnabled(false);

        JLabel lblMarca = new JLabel("Marca:");
        txtMarca = new JTextField(15);

        JLabel lblModelo = new JLabel("Modelo:");
        txtModelo = new JTextField(15);

        JLabel lblAnio = new JLabel("Año:");
        txtAnio = new JTextField(6);

        JLabel lblColor = new JLabel("Color:");
        txtColor = new JTextField(10);

        JLabel lblMatricula = new JLabel("Matrícula:");
        txtMatricula = new JTextField(10);

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(lblId, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panelFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(lblMarca, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panelFormulario.add(txtMarca, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(lblModelo, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panelFormulario.add(txtModelo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(lblAnio, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        panelFormulario.add(txtAnio, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(lblColor, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        panelFormulario.add(txtColor, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(lblMatricula, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        panelFormulario.add(txtMatricula, gbc);

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Panel tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Marca", "Modelo", "Año", "Color", "Matrícula"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no se pueden editar directamente
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabla);

        // Listeners
        btnAgregar.addActionListener(e -> agregarVehiculo());
        btnActualizar.addActionListener(e -> actualizarVehiculo());
        btnEliminar.addActionListener(e -> eliminarVehiculo());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        tabla.getSelectionModel().addListSelectionListener(e -> llenarFormularioDesdeTabla());

        // Layout
        add(panelFormulario, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
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

    private void cargarVehiculos() {
        modeloTabla.setRowCount(0);
        String query = "SELECT * FROM vehiculos";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Object[] fila = {
                        rs.getInt("id"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getInt("anio"),
                        rs.getString("color"),
                        rs.getString("matricula")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException ex) {
            mostrarError("Error cargando vehículos: " + ex.getMessage());
        }
    }

    private void agregarVehiculo() {
        if (!validarFormulario()) return;

        String sql = "INSERT INTO vehiculos(marca, modelo, anio, color, matricula) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, txtMarca.getText());
            pstmt.setString(2, txtModelo.getText());
            pstmt.setInt(3, Integer.parseInt(txtAnio.getText()));
            pstmt.setString(4, txtColor.getText());
            pstmt.setString(5, txtMatricula.getText());
            pstmt.executeUpdate();
            mostrarMensaje("Vehículo agregado con éxito.");
            cargarVehiculos();
            limpiarFormulario();
        } catch (SQLException ex) {
            mostrarError("Error agregando vehículo: " + ex.getMessage());
        }
    }

    private void actualizarVehiculo() {
        if (txtId.getText().isEmpty()) {
            mostrarError("Selecciona un vehículo para actualizar.");
            return;
        }
        if (!validarFormulario()) return;

        String sql = "UPDATE vehiculos SET marca=?, modelo=?, anio=?, color=?, matricula=? WHERE id=?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, txtMarca.getText());
            pstmt.setString(2, txtModelo.getText());
            pstmt.setInt(3, Integer.parseInt(txtAnio.getText()));
            pstmt.setString(4, txtColor.getText());
            pstmt.setString(5, txtMatricula.getText());
            pstmt.setInt(6, Integer.parseInt(txtId.getText()));
            pstmt.executeUpdate();
            mostrarMensaje("Vehículo actualizado con éxito.");
            cargarVehiculos();
            limpiarFormulario();
        } catch (SQLException ex) {
            mostrarError("Error actualizando vehículo: " + ex.getMessage());
        }
    }

    private void eliminarVehiculo() {
        if (txtId.getText().isEmpty()) {
            mostrarError("Selecciona un vehículo para eliminar.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este vehículo?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM vehiculos WHERE id=?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(txtId.getText()));
            pstmt.executeUpdate();
            mostrarMensaje("Vehículo eliminado con éxito.");
            cargarVehiculos();
            limpiarFormulario();
        } catch (SQLException ex) {
            mostrarError("Error eliminando vehículo: " + ex.getMessage());
        }
    }

    private void llenarFormularioDesdeTabla() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtMarca.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtModelo.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtAnio.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtColor.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            txtMatricula.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        txtColor.setText("");
        txtMatricula.setText("");
        tabla.clearSelection();
    }

    private boolean validarFormulario() {
        if (txtMarca.getText().trim().isEmpty() ||
            txtModelo.getText().trim().isEmpty() ||
            txtAnio.getText().trim().isEmpty() ||
            txtColor.getText().trim().isEmpty() ||
            txtMatricula.getText().trim().isEmpty()) {
            mostrarError("Por favor, completa todos los campos.");
            return false;
        }

        try {
            int anio = Integer.parseInt(txtAnio.getText());
            if (anio < 1886 || anio > 2100) {
                mostrarError("Introduce un año válido entre 1886 y 2100.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El año debe ser un número.");
            return false;
        }

        return true;
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
      public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            CRUDVehiculo app = new CRUDVehiculo();
            app.setVisible(true);
        });
    }
}