package futbolstats.vista;

import futbolstats.controlador.ControladorSistemaFutbol;
import futbolstats.controlador.GestorNotificaciones;
import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Entidades_Secundarias.Notificacion;
import futbolstats.modelo.Entidades_Secundarias.Suscriptor;
import futbolstats.modelo.Enumeraciones.EstadoPartido;
import futbolstats.modelo.Enumeraciones.TipoGol;
import futbolstats.modelo.Eventos.Gol;
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

        Torneo torneo = new Torneo("Liga 2026", LocalDate.now(), LocalDate.now().plusMonths(4));
        Club local = new Club("UADE FC", LocalDate.of(2000, 1, 1), "Lima 750");
        Club visitante = new Club("Ingenieria FC", LocalDate.of(2005, 5, 10), "Independencia 120");

        Suscriptor suscriptor = new Suscriptor("Eric");
        suscriptor.activarCanal(new SMS());
        suscriptor.agregarClub(local);
        gestorNotif.agregarSuscriptor(suscriptor);

        Jugador jugador = new Jugador("12345678", "Lionel", "Messi", LocalDate.of(1987, 6, 24), "DELANTERO", 10, 0.0);

        try {
            local.agregarJugador(jugador);
            torneo.agregarClub(local);
            torneo.agregarClub(visitante);
        } catch (ReglaNegocioException e) {
            System.out.println("Error de datos: " + e.getMessage());
        }

        Partido partido = new Partido(LocalDate.now(), LocalTime.of(21, 0), local, visitante);
        controlador.programarPartido(torneo, partido);

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 5) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Iniciar partido");
            System.out.println("2. Registrar gol de " + jugador.getApellido());
            System.out.println("3. Guardar partido en txt");
            System.out.println("4. Leer archivo txt");
            System.out.println("5. Salir");
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
                    System.out.print("Ingrese el minuto del gol: ");
                    int minuto = scanner.nextInt();

                    Gol gol = new Gol(minuto, "Gol de " + jugador.getApellido(), jugador, TipoGol.A_FAVOR);

                    try {
                        controlador.registrarEvento(partido, gol);
                        System.out.println("Gol registrado con exito.");

                        Notificacion alerta = new Notificacion(gol.getMinuto(), gol.getDescripcion());
                        gestorNotif.despacharNotificacion(alerta, partido.getClubLocal(), partido.getClubVisitante());


                    } catch (ReglaNegocioException e) {
                        System.out.println("Regla de negocio no cumplida: " + e.getMessage());
                    }
                    break;

                case 3:
                    controlador.guardarResumenEnArchivo(partido, "torneo.txt");
                    System.out.println("Datos guardados en torneo.txt.");
                    break;

                case 4:
                    String contenido = controlador.leerResumenPartido("torneo.txt");
                    System.out.println("\n--- CONTENIDO DEL ARCHIVO ---");
                    System.out.println(contenido);
                    break;

                case 5:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opcion inexistente.");
            }
        }
        scanner.close();
    }
}