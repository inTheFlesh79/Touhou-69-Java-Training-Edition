package Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Enemy {
	protected boolean isDestroyed = false;
	protected int health;
	protected int defaultHealth;
	protected int healthChoice;
	protected float speed;
	protected float defaultSpeed;
	protected int speedChoice;
	protected int bhpChoice;
	
	protected Sprite spr;
	protected Sound sonidoHerido;
	
	protected Sound soundBala;
	protected Texture txBala;
	
	protected boolean herido = false;
	protected int tiempoHeridoMax = 50;
	protected int tiempoHerido;
	
	protected SpriteBatch batch;
	protected Texture spriteSheet;
	protected TextureRegion[][] spriteRegions;
	protected TextureRegion lastSprite;
	protected Animation<TextureRegion> animation;
	protected float animationTime = 0f;
	
	protected boolean firstSpawn = true;
	protected float maxIdleTime;
	protected float idleTime = 0f;
	
	protected float maxShootingTime;//kk
	protected float shootingTime = 0f;
	protected float noShootingCooldown = 0f;
	
	protected float bulletGenInterval;//kk
	protected float bulletGenTimer = 0f;
	
	protected int currentArea = 1;
	//check
	protected boolean isShooting = true;
	protected boolean inTrack = false;
	
	protected Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("fairyDeath.ogg"));
	protected Sound shootingSound = Gdx.audio.newSound(Gdx.files.internal("ATTACK3.ogg"));
	
	public Enemy() {};
	
	public final void enemyRoutine(SpriteBatch batch, float scrWidth, float scrHeight) {
		draw(batch);
		update(scrWidth, scrHeight);
	}
	
	public abstract void draw(SpriteBatch batch);
	public abstract void update(float scrWidth, float scrHeight);
	
	public void outOfBounds(float scrWidth, float scrHeight) {
	    if (spr.getX() < 0) spr.setX(0);
	    if (spr.getX() + spr.getWidth() > scrWidth) 
	        spr.setX(scrWidth - spr.getWidth());

	    if (spr.getY() < 0) spr.setY(0);
	    if (spr.getY() + spr.getHeight() > scrHeight) 
	        spr.setY(scrHeight - spr.getHeight());
	}

	
	public int getHealth() {return health;}
	
	public void setHealth(int health) {this.health = health;}
	public void setSpeed(float speed) {this.speed = speed;}
	public void setDefaultSpeed(float speed) {this.defaultSpeed = speed;}
	public void setHealthChoice(int healthChoice) { this.healthChoice = healthChoice; }
	public void setDefaultHealth(int health) {this.defaultHealth = health;}
	public void setSpeedChoice(int speedChoice) { this.speedChoice = speedChoice; }
	public void setBhpChoice(int bhpChoice) { this.bhpChoice = bhpChoice; }
	public void setIsShooting (boolean is) {this.isShooting = is;}

	public int getHealthChoice() { return healthChoice; }
	public int getSpeedChoice() { return speedChoice; }
	public float getDefSpeed() {return defaultSpeed;}
	public float getSpeed() { return speed; }
	public int getBhpChoice() { return bhpChoice; }
	public Sprite getSpr() {return spr;}
	public boolean isShooting() {return isShooting;}

	public void playExplosionSound() {explosionSound.play(0.2f);}
	public Sound getShootingSound() {return shootingSound;}
}