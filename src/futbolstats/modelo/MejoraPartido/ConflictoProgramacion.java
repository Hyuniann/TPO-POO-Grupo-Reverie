package futbolstats.modelo.MejoraPartido;

public class ConflictoProgramacion {
    private TipoConflictoProgramacion tipo;
    private String descripcion;

    public ConflictoProgramacion(TipoConflictoProgramacion tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public TipoConflictoProgramacion getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return tipo + ": " + descripcion;
    }
}