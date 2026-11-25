package futbolstats;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Enumeraciones.*;
import futbolstats.modelo.Eventos.Falta;
import futbolstats.modelo.Eventos.Gol;
import futbolstats.modelo.Eventos.Remate;
import futbolstats.modelo.Eventos.Tarjeta;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;
import futbolstats.modelo.Scouting.CalculadorValorMercado;
import futbolstats.modelo.Scouting.EvaluadorJugador;
import futbolstats.modelo.Scouting.InformeJugador;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScoutingTest {
    public static void main(String[] args) throws ReglaNegocioException {
        Club local = new Club("Club A", LocalDate.of(2000, 1, 1), "Sede A");
        Club visitante = new Club("Club B", LocalDate.of(2001, 1, 1), "Sede B");

        Jugador jugador = new Jugador("111", "Jugador", "Prueba",
                LocalDate.of(2000, 1, 1), "DELANTERO", 9, 20.0);
        local.agregarJugador(jugador);

        Torneo torneo = new Torneo("Torneo de prueba",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(30));
        torneo.agregarClub(local);
        torneo.agregarClub(visitante);

        Partido partido = new Partido(LocalDate.now(), LocalTime.of(15, 0), local, visitante);
        partido.asignarEstado(EstadoPartido.EN_JUEGO);
        partido.registrarEvento(new Gol(10, "Gol", jugador, TipoGol.A_FAVOR));
        partido.registrarEvento(new Remate(20, "Remate", jugador, ZonaRemate.AREA_GRANDE));
        partido.registrarEvento(new Remate(35, "Remate", jugador, ZonaRemate.FUERA_DEL_AREA));
        partido.registrarEvento(new Tarjeta(50, "Amarilla", jugador,
                "Falta tactica", ColorTarjeta.AMARILLA));
        partido.registrarEvento(new Falta(60, "Falta", jugador, TipoFalta.TACTICA));
        partido.finalizar();
        torneo.programarPartido(partido);

        EvaluadorJugador evaluador = new EvaluadorJugador();
        InformeJugador informe = evaluador.evaluar(jugador, torneo);

        CalculadorValorMercado calculador = new CalculadorValorMercado();
        double nuevoValor = calculador.actualizarValorMercado(informe);

        comprobar(Math.abs(informe.getPuntaje() - 6.5) < 0.0001,
                "El puntaje calculado no coincide con el esperado.");
        comprobar(Math.abs(nuevoValor - 21.3) < 0.0001,
                "El nuevo valor de mercado no coincide con el esperado.");

        InformeJugador segundoInforme = evaluador.evaluar(jugador, torneo);
        calculador.actualizarValorMercado(segundoInforme);
        comprobar(Math.abs(jugador.getValorMercadoActual() - 21.3) < 0.0001,
                "El valor se calculo acumulativamente y no desde el valor inicial.");

        System.out.println(informe.generarResumen());
        System.out.println("\nPRUEBA SCOUTING: CORRECTA");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
