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
import futbolstats.modelo.Enumeraciones.*;
import futbolstats.modelo.Eventos.*;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.Interfaces_con_Implementaciones.SMS;
import futbolstats.modelo.JerarquiaPersona.Jugador;
import futbolstats.modelo.Scouting.CalculadorValorMercado;
import futbolstats.modelo.Scouting.ComparadorJugadores;
import futbolstats.modelo.Scouting.EvaluadorJugador;
import futbolstats.modelo.Scouting.InformeJugador;

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

        Jugador j1 = new Jugador("111", "Lionel", "Messi", LocalDate.of(1987, 6, 24), "DELANTERO", 10, 300.0);
        Jugador j2 = new Jugador("222", "Rodrigo", "De Paul", LocalDate.of(1994, 5, 24), "MEDIO", 7, 150.0);
        Jugador j3 = new Jugador("333", "Cristiano", "Ronaldo", LocalDate.of(1985, 2, 5), "DELANTERO", 7, 250.0);
        Jugador j4 = new Jugador("444", "Luka", "Modric", LocalDate.of(1985, 9, 9), "MEDIO", 10, 200.0);
        Jugador j5 = new Jugador("555", "Mateo", "Gonzalez", LocalDate.of(1998, 3, 15), "DEFENSOR", 4, 120.5);
        Jugador j6 = new Jugador("667", "Agustin", "Fernandez", LocalDate.of(2001, 7, 22), "DELANTERO", 9, 95.0);
        Jugador j7 = new Jugador("777", "Thiago", "Romero", LocalDate.of(2003, 11, 5), "ARQUERO", 68, 83.3);
        Jugador j8 = new Jugador("888", "Santiago", "Mendez", LocalDate.of(1995, 1, 30), "ARQUERO", 1, 57.0);

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

        while (opcion != 10) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Comenzar Partido");
            System.out.println("2. Ver Tabla de Posiciones");
            System.out.println("3. Ver Estadisticas de Jugadores");
            System.out.println("4. Ver Reporte Completo del Torneo (Lectura TXT)");
            System.out.println("5. Programar un encuentro");
            System.out.println("6. Consultar calendario");
            System.out.println("7. Generar informe de scouting");
            System.out.println("8. Comparar dos jugadores");
            System.out.println("9. Iniciar un partido programado");
            System.out.println("10. Salir");
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
                            try {
                                controlador.programarPartido(torneo, partidoActivo);
                            } catch (ReglaNegocioException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            gestionarPartido(partidoActivo, controlador, gestorNotif, torneo, scanner);
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
                        mostrarClubes(torneo);
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

                    case 7:
                        List<Jugador> jugadoresScouting = obtenerJugadores(torneo);
                        mostrarJugadores(jugadoresScouting);
                        System.out.print("Seleccione el numero del jugador a evaluar: ");
                        int numeroJugador = scanner.nextInt();
                        if (numeroJugador < 1 || numeroJugador > jugadoresScouting.size()) {
                            System.out.println("Jugador invalido.");
                            break;
                        }
                        try {
                            EvaluadorJugador evaluador = new EvaluadorJugador();
                            InformeJugador informe = evaluador.evaluar(
                                    jugadoresScouting.get(numeroJugador - 1), torneo);

                            CalculadorValorMercado calculador = new CalculadorValorMercado();
                            calculador.actualizarValorMercado(informe);

                            System.out.println(informe.generarResumen());
                        } catch (ReglaNegocioException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 8:
                        List<Jugador> jugadoresComparacion = obtenerJugadores(torneo);
                        mostrarJugadores(jugadoresComparacion);
                        System.out.print("Seleccione el primer jugador: ");
                        int primero = scanner.nextInt();
                        System.out.print("Seleccione el segundo jugador: ");
                        int segundo = scanner.nextInt();
                        if (primero < 1 || primero > jugadoresComparacion.size() ||
                                segundo < 1 || segundo > jugadoresComparacion.size() || primero == segundo) {
                            System.out.println("Seleccion invalida. Elija dos jugadores diferentes.");
                            break;
                        }
                        try {
                            EvaluadorJugador evaluador = new EvaluadorJugador();
                            InformeJugador informeUno = evaluador.evaluar(
                                    jugadoresComparacion.get(primero - 1), torneo);
                            InformeJugador informeDos = evaluador.evaluar(
                                    jugadoresComparacion.get(segundo - 1), torneo);

                            CalculadorValorMercado calculador = new CalculadorValorMercado();
                            calculador.actualizarValorMercado(informeUno);
                            calculador.actualizarValorMercado(informeDos);

                            ComparadorJugadores comparador = new ComparadorJugadores();
                            System.out.println(comparador.comparar(informeUno, informeDos));
                        } catch (ReglaNegocioException e) {
                            System.out.println(e.getMessage());
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
                        gestionarPartido(partidosPendientes.get(partidoElegido - 1),
                                controlador, gestorNotif, torneo, scanner);
                        break;

                    case 10:
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

    private static void gestionarPartido(Partido partidoActivo,
                                         ControladorSistemaFutbol controlador,
                                         GestorNotificaciones gestorNotif,
                                         Torneo torneo,
                                         Scanner scanner) {
        partidoActivo.asignarEstado(EstadoPartido.EN_JUEGO);
        System.out.println("El partido " + partidoActivo.getClubLocal().getNombre() + " vs " +
                partidoActivo.getClubVisitante().getNombre() + " ha comenzado.");

        boolean jugando = true;
        while (jugando) {
            System.out.println("\n[PARTIDO EN JUEGO] Opciones: 1.Gol | 2.Tarjeta | 3.Remate | 4.Falta | 5.Terminar Partido");
            int accion = scanner.nextInt();

            if (accion == 5) {
                partidoActivo.finalizar();
                controlador.guardarResumenEnArchivo(torneo, "torneo.txt");
                System.out.println("Partido finalizado.");
                jugando = false;
            } else if (accion >= 1 && accion <= 4) {
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
                Jugador jugadorSeleccionado = enCancha.get(numJug - 1);

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
                    evento = new Gol(minuto, "Gol", jugadorSeleccionado, TipoGol.A_FAVOR);
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
                    evento = new Tarjeta(minuto, "Infraccion", jugadorSeleccionado, motivo, color);
                } else if (accion == 3) {
                    System.out.println("Zona del remate: 1.Area chica | 2.Area grande | 3.Fuera del area | 4.Penal");
                    int zona = scanner.nextInt();
                    if (zona < 1 || zona > 4) {
                        System.out.println("Zona invalida. No se registro el evento.");
                        continue;
                    }
                    evento = new Remate(minuto, "Remate", jugadorSeleccionado, ZonaRemate.values()[zona - 1]);
                } else if (accion == 4) {
                    System.out.println("Tipo de falta: 1.Leve | 2.Grave | 3.Tactica");
                    int tipo = scanner.nextInt();
                    if (tipo < 1 || tipo > 3) {
                        System.out.println("Tipo de falta invalido. No se registro el evento.");
                        continue;
                    }
                    evento = new Falta(minuto, "Falta", jugadorSeleccionado, TipoFalta.values()[tipo - 1]);
                }

                try {
                    controlador.registrarEvento(partidoActivo, evento);
                    System.out.println("Evento registrado.");
                    Notificacion alerta = new Notificacion(evento.getMinuto(), evento.getDescripcion());
                    gestorNotif.despacharNotificacion(alerta,
                            partidoActivo.getClubLocal(), partidoActivo.getClubVisitante());
                } catch (ReglaNegocioException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Accion inexistente.");
            }
        }
    }

    private static void mostrarClubes(Torneo torneo) {
        for (int i = 0; i < torneo.getClubesInscriptos().size(); i++) {
            System.out.println((i + 1) + ". " + torneo.getClubesInscriptos().get(i).getNombre());
        }
    }

    private static List<Jugador> obtenerJugadores(Torneo torneo) {
        List<Jugador> jugadores = new ArrayList<>();
        for (Club club : torneo.getClubesInscriptos()) {
            jugadores.addAll(club.obtenerPlantel().obtenerJugadores());
        }
        return jugadores;
    }

    private static void mostrarJugadores(List<Jugador> jugadores) {
        System.out.println("\n--- JUGADORES DISPONIBLES ---");
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            System.out.println((i + 1) + ". " + jugador.getNombre() + " " + jugador.getApellido());
        }
    }
}