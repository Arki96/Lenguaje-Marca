package juegos2;

/**
 *
 * eN ESTE MÉTODO CREAMOS LA CLASE GUERRERO QUE HEREDA DE PERSONAJE Y LE
 * AÑADIMOS VALORES A SUS ESTADISTICAS
 */
public class GuerreroOscuro extends Enemigo {
    
    public GuerreroOscuro() {
        super(130, 20, 10, 50, 25, 0,50, true);
     
    }
    
    @Override
    public int habilidadEsp() throws EnergiaInsuficienteException {
        super.habilidadEsp();
        if (energia == 10) {
            throw new EnergiaInsuficienteException("Sin energia");
        } else {
        energia -= 10;
        System.out.println("Lanzó un espadazo letal");
        return habilidadEspecial;}
    }

    @Override
    public void estaVivo() {
        System.out.println("Vida del Guerrero Oscuro : " + vida);
        super.estaVivo();
    }
}
