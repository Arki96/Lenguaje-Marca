package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaJuego extends JFrame {

    // Componentes de la interfaz
    private JPanel contentPane;
    private JTextArea areaLog;
    private JButton btnAceptar;
    private JButton btnAtacar;
    private JButton btnAtaqueEspecial;
    private JButton btnHuir;
    private JButton btnSiguiente;
    private JTextField campoEntrada;
    private JLabel lblEnemigo;
    private JLabel lblPersonaje;
    private JLabel lblVidaEnemigo;
    private JLabel lblVidaPersonaje;
    private JProgressBar progressVidaEnemigo;
    private JProgressBar progressVidaPersonaje;
    private JScrollPane scrollLog;

    public VentanaJuego() {
        // Configuración básica de la ventana
        setTitle("Tierras de Zaltor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        configurarComponentes();
        configurarEntradaTexto();

    }

    private void inicializarComponentes() {
        // Panel superior (combate)
        JPanel panelCombate = new JPanel(new GridLayout(1, 2, 20, 0));

        // Panel personaje
        JPanel panelPersonaje = new JPanel(new BorderLayout());
        lblPersonaje = new JLabel("Personaje", SwingConstants.CENTER);
        lblPersonaje.setPreferredSize(new Dimension(200, 200));

        progressVidaPersonaje = new JProgressBar(0, 100);
        progressVidaPersonaje.setValue(100);
        lblVidaPersonaje = new JLabel("Vida: 100/100");

        JPanel panelStatsPersonaje = new JPanel();
        panelStatsPersonaje.add(lblVidaPersonaje);
        panelStatsPersonaje.add(progressVidaPersonaje);

        panelPersonaje.add(lblPersonaje, BorderLayout.CENTER);
        panelPersonaje.add(panelStatsPersonaje, BorderLayout.SOUTH);

        // Panel enemigo
        JPanel panelEnemigo = new JPanel(new BorderLayout());
        lblEnemigo = new JLabel("Enemigo", SwingConstants.CENTER);
        lblEnemigo.setPreferredSize(new Dimension(200, 200));

        progressVidaEnemigo = new JProgressBar(0, 100);
        progressVidaEnemigo.setValue(100);
        lblVidaEnemigo = new JLabel("Vida: 100/100");

        JPanel panelStatsEnemigo = new JPanel();
        panelStatsEnemigo.add(lblVidaEnemigo);
        panelStatsEnemigo.add(progressVidaEnemigo);

        panelEnemigo.add(lblEnemigo, BorderLayout.CENTER);
        panelEnemigo.add(panelStatsEnemigo, BorderLayout.SOUTH);

        panelCombate.add(panelPersonaje);
        panelCombate.add(panelEnemigo);

        // Panel central (log)
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        scrollLog = new JScrollPane(areaLog);

        JPanel panelControles = new JPanel(new GridLayout(1, 5, 10, 0)); // Cambiado a 5 columnas
        btnAtacar = new JButton("Atacar");
        btnAtaqueEspecial = new JButton("Ataque Especial");
        btnHuir = new JButton("Huir");
        btnAceptar = new JButton("Aceptar");
        btnSiguiente = new JButton("SIGUIENTE"); // Nuevo botón

        panelControles.add(btnAtacar);
        panelControles.add(btnAtaqueEspecial);
        panelControles.add(btnHuir);
        panelControles.add(btnAceptar);
        panelControles.add(btnSiguiente);

        // Panel entrada de texto
        JPanel panelEntrada = new JPanel();
        campoEntrada = new JTextField(20);
        panelEntrada.add(new JLabel("Comando: "));
        panelEntrada.add(campoEntrada);

        // Panel sur combinado
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(panelControles, BorderLayout.NORTH);
        panelSur.add(panelEntrada, BorderLayout.SOUTH);

        // Añadir todos los paneles al contentPane
        contentPane.add(panelCombate, BorderLayout.NORTH);
        contentPane.add(scrollLog, BorderLayout.CENTER);
        contentPane.add(panelSur, BorderLayout.PAGE_END);
    }

    private void configurarComponentes() {
        // Configuración de las barras de vida
        progressVidaPersonaje.setForeground(Color.GREEN);
        progressVidaPersonaje.setStringPainted(true);

        progressVidaEnemigo.setForeground(Color.GREEN);
        progressVidaEnemigo.setStringPainted(true);

        // Configuración del área de log
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);

        btnAtacar.setEnabled(true);
        btnAtaqueEspecial.setEnabled(true);
        btnHuir.setEnabled(true);

        // Configurar botón Siguiente
        btnSiguiente.setVisible(false);
        btnSiguiente.setBackground(new Color(50, 150, 50));
        btnSiguiente.setForeground(Color.WHITE);
    }

    private void configurarEntradaTexto() {
        campoEntrada.setEnabled(true);
        campoEntrada.setEditable(true);
        campoEntrada.requestFocusInWindow();

        // Habilitar Enter para aceptar
        campoEntrada.addActionListener(e -> {
            if (btnAceptar.isVisible()) {
                btnAceptar.doClick();
            }
        });
    }

    // ========== MÉTODOS PÚBLICOS ========== //
    public void mostrarPanelSeleccion(boolean mostrar) {
        campoEntrada.setVisible(mostrar);
        btnAceptar.setVisible(mostrar);
        // Solo controlar visibilidad de botones de combate si no estamos en modo "SIGUIENTE"
        if (!btnSiguiente.isVisible()) {
            btnAtacar.setVisible(!mostrar);
            btnAtaqueEspecial.setVisible(!mostrar);
            btnHuir.setVisible(!mostrar);
        }

        if (mostrar) {
            campoEntrada.requestFocusInWindow();
        }
    }

    public void mostrarBotonSiguiente(boolean mostrar) {
        btnSiguiente.setVisible(mostrar);
        btnAtacar.setEnabled(!mostrar);
        btnAtaqueEspecial.setEnabled(!mostrar);
        btnHuir.setEnabled(!mostrar);

        // Mantener siempre visibles los botones de combate
        btnAtacar.setVisible(true);
        btnAtaqueEspecial.setVisible(true);
        btnHuir.setVisible(true);
    }

    public void agregarLog(String mensaje) {
        areaLog.append(mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    public void actualizarVidaPersonaje(int vidaActual, int vidaMaxima) {
        progressVidaPersonaje.setMaximum(vidaMaxima);
        progressVidaPersonaje.setValue(vidaActual);
        lblVidaPersonaje.setText("Vida: " + vidaActual + "/" + vidaMaxima);
        actualizarColorBarra(progressVidaPersonaje, vidaActual, vidaMaxima);
    }

    public void actualizarVidaEnemigo(int vidaActual, int vidaMaxima) {
        progressVidaEnemigo.setMaximum(vidaMaxima);
        progressVidaEnemigo.setValue(vidaActual);
        lblVidaEnemigo.setText("Vida: " + vidaActual + "/" + vidaMaxima);
        actualizarColorBarra(progressVidaEnemigo, vidaActual, vidaMaxima);
    }

    private void actualizarColorBarra(JProgressBar barra, int vidaActual, int vidaMaxima) {
        double porcentaje = (double) vidaActual / vidaMaxima;
        if (porcentaje <= 0.2) {
            barra.setForeground(Color.RED);
        } else if (porcentaje <= 0.5) {
            barra.setForeground(Color.ORANGE);
        } else {
            barra.setForeground(Color.GREEN);
        }
    }

    public void configurarActionListeners(ActionListener atacarListener,
            ActionListener especialListener,
            ActionListener huirListener) {
        btnAtacar.addActionListener(atacarListener);
        btnAtaqueEspecial.addActionListener(especialListener);
        btnHuir.addActionListener(huirListener);
    }

    public void setImagenPersonaje(String tipoPersonaje) {
        try {
            String ruta = "/imagenes/" + tipoPersonaje.toLowerCase() + ".png";
            ImageIcon icono = new ImageIcon(getClass().getResource(ruta));
            lblPersonaje.setIcon(new ImageIcon(icono.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            lblPersonaje.setText(tipoPersonaje);
        }
    }

    public void setImagenEnemigo(String tipoEnemigo) {
        try {
            String nombreArchivo = tipoEnemigo.toLowerCase().replace(" ", "_");
            String ruta = "/imagenes/" + nombreArchivo + ".png";
            ImageIcon icono = new ImageIcon(getClass().getResource(ruta));
            lblEnemigo.setIcon(new ImageIcon(icono.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            lblEnemigo.setText(tipoEnemigo);
        }
    }

    public JButton getBtnSiguiente() {
        return btnSiguiente;
    }

    // ========== GETTERS ========== //
    public JButton getBtnAceptar() {
        return btnAceptar;
    }

    public JButton getBtnAtacar() {
        return btnAtacar;
    }

    public JButton getBtnAtaqueEspecial() {
        return btnAtaqueEspecial;
    }

    public JButton getBtnHuir() {
        return btnHuir;
    }

    public JTextField getCampoEntrada() {
        return campoEntrada;
    }
}
