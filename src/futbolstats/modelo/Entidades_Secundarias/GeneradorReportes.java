package futbolstats.modelo.Entidades_Secundarias;
import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.JerarquiaPersona.Jugador;
import futbolstats.modelo.Interfaces_con_Implementaciones.EstrategiaProyeccion;
import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Enumeraciones.EstadoPartido;
import futbolstats.modelo.JerarquiaTransferencia.Transferencia;

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

    public void obtenerTablaPosiciones(Torneo torneo) {
        System.out.println("\n--- TABLA DE POSICIONES ---");
        for (Club club : torneo.getClubesInscriptos()) {
            int puntos = 0;

            for (Partido p : torneo.getPartidos()) {
                if (p.getEstado() == EstadoPartido.FINALIZADO) {
                    if (p.getClubLocal().equals(club)) {
                        if (p.getGolesLocal() > p.getGolesVisitante()) puntos += 3;
                        else if (p.getGolesLocal() == p.getGolesVisitante()) puntos += 1;
                    } else if (p.getClubVisitante().equals(club)) {
                        if (p.getGolesVisitante() > p.getGolesLocal()) puntos += 3;
                        else if (p.getGolesVisitante() == p.getGolesLocal()) puntos += 1;
                    }
                }
            }
            System.out.println(club.getNombre() + " - Puntos: " + puntos);
        }
    }

    public void obtenerVallaMenosVencida(Torneo torneo) {
        System.out.println("\n--- VALLA MENOS VENCIDA ---");
        Club menosVencido = null;
        double mejorPromedio = 999.0;

        for (Club club : torneo.getClubesInscriptos()) {
            int golesRecibidos = 0;
            int partidosJugados = 0;

            for (Partido p : torneo.getPartidos()) {
                if (p.getEstado() == EstadoPartido.FINALIZADO) {
                    if (p.getClubLocal().equals(club)) {
                        golesRecibidos += p.getGolesVisitante();
                        partidosJugados++;
                    } else if (p.getClubVisitante().equals(club)) {
                        golesRecibidos += p.getGolesLocal();
                        partidosJugados++;
                    }
                }
            }

            if (partidosJugados > 0) {
                double promedio = (double) golesRecibidos / partidosJugados;
                if (promedio < mejorPromedio) {
                    mejorPromedio = promedio;
                    menosVencido = club;
                }
            }
        }

        if (menosVencido != null) {
            System.out.println("Club: " + menosVencido.getNombre() + " (Promedio: " + mejorPromedio + " goles recibidos por partido)");
        } else {
            System.out.println("No hay datos suficientes de partidos finalizados.");
        }
    }

    public void generarReporteTrayectoria(Jugador jugador) {
        System.out.println("\n--- TRAYECTORIA DEL JUGADOR: " + jugador.getDatosPersonales() + " ---");
        System.out.println("Valor de mercado: $" + jugador.getValorMercado() + "M | Camiseta actual: #" + jugador.getNumeroCamiseta());
        System.out.println("Estadisticas acumuladas (conservadas): " + jugador.getGolesAcumulados()
                + " goles | " + jugador.getTarjetasAcumuladas() + " tarjetas.");

        List<Transferencia> historial = jugador.getHistorialTransferencias();
        if (historial.isEmpty()) {
            System.out.println("Sin movimientos registrados en el mercado de pases.");
        } else {
            for (Transferencia t : historial) {
                System.out.println("  -> " + t.generarDetalle());
            }
        }
    }

    public void generarBalanceEconomico(Torneo torneo) {
        System.out.println("\n--- BALANCE ECONOMICO DEL MERCADO DE PASES ---");
        List<Club> clubesOrdenados = new ArrayList<>(torneo.getClubesInscriptos());

        clubesOrdenados.sort(new Comparator<Club>() {
            @Override
            public int compare(Club c1, Club c2) {
                return Double.compare(c2.getPresupuesto(), c1.getPresupuesto());
            }
        });

        for (Club c : clubesOrdenados) {
            System.out.println(c.getNombre()
                    + " | Presupuesto disponible: $" + c.getPresupuesto() + "M"
                    + " | Gastado: $" + c.getTotalGastado() + "M"
                    + " | Recaudado: $" + c.getTotalRecaudado() + "M");
        }
    }
}
