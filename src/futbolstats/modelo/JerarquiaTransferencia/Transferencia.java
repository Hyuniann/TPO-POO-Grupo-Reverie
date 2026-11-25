package futbolstats.modelo.JerarquiaTransferencia;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;

public abstract class Transferencia {
    protected Jugador jugador;
    protected LocalDate fecha;
    protected double monto;

    public Transferencia(Jugador jugador, LocalDate fecha, double monto){
        this.jugador = jugador;
        this.fecha = (fecha != null) ? fecha : LocalDate.now();
        this.monto = monto;
    }

    public abstract void validar(Torneo torneo) throws ReglaNegocioException;

    public abstract void ejecutar(Torneo torneo) throws ReglaNegocioException;

    public abstract String generarDetalle();

    public Club getClubOrigen() {
        return null; //no todas las operaciones en Transferencia tienen los 2 clubes (origen y destino), las hijas lo sobreescriben
    }

    public Club getClubDestino() {
        return null;
    }
    public Jugador getJugador() {
        return jugador;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getMonto() {
        return monto;
    }
}
