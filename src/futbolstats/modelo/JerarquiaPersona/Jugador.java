package futbolstats.modelo.JerarquiaPersona;
import java.time.LocalDate;

public class Jugador extends Persona {
    private String posicion;
    private int numeroCamiseta;
    private double valorMercado;
    private int partidosJugados;
    private int golesAcumulados;
    private int tarjetasAcumuladas;


    public Jugador(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String posicion, int numeroCamiseta, double valorMercado) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.valorMercado = valorMercado;
        this.partidosJugados = 0;
        this.golesAcumulados = 0;
        this.tarjetasAcumuladas = 0;
    }

    public int getGolesAcumulados() { return golesAcumulados; }
    public int getTarjetasAcumuladas() { return tarjetasAcumuladas; }

    public void incrementarTarjetasAmarillas() { this.tarjetasAcumuladas++; }
    public void incrementarGoles() { this.golesAcumulados++; }
    public void expulsar() { this.tarjetasAcumuladas ++; }

    public void obtenerEstadisticas() {
        System.out.println(getDatosPersonales() + " | Goles: " + golesAcumulados + " | Tarjetas: " + tarjetasAcumuladas);
    }
}
