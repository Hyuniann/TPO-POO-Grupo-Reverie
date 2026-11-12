package futbolstats.modelo.Entidades_Principales;
import futbolstats.modelo.Eventos.Evento;
import futbolstats.modelo.Eventos.Gol;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.Enumeraciones.EstadoPartido;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Partido {
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoPartido estado;
    private Club clubLocal;
    private Club clubVisitante;
    private Estadio estadio;
    private TernaArbitral terna;
    private int golesLocal;
    private int golesVisitante;
    private List<Evento> eventos;

    public Partido(LocalDate fecha, LocalTime hora, Club clubLocal, Club clubVisitante) {
        this.fecha = fecha;
        this.hora = hora;
        this.clubLocal = clubLocal;
        this.clubVisitante = clubVisitante;
        this.estado = EstadoPartido.PROGRAMADO;
        this.golesLocal = 0;
        this.golesVisitante = 0;
        this.eventos = new ArrayList<>();
    }

    public void asignarEstadio(Estadio estadio) { this.estadio = estadio; }
    public void asignarTerna(TernaArbitral terna) { this.terna = terna; }
    public void asignarEstado(EstadoPartido estado) { this.estado = estado; }

    public Club getClubLocal() { return clubLocal; }
    public Club getClubVisitante() { return clubVisitante; }

    public void registrarEvento(Evento evento) throws ReglaNegocioException {
        if (this.estado != EstadoPartido.EN_JUEGO) {
            throw new ReglaNegocioException("Solo se pueden registrar eventos si el partido está en juego.");
        }


        Jugador jugador = evento.getJugadorInvolucrado();
        boolean esLocal = clubLocal.obtenerPlantel().obtenerJugadores().contains(jugador);
        boolean esVisitante = clubVisitante.obtenerPlantel().obtenerJugadores().contains(jugador);

        if (!esLocal && !esVisitante) {
            throw new ReglaNegocioException("El jugador no forma parte de la nómina de ninguno de los clubes del partido.");
        }

        eventos.add(evento);

        if (evento instanceof Gol) {
            if (esLocal) {
                golesLocal++;
            } else {
                golesVisitante++;
            }
            jugador.incrementarGoles();
        }
    }

    public void finalizar() { this.estado = EstadoPartido.FINALIZADO; }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public EstadoPartido getEstado() {
        return estado;
    }
}

