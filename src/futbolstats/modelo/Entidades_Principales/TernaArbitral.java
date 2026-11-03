package futbolstats.modelo.Entidades_Principales;

import futbolstats.modelo.Enumeraciones.CategoriaArbitro;
import futbolstats.modelo.JerarquiaPersona.Arbitro;

public class TernaArbitral {
    private Arbitro principal;
    private Arbitro asistente1;
    private Arbitro asistente2;

    public TernaArbitral() {

    }

    public void asignarArbitroPrincipal(Arbitro arbitro) {
        this.principal = arbitro;
    }

    public void asignarArbitroAsistente1(Arbitro arbitro) {
        this.asistente1 = arbitro;
    }

    public void asignarArbitroAsistente2(Arbitro arbitro) {
        this.asistente2 = arbitro;
    }

    public boolean esInternacional() {
        if (principal == null || asistente1 == null || asistente2 == null) {
            return false;
        }
        return principal.getCategoria() == CategoriaArbitro.INTERNACIONAL &&
                asistente1.getCategoria() == CategoriaArbitro.INTERNACIONAL &&
                asistente2.getCategoria() == CategoriaArbitro.INTERNACIONAL;
    }
}
