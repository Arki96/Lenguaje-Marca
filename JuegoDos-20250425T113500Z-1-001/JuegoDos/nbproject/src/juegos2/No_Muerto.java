package juegos2;

/**
 *
 * CREAMOS LA CLASE NO MUERTO QUE HEREDA DE ENEMIGO Y LE DAMOS VALORES A SUS
 * ESTADISTICAS, ADEMAS DE AÑADIRLE EL METODO ATAQUE ESPECIAL PARA QUE PUEDA
 * AUMENTAR SU VIDA EN BASE A SU HABILIDAD
 */
public class No_Muerto extends Enemigo {


    public No_Muerto() {
        super(100, 15, 8, 40, 10, 0,50, true);

    }

    @Override
    public void ataqueEsp() throws EnergiaInsuficienteException {
        if (this.energia >= 5) {
            System.out.println("Ataque especial del No Muerto");
            this.vida += habilidadEspecial;
            this.energia -= 5;
        } else {
            throw new EnergiaInsuficienteException("No queda energia para el ataque especial");
        }
    }

    @Override
    public void estaVivo() {
        System.out.println("Vida: " + vida);
        super.estaVivo();
    }
}
