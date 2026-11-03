package futbolstats.modelo.Entidades_Principales;

import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Entrenador;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Club {
    private String nombre;
    private LocalDate fechaFundacion;
    private String sedeSocial;

    private Plantel plantel;
    private List<Entrenador> cuerpoTecnico;

    public Club(String nombre, LocalDate fechaFundacion, String sedeSocial) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.sedeSocial = sedeSocial;
        this.plantel = new Plantel();
        this.cuerpoTecnico = new ArrayList<>();
    }

    public  String getNombre() {
        return nombre;
    }

    public void agregarJugador(Jugador jugador) throws ReglaNegocioException {
        this.plantel.agregarJugador(jugador);
    }

    public void agregarEntrenador(Entrenador entrenador) {
        this.cuerpoTecnico.add(entrenador);
    }

    public Plantel obtenerPlantel() {
        return this.plantel;
    }

    public List<Entrenador> obtenerCuerpoTecnico() {
        return this.cuerpoTecnico;
    }
}
