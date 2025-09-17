package puppy.code;

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

	public PantallaEjercicios (Touhou game, Screen pantallaAnterior) {
		Touhou.getInstance();
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
        
        shapeRenderer = new ShapeRenderer();


        // Elegir una pregunta random
        Pregunta preguntaActual = questions.getPreguntaAleatoria();
        // se obtiene enunciado
        Label lblPregunta = new Label(preguntaActual.getEnunciado(), skin);
        // se fija posicion del enunciado, con ancho máximo y salto de linea
        lblPregunta.setPosition(60, 740);
        lblPregunta.setWrap(true);
        lblPregunta.setWidth(1100);

        // se agrega en la pantalla el enunciado
        stage.addActor(lblPregunta);
        TextButton[] botones = new TextButton[4];
        
        for (int i = 0; i < 4; i++) {
        	// misma logica para obtener las 4 alternativas
            botones[i] = new TextButton(preguntaActual.getRespuestas()[i], skin);
            botones[i].setPosition(60, rectY[i]+10);
            // se agregan en la pantalla las alternativas
            stage.addActor(botones[i]);
            // caja invisible para click
            Actor areaClick = new Actor();
            areaClick.setBounds(50, rectY[i], 1100, 40);
            areaClick.setTouchable(Touchable.enabled);
            final int index = i;
            areaClick.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                	// CONDICION EN CASO DE QUE EL USUARIO RESPONDA CORRECTAMENTE
                	// POSIBLEMENTE SE PUEDE MANEJAR LA LÓGICA DE BUFFEOS AQUI
                    if (index == preguntaActual.getIndiceCorrecto()) {
                        System.out.println("¡Correcto!");
                        game.setScreen(pantallaAnterior);
        				dispose();
        			// CONDICION EN CASO DE QUE EL USUARIO RESPONDA incorrectamente
                    } else {
                        System.out.println("Incorrecto");
                    }
                }
            });
            stage.addActor(areaClick);
        }
        // verificar si tiene imagen para mostrarla
        if (preguntaActual.tieneImagen()) {
            Texture texturaPregunta = new Texture(Gdx.files.internal(preguntaActual.getRuta()));
            texturaPregunta.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // mejora calidad

            Image imgPregunta = new Image(texturaPregunta);
            imgPregunta.setPosition(400, 320); // la colocas en pantalla
            // NO usamos setSize, mantiene el tamaño original
            stage.addActor(imgPregunta);
        }
	}

	@Override
	public void render(float delta) {
		// limpia la pantalla con color verde oscuro
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
	public void dispose() {ScreenUtils.clear(0, 0, 0.2f, 1);}

}

