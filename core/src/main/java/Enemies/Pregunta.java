package Enemies;

import com.badlogic.gdx.Gdx;

public class Pregunta {
	private String id;
	private int categoria;
	private String enunciado;
    private String[] respuestas;
    private int indiceCorrecto;
    private String rutaImagen;

    public Pregunta(String id, int categoria, String enunciado, String[] respuestas, int indiceCorrecto) {
    	this.id = id;
    	this.categoria = categoria;
        this.enunciado = enunciado;
        this.respuestas = respuestas;
        this.indiceCorrecto = indiceCorrecto;
        
        String ruta = "preguntas/" + this.id + ".png"; //se crea posible ruta
        if (Gdx.files.internal(ruta).exists()) {
        	this.rutaImagen = ruta; //si existe una ruta hacia el archivo, se asigna esta a rutaImagen
        } else {
        	this.rutaImagen = null; //null si no existe imagen
        }
        
    }

    public String getID() { return id; }
    public int getCategoria() { return categoria; }
    public String getEnunciado() { return enunciado; }
    public String[] getRespuestas() { return respuestas; }
    public int getIndiceCorrecto() { return indiceCorrecto; }
    public String getRuta() { return rutaImagen; }
    public boolean tieneImagen() { return rutaImagen != null; }
}
