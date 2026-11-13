package futbolstats;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;

public class PlantelTest {
    public static void main(String[] args) {
        Club club = new Club("Test FC", LocalDate.now(), "Sede Test");

        try {
            for (int i = 1; i <= 30; i++) {
                Jugador j = jugTest(i);
                club.agregarJugador(j);
            }

            Jugador jugadorExtra = new Jugador("99999999", "Jugador", "Extra", LocalDate.of(2000, 1, 1), "DELANTERO", 99, 1.0);
            club.agregarJugador(jugadorExtra);

            System.out.println("FALLO Mas de 30 jugadores");
        } catch (ReglaNegocioException e) {
            System.out.println("EXITO): " + e.getMessage());
        }

        System.out.println("FIN");
    }

    private static Jugador jugTest(int id) {
        return new Jugador(String.valueOf(10000000 + id), "Nombre" + id, "Apellido" + id,
                LocalDate.of(2000, 1, 1), "MEDIO", id, 1.0);
    }
}