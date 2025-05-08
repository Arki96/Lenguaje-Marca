package com.mycompany.crudos;

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
 *
 * @author Usuario
 */
    /**
     * -- Base de datos
     * 
      CREATE DATABASE IF NOT EXISTS bdestadisticas CHARACTER
      SET utf8mb4 COLLATE utf8mb4_unicode_ci;
     
      USE bdestadisticas;
     
      -- Tabla vehiculos
       
      CREATE TABLE IF NOT EXISTS estadisticas ( id INT
      AUTO_INCREMENT PRIMARY KEY, personaje VARCHAR(50) NOT NULL, rol
      VARCHAR(50) NOT NULL, vida INT NOT NULL, ataque VARCHAR(30) NOT NULL,
      defensa VARCHAR(20) NOT NULL );
     *
     * Pasos para usar:
     *
     * 1. Ejecuta el script SQL en tu servidor MySQL 8.3.0 para crear la base de
     * datos y tabla. 2. Ajusta las credenciales DB_USER y DB_PASSWORD. 3.
     * Asegúrate de incluir el driver JDBC de MySQL en el proyecto de NetBeans.
     * 4. Compila y ejecuta la aplicación. 5. Puedes usar la interfaz para
     * crear, leer, actualizar y eliminar.
     */
    public class CruDatos extends JFrame {

        private static final String DB_URL = "jdbc:mysql://localhost:3306/bdestadisticas?serverTimezone=UTC";
        private static final String DB_USER = "root";
        private static final String DB_PASSWORD = "";

        private JTable tabla;
        private DefaultTableModel modeloTabla;

        private JTextField txtId, txtPersonaje, txtRol, txtVida, txtAtaque, txtDefensa;
        private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

        private Connection conexion;

        public CruDatos() {
            inicializarInterfaz();
            conectarBaseDatos();
            cargar();
        }

        private void inicializarInterfaz() {
            setTitle("Aplicación CRUD de estadísticas");
            setSize(700, 450);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout(10, 10));

            // Panel formulario
            JPanel panelFormulario = new JPanel(new GridBagLayout());
            panelFormulario.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            JLabel lblId = new JLabel("ID:");
            txtId = new JTextField(5);
            txtId.setEnabled(false);

            JLabel lblPersonaje = new JLabel("Personaje:");
            txtPersonaje = new JTextField(15);

            JLabel lblRol = new JLabel("Rol:");
            txtRol = new JTextField(15);

            JLabel lblVida = new JLabel("Vida:");
            txtVida = new JTextField(6);

            JLabel lblAtaque = new JLabel("Ataque:");
            txtAtaque = new JTextField(10);

            JLabel lblDefensa = new JLabel("Defensa");
            txtDefensa = new JTextField(10);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panelFormulario.add(lblId, gbc);
            gbc.gridx = 1;
            gbc.gridy = 0;
            panelFormulario.add(txtId, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panelFormulario.add(lblPersonaje, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
            panelFormulario.add(txtPersonaje, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            panelFormulario.add(lblRol, gbc);
            gbc.gridx = 1;
            gbc.gridy = 2;
            panelFormulario.add(txtRol, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            panelFormulario.add(lblVida, gbc);
            gbc.gridx = 1;
            gbc.gridy = 3;
            panelFormulario.add(txtVida, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            panelFormulario.add(lblAtaque, gbc);
            gbc.gridx = 1;
            gbc.gridy = 4;
            panelFormulario.add(txtAtaque, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            panelFormulario.add(lblDefensa, gbc);
            gbc.gridx = 1;
            gbc.gridy = 5;
            panelFormulario.add(txtDefensa, gbc);

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
            modeloTabla = new DefaultTableModel(new String[]{"ID", "Personaje", "Rol", "Vida", "Ataque", "Defensa"}, 0) {
                public boolean isCellEditable(int row, int column) {
                    return false; // Las celdas no se pueden editar directamente
                }
            };
            tabla = new JTable(modeloTabla);
            tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(tabla);

            // Listeners
            btnAgregar.addActionListener(e -> agregar());
            btnActualizar.addActionListener(e -> actualizar());
            btnEliminar.addActionListener(e -> eliminar());
            btnLimpiar.addActionListener(e -> limpiar());
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

        private void cargar() {
            modeloTabla.setRowCount(0);
            String query = "SELECT * FROM estadisticas";
            try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
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

        private void agregar() {
            if (!validarFormulario()) {
                return;
            }

            String sql = "INSERT INTO estadisticas(personaje, rol, vida, ataque, defensa) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, txtPersonaje.getText());
                pstmt.setString(2, txtRol.getText());
                pstmt.setInt(3, Integer.parseInt(txtVida.getText()));
                pstmt.setString(4, txtAtaque.getText());
                pstmt.setString(5, txtDefensa.getText());
                pstmt.executeUpdate();
                mostrarMensaje("Agregado con éxito.");
                cargar();
                limpiar();
            } catch (SQLException ex) {
                mostrarError("Error agregando: " + ex.getMessage());
            }
        }

        private void actualizar() {
            if (txtId.getText().isEmpty()) {
                mostrarError("Selecciona para actualizar.");
                return;
            }
            if (!validarFormulario()) {
                return;
            }

            String sql = "UPDATE estadisticas SET personaje=?, rol=?, vida=?, ataque=?, defensa? WHERE id=?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, txtPersonaje.getText());
                pstmt.setString(2, txtRol.getText());
                pstmt.setInt(3, Integer.parseInt(txtVida.getText()));
                pstmt.setString(4, txtAtaque.getText());
                pstmt.setString(5, txtDefensa.getText());
                pstmt.setInt(6, Integer.parseInt(txtId.getText()));
                pstmt.executeUpdate();
                mostrarMensaje("Actualizado con éxito.");
                cargar();
                limpiar();
            } catch (SQLException ex) {
                mostrarError("Error actualizando: " + ex.getMessage());
            }
        }

        private void eliminar() {
            if (txtId.getText().isEmpty()) {
                mostrarError("Selecciona para eliminar.");
                return;
            }

            int confirmar = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            String sql = "DELETE FROM estadisticas WHERE id=?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(txtId.getText()));
                pstmt.executeUpdate();
                mostrarMensaje("Eliminado con éxito.");
                cargar();
                limpiar();
            } catch (SQLException ex) {
                mostrarError("Error eliminando: " + ex.getMessage());
            }
        }

        private void llenarFormularioDesdeTabla() {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada >= 0) {
                txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
                txtPersonaje.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
                txtRol.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
                txtVida.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
                txtAtaque.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
                txtDefensa.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
            }
        }

        private void limpiar() {
            txtId.setText("");
            txtPersonaje.setText("");
            txtRol.setText("");
            txtVida.setText("");
            txtAtaque.setText("");
            txtDefensa.setText("");
            tabla.clearSelection();
        }

        private boolean validarFormulario() {
            if (txtPersonaje.getText().trim().isEmpty()
                    || txtRol.getText().trim().isEmpty()
                    || txtVida.getText().trim().isEmpty()
                    || txtAtaque.getText().trim().isEmpty()
                    || txtDefensa.getText().trim().isEmpty()) {
                mostrarError("Por favor, completa todos los campos.");
                return false;
            }

            try {
                int vida = Integer.parseInt(txtVida.getText());
                if (vida < 0 || vida > 1000) {
                    mostrarError("La vida no puede ser mayor a 1000");
                    return false;
                }
            } catch (NumberFormatException e) {
                mostrarError("Debes de escribir números.");
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
            } catch (Exception ignored) {
            }

            SwingUtilities.invokeLater(() -> {
                CruDatos app = new CruDatos();
                app.setVisible(true);
            });
        }
    }

