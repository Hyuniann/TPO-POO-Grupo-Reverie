package futbolstats.modelo.Excepciones;

public class ReglaNegocioException extends Exception{
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
