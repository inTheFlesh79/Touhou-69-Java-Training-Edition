package puppy.code;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PantallaHint implements Screen {
    private Touhou game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Texture hintBackground;

    private BitmapFont fontTitle;
    private BitmapFont fontHint;

    private String randomHint;
    private float elapsedTime = 0f; // contador de tiempo
    
    private Screen pantallaDestino;

    public PantallaHint(Touhou game, Screen pantallaDestino) {
        this.game = Touhou.getInstance();
        this.batch = game.getBatch();
        this.pantallaDestino = pantallaDestino;

        camera = Touhou.getInstance().getCamera();
        viewport = Touhou.getInstance().getViewport();

        hintBackground = new Texture(Gdx.files.internal("pauseBackground.png")); // reutilizo el fondo de pausa

        // Fuentes
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("thFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Fuente para el título
        parameter.size = 36;
        parameter.borderColor = Color.MAGENTA;
        parameter.borderWidth = 1;
        parameter.color = Color.WHITE;
        fontTitle = generator.generateFont(parameter);
        fontTitle.getData().setScale(2f);

        // Fuente para la pista
        parameter.size = 22;
        parameter.borderColor = Color.MAGENTA;
        parameter.borderWidth = 1;
        parameter.color = Color.WHITE;
        fontHint = generator.generateFont(parameter);
        fontHint.getData().setScale(1.5f);

        generator.dispose();

        // Cargar pistas desde CSV y elegir una al azar
        List<String> hints = cargarHints("hints.csv");
        if (hints.isEmpty()) {
            randomHint = "No hay pistas disponibles...";
        } else {
            randomHint = hints.get(MathUtils.random(hints.size() - 1));
        }
    }

    private List<String> cargarHints(String path) {
        String text = Gdx.files.internal(path).readString("UTF-8");
        String[] splitText = text.split(","); // separar por coma
        List<String> list = new ArrayList<>();
        for (String h : splitText) {
            if (!h.trim().isEmpty()) {
                list.add(h.trim());
            }
        }
        return list;
    }

    @Override
    public void render(float delta) {
        timeCheck(delta);

        ScreenUtils.clear(0, 0, 0, 1f);
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Fondo
        batch.draw(hintBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Título
        GlyphLayout layoutTitle = new GlyphLayout(fontTitle, "Loading Level");
        float titleX = (viewport.getWorldWidth() - layoutTitle.width) / 2f;
        float titleY = viewport.getWorldHeight() * 0.75f;
        fontTitle.draw(batch, layoutTitle, titleX, titleY);

        // Pista
        GlyphLayout layoutHintTitle = new GlyphLayout(fontHint, "Sabias que?");
        float hintTitleX = (viewport.getWorldWidth() - layoutHintTitle.width) / 2f;
        float hintTitleY = (viewport.getWorldHeight() / 2f) - 250;
        fontHint.draw(batch, layoutHintTitle, hintTitleX, hintTitleY);
        
        GlyphLayout layoutHint = new GlyphLayout(fontHint, randomHint);
        layoutHint.setText(fontHint, randomHint, Color.WHITE, viewport.getWorldWidth() * 0.8f, 1, true); // wrap al 80% del ancho
        float hintX = (viewport.getWorldWidth() - layoutHint.width) / 2f;
        float hintY = (viewport.getWorldHeight() / 2f) - 300;
        fontHint.draw(batch, layoutHint, hintX, hintY);

        batch.end();
    }
    
    public void timeCheck(float delta) {
    	elapsedTime += delta;
        if (elapsedTime >= 3f) {
            // aqui se define a que pantalla se vuelve
            pantallaDestino.resize(1280, 960);
            game.setScreen(pantallaDestino);
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override
    public void dispose() {
        hintBackground.dispose();
        fontTitle.dispose();
        fontHint.dispose();
    }
}
