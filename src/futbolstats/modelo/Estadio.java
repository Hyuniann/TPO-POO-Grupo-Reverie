package futbolstats.modelo;

public class Estadio {
    private String nombre;
    private int capacidad;
    private String calle;
    private String numero;
    private String ciudad;
    private String provincia;
    private String condicionesHabituales;

    public Estadio(String nombre, int capacidad, String calle, String numero, String ciudad, String provincia, String condicionesHabituales) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.condicionesHabituales = condicionesHabituales;
    }

    public String getNombre() {
        return nombre;
    }

    public String obtenerDireccion() {
        return calle + " " + numero + ", " + ciudad + " (" + provincia + ")";
    }
}
