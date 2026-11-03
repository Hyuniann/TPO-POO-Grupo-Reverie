package futbolstats.modelo.Entidades_Principales;

import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.util.ArrayList;
import java.util.List;

public class Plantel {
    private List<Jugador> jugadores;

    public Plantel() {
        this.jugadores = new ArrayList<>();
    }

    public void agregarJugador(Jugador jugador) throws ReglaNegocioException {
        if (jugadores.size() >= 30) {
            throw new ReglaNegocioException("El plantel no puede superar el limite de 30 jugadores.");
        }
        jugadores.add(jugador);
    }

    public List<Jugador> obtenerJugadores() {
        return this.jugadores;
    }
}
