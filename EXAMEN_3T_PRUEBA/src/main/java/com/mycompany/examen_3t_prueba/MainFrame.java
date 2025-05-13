/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.examen_3t_prueba;

/**
 *
 * @author Usuario
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class MainFrame extends JFrame {

    // Clientes
    private JTextField txtClientName, txtClientEmail;
    private JButton btnAddClient, btnUpdateClient, btnDeleteClient, btnClearClient;
    private JTable tableClients;
    private DefaultTableModel modelClients;
    private TableRowSorter<DefaultTableModel> sorterClients;
    private JTextField txtSearchClient;

    // Pedidos
    private JComboBox<ClientItem> comboClientOrders;
    private JTextField txtOrderProduct;
    private JButton btnAddOrder, btnUpdateOrder, btnDeleteOrder, btnClearOrder;
    private JTable tableOrders;
    private DefaultTableModel modelOrders;
    private TableRowSorter<DefaultTableModel> sorterOrders;
    private JLabel lblStatus;

    public MainFrame() {
        setTitle("Gestión Clientes y Pedidos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10,10,10,10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);

        // Panel Clientes
        JPanel panelClients = new JPanel();
        panelClients.setBackground(new Color(220, 255, 220)); // verde claro
        panelClients.setLayout(new BorderLayout(15,15));
        panelClients.setBorder(BorderFactory.createTitledBorder("Clientes"));

        // Input Clientes
        JPanel panelClientInputs = new JPanel(new GridBagLayout());
        panelClientInputs.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelClientInputs.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtClientName = new JTextField(20);
        panelClientInputs.add(txtClientName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelClientInputs.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtClientEmail = new JTextField(20);
        panelClientInputs.add(txtClientEmail, gbc);

        // Buscador Clientes con etiqueta "Buscar:"
        JPanel panelSearch = new JPanel(new BorderLayout(5,0));
        panelSearch.setOpaque(false);
        JLabel labelBuscar = new JLabel("Buscar:");
        labelBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelSearch.add(labelBuscar, BorderLayout.WEST);
        txtSearchClient = new JTextField();
        txtSearchClient.setToolTipText("Buscar cliente...");
        panelSearch.add(txtSearchClient, BorderLayout.CENTER);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelClientInputs.add(panelSearch, gbc);

        // Botones Clientes con texto legible y sin redondear
        JPanel panelClientButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        btnAddClient = new JButton("Agregar");
        btnUpdateClient = new JButton("Actualizar");
        btnDeleteClient = new JButton("Eliminar");
        btnClearClient = new JButton("Limpiar");

        panelClientButtons.add(btnAddClient);
        panelClientButtons.add(btnUpdateClient);
        panelClientButtons.add(btnDeleteClient);
        panelClientButtons.add(btnClearClient);

        JPanel northClients = new JPanel(new BorderLayout(10,10));
        northClients.setOpaque(false);
        northClients.add(panelClientInputs, BorderLayout.CENTER);
        northClients.add(panelClientButtons, BorderLayout.SOUTH);

        modelClients = new DefaultTableModel(new String[]{"ID", "Nombre", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        tableClients = new JTable(modelClients);
        tableClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorterClients = new TableRowSorter<>(modelClients);
        tableClients.setRowSorter(sorterClients);
        JScrollPane scrollClients = new JScrollPane(tableClients);

        panelClients.add(northClients, BorderLayout.NORTH);
        panelClients.add(scrollClients, BorderLayout.CENTER);

        // Panel Pedidos
        JPanel panelOrders = new JPanel();
        panelOrders.setBackground(new Color(255, 220, 220)); // rojo claro
        panelOrders.setLayout(new BorderLayout(15,15));
        panelOrders.setBorder(BorderFactory.createTitledBorder("Pedidos"));

        // Inputs Pedidos
        JPanel panelOrderInputs = new JPanel(new GridBagLayout());
        panelOrderInputs.setOpaque(false);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelOrderInputs.add(new JLabel("Cliente:"), gbc);

        gbc.gridx = 1;
        comboClientOrders = new JComboBox<>();
        panelOrderInputs.add(comboClientOrders, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelOrderInputs.add(new JLabel("Producto:"), gbc);

        gbc.gridx = 1;
        txtOrderProduct = new JTextField(20);
        panelOrderInputs.add(txtOrderProduct, gbc);

        // Botones Pedidos con texto legible y sin redondear
        JPanel panelOrderButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        btnAddOrder = new JButton("Agregar");
        btnUpdateOrder = new JButton("Actualizar");
        btnDeleteOrder = new JButton("Eliminar");
        btnClearOrder = new JButton("Limpiar");

        panelOrderButtons.add(btnAddOrder);
        panelOrderButtons.add(btnUpdateOrder);
        panelOrderButtons.add(btnDeleteOrder);
        panelOrderButtons.add(btnClearOrder);

        JPanel northOrders = new JPanel(new BorderLayout(10,10));
        northOrders.setOpaque(false);
        northOrders.add(panelOrderInputs, BorderLayout.CENTER);
        northOrders.add(panelOrderButtons, BorderLayout.SOUTH);

        modelOrders = new DefaultTableModel(new String[]{"ID", "Cliente ID", "Cliente Nombre", "Producto"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        tableOrders = new JTable(modelOrders);
        tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorterOrders = new TableRowSorter<>(modelOrders);
        tableOrders.setRowSorter(sorterOrders);
        JScrollPane scrollOrders = new JScrollPane(tableOrders);

        panelOrders.add(northOrders, BorderLayout.NORTH);
        panelOrders.add(scrollOrders, BorderLayout.CENTER);

        // Estado
        lblStatus = new JLabel(" ");
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        add(lblStatus, BorderLayout.SOUTH);

        splitPane.setLeftComponent(panelClients);
        splitPane.setRightComponent(panelOrders);
        add(splitPane, BorderLayout.CENTER);

        // Eventos
        btnAddClient.addActionListener(e -> addClient());
        btnUpdateClient.addActionListener(e -> updateClient());
        btnDeleteClient.addActionListener(e -> deleteClient());
        btnClearClient.addActionListener(e -> clearClientFields());

        btnAddOrder.addActionListener(e -> addOrder());
        btnUpdateOrder.addActionListener(e -> updateOrder());
        btnDeleteOrder.addActionListener(e -> deleteOrder());
        btnClearOrder.addActionListener(e -> clearOrderFields());

        tableClients.getSelectionModel().addListSelectionListener(e -> loadClientSelection());
        tableOrders.getSelectionModel().addListSelectionListener(e -> loadOrderSelection());

        txtSearchClient.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = txtSearchClient.getText();
                if(text.trim().length() == 0) {
                    sorterClients.setRowFilter(null);
                } else {
                    sorterClients.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        // Carga inicial
        loadClientsFromDB();
        loadOrdersFromDB();
    }

    // Para mostrar clientes en ComboBox en pedidos
    private void refreshClientCombo() {
        comboClientOrders.removeAllItems();
        for(int i = 0; i < modelClients.getRowCount(); i++) {
            int id = (int) modelClients.getValueAt(i, 0);
            String name = (String) modelClients.getValueAt(i, 1);
            comboClientOrders.addItem(new ClientItem(id, name));
        }
        comboClientOrders.setSelectedIndex(-1);
    }

    // Clientes CRUD y métodos auxiliares

    private void addClient() {
        String name = txtClientName.getText().trim();
        String email = txtClientEmail.getText().trim();
        if(name.isEmpty() || email.isEmpty()) {
            setStatus("Complete todos los campos de Cliente.");
            return;
        }
        try(Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO clients (name, email) VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
            setStatus("Cliente agregado.");
            clearClientFields();
            loadClientsFromDB();
        } catch(SQLException ex) {
            ex.printStackTrace();
            setStatus("Error al agregar cliente: " + ex.getMessage());
        }
    }

    private void updateClient() {
        int selectedRow = tableClients.getSelectedRow();
        if(selectedRow == -1) {
            setStatus("Seleccione un cliente para actualizar.");
            return;
        }
        int id = (int) modelClients.getValueAt(selectedRow,0);
        String name = txtClientName.getText().trim();
        String email = txtClientEmail.getText().trim();
        if(name.isEmpty() || email.isEmpty()) {
            setStatus("Complete todos los campos de Cliente.");
            return;
        }
        try(Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE clients SET name=?, email=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, id);
            ps.executeUpdate();
            setStatus("Cliente actualizado.");
            clearClientFields();
            loadClientsFromDB();
            loadOrdersFromDB(); // Para refrescar nombres clientes en pedidos
        } catch(SQLException ex) {
            ex.printStackTrace();
            setStatus("Error al actualizar cliente: " + ex.getMessage());
        }
    }

    private void deleteClient() {
        int selectedRow = tableClients.getSelectedRow();
        if(selectedRow == -1) {
            setStatus("Seleccione un cliente para eliminar.");
            return;
        }
        int id = (int) modelClients.getValueAt(selectedRow,0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el cliente? Se eliminarán sus pedidos también.", "Confirmar", JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;
        try(Connection conn = DatabaseConnection.getConnection()) {
            String sqlDeleteOrders = "DELETE FROM orders WHERE client_id=?";
            PreparedStatement psOrders = conn.prepareStatement(sqlDeleteOrders);
            psOrders.setInt(1, id);
            psOrders.executeUpdate();

            String sql = "DELETE FROM clients WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            setStatus("Cliente eliminado.");
            clearClientFields();
            loadClientsFromDB();
            loadOrdersFromDB();
        } catch(SQLException ex) {
            ex.printStackTrace();
            setStatus("Error al eliminar cliente: " + ex.getMessage());
        }
    }

    private void clearClientFields() {
        txtClientName.setText("");
        txtClientEmail.setText("");
        tableClients.clearSelection();
        setStatus("Campos cliente limpios.");
    }

    private void loadClientsFromDB() {
        modelClients.setRowCount(0);
        try(Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM clients";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("email"));
                modelClients.addRow(row);
            }
            refreshClientCombo();
        } catch(SQLException ex) {
            ex.printStackTrace();
            setStatus("Error al cargar clientes: " + ex.getMessage());
        }
    }

    private void loadClientSelection() {
        int selectedRow = tableClients.getSelectedRow();
        if(selectedRow != -1) {
            txtClientName.setText((String)modelClients.getValueAt(selectedRow, 1));
            txtClientEmail.setText((String)modelClients.getValueAt(selectedRow, 2));
        }
    }

    // Pedidos CRUD y métodos auxiliares

    private void addOrder() {
        String product = txtOrderProduct.getText().trim();
        ClientItem selectedClient = (ClientItem)comboClientOrders.getSelectedItem();

        if(product.isEmpty()) {
            setStatus("Ingrese el producto del pedido.");
            return;
        }
        if(selectedClient == null) {
            setStatus("Seleccione un cliente para el pedido.");
            return;
        }
        int clientId = selectedClient.getId();

        try(Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO orders (client_id, product) VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, clientId);
            ps.setString(2, product);
            ps.executeUpdate();
            setStatus("Pedido agregado.");
            clearOrderFields();
            loadOrdersFromDB();
        } catch(SQLException ex) {
            ex.printStackTrace();
            setStatus("Error al agregar pedido: " + ex.getMessage());
        }
    }

    private void updateOrder() {
        int selectedRow = tableOrders.getSelectedRow();
        if(selectedRow == -1) {
            setStatus("Seleccione un pedido para actualizar.");
            return;
        }
        int id = (int) modelOrders.getValueAt(selectedRow, 0);
        String product = txtOrderProduct.getText().trim();
        ClientItem selectedClient = (ClientItem)comboClientOrders.getSelectedItem();

        if(product.isEmpty()) {
            setStatus("Ingrese el producto del pedido.");
            return;
        }
        if(selectedClient == null) {
            setStatus("Seleccione un cliente para el pedido.");
            return;
        }
        int clientId = selectedClient.getId();

        try(Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE orders SET client_id=?, product=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, clientId);
            ps.setString(2, product);
            ps.setInt(3, id);
            ps.executeUpdate();
            setStatus("Pedido actualizado.");
            clearOrderFields();
            loadOrdersFromDB();
        } catch(SQLException ex) {
            ex.printStackTrace();
            setStatus("Error al actualizar pedido: " + ex.getMessage());
        }
    }

    private void deleteOrder() {
        int selectedRow = tableOrders.getSelectedRow();
        if(selectedRow == -1) {
            setStatus("Seleccione un pedido para eliminar.");
            return;
        }
        int id = (int) modelOrders.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el pedido?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        try(Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM orders WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            setStatus("Pedido eliminado.");
            clearOrderFields();
            loadOrdersFromDB();
        } catch(SQLException ex) {
            ex.printStackTrace();
            setStatus("Error al eliminar pedido: " + ex.getMessage());
        }
    }

    private void clearOrderFields() {
        txtOrderProduct.setText("");
        comboClientOrders.setSelectedIndex(-1);
        tableOrders.clearSelection();
        setStatus("Campos pedido limpios.");
    }

    private void loadOrdersFromDB() {
        modelOrders.setRowCount(0);
        try(Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT o.id, o.client_id, c.name, o.product FROM orders o JOIN clients c ON o.client_id = c.id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getInt("client_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("product"));
                modelOrders.addRow(row);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            setStatus("Error al cargar pedidos: " + ex.getMessage());
        }
    }

    private void loadOrderSelection() {
        int selectedRow = tableOrders.getSelectedRow();
        if(selectedRow != -1) {
            txtOrderProduct.setText((String) modelOrders.getValueAt(selectedRow, 3));
            int clientId = (int) modelOrders.getValueAt(selectedRow, 1);
            for(int i=0; i < comboClientOrders.getItemCount(); i++) {
                if(comboClientOrders.getItemAt(i).getId() == clientId) {
                    comboClientOrders.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void setStatus(String msg) {
        lblStatus.setText(msg);
        Timer timer = new Timer(4000, e -> lblStatus.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }

    // Clase interna para combo clientes
    private static class ClientItem {
        private int id;
        private String name;
        public ClientItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {return id;}
        public String toString() {return name;}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}





