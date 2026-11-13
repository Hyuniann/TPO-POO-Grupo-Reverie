package futbolstats.controlador;

import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Enumeraciones.EstadoPartido;
import futbolstats.modelo.Eventos.Evento;
import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

public class ControladorSistemaFutbol {

    public ControladorSistemaFutbol() {
    }

    public void programarPartido(Torneo torneo, Partido partido) {
        if (torneo != null && partido != null) {
            torneo.programarPartido(partido);
        }
    }

    public void registrarEvento(Partido partido, Evento evento) throws ReglaNegocioException {
        if (partido != null && evento != null) {
            partido.registrarEvento(evento);
        }
    }

    public void guardarResumenEnArchivo(Torneo torneo, String rutaArchivo) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(rutaArchivo);
            fw.write("========== REPORTE GLOBAL: " + torneo.getNombre() + " ==========\n\n");

            // TABLA 1: TORNEO (POSICIONES)
            fw.write("--- 1. TABLA DE POSICIONES ---\n");
            for (Club c : torneo.getClubesInscriptos()) {
                int puntos = 0;
                for (Partido p : torneo.getPartidos()) {
                    if (p.getEstado() == EstadoPartido.FINALIZADO) {
                        if (p.getClubLocal().equals(c)) {
                            if (p.getGolesLocal() > p.getGolesVisitante()) puntos += 3;
                            else if (p.getGolesLocal() == p.getGolesVisitante()) puntos += 1;
                        } else if (p.getClubVisitante().equals(c)) {
                            if (p.getGolesVisitante() > p.getGolesLocal()) puntos += 3;
                            else if (p.getGolesVisitante() == p.getGolesLocal()) puntos += 1;
                        }
                    }
                }
                fw.write(c.getNombre() + " - Puntos: " + puntos + "\n");
            }

            // TABLA 2: PARTIDOS Y EVENTOS
            fw.write("\n--- 2. HISTORIAL DE PARTIDOS ---\n");
            if (torneo.getPartidos().isEmpty()) {
                fw.write("No hay partidos registrados.\n");
            } else {
                for (Partido p : torneo.getPartidos()) {
                    fw.write("\nPartido: " + p.getClubLocal().getNombre() + " " + p.getGolesLocal() +
                            " - " + p.getGolesVisitante() + " " + p.getClubVisitante().getNombre() + "\n");

                    fw.write("Fecha: " + p.getFecha() + " | Hora: " + p.getHora());
                    if (p.getEstadio() != null) {
                        fw.write(" | Estadio: " + p.getEstadio().getNombre() + "\n");
                    } else {
                        fw.write("\n");
                    }

                    fw.write("Incidencias:\n");
                    if (p.getEventos().isEmpty()) fw.write("  Sin incidencias.\n");
                    for (Evento e : p.getEventos()) {
                        fw.write("  [" + e.getMinuto() + "'] " + e.getDescripcion() + "\n");
                    }
                }
            }

            // TABLA 3: ESTADISTICAS DE JUGADORES
            fw.write("\n--- 3. JUGADORES Y ESTADISTICAS ---\n");
            for (Club c : torneo.getClubesInscriptos()) {
                fw.write("\nPlantel de " + c.getNombre() + ":\n");
                for (Jugador j : c.obtenerPlantel().obtenerJugadores()) {
                    fw.write("  - " + j.getDatosPersonales() + " | Goles: " + j.getGolesAcumulados() +
                            " | Tarjetas: " + j.getTarjetasAcumuladas() + "\n");
                }
            }
            fw.write("\n========================================================\n");

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivo: " + e.getMessage());
        } finally {
            try {
                if (fw != null) fw.close();
            } catch (IOException ex) {
            }
        }
    }

    public String leerResumenPartido(String ruta) {
        StringBuilder contenido = new StringBuilder();
        BufferedReader br = null;
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                return "El archivo no existe todavia.";
            }
            br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer archivo: " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
            }
        }
        return contenido.toString();
    }
}