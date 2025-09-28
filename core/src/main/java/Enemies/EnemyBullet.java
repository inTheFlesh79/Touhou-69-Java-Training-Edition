package Enemies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;

public class EnemyBullet {
	private Circle hitbox;
	private TextureRegion bulletTxt;
	private float x, y;
    private float velocityX, velocityY;
    private float radius;
    //private static Random random = new Random();
    //private TextureRegion[][] spriteRegions;
    private Texture spriteSheet;
    private boolean destroyed = false;
    
    public EnemyBullet(float x, float y, float velocityX, float velocityY, TextureRegion[][] sharedRegions) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        
        //spriteSheet = new Texture(Gdx.files.internal("bulletTypes.png"));
		//spriteRegions = TextureRegion.split(spriteSheet, 24, 30);
		TextureRegion currentSprite = sharedRegions[new Random().nextInt(4)][0];
        this.bulletTxt = currentSprite;

        this.radius = calculateRadius(bulletTxt);

        hitbox = new Circle(x, y, radius);
    }
    
    public EnemyBullet(float x, float y, TextureRegion[][] sharedRegions) {
    	this.x = x;
        this.y = y;
        
        TextureRegion currentSprite = sharedRegions[new Random().nextInt(4)][0];
        this.bulletTxt = currentSprite;

        this.radius = calculateRadius(bulletTxt);

        hitbox = new Circle(x, y, radius);
    }
    
	public float calculateRadius(TextureRegion bulletTxt) {
		int width = bulletTxt.getRegionWidth();
        int height = bulletTxt.getRegionHeight();
        
        if (width == height) {
            return width / 2f;
        } else {
            return (width + height) / 4f;
        }	
	}
	
	public void draw(SpriteBatch batch) {
        batch.draw(bulletTxt, x - radius, y - radius, radius * 2, radius * 2);
    }
	
	public void update(float scrWidth, float scrHeight) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		//System.out.println("deltaTime: " + deltaTime);
	    //System.out.println("velocityX: " + velocityX + ", velocityY: " + velocityY);
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        hitbox.setPosition(x, y);
        
        if (x - radius < -50 || x + radius > scrWidth+50) {
	        destroyed = true;
	    }
	    if (y - radius < -50 || y + radius > scrHeight+50) {
	        destroyed = true;
	    }
    }
	
	public Circle getHitbox() { return hitbox; }
	public void setHitbox(Circle hitbox) { this.hitbox = hitbox; }

	public TextureRegion getBulletTxt() { return bulletTxt; }
	public void setBulletTxt(TextureRegion bulletTxt) { this.bulletTxt = bulletTxt; }

	public float getX() { return x; }
	public void setX(float x) { this.x = x; }

	public float getY() { return y; }
	public void setY(float y) { this.y = y; }

	public float getVelocityX() { return velocityX; }
	public void setVelocityX(float velocityX) { this.velocityX = velocityX; }

	public float getVelocityY() { return velocityY; }
	public void setVelocityY(float velocityY) { this.velocityY = velocityY; }

	public float getRadius() { return radius; }
	public void setRadius(float radius) { this.radius = radius; }

	public Texture getSpriteSheet() { return spriteSheet; }
	public void setSpriteSheet(Texture spriteSheet) { this.spriteSheet = spriteSheet; }

	public boolean isDestroyed() { return destroyed; }
	public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
	
	public void dispose() {
        spriteSheet.dispose();
    }
}