package futbolstats;

import futbolstats.modelo.*;
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
        System.out.println("Ejecutando pruebas unitarias...");

        Club local = new Club("UADE FC", LocalDate.of(2000, 1, 1), "Lima 750");
        Club visitante = new Club("Ingenieria FC", LocalDate.of(2005, 5, 10), "Independencia 120");
        Partido partido = new Partido(LocalDate.now(), LocalTime.of(21, 00), local, visitante);

        Jugador jugador = new Jugador("12345678", "Lionel", "Messi", LocalDate.of(1987, 6, 24), "DELANTERO", 10, 0.0);
        Gol gol = new Gol(15, "Gol de Messi", jugador, TipoGol.A_FAVOR);

        try {
            partido.registrarEvento(gol);

            System.out.println("[FALLO] La prueba de negocio permitio registrar eventos con el partido cerrado.");

        } catch (ReglaNegocioException e) {
            System.out.println("[EXITO] La regla de negocio bloqueo el evento correctamente: " + e.getMessage());
        }

        System.out.println("Pruebas finalizadas.");
    }
}
