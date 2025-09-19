package Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import BulletHellPatterns.BulletHellPattern;

public abstract class Enemy {
	protected boolean isDestroyed = false;
	protected int health;
	protected int healthChoice;
	protected float speed;
	protected int speedChoice;
	protected BulletHellPattern bulletPattern; 
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
	
	protected Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("ATTACK3.mp3"));
	
	public Enemy() {};
	
	public final void enemyRoutine(SpriteBatch batch) {
		draw(batch);
		update();
	}
	
	public abstract void draw(SpriteBatch batch);
	public abstract void update();
	
	public int getHealth() {return health;}
	
	public void setHealth(int health) {this.health = health;}
	public void setSpeed(float speed) {this.speed = speed;}
	public void setBulletPattern(BulletHellPattern bhp) {this.bulletPattern = bhp;}
	public void setHealthChoice(int healthChoice) { this.healthChoice = healthChoice; }
	public void setSpeedChoice(int speedChoice) { this.speedChoice = speedChoice; }
	public void setBhpChoice(int bhpChoice) { this.bhpChoice = bhpChoice; }
	public void setIsShooting (boolean is) {this.isShooting = is;}

	public int getHealthChoice() { return healthChoice; }
	public int getSpeedChoice() { return speedChoice; }
	public float getSpeed() { return speed; }
	public int getBhpChoice() { return bhpChoice; }
	public Sprite getSpr() {return spr;}

	public void playExplosionSound() {explosionSound.play();}
}