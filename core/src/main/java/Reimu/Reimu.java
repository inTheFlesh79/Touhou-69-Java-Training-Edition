package Reimu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import Managers.BulletManager;

public class Reimu {
    private int lives;
    private int score;
    private int damage;
    private Sprite spr;
    private Circle sprHitbox;
    private Vector2 sprHitboxPos;
    private Rectangle sprDropHitbox;
    private Texture txBala;
    private Shield shield = null;
    private RingStripe ring1, ring2;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private boolean isShielded = false;
	private boolean dead = false;
    
    private Sound hurtSound;
    private boolean hurt = false;
    private float maxHurtTime=0.75f;
    private float hurtTime;
    private float bulletGenInterval = 0.1f;
    private float bulletGenTimer = 0f;
    
    private Texture spriteSheet, ringStripeSheet;
    private TextureRegion[][] spriteRegions, ringStripeRegions;
    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationRight;
    private float animationTime = 0f;
    
    public Reimu(int x, int y, Sound soundChoque, Texture txBala, Sound soundBala) {
    	hurtSound = soundChoque;
    	//this.soundBala = soundBala;
    	this.txBala = txBala;
    	
    	spriteSheet = new Texture(Gdx.files.internal("reimuSpriteSheet.png"));
    	spriteRegions = TextureRegion.split(spriteSheet, 32, 48);
    	ringStripeSheet = new Texture(Gdx.files.internal("ringSpriteSheet.png"));
    	ringStripeRegions = TextureRegion.split(ringStripeSheet, 80, 80);
    	reimuAnimation();
    	
    	spr.setPosition(x, y);
    	spr.setBounds(x, y, 32, 48);
    	sprHitbox = new Circle(spr.getX() + spr.getWidth() / 2, spr.getY() + spr.getHeight()/ 2, 5f);
    	sprHitboxPos = new Vector2((int) (spr.getX() + spr.getWidth() / 2), (int) (spr.getY() + spr.getHeight()/ 2));
    	sprDropHitbox = spr.getBoundingRectangle();
    }
    
    public void reimuAnimation() {
    	TextureRegion[] animationFrames = new TextureRegion[8];
    	for (int i = 0; i < 8; i++) {
    		TextureRegion currentSprite = spriteRegions[0][i];
            animationFrames[i] = currentSprite;
        }
    	
    	TextureRegion[] animationFramesRight = new TextureRegion[8];
    	for (int i = 0; i < 8; i++) {
    		TextureRegion currentSprite = spriteRegions[2][i];
    		animationFramesRight[i] = currentSprite;
        }
    	
    	TextureRegion[] animationFramesLeft = new TextureRegion[8];
    	for (int i = 0; i < 8; i++) {
    		TextureRegion currentSprite = spriteRegions[1][i];
    		animationFramesLeft[i] = currentSprite;
        }
    	
    	animation = new Animation<TextureRegion>(0.1f, animationFrames);
    	animationRight = new Animation<TextureRegion>(0.1f, animationFramesRight);
    	animationLeft = new Animation<TextureRegion>(0.1f, animationFramesLeft);
    	spr = new Sprite(animationFrames[0]);
    }
    
    public void draw(SpriteBatch batch, BulletManager bulletMng, float scrWidth, float scrHeight) {
        reimuAnimationAndMovement(batch, scrWidth, scrHeight);
        reimuShooting(bulletMng);
        sprDropHitbox.setPosition(spr.getX(), spr.getY());
    }
    
    public void reimuAnimationAndMovement(SpriteBatch batch, float scrWidth, float scrHeight) {
    	float deltaTime = Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true);
        float speed = 500f;
        
        bulletGenTimer += deltaTime;

        if (!hurt) {
            animationTime += deltaTime;
             // Loop animation
            spr.setRegion(currentFrame);

            float slowSpeed = speed * 0.43f; // Define the slow speed as half of the original speed

         // Movement logic
			 if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
				 currentFrame = animationLeft.getKeyFrame(animationTime, false); // Loop animation
		         spr.setRegion(currentFrame);
			     float moveSpeed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? slowSpeed : speed; // Use slowSpeed if shift is held
			     spr.setX(spr.getX() - moveSpeed * deltaTime); // Move left
			 }
			
			 if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
				 currentFrame = animationRight.getKeyFrame(animationTime, false); // Loop animation
		         spr.setRegion(currentFrame);
			     float moveSpeed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? slowSpeed : speed; // Use slowSpeed if shift is held
			     spr.setX(spr.getX() + moveSpeed * deltaTime); // Move right
			 }
			
			 if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
			     float moveSpeed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? slowSpeed : speed; // Use slowSpeed if shift is held
			     spr.setY(spr.getY() + moveSpeed * deltaTime); // Move up
			 }
			
			 if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
			     float moveSpeed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? slowSpeed : speed; // Use slowSpeed if shift is held
			     spr.setY(spr.getY() - moveSpeed * deltaTime); // Move down
			 }
			 outOfBounds(scrWidth, scrHeight);
			 spr.draw(batch);
        } 
        else {
            hurtState(batch, scrWidth-360);
        }
    }
    
    public void drawReimuHitbox(SpriteBatch batch, OrthographicCamera camera) {
        sprHitboxPos.set(
            spr.getX() + spr.getWidth() / 2f + 1,
            spr.getY() + spr.getHeight() / 2f + 1
        );
        sprHitbox.setPosition(sprHitboxPos.x, sprHitboxPos.y);

        batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // alpha = fully visible if not hurt, semi-transparent if hurt
        float alpha = hurt ? 0.1f : 1f;
        
        Gdx.app.debug("ReimuHitbox", "hurt=" + hurt + "  alpha=" + alpha);

        // --- Filled white circle ---
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 1f, alpha); // white with alpha
        shapeRenderer.circle(sprHitboxPos.x, sprHitboxPos.y, sprHitbox.radius);
        shapeRenderer.end();

        // --- Red outline ---
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 0f, 0f, alpha); // red with alpha
        shapeRenderer.circle(sprHitboxPos.x, sprHitboxPos.y, sprHitbox.radius);
        shapeRenderer.end();

        batch.begin();
    }


    public void reimuShooting(BulletManager bulletMng) {
    	// Shoot bullet
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        	if (bulletGenTimer >= bulletGenInterval) {
	            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 8, txBala, damage);
	            bulletMng.addReimuBullets(bala);
	            bulletGenTimer = 0f;
        	}
        }
    }
    
    public void hurtState(SpriteBatch batch, float scrWidth) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float progress = 1f - (hurtTime / maxHurtTime);

        // --- Step 1: squash/stretch scaling ---
        float scaleY = 3f * progress + 1f;   // grows tall
        float scaleX = 1f - 1f * progress;   // shrinks thin
        spr.setScale(scaleX, scaleY);

        // --- Step 2: fade-in alpha ---
        float alpha = 1f - progress; // starts transparent, becomes opaque
        Color c = spr.getColor();
        spr.setColor(c.r, c.g, c.b, alpha);
        float halfDuration = maxHurtTime / 2f;

        if (progress < 0.5f) {
            if (ring1 == null) {
                ring1 = new RingStripe(ringStripeRegions[0][0], 
	                    spr.getX() + spr.getWidth() / 2f, 
	                    spr.getY() + spr.getHeight() / 2f, 
	                    halfDuration, false); // shrinking
            }
            if (ring1 != null && !ring1.isFinished()) {
                ring1.updateAndDraw(batch, deltaTime);
            }
        } else {
            if (ring2 == null) {
                ring2 = new RingStripe(ringStripeRegions[0][1], 
	                    spr.getX() + spr.getWidth() / 2f, 
	                    spr.getY() + spr.getHeight() / 2f, 
	                    halfDuration, true); // expanding
            }
            if (ring2 != null && !ring2.isFinished()) {
                ring2.updateAndDraw(batch, deltaTime);
            }
        }
        
        spr.draw(batch);
        hurtTime -= deltaTime;
        if (hurtTime <= 0) {
            hurt = false;
            spr.setScale(1f, 1f); // reset scale
            spr.setColor(1f, 1f, 1f, 1f); // reset alpha
            spr.setPosition(scrWidth / 2f, 64f); // bottom center (like Touhou)
            ring1 = null;
            ring2 = null;
        }
    }
    
    public void outOfBounds(float scrWidth, float scrHeight) {
        if (spr.getX() < 0) spr.setX(0);
        if (spr.getX() + spr.getWidth() > 920) spr.setX(920 - spr.getWidth());
        if (spr.getY() < 0) spr.setY(0);
        if (spr.getY() + spr.getHeight() > scrHeight) spr.setY(scrHeight - spr.getHeight());
    }
    
    public void craftShield() {shield = new Shield(spr.getX(), spr.getY(), spr);}
    public void drawShield(SpriteBatch batch) {shield.draw(batch); shield.update();}
    public boolean shieldExists() {return shield != null;}
    public boolean shieldExpired() {return shield.isExpired();}
    public void removeShield() {shield = null;}
    
    public boolean isDead() {return !hurt && dead;}
    public boolean isHurt() {return hurt;}
    public boolean isShielded() {return isShielded;}
    
    public Circle getSprHitbox() {return sprHitbox;}
    public Circle getShieldHitbox() {return shield.getHitbox();}
    public Sprite getSpr() {return spr;}
    public int getLives() {return lives;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
    public int getDamageBala() {return damage;}
    public float getHurtTime() {return hurtTime;}
    public float getMaxHurtTime() {return maxHurtTime;}
    public int getScore() {return score;}
    
    public void setHurtTime(float t) {hurtTime = t;}
    public void setHurt(boolean b) {hurt = b;}
	public void setVidas(int lives) {this.lives = lives;}
	public void setDamage(int d) {this.damage = d;}
	public void setShielded (boolean b) {isShielded = b;}
	public void setDead(boolean b) {dead = b;}
	public void setScore(int s) {score = s;}
	
	public void oneUp() {this.lives += 1;}
	public void oneDown() {this.lives -= 1;}
	public void addDamage (int d) {this.damage += d;}
	public void addScore (int s) {this.score += s;}
	public void playHurtSound() {hurtSound.play();}
}