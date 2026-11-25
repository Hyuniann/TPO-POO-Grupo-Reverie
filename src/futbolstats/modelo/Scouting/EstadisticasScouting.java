package futbolstats.modelo.Scouting;

public class EstadisticasScouting {
    private int goles;
    private int remates;
    private int tarjetasAmarillas;
    private int tarjetasRojas;
    private int faltas;
    private int partidosConEventosRegistrados;

    public void registrarGol() { goles++; }
    public void registrarRemate() { remates++; }
    public void registrarAmarilla() { tarjetasAmarillas++; }
    public void registrarRoja() { tarjetasRojas++; }
    public void registrarFalta() { faltas++; }
    public void registrarPartidoConEventos() { partidosConEventosRegistrados++; }

    public int getGoles() { return goles; }
    public int getRemates() { return remates; }
    public int getTarjetasAmarillas() { return tarjetasAmarillas; }
    public int getTarjetasRojas() { return tarjetasRojas; }
    public int getFaltas() { return faltas; }
    public int getPartidosConEventosRegistrados() { return partidosConEventosRegistrados; }
}