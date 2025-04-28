package gui;

import logica.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorJuego {

    private final VentanaJuego ventana;
    private final Mundo mundo;
    private Personaje jugador;
    private Enemigo enemigoActual;
    private int combatesGanados = 0;
    private boolean turnoJugador;
    private boolean combateEnCurso = false;

    public ControladorJuego(VentanaJuego ventana) {
        this.ventana = ventana;
        this.mundo = new Mundo();
        configurarEventos();
        iniciarJuego();
    }

    private void configurarEventos() {
        ventana.getBtnAceptar().addActionListener(this::manejarSeleccionPersonaje);

        // Configurar listeners usando el nuevo método
        ventana.configurarActionListeners(
                e -> manejarAtaque(),
                e -> manejarAtaqueEspecial(),
                e -> manejarHuir()
        );
    }

    private void iniciarJuego() {
        ventana.agregarLog("Bienvenido a las Tierras de Zaltor!");
        ventana.agregarLog("Escribe tu personaje (Guerrero, Ninja o Chaman) y pulsa Aceptar");
        ventana.mostrarPanelSeleccion(true);

        // Asegurar que los botones estén habilitados
        ventana.mostrarBotonSiguiente(false);
        ventana.getBtnAtacar().setEnabled(true);
        ventana.getBtnAtaqueEspecial().setEnabled(true);
        ventana.getBtnHuir().setEnabled(true);
    }

    private void manejarSeleccionPersonaje(ActionEvent e) {
        String eleccion = ventana.getCampoEntrada().getText().trim();

        if (eleccion.isEmpty()) {
            ventana.agregarLog("Error: Debes escribir un personaje");
            ventana.getCampoEntrada().requestFocusInWindow();
            return;
        }

        try {
            // Convertir a minúsculas y eliminar espacios redundantes
            eleccion = eleccion.toLowerCase().trim();

            // Selección de personaje con mejor manejo de casos
            switch (eleccion) {
                case "guerrero":
                    jugador = new Guerrero();
                    break;
                case "ninja":
                    jugador = new Ninja();
                    break;
                case "chaman":
                    jugador = new Chaman();
                    break;
                default:
                    throw new IllegalArgumentException("Personaje no válido. Opciones: Guerrero, Ninja o Chaman");
            }

            // Configuración inicial del personaje
            ventana.setImagenPersonaje(eleccion);
            ventana.actualizarVidaPersonaje(jugador.vida, jugador.vidaMaxima);
            ventana.agregarLog("¡Has elegido ser un " + eleccion.substring(0, 1).toUpperCase() + eleccion.substring(1) + "!");
            ventana.agregarLog("Estadísticas: " + jugador.toString());

            // Preparar interfaz para el combate
            ventana.getCampoEntrada().setText("");
            ventana.mostrarPanelSeleccion(false);

            // Asegurar que los botones de combate estén activos
            ventana.getBtnAtacar().setEnabled(true);
            ventana.getBtnAtaqueEspecial().setEnabled(true);
            ventana.getBtnHuir().setEnabled(true);
            ventana.mostrarBotonSiguiente(false);

            // Iniciar primer combate
            iniciarNuevoCombate();

        } catch (Exception ex) {
            ventana.agregarLog("Error: " + ex.getMessage());
            ventana.agregarLog("Por favor, elige entre: Guerrero, Ninja o Chaman");
            ventana.getCampoEntrada().requestFocusInWindow();
            ventana.getCampoEntrada().selectAll();
        }
    }

    private void iniciarNuevoCombate() {
        combateEnCurso = true;
        turnoJugador = true;

        try {
            if (combatesGanados >= 3) {
                iniciarCombateFinal();
                return;
            }

            enemigoActual = mundo.generarEnemigoAleatorio();
            ventana.actualizarVidaEnemigo(enemigoActual.vida, enemigoActual.vidaMaxima);
            ventana.setImagenEnemigo(enemigoActual.getClass().getSimpleName());
            ventana.agregarLog("\n¡Te enfrentas a un " + enemigoActual.getClass().getSimpleName() + "!");

            // Determinar primer turno basado en velocidad
            turnoJugador = jugador.velocidad >= enemigoActual.velocidad;

            if (turnoJugador) {
                ventana.agregarLog("¡Eres más rápido! Atacas primero.");
            } else {
                ventana.agregarLog("¡El enemigo es más rápido y ataca primero!");
                Timer primerAtaqueTimer = new Timer(1000, e -> {
                    if (combateEnCurso) {
                        turnoEnemigo();
                    }
                });
                primerAtaqueTimer.setRepeats(false);
                primerAtaqueTimer.start();
            }
        } catch (ZonaBloqueadaException e) {
            terminarJuego(false);
        }
    }

    private void turnoEnemigo() {
        if (!combateEnCurso || jugador.vida <= 0) {
            terminarJuego(false);
            return;
        }

        try {
            int danio = enemigoActual.ataque;
            jugador.recibirDanio(danio);
            ventana.actualizarVidaPersonaje(jugador.vida, jugador.vidaMaxima);
            ventana.agregarLog("¡El enemigo te ataca por " + danio + " de daño!");

            if (jugador.vida <= 0) {
                combateEnCurso = false;
                terminarJuego(false);
            } else {
                turnoJugador = true; // Pasar turno al jugador
                ventana.agregarLog("¡Es tu turno!");
            }
        } catch (JuegoException e) {
            combateEnCurso = false;
            terminarJuego(false);
        }
    }

    private void manejarAtaque() {
        if (!combateEnCurso || !turnoJugador || jugador.vida <= 0) {
            ventana.agregarLog("\n¡No puedes atacar ahora!");
            return;
        }

        try {
            int danio = jugador.ataque;
            enemigoActual.recibirDanio(danio);
            ventana.actualizarVidaEnemigo(enemigoActual.vida, enemigoActual.vidaMaxima);
            ventana.agregarLog("\n¡Atacas al enemigo por " + danio + " de daño!");

            if (enemigoActual.vida <= 0) {
                combateEnCurso = false;
                enemigoDerrotado();
                return;
            }

            turnoJugador = false; // Termina turno del jugador

            // Usar Timer con setRepeats(false) para un solo ataque
            Timer ataqueTimer = new Timer(1500, e -> {
                if (combateEnCurso) {
                    turnoEnemigo();
                }
            });
            ataqueTimer.setRepeats(false);
            ataqueTimer.start();

        } catch (JuegoException e) {
            combateEnCurso = false;
            enemigoActual.vida = 0;
            ventana.actualizarVidaEnemigo(0, enemigoActual.vidaMaxima);
            enemigoDerrotado();
        }
    }

    private void manejarAtaqueEspecial() {
        if (!combateEnCurso || !turnoJugador || jugador.vida <= 0) {
            ventana.agregarLog("\n¡No puedes atacar ahora!");
            return;
        }

        try {
            int danio = jugador.habilidadEspecial;
            enemigoActual.recibirDanio(danio);
            ventana.actualizarVidaEnemigo(enemigoActual.vida, enemigoActual.vidaMaxima);
            ventana.agregarLog("\n¡Ataque especial! Daño: " + danio);

            if (enemigoActual.vida <= 0) {
                combateEnCurso = false;
                enemigoDerrotado();
                return;
            }

            turnoJugador = false;
            Timer especialTimer = new Timer(1500, e -> {
                if (!combateEnCurso) {
                    return;
                }

                try {
                    int danioEnemigo = enemigoActual.habilidadEspecial;
                    jugador.recibirDanio(danioEnemigo);
                    ventana.actualizarVidaPersonaje(jugador.vida, jugador.vidaMaxima);
                    ventana.agregarLog("¡El enemigo contraataca con su habilidad especial por " + danioEnemigo + " de daño!");

                    if (jugador.vida <= 0) {
                        combateEnCurso = false;
                        terminarJuego(false);
                    } else {
                        turnoJugador = true;
                        ventana.agregarLog("¡Es tu turno de nuevo!");
                    }
                } catch (JuegoException ex) {
                    combateEnCurso = false;
                    terminarJuego(false);
                }
            });
            especialTimer.setRepeats(false);
            especialTimer.start();

        } catch (JuegoException ex) {
            combateEnCurso = false;
            enemigoActual.vida = 0;
            ventana.actualizarVidaEnemigo(0, enemigoActual.vidaMaxima);
            enemigoDerrotado();
        }
    }

    private void manejarHuir() {
        // No se puede huir del combate final
        if (enemigoActual instanceof ReyMorgan) {
            ventana.agregarLog("\n¡No puedes huir del Rey Morgan!");
            return;
        }

        if (Math.random() < 0.5) {
            ventana.agregarLog("\n¡Has logrado huir del combate!");
            iniciarNuevoCombate();
        } else {
            ventana.agregarLog("\n¡No has podido huir!");
            try {
                int danio = enemigoActual.ataque / 2;
                jugador.recibirDanio(danio);
                ventana.actualizarVidaPersonaje(jugador.vida, jugador.vidaMaxima);
                ventana.agregarLog("¡El enemigo te ataca por " + danio + " de daño mientras intentas huir!");

                if (jugador.vida <= 0) {
                    terminarJuego(false);
                } else {
                    turnoEnemigo();
                }
            } catch (JuegoException e) {
                ventana.agregarLog(e.getMessage());
            }
        }
    }

    private void enemigoDerrotado() {
        combateEnCurso = false;
        combatesGanados++;
        jugador.recibirExp();

        ventana.agregarLog("\n¡Has derrotado al enemigo!");
        ventana.agregarLog("¡Ganas experiencia! Nivel actual: " + jugador.nivel);

        // Verificar si era el Rey Morgan
        if (enemigoActual instanceof ReyMorgan) {
            terminarJuego(true); // ¡Victoria final!
            return;
        }

        // Configurar botón "Siguiente" para combates normales
        for (ActionListener al : ventana.getBtnSiguiente().getActionListeners()) {
            ventana.getBtnSiguiente().removeActionListener(al);
        }
        ventana.getBtnSiguiente().addActionListener(e -> {
            ventana.mostrarBotonSiguiente(false);
            iniciarNuevoCombate();
        });
        ventana.mostrarBotonSiguiente(true);
    }

    private void iniciarCombateFinal() {
        try {
            combateEnCurso = true;
            enemigoActual = new ReyMorgan();
            ventana.agregarLog("\n¡ALERTA! Te enfrentas al Rey Morgan en el combate final!");
            ventana.setImagenEnemigo("ReyMorgan");
            ventana.actualizarVidaEnemigo(enemigoActual.vida, enemigoActual.vidaMaxima);

            // Deshabilitar huir en combate final
            ventana.getBtnHuir().setEnabled(false);

            // Turno inicial
            turnoJugador = jugador.velocidad >= enemigoActual.velocidad;
            if (!turnoJugador) {
                ventana.agregarLog("¡El Rey Morgan es más rápido y ataca primero!");
                Timer timerAtaqueInicial = new Timer(1000, e -> turnoEnemigo());
                timerAtaqueInicial.setRepeats(false);
                timerAtaqueInicial.start();
            } else {
                ventana.agregarLog("¡Eres más rápido! Atacas primero.");
            }
        } catch (Exception e) {
            ventana.agregarLog("Error al iniciar combate final: " + e.getMessage());
        }
    }

    private void terminarJuego(boolean victoria) {
        if (victoria) {
            ventana.agregarLog("\n¡FELICIDADES! Has completado el juego.");
            ventana.agregarLog("Derrotaste al Rey Morgan y te convertiste en leyenda.");
        } else {
            ventana.agregarLog("\n¡HAS SIDO DERROTADO! Fin del juego.");
        }

        ventana.getBtnAtacar().setEnabled(false);
        ventana.getBtnAtaqueEspecial().setEnabled(false);
        ventana.getBtnHuir().setEnabled(false);
        ventana.getBtnAceptar().setText("Reiniciar");
        ventana.getBtnAceptar().setVisible(true);

        // Limpiar listeners anteriores y añadir nuevo
        for (ActionListener al : ventana.getBtnAceptar().getActionListeners()) {
            ventana.getBtnAceptar().removeActionListener(al);
        }
        ventana.getBtnAceptar().addActionListener(e -> reiniciarJuego());
    }

    private void reiniciarJuego() {
        ventana.dispose();
        VentanaJuego nuevaVentana = new VentanaJuego();
        nuevaVentana.setVisible(true);
        new ControladorJuego(nuevaVentana);
    }
}
