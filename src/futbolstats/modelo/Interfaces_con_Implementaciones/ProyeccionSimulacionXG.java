package futbolstats.modelo.Interfaces_con_Implementaciones;

import futbolstats.modelo.Entidades_Principales.Torneo;

public class ProyeccionSimulacionXG implements EstrategiaProyeccion{
    @Override
    public double calcular(Torneo torneo) {
        return 82.0;
    }
}
