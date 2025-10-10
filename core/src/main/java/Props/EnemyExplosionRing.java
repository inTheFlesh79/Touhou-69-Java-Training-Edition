package Props;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemyExplosionRing {
    private Sprite spr;
    private float timeAlive;
    private float duration = 0.45f;
    private float centerX, centerY;

    public EnemyExplosionRing(TextureRegion tx, float fairyX, float fairyY) {
        spr = new Sprite(tx);
        spr.setOriginCenter();
        centerX = fairyX;
        centerY = fairyY;
        spr.setPosition(centerX - (spr.getWidth() / 2f), centerY - spr.getHeight() / 2f);
        timeAlive = 0f;
    }

    public boolean isAlive() {
        return timeAlive < duration;
    }

    public void update(float delta) {
        timeAlive += delta;

        float progress = timeAlive / duration;

        // Scale up from 0.5 to 2.0
        float scale = 0.5f + 2.5f * progress;
        spr.setScale(scale);

        // Fade out
        spr.setColor(1f, 1f, 1f, 1f - progress);

        // Keep centered
        spr.setPosition(centerX - spr.getWidth() / 2f, centerY - spr.getHeight() / 2f);
    }

    public void drawRing(SpriteBatch batch) {
        spr.draw(batch);
    }
}

