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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        GestorNotificaciones gestorNotif = new GestorNotificaciones();
        ControladorSistemaFutbol controlador = new ControladorSistemaFutbol();
        GeneradorReportes reportes = new GeneradorReportes();

        Torneo torneo = new Torneo("Liga Universitaria 2026", LocalDate.now(), LocalDate.now().plusMonths(4));
        Club c1 = new Club("UADE FC", LocalDate.of(2000, 1, 1), "Lima 750");
        Club c2 = new Club("Ingenieria FC", LocalDate.of(2005, 5, 10), "Independencia 120");
        Club c3 = new Club("Buongiorno Tutti", LocalDate.of(1998, 7, 3), "Italia 67");
        Club c4 = new Club("Procyknator", LocalDate.of(2020, 4, 23), "RealTime");

        Estadio e1 = new Estadio("TTT United", 77700, "Tung Valley", "777", "Sahur City", "BSAS", "Soleado");
        Estadio e2 = new Estadio("Eric Song", 45000, "Micasa", "378", "Halliday", "BSAS", "Nublado");

        Suscriptor suscriptor = new Suscriptor("Eric");
        suscriptor.activarCanal(new SMS());
        suscriptor.agregarClub(c1);
        gestorNotif.agregarSuscriptor(suscriptor);

        Jugador j1 = new Jugador("111", "Lionel", "Messi", LocalDate.of(1987, 6, 24), "DELANTERO", 10, 15.0);
        Jugador j2 = new Jugador("222", "Rodrigo", "De Paul", LocalDate.of(1994, 5, 24), "MEDIO", 7, 20.0);
        Jugador j3 = new Jugador("333", "Cristiano", "Ronaldo", LocalDate.of(1985, 2, 5), "DELANTERO", 7, 2.0);
        Jugador j4 = new Jugador("444", "Luka", "Modric", LocalDate.of(1985, 9, 9), "MEDIO", 10, 10.0);
        Jugador j5 = new Jugador("555", "Mateo", "Gonzalez", LocalDate.of(1998, 3, 15), "DEFENSOR", 4, 12.5);
        Jugador j6 = new Jugador("667", "Agustin", "Fernandez", LocalDate.of(2001, 7, 22), "DELANTERO", 9, 25.0);
        Jugador j7 = new Jugador("777", "Thiago", "Romero", LocalDate.of(2003, 11, 5), "ARQUERO", 68, 8.3);
        Jugador j8 = new Jugador("888", "Santiago", "Mendez", LocalDate.of(1995, 1, 30), "ARQUERO", 1, 5.0);

        try {
            c1.agregarJugador(j1); c1.agregarJugador(j2);
            c2.agregarJugador(j3); c2.agregarJugador(j4);
            c3.agregarJugador(j5); c3.agregarJugador(j6);
            c4.agregarJugador(j7); c4.agregarJugador(j8);

            torneo.agregarClub(c1); torneo.agregarClub(c2);
            torneo.agregarClub(c3); torneo.agregarClub(c4);
            torneo.iniciarTorneo();
        } catch (ReglaNegocioException e) {
            System.out.println("Error de carga inicial: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 8) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Comenzar Partido");
            System.out.println("2. Ver Tabla de Posiciones");
            System.out.println("3. Ver Estadisticas de Jugadores");
            System.out.println("4. Ver Reporte Completo del Torneo (Lectura TXT)");
            System.out.println("5. Programar un encuentro");
            System.out.println("6. Consultar calendario");
            System.out.println("7. Iniciar un partido programado");
            System.out.println("8. Salir");
            System.out.print("Elija una opcion: ");

            try {
                opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        System.out.println("\n--- NUEVO PARTIDO ---");
                        System.out.println("Clubes: 1.UADE FC | 2.Ingenieria FC | 3.Buongiorno Tutti | 4.Procyknator");
                        System.out.print("Numero Club Local: ");
                        int numLocal = scanner.nextInt();
                        System.out.print("Numero Club Visitante: ");
                        int numVisita = scanner.nextInt();

                        if (numLocal >= 1 && numLocal <= 4 && numVisita >= 1 && numVisita <= 4 && numLocal != numVisita) {
                            Club local = torneo.getClubesInscriptos().get(numLocal - 1);
                            Club visita = torneo.getClubesInscriptos().get(numVisita - 1);

                            Partido partidoActivo = new Partido(LocalDate.now(), LocalTime.now(), local, visita);
                            partidoActivo.asignarEstadio((numLocal % 2 == 1) ? e1 : e2);
                            partidoActivo.asignarEstado(EstadoPartido.EN_JUEGO);

                            System.out.println("El partido " + local.getNombre() + " vs " + visita.getNombre() + " ha comenzado.");

                            boolean jugando = true;
                            while (jugando) {
                                System.out.println("\n[PARTIDO EN JUEGO] Opciones: 1.Registrar Gol | 2.Registrar Tarjeta | 3.Terminar Partido");
                                int accion = scanner.nextInt();

                                if (accion == 3) {
                                    partidoActivo.finalizar();
                                    controlador.guardarResumenEnArchivo(torneo, "torneo.txt");
                                    System.out.println("Partido finalizado y guardado en disco automaticamente.");
                                    jugando = false;
                                } else if (accion == 1 || accion == 2) {
                                    List<Jugador> enCancha = new ArrayList<>();
                                    enCancha.addAll(partidoActivo.getClubLocal().obtenerPlantel().obtenerJugadores());
                                    enCancha.addAll(partidoActivo.getClubVisitante().obtenerPlantel().obtenerJugadores());

                                    for (int i = 0; i < enCancha.size(); i++) {
                                        System.out.println((i + 1) + ". " + enCancha.get(i).getApellido());
                                    }
                                    System.out.print("Numero de jugador involucrado: ");
                                    int numJug = scanner.nextInt();
                                    if (numJug < 1 || numJug > enCancha.size()) {
                                        System.out.println("Jugador invalido.");
                                        continue;
                                    }
                                    Jugador jugSelec = enCancha.get(numJug - 1);

                                    // VALIDACIÓN DEL MINUTO (0 a 120)
                                    int minuto = -1;
                                    while (minuto < 0 || minuto > 120) {
                                        System.out.print("Minuto del evento (0 a 120): ");
                                        if (scanner.hasNextInt()) {
                                            minuto = scanner.nextInt();
                                            if (minuto < 0 || minuto > 120) {
                                                System.out.println("Error: El minuto debe estar entre 0 y 120.");
                                            }
                                        } else {
                                            System.out.println("Debe ingresar un numero entero.");
                                            scanner.next();
                                        }
                                    }

                                    // CREACIÓN DEL EVENTO
                                    Evento evento = null;
                                    if (accion == 1) {
                                        evento = new Gol(minuto, "Gol", jugSelec, TipoGol.A_FAVOR);
                                    } else if (accion == 2) {
                                        int tipoTarjeta = 0;
                                        while (tipoTarjeta != 1 && tipoTarjeta != 2) {
                                            System.out.print("Color de Tarjeta (1.Amarilla | 2.Roja): ");
                                            if (scanner.hasNextInt()) {
                                                tipoTarjeta = scanner.nextInt();
                                            } else {
                                                System.out.println("Opcion invalida.");
                                                scanner.next();
                                            }
                                        }
                                        ColorTarjeta color = (tipoTarjeta == 1) ? ColorTarjeta.AMARILLA : ColorTarjeta.ROJA;
                                        String motivo = (tipoTarjeta == 1) ? "Falta tactica" : "Juego brusco";
                                        evento = new Tarjeta(minuto, "Infraccion", jugSelec, motivo, color);
                                    }

                                    try {
                                        controlador.registrarEvento(partidoActivo, evento);
                                        System.out.println("Evento registrado.");
                                        Notificacion alerta = new Notificacion(evento.getMinuto(), evento.getDescripcion());
                                        gestorNotif.despacharNotificacion(alerta, partidoActivo.getClubLocal(), partidoActivo.getClubVisitante());
                                    } catch (ReglaNegocioException e) {
                                        System.out.println("Error: " + e.getMessage());
                                    }
                                }
                            }
                        } else {
                            System.out.println("Seleccion de clubes invalida.");
                        }
                        break;

                    case 2:
                        reportes.obtenerTablaPosiciones(torneo);
                        break;

                    case 3:
                        System.out.println("\n--- ESTADISTICAS DE JUGADORES ---");
                        for (Club c : torneo.getClubesInscriptos()) {
                            for (Jugador j : c.obtenerPlantel().obtenerJugadores()) {
                                j.obtenerEstadisticas();
                            }
                        }
                        break;

                    case 4:
                        String contenido = controlador.leerResumenPartido("torneo.txt");
                        System.out.println(contenido);
                        break;


                    case 5:
                        System.out.println("\n--- PROGRAMAR ENCUENTRO ---");
                        System.out.print("Numero Club Local: ");
                        int localSeleccionado = scanner.nextInt();
                        System.out.print("Numero Club Visitante: ");
                        int visitanteSeleccionado = scanner.nextInt();

                        if (localSeleccionado < 1 || localSeleccionado > torneo.getClubesInscriptos().size() ||
                                visitanteSeleccionado < 1 || visitanteSeleccionado > torneo.getClubesInscriptos().size() ||
                                localSeleccionado == visitanteSeleccionado) {
                            System.out.println("Seleccion de clubes invalida.");
                            break;
                        }

                        System.out.print("Fecha (AAAA-MM-DD): ");
                        String fechaTexto = scanner.next();
                        System.out.print("Hora (HH:MM): ");
                        String horaTexto = scanner.next();
                        System.out.println("Duracion predeterminada del encuentro: 120 minutos.");
                        System.out.println("Estadio: 1." + e1.getNombre() + " | 2." + e2.getNombre());
                        int estadioSeleccionado = scanner.nextInt();
                        if (estadioSeleccionado < 1 || estadioSeleccionado > 2) {
                            System.out.println("Seleccion de estadio invalida.");
                            break;
                        }

                        try {
                            LocalDate fechaPartido = LocalDate.parse(fechaTexto);
                            LocalTime horaPartido = LocalTime.parse(horaTexto);
                            Club localProgramado = torneo.getClubesInscriptos().get(localSeleccionado - 1);
                            Club visitanteProgramado = torneo.getClubesInscriptos().get(visitanteSeleccionado - 1);
                            Partido nuevoPartido = new Partido(fechaPartido, horaPartido,
                                    localProgramado, visitanteProgramado);
                            nuevoPartido.asignarEstadio(estadioSeleccionado == 1 ? e1 : e2);
                            controlador.programarPartido(torneo, nuevoPartido);
                            System.out.println("Partido programado correctamente.");
                        } catch (DateTimeParseException e) {
                            System.out.println("Formato invalido. Utilice AAAA-MM-DD para la fecha y HH:MM para la hora.");
                        } catch (ReglaNegocioException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 6:
                        System.out.println("\n--- CALENDARIO DEL TORNEO ---");
                        List<Partido> calendario = controlador.obtenerCalendario(torneo);
                        if (calendario.isEmpty()) {
                            System.out.println("Todavia no hay partidos programados.");
                        } else {
                            for (int i = 0; i < calendario.size(); i++) {
                                Partido p = calendario.get(i);
                                String estadio = p.getEstadio() == null ? "Sin estadio" : p.getEstadio().getNombre();
                                System.out.println((i + 1) + ". " + p.getFecha() + " " + p.getHora() + " | " +
                                        p.getClubLocal().getNombre() + " vs. " + p.getClubVisitante().getNombre() +
                                        " | " + estadio + " | Duracion: " + p.getDuracionMinutos() +
                                        " min | Estado: " + p.getEstado());
                            }
                        }
                        break;

                    case 9:
                        List<Partido> partidosPendientes = new ArrayList<>();
                        for (Partido p : controlador.obtenerCalendario(torneo)) {
                            if (p.getEstado() == EstadoPartido.PROGRAMADO) {
                                partidosPendientes.add(p);
                            }
                        }
                        if (partidosPendientes.isEmpty()) {
                            System.out.println("No hay partidos pendientes para iniciar.");
                            break;
                        }
                        System.out.println("\n--- PARTIDOS PENDIENTES ---");
                        for (int i = 0; i < partidosPendientes.size(); i++) {
                            Partido p = partidosPendientes.get(i);
                            System.out.println((i + 1) + ". " + p.getFecha() + " " + p.getHora() + " | " +
                                    p.getClubLocal().getNombre() + " vs. " + p.getClubVisitante().getNombre());
                        }
                        System.out.print("Seleccione el partido que desea iniciar: ");
                        int partidoElegido = scanner.nextInt();
                        if (partidoElegido < 1 || partidoElegido > partidosPendientes.size()) {
                            System.out.println("Seleccion de partido invalida.");
                            break;
                        }
                        break;

                    case 8:
                        System.out.println("Saliendo del sistema...");
                        break;

                    default:
                        System.out.println("Opcion inexistente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un numero valido.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}