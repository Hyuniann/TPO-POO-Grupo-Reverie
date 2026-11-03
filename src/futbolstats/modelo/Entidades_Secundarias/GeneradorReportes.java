package futbolstats.modelo.Entidades_Secundarias;
import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.JerarquiaPersona.Jugador;
import futbolstats.modelo.Interfaces_con_Implementaciones.EstrategiaProyeccion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneradorReportes {
    private EstrategiaProyeccion estrategia;

    public void setEstrategia(EstrategiaProyeccion estrategia) {
        this.estrategia = estrategia;
    }

    public void obtenerGoleadores(Torneo torneo) {
        List<Jugador> todosJugadores = new ArrayList<>();
        for (Club club : torneo.getClubesInscriptos()) {
            todosJugadores.addAll(club.obtenerPlantel().obtenerJugadores());
        }

        todosJugadores.sort(new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.getGolesAcumulados(), j1.getGolesAcumulados());
            }
        });

        System.out.println("\n--- GOLEADORES DEL TORNEO ---");
        for (Jugador j : todosJugadores) {
            if(j.getGolesAcumulados() > 0) {
                System.out.println(j.getApellido() + " - Goles: " + j.getGolesAcumulados());
            }
        }
    }

    public void calcularProyeccion(Torneo torneo) {
        if (estrategia != null) {
            System.out.println("Proyección calculada: " + estrategia.calcular(torneo) + "%");
        }
    }
}