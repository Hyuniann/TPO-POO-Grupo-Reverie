package futbolstats;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Estadio;
import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Enumeraciones.ColorTarjeta;
import futbolstats.modelo.Enumeraciones.EstadoPartido;
import futbolstats.modelo.Enumeraciones.TipoGol;
import futbolstats.modelo.Enumeraciones.ZonaRemate;
import futbolstats.modelo.Eventos.Falta;
import futbolstats.modelo.Eventos.Gol;
import futbolstats.modelo.Eventos.Remate;
import futbolstats.modelo.Eventos.Tarjeta;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;
import futbolstats.modelo.MejoraPartido.ProgramadorPartidos;
import futbolstats.modelo.Scouting.ComparadorJugadores;
import futbolstats.modelo.Scouting.EvaluadorJugador;
import futbolstats.modelo.Scouting.InformeJugador;

import java.time.LocalDate;
import java.time.LocalTime;

public class MejorasFutbolStatsTest {
    public static void main(String[] args) throws Exception {
        Torneo torneo = new Torneo("Torneo de prueba", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        Club clubA = new Club("Club A", LocalDate.of(2000, 1, 1), "Sede A");
        Club clubB = new Club("Club B", LocalDate.of(2000, 1, 1), "Sede B");
        Club clubC = new Club("Club C", LocalDate.of(2000, 1, 1), "Sede C");
        Club clubD = new Club("Club D", LocalDate.of(2000, 1, 1), "Sede D");
        Estadio estadioUno = new Estadio("Estadio Uno", 20000, "Calle", "1", "Ciudad", "Provincia", "Normal");
        Estadio estadioDos = new Estadio("Estadio Dos", 15000, "Calle", "2", "Ciudad", "Provincia", "Normal");

        Jugador jugadorA = new Jugador("101", "Ana", "Delantera", LocalDate.of(2001, 1, 1), "DELANTERA", 9, 10);
        Jugador jugadorB = new Jugador("102", "Bea", "Defensora", LocalDate.of(2001, 1, 1), "DEFENSORA", 4, 8);
        clubA.agregarJugador(jugadorA);
        clubB.agregarJugador(jugadorB);
        torneo.agregarClub(clubA);
        torneo.agregarClub(clubB);
        torneo.agregarClub(clubC);
        torneo.agregarClub(clubD);
        torneo.iniciarTorneo();

        ProgramadorPartidos programador = new ProgramadorPartidos();
        Partido primerPartido = new Partido(LocalDate.of(2026, 6, 10), LocalTime.of(15, 0), clubA, clubB);
        primerPartido.asignarEstadio(estadioUno);
        programador.programar(torneo, primerPartido);

        Partido conflictoClub = new Partido(LocalDate.of(2026, 6, 10), LocalTime.of(16, 0), clubA, clubC);
        conflictoClub.asignarEstadio(estadioDos);
        boolean rechazoClub = false;
        try {
            programador.programar(torneo, conflictoClub);
        } catch (ReglaNegocioException e) {
            rechazoClub = e.getMessage().contains("club") || e.getMessage().contains("Club");
        }
        comprobar(rechazoClub, "Debe rechazar dos partidos superpuestos del mismo club.");

        Partido conflictoEstadio = new Partido(LocalDate.of(2026, 6, 10), LocalTime.of(16, 0), clubC, clubD);
        conflictoEstadio.asignarEstadio(estadioUno);
        boolean rechazoEstadio = false;
        try {
            programador.programar(torneo, conflictoEstadio);
        } catch (ReglaNegocioException e) {
            rechazoEstadio = e.getMessage().contains("estadio");
        }
        comprobar(rechazoEstadio, "Debe rechazar dos partidos superpuestos en el mismo estadio.");

        Partido partidoSinConflicto = new Partido(LocalDate.of(2026, 6, 10), LocalTime.of(16, 0), clubC, clubD);
        partidoSinConflicto.asignarEstadio(estadioDos);
        programador.programar(torneo, partidoSinConflicto);
        comprobar(torneo.getPartidos().contains(partidoSinConflicto),
                "Debe permitir un encuentro simultaneo cuando usa clubes y estadio disponibles.");

        primerPartido.asignarEstado(EstadoPartido.EN_JUEGO);
        primerPartido.registrarEvento(new Gol(20, "Gol", jugadorA, TipoGol.A_FAVOR));
        primerPartido.registrarEvento(new Remate(32, "Remate", jugadorA, ZonaRemate.AREA_GRANDE));
        primerPartido.registrarEvento(new Tarjeta(50, "Amarilla", jugadorA, "Falta tactica", ColorTarjeta.AMARILLA));
        primerPartido.registrarEvento(new Falta(60, "Falta", jugadorA, futbolstats.modelo.Enumeraciones.TipoFalta.TACTICA));
        primerPartido.finalizar();

        EvaluadorJugador evaluador = new EvaluadorJugador();
        InformeJugador informe = evaluador.evaluar(jugadorA, torneo);
        comprobar(informe.getEstadisticas().getGoles() == 1, "Debe contar un gol.");
        comprobar(informe.getEstadisticas().getRemates() == 1, "Debe contar un remate.");
        comprobar(informe.getEstadisticas().getTarjetasAmarillas() == 1, "Debe contar una amarilla.");
        comprobar(informe.getEstadisticas().getFaltas() == 1, "Debe contar una falta.");
        comprobar(Math.abs(informe.getPuntaje() - 5.0) < 0.001, "El puntaje esperado es 5.0.");

        InformeJugador informeB = evaluador.evaluar(jugadorB, torneo);
        String comparacion = new ComparadorJugadores().comparar(informe, informeB);
        comprobar(comparacion.contains("Mayor puntaje"), "Debe comparar los puntajes de ambos jugadores.");

        System.out.println("OK: validaciones de programación y scouting verificadas.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
