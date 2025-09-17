package Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
                    	String enunciado = partes[1].trim();
                        String[] respuestas = Arrays.copyOfRange(partes, 2, 6); // 4 opciones
                        int indiceCorrecto = Integer.parseInt(partes[6].trim()) - 1; // Ajustar a base 0

                        preguntas.add(new Pregunta(id, enunciado, respuestas, indiceCorrecto));
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

    public void dispose() {}
}

