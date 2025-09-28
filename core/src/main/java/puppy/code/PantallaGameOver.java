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

public class PantallaGameOver implements Screen {
    private Touhou game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture background;
    private Sound pickingSound;
    private Sound enterSound;

    // Fonts
    private BitmapFont fontTitle;
    private BitmapFont fontOptionBright;
    private BitmapFont fontOptionDark;

    // Menu items
    private String[] menuItems = {"Retry", "Quit"};
    private int selectedIndex = 0;

    private GlyphLayout[] optionLayouts;

    // Timer vars
    private boolean optionActivated = false;
    private float timer = 0f;
    private int pendingOption = -1; // which option to execute after timer

    public PantallaGameOver() {
        game = Touhou.getInstance();
        batch = game.getBatch();
        camera = Touhou.getInstance().getCamera();
        viewport = Touhou.getInstance().getViewport();

        background = new Texture(Gdx.files.internal("gameOverBg.png"));
        pickingSound = Gdx.audio.newSound(Gdx.files.internal("pickOption.ogg"));
        enterSound = Gdx.audio.newSound(Gdx.files.internal("enterSound.ogg"));

        // Load fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("thFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderColor = Color.RED;
        parameter.borderWidth = 1;

        // Title font
        parameter.size = 36;
        fontTitle = generator.generateFont(parameter);
        fontTitle.getData().setScale(2f);
        fontTitle.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

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
        ScreenUtils.clear(0, 0, 0.1f, 1);

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

        // Draw background filling viewport
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Draw "Game Over" title
        GlyphLayout titleLayout = new GlyphLayout(fontTitle, "GAME OVER");
        float titleX = (viewport.getWorldWidth() - titleLayout.width) / 2f;
        float titleY = viewport.getWorldHeight() * 0.75f;
        fontTitle.draw(batch, titleLayout, titleX, titleY);

        // Draw menu options
        for (int i = 0; i < menuItems.length; i++) {
            BitmapFont font = (i == selectedIndex) ? fontOptionBright : fontOptionDark;
            optionLayouts[i].setText(font, menuItems[i]);
            float x = (viewport.getWorldWidth() - optionLayouts[i].width) / 2f;
            float y = (viewport.getWorldHeight() / 2f) - (i * 60);
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
            float y = (viewport.getWorldHeight() / 2f) - (i * 60);
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
            // Retry
            Screen ss = new PantallaJuego(1, 3, 0, 10);
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
        background.dispose();
        fontTitle.dispose();
        fontOptionBright.dispose();
        fontOptionDark.dispose();
        pickingSound.dispose();
        enterSound.dispose();
    }
}
