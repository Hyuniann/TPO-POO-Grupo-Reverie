package futbolstats.modelo.JerarquiaTransferencia;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;

public class RescisionContrato extends Transferencia {
    private static final int MIN_PLANTEL = 1;

    private Club clubOrigen;
    private String motivo;

    public RescisionContrato(Jugador jugador, Club clubOrigen, String motivo, LocalDate fecha) {
        super(jugador, fecha, 0.0);
        this.clubOrigen = clubOrigen;
        this.motivo = motivo;
    }

    @Override
    public void validar(Torneo torneo) throws ReglaNegocioException {
        if (jugador == null || clubOrigen == null) {
            throw new ReglaNegocioException("Datos incompletos para registrar la rescision.");
        }
        if (!torneo.getClubesInscriptos().contains(clubOrigen)) {
            throw new ReglaNegocioException("El club no esta inscripto en el torneo.");
        }
        if (!clubOrigen.obtenerPlantel().contieneJugador(jugador)) {
            throw new ReglaNegocioException("El jugador no pertenece al plantel de " + clubOrigen.getNombre() + ".");
        }
        if (clubOrigen.obtenerPlantel().getCantidadJugadores() <= MIN_PLANTEL) {
            throw new ReglaNegocioException("No se puede rescindir: el club quedaria por debajo del plantel minimo.");
        }
    }

    @Override
    public void ejecutar(Torneo torneo) {
        clubOrigen.obtenerPlantel().quitarJugador(jugador);
        torneo.agregarJugadorLibre(jugador);
    }

    @Override
    public String generarDetalle() {
        return "[" + fecha + "] RESCISION DE CONTRATO | Jugador: " + jugador.getNombre() + " " + jugador.getApellido()
                + " | Club: " + clubOrigen.getNombre()
                + " | Estado: Jugador Libre (" + motivo + ")";
    }

    @Override
    public Club getClubOrigen() {
        return clubOrigen;
    }
}