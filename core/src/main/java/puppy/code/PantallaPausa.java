package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class PantallaPausa implements Screen {

	private Touhou game;
	private Screen pantallaAnterior;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;

	public PantallaPausa (Touhou game, Screen pantallaAnterior) {
		this.game = Touhou.getInstance();
        this.batch = game.getBatch();
        this.pantallaAnterior = pantallaAnterior;
        // camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1200, 800);
		
		stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        skin = new Skin();
        
        // Fuente básica
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);
        
        // Estilo para Botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;
        skin.add("default", buttonStyle);
        
        shapeRenderer = new ShapeRenderer();
        
        TextButton boton = new TextButton("reanudar", skin);
        
        boton.setPosition(570, 312);


        stage.addActor(boton);

	}

	@Override
	public void render(float delta) {
		// limpia la pantalla con color azul
		ScreenUtils.clear(0.0f, 0.08f, 0.16f, 1f);
		// actualizar matrices de la cámara
		camera.update();
		// actualizar
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		float mouseX = Gdx.input.getX();
		float mouseY = 800 - Gdx.input.getY(); // invertimos Y porque LibGDX tiene origen abajo
		
		shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE); // color normal
        shapeRenderer.rect(200, 400, 800, 100);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeType.Line);
	    shapeRenderer.setColor(Color.BLACK);
	    shapeRenderer.rect(200, 400, 800, 100);
	    shapeRenderer.end();

	    if (mouseX >= 550 && mouseX <= 550 + 100 && mouseY >= 300 && mouseY <= 300 + 40) {
	    	shapeRenderer.begin(ShapeType.Filled);
	        shapeRenderer.setColor(Color.LIGHT_GRAY); // color gris
	        shapeRenderer.rect(550, 300, 100, 40);
	        shapeRenderer.end();
	        if (Gdx.input.isTouched()) {
	    		game.setScreen(pantallaAnterior);
	    		dispose();
	    	}
	    } else {
	        shapeRenderer.begin(ShapeType.Filled);
	        shapeRenderer.setColor(Color.WHITE); // color normal
	        shapeRenderer.rect(550, 300, 100, 40);
	        shapeRenderer.end();
	    }
	    
	    // verificar si se sale del escape
	    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
	    	game.setScreen(pantallaAnterior);
    		dispose();
		}

	    // Borde
	    shapeRenderer.begin(ShapeType.Line);
	    shapeRenderer.setColor(Color.BLACK);
	    shapeRenderer.rect(550, 300, 100, 40);
	    shapeRenderer.end();
	    
	    BitmapFont fontGrande = new BitmapFont(); // nueva instancia
	    fontGrande.getData().setScale(5f);        // tamaño grande
	    fontGrande.setColor(Color.RED);           // color solo para esta fuente

	    fontGrande.draw(batch, "PAUSA", 480, 490);
	    
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

