package futbolstats.modelo.MejoraPartido;

import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Enumeraciones.EstadoPartido;
import futbolstats.modelo.Enumeraciones.EstadoTorneo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidadorProgramacion {

    public List<ConflictoProgramacion> validar(Torneo torneo, Partido partido) {
        List<ConflictoProgramacion> conflictos = new ArrayList<>();

        if (torneo == null || partido == null) {
            agregar(conflictos, TipoConflictoProgramacion.DATOS_INCOMPLETOS,
                    "El torneo y el partido deben estar informados.");
            return conflictos;
        }

        if (torneo.getEstado() == EstadoTorneo.FINALIZADO) {
            agregar(conflictos, TipoConflictoProgramacion.TORNEO_FINALIZADO,
                    "No se pueden programar partidos en un torneo finalizado.");
        }

        if (partido.getEstado() != EstadoPartido.PROGRAMADO) {
            agregar(conflictos, TipoConflictoProgramacion.ESTADO_PARTIDO_INVALIDO,
                    "El partido debe estar en estado PROGRAMADO para poder incorporarlo al calendario.");
        }

        if (torneo.getPartidos().contains(partido)) {
            agregar(conflictos, TipoConflictoProgramacion.PARTIDO_DUPLICADO,
                    "El partido ya se encuentra registrado en el torneo.");
        }

        if (partido.getClubLocal() == null || partido.getClubVisitante() == null ||
                partido.getFecha() == null || partido.getHora() == null) {
            agregar(conflictos, TipoConflictoProgramacion.DATOS_INCOMPLETOS,
                    "El partido debe tener dos clubes, fecha y hora válidos.");
        } else {
            if (partido.getClubLocal() == partido.getClubVisitante()) {
                agregar(conflictos, TipoConflictoProgramacion.CLUBES_IGUALES,
                        "El club local y el visitante no pueden ser el mismo.");
            }

            boolean localInscripto = torneo.getClubesInscriptos().contains(partido.getClubLocal());
            boolean visitanteInscripto = torneo.getClubesInscriptos().contains(partido.getClubVisitante());
            if (!localInscripto || !visitanteInscripto) {
                agregar(conflictos, TipoConflictoProgramacion.CLUB_NO_INSCRIPTO,
                        "Ambos clubes deben estar inscriptos en el torneo.");
            }

            LocalDate fecha = partido.getFecha();
            LocalDate inicio = torneo.getFechaInicio();
            LocalDate fin = torneo.getFechaFin();
            if (inicio == null || fin == null || fecha.isBefore(inicio) || fecha.isAfter(fin)) {
                agregar(conflictos, TipoConflictoProgramacion.FECHA_FUERA_DE_PERIODO,
                        "La fecha del partido debe estar dentro del período del torneo.");
            }
        }

        if (partido.getEstadio() == null) {
            agregar(conflictos, TipoConflictoProgramacion.ESTADIO_NO_ASIGNADO,
                    "Se debe asignar un estadio antes de programar el partido.");
        }

        if (partido.getFechaHoraInicio() == null || partido.getFechaHoraFin() == null) {
            return conflictos;
        }

        for (Partido existente : torneo.getPartidos()) {
            if (existente == null || existente == partido || !ocupaAgenda(existente)) {
                continue;
            }
            if (existente.getFechaHoraInicio() == null || existente.getFechaHoraFin() == null) {
                continue;
            }

            boolean seSuperponen = seSuperponen(partido, existente);
            if (!seSuperponen) {
                continue;
            }

            if (comparteClub(partido, existente)) {
                agregar(conflictos, TipoConflictoProgramacion.CLUB_OCUPADO,
                        "Uno de los clubes ya tiene otro partido en ese horario: " +
                                existente.getClubLocal().getNombre() + " vs. " +
                                existente.getClubVisitante().getNombre() + ".");
            }

            if (partido.getEstadio() != null && partido.getEstadio() == existente.getEstadio()) {
                agregar(conflictos, TipoConflictoProgramacion.ESTADIO_OCUPADO,
                        "El estadio " + partido.getEstadio().getNombre() +
                                " ya está asignado a otro partido en ese horario.");
            }
        }

        return conflictos;
    }

    private boolean ocupaAgenda(Partido partido) {
        return partido.getEstado() == EstadoPartido.PROGRAMADO ||
                partido.getEstado() == EstadoPartido.EN_JUEGO;
    }

    private boolean seSuperponen(Partido uno, Partido otro) {
        LocalDateTime inicioUno = uno.getFechaHoraInicio();
        LocalDateTime finUno = uno.getFechaHoraFin();
        LocalDateTime inicioOtro = otro.getFechaHoraInicio();
        LocalDateTime finOtro = otro.getFechaHoraFin();

        return inicioUno.isBefore(finOtro) && inicioOtro.isBefore(finUno);
    }

    private boolean comparteClub(Partido uno, Partido otro) {
        return uno.getClubLocal() == otro.getClubLocal() ||
                uno.getClubLocal() == otro.getClubVisitante() ||
                uno.getClubVisitante() == otro.getClubLocal() ||
                uno.getClubVisitante() == otro.getClubVisitante();
    }

    private void agregar(List<ConflictoProgramacion> conflictos,
                         TipoConflictoProgramacion tipo, String descripcion) {
        conflictos.add(new ConflictoProgramacion(tipo, descripcion));
    }
}