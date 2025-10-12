package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Managers.MusicManager;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * PantallaMenu - Main menu screen modeled after PantallaGameOver
 */
public class PantallaMenu implements Screen {
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
    private String[] menuItems = {"Play", "Tutorial", "Previous Runs", "Quit"};
    private int selectedIndex = 0;

    private GlyphLayout[] optionLayouts;

    // Timer vars (cooldown before executing)
    private boolean optionActivated = false;
    private float timer = 0f;
    private int pendingOption = -1; // which option to execute after timer

    private MusicManager musicMng;

    // Background fade variables
    private float bgAlpha = 0.5f;        // start at 0.5
    private float fadeTimer = 0f;
    private final float fadeDuration = 1.5f; // seconds to fade to 1.0f

    public PantallaMenu() {
        game = Touhou.getInstance();
        batch = game.getBatch();
        camera = Touhou.getInstance().getCamera();
        viewport = Touhou.getInstance().getViewport();
        this.musicMng = game.getMusicMng();
        musicMng.playMainMenu();

        background = new Texture(Gdx.files.internal("th69MainMenu.png"));
        pickingSound = Gdx.audio.newSound(Gdx.files.internal("pickOption.ogg"));
        enterSound = Gdx.audio.newSound(Gdx.files.internal("enterSound.ogg"));

        // Load fonts (same style as PantallaGameOver)
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
        parameter.size = 28;
        parameter.color = Color.WHITE;
        fontOptionBright = generator.generateFont(parameter);
        fontOptionBright.getData().setScale(2f);
        fontOptionBright.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Option dark
        parameter.size = 28;
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
    public void show() {
        // Initialize fade
        fadeTimer = 0f;
        bgAlpha = 0.5f;
        optionActivated = false;
        timer = 0f;
        pendingOption = -1;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.1f, 1);

        // Fade background
        if (fadeTimer < fadeDuration) {
            fadeTimer += delta;
            float t = fadeTimer / fadeDuration;
            if (t > 1f) t = 1f;
            bgAlpha = 0.25f + (1f - 0.25f) * t;
        } else {
            bgAlpha = 1f;
        }

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

        // Draw fading background
        batch.setColor(1f, 1f, 1f, bgAlpha);
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setColor(1f, 1f, 1f, 1f);

        // ----- MENU OPTIONS -----
        // Position anchored to lower-left
        float baseX = viewport.getWorldWidth() * 0.08f;   // ~8% from left edge
        float baseY = viewport.getWorldHeight() * 0.30f;  // start around lower third
        float spacing = 70f;                              // vertical distance between options

        for (int i = 0; i < menuItems.length; i++) {
            BitmapFont font = (i == selectedIndex) ? fontOptionBright : fontOptionDark;
            optionLayouts[i].setText(font, menuItems[i]);
            float x = baseX;
            float y = baseY + (menuItems.length - 1 - i) * spacing; // stack upward
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
            if (selectedIndex == 1 || selectedIndex == 2) {
                // Tutorial and Previous Runs — only play sound
                pickingSound.play(0.7f);
            } else {
                queueOption(selectedIndex);
            }
        }

        // Mouse hover & click
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);

        // Match render positions
        float baseX = viewport.getWorldWidth() * 0.08f;
        float baseY = viewport.getWorldHeight() * 0.30f;
        float spacing = 70f;

        for (int i = 0; i < menuItems.length; i++) {
            BitmapFont font = (i == selectedIndex) ? fontOptionBright : fontOptionDark;
            optionLayouts[i].setText(font, menuItems[i]);
            float x = baseX;
            float y = baseY + (menuItems.length - 1 - i) * spacing;
            float w = optionLayouts[i].width;
            float h = optionLayouts[i].height;

            // Adjust bounds — LibGDX text baseline is at y, so hover box should include y - h
            if (mouse.x >= x && mouse.x <= x + w && mouse.y >= y - h && mouse.y <= y) {
                if (selectedIndex != i) pickingSound.play(0.7f);
                selectedIndex = i;

                if (Gdx.input.justTouched()) {queueOption(i);}
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
    	Screen ss;
        switch (index) {
	            // Play -> create PantallaJuego and set screen (same params as your GameOver example)
        	case 0: ss = new PantallaJuego(1, 1, 0, 2000);
        			ss.resize(1280, 960);
	                game.setScreen(new PantallaHint(game, ss));
	                musicMng.stopMainMenu();
	                musicMng.resetMusicMng();
	                dispose();
	                break;
        	case 1:	ss = new PantallaTutorial();
        			ss.resize(1280, 960);
        			game.setScreen(ss);
        			musicMng.stopMainMenu();
        			musicMng.resetMusicMng();
        			dispose();
        			break;
        	case 2: // Previous runs
        		
        	case 3: musicMng.stopMainMenu();
        			Gdx.app.exit();
        			break;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

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
