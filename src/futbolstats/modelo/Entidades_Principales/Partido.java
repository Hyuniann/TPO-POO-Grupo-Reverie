package futbolstats.modelo.Entidades_Principales;
import futbolstats.modelo.Eventos.Evento;
import futbolstats.modelo.Eventos.Gol;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.Enumeraciones.EstadoPartido;

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
        eventos.add(evento);
        if (evento instanceof Gol) {
            if (clubLocal.obtenerPlantel().obtenerJugadores().contains(evento.getJugadorInvolucrado())) {
                golesLocal++;
            } else {
                golesVisitante++;
            }
            evento.getJugadorInvolucrado().incrementarGoles();
        }
    }

    public void finalizar() { this.estado = EstadoPartido.FINALIZADO; }
}

