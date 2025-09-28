package puppy.code;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HUD {
    private final ShapeRenderer shapeRenderer;
    private final Touhou game;
    private final SpriteBatch gameBatch;
    private final Viewport viewport;

    public HUD(SpriteBatch gameBatch, Viewport viewport) {
        this.shapeRenderer = new ShapeRenderer();
        this.game = Touhou.getInstance();
        this.gameBatch = gameBatch;
        this.viewport = viewport;
    }

    public void drawHUD(int lives, int level, int score, int damage) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // HUD is always 360 wide, full height, anchored to the right
        float hudWidth = 360f;
        float hudHeight = worldHeight;
        float hudX = worldWidth - hudWidth;
        float hudY = 0;

        // draw burgundy background
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(54/255f, 33/255f, 33/255f, 1f));
        shapeRenderer.rect(hudX, hudY, hudWidth, hudHeight);
        shapeRenderer.end();

        // draw text on top
        hudElements(lives, level, score, damage, hudX, hudWidth, worldHeight);
    }

    private void hudElements(int lives, int level, int score, int damage,
                             float hudX, float hudWidth, float worldHeight) {
        game.getFont().getData().setScale(1f);

        gameBatch.setProjectionMatrix(viewport.getCamera().combined);
        gameBatch.begin();

        float textX = hudX + 40;  // padding from left inside HUD
        float y = worldHeight - 60;

        game.getFont().draw(gameBatch, "HiScore: " + game.getHighScore(), textX, y);
        y -= 35;
        game.getFont().draw(gameBatch, "Score:    " + score, textX, y);
        y -= 70;
        game.getFont().draw(gameBatch, "Lives: " + lives, textX, y);
        y -= 35;
        game.getFont().draw(gameBatch, "Power: " + damage, textX, y);
        y -= 70;
        game.getFont().draw(gameBatch, "Level: " + level, textX, y);

        gameBatch.end();
    }
}
