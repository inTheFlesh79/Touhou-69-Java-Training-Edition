package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Managers.MusicManager;
import Sessions.SessionDataManager;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class PantallaMenu implements Screen {
    private Touhou game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture background;
    private Sound pickingSound;
    private Sound enterSound;

    // Menu Fonts (existing)
    private BitmapFont fontTitle;
    private BitmapFont fontOptionBright;
    private BitmapFont fontOptionDark;

    // Overlay-specific Fonts (NEW: smaller so overlay doesn't shrink menu visuals)
    private BitmapFont overlayTitleFont;
    private BitmapFont overlayInputFont;
    private BitmapFont overlayInstrFont;

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

    // Username prompt overlay state
    private boolean askingForName = false;
    private StringBuilder nameBuffer = new StringBuilder();
    private float caretTimer = 0f;
    private boolean caretVisible = true;
    private final int MAX_NAME_LENGTH = 20;
    private int optionToProceed = -1; // 0=Play,1=Tutorial

    // 1x1 white pixel texture used to draw rectangles
    private Texture pixel;

    // Backspace hold behaviour
    private boolean backspaceHeld = false;
    private float backspaceHeldTime = 0f;
    private float lastBackspaceRepeatTime = 0f;
    private final float BACKSPACE_INITIAL_DELAY = 0.5f; // seconds before repeat starts
    private final float BACKSPACE_REPEAT_INTERVAL = 0.06f; // seconds per repeat

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

        // Load fonts (same style as PantallaGameOver) - menu fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("thFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderColor = Color.RED;
        parameter.borderWidth = 1;

        // Title font
        parameter.size = 36;
        parameter.color = Color.WHITE;
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

        // --- Create overlay-specific fonts (smaller, no scaling) ---
        FreeTypeFontGenerator.FreeTypeFontParameter overlayParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        overlayParam.borderColor = Color.RED;
        overlayParam.borderWidth = 1;

        // Overlay title (smaller than main title)
        overlayParam.size = 20; // smaller size
        overlayParam.color = Color.WHITE;
        overlayTitleFont = generator.generateFont(overlayParam);
        overlayTitleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Overlay input (slightly larger for readability)
        overlayParam.size = 18;
        overlayParam.color = Color.WHITE;
        overlayInputFont = generator.generateFont(overlayParam);
        overlayInputFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        
        // Overlay instruction font (small)
        overlayParam.size = 14;
        overlayParam.color = new Color(0.85f, 0.85f, 0.85f, 1f); // slightly off-white
        overlayInstrFont = generator.generateFont(overlayParam);
        overlayInstrFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


        generator.dispose();

        optionLayouts = new GlyphLayout[menuItems.length];
        for (int i = 0; i < menuItems.length; i++) {
            optionLayouts[i] = new GlyphLayout(fontOptionBright, menuItems[i]);
        }

        // create a 1x1 white pixel texture for drawing rectangles
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, 1f);
        pm.fill();
        pixel = new Texture(pm);
        pm.dispose();
    }

    @Override
    public void show() {
        // Initialize fade
        fadeTimer = 0f;
        bgAlpha = 0.5f;
        optionActivated = false;
        timer = 0f;
        pendingOption = -1;
        askingForName = false;
        nameBuffer.setLength(0);
        caretTimer = 0f;
        caretVisible = true;
        backspaceHeld = false;
        backspaceHeldTime = 0f;
        lastBackspaceRepeatTime = 0f;
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

        if (!optionActivated && !askingForName) {
            handleInput();
        } else if (optionActivated && !askingForName) {
            timer += delta;
            if (timer >= 1f && pendingOption != -1) {
                // Instead of executing immediately we now ask for name before set screen if option is Play/Tutorial
                if (pendingOption == 0 || pendingOption == 1) {
                    // Begin asking for player name
                    askingForName = true;
                    optionToProceed = pendingOption; // store which option to continue to
                    nameBuffer.setLength(0);
                    optionActivated = false; // stop default execute
                } else {
                    executeOption(pendingOption);
                }
                pendingOption = -1;
            }
        } else if (askingForName) {
            // update caret blinking
            caretTimer += delta;
            if (caretTimer >= 0.5f) {
                caretTimer = 0f;
                caretVisible = !caretVisible;
            }
            // capture typed keys for name, includes backspace hold handling
            handleNameInput(delta);
        }

        batch.begin();

        // Draw fading background
        batch.setColor(1f, 1f, 1f, bgAlpha);
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setColor(1f, 1f, 1f, 1f);

        // ----- MENU OPTIONS -----
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

        // If asking for name, draw overlay + input box
        if (askingForName) {
            drawNameOverlay();
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
        String playerName = nameBuffer.toString().trim();
        switch (index) {
            case 0: // Play -> go to hint -> game
                ss = new PantallaJuego(1, 3, 0, 10);
                ss.resize(1280, 960);
                playerName = nameBuffer.toString().trim();
                if (playerName.isEmpty()) playerName = "Player"; // fallback
                // start provisional session with playerName
                SessionDataManager.getInstance().startRecordingSession(playerName);
                game.setScreen(new PantallaHint(game, ss));
                musicMng.stopMainMenu();
                musicMng.resetMusicMng();
                dispose();
                break;
            case 1: // Tutorial
                ss = new PantallaTutorial();
                ss.resize(1280, 960);
                playerName = nameBuffer.toString().trim();
                if (playerName.isEmpty()) playerName = "Player"; // fallback
                // start provisional session with playerName
                SessionDataManager.getInstance().startRecordingSession(playerName);
                game.setScreen(ss);
                musicMng.stopMainMenu();
                musicMng.resetMusicMng();
                dispose();
                break;
            case 2: // Previous runs
            	ss = new PantallaSesiones(this);
            	ss.resize(1280, 960);
            	game.setScreen(ss);
                break;
            case 3:
                musicMng.stopMainMenu();
                Gdx.app.exit();
                break;
        }
    }

    /**
     * Draw the dark translucent overlay + input box centered on the screen using pixel texture.
     * Uses overlay-specific fonts so menu fonts keep their visual sizes.
     */
    private void drawNameOverlay() {
        float ww = viewport.getWorldWidth();
        float wh = viewport.getWorldHeight();

        // Dark translucent overlay (simulate blur attention)
        batch.setColor(0f, 0f, 0f, 0.55f); // 55% opaque black
        batch.draw(pixel, 0, 0, ww, wh);
        batch.setColor(1f, 1f, 1f, 1f);

        // Input box rectangle: centered (slightly translucent black)
        float boxW = ww * 0.5f;
        float boxH = 140f;
        float boxX = (ww - boxW) / 2f;
        float boxY = (wh - boxH) / 2f;

        // draw box background (more opaque)
        batch.setColor(0f, 0f, 0f, 0.58f); // box background
        batch.draw(pixel, boxX, boxY, boxW, boxH);
        batch.setColor(1f, 1f, 1f, 1f);

        // Title text: "Enter your name" using overlayTitleFont
        String title = "Enter your name";
        GlyphLayout titleLayout = new GlyphLayout(overlayTitleFont, title);
        float titleX = boxX + (boxW - titleLayout.width) / 2f;
        float titleY = boxY + boxH - 18f; // small padding from top
        overlayTitleFont.draw(batch, titleLayout, titleX, titleY);

        // Input text and caret using overlayInputFont
        String nameDisplay = nameBuffer.length() == 0 ? "(type name)" : nameBuffer.toString();
        String displayWithCaret = nameDisplay + (caretVisible ? "_" : "");
        GlyphLayout inputLayout = new GlyphLayout(overlayInputFont, displayWithCaret);
        float inputX = boxX + 20f;
        // vertically center input text a bit below the title
        float inputY = boxY + boxH * 0.55f + inputLayout.height / 2f - 4f;
        overlayInputFont.draw(batch, inputLayout, inputX, inputY);

        // Instruction line using overlayInstrFont (smaller)
        String instr = "Press Enter to confirm, Esc to cancel";
        GlyphLayout instrLayout = new GlyphLayout(overlayInstrFont, instr);
        float instrX = boxX + (boxW - instrLayout.width) / 2f;
        float instrY = boxY + 10f + instrLayout.height; // small padding from bottom
        overlayInstrFont.draw(batch, instrLayout, instrX, instrY);
    }

    /**
     * Handle keyboard input for the name box (only while askingForName is true).
     * Adds backspace-hold support by tracking time; delta supplied by render loop.
     */
    private void handleNameInput(float delta) {
        // Backspace pressed logic with hold-repeat
        boolean bsPressed = Gdx.input.isKeyPressed(Input.Keys.BACKSPACE) || Gdx.input.isKeyPressed(Input.Keys.DEL);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isKeyJustPressed(Input.Keys.DEL)) {
            // immediate single deletion on key down
            if (nameBuffer.length() > 0) {
                nameBuffer.deleteCharAt(nameBuffer.length() - 1);
            }
            // start hold timers
            backspaceHeld = true;
            backspaceHeldTime = 0f;
            lastBackspaceRepeatTime = 0f;
        } else if (bsPressed) {
            // key is held down across frames
            backspaceHeldTime += delta;
            if (backspaceHeldTime >= BACKSPACE_INITIAL_DELAY) {
                // time to start repeating deletes
                lastBackspaceRepeatTime += delta;
                if (lastBackspaceRepeatTime >= BACKSPACE_REPEAT_INTERVAL) {
                    // delete one char and reset repeat timer
                    if (nameBuffer.length() > 0) {
                        nameBuffer.deleteCharAt(nameBuffer.length() - 1);
                    }
                    lastBackspaceRepeatTime = 0f;
                }
            }
        } else {
            // backspace not pressed
            backspaceHeld = false;
            backspaceHeldTime = 0f;
            lastBackspaceRepeatTime = 0f;
        }

        // Cancel
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            askingForName = false;
            nameBuffer.setLength(0);
            return;
        }

        // Enter => confirm
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

            executeOption(optionToProceed);
            return;
        }

        // Add printable characters (letters, numbers, punctuation) — only on keyJustPressed to avoid repeats handled above
        // Letters
        for (int k = Input.Keys.A; k <= Input.Keys.Z; k++) {
            if (Gdx.input.isKeyJustPressed(k) && nameBuffer.length() < MAX_NAME_LENGTH) {
                char c = (char) ('a' + (k - Input.Keys.A));
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                    c = Character.toUpperCase(c);
                }
                nameBuffer.append(c);
                return;
            }
        }
        // numbers (top row)
        for (int k = Input.Keys.NUM_0; k <= Input.Keys.NUM_9; k++) {
            if (Gdx.input.isKeyJustPressed(k) && nameBuffer.length() < MAX_NAME_LENGTH) {
                char c = (char) ('0' + (k - Input.Keys.NUM_0));
                nameBuffer.append(c);
                return;
            }
        }
        // numpad numbers
        for (int k = Input.Keys.NUMPAD_0; k <= Input.Keys.NUMPAD_9; k++) {
            if (Gdx.input.isKeyJustPressed(k) && nameBuffer.length() < MAX_NAME_LENGTH) {
                char c = (char) ('0' + (k - Input.Keys.NUMPAD_0));
                nameBuffer.append(c);
                return;
            }
        }
        // space
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && nameBuffer.length() < MAX_NAME_LENGTH) {
            nameBuffer.append(' ');
            return;
        }
        // punctuation: minus and period
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS) && nameBuffer.length() < MAX_NAME_LENGTH) {
            nameBuffer.append('-');
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD) && nameBuffer.length() < MAX_NAME_LENGTH) {
            nameBuffer.append('.');
            return;
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
        overlayTitleFont.dispose();
        overlayInputFont.dispose();
        overlayInstrFont.dispose(); 
        pixel.dispose();
        pickingSound.dispose();
        enterSound.dispose();
    }
}