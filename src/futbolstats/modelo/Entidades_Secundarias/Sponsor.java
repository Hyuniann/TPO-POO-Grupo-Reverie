package futbolstats.modelo.Entidades_Secundarias;
import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Principales.Torneo;

public class Sponsor {
    private String nombre;
    private String marca;
    private String logo;

    public Sponsor(String nombre, String marca, String logo) {
        this.nombre = nombre;
        this.marca = marca;
        this.logo = logo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }

    public String getLogo() {
        return logo;
    }

    // Métodos exactos del diagrama
    public void patrocinarClub(Club club) {
        if (club != null) {
        }
    }

    public void patrocinarTorneo(Torneo torneo) {
        if (torneo != null) {
        }
    }
}