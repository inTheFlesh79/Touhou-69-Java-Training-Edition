package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import Sessions.SessionDataManager;

public class HUD {
    private final ShapeRenderer shapeRenderer;
    private final Touhou game;
    private final SpriteBatch gameBatch;
    private final Viewport viewport;

    // visual assets
    private final Texture hudBackground;
    private final Texture logo;

    // fonts
    private final BitmapFont font;
    private final BitmapFont difficultyFontNormal;
    private final BitmapFont difficultyFontHard;

    // difficulty flag (determines what text is shown)
    private boolean hardMode = true; // default is Hard mode

    public HUD(SpriteBatch gameBatch, Viewport viewport) {
        this.shapeRenderer = new ShapeRenderer();
        this.game = Touhou.getInstance();
        this.gameBatch = gameBatch;
        this.viewport = viewport;

        // Load background and logo
        hudBackground = new Texture(Gdx.files.internal("hudBackground.jpg"));
        logo = new Texture(Gdx.files.internal("logo.png"));

        // Create base font (white with black outline)
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("thFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 11;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 0.5f;

        font = generator.generateFont(parameter);

        // Difficulty fonts
        FreeTypeFontGenerator.FreeTypeFontParameter diffNormal = new FreeTypeFontGenerator.FreeTypeFontParameter();
        diffNormal.size = 11;
        diffNormal.color = Color.WHITE;
        diffNormal.borderColor = Color.BLUE;     // Normal outlined in blue
        diffNormal.borderWidth = 1f;
        difficultyFontNormal = generator.generateFont(diffNormal);

        FreeTypeFontGenerator.FreeTypeFontParameter diffHard = new FreeTypeFontGenerator.FreeTypeFontParameter();
        diffHard.size = 11;
        diffHard.color = Color.WHITE;
        diffHard.borderColor = Color.MAGENTA;    // Hard outlined in magenta
        diffHard.borderWidth = 1f;
        difficultyFontHard = generator.generateFont(diffHard);

        generator.dispose();

        // Scale fonts for pixel-art appearance
        font.getData().setScale(2f);
        difficultyFontNormal.getData().setScale(2f);
        difficultyFontHard.getData().setScale(2f);
    }

    public void drawHUD(int lives, int level, int score, int damage) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float hudWidth = 360f;
        float hudHeight = worldHeight;
        float hudX = worldWidth - hudWidth;
        float hudY = 0;

        // draw background image
        gameBatch.setProjectionMatrix(viewport.getCamera().combined);
        gameBatch.begin();
        gameBatch.draw(hudBackground, hudX, hudY, hudWidth, hudHeight);
        gameBatch.end();

        hudElements(lives, level, score, damage, hudX, hudWidth, hudHeight);
    }

    private void hudElements(int lives, int level, int score, int damage,
                             float hudX, float hudWidth, float hudHeight) {

        gameBatch.setProjectionMatrix(viewport.getCamera().combined);
        gameBatch.begin();

        float textX = hudX + 40;
        float y = hudHeight - 60;

        font.draw(gameBatch, "HiScore: " + SessionDataManager.getInstance().getHighestScore(), textX, y);
        y -= 35;
        font.draw(gameBatch, "Score:    " + score, textX, y);
        y -= 70;
        font.draw(gameBatch, "Lives: " + lives, textX, y);
        y -= 35;
        font.draw(gameBatch, "Power: " + damage, textX, y);
        y -= 70;
        font.draw(gameBatch, "Level: " + level, textX, y);
        y -= 35;

        // Draw "Difficulty: " with main font
        String diffLabel = "Boss Difficulty: ";
        font.draw(gameBatch, diffLabel, textX, y);

        // Draw actual difficulty ("Normal" or "Hard") with the correct font
        drawDifficultyText(diffLabel, textX, y);

        y -= 70;
        font.draw(gameBatch, "Test Rounds: " + Touhou.getRondasCompletadas(), textX, y);

        // Draw logo
        float logoY = y - 580;

        float aspect = 1280f / 720f;
        float targetHeight = hudHeight * 0.55f;
        float logoHeight = targetHeight;
        float logoWidth = logoHeight / aspect;

        if (logoWidth > hudWidth * 0.9f) {
            logoWidth = hudWidth * 0.9f;
            logoHeight = logoWidth * aspect;
        }

        float logoX = hudX + (hudWidth - logoWidth) / 2f;
        if (logoY < 0) logoY = 0;

        gameBatch.draw(logo, logoX, logoY, logoWidth, logoHeight);

        gameBatch.end();
    }

    /**
     * Draws the difficulty label next to "Difficulty: " text,
     * using the appropriate outline color and font.
     */
    private void drawDifficultyText(String label, float textX, float y) {
        GlyphLayout layout = new GlyphLayout(font, label);
        float labelWidth = layout.width;
        float difficultyX = textX + labelWidth + 8;

        String difficultyText = hardMode ? "Hard" : "Normal";
        BitmapFont diffFont = hardMode ? difficultyFontHard : difficultyFontNormal;

        diffFont.draw(gameBatch, difficultyText, difficultyX, y);
    }

    public void setHardMode(boolean hard) {this.hardMode = hard;}

    public void dispose() {
        shapeRenderer.dispose();
        hudBackground.dispose();
        logo.dispose();
        font.dispose();
        difficultyFontNormal.dispose();
        difficultyFontHard.dispose();
    }
}