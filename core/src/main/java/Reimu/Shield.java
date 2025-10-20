package Reimu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;

public class Shield {
    private Sprite spr;
    private Sprite reimuSpr;

    private Circle hitbox;
    private float rotation;
    private float maxDuration = 5f;
    private float elapsed;

    private float initialRadius;
    private float currentRadius;
    private Texture spriteSheet;
    private TextureRegion[][] spriteRegions;

    public Shield(float spawnX, float spawnY, Sprite reimuSpr) {
        spriteSheet = new Texture(Gdx.files.internal("shield.png"));
        spriteRegions = TextureRegion.split(spriteSheet, 254, 254);
        spr = new Sprite(spriteRegions[0][0]);

        this.reimuSpr = reimuSpr;
        spr.setOriginCenter(); // important for rotation and scaling
        spr.setPosition(spawnX, spawnY);

        float radius = spr.getWidth() / 2f;
        initialRadius = radius;
        currentRadius = radius;
        hitbox = new Circle(spr.getX() + radius, spr.getY() + radius, radius);
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        elapsed += deltaTime;
        if (elapsed >= maxDuration) {
            dispose();
            return;
        }

        shieldMovement(deltaTime);
        reduceSprRadius();
    }

    private void shieldMovement(float deltaTime) {
        // Follow Reimu's position (centered at bottom)
        float shieldX = reimuSpr.getX() + reimuSpr.getWidth() / 2f - spr.getWidth() / 2f;
        float shieldY = reimuSpr.getY() - ((spr.getHeight() / 2f)-24); 
        spr.setPosition(shieldX, shieldY);

        // Rotate
        rotation += 90 * deltaTime; // degrees per second
        if (rotation >= 360) rotation -= 360;
        spr.setRotation(rotation);

        // Update hitbox
        hitbox.setPosition(spr.getX() + spr.getWidth() / 2f, spr.getY() + spr.getHeight() / 2f);
    }

    private void reduceSprRadius() {
        // Scale radius proportionally with remaining time
        float timeLeft = maxDuration - elapsed;
        float ratio = timeLeft / maxDuration;
        currentRadius = initialRadius * ratio;

        float scale = ratio; // shrink sprite visually too
        spr.setScale(scale);
        hitbox.setRadius(currentRadius);
    }
    
    public Circle getHitbox() {return hitbox;}

    public boolean isExpired() {
        return elapsed >= maxDuration;
    }

    private void dispose() {
        // Notify game to remove this shield instance
    }
}