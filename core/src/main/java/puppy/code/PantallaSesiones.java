package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.List;

import Sessions.GameSessionData;
import Sessions.SessionDataManager;

/**
 * PantallaSesiones - fixed to show exactly 13 visible rows and proper scrolling
 */
public class PantallaSesiones implements Screen {

    private Touhou game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture background;
    private Texture pixel;
    private Screen previousScreen;

    // Fonts
    private BitmapFont titleFont, headerFont, rowFont, bottomFont;
    // Sounds
    private Sound pickSound, enterSound;

    // Data
    private List<GameSessionData> sessions;
    private int selectedIndex = 0;   // absolute index into sessions
    private int startIndex = 0;      // first index currently visible
    private final int VISIBLE_ROWS = 13;

    // Layout tuning (world coordinates)
    private final float rowHeight = 52f;          // baseline-to-baseline spacing (used for vertical spacing)
    private final float topMargin = 160f;         // distance from top to title/header area
    private final float leftMargin = 100f;        // left indent of table
    private final float tableWidth = 1040f;       // width of table area
    private final float bottomMargin = 80f;       // space reserved for bottom text

    // UI state
    private float blinkTimer = 0f;
    private boolean blinkVisible = true;

    // scroll handling (mouse wheel)
    private int scrollAccumulator = 0;
    private InputAdapter scrollListener;
    private com.badlogic.gdx.InputProcessor previousProcessor;

    // temp layout used for measurement
    private final GlyphLayout tmpLayout = new GlyphLayout();

    public PantallaSesiones(Screen previousScreen) {
        this.game = Touhou.getInstance();
        this.batch = game.getBatch();
        this.camera = (OrthographicCamera) game.getCamera();
        this.viewport = (FitViewport) game.getViewport();
        this.previousScreen = previousScreen;

        // assets
        background = new Texture(Gdx.files.internal("previousRunsBG.png"));
        pickSound = Gdx.audio.newSound(Gdx.files.internal("pickOption.ogg"));
        enterSound = Gdx.audio.newSound(Gdx.files.internal("enterSound.ogg"));

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        pixel = new Texture(pm);
        pm.dispose();

        // fonts (same style you use)
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("thFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();

        p.size = 36; p.color = Color.WHITE; p.borderColor = Color.BLUE; p.borderWidth = 1;
        titleFont = gen.generateFont(p); titleFont.getData().setScale(2f);

        p.size = 20; p.color = Color.WHITE; p.borderColor = Color.BLUE; p.borderWidth = 1;
        headerFont = gen.generateFont(p); headerFont.getData().setScale(1.5f);

        p.size = 18; p.color = Color.WHITE; p.borderColor = Color.BLACK; p.borderWidth = 1;
        rowFont = gen.generateFont(p); rowFont.getData().setScale(1.3f);

        p.size = 16; p.color = Color.WHITE; p.borderColor = Color.BLACK; p.borderWidth = 1;
        bottomFont = gen.generateFont(p); bottomFont.getData().setScale(1.2f);

        gen.dispose();

        sessions = SessionDataManager.getInstance().getAllSavedSessions();
        if (sessions == null) sessions = java.util.Collections.emptyList();

        // input adapter for mouse wheel (both signatures)
        scrollListener = new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                int delta = Math.abs(amountY) > 1e-4f ? (int) -Math.signum(amountY) : (int) -Math.signum(amountX);
                scrollAccumulator += delta;
                return true;
            }

        };
    }

    @Override
    public void show() {
        previousProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(scrollListener);

        sessions = SessionDataManager.getInstance().getAllSavedSessions();
        if (sessions == null) sessions = java.util.Collections.emptyList();

        // clamp indices
        if (sessions.isEmpty()) {
            selectedIndex = 0;
            startIndex = 0;
        } else {
            selectedIndex = Math.min(selectedIndex, sessions.size() - 1);
            startIndex = Math.min(startIndex, Math.max(0, sessions.size() - VISIBLE_ROWS));
            // ensure selected visible
            ensureSelectedVisible();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        handleInput(delta);

        // process mouse wheel accumulated steps (each step moves selection)
        if (scrollAccumulator != 0) {
            int steps = scrollAccumulator;
            scrollAccumulator = 0;
            if (steps > 0) {
                for (int i = 0; i < steps; i++) {
                    if (selectedIndex > 0) {
                        selectedIndex--;
                        pickSound.play(0.7f);
                    }
                }
            } else {
                for (int i = 0; i < -steps; i++) {
                    if (selectedIndex < sessions.size() - 1) {
                        selectedIndex++;
                        pickSound.play(0.7f);
                    }
                }
            }
            ensureSelectedVisible();
        }

        // blinking footer
        blinkTimer += delta;
        if (blinkTimer >= 0.5f) { blinkTimer = 0f; blinkVisible = !blinkVisible; }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float ww = viewport.getWorldWidth();
        float wh = viewport.getWorldHeight();

        // background
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(background, 0f, 0f, ww, wh);

        // title
        GlyphLayout titleLayout = new GlyphLayout(titleFont, "Previous runs");
        float titleX = (ww - titleLayout.width) / 2f;
        float titleY = wh - 80f;
        titleFont.draw(batch, titleLayout, titleX, titleY);

        // columns positions
        float baseX = leftMargin;
        float headerY = wh - topMargin;
        float colNoX = baseX;
        float colPlayerX = baseX + 80f;
        float colDateX = baseX + 320f;
        float colScoreX = baseX + 680f;
        float colRoundsX = baseX + 900f;

        // calculate header height in world units to place divider below header text
        GlyphLayout ghNo = new GlyphLayout(headerFont, "No.");
        GlyphLayout ghPlayer = new GlyphLayout(headerFont, "Player");
        GlyphLayout ghDate = new GlyphLayout(headerFont, "Date");
        GlyphLayout ghScore = new GlyphLayout(headerFont, "Score");
        GlyphLayout ghTR = new GlyphLayout(headerFont, "TestRounds");
        float headerHeight = Math.max(Math.max(ghNo.height, ghPlayer.height), Math.max(ghDate.height, Math.max(ghScore.height, ghTR.height)));

        // draw header text
        headerFont.draw(batch, "No.", colNoX, headerY);
        headerFont.draw(batch, "Player", colPlayerX, headerY);
        headerFont.draw(batch, "Date", colDateX, headerY);
        headerFont.draw(batch, "Score", colScoreX, headerY);
        headerFont.draw(batch, "TestRounds", colRoundsX, headerY);

        // divider just below header glyphs
        float dividerGap = 8f;
        float dividerY = headerY - headerHeight - dividerGap;
        batch.setColor(1f, 1f, 1f, 0.25f);
        batch.draw(pixel, baseX - 20f, dividerY, tableWidth, 2f);
        batch.setColor(1f, 1f, 1f, 1f);

        // compute first row baseline (text baseline) below divider
        float baselineGap = 18f;
        float firstRowBaselineY = dividerY - baselineGap;

        // visible rows count is fixed to VISIBLE_ROWS but may be less if fewer sessions
        int visible = Math.min(VISIBLE_ROWS, Math.max(0, sessions.size()));

        // Draw rows from startIndex .. startIndex + visible - 1
        for (int i = 0; i < visible; i++) {
            int idx = startIndex + i;
            if (idx < 0 || idx >= sessions.size()) continue;
            GameSessionData s = sessions.get(idx);

            float rowBaselineY = firstRowBaselineY - (i * rowHeight); // i is row slot (0..visible-1)

            // measure row text height (player column is representative)
            String playerText = s.getPlayerTag() != null ? s.getPlayerTag() : "Unknown";
            tmpLayout.setText(rowFont, playerText);
            float textHeight = tmpLayout.height;

            // highlight band dimensions computed from text height (keeps band tight to text)
            float highlightH = textHeight + 12f;
            float highlightTopY = rowBaselineY + (textHeight * 0.2f); // small offset so band sits behind glyphs visually
            float highlightY = highlightTopY - highlightH; // bottom-left y for draw(pixel,...)

            // clamp highlight to stay below divider and above bottom area
            float minHighlightY = dividerY - 6f - highlightH;       // ensure not overlapping divider/header
            float maxHighlightY = bottomMargin + 30f;               // ensure not overlapping footer area
            if (highlightY > minHighlightY) highlightY = minHighlightY;
            if (highlightY < maxHighlightY - highlightH) highlightY = maxHighlightY - highlightH;

            if (idx == selectedIndex) {
                batch.setColor(0.20f, 0.35f, 0.9f, 0.46f);
                batch.draw(pixel, baseX - 24f, highlightY, tableWidth + 40f, highlightH);
                batch.setColor(1f, 1f, 1f, 1f);
            } else if (i % 2 == 0) {
                batch.setColor(0f, 0f, 0f, 0.15f);
                batch.draw(pixel, baseX - 24f, highlightY, tableWidth + 40f, highlightH);
                batch.setColor(1f, 1f, 1f, 1f);
            }

            // draw columns using baseline
            String no = s.getGameId() != null ? String.valueOf(s.getGameId()) : String.valueOf(idx + 1);
            rowFont.draw(batch, no, colNoX, rowBaselineY);

            String player = s.getPlayerTag() != null ? s.getPlayerTag() : "Unknown";
            rowFont.draw(batch, player, colPlayerX, rowBaselineY);

            String date = (s.getDate() != null ? s.getDate() : "") + (s.getTime() != null ? " " + s.getTime() : "");
            rowFont.draw(batch, date, colDateX, rowBaselineY);

            rowFont.draw(batch, String.valueOf(s.getScore()), colScoreX, rowBaselineY);

            int roundsCount = 0;
            try { roundsCount = s.getTestRoundsCount(); }
            catch (Throwable t1) {
                try {
                    Object r = s.getClass().getMethod("getRounds").invoke(s);
                    if (r instanceof java.util.List) roundsCount = ((java.util.List<?>) r).size();
                } catch (Throwable ignored) {}
            }
            rowFont.draw(batch, String.valueOf(roundsCount), colRoundsX, rowBaselineY);
        }

        // bottom blinking text
        if (blinkVisible) {
            GlyphLayout bottom = new GlyphLayout(bottomFont, "Press ESC to return to Main Menu");
            bottomFont.draw(batch, bottom, 50f, bottomMargin - 20f);
        }

        batch.end();
    }

    private void handleInput(float delta) {
        // keys up / down
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (selectedIndex > 0) {
                selectedIndex--;
                pickSound.play(0.7f);
                // if moved above visible window, shift window up
                if (selectedIndex < startIndex) startIndex = selectedIndex;
                clampStartIndex();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (selectedIndex < sessions.size() - 1) {
                selectedIndex++;
                pickSound.play(0.7f);
                // if moved below visible window, shift window down so selected is bottom-most
                if (selectedIndex >= startIndex + VISIBLE_ROWS) {
                    startIndex = selectedIndex - (VISIBLE_ROWS - 1);
                }
                clampStartIndex();
            }
        }

        // page up / page down (jump by visible rows)
        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP)) {
            selectedIndex = Math.max(0, selectedIndex - VISIBLE_ROWS);
            pickSound.play(0.7f);
            if (selectedIndex < startIndex) startIndex = selectedIndex;
            clampStartIndex();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN)) {
            selectedIndex = Math.min(sessions.size() - 1, selectedIndex + VISIBLE_ROWS);
            pickSound.play(0.7f);
            if (selectedIndex >= startIndex + VISIBLE_ROWS) startIndex = selectedIndex - (VISIBLE_ROWS - 1);
            clampStartIndex();
        }

        // mouse click -> compute clicked slot and map to absolute index
        if (Gdx.input.justTouched()) {
            com.badlogic.gdx.math.Vector3 m = new com.badlogic.gdx.math.Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(m);
            float wh = viewport.getWorldHeight();

            // recompute header/divider/baseline exactly like render
            GlyphLayout ghNo = new GlyphLayout(headerFont, "No.");
            GlyphLayout ghPlayer = new GlyphLayout(headerFont, "Player");
            GlyphLayout ghDate = new GlyphLayout(headerFont, "Date");
            GlyphLayout ghScore = new GlyphLayout(headerFont, "Score");
            GlyphLayout ghTR = new GlyphLayout(headerFont, "TestRounds");
            float headerHeight = Math.max(Math.max(ghNo.height, ghPlayer.height), Math.max(ghDate.height, Math.max(ghScore.height, ghTR.height)));
            float headerY = wh - topMargin;
            float dividerGap = 8f;
            float dividerY = headerY - headerHeight - dividerGap;
            float baselineGap = 18f;
            float firstRowBaselineY = dividerY - baselineGap;

            // find slot clicked (0..VISIBLE_ROWS-1)
            float relative = firstRowBaselineY - m.y;
            int slot = (int) Math.floor(relative / rowHeight);
            int clickedIndex = startIndex + slot;
            if (slot >= 0 && slot < VISIBLE_ROWS && clickedIndex >= 0 && clickedIndex < sessions.size()) {
                if (clickedIndex != selectedIndex) {
                    selectedIndex = clickedIndex;
                    pickSound.play(0.7f);
                    // adjust window if needed
                    if (selectedIndex < startIndex) startIndex = selectedIndex;
                    if (selectedIndex >= startIndex + VISIBLE_ROWS) startIndex = selectedIndex - (VISIBLE_ROWS - 1);
                    clampStartIndex();
                } else {
                    enterSound.play(0.7f);
                }
            }
        }

        // Enter placeholder
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            enterSound.play(0.7f);
        }

        // Escape -> back
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            enterSound.play(0.7f);
            if (previousProcessor != null) Gdx.input.setInputProcessor(previousProcessor);
            game.setScreen(previousScreen);
            dispose();
        }
    }

    // clamp startIndex so it's within bounds and leaves exactly VISIBLE_ROWS (when possible)
    private void clampStartIndex() {
        int maxStart = Math.max(0, sessions.size() - VISIBLE_ROWS);
        if (startIndex < 0) startIndex = 0;
        if (startIndex > maxStart) startIndex = maxStart;

        // also ensure selected is visible (redundant but safe)
        if (selectedIndex < startIndex) startIndex = selectedIndex;
        if (selectedIndex >= startIndex + VISIBLE_ROWS) startIndex = selectedIndex - (VISIBLE_ROWS - 1);

        if (startIndex < 0) startIndex = 0;
        if (startIndex > maxStart) startIndex = maxStart;
    }

    private void ensureSelectedVisible() {
        if (selectedIndex < startIndex) startIndex = selectedIndex;
        if (selectedIndex >= startIndex + VISIBLE_ROWS) startIndex = selectedIndex - (VISIBLE_ROWS - 1);
        clampStartIndex();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        if (previousProcessor != null) Gdx.input.setInputProcessor(previousProcessor);
    }

    @Override
    public void dispose() {
        if (previousProcessor != null) Gdx.input.setInputProcessor(previousProcessor);
        background.dispose();
        pixel.dispose();
        titleFont.dispose();
        headerFont.dispose();
        rowFont.dispose();
        bottomFont.dispose();
        pickSound.dispose();
        enterSound.dispose();
    }
}
