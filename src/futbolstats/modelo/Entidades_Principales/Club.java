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

    private double presupuesto;
    private double totalGastado;
    private double totalRecaudado;

    public Club(String nombre, LocalDate fechaFundacion, String sedeSocial) {
        this(nombre, fechaFundacion, sedeSocial, 50.0);
    }

    public Club(String nombre, LocalDate fechaFundacion, String sedeSocial, double presupuesto) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
        this.sedeSocial = sedeSocial;
        this.plantel = new Plantel();
        this.cuerpoTecnico = new ArrayList<>();
        this.presupuesto = presupuesto;
        this.totalGastado = 0.0;
        this.totalRecaudado = 0.0;
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

    public void descontarPresupuesto(double monto) {
        this.presupuesto -= monto;
        this.totalGastado += monto;
    }

    public void acreditarPresupuesto(double monto) {
        this.presupuesto += monto;
        this.totalRecaudado += monto;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public double getTotalGastado() {
        return totalGastado;
    }

    public double getTotalRecaudado() {
        return totalRecaudado;
    }
}
