package futbolstats.modelo.JerarquiaTransferencia;

import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;

public class RescisionContrato extends Transferencia{
    public RescisionContrato(Jugador jugador, LocalDate fecha, double monto) {
        super(jugador, fecha, monto);
    }

    @Override
    public void validar(Torneo torneo) throws ReglaNegocioException {

    }

    @Override
    public void ejecutar(Torneo torneo) throws ReglaNegocioException {

    }

    @Override
    public String generarDetalle() {
        return "";
    }
}
