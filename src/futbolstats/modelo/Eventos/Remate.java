package futbolstats.modelo.Eventos;

import futbolstats.modelo.Enumeraciones.ZonaRemate;
import futbolstats.modelo.JerarquiaPersona.Jugador;

public class Remate extends Evento {
    private ZonaRemate zonaRemate;

    public Remate(int minuto, String descripcion, Jugador jugadorInvolucrado, ZonaRemate zonaRemate) {
        super(minuto, descripcion, jugadorInvolucrado);
        this.zonaRemate = zonaRemate;
    }

    public ZonaRemate getZonaRemate() {
        return zonaRemate;
    }

    @Override
    public String getDescripcion() {
        return "Minuto " + getMinuto() + " - Remate desde " + zonaRemate + " por " + getJugadorInvolucrado().getApellido();
    }
}