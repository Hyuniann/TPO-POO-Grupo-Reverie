package futbolstats.modelo.JerarquiaPersona;
import futbolstats.modelo.Enumeraciones.EspecialidadEntrenador;

import java.time.LocalDate;

public class Entrenador extends Persona{
    private EspecialidadEntrenador especialidad;
    private String licencia;

    public Entrenador(String dni, String nombre, String apellido, LocalDate fechaNacimiento, EspecialidadEntrenador especialidad, String licencia) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.especialidad = especialidad;
        this.licencia = licencia;
    }
}
