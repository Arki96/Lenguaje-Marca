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

    private JTextField txtClientName, txtClientApellidos, txtClientDireccion, txtClientDNI, txtClientEmail;
    private JButton btnAddClient, btnUpdateClient, btnDeleteClient, btnClearClient;
    private JTable tableClients;
    private DefaultTableModel modelClients;
    private TableRowSorter<DefaultTableModel> sorterClients;
    private JTextField txtSearchClient;

    private JComboBox<ClientItem> comboClientOrders;
    private JTextField txtOrderProductName, txtOrderQuantity, txtOrderUnitPrice, txtOrderTotalPrice;
    private JButton btnAddOrder, btnUpdateOrder, btnDeleteOrder, btnClearOrder;
    private JTable tableOrders;
    private DefaultTableModel modelOrders;
    private TableRowSorter<DefaultTableModel> sorterOrders;
    private JLabel lblStatus;

    private JTextArea logArea;

    public MainFrame() {
        setTitle("Gestión Clientes y Pedidos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10,10,10,10));

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(520);

        JSplitPane topSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topSplitPane.setDividerLocation(550);

        // Panel Clientes
        JPanel panelClients = new JPanel(new BorderLayout(15,15));
        panelClients.setBackground(new Color(220, 255, 220));
        panelClients.setBorder(BorderFactory.createTitledBorder("Clientes"));

        JPanel panelClientInputs = new JPanel(new GridBagLayout());
        panelClientInputs.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelClientInputs.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtClientName = new JTextField(15);
        panelClientInputs.add(txtClientName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelClientInputs.add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        txtClientApellidos = new JTextField(15);
        panelClientInputs.add(txtClientApellidos, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelClientInputs.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtClientDireccion = new JTextField(15);
        panelClientInputs.add(txtClientDireccion, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelClientInputs.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1;
        txtClientDNI = new JTextField(15);
        panelClientInputs.add(txtClientDNI, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelClientInputs.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtClientEmail = new JTextField(15);
        panelClientInputs.add(txtClientEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel panelSearch = new JPanel(new BorderLayout(5,0));
        panelSearch.setOpaque(false);
        JLabel labelBuscar = new JLabel("Buscar:");
        labelBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelSearch.add(labelBuscar, BorderLayout.WEST);
        txtSearchClient = new JTextField();
        txtSearchClient.setToolTipText("Buscar cliente...");
        panelSearch.add(txtSearchClient, BorderLayout.CENTER);
        panelClientInputs.add(panelSearch, gbc);

        JPanel panelClientButtons = new JPanel(new FlowLayout(FlowLayout.CENTER,12,6));
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

        modelClients = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Apellidos", "Dirección", "DNI", "Email"}, 0){
            @Override public boolean isCellEditable(int row, int column){return false;}
        };
        tableClients = new JTable(modelClients);
        tableClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorterClients = new TableRowSorter<>(modelClients);
        tableClients.setRowSorter(sorterClients);
        JScrollPane scrollClients = new JScrollPane(tableClients);

        panelClients.add(northClients, BorderLayout.NORTH);
        panelClients.add(scrollClients, BorderLayout.CENTER);

        // Panel Pedidos
        JPanel panelOrders = new JPanel(new BorderLayout(15,15));
        panelOrders.setBackground(new Color(255,220,220));
        panelOrders.setBorder(BorderFactory.createTitledBorder("Pedidos"));

        JPanel panelOrderInputs = new JPanel(new GridBagLayout());
        panelOrderInputs.setOpaque(false);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0;
        panelOrderInputs.add(new JLabel("Cliente:"), gbc);
        gbc.gridx=1;
        comboClientOrders = new JComboBox<>();
        panelOrderInputs.add(comboClientOrders, gbc);

        gbc.gridx=0; gbc.gridy=1;
        panelOrderInputs.add(new JLabel("Producto:"), gbc);
        gbc.gridx=1;
        txtOrderProductName = new JTextField(15);
        panelOrderInputs.add(txtOrderProductName, gbc);

        gbc.gridx=0; gbc.gridy=2;
        panelOrderInputs.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx=1;
        txtOrderQuantity = new JTextField(15);
        panelOrderInputs.add(txtOrderQuantity, gbc);

        gbc.gridx=0; gbc.gridy=3;
        panelOrderInputs.add(new JLabel("Precio Unitario:"), gbc);
        gbc.gridx=1;
        txtOrderUnitPrice = new JTextField(15);
        panelOrderInputs.add(txtOrderUnitPrice, gbc);

        gbc.gridx=0; gbc.gridy=4;
        panelOrderInputs.add(new JLabel("Precio Total:"), gbc);
        gbc.gridx=1;
        txtOrderTotalPrice = new JTextField(15);
        txtOrderTotalPrice.setEditable(false);
        txtOrderTotalPrice.setBackground(Color.LIGHT_GRAY);
        panelOrderInputs.add(txtOrderTotalPrice, gbc);

        KeyAdapter totalCalculator = new KeyAdapter(){
            @Override public void keyReleased(KeyEvent e){
                try{
                    int q = Integer.parseInt(txtOrderQuantity.getText().trim());
                    double u = Double.parseDouble(txtOrderUnitPrice.getText().trim());
                    txtOrderTotalPrice.setText(String.format("%.2f", q*u));
                }catch(NumberFormatException ex){
                    txtOrderTotalPrice.setText("");
                }
            }
        };
        txtOrderQuantity.addKeyListener(totalCalculator);
        txtOrderUnitPrice.addKeyListener(totalCalculator);

        JPanel panelOrderButtons = new JPanel(new FlowLayout(FlowLayout.CENTER,12,6));
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

        modelOrders = new DefaultTableModel(
                new Object[]{"ID", "Cliente ID", "Cliente Nombre", "Producto", "Cantidad", "Precio Unitario", "Precio Total"},0){
            @Override public boolean isCellEditable(int row, int column){return false;}
        };
        tableOrders = new JTable(modelOrders);
        tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorterOrders = new TableRowSorter<>(modelOrders);
        tableOrders.setRowSorter(sorterOrders);
        JScrollPane scrollOrders = new JScrollPane(tableOrders);

        panelOrders.add(northOrders, BorderLayout.NORTH);
        panelOrders.add(scrollOrders, BorderLayout.CENTER);

        logArea = new JTextArea(6, 100);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Registro de acciones"));

        topSplitPane.setLeftComponent(panelClients);
        topSplitPane.setRightComponent(panelOrders);

        mainSplitPane.setTopComponent(topSplitPane);
        mainSplitPane.setBottomComponent(logScrollPane);

        add(mainSplitPane, BorderLayout.CENTER);

        // Eventos clientes
        btnAddClient.addActionListener(e -> addClient());
        btnUpdateClient.addActionListener(e -> updateClient());
        btnDeleteClient.addActionListener(e -> deleteClient());
        btnClearClient.addActionListener(e -> {
            clearClientFields();
            log("Campos cliente limpiados.");
        });

        // Eventos pedidos
        btnAddOrder.addActionListener(e -> addOrder());
        btnUpdateOrder.addActionListener(e -> updateOrder());
        btnDeleteOrder.addActionListener(e -> deleteOrder());
        btnClearOrder.addActionListener(e -> {
            clearOrderFields();
            log("Campos pedido limpiados.");
        });

        tableClients.getSelectionModel().addListSelectionListener(e -> loadClientSelection());
        tableOrders.getSelectionModel().addListSelectionListener(e -> loadOrderSelection());

        txtSearchClient.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            private void filter(){
                String text = txtSearchClient.getText();
                if(text.trim().length()==0) sorterClients.setRowFilter(null);
                else sorterClients.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e){filter();}
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e){filter();}
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e){filter();}
        });

        loadClientsFromDB();
        loadOrdersFromDB();
    }

    private void log(String message){
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void addClient(){
        String nombre = txtClientName.getText().trim();
        String apellidos = txtClientApellidos.getText().trim();
        String direccion = txtClientDireccion.getText().trim();
        String dni = txtClientDNI.getText().trim();
        String email = txtClientEmail.getText().trim();

        if(nombre.isEmpty() || apellidos.isEmpty() || direccion.isEmpty() || dni.isEmpty() || email.isEmpty()){
            setStatus("Complete todos los campos de Cliente.");
            return;
        }

        try(Connection conn = DatabaseConnection.getConnection()){
            String sql = "INSERT INTO clients (nombre, apellidos, direccion, dni, email) VALUES (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, direccion);
            ps.setString(4, dni);
            ps.setString(5, email);
            ps.executeUpdate();
            setStatus("Cliente agregado.");
            log("Cliente agregado: " + nombre + " " + apellidos);
            clearClientFields();
            loadClientsFromDB();
        }catch(SQLException ex){
            ex.printStackTrace();
            setStatus("Error al agregar cliente: " + ex.getMessage());
            log("Error al agregar cliente: " + ex.getMessage());
        }
    }

    private void updateClient(){
        int selectedRow = tableClients.getSelectedRow();
        if(selectedRow == -1){
            setStatus("Seleccione un cliente para actualizar.");
            return;
        }
        int id = (int) modelClients.getValueAt(selectedRow, 0);
        String nombre = txtClientName.getText().trim();
        String apellidos = txtClientApellidos.getText().trim();
        String direccion = txtClientDireccion.getText().trim();
        String dni = txtClientDNI.getText().trim();
        String email = txtClientEmail.getText().trim();

        if(nombre.isEmpty() || apellidos.isEmpty() || direccion.isEmpty() || dni.isEmpty() || email.isEmpty()){
            setStatus("Complete todos los campos de Cliente.");
            return;
        }

        try(Connection conn = DatabaseConnection.getConnection()){
            String sql = "UPDATE clients SET nombre=?, apellidos=?, direccion=?, dni=?, email=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, direccion);
            ps.setString(4, dni);
            ps.setString(5, email);
            ps.setInt(6, id);
            ps.executeUpdate();
            setStatus("Cliente actualizado.");
            log("Cliente actualizado (ID "+id+"): "+nombre+" "+apellidos);
            clearClientFields();
            loadClientsFromDB();
            loadOrdersFromDB();
        }catch(SQLException ex){
            ex.printStackTrace();
            setStatus("Error al actualizar cliente: "+ex.getMessage());
            log("Error al actualizar cliente: "+ex.getMessage());
        }
    }

    private void deleteClient(){
        int selectedRow = tableClients.getSelectedRow();
        if(selectedRow == -1){
            setStatus("Seleccione un cliente para eliminar.");
            return;
        }
        int id = (int) modelClients.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el cliente? Se eliminarán sus pedidos también.", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        try(Connection conn = DatabaseConnection.getConnection()){
            String sqlDeleteOrders = "DELETE FROM orders WHERE client_id=?";
            PreparedStatement psOrders = conn.prepareStatement(sqlDeleteOrders);
            psOrders.setInt(1, id);
            psOrders.executeUpdate();

            String sql = "DELETE FROM clients WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            setStatus("Cliente eliminado.");
            log("Cliente eliminado (ID "+id+")");
            clearClientFields();
            loadClientsFromDB();
            loadOrdersFromDB();
        }catch(SQLException ex){
            ex.printStackTrace();
            setStatus("Error al eliminar cliente: "+ex.getMessage());
            log("Error al eliminar cliente: "+ex.getMessage());
        }
    }

    private void clearClientFields(){
        txtClientName.setText("");
        txtClientApellidos.setText("");
        txtClientDireccion.setText("");
        txtClientDNI.setText("");
        txtClientEmail.setText("");
        tableClients.clearSelection();
        setStatus("Campos cliente limpios.");
    }

    private void loadClientsFromDB(){
        modelClients.setRowCount(0);
        try(Connection conn = DatabaseConnection.getConnection()){
            String sql = "SELECT * FROM clients";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("nombre"));
                row.add(rs.getString("apellidos"));
                row.add(rs.getString("direccion"));
                row.add(rs.getString("dni"));
                row.add(rs.getString("email"));
                modelClients.addRow(row);
            }
            refreshClientCombo();
        }catch(SQLException ex){
            ex.printStackTrace();
            setStatus("Error al cargar clientes: "+ex.getMessage());
            log("Error al cargar clientes: "+ex.getMessage());
        }
    }

    private void loadClientSelection(){
        int selectedRow = tableClients.getSelectedRow();
        if(selectedRow != -1){
            txtClientName.setText((String)modelClients.getValueAt(selectedRow,1));
            txtClientApellidos.setText((String)modelClients.getValueAt(selectedRow,2));
            txtClientDireccion.setText((String)modelClients.getValueAt(selectedRow,3));
            txtClientDNI.setText((String)modelClients.getValueAt(selectedRow,4));
            txtClientEmail.setText((String)modelClients.getValueAt(selectedRow,5));
        }
    }

    private void refreshClientCombo(){
        comboClientOrders.removeAllItems();
        for(int i=0; i<modelClients.getRowCount(); i++){
            int id = ((Number)modelClients.getValueAt(i,0)).intValue();
            String firstName = modelClients.getValueAt(i,1) != null ? modelClients.getValueAt(i,1).toString() : "";
            String lastName = modelClients.getValueAt(i,2) != null ? modelClients.getValueAt(i,2).toString() : "";
            String fullName = firstName + (lastName.isEmpty() ? "" : " "+lastName);
            comboClientOrders.addItem(new ClientItem(id, fullName));
        }
        comboClientOrders.setSelectedIndex(-1);
    }

    private void addOrder(){
        String productName = txtOrderProductName.getText().trim();
        String quantityStr = txtOrderQuantity.getText().trim();
        String unitPriceStr = txtOrderUnitPrice.getText().trim();
        ClientItem selectedClient = (ClientItem)comboClientOrders.getSelectedItem();

        if(productName.isEmpty() || quantityStr.isEmpty() || unitPriceStr.isEmpty()){
            setStatus("Complete todos los campos de Pedido.");
            return;
        }
        if(selectedClient == null){
            setStatus("Seleccione un cliente para el pedido.");
            return;
        }
        int clientId = selectedClient.getId();

        try{
            int quantity = Integer.parseInt(quantityStr);
            double unitPrice = Double.parseDouble(unitPriceStr);
            double totalPrice = quantity * unitPrice;

            try(Connection conn = DatabaseConnection.getConnection()){
                String sql = "INSERT INTO orders (client_id, product_name, quantity, unit_price, total_price) VALUES (?,?,?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, clientId);
                ps.setString(2, productName);
                ps.setInt(3, quantity);
                ps.setDouble(4, unitPrice);
                ps.setDouble(5, totalPrice);
                ps.executeUpdate();
                setStatus("Pedido agregado.");
                log("Pedido agregado: Cliente ID "+clientId+", Producto "+productName+", Cantidad "+quantity+", Precio Unitario "+unitPrice);
                clearOrderFields();
                loadOrdersFromDB();
            }catch(SQLException ex){
                ex.printStackTrace();
                setStatus("Error al agregar pedido: "+ex.getMessage());
                log("Error al agregar pedido: "+ex.getMessage());
            }
        }catch(NumberFormatException ex){
            setStatus("Cantidad y Precio Unitario deben ser números válidos.");
        }
    }

    private void updateOrder(){
        int selectedRow = tableOrders.getSelectedRow();
        if(selectedRow == -1){
            setStatus("Seleccione un pedido para actualizar.");
            return;
        }
        int id = (int)modelOrders.getValueAt(selectedRow,0);
        String productName = txtOrderProductName.getText().trim();
        String quantityStr = txtOrderQuantity.getText().trim();
        String unitPriceStr = txtOrderUnitPrice.getText().trim();
        ClientItem selectedClient = (ClientItem)comboClientOrders.getSelectedItem();

        if(productName.isEmpty() || quantityStr.isEmpty() || unitPriceStr.isEmpty()){
            setStatus("Complete todos los campos de Pedido.");
            return;
        }
        if(selectedClient == null){
            setStatus("Seleccione un cliente para el pedido.");
            return;
        }
        int clientId = selectedClient.getId();

        try{
            int quantity = Integer.parseInt(quantityStr);
            double unitPrice = Double.parseDouble(unitPriceStr);
            double totalPrice = quantity * unitPrice;

            try(Connection conn = DatabaseConnection.getConnection()){
                String sql = "UPDATE orders SET client_id=?, product_name=?, quantity=?, unit_price=?, total_price=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, clientId);
                ps.setString(2, productName);
                ps.setInt(3, quantity);
                ps.setDouble(4, unitPrice);
                ps.setDouble(5, totalPrice);
                ps.setInt(6, id);
                ps.executeUpdate();
                setStatus("Pedido actualizado.");
                log("Pedido actualizado: ID "+id+", Cliente ID "+clientId+", Producto "+productName+", Cantidad "+quantity+", Precio Unitario "+unitPrice);
                clearOrderFields();
                loadOrdersFromDB();
            }catch(SQLException ex){
                ex.printStackTrace();
                setStatus("Error al actualizar pedido: "+ex.getMessage());
                log("Error al actualizar pedido: "+ex.getMessage());
            }
        }catch(NumberFormatException ex){
            setStatus("Cantidad y Precio Unitario deben ser números válidos.");
        }
    }

    private void deleteOrder(){
        int selectedRow = tableOrders.getSelectedRow();
        if(selectedRow == -1){
            setStatus("Seleccione un pedido para eliminar.");
            return;
        }
        int id = (int) modelOrders.getValueAt(selectedRow,0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el pedido?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        try(Connection conn = DatabaseConnection.getConnection()){
            String sql = "DELETE FROM orders WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            setStatus("Pedido eliminado.");
            log("Pedido eliminado: ID "+id);
            clearOrderFields();
            loadOrdersFromDB();
        }catch(SQLException ex){
            ex.printStackTrace();
            setStatus("Error al eliminar pedido: "+ex.getMessage());
            log("Error al eliminar pedido: "+ex.getMessage());
        }
    }

    private void clearOrderFields(){
        txtOrderProductName.setText("");
        txtOrderQuantity.setText("");
        txtOrderUnitPrice.setText("");
        txtOrderTotalPrice.setText("");
        comboClientOrders.setSelectedIndex(-1);
        tableOrders.clearSelection();
        setStatus("Campos pedido limpios.");
    }

    private void loadOrdersFromDB(){
        modelOrders.setRowCount(0);
        try(Connection conn = DatabaseConnection.getConnection()){
            String sql = "SELECT o.id, o.client_id, c.nombre, c.apellidos, o.product_name, o.quantity, o.unit_price, o.total_price " +
                    "FROM orders o JOIN clients c ON o.client_id = c.id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getInt("client_id"));
                row.add(rs.getString("nombre")+" "+rs.getString("apellidos"));
                row.add(rs.getString("product_name"));
                row.add(rs.getInt("quantity"));
                row.add(rs.getDouble("unit_price"));
                row.add(rs.getDouble("total_price"));
                modelOrders.addRow(row);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            setStatus("Error al cargar pedidos: "+ex.getMessage());
            log("Error al cargar pedidos: "+ex.getMessage());
        }
    }

    private void loadOrderSelection(){
        int selectedRow = tableOrders.getSelectedRow();
        if(selectedRow != -1){
            txtOrderProductName.setText((String)modelOrders.getValueAt(selectedRow,3));
            txtOrderQuantity.setText(modelOrders.getValueAt(selectedRow,4).toString());
            txtOrderUnitPrice.setText(modelOrders.getValueAt(selectedRow,5).toString());
            txtOrderTotalPrice.setText(modelOrders.getValueAt(selectedRow,6).toString());
            int clientId = (int)modelOrders.getValueAt(selectedRow,1);
            for(int i=0; i<comboClientOrders.getItemCount(); i++){
                if(comboClientOrders.getItemAt(i).getId() == clientId){
                    comboClientOrders.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void setStatus(String msg){
        if(lblStatus == null) {
            lblStatus = new JLabel();
            add(lblStatus, BorderLayout.SOUTH);
        }
        lblStatus.setText(msg);
        Timer timer = new Timer(4000, e -> lblStatus.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    // Clase interna para combo clientes
    private static class ClientItem {
        private int id;
        private String name;
        public ClientItem(int id, String name){
            this.id = id;
            this.name = name;
        }
        public int getId(){
            return id;
        }
        public String toString(){
            return name;
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            try{
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }catch(Exception ignored){}
            new MainFrame().setVisible(true);
        });
    }
}







