package futbolstats.modelo.Entidades_Secundarias;
import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Interfaces_con_Implementaciones.CanalNotificacion;

import java.util.ArrayList;
import java.util.List;

public class Suscriptor {
    private String nombre;
    private List<Club> clubesSeguidos;
    private List<CanalNotificacion> canalesHabilitados;

    public Suscriptor(String nombre) {
        this.nombre = nombre;
        this.clubesSeguidos = new ArrayList<>();
        this.canalesHabilitados = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarClub(Club club) {
        if (club != null) {
            this.clubesSeguidos.add(club);
        }
    }

    public void activarCanal(CanalNotificacion canal) {
        if (canal != null) {
            this.canalesHabilitados.add(canal);
        }
    }

    public List<Club> getClubesSeguidos() {
        return clubesSeguidos;
    }

    public List<CanalNotificacion> getCanalesHabilitados() {
        return canalesHabilitados;
    }
}