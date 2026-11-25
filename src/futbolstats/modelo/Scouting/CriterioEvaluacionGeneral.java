package futbolstats.modelo.Scouting;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CriterioEvaluacionGeneral implements CriterioEvaluacion {
    private final double puntosPorGol;
    private final double puntosPorRemate;
    private final double descuentoPorAmarilla;
    private final double descuentoPorRoja;
    private final double descuentoPorFalta;

    public CriterioEvaluacionGeneral() {
        this(5.0, 1.5, 1.0, 4.0, 0.5);
    }

    public CriterioEvaluacionGeneral(double puntosPorGol,
                                     double puntosPorRemate,
                                     double descuentoPorAmarilla,
                                     double descuentoPorRoja,
                                     double descuentoPorFalta) {
        if (puntosPorGol < 0 || puntosPorRemate < 0 ||
                descuentoPorAmarilla < 0 || descuentoPorRoja < 0 ||
                descuentoPorFalta < 0) {
            throw new IllegalArgumentException("Las ponderaciones no pueden ser negativas.");
        }

        this.puntosPorGol = puntosPorGol;
        this.puntosPorRemate = puntosPorRemate;
        this.descuentoPorAmarilla = descuentoPorAmarilla;
        this.descuentoPorRoja = descuentoPorRoja;
        this.descuentoPorFalta = descuentoPorFalta;
    }

    @Override
    public double calcularPuntaje(EstadisticasScouting estadisticas) {
        if (estadisticas == null) {
            return 0;
        }

        return estadisticas.getGoles() * puntosPorGol
                + estadisticas.getRemates() * puntosPorRemate
                - estadisticas.getTarjetasAmarillas() * descuentoPorAmarilla
                - estadisticas.getTarjetasRojas() * descuentoPorRoja
                - estadisticas.getFaltas() * descuentoPorFalta;
    }

    @Override
    public String getDescripcion() {
        return "Ponderaciones: +" + formato(puntosPorGol) + " por gol, +" + formato(puntosPorRemate)
                + " por remate, -" + formato(descuentoPorAmarilla) + " por amarilla, -"
                + formato(descuentoPorRoja) + " por roja y -" + formato(descuentoPorFalta) + " por falta.";
    }

    private String formato(double valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("es", "AR"));
        DecimalFormat formato = new DecimalFormat("0.##", simbolos);
        return formato.format(valor);
    }
}
