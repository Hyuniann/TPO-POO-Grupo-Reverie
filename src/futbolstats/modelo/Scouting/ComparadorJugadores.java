package futbolstats.modelo.Scouting;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ComparadorJugadores {

    public String comparar(InformeJugador informeUno, InformeJugador informeDos) {
        if (informeUno == null || informeDos == null) {
            return "No se pueden comparar informes incompletos.";
        }

        StringBuilder resultado = new StringBuilder();
        resultado.append("========== COMPARACION DE SCOUTING ==========\n");
        resultado.append(informeUno.getJugador().getApellido()).append(": ")
                .append(formato(informeUno.getPuntaje())).append(" puntos | Valor actual: ")
                .append(formato(informeUno.getValorMercadoCalculado())).append(" u.m.\n");
        resultado.append(informeDos.getJugador().getApellido()).append(": ")
                .append(formato(informeDos.getPuntaje())).append(" puntos | Valor actual: ")
                .append(formato(informeDos.getValorMercadoCalculado())).append(" u.m.\n");

        resultado.append("Goles: ").append(informeUno.getEstadisticas().getGoles()).append(" vs. ")
                .append(informeDos.getEstadisticas().getGoles()).append("\n");
        resultado.append("Remates: ").append(informeUno.getEstadisticas().getRemates()).append(" vs. ")
                .append(informeDos.getEstadisticas().getRemates()).append("\n");
        resultado.append("Amarillas: ").append(informeUno.getEstadisticas().getTarjetasAmarillas()).append(" vs. ")
                .append(informeDos.getEstadisticas().getTarjetasAmarillas()).append("\n");
        resultado.append("Rojas: ").append(informeUno.getEstadisticas().getTarjetasRojas()).append(" vs. ")
                .append(informeDos.getEstadisticas().getTarjetasRojas()).append("\n");
        resultado.append("Faltas: ").append(informeUno.getEstadisticas().getFaltas()).append(" vs. ")
                .append(informeDos.getEstadisticas().getFaltas()).append("\n");

        if (informeUno.getPuntaje() > informeDos.getPuntaje()) {
            resultado.append("Mayor puntaje segun el criterio seleccionado: ")
                    .append(informeUno.getJugador().getApellido());
        } else if (informeDos.getPuntaje() > informeUno.getPuntaje()) {
            resultado.append("Mayor puntaje segun el criterio seleccionado: ")
                    .append(informeDos.getJugador().getApellido());
        } else {
            resultado.append("Ambos jugadores tienen el mismo puntaje.");
        }

        return resultado.toString();
    }

    private String formato(double valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("es", "AR"));
        DecimalFormat formato = new DecimalFormat("#,##0.00", simbolos);
        return formato.format(valor);
    }
}