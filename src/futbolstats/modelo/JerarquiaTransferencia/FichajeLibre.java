package futbolstats.modelo.JerarquiaTransferencia;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;

public class FichajeLibre extends Transferencia {
    private static final int MAX_PLANTEL = 30;

    private Club clubDestino;
    private int nuevoDorsal;

    public FichajeLibre(Jugador jugador, Club clubDestino, int nuevoDorsal, LocalDate fecha) {
        super(jugador, fecha, 0.0);
        this.clubDestino = clubDestino;
        this.nuevoDorsal = nuevoDorsal;
    }

    @Override
    public void validar(Torneo torneo) throws ReglaNegocioException {
        if (jugador == null || clubDestino == null) {
            throw new ReglaNegocioException("Datos incompletos para fichar al jugador libre.");
        }
        if (!torneo.getClubesInscriptos().contains(clubDestino)) {
            throw new ReglaNegocioException("El club destino no esta inscripto en el torneo.");
        }
        if (!torneo.estaLibre(jugador)) {
            throw new ReglaNegocioException("El jugador no figura en la lista de jugadores libres del torneo.");
        }
        if (clubDestino.obtenerPlantel().getCantidadJugadores() >= MAX_PLANTEL) {
            throw new ReglaNegocioException("El plantel de " + clubDestino.getNombre() + " ya alcanzo el cupo maximo de 30 jugadores.");
        }
        if (clubDestino.obtenerPlantel().existeDorsal(nuevoDorsal)) {
            throw new ReglaNegocioException("El dorsal #" + nuevoDorsal + " ya esta ocupado en " + clubDestino.getNombre() + ".");
        }
    }

    @Override
    public void ejecutar(Torneo torneo) throws ReglaNegocioException {
        torneo.quitarJugadorLibre(jugador);
        jugador.setNumeroCamiseta(nuevoDorsal);
        clubDestino.obtenerPlantel().agregarJugador(jugador);
    }

    @Override
    public String generarDetalle() {
        return "[" + fecha + "] FICHAJE LIBRE | Jugador: " + jugador.getNombre() + " " + jugador.getApellido()
                + " | Destino: " + clubDestino.getNombre()
                + " | Dorsal: #" + nuevoDorsal;
    }

    @Override
    public Club getClubDestino() {
        return clubDestino;
    }
}