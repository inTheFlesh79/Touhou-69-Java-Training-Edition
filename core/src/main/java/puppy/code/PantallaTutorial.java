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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class PantallaTutorial implements Screen {
    private Touhou game;
    private FitViewport viewport;
    private SpriteBatch batch;

    private Texture[] backgrounds;
    private int currentIndex = 0; // 0..3 for tutorialBg1..tutorialBg4

    private Sound enterSound;

    private BitmapFont hintFont;
    private GlyphLayout hintLayout;

    // fading state for "Press ANY key to continue..."
    private float hintAlpha = 0f;            // current alpha (0..1)
    private float hintFadeTime = 0f;        // accumulator for time-based fade
    private final float hintFadePeriod = 2f; // period in seconds for a full in-out cycle

    // transition to game
    private boolean transitionQueued = false;
    private float transitionTimer = 0f;     // counts up to transitionDelay
    private final float transitionDelay = 2f; // 2 seconds delay before switching to PantallaJuego

    public PantallaTutorial() {
        game = Touhou.getInstance();
        batch = game.getBatch();
        viewport = game.getViewport();

        // load the four tutorial backgrounds (hardcoded filenames as requested)
        backgrounds = new Texture[4];
        backgrounds[0] = new Texture(Gdx.files.internal("tutorialBg1.png"));
        backgrounds[1] = new Texture(Gdx.files.internal("tutorialBg2.png"));
        backgrounds[2] = new Texture(Gdx.files.internal("tutorialBg3.png"));
        backgrounds[3] = new Texture(Gdx.files.internal("tutorialBg4.png"));

        enterSound = Gdx.audio.newSound(Gdx.files.internal("enterSound.ogg"));

        // Create hint font mimicking PantallaGameOver style
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("thFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;

        parameter.size = 14; // base size; we'll scale to make it readable on most viewports
        parameter.color = Color.WHITE;
        hintFont = generator.generateFont(parameter);
        hintFont.getData().setScale(2f); // similar scaling used in PantallaGameOver
        hintFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        generator.dispose();

        hintLayout = new GlyphLayout(hintFont, "Press ANY key to continue...");
    }

    @Override
    public void render(float delta) {
        // update fading time (continuous regardless of transition state)
        hintFadeTime += delta;
        float t = (hintFadeTime % hintFadePeriod) / hintFadePeriod; // 0..1 over period
        // triangle wave 0..1..0: 1 - |2t-1|
        hintAlpha = 1f - Math.abs(2f * t - 1f);

        // if transition is queued, advance timer and after delay perform screen switch
        if (transitionQueued) {
            transitionTimer += delta;
            if (transitionTimer >= transitionDelay) {
                Screen ss = new PantallaJuego(1, 3, 0, 10);
                Screen juego = new PantallaHint(game, ss);
                juego.resize(1280, 960);
                game.setScreen(juego);
                dispose();
                return; // avoid drawing after changing screen
            }
        } else {
            // handle input (any key or touch) only when not already queued
            if (anyKeyJustPressed() || Gdx.input.justTouched()) {
                enterSound.play(0.7f);
                // advance image or queue transition if last
                if (currentIndex < backgrounds.length - 1) {
                    currentIndex++;
                } else {
                    // queue transition to PantallaJuego with a small delay
                    transitionQueued = true;
                    transitionTimer = 0f;
                }
            }
        }

        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();
        batch.setProjectionMatrix(game.getCamera().combined);

        batch.begin();
        // draw current background scaled to world size
        Texture bg = backgrounds[currentIndex];
        batch.draw(bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // draw fading hint in bottom-right
        hintLayout.setText(hintFont, "Press ANY key to continue...");
        float x = viewport.getWorldWidth() - hintLayout.width - 20f;
        float y = 20f + hintLayout.height; // 20 px margin from bottom

        // set font color with current alpha (use new Color to avoid mutating shared color state unexpectedly)
        hintFont.setColor(1f, 1f, 1f, hintAlpha);
        hintFont.draw(batch, hintLayout, x, y);

        batch.end();
    }

    // helper: check a reasonable range of keycodes for just-pressed keys
    // LibGDX doesn't provide a single "ANY_KEY" constant, so we poll a range.
    private boolean anyKeyJustPressed() {
        // check keyboard keys in a reasonable range
        for (int k = 0; k < 256; k++) {
            if (Gdx.input.isKeyJustPressed(k)) return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        for (Texture t : backgrounds) {
            if (t != null) t.dispose();
        }
        if (hintFont != null) hintFont.dispose();
        if (enterSound != null) enterSound.dispose();
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
