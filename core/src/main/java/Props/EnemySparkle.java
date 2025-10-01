package Props;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemySparkle {
    private Sprite spr;
    private float timeAlive;
    private float duration = 0.65f;
    private float vx, vy;
    private float x, y;

    public EnemySparkle(TextureRegion tx, float fairyX, float fairyY) {
        spr = new Sprite(tx);
        spr.setOriginCenter();
        x = fairyX;
        y = fairyY;

        // Random direction + speed
        float angle = (float)(Math.random() * 360.0);
        float speed = 60f + (float)Math.random() * 40f; // pixels/sec
        vx = (float)Math.cos(Math.toRadians(angle)) * speed;
        vy = (float)Math.sin(Math.toRadians(angle)) * speed;

        timeAlive = 0f;
    }

    public boolean isAlive() {
        return timeAlive < duration;
    }

    public void update(float delta) {
        timeAlive += delta;
        x += vx * delta;
        y += vy * delta;

        float progress = timeAlive / duration;

        // shrink and fade
        float scale = 1f - 0.8f * progress;
        spr.setScale(scale);
        spr.setColor(1f, 1f, 1f, 1f - progress);

        spr.setPosition(x - spr.getWidth() / 2f, y - spr.getHeight() / 2f);
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }
}