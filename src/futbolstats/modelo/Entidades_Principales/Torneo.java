package futbolstats.modelo.Entidades_Principales;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.Enumeraciones.EstadoTorneo;
import futbolstats.modelo.JerarquiaPersona.Jugador;
import futbolstats.modelo.JerarquiaTransferencia.Transferencia;

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

    private List<Jugador> jugadoresLibres;
    private List<Transferencia> historialTransferencias;
    private List<Jugador> jugadoresIncorporadosEnVentana;

    public Torneo(String nombre, LocalDate fechaInicio, LocalDate fechaFin){
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = EstadoTorneo.PLANIFICADO;
        this.clubesInscriptos = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.jugadoresLibres = new ArrayList<>();
        this.historialTransferencias = new ArrayList<>();
        this.jugadoresIncorporadosEnVentana = new ArrayList<>();
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

    public EstadoTorneo getEstado() {
        return estado;
    }

    public List<Jugador> getJugadoresLibres() {
        return jugadoresLibres;
    }

    public List<Transferencia> getHistorialTransferencias() {
        return historialTransferencias;
    }

    public void agregarJugadorLibre(Jugador jugador) {
        if (jugador != null && !jugadoresLibres.contains(jugador)) {
            jugadoresLibres.add(jugador);
        }
    }

    public void quitarJugadorLibre(Jugador jugador) {
        jugadoresLibres.remove(jugador);
    }

    public boolean estaLibre(Jugador jugador) {
        return jugadoresLibres.contains(jugador);
    }

    public void registrarTransferencia(Transferencia transferencia) throws ReglaNegocioException {
        if (transferencia == null) {
            throw new ReglaNegocioException("La transferencia no puede ser nula.");
        }
        if (this.estado != EstadoTorneo.PLANIFICADO) {
            throw new ReglaNegocioException("La ventana de pases esta cerrada (el torneo ya esta en curso o finalizado).");
        }
        if (transferencia.getClubDestino() != null && jugadoresIncorporadosEnVentana.contains(transferencia.getJugador())) {
            throw new ReglaNegocioException("El jugador " + transferencia.getJugador().getApellido()
                    + " ya fue incorporado por un club en esta ventana de pases.");
        }
        //polimorfismo, cada subclase valida y ejecuta sus propias reglas
        transferencia.validar(this);
        transferencia.ejecutar(this);

        if (transferencia.getClubDestino() != null) {
            this.jugadoresIncorporadosEnVentana.add(transferencia.getJugador());
        }
        this.historialTransferencias.add(transferencia);
        transferencia.getJugador().agregarTransferencia(transferencia);
    }
}
