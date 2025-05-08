package logica;

/**
 *
 * CREAMOS LA CLASE NINJA QUE HEREDA DE PERSONAJE PARA DEFINIR EL VALOR DE SUS
 * ESTADISTICAS
 */
public class Ninja extends Personaje {
    private int energia;
    public Ninja() {
        super("Ninja",100, 15, 8, 15, 20, 0, true,50,100);
        this.energia = 50;
    }

    @Override
    public void recibirExp() {
        habilidadEspecial = ataque + 5;
        super.recibirExp();
    }
    
    @Override
    public int habilidadEsp() throws EnergiaInsuficienteException {
        super.habilidadEsp();
        if (energia < 5) {
            throw new EnergiaInsuficienteException("Sin energia");
        }
        energia -= 5;
        System.out.println("Lanzó una triple patada");
        return habilidadEspecial;
    }

    @Override
    public void estaVivo() {
        System.out.println("Vida del Ninja : " + vida);
        super.estaVivo();
    }

    @Override
    public void ataqueEsp() throws EnergiaInsuficienteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
