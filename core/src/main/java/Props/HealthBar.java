package Props;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HealthBar {
    private Texture texture;
    private Sprite spr;
    private int maxHealth;
    private int currentHealth;

    private float fullWidth;  // <- store once
    private float fullHeight;

    public HealthBar(int maxHealth, float drawWidth, float drawHeight, float marginTop) {
        this.texture = new Texture(Gdx.files.internal("bossHpBar.png"));
        this.spr = new Sprite(texture);

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;

        this.fullWidth = drawWidth;
        this.fullHeight = drawHeight;

        spr.setSize(fullWidth, fullHeight);

        float x = (Gdx.graphics.getWidth() - fullWidth) / 2f;
        float y = Gdx.graphics.getHeight() - fullHeight - marginTop;
        spr.setPosition(x, y);
    }

    public void setHealth(int health) {
        this.currentHealth = Math.max(0, Math.min(health, maxHealth));

        float healthPercent = (float) currentHealth / maxHealth;

        // Always use fullWidth for consistency
        float visibleWidth = fullWidth * healthPercent;

        spr.setRegion(0, 0, (int)(texture.getWidth() * healthPercent), texture.getHeight());
        spr.setSize(visibleWidth, fullHeight);
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void dispose() {
        texture.dispose();
    }
}