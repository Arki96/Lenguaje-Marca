package logica;

/**
 *
 * CREMOAS LA CLASE LOBO SALVAJE QUE HEREDA DE ENEMIGO Y LA AÑADIMOS VALORES A
 * SUS ESTADISTICAS
 */
public class LoboSalvaje extends Enemigo {

    public LoboSalvaje() {
        super(80, 12, 5, 30, 14, 0, true,50,80);
    }

    
    
    @Override
    public int habilidadEsp() throws EnergiaInsuficienteException {
        super.habilidadEsp();
        if (energia < 0) {
            throw new EnergiaInsuficienteException("Sin energia");
        }
        energia -= 5;
        System.out.println("Lanzó una Mordida Rapida");
        return habilidadEspecial;
    }

    @Override
    public void estaVivo() {
        System.out.println("Vida del Lobo Salvaje : " + vida);
        super.estaVivo();
    }

}
