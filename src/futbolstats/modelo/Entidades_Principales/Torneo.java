package futbolstats.modelo.Entidades_Principales;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.Enumeraciones.EstadoTorneo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Torneo {
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoTorneo estado;
    private List<Club> clubesInscriptos;
    private List<Partido> partidos;

    public Torneo(String nombre, LocalDate fechaInicio, LocalDate fechaFin){
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = EstadoTorneo.PLANIFICADO;
        this.clubesInscriptos = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public List<Club> getClubesInscriptos() { return clubesInscriptos; }
    public List<Partido> getPartidos() { return partidos; }

    public void agregarClub(Club club) throws ReglaNegocioException {
        if(estado != EstadoTorneo.PLANIFICADO){
            throw new ReglaNegocioException("No se admiten clubes. el torneo ya no esta planificado");
        }
        clubesInscriptos.add(club);
    }

    public void iniciarTorneo() throws ReglaNegocioException {
        if (clubesInscriptos.size() < 4) {
            throw new ReglaNegocioException("Mínimo 4 clubes para iniciar el torneo.");
        }
        this.estado = EstadoTorneo.EN_CURSO;
    }

    public void programarPartido(Partido partido) {
        partidos.add(partido);
    }
}
