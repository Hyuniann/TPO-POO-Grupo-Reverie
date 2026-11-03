package futbolstats.modelo.Eventos;

import futbolstats.modelo.Enumeraciones.ColorTarjeta;
import futbolstats.modelo.JerarquiaPersona.Jugador;

public class Tarjeta extends Evento {
    private String motivo;
    private ColorTarjeta color;

    public Tarjeta(int minuto, String descripcion, Jugador jugadorInvolucrado, String motivo, ColorTarjeta color) {
        super(minuto, descripcion, jugadorInvolucrado);
        this.motivo = motivo;
        this.color = color;
    }

    public ColorTarjeta getColor() {
        return color;
    }

    @Override
    public String getDescripcion() {
        return "Minuto " + getMinuto() + " - Tarjeta " + color + " a " + getJugadorInvolucrado().getApellido() + " (Motivo: " + motivo + ")";
    }
}