package com.mycompany.crudos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class EstadisticasFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtPersonaje, txtRol, txtVida, txtAtaque, txtDefensa;
    private CRUDOperations crudOperations;

    public EstadisticasFrame(CRUDOperations crudOperations) {
        this.crudOperations = crudOperations;
        inicializarInterfaz();  // Este método ya configura la visibilidad
    }

    private void inicializarInterfaz() {
    setTitle("Aplicación CRUD de estadísticas");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));

    // Inicializar tabla primero
    modeloTabla = new DefaultTableModel(new String[]{"ID", "Personaje", "Rol", "Vida", "Ataque", "Defensa"}, 0) {
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    tabla = new JTable(modeloTabla);
    tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(tabla);

    // Luego inicializar los demás componentes
    JPanel panelFormulario = crearPanelFormulario();
    JPanel panelBotones = crearPanelBotones();

    add(panelFormulario, BorderLayout.WEST);
    add(scrollPane, BorderLayout.CENTER);
    add(panelBotones, BorderLayout.SOUTH);

    crudOperations.cargar(modeloTabla);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
}

    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(5);
        txtId.setEnabled(false);
        txtPersonaje = new JTextField(15);
        txtRol = new JTextField(15);
        txtVida = new JTextField(6);
        txtAtaque = new JTextField(10);
        txtDefensa = new JTextField(10);

        agregarComponente(panelFormulario, gbc, new JLabel("ID:"), 0, 0);
        agregarComponente(panelFormulario, gbc, txtId, 1, 0);
        agregarComponente(panelFormulario, gbc, new JLabel("Personaje:"), 0, 1);
        agregarComponente(panelFormulario, gbc, txtPersonaje, 1, 1);
        agregarComponente(panelFormulario, gbc, new JLabel("Rol:"), 0, 2);
        agregarComponente(panelFormulario, gbc, txtRol, 1, 2);
        agregarComponente(panelFormulario, gbc, new JLabel("Vida:"), 0, 3);
        agregarComponente(panelFormulario, gbc, txtVida, 1, 3);
        agregarComponente(panelFormulario, gbc, new JLabel("Ataque:"), 0, 4);
        agregarComponente(panelFormulario, gbc, txtAtaque, 1, 4);
        agregarComponente(panelFormulario, gbc, new JLabel("Defensa"), 0, 5);
        agregarComponente(panelFormulario, gbc, txtDefensa, 1, 5);

        return panelFormulario;
    }

    private JPanel crearPanelBotones() {
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    JButton btnAgregar = new JButton("Agregar");
    JButton btnActualizar = new JButton("Actualizar");
    JButton btnEliminar = new JButton("Eliminar");
    JButton btnLimpiar = new JButton("Limpiar");

    panelBotones.add(btnAgregar);
    panelBotones.add(btnActualizar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnLimpiar);

    // Listeners
    btnAgregar.addActionListener(e -> agregar());
    btnActualizar.addActionListener(e -> actualizar());
    btnEliminar.addActionListener(e -> eliminar());
    btnLimpiar.addActionListener(e -> limpiar());

    // Mover este listener después de inicializar la tabla
    if (tabla != null) {
        tabla.getSelectionModel().addListSelectionListener(e -> llenarFormularioDesdeTabla());
    }

    return panelBotones;
}

    private void agregarComponente(JPanel panel, GridBagConstraints gbc, Component componente, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(componente, gbc);
    }

    private void agregar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            crudOperations.agregar(
                    txtPersonaje.getText(),
                    txtRol.getText(),
                    Integer.parseInt(txtVida.getText()),
                    txtAtaque.getText(),
                    txtDefensa.getText()
            );
            mostrarMensaje("Agregado con éxito.");
            crudOperations.cargar(modeloTabla);
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

        try {
            crudOperations.actualizar(
                    Integer.parseInt(txtId.getText()),
                    txtPersonaje.getText(),
                    txtRol.getText(),
                    Integer.parseInt(txtVida.getText()),
                    txtAtaque.getText(),
                    txtDefensa.getText()
            );
            mostrarMensaje("Actualizado con éxito.");
            crudOperations.cargar(modeloTabla);
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

        try {
            crudOperations.eliminar(Integer.parseInt(txtId.getText()));
            mostrarMensaje("Eliminado con éxito.");
            crudOperations.cargar(modeloTabla);
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
}
