package futbolstats.modelo.Eventos;

import futbolstats.modelo.Enumeraciones.TipoFalta;
import futbolstats.modelo.JerarquiaPersona.Jugador;

public class Falta extends Evento {
    private TipoFalta tipoFalta;

    public Falta(int minuto, String descripcion, Jugador jugadorInvolucrado, TipoFalta tipoFalta) {
        super(minuto, descripcion, jugadorInvolucrado);
        this.tipoFalta = tipoFalta;
    }

    public TipoFalta getTipoFalta() {
        return tipoFalta;
    }

    @Override
    public String getDescripcion() {
        return "Minuto " + getMinuto() + " - Falta (" + tipoFalta + ") cometida por " + getJugadorInvolucrado().getApellido();
    }
}