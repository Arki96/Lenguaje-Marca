
package logica;

/**
 *
 * CREAMOS LA CLASE CHAMAN QUE HEREDA DE PERSONAJE, LE DAMOS VALORES A SUS ESTADISTICAS
 * Y SOBREESCRIBIMOS UN METODO QUE HAGA QUE SU HABILIDAD ESPECIAL SUBA SUS PUNTOS DE VIDA.
 */
public class Chaman extends Personaje {
    private int usosHabilidad = 50;
    private final int vidaMaxima = 120; 

    public Chaman() {
        super(120, 12, 10, 10, 20, 0, true, 50, 120);
       
    }

    @Override
    public void ataqueEsp() throws EnergiaInsuficienteException {
        if (energia < 10) {
            Logger.registrarEvento("El jugador se queda sin energia");
            throw new EnergiaInsuficienteException("Energia insuficiente");
        }
        
        energia -= 10;
        usosHabilidad++;
        int curacion = calcularCuracion();
        
        vida = Math.min(vida + curacion, vidaMaxima);
        System.out.println("El Chaman usa Curacion Mistica");
        System.out.println("Vida del Chaman: " + vida);
    }
    
    private int calcularCuracion() {
        if (usosHabilidad <= 8) return 20;
        if (usosHabilidad <= 10) {
            Logger.registrarEvento("El ataque especial del jugador es mas debil");
            System.out.println("La curación ya no surge tanto efecto");
            return 10;
        }
        return 0;
    }
}