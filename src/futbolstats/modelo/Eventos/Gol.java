package futbolstats.modelo.Eventos;

import futbolstats.modelo.Enumeraciones.TipoGol;
import futbolstats.modelo.JerarquiaPersona.Jugador;

public class Gol extends Evento {
    private TipoGol tipoGol;

    public Gol(int minuto, String descripcion, Jugador jugadorInvolucrado, TipoGol tipoGol) {
        super(minuto, descripcion, jugadorInvolucrado);
        this.tipoGol = tipoGol;
    }

    public TipoGol getTipoGol() {
        return tipoGol;
    }

    @Override
    public String getDescripcion() {
        return "Minuto " + getMinuto() + " - GOL (" + tipoGol + ") de " + getJugadorInvolucrado().getApellido();
    }
}