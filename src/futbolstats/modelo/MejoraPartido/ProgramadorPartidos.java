package futbolstats.modelo.MejoraPartido;

import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Excepciones.ReglaNegocioException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProgramadorPartidos {
    private ValidadorProgramacion validador;

    public ProgramadorPartidos() {
        this.validador = new ValidadorProgramacion();
    }

    public void programar(Torneo torneo, Partido partido) throws ReglaNegocioException {
        List<ConflictoProgramacion> conflictos = validador.validar(torneo, partido);

        if (!conflictos.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("No se pudo programar el partido:");
            for (ConflictoProgramacion conflicto : conflictos) {
                mensaje.append("\n- ").append(conflicto.getDescripcion());
            }
            throw new ReglaNegocioException(mensaje.toString());
        }

        torneo.programarPartido(partido);
    }

    public List<Partido> obtenerCalendario(Torneo torneo) {
        List<Partido> calendario = new ArrayList<>();
        if (torneo == null) {
            return calendario;
        }

        calendario.addAll(torneo.getPartidos());
        calendario.sort(Comparator.comparing(Partido::getFechaHoraInicio,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return calendario;
    }

    public List<ConflictoProgramacion> validar(Torneo torneo, Partido partido) {
        return validador.validar(torneo, partido);
    }
}
