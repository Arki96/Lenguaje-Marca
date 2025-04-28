
package juegos2;

/**
 *
 * CREAMOS LA CLASE CHAMAN QUE HEREDA DE PERSONAJE, LE DAMOS VALORES A SUS ESTADISTICAS
 * Y SOBREESCRIBIMOS UN METODO QUE HAGA QUE SU HABILIDAD ESPECIAL SUBA SUS PUNTOS DE VIDA.
 */
public class Chaman extends Personaje {

    public Chaman() {
        super(120, 12, 10, 10, 20, 0,50, true);
    }

    @Override
    public void ataqueEsp() throws EnergiaInsuficienteException {
        
        int curar = 20;
        System.out.println("El Chaman usa Curacion Mistica ");
        int contadorHabilidad = 0;
       
        if (contadorHabilidad < 8) {
            vida += curar;
            contadorHabilidad++;
            System.out.println("Vida del Chaman: " +vida);
        } if (contadorHabilidad == 8 || contadorHabilidad < 10) {
            vida+=10;
            System.out.println("La curación ya no surge tanto efecto");
            System.out.println("Vida del Chaman: " +vida);
            contadorHabilidad++;
        } if (contadorHabilidad > 10){
            throw new EnergiaInsuficienteException ("Energia insuficiente");
        }
    }

    @Override
    public void estaVivo() {
        System.out.println("Vida del Chaman : " + vida);
        super.estaVivo();
    }
}
