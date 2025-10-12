package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Managers.MusicManager;

public class PantallaRetryBuff implements Screen {
    private Touhou game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Texture retryBackground;

    private BitmapFont fontTitle;
    private BitmapFont fontHint;
    
    private BitmapFont fontOptionBright;
    private BitmapFont fontOptionDark;
    private GlyphLayout[] optionLayouts;
    private Sound pickingSound;
    private Sound enterSound;
    private int selectedIndex = 0;
    private String[] menuItems = {"Continue", "Quit"};
    // Timer vars
    private boolean optionActivated = false;
    private float timer = 0f;
    private int pendingOption = -1; // which option to execute after timer
    
    private MusicManager musicMng;
    
    public PantallaRetryBuff(MusicManager musicMng) {
        this.game = Touhou.getInstance();
        this.batch = game.getBatch();
        this.musicMng = musicMng;

        camera = Touhou.getInstance().getCamera();
        viewport = Touhou.getInstance().getViewport();

        retryBackground = new Texture(Gdx.files.internal("testCodeBackground.png")); // reutilizo el fondo de pausa
        pickingSound = Gdx.audio.newSound(Gdx.files.internal("pickOption.ogg"));
        enterSound = Gdx.audio.newSound(Gdx.files.internal("enterSound.ogg"));
        
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
        
     // Option bright
        parameter.size = 18;
        parameter.color = Color.WHITE;
        fontOptionBright = generator.generateFont(parameter);
        fontOptionBright.getData().setScale(2f);
        fontOptionBright.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        // Option dark
        parameter.size = 18;
        parameter.color = new Color(0.6f, 0.6f, 0.6f, 1f);
        fontOptionDark = generator.generateFont(parameter);
        fontOptionDark.getData().setScale(2f);
        fontOptionDark.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        generator.dispose();
        
        optionLayouts = new GlyphLayout[menuItems.length];
        for (int i = 0; i < menuItems.length; i++) {
            optionLayouts[i] = new GlyphLayout(fontOptionBright, menuItems[i]);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1f);
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        if (!optionActivated) {
            handleInput();
        } else {
            timer += delta;
            if (timer >= 1f && pendingOption != -1) {
                executeOption(pendingOption);
                pendingOption = -1;
            }
        }
        
        batch.begin();
        // Fondo
        batch.draw(retryBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Título
        GlyphLayout layoutTitle = new GlyphLayout(fontTitle, "Retry + Buff");
        float titleX = (viewport.getWorldWidth() - layoutTitle.width) / 2f;
        float titleY = viewport.getWorldHeight() * 0.75f;
        fontTitle.draw(batch, layoutTitle, titleX, titleY);
        
        String text = "Esta opcion te permite realizar una ronda de preguntas antes de comenzar el juego, para obtener recompensar y facilitar tu experiencia!";
        GlyphLayout layoutText = new GlyphLayout(fontHint, text);
        layoutText.setText(fontHint, text, Color.WHITE, viewport.getWorldWidth() * 0.8f, 1, true); // wrap al 80% del ancho
        float textX = (viewport.getWorldWidth() - layoutText.width) / 2f;
        float textY = (viewport.getWorldHeight() / 2f) + 100;
        fontHint.draw(batch, layoutText, textX, textY);

        // Draw menu options
        for (int i = 0; i < menuItems.length; i++) {
            BitmapFont font = (i == selectedIndex) ? fontOptionBright : fontOptionDark;
            optionLayouts[i].setText(font, menuItems[i]);
            float x = (viewport.getWorldWidth() - optionLayouts[i].width) / 2f;
            float y = (viewport.getWorldHeight() / 2f) - (i * 60) - 250;
            font.draw(batch, optionLayouts[i], x, y);
        }
        
        batch.end();
    }
    
    private void handleInput() {
        // Keyboard navigation
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            pickingSound.play(0.7f);
            selectedIndex = (selectedIndex - 1 + menuItems.length) % menuItems.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            pickingSound.play(0.7f);
            selectedIndex = (selectedIndex + 1) % menuItems.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            queueOption(selectedIndex);
        }

        // Mouse hover & click
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);

        for (int i = 0; i < menuItems.length; i++) {
            float x = (viewport.getWorldWidth() - optionLayouts[i].width) / 2f;
            float y = (viewport.getWorldHeight() / 2f) - (i * 60) - 250;
            float w = optionLayouts[i].width;
            float h = optionLayouts[i].height;

            if (mouse.x >= x && mouse.x <= x + w && mouse.y >= y - h && mouse.y <= y) {
                if (selectedIndex != i) pickingSound.play(0.7f);
                selectedIndex = i;

                if (Gdx.input.justTouched()) {
                    queueOption(i);
                }
            }
        }
    }
    
    private void queueOption(int index) {
        enterSound.play(0.7f);
        optionActivated = true;
        timer = 0f;
        pendingOption = index;
    }
    
    private void executeOption(int index) {
        if (index == 0) {
        	// Continue (Retry + Buff)
        	musicMng.resetMusicMng();
        	Screen ss = new PantallaEjercicios(game, musicMng, new PantallaJuego(1, 3, 0, 10), true);
        	ss.resize(1280, 960);
        	game.setScreen(ss);
            dispose();
        } else if (index == 1) {
            // Quit
            Gdx.app.exit();
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
    	retryBackground.dispose();
        fontTitle.dispose();
        fontHint.dispose();
    }
}
