package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Touhou extends Game {
    private static Touhou instance;
    private SpriteBatch batch;
    private BitmapFont font;
    private int highScore;
    // para manejar rotacion de categorias en TODO el juego
    private static int categoriaActual = 1;

    private Touhou() {
    }

    public static Touhou getInstance() {
        if (instance == null) {
            instance = new Touhou();
        }
        return instance;
    }

    @Override
    public void create() {
        highScore = 0;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        Screen mainMenu = new PantallaMenu();
        this.setScreen(mainMenu);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    
    public static int getCategoria() {
        int cat = categoriaActual;
        categoriaActual++;
        if (categoriaActual > 3) categoriaActual = 1;
        return cat;
    }
}
