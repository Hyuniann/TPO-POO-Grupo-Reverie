package futbolstats.vista;

import futbolstats.controlador.ControladorSistemaFutbol;
import futbolstats.controlador.GestorNotificaciones;
import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Estadio;
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
        Club c1 = new Club("UADE FC", LocalDate.of(2000, 1, 1), "Lima 750");
        Club c2 = new Club("Ingenieria FC", LocalDate.of(2005, 5, 10), "Independencia 120");
        Club c3 = new Club("Buongiorno Tutti", LocalDate.of(1998, 7, 3), "Italia 67");
        Club c4 = new Club("Procyknator", LocalDate.of(2020, 4, 23), "RealTime");

        Estadio e1 = new Estadio("TTT United", 77700,"Tung Valley", "777", "Sahur City", "BSAS", "Soleado");
        Estadio e2 = new Estadio("Eric Song", 45000, "Micasa", "378", "Halliday", "BSAS", "Nublado");

        Suscriptor suscriptor = new Suscriptor("Eric");
        suscriptor.activarCanal(new SMS());
        suscriptor.agregarClub(c1);
        gestorNotif.agregarSuscriptor(suscriptor);

        Jugador j1 = new Jugador("111", "Lionel", "Messi", LocalDate.of(1987, 6, 24), "DELANTERO", 10, 15.0);
        Jugador j2 = new Jugador("222", "Rodrigo", "De Paul", LocalDate.of(1994, 5, 24), "MEDIO", 7, 20.0);
        Jugador j3 = new Jugador("333", "Cristiano", "Ronaldo", LocalDate.of(1985, 2, 5), "DELANTERO", 7, 2.0);
        Jugador j4 = new Jugador("444", "Luka", "Modric", LocalDate.of(1985, 9, 9), "MEDIO", 10, 10.0);
        Jugador j5 = new Jugador("555", "Mateo", "González", LocalDate.of(1998, 3, 15), "DEFENSOR", 4, 12.5);
        Jugador j6 = new Jugador("667", "Agustin", "Fernández", LocalDate.of(2001, 7, 22), "DELANTERO", 9, 25.0);
        Jugador j7 = new Jugador("777", "Thiago", "Romero", LocalDate.of(2003, 11, 5), "ARQUERO", 68, 8.3);
        Jugador j8 = new Jugador("888", "Santiago", "Méndez", LocalDate.of(1995, 1, 30), "AQUERO", 1, 5.0);
        Jugador j9 = new Jugador("999", "Franco", "Díaz", LocalDate.of(2000, 9, 12), "DELANTERO", 3, 15.7);
        Jugador j10 = new Jugador("4343", "Nicolás", "Paredes", LocalDate.of(2004, 5, 18), "MEDIO", 11, 3.2);
        Jugador j11 = new Jugador("8945", "Joaquín", "Sosa", LocalDate.of(1997, 12, 8), "MEDIO", 10, 18.0);
        Jugador j12 = new Jugador("4321", "Emiliano", "Vidal", LocalDate.of(2002, 4, 27), "DEFENSOR", 5, 7.5);

        try {
            c1.agregarJugador(j1);
            c1.agregarJugador(j2);
            c2.agregarJugador(j3);
            c2.agregarJugador(j4);
            c3.agregarJugador(j5);
            c3.agregarJugador(j6);
            c4.agregarJugador(j7);
            c4.agregarJugador(j8);
            torneo.agregarClub(c1);
            torneo.agregarClub(c2);
            torneo.agregarClub(c3);
            torneo.agregarClub(c4);

        } catch (ReglaNegocioException e) {
            System.out.println("Error de datos: " + e.getMessage());
        }

        Partido partido = new Partido(LocalDate.now(), LocalTime.of(21, 0), c1, c2);
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
                    System.out.println(c1.getNombre() + " " + partido.getGolesLocal() + " - " + partido.getGolesVisitante() + " " + c2.getNombre());
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