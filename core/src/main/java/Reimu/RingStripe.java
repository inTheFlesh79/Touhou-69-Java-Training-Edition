package Reimu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RingStripe {
    private Sprite sprite;
    private float duration;   // how long the ring lives
    private float time;       // current age
    private float startScale, endScale;
    private boolean expanding;

    public RingStripe(TextureRegion region, float x, float y, float duration, boolean expanding) {
        this.sprite = new Sprite(region);
        this.duration = duration;
        this.expanding = expanding;
        this.time = 0f;

        if (expanding) {
            startScale = 0.2f;
            endScale = 7.5f;
        } else {
            startScale = 7.5f;
            endScale = 0.1f;
        }

        // Center sprite around given position
        sprite.setOriginCenter();
        sprite.setPosition(x - sprite.getWidth() / 2f, y - sprite.getHeight() / 2f);
    }

    public boolean isFinished() {return time >= duration;}

    public void updateAndDraw(SpriteBatch batch, float deltaTime) {
        time += deltaTime;
        float progress = Math.min(time / duration, 1f);
        float scale = startScale + (endScale - startScale) * progress;
        // Fade only this sprite
        float alpha = expanding 
            ? (1f - progress)   // fades at the end of expand
            : (1f - progress);  // fades as it shrinks

        sprite.setScale(scale);
        sprite.setColor(1f, 1f, 1f, alpha);  // <--- only affects this sprite
        sprite.draw(batch);
    }
}