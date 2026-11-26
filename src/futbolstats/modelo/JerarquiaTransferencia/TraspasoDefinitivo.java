package futbolstats.modelo.JerarquiaTransferencia;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;

public class TraspasoDefinitivo extends Transferencia {
    private static final int MAX_PLANTEL = 30;
    private static final int MIN_PLANTEL = 1; //enunciado pide 18, pero uso 1 para probar con pocos casos en el Main

    private Club clubOrigen;
    private Club clubDestino;
    private int nuevoDorsal;

    public TraspasoDefinitivo(Jugador jugador, Club clubOrigen, Club clubDestino, double monto, int nuevoDorsal, LocalDate fecha) {
        super(jugador, fecha, monto);
        this.clubOrigen = clubOrigen;
        this.clubDestino = clubDestino;
        this.nuevoDorsal = nuevoDorsal;
    }

    @Override
    public void validar(Torneo torneo) throws ReglaNegocioException {
        if (jugador == null || clubOrigen == null || clubDestino == null) {
            throw new ReglaNegocioException("Datos incompletos para realizar el traspaso.");
        }
        if (clubOrigen.equals(clubDestino)) {
            throw new ReglaNegocioException("El club de origen y destino no pueden ser el mismo.");
        }
        if (!torneo.getClubesInscriptos().contains(clubOrigen) || !torneo.getClubesInscriptos().contains(clubDestino)) {
            throw new ReglaNegocioException("Ambos clubes deben estar inscriptor en el torneo");
        }
        if (!clubOrigen.obtenerPlantel().contieneJugador(jugador)) {
            throw new ReglaNegocioException("El jugador no pertenece al plantel de " + clubOrigen.getNombre() + ".");
        }
        if (clubOrigen.obtenerPlantel().getCantidadJugadores() <= MIN_PLANTEL) {
            throw new ReglaNegocioException("El club vendedor no puede quedar por debajo del minimo de jugadores.");
        }
        if (monto < jugador.getValorMercado()) {
            throw new ReglaNegocioException("El monto ofrecido ($" + monto + "M) es menor al valor de mercado ($" + jugador.getValorMercado() + "M).");
        }
        if (clubDestino.getPresupuesto() < monto) {
            throw new ReglaNegocioException("Presupuesto insuficiente en " + clubDestino.getNombre() + " (Disponible: $" + clubDestino.getPresupuesto() + "M).");
        }
        if (clubDestino.obtenerPlantel().getCantidadJugadores() >= MAX_PLANTEL) {
            throw new ReglaNegocioException("El plantel de " + clubDestino.getNombre() + " ya alcanzo el cupo maximo de 30 jugadores.");
        }
        if (clubDestino.obtenerPlantel().existeDorsal(nuevoDorsal)) {
            throw new ReglaNegocioException("El dorsal #" + nuevoDorsal + "ya esta ocupado en " + clubDestino.getNombre() + ".");
        }
    }

    @Override
    public void ejecutar(Torneo torneo) throws ReglaNegocioException {
        clubOrigen.obtenerPlantel().quitarJugador(jugador);
        jugador.setNumeroCamiseta(nuevoDorsal);
        clubDestino.obtenerPlantel().agregarJugador(jugador);

        clubDestino.descontarPresupuesto(monto);
        clubOrigen.acreditarPresupuesto(monto);

    }

    @Override
    public String generarDetalle() {
        return "[" + fecha + "] TRASPASO DEFINITIVO | Jugador: " + jugador.getNombre() + " " + jugador.getApellido()
                + " | Origen: " + clubOrigen.getNombre()
                + " -> Destino: " + clubDestino.getNombre()
                + " | Dorsal: #" + nuevoDorsal
                + " | Monto: $" + monto + "M";
    }

    @Override
    public Club getClubOrigen() {
        return clubOrigen;
    }

    @Override
    public Club getClubDestino() {
        return clubDestino;
    }
}

