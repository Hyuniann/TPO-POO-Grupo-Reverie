package futbolstats.modelo.Entidades_Secundarias;
import java.time.LocalDate;

public class Patrocinio {
    private double monto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Patrocinio(double monto, LocalDate fechaInicio, LocalDate fechaFin) {
        this.monto = monto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        return (hoy.isEqual(fechaInicio) || hoy.isAfter(fechaInicio)) &&
                (hoy.isEqual(fechaFin) || hoy.isBefore(fechaFin));
    }
}