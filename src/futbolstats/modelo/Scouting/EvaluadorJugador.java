package futbolstats.modelo.Scouting;

import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Enumeraciones.ColorTarjeta;
import futbolstats.modelo.Enumeraciones.EstadoPartido;
import futbolstats.modelo.Enumeraciones.TipoGol;
import futbolstats.modelo.Eventos.*;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

public class EvaluadorJugador {
    private CriterioEvaluacion criterio;

    public EvaluadorJugador() {
        this(new CriterioEvaluacionGeneral());
    }

    public EvaluadorJugador(CriterioEvaluacion criterio) {
        this.criterio = criterio;
    }

    public InformeJugador evaluar(Jugador jugador, Torneo torneo) throws ReglaNegocioException {
        if (jugador == null || torneo == null) {
            throw new ReglaNegocioException("Se necesita un jugador y un torneo para generar el informe.");
        }
        if (criterio == null) {
            throw new ReglaNegocioException("No se configuro un criterio de evaluacion.");
        }

        EstadisticasScouting estadisticas = new EstadisticasScouting();

        for (Partido partido : torneo.getPartidos()) {
            if (partido == null || partido.getEstado() != EstadoPartido.FINALIZADO) {
                continue;
            }

            boolean tuvoEventosEnPartido = false;

            for (Evento evento : partido.getEventos()) {
                if (evento == null) {
                    continue;
                }

                boolean jugadorInvolucrado = evento.getJugadorInvolucrado() == jugador;

                // En un Cambio, tambien cuenta el jugador que ingresa.
                if (evento instanceof Cambio) {
                    Cambio cambio = (Cambio) evento;
                    jugadorInvolucrado = jugadorInvolucrado || cambio.getJugadorEntra() == jugador;
                }

                if (!jugadorInvolucrado) {
                    continue;
                }

                tuvoEventosEnPartido = true;

                if (evento instanceof Gol) {
                    Gol gol = (Gol) evento;
                    if (gol.getTipoGol() != TipoGol.EN_CONTRA) {
                        estadisticas.registrarGol();
                    }
                } else if (evento instanceof Remate) {
                    estadisticas.registrarRemate();
                } else if (evento instanceof Tarjeta) {
                    Tarjeta tarjeta = (Tarjeta) evento;
                    if (tarjeta.getColor() == ColorTarjeta.AMARILLA) {
                        estadisticas.registrarAmarilla();
                    } else if (tarjeta.getColor() == ColorTarjeta.ROJA) {
                        estadisticas.registrarRoja();
                    }
                } else if (evento instanceof Falta) {
                    estadisticas.registrarFalta();
                }
            }

            if (tuvoEventosEnPartido) {
                estadisticas.registrarPartidoConEventos();
            }
        }

        double puntaje = criterio.calcularPuntaje(estadisticas);
        return new InformeJugador(jugador, estadisticas, puntaje, criterio.getDescripcion());
    }
}
