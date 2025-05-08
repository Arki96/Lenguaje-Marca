package logica;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * EN ESTA CLASE CREAMOS EL MAIN PARA INICIAR EL JUEGO, UTILIZAMOS LA FUNCIÓN
 * .EQUALSIGNORECASE PARA DETERMINAR LA ELECCION DEL JUGADOR, ADEMÁS DE AÑADIR
 * CONDICIONALES PARA QUE EL JUEGO ACABE A LOS 4 COMBATES, SIENDO EL COMBATE
 * NUMERO 4 CONTRA EL REY MORGAN QUE DETERMINARÁ SI HAS COMPLETADO EL JUEGO. EL
 * JUEGO TAMBIÉN ACABA CUANDO EL JUGADOR SE QUEDA SIN VIDA.
 *
 * IMPLEMENTAMOS UN TRY CATH PARA CORREGIR POSIBLES ERRORES A LA HORA DE LA
 * INTERACCIÓN CON EL JUEGO, SI EL JUGADOR CONSIGUE GANAR 3 COMBATES SE ENFRENTA
 * AL REY MORGAN, Y SOLO SI GANÁNDOLE SE COMPLETA EL JUEGO. SI EL JUGADOR PIERDE
 * LA BATALLA EL JUEGO TAMBIÉN TERMINA.
 *
 * (Se produce un fallo cuando, tanto el jugador como el enemigo mueren a la vez
 * en la batalla final (contra el rey morgan) y el programa lo interpreta como
 * que se ha completado el juego).
 */
public class Juego {

    public static void main(String[] args) throws ZonaBloqueadaException, EnergiaInsuficienteException, JuegoException {
        
        Logger.registrarEvento("COMIENZA EL JUEGO");
        Mundo mundo = new Mundo();
        Scanner leer = new Scanner(System.in);
        Personaje jugador = null;
        Enemigo enemigo = null;
        boolean continua = true;

        System.out.println("Bienvenid@ a las Tierras de Zaltor");
        System.out.println("Como te llamas?");
        String nombre = leer.nextLine();
        System.out.println("Encantado!! " + nombre);
        Logger.registrarEvento("El jugador se llama: " + nombre);
        do {
            try {
                Logger.registrarEvento("El jugador debe elegir personaje");
                System.out.println("Elije al personaje que te acompañará durante tu aventura:");
                System.out.println("Guerrero, Ninja o Chaman");
                String eleccion = leer.nextLine();
                if (eleccion.equalsIgnoreCase("Guerrero")) {
                    Logger.registrarEvento("El jugador elige usar al Guerrero");
                    continua = false;
                    int contador = 0;
                    jugador = new Guerrero();
                    while (contador <= 3 && jugador.vida > 0) {
                        contador++;
                        System.out.println("--- Guerrero --->  " + jugador.toString());
                        System.out.println("Te adentras en las tierras y...");
                        mundo.caminosAlternativos(jugador);
                        mundo.iniciarCombate(jugador, mundo.generarEnemigoAleatorio());
                        if (contador == 3) {
                            System.out.println("CASI HAS COMPLETADO EL JUEGO");
                            System.out.println("TE ENFRENTAS AL REY MORGAN EN EL COMBATE FINAL");
                            Logger.registrarEvento("El enemigo es el Rey Morgan");
                            Enemigo morgan = new ReyMorgan();
                            mundo.iniciarCombate(jugador, morgan);

                            if (jugador.vida <= 0) {
                                Logger.registrarEvento("El jugador ha sido derrotado");
                                throw new JuegoException("HAS CAIDO EN COMBATE.");
                            }
                            if (jugador.vida > 0 && morgan.vida <= 0) {
                                Logger.registrarEvento("El Rey Morgan es eliminado");
                                System.out.println(" Derrotaste a Morgan y te hiciste Rey de las Tierras de Zaltor!!");
                                System.out.println("ENHORABUENA HAS COMPLETADO EL JUEGO");
                                jugador.vida = 0;
                            }

                        }

                    }

                }

                if (eleccion.equalsIgnoreCase("Ninja")) {
                    Logger.registrarEvento("El jugador elige usar al Ninja");
                    continua = false;
                    int contador = 0;
                    jugador = new Ninja();
                    while (contador <= 3 && jugador.vida > 0) {
                        contador++;
                        System.out.println("Ninja ---> " + jugador.toString());
                        System.out.println("Te adentras en las tierras y...");
                        mundo.caminosAlternativos(jugador);
                        mundo.iniciarCombate(jugador, mundo.generarEnemigoAleatorio());

                        if (contador == 3) {
                            System.out.println(" CASI HAS COMPLETADO EL JUEGO");
                            System.out.println(" ES HORA DE DESAFIAR AL REY MORGAN");
                            Logger.registrarEvento("El enemigo es el Rey Morgan");
                            Enemigo morgan = new ReyMorgan();
                            mundo.iniciarCombate(jugador, morgan);

                            if (jugador.vida <= 0) {
                                Logger.registrarEvento("El jugador ha sido eliminado");
                                throw new JuegoException("HAS CAIDO EN COMBATE.");
                            }
                            if (jugador.vida > 0 && morgan.vida <= 0) {
                                Logger.registrarEvento("El Rey Morgan ha sido eliminado");
                                System.out.println(" Derrotaste a Morgan y te hiciste Rey de las Tierras de Zaltor!!");
                                System.out.println("ENHORABUENA HAS COMPLETADO EL JUEGO");
                                jugador.vida = 0;
                            }

                        }
                    }
                    if (jugador.vida <= 0) {
                        Logger.registrarEvento("FIN DEL JUEGO");
                        System.out.println("FIN DEL JUEGO");
                    }
                }

                if (eleccion.equalsIgnoreCase("Chaman")) {
                    Logger.registrarEvento("El jugador elige usar al Chaman");
                    continua = false;
                    int contador = 0;
                    jugador = new Chaman();
                    while (contador <=3 && jugador.vida > 0) {
                        contador++;
                        System.out.println("Chaman ---> " + jugador.toString());
                        System.out.println("Te adentras en las tierras y...");
                        mundo.caminosAlternativos(jugador);
                        mundo.iniciarCombate(jugador, mundo.generarEnemigoAleatorio());

                        if (contador == 3) {
                            System.out.println("CASI HAS COMPLETADO EL JUEGO");
                            System.out.println("TE ENFRENTAS EN EL COMBATE FINAL AL REY MORGAN");
                            Logger.registrarEvento("El enemigo es el Rey Morgan");
                            Enemigo morgan = new ReyMorgan();
                            mundo.iniciarCombate(jugador, morgan);

                            if (jugador.vida <= 0) {
                                Logger.registrarEvento("El jugador ha sido eliminado");
                                throw new JuegoException("HAS CAIDO EN COMBATE.");
                            }
                            if (jugador.vida > 0 && morgan.vida <= 0) {
                                Logger.registrarEvento("El Rey Morgan ha sido eliminado");
                                System.out.println(" Derrotaste a Morgan y te hiciste Rey de las Tierras de Zaltor!!");
                                System.out.println("ENHORABUENA HAS COMPLETADO EL JUEGO");
                                jugador.vida = 0;
                            }

                        }
                    }
                }
            } catch (InputMismatchException ex) {
                Logger.registrarEvento("El jugador no pulsa bien la entrada por teclado");
                System.out.println("Debe ingresar obligatoriamente el nombre correcto del personaje.");
                leer.next();
            }
        } while (continua);
        Logger.registrarEvento("El jugador completa el juego");
        System.out.println("PRONTO HABRA NUEVAS ACTUALIZACIONES, GRACIAS POR JUGAR ;)");
    }
}
