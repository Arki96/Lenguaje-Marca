package logica;

/**
 *
 * VAMOS A CREAR ESTA CLASE QUE HEREDA DE PERSONAJE PARA DEFINIR A LOS ENEMIGOS
 * DENTRO DEL JUEGO
 */
public class Enemigo extends Personaje {

    public Enemigo(String nombre, int vida, int ataque, int defensa, int velocidad, int habilidadEspecial, int nivel, boolean estaVivo, int energia, int vidaMaxima) {
        super(nombre,vida, ataque, defensa, velocidad, habilidadEspecial, nivel, estaVivo, energia, vidaMaxima);
    }

    
    public void ataqueEspecial() {
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void ataqueEsp() throws EnergiaInsuficienteException {
        throw new UnsupportedOperationException("Sin energia"); 
    }

}
