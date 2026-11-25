package futbolstats.modelo.Scouting;

import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class InformeJugador {
    private final Jugador jugador;
    private final EstadisticasScouting estadisticas;
    private final double puntaje;
    private final String descripcionCriterio;
    private final double valorMercadoInicial;
    private double valorMercadoCalculado;
    private double variacionPorcentual;

    public InformeJugador(Jugador jugador, EstadisticasScouting estadisticas,
                          double puntaje, String descripcionCriterio) {
        if (jugador == null || estadisticas == null) {
            throw new IllegalArgumentException("El informe necesita un jugador y sus estadisticas.");
        }

        this.jugador = jugador;
        this.estadisticas = estadisticas;
        this.puntaje = puntaje;
        this.descripcionCriterio = descripcionCriterio == null
                ? "Criterio no especificado." : descripcionCriterio;
        this.valorMercadoInicial = jugador.getValorMercadoInicial();
        this.valorMercadoCalculado = jugador.getValorMercadoActual();
        this.variacionPorcentual = calcularVariacion(valorMercadoInicial, valorMercadoCalculado);
    }

    /** Constructor de compatibilidad con la version anterior del informe. */
    public InformeJugador(Jugador jugador, EstadisticasScouting estadisticas, double puntaje) {
        this(jugador, estadisticas, puntaje, "Criterio no especificado.");
    }

    public Jugador getJugador() { return jugador; }
    public EstadisticasScouting getEstadisticas() { return estadisticas; }
    public double getPuntaje() { return puntaje; }
    public String getDescripcionCriterio() { return descripcionCriterio; }
    public double getValorMercadoInicial() { return valorMercadoInicial; }
    public double getValorMercadoCalculado() { return valorMercadoCalculado; }
    public double getVariacionPorcentual() { return variacionPorcentual; }

    void registrarValorMercadoCalculado(double nuevoValor) {
        this.valorMercadoCalculado = nuevoValor;
        this.variacionPorcentual = calcularVariacion(valorMercadoInicial, nuevoValor);
    }

    public String generarResumen() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("========== INFORME DE SCOUTING ==========\n");
        resumen.append("Jugador: ").append(jugador.getNombre()).append(" ")
                .append(jugador.getApellido()).append("\n");
        resumen.append("Partidos con eventos registrados: ")
                .append(estadisticas.getPartidosConEventosRegistrados()).append("\n");
        resumen.append("Goles: ").append(estadisticas.getGoles()).append("\n");
        resumen.append("Remates: ").append(estadisticas.getRemates()).append("\n");
        resumen.append("Tarjetas amarillas: ").append(estadisticas.getTarjetasAmarillas()).append("\n");
        resumen.append("Tarjetas rojas: ").append(estadisticas.getTarjetasRojas()).append("\n");
        resumen.append("Faltas registradas: ").append(estadisticas.getFaltas()).append("\n");
        resumen.append("Puntaje de evaluacion: ").append(formato(puntaje)).append(" puntos\n");
        resumen.append("Criterio: ").append(descripcionCriterio).append("\n");
        resumen.append("Valor de mercado inicial: ").append(formato(valorMercadoInicial)).append(" u.m.\n");
        resumen.append("Valor de mercado actual: ").append(formato(valorMercadoCalculado)).append(" u.m.\n");
        resumen.append("Variacion respecto del valor inicial: ")
                .append(formato(variacionPorcentual)).append("%\n");
        resumen.append("=========================================");
        return resumen.toString();
    }

    private double calcularVariacion(double inicial, double actual) {
        if (inicial == 0) {
            return 0;
        }
        return ((actual - inicial) / inicial) * 100.0;
    }

    private String formato(double valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("es", "AR"));
        DecimalFormat formato = new DecimalFormat("#,##0.00", simbolos);
        return formato.format(valor);
    }
}
