package futbolstats;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Excepciones.ReglaNegocioException;

import java.time.LocalDate;

public class TorneoTest {
    public static void main(String[] args) {

        Torneo torneo = new Torneo("Liga Test", LocalDate.now(), LocalDate.now().plusMonths(2));
        Club c1 = new Club("Club A", LocalDate.of(2000, 1, 1), "Sede A");
        Club c2 = new Club("Club B", LocalDate.of(2001, 1, 1), "Sede B");

        try {
            torneo.agregarClub(c1);
            torneo.agregarClub(c2);
            torneo.iniciarTorneo();
            System.out.println("FALLO Se empieza el torneo con insuficiente clubes");
        } catch (ReglaNegocioException e) {
            System.out.println("EXITO: " + e.getMessage());
        }

        try {
            Club c3 = new Club("Club C", LocalDate.of(2002, 1, 1), "Sede C");
            Club c4 = new Club("Club D", LocalDate.of(2003, 1, 1), "Sede D");
            torneo.agregarClub(c3);
            torneo.agregarClub(c4);
            torneo.iniciarTorneo();

            Club c5 = tardioClubTest(torneo);
            System.out.println("FALLO Se agrego un club aunque empezo");
        } catch (ReglaNegocioException e) {
            System.out.println("EXITO: " + e.getMessage());
        }

        System.out.println("FIN");
    }

    private static Club tardioClubTest(Torneo torneo) throws ReglaNegocioException {
        Club cExtra = new Club("Club +", LocalDate.now(), "Sede C");
        torneo.agregarClub(cExtra);
        return cExtra;
    }
}