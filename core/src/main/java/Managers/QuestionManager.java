package Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import Enemies.Pregunta;

public class QuestionManager {
	private ArrayList<Pregunta> preguntas;
    private Random random;

    public QuestionManager() {
        preguntas = new ArrayList<>();
        random = new Random();
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

    public Pregunta getPreguntaAleatoria() {
        if (preguntas.isEmpty()) {
            throw new IllegalStateException("No hay preguntas cargadas");
        }
        return preguntas.get(random.nextInt(preguntas.size()));
    }
    
    public ArrayList<Pregunta> getPreguntasAleatoriasPorCategoria(int categoria, int cantidad) {
        ArrayList<Pregunta> preguntasCat = new ArrayList<>();
        for (Pregunta p : preguntas) {
            if (p.getCategoria() == categoria) {
                preguntasCat.add(p);
            }
        }

        if (preguntasCat.size() < cantidad) {
            throw new IllegalStateException("No hay suficientes preguntas en la categoría " + categoria);
        }

        Collections.shuffle(preguntasCat); // mezclar
        ArrayList<Pregunta> seleccion = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            seleccion.add(preguntasCat.get(i));
        }
        return seleccion;
    }


    public void dispose() {}
}

