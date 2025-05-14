package com.mycompany.prueba_3t;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MainFrame extends JFrame {

    // Componentes para Clientes (Panel Verde)
    private JPanel panelClientes;
    private JTable tablaClientes;
    private JTextField txtNombre, txtApellidos, txtDni, txtEmail;
    private JButton btnAddCliente, btnUpdateCliente, btnDeleteCliente, btnClearCliente;

    // Componentes para Pedidos (Panel Rojo)
    private JPanel panelPedidos;
    private JTable tablaPedidos;
    private JComboBox<String> comboClientes;
    private JTextField txtProducto, txtCantidad, txtPrecio;
    private JButton btnAddPedido, btnUpdatePedido, btnDeletePedido, btnClearPedido;

    // Modelos de tabla
    private DefaultTableModel modelClientes, modelPedidos;

    public MainFrame() {
        setTitle("Examen - CRUD Completo");
        setSize(900, 700);
        setLayout(new GridLayout(2, 1));

        // Panel Clientes (Verde)
        panelClientes = new JPanel(new BorderLayout());
        panelClientes.setBackground(new Color(200, 255, 200));
        panelClientes.setBorder(BorderFactory.createTitledBorder("Clientes"));
        initPanelClientes();

        // Panel Pedidos (Rojo)
        panelPedidos = new JPanel(new BorderLayout());
        panelPedidos.setBackground(new Color(255, 200, 200));
        panelPedidos.setBorder(BorderFactory.createTitledBorder("Pedidos"));
        initPanelPedidos();

        add(panelClientes);
        add(panelPedidos);

        // Cargar datos iniciales
        loadClientesFromDB();
        loadPedidosFromDB();
    }

    private void initPanelClientes() {
        modelClientes = new DefaultTableModel(new Object[]{"ID", "Nombre", "Apellidos", "DNI", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaClientes = new JTable(modelClientes);

        // Campos de texto
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        txtNombre = new JTextField();
        txtApellidos = new JTextField();
        txtDni = new JTextField();
        txtEmail = new JTextField();

        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(txtNombre);
        inputPanel.add(new JLabel("Apellidos:"));
        inputPanel.add(txtApellidos);
        inputPanel.add(new JLabel("DNI:"));
        inputPanel.add(txtDni);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);

        // Botones CRUD
        JPanel buttonPanel = new JPanel();
        btnAddCliente = new JButton("Agregar");
        btnUpdateCliente = new JButton("Actualizar");
        btnDeleteCliente = new JButton("Eliminar");
        btnClearCliente = new JButton("Limpiar");

        // Asignar acciones
        btnAddCliente.addActionListener(e -> addCliente());
        btnUpdateCliente.addActionListener(e -> updateCliente());
        btnDeleteCliente.addActionListener(e -> deleteCliente());
        btnClearCliente.addActionListener(e -> clearClientFields());

        buttonPanel.add(btnAddCliente);
        buttonPanel.add(btnUpdateCliente);
        buttonPanel.add(btnDeleteCliente);
        buttonPanel.add(btnClearCliente);

        panelClientes.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        panelClientes.add(inputPanel, BorderLayout.NORTH);
        panelClientes.add(buttonPanel, BorderLayout.SOUTH);

        // Selección de fila en la tabla
        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablaClientes.getSelectedRow();
                if (row >= 0) {
                    txtNombre.setText(modelClientes.getValueAt(row, 1).toString());
                    txtApellidos.setText(modelClientes.getValueAt(row, 2).toString());
                    txtDni.setText(modelClientes.getValueAt(row, 3).toString());
                    txtEmail.setText(modelClientes.getValueAt(row, 4).toString());
                }
            }
        });
    }

    private void initPanelPedidos() {
        // Cambiado: Eliminada la columna "Cliente"
        modelPedidos = new DefaultTableModel(new Object[]{"ID", "Producto", "Cantidad", "Precio Unitario", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedidos = new JTable(modelPedidos);

        // ComboBox para clientes
        comboClientes = new JComboBox<>();

        // Campos de texto
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        txtProducto = new JTextField();
        txtCantidad = new JTextField();
        txtPrecio = new JTextField();
        JLabel lblTotal = new JLabel("0.00");

        inputPanel.add(new JLabel("Cliente:"));
        inputPanel.add(comboClientes);
        inputPanel.add(new JLabel("Producto:"));
        inputPanel.add(txtProducto);
        inputPanel.add(new JLabel("Cantidad:"));
        inputPanel.add(txtCantidad);
        inputPanel.add(new JLabel("Precio Unitario:"));
        inputPanel.add(txtPrecio);
        inputPanel.add(new JLabel("Total:"));
        inputPanel.add(lblTotal);

        // Calcular total automáticamente
        KeyAdapter calculator = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int cantidad = Integer.parseInt(txtCantidad.getText());
                    double precio = Double.parseDouble(txtPrecio.getText());
                    lblTotal.setText(String.format("%.2f", cantidad * precio));
                } catch (NumberFormatException ex) {
                    lblTotal.setText("0.00");
                }
            }
        };
        txtCantidad.addKeyListener(calculator);
        txtPrecio.addKeyListener(calculator);

        // Botones CRUD
        JPanel buttonPanel = new JPanel();
        btnAddPedido = new JButton("Agregar");
        btnUpdatePedido = new JButton("Actualizar");
        btnDeletePedido = new JButton("Eliminar");
        btnClearPedido = new JButton("Limpiar");

        // Asignar acciones
        btnAddPedido.addActionListener(e -> addPedido());
        btnUpdatePedido.addActionListener(e -> updatePedido());
        btnDeletePedido.addActionListener(e -> deletePedido());
        btnClearPedido.addActionListener(e -> clearPedidoFields());

        buttonPanel.add(btnAddPedido);
        buttonPanel.add(btnUpdatePedido);
        buttonPanel.add(btnDeletePedido);
        buttonPanel.add(btnClearPedido);

        panelPedidos.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);
        panelPedidos.add(inputPanel, BorderLayout.NORTH);
        panelPedidos.add(buttonPanel, BorderLayout.SOUTH);

        // Selección de fila en la tabla
        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablaPedidos.getSelectedRow();
                if (row >= 0) {
                    // Obtener el ID del pedido seleccionado
                    int pedidoId = (int) modelPedidos.getValueAt(row, 0);
                    
                    // Buscar el cliente asociado a este pedido
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        PreparedStatement pstmt = conn.prepareStatement(
                            "SELECT cliente_id FROM pedidos WHERE id = ?");
                        pstmt.setInt(1, pedidoId);
                        ResultSet rs = pstmt.executeQuery();
                        
                        if (rs.next()) {
                            int clienteId = rs.getInt("cliente_id");
                            // Buscar el cliente en el combo box
                            for (int i = 0; i < comboClientes.getItemCount(); i++) {
                                String item = comboClientes.getItemAt(i);
                                if (item.startsWith(clienteId + " - ")) {
                                    comboClientes.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    
                    txtProducto.setText(modelPedidos.getValueAt(row, 1).toString());
                    txtCantidad.setText(modelPedidos.getValueAt(row, 2).toString());
                    txtPrecio.setText(modelPedidos.getValueAt(row, 3).toString());
                    lblTotal.setText(modelPedidos.getValueAt(row, 4).toString());
                }
            }
        });
    }

    // ==================== MÉTODOS CRUD PARA CLIENTES ====================
    private void addCliente() {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String dni = txtDni.getText().trim();
        String email = txtEmail.getText().trim();

        if (nombre.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO clientes (nombre, apellidos, dni, email) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, dni);
            pstmt.setString(4, email);
            pstmt.executeUpdate();

            loadClientesFromDB();
            clearClientFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar cliente: " + ex.getMessage());
        }
    }

    private void updateCliente() {
        int row = tablaClientes.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para actualizar");
            return;
        }

        int id = (int) modelClientes.getValueAt(row, 0);
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String dni = txtDni.getText().trim();
        String email = txtEmail.getText().trim();

        if (nombre.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE clientes SET nombre=?, apellidos=?, dni=?, email=? WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, dni);
            pstmt.setString(4, email);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();

            loadClientesFromDB();
            clearClientFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar cliente: " + ex.getMessage());
        }
    }

    private void deleteCliente() {
        int row = tablaClientes.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar este cliente? Se eliminarán también sus pedidos.", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modelClientes.getValueAt(row, 0);
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Primero eliminar los pedidos asociados
                String deletePedidos = "DELETE FROM pedidos WHERE cliente_id=?";
                PreparedStatement pstmtPedidos = conn.prepareStatement(deletePedidos);
                pstmtPedidos.setInt(1, id);
                pstmtPedidos.executeUpdate();

                // Luego eliminar el cliente
                String deleteCliente = "DELETE FROM clientes WHERE id=?";
                PreparedStatement pstmtCliente = conn.prepareStatement(deleteCliente);
                pstmtCliente.setInt(1, id);
                pstmtCliente.executeUpdate();

                loadClientesFromDB();
                loadPedidosFromDB();
                clearClientFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar cliente: " + ex.getMessage());
            }
        }
    }

    private void clearClientFields() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        tablaClientes.clearSelection();
    }

    // ==================== MÉTODOS CRUD PARA PEDIDOS ====================
    private void addPedido() {
        if (comboClientes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }

        String producto = txtProducto.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();
        String precioStr = txtPrecio.getText().trim();

        if (producto.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);
            double total = cantidad * precio;

            // Extraer el ID del cliente del texto del combo box
            String clienteSeleccionado = comboClientes.getSelectedItem().toString();
            int clienteId = Integer.parseInt(clienteSeleccionado.split(" - ")[0]);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO pedidos (cliente_id, producto, cantidad, precio_unitario, total) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, clienteId);
                pstmt.setString(2, producto);
                pstmt.setInt(3, cantidad);
                pstmt.setDouble(4, precio);
                pstmt.setDouble(5, total);
                pstmt.executeUpdate();

                loadPedidosFromDB();
                clearPedidoFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar pedido: " + ex.getMessage());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y Precio deben ser números válidos");
        }
    }

    private void updatePedido() {
        int row = tablaPedidos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para actualizar");
            return;
        }

        if (comboClientes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }

        String producto = txtProducto.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();
        String precioStr = txtPrecio.getText().trim();

        if (producto.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);
            double total = cantidad * precio;
            int pedidoId = (int) modelPedidos.getValueAt(row, 0);

            // Extraer el ID del cliente del texto del combo box
            String clienteSeleccionado = comboClientes.getSelectedItem().toString();
            int clienteId = Integer.parseInt(clienteSeleccionado.split(" - ")[0]);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "UPDATE pedidos SET cliente_id=?, producto=?, cantidad=?, precio_unitario=?, total=? WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, clienteId);
                pstmt.setString(2, producto);
                pstmt.setInt(3, cantidad);
                pstmt.setDouble(4, precio);
                pstmt.setDouble(5, total);
                pstmt.setInt(6, pedidoId);
                pstmt.executeUpdate();

                loadPedidosFromDB();
                clearPedidoFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar pedido: " + ex.getMessage());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y Precio deben ser números válidos");
        }
    }

    private void deletePedido() {
        int row = tablaPedidos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar este pedido?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modelPedidos.getValueAt(row, 0);
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM pedidos WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();

                loadPedidosFromDB();
                clearPedidoFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar pedido: " + ex.getMessage());
            }
        }
    }

    private void clearPedidoFields() {
        txtProducto.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        tablaPedidos.clearSelection();
    }

    // ==================== CARGAR DATOS DESDE BD ====================
    private void loadClientesFromDB() {
        modelClientes.setRowCount(0);
        comboClientes.removeAllItems();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nombre, apellidos, dni, email FROM clientes");
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                String dni = rs.getString("dni");
                String email = rs.getString("email");
                
                modelClientes.addRow(new Object[]{id, nombre, apellidos, dni, email});
                comboClientes.addItem(id + " - " + nombre + " " + apellidos);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + ex.getMessage());
        }
    }

    private void loadPedidosFromDB() {
        modelPedidos.setRowCount(0);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT p.id, p.producto, p.cantidad, p.precio_unitario, p.total " +
                "FROM pedidos p"
            );
            
            while (rs.next()) {
                modelPedidos.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("producto"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getDouble("total")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar pedidos: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}