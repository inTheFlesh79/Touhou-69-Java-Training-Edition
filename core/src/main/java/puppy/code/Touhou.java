package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Touhou extends Game {
    private static Touhou instance;
    private SpriteBatch gameBatch;
    private BitmapFont font;
    // global camera + viewport
    private OrthographicCamera camera;
    private FitViewport viewport;
    private static final float VIRTUAL_WIDTH = 1280f;
    private static final float VIRTUAL_HEIGHT = 960f;
    private int highScore;
    // para manejar rotacion de categorias en TODO el juego
    private static int categoriaActual = 1;

    private Touhou() {}

    public static Touhou getInstance() {
        if (instance == null) {
            instance = new Touhou();
        }
        return instance;
    }
    
    private void loadFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("thFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 27;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void create() {
        highScore = 0;
        gameBatch = new SpriteBatch();
        loadFont();
        // standard logical resolution (your “official” resolution)
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
        
        Screen mainMenu = new PantallaMenu();
        this.setScreen(mainMenu);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        gameBatch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {return gameBatch;}
    public BitmapFont getFont() {return font;}
    public FitViewport getViewport() { return viewport; }
    public OrthographicCamera getCamera() { return camera; }
    public int getHighScore() {return highScore;}
    
    public void setHighScore(int highScore) {this.highScore = highScore;}
    
    public static int getCategoria() {
        int cat = categoriaActual;
        categoriaActual++;
        if (categoriaActual > 3) categoriaActual = 1;
        return cat;
    }
}