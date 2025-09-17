package Reimu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Drop {
    protected Sprite spr;
    protected boolean isDestroyed = false;
    protected float yVelocity;     // current vertical velocity
    protected float gravity = -200f; // gravity force (negative = down)
    protected float maxFallSpeed = -500f; // maximum downward velocity
    protected Texture spriteSheet;
    protected TextureRegion[][] spriteRegions;
    protected Rectangle hitbox;
    protected float deltaTime = Gdx.graphics.getDeltaTime();
    
    public Drop(float spawnX, float spawnY) {
    	spr = new Sprite();
        spriteSheet = new Texture(Gdx.files.internal("proyectilesSpriteSheet.png"));
        spriteRegions = TextureRegion.split(spriteSheet, 16, 18);
        
        spr.setBounds(spawnX, spawnY, 22, 24);
        // initial upward velocity (state 1: goes up before falling)
        yVelocity = 250f; // tweak this to control how high the item rises
    }

    public Drop() {}

	public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void update() {
        dropMovement(deltaTime);
        hitbox.setPosition(spr.getX(), spr.getY());
    }

    public void dropMovement(float deltaTime) {
        // apply gravity
        yVelocity += gravity * deltaTime;

        // clamp velocity to maximum fall speed
        if (yVelocity < maxFallSpeed) {
            yVelocity = maxFallSpeed;
        }

        // update position
        spr.setY(spr.getY() + yVelocity * deltaTime);
    }
    
    public boolean isDestroyed() {return isDestroyed;}
    public void dispose() {spriteSheet.dispose();}
    
    public Rectangle getHitbox() {return hitbox;};
}