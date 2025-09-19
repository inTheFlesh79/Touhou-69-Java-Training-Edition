package Reimu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import Managers.GameObjectManager;

public class Reimu {
    private int lives;
    private int score;
    private int damage = 10;
    private Sprite spr;
    private Circle sprHitbox;
    private Rectangle sprDropHitbox;
    private Texture txBala;
    private Shield shield = null;
    private boolean isShielded = false;
	private boolean destruida = false;
    
    private Sound hurtSound;
    private boolean hurt = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;
    private float bulletGenInterval = 0.1f;
    private float bulletGenTimer = 0f;
    
    private Texture spriteSheet;
    private TextureRegion[][] spriteRegions;
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
    	//lastSprite = new TextureRegion(spriteSheet, 320, 0, 3, 48);
    	
    	reimuAnimation();
    	
    	spr.setPosition(x, y);
    	//spr.setOriginCenter();
    	spr.setBounds(x, y, 32, 48);
    	sprHitbox = new Circle(spr.getX() + spr.getWidth() / 2, spr.getY() + spr.getHeight()/ 2, 5f);
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
    
    public void draw(SpriteBatch batch, GameObjectManager gameMng) {
        reimuAnimationAndMovement(batch);
        reimuShooting(gameMng);
        sprDropHitbox.setPosition(spr.getX(), spr.getY());
    }
    
    public void reimuAnimationAndMovement(SpriteBatch batch) {
    	float deltaTime = Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true);
        float speed = 500f;
        
        bulletGenTimer += deltaTime;

        if (!hurt) {
            animationTime += deltaTime;
             // Loop animation
            spr.setRegion(currentFrame);

            float slowSpeed = speed * 0.5f; // Define the slow speed as half of the original speed

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

			 outOfBounds();
			 
			 spr.draw(batch);
			 drawReimuHitbox(batch);
        } 
        else {
            heridoState(batch);
        }
    }
    
    public void drawReimuHitbox(SpriteBatch batch) {
    	sprHitbox.setPosition(spr.getX() + spr.getWidth() / 2, spr.getY() + spr.getHeight() / 2);
    	batch.end(); // End the sprite batch temporarily
		ShapeRenderer shapeRenderer = new ShapeRenderer(); // Initialize shapeRenderer
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.circle(sprHitbox.x, sprHitbox.y, 5f); // Draw hitbox circle
		shapeRenderer.end();
		shapeRenderer.dispose();
		batch.begin(); // Restart the sprite batch
    }
    
    public void reimuShooting(GameObjectManager gameMng) {
    	// Shoot bullet
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        	if (bulletGenTimer >= bulletGenInterval) {
	            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 8, txBala);
	            gameMng.agregarReimuBullets(bala);
	            bulletGenTimer = 0f;
        	}
        }
    }
    public void heridoState(SpriteBatch batch) {
    	spr.setX(spr.getX() + MathUtils.random(-2, 2));
        spr.draw(batch); 
        spr.setX(spr.getX());
        tiempoHerido--;
        if (tiempoHerido <= 0) hurt = false;
    }
    
    public void outOfBounds() {
    	if (spr.getX() < 0) spr.setX(0);
		if (spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) spr.setX(Gdx.graphics.getWidth() - spr.getWidth());
		if (spr.getY() < 0) spr.setY(0);
		if (spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) spr.setY(Gdx.graphics.getHeight() - spr.getHeight());
    }
    
    public void craftShield() {shield = new Shield(spr.getX(), spr.getY(), spr);}
    public void drawShield(SpriteBatch batch) {shield.draw(batch); shield.update();}
    public boolean shieldExists() {return shield != null;}
    public boolean shieldExpired() {return shield.isExpired();}
    public void removeShield() {shield = null;}
    
    public boolean estaDestruido() {return !hurt && destruida;}
    public boolean isHurt() {return hurt;}
    public boolean isShielded() {return isShielded;}
    
    public int getDamageBala() {return damage;}
    public Circle getSprHitbox() {return sprHitbox;}
    public int getVidas() {return lives;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
    public Sprite getSpr() {return spr;}
    public int getTHerido() {return tiempoHerido;}
    public int getTHeridoMax() {return tiempoHeridoMax;}
    public int getScore() {return score;}
    public Circle getShieldHitbox() {return shield.getHitbox();}
    
    public void setTHerido(int t) {tiempoHerido = t;}
    public void setHurt(boolean b) {hurt = b;}
	public void setVidas(int lives) {this.lives = lives;}
	public void setDamage(int d) {this.damage = d;}
	public void setShielded (boolean b) {isShielded = b;}
	public void setDestroyed(boolean b) {destruida = b;}
	public void setScore(int s) {score = s;}
	
	public void oneUp() {this.lives += 1;}
	public void oneDown() {this.lives -= 1;}
	
	public void addDamage (int d) {this.damage += d;}
	public void addScore (int s) {this.score += s;}
	
	public void playHurtSound() {hurtSound.play();}
}