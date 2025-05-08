package logica;

/**
 *
 * eN ESTE MÉTODO CREAMOS LA CLASE GUERRERO QUE HEREDA DE PERSONAJE Y LE DAMOS
 * VALORES A SUS ESTADÍSTICAS
 */
public class Guerrero extends Personaje {

    /**
     *
     */

    public Guerrero(String nombre, int vida, int ataque, int defensa, int velocidad, int habilidadEspecial, int nivel, boolean estaVivo, int energia, int vidaMaxima) {
        super(nombre, vida, ataque, defensa, velocidad, habilidadEspecial, nivel, estaVivo, energia, vidaMaxima);
    }

    public Guerrero() {
        super("Guerrero",150, 18, 12, 6, 36, 0, true, 50, 150);
    }
    
    @Override
    public void recibirExp() {
        habilidadEspecial = ataque * 2;
        super.recibirExp();
    }

    @Override
    public int habilidadEsp() throws EnergiaInsuficienteException {
        super.habilidadEsp();
        if (energia < 10) {
            throw new EnergiaInsuficienteException("Sin energia");
        }
        energia -= 10;
        System.out.println("Lanzó un golpe devastador! Energia: " +energia);
        return habilidadEspecial;
    }

    @Override
    public void estaVivo() {
        System.out.println("Vida del Guerrero : " + vida);
        super.estaVivo();
    }

    @Override
    public void ataqueEsp() throws EnergiaInsuficienteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
