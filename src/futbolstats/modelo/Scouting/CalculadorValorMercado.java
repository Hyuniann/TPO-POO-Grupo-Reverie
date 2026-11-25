package futbolstats.modelo.Scouting;

import futbolstats.modelo.Excepciones.ReglaNegocioException;
import futbolstats.modelo.JerarquiaPersona.Jugador;

public class CalculadorValorMercado {
    private final double porcentajePorPunto;
    private final double variacionPositivaMaxima;

    public CalculadorValorMercado() {
        this(1.0, 50.0);
    }

    public CalculadorValorMercado(double porcentajePorPunto, double variacionPositivaMaxima) {
        if (porcentajePorPunto < 0 || variacionPositivaMaxima < 0) {
            throw new IllegalArgumentException("Los porcentajes de configuracion no pueden ser negativos.");
        }
        this.porcentajePorPunto = porcentajePorPunto;
        this.variacionPositivaMaxima = variacionPositivaMaxima;
    }

    public double calcularValor(InformeJugador informe) throws ReglaNegocioException {
        validarInforme(informe);

        double variacion = informe.getPuntaje() * porcentajePorPunto;

        if (variacion > variacionPositivaMaxima) {
            variacion = variacionPositivaMaxima;
        }
        if (variacion < -100.0) {
            variacion = -100.0;
        }

        double valorInicial = informe.getJugador().getValorMercadoInicial();
        double nuevoValor = valorInicial * (1.0 + variacion / 100.0);
        return Math.max(0.0, nuevoValor);
    }

    public double actualizarValorMercado(InformeJugador informe) throws ReglaNegocioException {
        double nuevoValor = calcularValor(informe);
        Jugador jugador = informe.getJugador();
        jugador.actualizarValorMercado(nuevoValor);
        informe.registrarValorMercadoCalculado(nuevoValor);
        return nuevoValor;
    }

    private void validarInforme(InformeJugador informe) throws ReglaNegocioException {
        if (informe == null || informe.getJugador() == null) {
            throw new ReglaNegocioException("No se puede calcular el valor de mercado sin un informe valido.");
        }
    }
}
