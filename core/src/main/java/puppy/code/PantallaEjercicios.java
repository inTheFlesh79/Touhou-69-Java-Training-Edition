package puppy.code;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Enemies.Pregunta;
import Managers.MusicManager;
import Managers.QuestionManager;

public class PantallaEjercicios implements Screen {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private FitViewport screenViewport;
	private Stage stage;
    private Skin skin;
    private QuestionManager questions = new QuestionManager();
    private ShapeRenderer shapeRenderer;
    private final float[] rectY = {250, 180, 110, 40};
    private ArrayList<Pregunta> preguntasRonda;
    private int indicePregunta = 0;
    private int correctas = 0; // (era para ruteo)
    private PantallaJuego tempPJ;
    
    private boolean retryAndBuff;

	public PantallaEjercicios (Touhou game, MusicManager musicMng, PantallaJuego pantallaAnterior, boolean retryAndBuff) {
		tempPJ = pantallaAnterior;
		game = Touhou.getInstance();
		this.retryAndBuff = retryAndBuff;
        this.batch = game.getBatch();
        // camera
		camera = game.getCamera();
		screenViewport = game.getViewport();
		
		stage = new Stage(screenViewport);
        Gdx.input.setInputProcessor(stage);
        
        skin = new Skin();
        
        // Fuente básica
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // Estilo para Label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);
        
        // Estilo para Botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;
        skin.add("default", buttonStyle);
        
        // se obtiene categoria actual
        int categoria = Touhou.getCategoria();

        // obtener 6 preguntas de una categoria
        preguntasRonda = questions.getPreguntasPorCategoria(categoria);
        // mostrar preguntas
        mostrarPregunta(indicePregunta, game, musicMng, pantallaAnterior);
	}
	
	private void mostrarPregunta(int index, Touhou game, MusicManager musicMng, Screen pantallaAnterior) {
		stage.clear(); // limpiar stage
		shapeRenderer = new ShapeRenderer();
		// limpiar stage

	    Pregunta preguntaActual = preguntasRonda.get(index);

	    // Label enunciado
	    Label lblPregunta = new Label(preguntaActual.getEnunciado(), skin);
	    lblPregunta.setPosition(64, 888);
	    lblPregunta.setWrap(true);
	    lblPregunta.setWidth(1178);
	    stage.addActor(lblPregunta);

	    // Botones y click
	    TextButton[] botones = new TextButton[4];
	    for (int i = 0; i < 4; i++) {
	        botones[i] = new TextButton(preguntaActual.getRespuestas()[i], skin);
	        botones[i].setPosition(64, rectY[i] + 12);
	        stage.addActor(botones[i]);

	        Actor areaClick = new Actor();
	        areaClick.setBounds(54, rectY[i], 1174, 48);
	        areaClick.setTouchable(Touchable.enabled);
	        final int opcion = i;

	        areaClick.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	            	preguntaActual.setIndiceSeleccionado(opcion);
	                if (opcion == preguntaActual.getIndiceCorrecto()) {
	                	musicMng.playCorrect();
	                	System.out.println("¡Correcto!");
	                    correctas++;
	                    preguntaActual.setRespondidaCorrecta(true);
	                } else {
	                	musicMng.playIncorrect();
	                    System.out.println("Incorrecto");
	                    preguntaActual.setRespondidaCorrecta(false);
	                }

	                // Siguiente pregunta
	                indicePregunta++;
	                if (indicePregunta < preguntasRonda.size()) {
	                	mostrarPregunta(indicePregunta, game, musicMng, pantallaAnterior);
	                } else {
	                    System.out.println("Ronda finalizada. Correctas: " + correctas);
	                    if (correctas < 4) {
	                    	Touhou.setIntentosRonda(Touhou.getIntentosRonda() + 1);
	                    }
	                    game.setScreen(new PantallaResultados(preguntasRonda, Touhou.getNivel(), Touhou.getIntentosRonda(), musicMng, tempPJ, retryAndBuff));
	                    dispose();
	                }
	            }
	        });

	        stage.addActor(areaClick);
	    }

	    // en caso de que la pregunta tenga imagen se displayea en la pantalla
	    if (preguntaActual.tieneImagen()) {
	        Texture texturaPregunta = new Texture(Gdx.files.internal(preguntaActual.getRuta()));
	        texturaPregunta.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

	        Image imgPregunta = new Image(texturaPregunta);
	        float imgWidth = texturaPregunta.getWidth();
	        float imgHeight = texturaPregunta.getHeight();

	        // variables para crear el area apta para mostrar imagen
	        float rectX = 0;
	        float rectY = 335;
	        float rectWidth = 1200;
	        float rectHeight = 412;
	        float posX = rectX + (rectWidth - imgWidth) / 2f;
	        float posY = rectY + (rectHeight - imgHeight) / 2f;

	        // se setea posicion de imagen en el centro del area disponible
	        imgPregunta.setPosition(posX, posY);
	        stage.addActor(imgPregunta);
	    }
	}

	@Override
	public void render(float delta) {
		// limpia la pantalla con color rojo 
		ScreenUtils.clear(0.25f, 0.0f, 0.08f, 1f);
		// actualizar matrices de la cámara
		camera.update();
		// actualizar
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		// convertir coordenadas de pantalla a coordenadas del stage 
		Vector2 mouseStage = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		float mouseX = mouseStage.x;
		float mouseY = mouseStage.y;

		
		// Dibujar el rectángulo de fondo para preguntas y respuestas
		shapeRenderer.setProjectionMatrix(camera.combined);
	    shapeRenderer.begin(ShapeType.Filled);
	    shapeRenderer.setColor(0, 0, 0, 0.5f); // negro semi-transparente
	    // x, y, ancho, alto
	    shapeRenderer.rect(54, 852, 1178, 84);
	    shapeRenderer.end();
	    
	    // Dibujar borde opcional
	    shapeRenderer.begin(ShapeType.Line);
	    shapeRenderer.setColor(Color.WHITE);
	    shapeRenderer.rect(54, 852, 1178, 84);
	    shapeRenderer.end();

	    for (int i = 0; i < 4 ; i++) {
	        float y = rectY[i];

	        // Cambiar color si el mouse está encima del rectángulo
	        if (mouseX >= 54 && mouseX <= 54 + 1178 && mouseY >= y && mouseY <= y + 48) {
	            shapeRenderer.begin(ShapeType.Filled);
	            shapeRenderer.setColor(Color.LIGHT_GRAY); // color gris
	            shapeRenderer.rect(54, y, 1178, 48);
	            shapeRenderer.end();
	        } else {
	            shapeRenderer.begin(ShapeType.Filled);
	            shapeRenderer.setColor(Color.WHITE); // color normal
	            shapeRenderer.rect(54, y, 1178, 48);
	            shapeRenderer.end();
	        }

	        // Borde
	        shapeRenderer.begin(ShapeType.Line);
	        shapeRenderer.setColor(Color.BLACK);
	        shapeRenderer.rect(54, y, 1178, 48);
	        shapeRenderer.end();
	    }
		stage.act(delta);
        stage.draw();
		batch.end();
	}

	@Override
	public void show() {}

	@Override
	public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		stage.clear(); // limpiar stage
	}

}

