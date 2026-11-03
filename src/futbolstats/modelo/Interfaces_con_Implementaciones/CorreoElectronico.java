package futbolstats.modelo.Interfaces_con_Implementaciones;

import futbolstats.modelo.Entidades_Secundarias.Notificacion;

public class CorreoElectronico implements CanalNotificacion{
    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        System.out.println("[EMAIL] Minuto " + notificacion.getMinuto() + " - " + notificacion.getHecho());
    }
}
