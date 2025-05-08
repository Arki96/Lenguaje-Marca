package gui;

import BBDD.PersonajeDAO;
import logica.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ControladorJuego {

    private final VentanaJuego ventana;
    private final Mundo mundo;
    private Personaje jugador;
    private Enemigo enemigoActual;
    private int combatesGanados = 0;
    private boolean turnoJugador;
    private boolean combateEnCurso = false;
    private PersonajeDAO personajeDAO;
    private int rondasCombateActual = 0;
    private int danioTotalCombate = 0;

    public ControladorJuego(VentanaJuego ventana) {
        this.ventana = ventana;
        this.mundo = new Mundo();
        this.personajeDAO = new PersonajeDAO();
        try {
            inicializarBaseDatos();
        } catch (SQLException e) {
            ventana.agregarLog("Error al inicializar la base de datos: " + e.getMessage());
        }
        configurarEventos();
        iniciarJuego();
    }

    private void inicializarBaseDatos() throws SQLException {
        // Insertar enemigos si no existen
        if (!personajeDAO.enemigosExistenEnDB()) {
            personajeDAO.insertarEnemigo(new LoboSalvaje());
            personajeDAO.insertarEnemigo(new No_Muerto());
            personajeDAO.insertarEnemigo(new GuerreroOscuro());
            personajeDAO.insertarEnemigo(new ReyMorgan());
            ventana.agregarLog("Base de datos inicializada con enemigos por defecto");
        }
    }

    private void configurarEventos() {
        ventana.getBtnAceptar().addActionListener(this::manejarSeleccionPersonaje);

        ventana.configurarActionListeners(
                e -> manejarAtaque(),
                e -> manejarAtaqueEspecial(),
                e -> manejarHuir()
        );
        ventana.getBtnEstadisticas().addActionListener(e -> mostrarEstadisticas());
    }

    private void iniciarJuego() {
        ventana.agregarLog("Bienvenido a las Tierras de Zaltor!");
        ventana.agregarLog("Escribe tu personaje (Guerrero, Ninja o Chaman) y pulsa Aceptar");
        ventana.mostrarPanelSeleccion(true);

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
            eleccion = eleccion.toLowerCase().trim();

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

            try {
                personajeDAO.insertarPersonaje(jugador);
            } catch (SQLException ex) {
                ventana.agregarLog("Error al guardar personaje en la base de datos: " + ex.getMessage());
            }

            ventana.setImagenPersonaje(eleccion);
            ventana.actualizarVidaPersonaje(jugador.vida, jugador.vidaMaxima);
            ventana.agregarLog("¡Has elegido ser un " + eleccion.substring(0, 1).toUpperCase() + eleccion.substring(1) + "!");
            ventana.agregarLog("Estadísticas: " + jugador.toString());

            ventana.getCampoEntrada().setText("");
            ventana.mostrarPanelSeleccion(false);

            ventana.getBtnAtacar().setEnabled(true);
            ventana.getBtnAtaqueEspecial().setEnabled(true);
            ventana.getBtnHuir().setEnabled(true);
            ventana.mostrarBotonSiguiente(false);

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
        rondasCombateActual = 0;
        danioTotalCombate = 0;

        try {
            if (combatesGanados >= 3) {
                iniciarCombateFinal();
                return;
            }

            enemigoActual = mundo.generarEnemigoAleatorio();
            ventana.actualizarVidaEnemigo(enemigoActual.vida, enemigoActual.vidaMaxima);
            ventana.setImagenEnemigo(enemigoActual.getClass().getSimpleName());
            ventana.agregarLog("\n¡Te enfrentas a un " + enemigoActual.getClass().getSimpleName() + "!");

            turnoJugador = jugador.velocidad >= enemigoActual.velocidad;

            if (turnoJugador) {
                ventana.agregarLog("¡Eres más rápido! Atacas primero.");
            } else {
                ventana.agregarLog("¡El enemigo es más rápido y ataca primero!");
                Timer primerAtaqueTimer = new Timer(1000, evt -> {
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
            danioTotalCombate += danio;
            ventana.actualizarVidaPersonaje(jugador.vida, jugador.vidaMaxima);
            ventana.agregarLog("¡El enemigo te ataca por " + danio + " de daño!");

            if (jugador.vida <= 0) {
                combateEnCurso = false;
                terminarJuego(false);
            } else {
                turnoJugador = true;
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
            danioTotalCombate += danio;
            rondasCombateActual++;
            ventana.actualizarVidaEnemigo(enemigoActual.vida, enemigoActual.vidaMaxima);
            ventana.agregarLog("\n¡Atacas al enemigo por " + danio + " de daño!");

            if (enemigoActual.vida <= 0) {
                combateEnCurso = false;
                enemigoDerrotado();
                return;
            }

            turnoJugador = false;

            Timer ataqueTimer = new Timer(1500, evt -> {
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
            danioTotalCombate += danio;
            rondasCombateActual++;
            ventana.actualizarVidaEnemigo(enemigoActual.vida, enemigoActual.vidaMaxima);
            ventana.agregarLog("\n¡Ataque especial! Daño: " + danio);

            if (enemigoActual.vida <= 0) {
                combateEnCurso = false;
                enemigoDerrotado();
                return;
            }

            turnoJugador = false;
            Timer especialTimer = new Timer(1500, evt -> {
                if (!combateEnCurso) {
                    return;
                }

                try {
                    int danioEnemigo = enemigoActual.habilidadEspecial;
                    jugador.recibirDanio(danioEnemigo);
                    danioTotalCombate += danioEnemigo;
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
                danioTotalCombate += danio;
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

        try {
            personajeDAO.registrarCombate(jugador, enemigoActual, true, rondasCombateActual, danioTotalCombate);
            ventana.agregarLog("\n¡Has derrotado al enemigo!");
            ventana.agregarLog("¡Ganas experiencia! Nivel actual: " + jugador.nivel);

            if (enemigoActual instanceof ReyMorgan) {
                terminarJuego(true);
                return;
            }

            for (ActionListener al : ventana.getBtnSiguiente().getActionListeners()) {
                ventana.getBtnSiguiente().removeActionListener(al);
            }
            ventana.getBtnSiguiente().addActionListener(evt -> {
                ventana.mostrarBotonSiguiente(false);
                iniciarNuevoCombate();
            });
            ventana.mostrarBotonSiguiente(true);
            
        } catch (SQLException e) {
            ventana.agregarLog("Error al guardar el combate: " + e.getMessage());
            ventana.agregarLog("\n¡Has derrotado al enemigo!");
            ventana.agregarLog("¡Ganas experiencia! Nivel actual: " + jugador.nivel);
            
            if (enemigoActual instanceof ReyMorgan) {
                mostrarVictoriaFinal();
            } else {
                for (ActionListener al : ventana.getBtnSiguiente().getActionListeners()) {
                    ventana.getBtnSiguiente().removeActionListener(al);
                }
                ventana.getBtnSiguiente().addActionListener(evt -> {
                    ventana.mostrarBotonSiguiente(false);
                    iniciarNuevoCombate();
                });
                ventana.mostrarBotonSiguiente(true);
            }
        }
    }

    private void mostrarVictoriaFinal() {
        ventana.agregarLog("\n¡FELICIDADES! Has completado el juego.");
        ventana.agregarLog("Derrotaste al Rey Morgan y te convertiste en leyenda.");

        ventana.getBtnAtacar().setEnabled(false);
        ventana.getBtnAtaqueEspecial().setEnabled(false);
        ventana.getBtnHuir().setEnabled(false);
        ventana.getBtnAceptar().setText("Reiniciar");
        ventana.getBtnAceptar().setVisible(true);

        for (ActionListener al : ventana.getBtnAceptar().getActionListeners()) {
            ventana.getBtnAceptar().removeActionListener(al);
        }
        ventana.getBtnAceptar().addActionListener(evt -> reiniciarJuego());
    }

    private void iniciarCombateFinal() {
        try {
            combateEnCurso = true;
            enemigoActual = new ReyMorgan();
            ventana.agregarLog("\n¡ALERTA! Te enfrentas al Rey Morgan en el combate final!");
            ventana.setImagenEnemigo("ReyMorgan");
            ventana.actualizarVidaEnemigo(enemigoActual.vida, enemigoActual.vidaMaxima);

            ventana.getBtnHuir().setEnabled(false);

            turnoJugador = jugador.velocidad >= enemigoActual.velocidad;
            if (!turnoJugador) {
                ventana.agregarLog("¡El Rey Morgan es más rápido y ataca primero!");
                Timer timerAtaqueInicial = new Timer(1000, evt -> turnoEnemigo());
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
        if (!victoria) {
            try {
                personajeDAO.registrarCombate(jugador, enemigoActual, false, rondasCombateActual, danioTotalCombate);
            } catch (SQLException e) {
                ventana.agregarLog("Error al guardar resultado del combate: " + e.getMessage());
            }
            ventana.agregarLog("\n¡HAS SIDO DERROTADO! Fin del juego.");
        } else {
            ventana.agregarLog("\n¡FELICIDADES! Has completado el juego.");
            ventana.agregarLog("Derrotaste al Rey Morgan y te convertiste en leyenda.");
        }

        ventana.getBtnAtacar().setEnabled(false);
        ventana.getBtnAtaqueEspecial().setEnabled(false);
        ventana.getBtnHuir().setEnabled(false);
        ventana.getBtnAceptar().setText("Reiniciar");
        ventana.getBtnAceptar().setVisible(true);

        for (ActionListener al : ventana.getBtnAceptar().getActionListeners()) {
            ventana.getBtnAceptar().removeActionListener(al);
        }
        ventana.getBtnAceptar().addActionListener(evt -> reiniciarJuego());
    }

    private void mostrarEstadisticas() {
        try {
            StringBuilder stats = new StringBuilder();
            stats.append("\n=== ESTADÍSTICAS DEL JUGADOR ===\n");
            stats.append("Nombre: ").append(jugador.nombre).append("\n");
            stats.append("Nivel: ").append(jugador.nivel).append("\n");
            stats.append("Combates ganados: ").append(combatesGanados).append("\n");
            stats.append("Vida actual: ").append(jugador.vida).append("/").append(jugador.vidaMaxima).append("\n");
            
            ventana.agregarLog(stats.toString());
        } catch (Exception e) {
            ventana.agregarLog("Error al mostrar estadísticas: " + e.getMessage());
        }
    }

    private void reiniciarJuego() {
        ventana.dispose();
        VentanaJuego nuevaVentana = new VentanaJuego();
        nuevaVentana.setVisible(true);
        new ControladorJuego(nuevaVentana);
    }
}