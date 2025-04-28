package juegos2;

/*
 * Aqui creamos una clase para el Enemigo final que este caso el Rey Morgan, le hemos dado bastante velocidad, vida y ataque para que pueda ser 
    dificil de batir con Ninja o Chaman a no ser que gracias al factor azar que propone el juego puedas ganarle facilmente.
 * 
 */
public class ReyMorgan extends Enemigo {



    public ReyMorgan() {
        super(150, 25, 1, 15, 28, 0,50, true);
    }

    @Override
    public int habilidadEsp() throws EnergiaInsuficienteException {
        super.habilidadEsp();
        if (energia < 10) {
            throw new EnergiaInsuficienteException("Sin energia");
        }
        energia -= 15;
        System.out.println("Lanzó un golpe critico");
        return habilidadEspecial;
    }

    @Override
    public void estaVivo() {
        System.out.println("Vida del Rey Morgan: " + vida);
        super.estaVivo();
    }

}
