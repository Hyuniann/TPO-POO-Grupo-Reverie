package futbolstats.modelo;
import java.time.LocalDate;

public class Arbitro extends Persona {
    private CategoriaArbitro categoria;
    private String colegioArbitral;

    public Arbitro(String dni, String nombre, String apellido, LocalDate fechaNacimiento,CategoriaArbitro categoria, String colegioArbitral) {
        super(dni, nombre, apellido, fechaNacimiento);
        this.categoria = categoria;
        this.colegioArbitral = colegioArbitral;
    }
    public CategoriaArbitro getCategoria() { return categoria; }
}
