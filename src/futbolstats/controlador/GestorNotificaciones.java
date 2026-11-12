package futbolstats.controlador;
import futbolstats.modelo.Entidades_Principales.Club;
import futbolstats.modelo.Entidades_Secundarias.Notificacion;
import futbolstats.modelo.Entidades_Secundarias.Suscriptor;
import futbolstats.modelo.Interfaces_con_Implementaciones.CanalNotificacion;

import java.util.ArrayList;
import java.util.List;

public class GestorNotificaciones {
    private List<Suscriptor> suscriptores;

    public GestorNotificaciones() {
        this.suscriptores = new ArrayList<>();
    }

    public void agregarSuscriptor(Suscriptor suscriptor) {
        if (suscriptor != null) {
            this.suscriptores.add(suscriptor);
        }
    }


    public void despacharNotificacion(Notificacion notificacion, Club clubLocal, Club clubVisitante) {
        if (notificacion == null) return;

        notificacion.enviarNotificacion();

        for (Suscriptor suscriptor : suscriptores) {
            boolean interesado = false;
            if (clubLocal != null && suscriptor.getClubesSeguidos().contains(clubLocal)) interesado = true;
            if (clubVisitante != null && suscriptor.getClubesSeguidos().contains(clubVisitante)) interesado = true;

            if (interesado) {
                for (CanalNotificacion canal : suscriptor.getCanalesHabilitados()) {
                    canal.enviarNotificacion(notificacion);
                }
            }
        }
    }
}
