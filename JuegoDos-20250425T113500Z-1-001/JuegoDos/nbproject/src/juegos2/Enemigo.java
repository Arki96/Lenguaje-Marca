package juegos2;

/**
 *
 * VAMOS A CREAR ESTA CLASE QUE HEREDA DE PERSONAJE PARA DEFINIR A LOS ENEMIGOS
 * DENTRO DEL JUEGO
 */
public class Enemigo extends Personaje {

    public Enemigo(int vida, int ataque, int defensa, int velocidad, int habilidadEspecial, int nivel, int energia, boolean estaVivo) {
        super(vida, ataque, defensa, velocidad, habilidadEspecial, nivel, energia, estaVivo);
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
