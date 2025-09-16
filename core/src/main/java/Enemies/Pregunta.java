package Enemies;

public class Pregunta {
	private String enunciado;
    private String[] respuestas;
    private int indiceCorrecto;

    public Pregunta(String enunciado, String[] respuestas, int indiceCorrecto) {
        this.enunciado = enunciado;
        this.respuestas = respuestas;
        this.indiceCorrecto = indiceCorrecto;
    }

    public String getEnunciado() { return enunciado; }
    public String[] getRespuestas() { return respuestas; }
    public int getIndiceCorrecto() { return indiceCorrecto; }
}
