package futbolstats;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Enumeraciones.TipoGol;
import futbolstats.modelo.Eventos.Gol;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;
import java.time.LocalTime;

public class PartidoTest {
    public static void main(String[] args) {
        Club local = new Club("UADE FC", LocalDate.of(2000, 1, 1), "Lima 750");
        Club visitante = new Club("Ingenieria FC", LocalDate.of(2005, 5, 10), "Independencia 120");
        Partido partido = new Partido(LocalDate.now(), LocalTime.of(21, 0), local, visitante);

        Jugador jugadorOtroClub = new Jugador("87654321", "Eric", "Ajeno", LocalDate.of(1990, 1, 1), "DELANTERO", 9, 0.0);
        Gol gol = new Gol(15, "Gol ajeno", jugadorOtroClub, TipoGol.A_FAVOR);

        try {
            partido.asignarEstado(futbolstats.modelo.Enumeraciones.EstadoPartido.EN_JUEGO);
            partido.registrarEvento(gol);
            System.out.println("FALLO Gol ajeno");
        } catch (ReglaNegocioException e) {
            System.out.println("EXITO " + e.getMessage());
        }

        System.out.println("FIN");
    }
}