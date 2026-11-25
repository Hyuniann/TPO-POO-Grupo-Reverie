package futbolstats.modelo.JerarquiaPersona;
import java.time.LocalDate;

public class Jugador extends Persona {
    private String posicion;
    private int numeroCamiseta;
    private double valorMercadoInicial;
    private double valorMercadoActual;
    private int partidosJugados;
    private int golesAcumulados;
    private int tarjetasAcumuladas;

    public Jugador(String dni, String nombre, String apellido,
                   LocalDate fechaNacimiento, String posicion,
                   int numeroCamiseta, double valorMercado) {
        super(dni, nombre, apellido, fechaNacimiento);
        if (valorMercado < 0) {
            throw new IllegalArgumentException("El valor de mercado no puede ser negativo.");
        }
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.valorMercadoInicial = valorMercado;
        this.valorMercadoActual = valorMercado;
        this.partidosJugados = 0;
        this.golesAcumulados = 0;
        this.tarjetasAcumuladas = 0;
    }

    public int getGolesAcumulados() { return golesAcumulados; }
    public int getTarjetasAcumuladas() { return tarjetasAcumuladas; }

    public double getValorMercadoInicial() { return valorMercadoInicial; }
    public double getValorMercadoActual() { return valorMercadoActual; }

    public double getValorMercado() { return valorMercadoActual; }

    public void actualizarValorMercado(double nuevoValor) {
        if (nuevoValor < 0) {
            throw new IllegalArgumentException("El valor de mercado no puede ser negativo.");
        }
        this.valorMercadoActual = nuevoValor;
    }

    public void incrementarTarjetasAmarillas() { this.tarjetasAcumuladas++; }
    public void incrementarGoles() { this.golesAcumulados++; }
    public void expulsar() { this.tarjetasAcumuladas++; }

    public void obtenerEstadisticas() {
        System.out.println(getDatosPersonales() + " | Goles: " + golesAcumulados
                + " | Tarjetas: " + tarjetasAcumuladas);
    }
}
