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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Enemies.Pregunta;
import Managers.QuestionManager;


public class PantallaEjercicios implements Screen {

	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private Stage stage;
    private Skin skin;
    private QuestionManager questions = new QuestionManager();
    private ShapeRenderer shapeRenderer;
    private final float[] rectY = {250, 180, 110, 40};
    private ArrayList<Pregunta> preguntasRonda;
    private int indicePregunta = 0;
    private int correctas = 0;

	public PantallaEjercicios (Touhou game, Screen pantallaAnterior) {
		game = Touhou.getInstance();
        this.batch = game.getBatch();
        // camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1200, 800);
		
		stage = new Stage(new ScreenViewport());
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
        mostrarPregunta(indicePregunta, game, pantallaAnterior);
        
	}
	
	private void mostrarPregunta(int index, Touhou game, Screen pantallaAnterior) {
		stage.clear(); // limpiar stage
		shapeRenderer = new ShapeRenderer();
		// limpiar stage

	    Pregunta preguntaActual = preguntasRonda.get(index);

	    // Label enunciado
	    Label lblPregunta = new Label(preguntaActual.getEnunciado(), skin);
	    lblPregunta.setPosition(60, 740);
	    lblPregunta.setWrap(true);
	    lblPregunta.setWidth(1100);
	    stage.addActor(lblPregunta);

	    // Botones y click
	    TextButton[] botones = new TextButton[4];
	    for (int i = 0; i < 4; i++) {
	        botones[i] = new TextButton(preguntaActual.getRespuestas()[i], skin);
	        botones[i].setPosition(60, rectY[i] + 10);
	        stage.addActor(botones[i]);

	        Actor areaClick = new Actor();
	        areaClick.setBounds(50, rectY[i], 1100, 40);
	        areaClick.setTouchable(Touchable.enabled);
	        final int opcion = i;

	        areaClick.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	                if (opcion == preguntaActual.getIndiceCorrecto()) {
	                	System.out.println("¡Correcto!");
	                    correctas++;
	                    System.out.println("correctas+1");
	                    preguntaActual.setRespondidaCorrecta(true);
	                } else {
	                    System.out.println("Incorrecto");
	                    preguntaActual.setRespondidaCorrecta(false);
	                }

	                // Siguiente pregunta
	                indicePregunta++;
	                if (indicePregunta < preguntasRonda.size()) {
	                	mostrarPregunta(indicePregunta, game, pantallaAnterior);
	                } else {
	                    System.out.println("Ronda finalizada. Correctas: " + correctas);
	                    game.setScreen(new PantallaResultados(preguntasRonda, 1, pantallaAnterior));
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
	        float rectY = 292;
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
		
		float mouseX = Gdx.input.getX();
		float mouseY = 800 - Gdx.input.getY(); // invertimos Y porque LibGDX tiene origen abajo
		
		// Dibujar el rectángulo de fondo para preguntas y respuestas
		shapeRenderer.setProjectionMatrix(camera.combined);
	    shapeRenderer.begin(ShapeType.Filled);
	    shapeRenderer.setColor(0, 0, 0, 0.5f); // negro semi-transparente
	    // x, y, ancho, alto
	    shapeRenderer.rect(50, 710, 1100, 70);
	    shapeRenderer.end();
	    
	    // Dibujar borde opcional
	    shapeRenderer.begin(ShapeType.Line);
	    shapeRenderer.setColor(Color.WHITE);
	    shapeRenderer.rect(50, 710, 1100, 70);
	    shapeRenderer.end();

	    for (int i = 0; i < 4 ; i++) {
	        float y = rectY[i];

	        // Cambiar color si el mouse está encima del rectángulo
	        if (mouseX >= 50 && mouseX <= 50 + 1100 && mouseY >= y && mouseY <= y + 40) {
	            shapeRenderer.begin(ShapeType.Filled);
	            shapeRenderer.setColor(Color.LIGHT_GRAY); // color gris
	            shapeRenderer.rect(50, y, 1100, 40);
	            shapeRenderer.end();
	        } else {
	            shapeRenderer.begin(ShapeType.Filled);
	            shapeRenderer.setColor(Color.WHITE); // color normal
	            shapeRenderer.rect(50, y, 1100, 40);
	            shapeRenderer.end();
	        }

	        // Borde
	        shapeRenderer.begin(ShapeType.Line);
	        shapeRenderer.setColor(Color.BLACK);
	        shapeRenderer.rect(50, y, 1100, 40);
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

