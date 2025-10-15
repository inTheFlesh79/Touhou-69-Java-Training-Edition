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
 * PantallaSesiones - fixed:
 *  - highlight vertical offset adjusted (moved up a bit)
 *  - scrolling selection no longer drifts the whole table downward
 *  - rows won't draw under the bottom "Press ESC..." message
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
    private int selectedIndex = 0;
    private float scrollOffset = 0f;

    // Layout tuning (world coordinates)
    private final float rowHeight = 52f;          // distance between rows
    private final float topMargin = 160f;         // distance from top to header baseline
    private final float leftMargin = 100f;        // left indent of table
    private final float tableWidth = 1040f;       // width of table area
    private final float bottomMargin = 80f;       // reserved bottom area for "Press ESC..." etc

    // UI state
    private float blinkTimer = 0f;
    private boolean blinkVisible = true;

    // scroll handling
    private int scrollAccumulator = 0;
    private InputAdapter scrollListener;
    private com.badlogic.gdx.InputProcessor previousProcessor;

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

        // mouse wheel handling (compatible across libGDX versions)
        scrollListener = new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                int delta = (Math.abs(amountY) > 0.0001f) ? (int) -Math.signum(amountY) : (int) -Math.signum(amountX);
                scrollAccumulator += delta;
                return true;
            }

        };
    }

    @Override
    public void show() {
        // keep existing input processor to restore later
        previousProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(scrollListener);

        if (sessions == null) sessions = SessionDataManager.getInstance().getAllSavedSessions();
        if (sessions == null) sessions = java.util.Collections.emptyList();

        if (sessions.isEmpty()) selectedIndex = 0;
        else if (selectedIndex >= sessions.size()) selectedIndex = sessions.size() - 1;

        ensureSelectedVisible();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        handleInput(delta);

        // process wheel scroll events (each step moves selection)
        if (scrollAccumulator != 0) {
            int steps = scrollAccumulator;
            scrollAccumulator = 0;
            if (steps > 0) {
                for (int i = 0; i < steps; i++) {
                    if (selectedIndex > 0) { selectedIndex--; pickSound.play(0.7f); }
                }
            } else {
                for (int i = 0; i < -steps; i++) {
                    if (selectedIndex < sessions.size() - 1) { selectedIndex++; pickSound.play(0.7f); }
                }
            }
            ensureSelectedVisible();
        }

        // blink footer
        blinkTimer += delta;
        if (blinkTimer >= 0.5f) { blinkTimer = 0f; blinkVisible = !blinkVisible; }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float ww = viewport.getWorldWidth();
        float wh = viewport.getWorldHeight();

        // background
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(background, 0f, 0f, ww, wh);

        // TITLE
        GlyphLayout titleLayout = new GlyphLayout(titleFont, "Previous runs");
        titleFont.draw(batch, titleLayout, (ww - titleLayout.width) / 2f, wh - 80f);

        // COLUMNS
        float baseX = leftMargin;
        float headerY = wh - topMargin;
        float colNoX = baseX;
        float colPlayerX = baseX + 80f;
        float colDateX = baseX + 320f;
        float colScoreX = baseX + 680f;
        float colRoundsX = baseX + 900f;

        headerFont.draw(batch, "No.", colNoX, headerY);
        headerFont.draw(batch, "Player", colPlayerX, headerY);
        headerFont.draw(batch, "Date", colDateX, headerY);
        headerFont.draw(batch, "Score", colScoreX, headerY);
        headerFont.draw(batch, "TestRounds", colRoundsX, headerY);

        // divider under headers
        batch.setColor(1f, 1f, 1f, 0.25f);
        float dividerY = headerY - 10f;
        batch.draw(pixel, baseX - 20f, dividerY, tableWidth, 2f);
        batch.setColor(1f, 1f, 1f, 1f);

        // rows area
        float firstRowBaselineY = dividerY - 22f; // baseline Y of the first row (text baseline)
        float rowsTop = firstRowBaselineY;        // top-most baseline we draw
        float rowsBottom = bottomMargin + 30f;    // protect area near bottom (keep a gap for the footer)

        // compute visible rows count dynamically from available vertical space
        int visibleRowsCount = Math.max(1, (int) Math.floor((rowsTop - rowsBottom) / rowHeight));

        // draw rows
        for (int i = 0; i < sessions.size(); i++) {
            GameSessionData s = sessions.get(i);
            // baseline for this row
            float rowBaselineY = firstRowBaselineY - (i * rowHeight) - scrollOffset;

            // skip rows outside visible vertical window
            if (rowBaselineY < rowsBottom - rowHeight || rowBaselineY > rowsTop + 30f) continue;

            // highlight rectangle should sit slightly above the baseline (so it doesn't graze text top)
            // rectangle top Y is calculated relative to baseline so we tune it visually
            float highlightHeight = rowHeight - 10f;
            // move highlight up by 6 px to avoid grazing the top of the text (user requested)
            float highlightY = rowBaselineY - highlightHeight + 14f;

            if (i == selectedIndex) {
                batch.setColor(0.20f, 0.35f, 0.9f, 0.46f); // bluish translucent
                batch.draw(pixel, baseX - 24f, highlightY, tableWidth + 40f, highlightHeight);
                batch.setColor(1f, 1f, 1f, 1f);
            } else if (i % 2 == 0) {
                batch.setColor(0f, 0f, 0f, 0.15f);
                batch.draw(pixel, baseX - 24f, highlightY, tableWidth + 40f, highlightHeight);
                batch.setColor(1f, 1f, 1f, 1f);
            }

            // draw text using baseline at rowBaselineY (this matches previous render behaviour)
            String no = (s.getGameId() != null) ? String.valueOf(s.getGameId()) : String.valueOf(i + 1);
            rowFont.draw(batch, no, colNoX, rowBaselineY);

            String player = (s.getPlayerTag() != null) ? s.getPlayerTag() : "Unknown";
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

        // bottom "Press Esc..." blinking text (kept above bottomMargin)
        if (blinkVisible) {
            GlyphLayout bottom = new GlyphLayout(bottomFont, "Press ESC to return to Main Menu");
            bottomFont.draw(batch, bottom, 50f, bottomMargin - 20f);
        }

        batch.end();
    }

    private void handleInput(float delta) {
        // up / down single-step
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (selectedIndex > 0) { selectedIndex--; pickSound.play(0.7f); ensureSelectedVisible(); }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (selectedIndex < sessions.size() - 1) { selectedIndex++; pickSound.play(0.7f); ensureSelectedVisible(); }
        }

        // page up / page down
        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP)) {
            int jump = Math.max(1, getDynamicVisibleRows() - 1);
            selectedIndex = Math.max(0, selectedIndex - jump);
            pickSound.play(0.7f);
            ensureSelectedVisible();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN)) {
            int jump = Math.max(1, getDynamicVisibleRows() - 1);
            selectedIndex = Math.min(sessions.size() - 1, selectedIndex + jump);
            pickSound.play(0.7f);
            ensureSelectedVisible();
        }

        // mouse click -> select row
        if (Gdx.input.justTouched()) {
            com.badlogic.gdx.math.Vector3 m = new com.badlogic.gdx.math.Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(m);
            float wh = viewport.getWorldHeight();
            float headerY = wh - topMargin;
            float dividerY = headerY - 10f;
            float firstRowBaselineY = dividerY - 22f;

            float clickOffset = firstRowBaselineY - m.y + scrollOffset; // pixels from top to clicked baseline
            int clickedRow = (int) Math.floor(clickOffset / rowHeight);
            if (clickedRow >= 0 && clickedRow < sessions.size()) {
                if (clickedRow != selectedIndex) {
                    selectedIndex = clickedRow;
                    pickSound.play(0.7f);
                    ensureSelectedVisible();
                } else {
                    enterSound.play(0.7f);
                }
            }
        }

        // enter -> play sound (placeholder for future action)
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            enterSound.play(0.7f);
        }

        // escape -> return to previous screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            enterSound.play(0.7f);
            if (previousProcessor != null) Gdx.input.setInputProcessor(previousProcessor);
            game.setScreen(previousScreen);
            dispose();
        }
    }

    /**
     * keep selected row visible and clamp scrollOffset.
     * uses dynamic visible rows computed from viewport space and bottom margin.
     */
    private void ensureSelectedVisible() {
        float wh = viewport.getWorldHeight();
        float headerY = wh - topMargin;
        float dividerY = headerY - 10f;
        float firstRowBaselineY = dividerY - 22f;
        float rowsTop = firstRowBaselineY;
        float rowsBottom = bottomMargin + 30f;

        int visibleRowsCount = Math.max(1, (int) Math.floor((rowsTop - rowsBottom) / rowHeight));
        float minOffset = 0f;
        float maxOffset = Math.max(0f, sessions.size() * rowHeight - visibleRowsCount * rowHeight);

        float selectedTop = selectedIndex * rowHeight; // pixel position of selected row from top-of-list origin

        // If selected above view -> bring it to top
        if (selectedTop < scrollOffset) scrollOffset = selectedTop;

        // If selected below view -> bring it up so selected is last visible row
        float bottomVisible = scrollOffset + (visibleRowsCount - 1) * rowHeight;
        if (selectedTop > bottomVisible) scrollOffset = selectedTop - (visibleRowsCount - 1) * rowHeight;

        // clamp
        if (scrollOffset < minOffset) scrollOffset = minOffset;
        if (scrollOffset > maxOffset) scrollOffset = maxOffset;
    }

    private int getDynamicVisibleRows() {
        float wh = viewport.getWorldHeight();
        float headerY = wh - topMargin;
        float dividerY = headerY - 10f;
        float firstRowBaselineY = dividerY - 22f;
        float rowsTop = firstRowBaselineY;
        float rowsBottom = bottomMargin + 30f;
        return Math.max(1, (int) Math.floor((rowsTop - rowsBottom) / rowHeight));
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
