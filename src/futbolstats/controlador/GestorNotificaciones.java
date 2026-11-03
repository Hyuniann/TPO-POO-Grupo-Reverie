package futbolstats.controlador;
import futbolstats.modelo.*;
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

    public void despacharNotificacion(Notificacion notificacion) {
        if (notificacion == null) return;

        notificacion.enviarNotificacion();

        for (Suscriptor suscriptor : suscriptores) {
            for (CanalNotificacion canal : suscriptor.getCanalesHabilitados()) {
                canal.enviarNotificacion(notificacion);
            }
        }
    }
}
