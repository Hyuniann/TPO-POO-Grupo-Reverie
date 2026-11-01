package futbolstats.modelo;

public abstract class Evento {
    private int minuto;
    private String descripcion;
    private Jugador jugadorInvolucrado;

    public Evento(int minuto, String descripcion, Jugador jugadorInvolucrado) {
        this.minuto = minuto;
        this.descripcion = descripcion;
        this.jugadorInvolucrado = jugadorInvolucrado;
    }

    public int getMinuto() {
        return minuto;
    }

    public Jugador getJugadorInvolucrado() {
        return jugadorInvolucrado;
    }

    public abstract String getDescripcion();
}
