package com.mycompany.crudos;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {

            try {
                DatabaseConnection db = new DatabaseConnection();
                CRUDOperations crud = new CRUDOperations(db);
                new EstadisticasFrame(crud);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al iniciar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
