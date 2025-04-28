package juegos2;
/**
 * En esta clase padre llamada Personaje vamos a crear todas las variables y todos los atributos necesarios para
 * la funcionalidad del juego.
 * @author Usuario
 */
public abstract class Personaje {

    int vida;
    int ataque;
    int defensa;
    int velocidad;
    int habilidadEspecial;
    int nivel;
    boolean estaVivo;
    int energia;

    public Personaje(int vida, int ataque, int defensa, int velocidad, int habilidadEspecial, int nivel, int energia, boolean estaVivo) {
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.habilidadEspecial = habilidadEspecial;
        this.nivel = nivel;
        this.energia = energia;
        this.estaVivo = true;
    }

    public void recibirDanio(int cantidad) throws JuegoException {
        if (this.vida > 50) {
            this.vida -= cantidad;
        }
        if (this.vida <= 50) {
            this.vida += this.defensa;
            this.vida -= cantidad;
            System.out.println("La defensa redujo el daño");
        }
        if (this.vida <= 0) {
            throw new JuegoException("el combate a terminado");
        }
    }

    public void recibirExp() {
        this.vida += 20;
        this.ataque += 7;
        this.defensa += 2;
        this.velocidad += 5;
        nivel += 1;
        if (nivel <= 3) {
            System.out.println("ENHORABUENA, obtuviste puntos de experiencia");
            System.out.println("Has subido al nivel: " + nivel);
            if (nivel == 4) {
                System.out.println("Has alcanzado el nivel MAXIMO");
            }
        }
    }

    public abstract void ataqueEsp() throws EnergiaInsuficienteException;

    public int habilidadEsp() throws EnergiaInsuficienteException {
        return habilidadEspecial;
    }

    public void estaVivo() {
        if (this.vida > 0) {
            this.estaVivo = true;
        }
    }

    @Override
    public String toString() {
        return "Skills: " + "vida= " + vida + ", ataque= " + ataque + ", defensa= " + defensa + ", velocidad= " + velocidad + ", habilidadEspecial= " + habilidadEspecial + ",nivel= " + nivel + '}';
    }
}