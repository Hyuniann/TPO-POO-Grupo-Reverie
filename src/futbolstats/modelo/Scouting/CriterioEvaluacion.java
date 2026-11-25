package futbolstats.modelo.Scouting;

public interface CriterioEvaluacion {
    double calcularPuntaje(EstadisticasScouting estadisticas);

    String getDescripcion();
}