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

public class PantallaPausa implements Screen {
    private Touhou game;
    private SpriteBatch batch;
    private PantallaJuego pantallaAnterior;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Texture pauseBackground;
    private Sound pickingSound;
    private Sound enterSound;

    // Fonts
    private BitmapFont fontPause;
    private BitmapFont fontOptionBright;
    private BitmapFont fontOptionDark;

    // Menu items
    private String[] menuItems = {"Return to the Game", "Quit"};
    private int selectedIndex = 0; // 0 = Return, 1 = Quit

    // Layout cache for options (for hitboxes)
    private GlyphLayout[] optionLayouts;

    public PantallaPausa(Touhou game, PantallaJuego pantallaAnterior) {
        this.game = Touhou.getInstance();
        this.batch = game.getBatch();
        this.pantallaAnterior = pantallaAnterior;

        camera = Touhou.getInstance().getCamera();
        viewport = Touhou.getInstance().getViewport();

        pauseBackground = new Texture(Gdx.files.internal("pauseBackground.png"));
        pickingSound = Gdx.audio.newSound(Gdx.files.internal("pickOption.ogg"));
        enterSound = Gdx.audio.newSound(Gdx.files.internal("enterSound.ogg"));

        // Load font with outline
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("thFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderColor = Color.MAGENTA;
        parameter.borderWidth = 1;

        // Big "Pause" font (pixel-art style)
        parameter.size = 36; // half of 72
        fontPause = generator.generateFont(parameter);
        fontPause.getData().setScale(2f);
        fontPause.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Bright option font (pixel-art style)
        parameter.size = 18; // half of 36
        parameter.color = Color.WHITE;
        fontOptionBright = generator.generateFont(parameter);
        fontOptionBright.getData().setScale(2f);
        fontOptionBright.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Darker option font (pixel-art style)
        parameter.size = 18; // half of 36
        parameter.color = new Color(0.6f, 0.6f, 0.6f, 1f); // greyed out
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
        ScreenUtils.clear(0.0f, 0.08f, 0.16f, 1f);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        handleInput();

        batch.begin();
        // Draw background filling viewport
        batch.draw(pauseBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Draw "Pause" centered at 3/4 height
        float titleY = viewport.getWorldHeight() * 0.75f;
        GlyphLayout layout = new GlyphLayout(fontPause, "Pause");
        fontPause.draw(batch, layout, (viewport.getWorldWidth() - layout.width) / 2f, titleY);

        // Draw options
        for (int i = 0; i < menuItems.length; i++) {
            BitmapFont font = (i == selectedIndex) ? fontOptionBright : fontOptionDark;
            optionLayouts[i].setText(font, menuItems[i]);
            float x = (viewport.getWorldWidth() - optionLayouts[i].width) / 2f;
            float y = (viewport.getWorldHeight() / 2f) - (i * 50);
            font.draw(batch, optionLayouts[i], x, y);
        }

        batch.end();
    }

    private void handleInput() {
        // Keyboard input
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            pickingSound.play(0.7f);
            selectedIndex = (selectedIndex - 1 + menuItems.length) % menuItems.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            pickingSound.play(0.7f);
            selectedIndex = (selectedIndex + 1) % menuItems.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            activateOption(selectedIndex);
        }

        // Mouse hover
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);
        for (int i = 0; i < menuItems.length; i++) {
            float x = (viewport.getWorldWidth() - optionLayouts[i].width) / 2f;
            float y = (viewport.getWorldHeight() / 2f) - (i * 50);
            float w = optionLayouts[i].width;
            float h = optionLayouts[i].height;

            if (mouse.x >= x && mouse.x <= x + w && mouse.y >= y - h && mouse.y <= y) {
                if (selectedIndex != i) {
                    pickingSound.play(0.7f);
                }
                selectedIndex = i;

                if (Gdx.input.justTouched()) {
                    activateOption(i);
                }
            }
        }
    }

    private void activateOption(int index) {
        enterSound.play(0.7f);
        if (index == 0) {
            // Return
            pantallaAnterior.setPaused(false);
            pantallaAnterior.getMusicManager().unpauseMusic();
            game.setScreen(pantallaAnterior);
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
    @Override public void dispose() {
        pauseBackground.dispose();
        fontPause.dispose();
        fontOptionBright.dispose();
        fontOptionDark.dispose();
    }
}
