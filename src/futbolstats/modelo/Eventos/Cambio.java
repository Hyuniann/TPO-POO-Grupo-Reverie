package futbolstats.modelo.Eventos;

import futbolstats.modelo.JerarquiaPersona.Jugador;

public class Cambio extends Evento {
    private Jugador jugadorEntra;

    public Cambio(int minuto, String descripcion, Jugador jugadorSale, Jugador jugadorEntra) {
        super(minuto, descripcion, jugadorSale);
        this.jugadorEntra = jugadorEntra;
    }

    public Jugador getJugadorEntra() {
        return jugadorEntra;
    }

    @Override
    public String getDescripcion() {
        return "Minuto " + getMinuto() + " - CAMBIO: Sale " + getJugadorInvolucrado().getApellido() + ", Entra " + jugadorEntra.getApellido();
    }
}