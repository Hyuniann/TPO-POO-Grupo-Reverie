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
import futbolstats.modelo.Enumeraciones.EstadoTorneo;
import futbolstats.modelo.Enumeraciones.TipoGol;
import futbolstats.modelo.Eventos.Evento;
import futbolstats.modelo.Eventos.Gol;
import futbolstats.modelo.Eventos.Tarjeta;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.Interfaces_con_Implementaciones.SMS;
import futbolstats.modelo.JerarquiaPersona.Jugador;
import futbolstats.modelo.JerarquiaTransferencia.FichajeLibre;
import futbolstats.modelo.JerarquiaTransferencia.RescisionContrato;
import futbolstats.modelo.JerarquiaTransferencia.Transferencia;
import futbolstats.modelo.JerarquiaTransferencia.TraspasoDefinitivo;

import java.time.LocalDate;
import java.time.LocalTime;
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

        //el presupuesto es en millones
        Club c1 = new Club("UADE FC", LocalDate.of(2000, 1, 1), "Lima 750", 50.0);
        Club c2 = new Club("Ingenieria FC", LocalDate.of(2005, 5, 10), "Independencia 120", 35.0);
        Club c3 = new Club("Buongiorno Tutti", LocalDate.of(1998, 7, 3), "Italia 67", 25.0);
        Club c4 = new Club("Procyknator", LocalDate.of(2020, 4, 23), "RealTime", 10.0);

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
            //el torneo arranca PLANIFICADO para tener la ventana de pases abierta
            //pasa a EN_CURSO al jugar el primer partido
        } catch (ReglaNegocioException e) {
            System.out.println("Error de carga inicial: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 6) {
            System.out.println("\n--- MENU PRINCIPAL (" + torneo.getEstado() + ") ---");
            System.out.println("1. Comenzar Partido");
            System.out.println("2. Ver Tabla de Posiciones");
            System.out.println("3. Ver Estadisticas de Jugadores");
            System.out.println("4. Ver Reporte Completo del Torneo (Lectura TXT)");
            System.out.println("5. Mercado de Pases y Jugadores Libres");
            System.out.println("6. Salir");
            System.out.print("Elija una opcion: ");

            try {
                opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        if (torneo.getEstado() == EstadoTorneo.PLANIFICADO) {
                            try {
                                torneo.iniciarTorneo();
                                System.out.println("El torneo ha comenzado (ventana de pases cerrada).");
                            } catch (ReglaNegocioException e) {
                                System.out.println("Error: " + e.getMessage());
                                break;
                            }
                        }

                        System.out.println("\n--- NUEVO PARTIDO ---");
                        List<Club> clubes = torneo.getClubesInscriptos();
                        for (int i = 0; i < clubes.size(); i++) {
                            System.out.println((i + 1) + ". " + clubes.get(i).getNombre());
                        }
                        System.out.print("Numero Club Local: ");
                        int numLocal = scanner.nextInt();
                        System.out.print("Numero Club Visitante: ");
                        int numVisita = scanner.nextInt();

                        if (numLocal >= 1 && numLocal <= clubes.size() && numVisita >= 1 && numVisita <= clubes.size() && numLocal != numVisita) {
                            Club local = clubes.get(numLocal - 1);
                            Club visita = clubes.get(numVisita - 1);

                            Partido partidoActivo = new Partido(LocalDate.now(), LocalTime.now(), local, visita);
                            partidoActivo.asignarEstadio((numLocal % 2 == 1) ? e1 : e2);
                            controlador.programarPartido(torneo, partidoActivo);
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
                        gestionarMercadoPases(scanner, torneo, controlador, reportes, gestorNotif);
                        break;

                    case 6:
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

    //submenu para el mercado de pases
    private static void gestionarMercadoPases(Scanner scanner, Torneo torneo,
                                              ControladorSistemaFutbol controlador,
                                              GeneradorReportes reportes,
                                              GestorNotificaciones gestorNotif) {
        int subOpcion = -1;
        while (subOpcion != 0) {
            System.out.println("\n--- MERCADO DE PASES Y JUGADORES LIBRES ---");
            System.out.println("1. Registrar Traspaso Definitivo (Club -> Club)");
            System.out.println("2. Registrar Rescision de Contrato (Club -> Libre)");
            System.out.println("3. Fichar Jugador Libre (Libre -> Club)");
            System.out.println("4. Reporte: Trayectoria de un Jugador");
            System.out.println("5. Reporte: Balance Economico de Clubes");
            System.out.println("6. Ver Libro de Pases Guardado (transferencias.txt)");
            System.out.println("0. Volver al Menu Principal");
            System.out.print("Elija una opcion: ");

            try {
                subOpcion = scanner.nextInt();
                List<Club> clubes = torneo.getClubesInscriptos();

                switch (subOpcion) {
                    case 1: {
                        System.out.println("\nSeleccione Club Vendedor (Origen):");
                        for (int i = 0; i < clubes.size(); i++) {
                            System.out.println((i + 1) + ". " + clubes.get(i).getNombre() + " (Presupuesto: $" + clubes.get(i).getPresupuesto() + "M)");
                        }
                        int idxOrigen = scanner.nextInt() - 1;
                        if (idxOrigen < 0 || idxOrigen >= clubes.size()) {
                            System.out.println("Club invalido.");
                            break;
                        }
                        Club origen = clubes.get(idxOrigen);

                        List<Jugador> plantelOrigen = origen.obtenerPlantel().obtenerJugadores();
                        System.out.println("Seleccione Jugador a transferir:");
                        for (int i = 0; i < plantelOrigen.size(); i++) {
                            Jugador j = plantelOrigen.get(i);
                            System.out.println((i + 1) + ". " + j.getApellido() + " (#" + j.getNumeroCamiseta() + " | Valor mercado: $" + j.getValorMercado() + "M)");
                        }
                        int idxJug = scanner.nextInt() - 1;
                        if (idxJug < 0 || idxJug >= plantelOrigen.size()) {
                            System.out.println("Jugador invalido.");
                            break;
                        }
                        Jugador jugador = plantelOrigen.get(idxJug);

                        System.out.println("Seleccione Club Comprador (Destino):");
                        for (int i = 0; i < clubes.size(); i++) {
                            System.out.println((i + 1) + ". " + clubes.get(i).getNombre() + " (Presupuesto: $" + clubes.get(i).getPresupuesto() + "M)");
                        }
                        int idxDestino = scanner.nextInt() - 1;
                        if (idxDestino < 0 || idxDestino >= clubes.size()) {
                            System.out.println("Club invalido.");
                            break;
                        }
                        Club destino = clubes.get(idxDestino);

                        System.out.print("Monto de la transferencia (en millones): ");
                        double monto = scanner.nextDouble();
                        System.out.print("Nuevo numero de camiseta en " + destino.getNombre() + ": ");
                        int dorsal = scanner.nextInt();

                        Transferencia traspaso = new TraspasoDefinitivo(jugador, origen, destino, monto, dorsal, LocalDate.now());
                        controlador.procesarTransferencia(torneo, traspaso, gestorNotif, "transferencias.txt");
                        System.out.println("EXITO: " + traspaso.generarDetalle());
                        break;
                    }

                    case 2: {
                        System.out.println("\nSeleccione Club que rescinde contrato:");
                        for (int i = 0; i < clubes.size(); i++) {
                            System.out.println((i + 1) + ". " + clubes.get(i).getNombre());
                        }
                        int idxClub = scanner.nextInt() - 1;
                        if (idxClub < 0 || idxClub >= clubes.size()) {
                            System.out.println("Club invalido.");
                            break;
                        }
                        Club origen = clubes.get(idxClub);

                        List<Jugador> plantel = origen.obtenerPlantel().obtenerJugadores();
                        System.out.println("Seleccione Jugador a liberar:");
                        for (int i = 0; i < plantel.size(); i++) {
                            System.out.println((i + 1) + ". " + plantel.get(i).getApellido());
                        }
                        int idxJug = scanner.nextInt() - 1;
                        if (idxJug < 0 || idxJug >= plantel.size()) {
                            System.out.println("Jugador invalido.");
                            break;
                        }
                        Jugador jugador = plantel.get(idxJug);

                        scanner.nextLine();
                        System.out.print("Motivo de la rescision: ");
                        String motivo = scanner.nextLine();

                        Transferencia rescision = new RescisionContrato(jugador, origen, motivo, LocalDate.now());
                        controlador.procesarTransferencia(torneo, rescision, gestorNotif, "transferencias.txt");
                        System.out.println("EXITO: " + rescision.generarDetalle());
                        break;
                    }

                    case 3: {
                        List<Jugador> libres = torneo.getJugadoresLibres();
                        if (libres.isEmpty()) {
                            System.out.println("No hay jugadores libres disponibles en este momento.");
                            break;
                        }
                        System.out.println("\nJugadores Libres disponibles:");
                        for (int i = 0; i < libres.size(); i++) {
                            System.out.println((i + 1) + ". " + libres.get(i).getDatosPersonales());
                        }
                        int idxLibre = scanner.nextInt() - 1;
                        if (idxLibre < 0 || idxLibre >= libres.size()) {
                            System.out.println("Seleccion invalida.");
                            break;
                        }
                        Jugador libre = libres.get(idxLibre);

                        System.out.println("Seleccione Club que lo ficha:");
                        for (int i = 0; i < clubes.size(); i++) {
                            System.out.println((i + 1) + ". " + clubes.get(i).getNombre());
                        }
                        int idxDestino = scanner.nextInt() - 1;
                        if (idxDestino < 0 || idxDestino >= clubes.size()) {
                            System.out.println("Club invalido.");
                            break;
                        }
                        Club destino = clubes.get(idxDestino);

                        System.out.print("Numero de camiseta asignado: ");
                        int dorsal = scanner.nextInt();

                        Transferencia fichaje = new FichajeLibre(libre, destino, dorsal, LocalDate.now());
                        controlador.procesarTransferencia(torneo, fichaje, gestorNotif, "transferencias.txt");
                        System.out.println("EXITO: " + fichaje.generarDetalle());
                        break;
                    }

                    case 4: {
                        List<Jugador> todos = new ArrayList<>();
                        for (Club c : clubes) {
                            todos.addAll(c.obtenerPlantel().obtenerJugadores());
                        }
                        todos.addAll(torneo.getJugadoresLibres());

                        System.out.println("\nSeleccione Jugador para ver su trayectoria:");
                        for (int i = 0; i < todos.size(); i++) {
                            System.out.println((i + 1) + ". " + todos.get(i).getDatosPersonales());
                        }
                        int idx = scanner.nextInt() - 1;
                        if (idx >= 0 && idx < todos.size()) {
                            reportes.generarReporteTrayectoria(todos.get(idx));
                        } else {
                            System.out.println("Jugador invalido.");
                        }
                        break;
                    }

                    case 5:
                        reportes.generarBalanceEconomico(torneo);
                        break;

                    case 6:
                        System.out.println("\n--- LIBRO DE PASES (ARCHIVO) ---");
                        System.out.println(controlador.leerHistorialTransferencias("transferencias.txt"));
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("Opcion invalida.");
                }
            } catch (ReglaNegocioException e) {
                System.out.println("REGLA DE NEGOCIO VIOLADA: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un numero valido.");
                scanner.nextLine();
            }
        }
    }
}