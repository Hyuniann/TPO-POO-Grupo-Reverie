package futbolstats.controlador;

import futbolstats.modelo.Entidades_Principales.Partido;
import futbolstats.modelo.Entidades_Principales.Torneo;
import futbolstats.modelo.Eventos.Evento;
import futbolstats.modelo.Excepciones.ReglaNegocioException;

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

    public void guardarResumenEnArchivo(Partido partido, String rutaArchivo) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(rutaArchivo);
            fw.write("================================================\n");
            fw.write("ENCUENTRO: " + partido.getClubLocal().getNombre() + " vs " + partido.getClubVisitante().getNombre() + "\n");
            fw.write("JUGADO EL: " + partido.getFecha() + " A LAS " + partido.getHora() + "\n");
            fw.write("MARCADOR FINAL: " + partido.getGolesLocal() + " - " + partido.getGolesVisitante() + "\n");
            fw.write("================================================\n");
            fw.write("DETALLE DE INCIDENCIAS:\n\n");

            if (partido.getEventos().isEmpty()) {
                fw.write("  > No hubo eventos registrados en este encuentro.\n");
            } else {
                for (Evento e : partido.getEventos()) {
                    fw.write("  [" + e.getMinuto() + "'] " + e.getDescripcion() + "\n");
                }
            }
            fw.write("\n================================================\n");
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