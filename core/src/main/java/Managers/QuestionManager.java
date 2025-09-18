package Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import Enemies.Pregunta;

public class QuestionManager {
	private ArrayList<Pregunta> preguntas;

    public QuestionManager() {
        preguntas = new ArrayList<>();
        cargarPreguntas();
    }

    private void cargarPreguntas() {
        try {
            FileHandle file = Gdx.files.internal("preguntas.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.read(), "UTF-8"));

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(",");

                    if (partes.length >= 7) {
                    	String id = partes[0].trim();
                    	int categoria = Integer.parseInt(partes[1].trim());
                    	String enunciado = partes[2].trim();
                        String[] respuestas = Arrays.copyOfRange(partes, 3, 7); // 4 opciones
                        int indiceCorrecto = Integer.parseInt(partes[7].trim()) - 1; // Ajustar a base 0

                        preguntas.add(new Pregunta(id, categoria, enunciado, respuestas, indiceCorrecto));
                    }
                }
            }
            reader.close();

        } catch (IOException e) {
            Gdx.app.error("QuestionManager", "Error al leer preguntas.csv", e);
        }
    }
    
    public ArrayList<Pregunta> getPreguntasPorCategoria(int categoria) {
    	// se crea un arraylist para almacenar preguntas de una categoria específica
        ArrayList<Pregunta> preguntasCat = new ArrayList<>();
        for (Pregunta p : preguntas) {
        	// se extraen todas las preguntas de cierta categoria
            if (p.getCategoria() == categoria) {
            	// se añaden al arraylist
                preguntasCat.add(p);
            }
        }
        
        // deben existir al menos 6 preguntas para que se pueda ejecutar una ronda de preguntas
        if (preguntasCat.size() < 6) {
            throw new IllegalStateException("No hay suficientes preguntas en la categoría " + categoria);
        }
        
        // se mezclan las preguntas obtenidas y almacenadas en el arraylist
        Collections.shuffle(preguntasCat);
        ArrayList<Pregunta> seleccion = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
        	// se crea otro arraylist para almacenar solo 6 preguntas aleatorias de la categoria
            seleccion.add(preguntasCat.get(i));
        }
        return seleccion;
    }


    public void dispose() {}
}

