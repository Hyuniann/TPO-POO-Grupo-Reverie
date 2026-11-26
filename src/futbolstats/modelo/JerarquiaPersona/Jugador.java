package futbolstats.modelo.JerarquiaPersona;

import futbolstats.modelo.JerarquiaTransferencia.Transferencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Jugador extends Persona {
    private String posicion;
    private int numeroCamiseta;
    private double valorMercado;
    private int partidosJugados;
    private int golesAcumulados;
    private int tarjetasAcumuladas;

    private List<Transferencia> historialTransferencias;


    public Jugador(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String posicion, int numeroCamiseta, double valorMercado) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.valorMercado = valorMercado;
        this.partidosJugados = 0;
        this.golesAcumulados = 0;
        this.tarjetasAcumuladas = 0;
        this.historialTransferencias = new ArrayList<>();
    }

    public int getGolesAcumulados() { return golesAcumulados; }
    public int getTarjetasAcumuladas() { return tarjetasAcumuladas; }

    public void incrementarTarjetasAmarillas() { this.tarjetasAcumuladas++; }
    public void incrementarGoles() { this.golesAcumulados++; }
    public void expulsar() { this.tarjetasAcumuladas ++; }

    public void obtenerEstadisticas() {
        System.out.println(getDatosPersonales() + " | Goles: " + golesAcumulados + " | Tarjetas: " + tarjetasAcumuladas);
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(int numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public double getValorMercado() {
        return valorMercado;
    }

    public void setValorMercado(double valorMercado) {
        this.valorMercado = valorMercado;
    }

    public void agregarTransferencia(Transferencia transferencia) {
        if (transferencia != null) {
            this.historialTransferencias.add(transferencia);
        }
    }

    public List<Transferencia> getHistorialTransferencias() {
        return historialTransferencias;
    }


}
