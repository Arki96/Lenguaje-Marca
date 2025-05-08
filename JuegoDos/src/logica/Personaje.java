package logica;

/**
 * En esta clase padre llamada Personaje vamos a crear todas las variables y
 * todos los atributos necesarios para la funcionalidad del juego.
 *
 * @author Usuario
 */
public abstract class Personaje {
    public int id;
    public String nombre;
    public int vida;
    public int ataque;
    public int defensa;
    public int velocidad;
    public int habilidadEspecial;
    public int nivel;
    public boolean estaVivo;
    public int energia;
    public int vidaMaxima;

    public Personaje(String nombre, int vida, int ataque, int defensa, int velocidad, int habilidadEspecial, int nivel, boolean estaVivo, int energia, int vidaMaxima) {
        this.nombre = nombre;
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.habilidadEspecial = habilidadEspecial;
        this.nivel = nivel;
        this.estaVivo = estaVivo;
        this.energia = energia;
        this.vidaMaxima = vidaMaxima;
    }

    public void recibirDanio(int cantidad) throws JuegoException {
        int danioFinal = Math.max(1, cantidad - this.defensa);
        this.vida -= danioFinal;
        this.vida = Math.max(0, this.vida);

        if (this.vida <= 0) {
            this.estaVivo = false; // Asegurar que estaVivo se actualice
            throw new JuegoException("¡Combate terminado!");
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
            Logger.registrarEvento("El jugador suba al nivel: " + nivel);
            if (nivel == 4) {
                Logger.registrarEvento("El jugador alcanza el nivel max de experiencia");
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
