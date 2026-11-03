package futbolstats.modelo.Interfaces_con_Implementaciones;

import futbolstats.modelo.Entidades_Secundarias.Notificacion;

public class AppMovil implements CanalNotificacion {
    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        System.out.println("[APP MOVIL] Minuto " + notificacion.getMinuto() + " - " + notificacion.getHecho());
    }
}
