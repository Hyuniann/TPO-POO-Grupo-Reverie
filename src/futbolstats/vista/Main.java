package futbolstats.vista;

import futbolstats.controlador.ControladorSistemaFutbol;
import futbolstats.controlador.GestorNotificaciones;
import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Entidades_Secundarias.GeneradorReportes;
import futbolstats.modelo.Entidades_Secundarias.Notificacion;
import futbolstats.modelo.Entidades_Secundarias.Suscriptor;
import futbolstats.modelo.Enumeraciones.ColorTarjeta;
import futbolstats.modelo.Enumeraciones.EstadoPartido;
import futbolstats.modelo.Enumeraciones.TipoGol;
import futbolstats.modelo.Eventos.Evento;
import futbolstats.modelo.Eventos.Gol;
import futbolstats.modelo.Eventos.Tarjeta;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.Interfaces_con_Implementaciones.SMS;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        GestorNotificaciones gestorNotif = new GestorNotificaciones();
        ControladorSistemaFutbol controlador = new ControladorSistemaFutbol();
        GeneradorReportes reportes = new GeneradorReportes();

        Torneo torneo = new Torneo("Liga 2026", LocalDate.now(), LocalDate.now().plusMonths(4));
        Club local = new Club("UADE FC", LocalDate.of(2000, 1, 1), "Lima 750");
        Club visitante = new Club("Ingenieria FC", LocalDate.of(2005, 5, 10), "Independencia 120");

        Suscriptor suscriptor = new Suscriptor("Eric");
        suscriptor.activarCanal(new SMS());
        suscriptor.agregarClub(local);
        gestorNotif.agregarSuscriptor(suscriptor);

        // Precarga de multiples jugadores para dinamismo
        Jugador j1 = new Jugador("111", "Lionel", "Messi", LocalDate.of(1987, 6, 24), "DELANTERO", 10, 0.0);
        Jugador j2 = new Jugador("222", "Rodrigo", "De Paul", LocalDate.of(1994, 5, 24), "MEDIO", 7, 0.0);
        Jugador j3 = new Jugador("333", "Cristiano", "Ronaldo", LocalDate.of(1985, 2, 5), "DELANTERO", 7, 0.0);
        Jugador j4 = new Jugador("444", "Luka", "Modric", LocalDate.of(1985, 9, 9), "MEDIO", 10, 0.0);

        try {
            local.agregarJugador(j1);
            local.agregarJugador(j2);
            visitante.agregarJugador(j3);
            visitante.agregarJugador(j4);
            torneo.agregarClub(local);
            torneo.agregarClub(visitante);
        } catch (ReglaNegocioException e) {
            System.out.println("Error de datos: " + e.getMessage());
        }

        Partido partido = new Partido(LocalDate.now(), LocalTime.of(21, 0), local, visitante);
        controlador.programarPartido(torneo, partido);

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 7) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Iniciar partido");
            System.out.println("2. Registrar Evento (Gol o Tarjeta)");
            System.out.println("3. Finalizar partido");
            System.out.println("4. Ver Estadisticas y Tablas");
            System.out.println("5. Guardar resumen en disco (TXT)");
            System.out.println("6. Leer archivo desde disco (TXT)");
            System.out.println("7. Salir");
            System.out.print("Elija una opcion: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
            } else {
                System.out.println("Opcion invalida.");
                scanner.next();
                continue;
            }

            switch (opcion) {
                case 1:
                    partido.asignarEstado(EstadoPartido.EN_JUEGO);
                    System.out.println("El partido ha comenzado.");
                    break;

                case 2:
                    try {
                        System.out.println("Jugador: 1.Messi (L) | 2.De Paul (L) | 3.Ronaldo (V) | 4.Modric (V)");
                        int opcJugador = scanner.nextInt();
                        Jugador jugSelec = (opcJugador == 1) ? j1 : (opcJugador == 2) ? j2 : (opcJugador == 3) ? j3 : j4;

                        System.out.println("Evento: 1.Gol | 2.Tarjeta Amarilla | 3.Tarjeta Roja");
                        int opcEvento = scanner.nextInt();

                        System.out.print("Minuto del evento: ");
                        int minuto = scanner.nextInt();

                        Evento evento = null;
                        if (opcEvento == 1) {
                            evento = new Gol(minuto, "Gol de " + jugSelec.getApellido(), jugSelec, TipoGol.A_FAVOR);
                        } else if (opcEvento == 2) {
                            evento = new Tarjeta(minuto, "Infraccion", jugSelec, "Falta leve", ColorTarjeta.AMARILLA);
                        } else {
                            evento = new Tarjeta(minuto, "Expulsion", jugSelec, "Juego brusco", ColorTarjeta.ROJA);
                        }

                        controlador.registrarEvento(partido, evento);
                        System.out.println("Evento registrado con exito.");

                        Notificacion alerta = new Notificacion(evento.getMinuto(), evento.getDescripcion());
                        gestorNotif.despacharNotificacion(alerta, partido.getClubLocal(), partido.getClubVisitante());

                    } catch (ReglaNegocioException e) {
                        System.out.println("Regla de negocio: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error de tipeo. Ingrese solo numeros enteros.");
                        scanner.nextLine();
                    }
                    break;

                case 3:
                    partido.finalizar();
                    System.out.println("Partido finalizado. Los puntos ya impactan en la tabla.");
                    break;

                case 4:
                    System.out.println("\n--- ESTADO DEL PARTIDO ---");
                    System.out.println(local.getNombre() + " " + partido.getGolesLocal() + " - " + partido.getGolesVisitante() + " " + visitante.getNombre());
                    System.out.println("Estado: " + partido.getEstado());

                    reportes.obtenerTablaPosiciones(torneo);
                    reportes.obtenerGoleadores(torneo);
                    break;

                case 5:
                    controlador.guardarResumenEnArchivo(partido, "torneo.txt");
                    System.out.println("Datos guardados en torneo.txt.");
                    break;

                case 6:
                    String contenido = controlador.leerResumenPartido("torneo.txt");
                    System.out.println("\n--- CONTENIDO DEL ARCHIVO ---");
                    System.out.println(contenido);
                    break;

                case 7:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opcion inexistente.");
            }
        }
        scanner.close();
    }
}