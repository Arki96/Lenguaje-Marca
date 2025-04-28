package gui;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        VentanaJuego ventana = new VentanaJuego();
        new ControladorJuego(ventana);
        ventana.setVisible(true);
    }
}