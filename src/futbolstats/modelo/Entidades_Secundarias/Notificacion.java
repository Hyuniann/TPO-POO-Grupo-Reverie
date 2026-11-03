package futbolstats.modelo.Entidades_Secundarias;

public class Notificacion {
    private int minuto;
    private String hecho;

    public Notificacion(int minuto, String hecho) {
        this.minuto = minuto;
        this.hecho = hecho;
    }

    public int getMinuto() {
        return minuto;
    }

    public String getHecho() {
        return hecho;
    }

    public void enviarNotificacion() {
        System.out.println("Procesando notificacion del minuto " + minuto + ": " + hecho);
    }
}
