/*
 * EN ESTA CLASE CREAMOS EL MÉTODO QUE ELEGIRÁ A LOS ENEMIGOS ALEATORIAMENTE.

    OBTUVIMOS PROBLEMAS A LA HORA DE VER POR PANTALLA EL ESCANER
    QUE MOSTRABA AL ENEMIGO CONTRA EL QUE TE ENFRENTABAS,
    PERO LIMPIANDO EL BUFFER LO SOLUCIONAMOS, AUNQUE HAY MOMENTOS
    EN LOS QUE SE TIENE QUE PULSAR LA TECLA INTRO PARA SEGUIR ADELANTE EN EL JUEGO.

    POR OTRO LADO CREAMOS EL MÉTODO INICIAR COMBATE QUE SERVIRÁ PARA DECANTAR SU COMIENZO,
    EL LUCHADOR CON MAS VELOCIDAD SERÁ EL QUE COMENZARÁ EL DUELO.

    AQUI TAMBIEN HAY CONDICIONES PARA LA MUERTE DEL JUGADOR, HACIENDO ESTA ACABE EL JUEGO Y LUEGO OTRA CONDICION
    PARA LA MUERTE DEL ENEMIGO, OTORGAR EXPERIENCIA AL JUGADOR PARA QUE MEJOREN SUS ESTADÍSTICAS Y SIGA COMBATIENDO.

    HEMOS CREADO LA CLASE CAMINO ALTERNATIVO, HACE QUE SE PRODUZCAN DIFERENTES INTERACCIONES DURANTE EL JUEGO COMO CONSEGUIR
    MEJORES SKILLS, PERDER VIDA, SUBIR DE NIVEL O DIRECTAMENTE LUCHAR CONTRA ENEMIGOS. Y AHORA TAMBIEN PUEDES CAER EN ALGUNA EXCEPTION

    DESPUÉS DE TODAS ESTAS FUNCIONALIDADES, HEMOS DECIDIDO DARLE LA OPCIÓN AL JUGADOR 
    DE QUE PUEDA HUIR TRAS CADA COMBATE O EN EL TRANSCURSO DE ESTE.

    ESTOY EN PROCESO DE IMPLEMENTAR AL REY MORGAN POR DEFECTO EN EL MÉTODO "GENERAR ENEMIGO ALEATORIO"
    PARA QUE ESTUVIESE LA POSIBILIDAD DE COMPLETAR EL JUEGO INMEDIATAMENTE DESPUÉS DE GANARLE.
 *
 */
package logica;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Mundo {

    public Enemigo generarEnemigoAleatorio() throws ZonaBloqueadaException {
        Random random = new Random();

        int opcion = random.nextInt(0,7);
        switch (opcion) {
            case 1:
                Logger.registrarEvento("El enemigo es un Lobo Salvaje ");
                System.out.println("Te encuentras con un lobo Salvaje");
                return new LoboSalvaje();
            case 2:
                Logger.registrarEvento("El enemigo es un Guerrero OScuro ");
                System.out.println("Te encuentras con el Guerrero Oscuro");
                return new GuerreroOscuro();
            case 3:
                Logger.registrarEvento("El enemigo es un No Muerto ");
                System.out.println("Te encuentras con el No Muerto");
                return new No_Muerto();
            case 4:
                Logger.registrarEvento("El jugador cae en la trampa y pierde la partida ");
                throw new ZonaBloqueadaException ("El Rey Morgan te encontró y acabó con tu vida");
            default:
                Logger.registrarEvento("El enemigo es un Lobo Salvaje ");
                System.out.println("Te encuentras con un lobo Salvaje furioso");
                return new LoboSalvaje();
        }
    }

    public void iniciarCombate(Personaje jugador, Enemigo enemigo) throws EnergiaInsuficienteException {
        Scanner leer = new Scanner(System.in);
        Logger.registrarEvento("Combate iniciado ");
        try {
            while (jugador.vida > 0 && enemigo.vida > 0) {
                if (enemigo.velocidad > jugador.velocidad) {
                    
                    System.out.println("----¡OS ENZARZAIS EN DUELO!---- ");
                    int opcion = 0;
                    while (opcion < 1 || opcion > 3) {
                        try {
                            System.out.println("--Elije una accion-- ");
                            System.out.println("1. Atacar ");
                            System.out.println("2. Ataque Especial ");
                            System.out.println("3. Huir ");
                            opcion = leer.nextInt();
                            leer.nextLine(); // Limpiar el buffer
                            if (opcion < 1 || opcion > 3) {
                                System.out.println("Elija el número asociado a la acción que quiere realizar");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Introduce un número por favor");
                            leer.next(); // Limpiar el buffer
                        }
                    }
                    switch (opcion) {
                        case 1:
                            Logger.registrarEvento("El enemigo ataca ");
                            System.out.println("EL ENEMIGO ATACA PRIMERO");
                            jugador.recibirDanio(enemigo.ataque);
                            jugador.estaVivo();
                            Logger.registrarEvento("El jugador ataca ");
                            System.out.println("Lanzaste un contraataque");
                            enemigo.recibirDanio(jugador.ataque);
                            enemigo.estaVivo();

                            if (enemigo.vida <= 0) {
                                Scanner scanner = new Scanner(System.in);
                                Logger.registrarEvento("El enemigo a caido ");
                                System.out.println("El enemigo a caido");
                                Logger.registrarEvento("El jugador recibe experiencia");
                                jugador.recibirExp();
                                System.out.println("deseas salir del juego?" + "/ Si / No");
                                String respuesta = scanner.nextLine();
                                if (respuesta.equalsIgnoreCase("Si")) {
                                    Logger.registrarEvento("El jugador decide abandonar el juego ");
                                     throw new JuegoException("Abandonaste correctamente.");
                                } else {
                                    break;
                                }
                            }
                            if (jugador.vida <= 0) {
                                Logger.registrarEvento("El jugador ha sido derrotado ");
                                throw new JuegoException ("Has sido derrotado!");
                            }
                            leer.nextLine();
                            break;
                        case 2:

                            if (jugador instanceof Chaman) {
                                if (enemigo instanceof No_Muerto) {
                                    Logger.registrarEvento("El enemigo usa su ataque especial ");
                                    enemigo.ataqueEsp();
                                    Logger.registrarEvento("El jugador usa su ataque especial ");
                                    jugador.ataqueEsp();
                                    break;
                                } else {
                                    Logger.registrarEvento("El enemigo usa su ataque especial ");
                                    jugador.recibirDanio(enemigo.habilidadEspecial);
                                    Logger.registrarEvento("El jugador usa su ataque especial ");
                                    jugador.ataqueEsp();
                                    break;
                                }
                            } else {
                                if (enemigo instanceof No_Muerto) {
                                    Logger.registrarEvento("El enemigo usa su ataque especial ");
                                    enemigo.ataqueEsp();
                                    jugador.estaVivo();
                                    Logger.registrarEvento("El jugador usa su ataque especial ");
                                    enemigo.recibirDanio(jugador.habilidadEspecial);
                                    enemigo.estaVivo();
                                    break;
                                } else {
                                    Logger.registrarEvento("El enemigo usa su ataque especial ");
                                    System.out.println("El enemigo se adelanta con su ataque especial");
                                    jugador.recibirDanio(enemigo.habilidadEsp());
                                    jugador.estaVivo();
                                    Logger.registrarEvento("El jugador usa su ataque especial ");
                                    System.out.println("Contraatacaste con tu ataque especial");
                                    enemigo.recibirDanio(jugador.habilidadEspecial);
                                    enemigo.estaVivo();
                                }
                            }
                            if (enemigo.vida <= 0) {
                                Scanner scanner = new Scanner(System.in);
                                Logger.registrarEvento("El enemigo ha sido derrotado ");
                                System.out.println("El enemigo a caido");
                                Logger.registrarEvento("El jugador recibe experiencia");
                                jugador.recibirExp();
                                System.out.println("deseas salir del juego?" + "/ Si / No");
                                String respuesta = scanner.nextLine();
                                if (respuesta.equalsIgnoreCase("Si")) {
                                    jugador.vida = 0;
                                    Logger.registrarEvento("El jugador decide abandonar el juego ");
                                    throw new JuegoException ("Abandonaste correctamente.");
                                    
                                } else {
                                    break;
                                }
                            }

                            if (jugador.vida <= 0) {
                                Logger.registrarEvento("El jugador se ha quedado sin vida ");
                                throw new JuegoException (" HAS SIDO ELIMINADO");
                                
                            }
                            leer.nextLine();
                            break;
                        case 3:
                            System.out.println("EL COMBATE DEBE ACABAR CON UN VENCEDOR " + " deseas salir del juego? " + " Si / No");
                            String respuesta = leer.nextLine();
                            if (respuesta.equalsIgnoreCase("Si")) {
                                    jugador.vida = 0;
                                    Logger.registrarEvento("El jugador decide abandonar el juego ");
                                    throw new JuegoException ("Abandonaste correctamente.");
                                    
                                } else {
                                    break;
                            }
                        default:
                            Logger.registrarEvento("El jugador pulsa mal una entrada por teclado y recibe daño");
                            System.out.println("PimPamPum escribiste mal tu accion y el enemigo aprovecha para atacar");
                            jugador.recibirDanio(enemigo.ataque);
                            jugador.estaVivo();
                    }
                }
                if (enemigo.velocidad < jugador.velocidad) {
                    System.out.println("----¡OS ENZARZAIS EN DUELO!---- ");
                    int opcion = 0;
                    while (opcion < 1 || opcion > 3) {
                        try {
                            System.out.println("--Elije una accion-- ");
                            System.out.println("1. Atacar ");
                            System.out.println("2. Ataque Especial ");
                            System.out.println("3. Huir ");
                            opcion = leer.nextInt();
                            leer.nextLine(); // Limpiar el buffer
                            if (opcion < 1 || opcion > 3) {
                                System.out.println("Elige entre 1 / 2 / 3");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Entrada inválida. Se esperaba un número.");
                            leer.next(); // Limpiar el buffer
                        }
                        switch (opcion) {
                            case 1:
                                Logger.registrarEvento("El jugador usa su ataque ");
                                System.out.println("Lanzaste un ataque");
                                enemigo.recibirDanio(jugador.ataque);
                                enemigo.estaVivo();
                                Logger.registrarEvento("El enemigo usa su ataque ");
                                System.out.println("El enemigo contraataca");
                                jugador.recibirDanio(enemigo.ataque);
                                jugador.estaVivo();

                                if (enemigo.vida <= 0) {

                                    Scanner scanner = new Scanner(System.in);
                                    Logger.registrarEvento("El enemigo ha sido eliminado ");
                                    System.out.println("El enemigo a caido");
                                    Logger.registrarEvento("El jugador recibe experiencia ");
                                    jugador.recibirExp();
                                    System.out.println("deseas salir del juego? " + " Si / No");
                                    String respuesta = scanner.nextLine();
                                    if (respuesta.equalsIgnoreCase("Si")) {
                                    jugador.vida = 0;
                                    Logger.registrarEvento("El jugador decide abandonar el juego ");
                                    throw new JuegoException ("Abandonaste correctamente.");
                                    
                                } else {
                                    break;
                                    }
                                }
                                if (jugador.vida <= 0) {
                                    Logger.registrarEvento("Al jugador no le quedan puntos de vida ");
                                    throw new JuegoException ("HAS SIDO ELIMINADO");
                                }
                                leer.nextLine();
                                break;
                            case 2:

                                if (jugador instanceof Chaman) {
                                    if (enemigo instanceof No_Muerto) {
                                        Logger.registrarEvento("El jugador usa su ataque especial ");
                                        jugador.ataqueEsp();
                                        Logger.registrarEvento("El enemigo usa su ataque especial ");
                                        enemigo.ataqueEsp();
                                        break;
                                    } else {
                                        Logger.registrarEvento("El jugador usa su ataque especial ");
                                        jugador.ataqueEsp();
                                        Logger.registrarEvento("El enemigo usa su ataque especial ");
                                        System.out.println("El enemigo usa su ataque especial");
                                        jugador.recibirDanio(enemigo.habilidadEspecial);
                                        jugador.estaVivo();
                                        break;
                                    }
                                } else {
                                    if (enemigo instanceof No_Muerto) {
                                        Logger.registrarEvento("El jugador usa su ataque especial ");
                                        enemigo.recibirDanio(jugador.habilidadEsp());
                                        enemigo.estaVivo();
                                        Logger.registrarEvento("El enemigo usa su ataque especial ");
                                        System.out.println("El enemigo usa su ataque especial");
                                        enemigo.ataqueEsp();
                                        break;
                                    } else {
                                        Logger.registrarEvento("El jugador usa su ataque especial ");
                                        System.out.println("Usaste tu ataque especial");
                                        enemigo.recibirDanio(jugador.habilidadEsp());
                                        enemigo.estaVivo();
                                        Logger.registrarEvento("El enemigo usa su ataque especial ");
                                        System.out.println("El enemigo usa su ataque especial");
                                        jugador.recibirDanio(enemigo.habilidadEspecial);
                                        jugador.estaVivo();
                                    }
                                }
                                if (enemigo.vida <= 0) {
                                    Scanner scanner = new Scanner(System.in);
                                    Logger.registrarEvento("El enemigo ha sido derrotado ");
                                    System.out.println("El enemigo a caido");
                                    Logger.registrarEvento("El jugador recibe experiencia ");
                                    jugador.recibirExp();
                                    System.out.println("deseas salir del juego? " + " Si / No");
                                    String respuesta = scanner.nextLine();
                                    if (respuesta.equalsIgnoreCase("Si")) {
                                        jugador.vida = 0;
                                        Logger.registrarEvento("El jugador decide abandonar el juego ");
                                        throw new JuegoException ("Abandonaste correctamente.");
                                    } else {
                                        break;
                                    }
                                }
                                if (jugador.vida <= 0) {
                                    Logger.registrarEvento("El jugador pierde todos los puntos de vida ");
                                    throw new JuegoException ("HAS SIDO ELIMINADO");
                                }
                                leer.nextLine();
                                break;
                            case 3:
                                System.out.println("EL COMBATE DEBE ACABAR CON UN VENCEDOR " + " deseas salir del juego? " + " Si / No");
                                String respuesta = leer.nextLine();
                                if (respuesta.equalsIgnoreCase("Si")) {
                                        jugador.vida = 0;
                                        Logger.registrarEvento("El jugador decide abandonar el juego ");
                                        throw new JuegoException ("Abandonaste correctamente.");
                                    } else {
                                        break;
                                    }

                            default:
                                Logger.registrarEvento("El jugador pulsa mal una entrada por teclado y recibe daño ");
                                System.out.println("PimPamPum escribiste mal la accion y el enemigo aprovecha para atacar");
                                jugador.recibirDanio(enemigo.ataque);
                                jugador.estaVivo();
                        }
                    }
                }
            }
        } catch (JuegoException e) {
            System.out.println( e.getMessage());
        }
    }

    public void caminosAlternativos(Personaje jugador)throws ZonaBloqueadaException {
        Scanner leer = new Scanner(System.in);
        System.out.println("Te encuentras con tres caminos");

        System.out.println("Escribe tu opcion --- 'Izquierda' --- 'Recto' --- 'Derecha' ---");
        String eleccion = leer.nextLine();
        Random azar = new Random();
        if (eleccion.equalsIgnoreCase("Izquierda")) {

            System.out.println("Acabas adentrándote en el bosque");
            int opcion = azar.nextInt(1,6);
            switch (opcion) {
                case 1:
                    leer.nextLine();
                    Logger.registrarEvento("El jugador cae en la trampa de las zonas y es eliminado");
                    throw new ZonaBloqueadaException ("Oh, no! Has caido en la trampa");
                    
                    
                case 2:
                    leer.nextLine();
                    System.out.println("Gran elección!! El camino está lleno de comida y consigues reponer tu energia");
                    Logger.registrarEvento("El jugador recibe experiencia por las zonas");
                    jugador.recibirExp();
                    jugador.estaVivo();
                    break;
                case 3:
                    leer.nextLine();
                    Logger.registrarEvento("El jugador cae en la trampa de las zonas y es eliminado");
                    throw new ZonaBloqueadaException ("Alguien te sorprende derrepente!");
                    

                default:
                    System.out.println("Consigues encontrar un refugio y pasas allí la noche");
                    Logger.registrarEvento("El jugador recibe experiencia por las zonas");
                    jugador.recibirExp();
                    System.out.println("Se hace de día y recuperas fuerzas pero...");
                    leer.nextLine();

            }

        }
        if (eleccion.equalsIgnoreCase("Recto")) {

            int opcion = azar.nextInt(1,4);
            switch (opcion) {
                case 1:
                    System.out.println("Han pasado varias horas y empiezas escuchar el sonido del rio");
                    jugador.vida -= 10;
                    System.out.println("Aceleras el paso para alcanzar a beber un poco de agua y...");
                    System.out.println("Eres mordido por una serpiente ----" + jugador.toString());
                    break;
                case 2:
                    System.out.println("Gran elección!! El camino está lleno de comida y consigues reponer tu energia");
                    Logger.registrarEvento("El jugador recibe experiencia por las zonas");
                    jugador.recibirExp();
                    jugador.estaVivo();
                    leer.nextLine();
                    System.out.println("pero...");
                    break;
                case 3:
                    Logger.registrarEvento("El jugador cae en la trampa de las zonas y es eliminado");
                    System.out.println("Llevas caminando horas, se está haciendo de noche y...");
                    throw new ZonaBloqueadaException ("De pronto te atacan!");
                    

                default:
                    Logger.registrarEvento("El jugador cae en la trampa de las zonas y es eliminado");
                    System.out.println("Empiezas a escuchar ruido y...");
                    throw new ZonaBloqueadaException ("Has caido en la trampa del Rey Morgan");

            }
        }
        if (eleccion.equalsIgnoreCase("Derecha")) {
            int opcion = azar.nextInt(0,6);
            switch (opcion) {
                case 1:
                    Logger.registrarEvento("El jugador cae en la trampa de las zonas y es eliminado");
                    System.out.println("Te adentras por el sendero y...");
                    System.out.println("despues de varias horas... el camino te lleva a cruzar por el cementerio");
                    throw new ZonaBloqueadaException ("Una fuerza descomunal te absorvió hacia el fondo de la Tierra");
                case 2:
                    Logger.registrarEvento("El jugador recibe puntos de ataque y vida extra por las zonas");
                    System.out.println("Una fuerza sobrenatural entra por tu cuerpo y empiezas a volverte más fuerte");
                    jugador.vida += 50;
                    jugador.ataque += 20;
                    break;
                case 3:
                    leer.nextLine();
                    Logger.registrarEvento("El jugador recibe experiencia");
                    System.out.println("Consigues encontrar un refugio y pasas allí la noche");
                    jugador.recibirExp();
                    System.out.println("Se hace de día y recuperas fuerzas pero...");
                    break;
                default:
                    Logger.registrarEvento("El jugador recibe experiencia");
                    System.out.println("Que suerte! encuentras alimentos en buen estado");
                    jugador.recibirExp();
                    System.out.println("Pero el dueño está cerca y...");
            }
        }
    }
}
