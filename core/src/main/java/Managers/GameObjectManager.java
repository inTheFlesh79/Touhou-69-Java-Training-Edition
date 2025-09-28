package Managers;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Factory.EnemyFactory;
import Factory.TouhouEnemyFactory;
import Reimu.Reimu;

public class GameObjectManager {
	private float scrWidth, scrHeight;
    private int correctas;
    
	// Personajes y Objetos de Personaje
    private FitViewport viewport;
	private SpriteBatch batch;
	private Reimu reimu;
	private EnemyFactory eFactory = new TouhouEnemyFactory();
	private Random random = new Random();
	
	// Managers de Personajes con comportamientos dinamicos (en este caso: Fairy)
	private FairyManager fairyMng = new FairyManager();
	private LevelManager levelMng = new LevelManager();
	private DropManager dropMng = new DropManager();
	private CollisionManager collisionMng = new CollisionManager();
	private BulletManager bulletMng = new BulletManager();
	private BossManager bossMng = new BossManager();
	private PropManager propMng = new PropManager();
	
	// ESTADO PARA CONTROLAR PANTALLA PREGUNTAS
	private boolean exerciseDone = false;
	private boolean fightBoss = false;
	private boolean checkRewards = false;
	
	public GameObjectManager(SpriteBatch batch, FitViewport viewport, int nivel, int vidas, int score, int power) {
		this.batch = batch;
		this.viewport = viewport;
		scrWidth = viewport.getWorldWidth();
		scrHeight = viewport.getWorldHeight();
		levelMng.setCurrentLevel(nivel, scrWidth, scrHeight);
		//crear a Reimu ^_^
		Vector2 spawn = new Vector2(920/2f, 30);
		reimu = new Reimu((int)spawn.x,(int)spawn.y,
				Gdx.audio.newSound(Gdx.files.internal("DEAD.ogg")), 
				new Texture(Gdx.files.internal("Rocket2.png")), 
				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        reimu.setVidas(vidas);
        reimu.setDamage(power);
        reimu.setScore(score);
        bulletMng.setReimu(reimu);
        
        eFactory.setCurrentBulletManager(bulletMng);
        gameSetup();
	}                     
	
	public void update() {
		scrWidth = viewport.getWorldWidth();
		scrHeight = viewport.getWorldHeight();
		bulletMng.reimuBulletsDrawer(batch, scrWidth, scrHeight);
		bulletMng.enemyBulletsDrawer(batch, scrWidth, scrHeight);
		fairyDrawer();
		bossDrawer();
		dropMng.drawDrops(batch);
		enemyDropsCollisionManager();
		reimu.draw(batch, bulletMng, scrWidth, scrHeight);
		reimu.drawReimuHitbox(batch, (OrthographicCamera) viewport.getCamera());
		
		if (reimu.shieldExists() && !reimu.shieldExpired()) {
			reimu.drawShield(batch);
		}
		else {
			reimu.setShielded(false);
			reimu.removeShield();
		}
		
		if (!reimu.isHurt()) {
			reimuBulletsCollisionManager();
			enemyBulletsCollisionManager();
		}
	}
	
	public void gameSetup() {
		levelMng.whatLevelIsIt();
		System.out.println("Current Wave: "+levelMng.getCurrentLvlWave());
	    fairyMng.fairySetup(levelMng.getFairiesCurrentWave(), levelMng.getFairyStartingPoint(), levelMng.getFairyIsShooting(), bulletMng, scrWidth, scrHeight);
	    levelMng.changeCurrentWave();
	    levelMng.areWavesOver();
	    bossMng.createBoss(eFactory.craftBoss(levelMng.getLvlId(), scrWidth-360, scrHeight));
	}
	
	/*
	 * FUNCIONES DE DRAW, UPDATE Y MANEJO DE OBJETOS EN PANTALLAJUEGO
	 * 
	 */
	public void reimuBulletsCollisionManager() {
		// COLISION DE BOSS VS BULLETS
		for (int i = 0; i < bulletMng.getReimuBulletsSize(); i++) {
			if (collisionMng.chkColEnemyVsBullet(bulletMng.getReimuBullet(i), bossMng.getBoss())) {
				propMng.getBossHPBar().setHealth(bossMng.getBoss().getHealth());
			    //System.out.println("Boss Health: "+boss.getHealth());
				if (collisionMng.isAliveAfterLastCol(bossMng.getBoss()) == null) {
					bossMng.getBoss().dispose();
					bossMng.destroyBoss();
					bulletMng.clearReimuBullets();
					reimu.addScore(1000);
					break;
				}
			}
		
			// COLISION DE FAIRIES VS BULLETS
			for (int j = 0; j < fairyMng.getFairiesSize(); j++) {
			    if (collisionMng.chkColEnemyVsBullet(bulletMng.getReimuBullet(i),fairyMng.getFairy(j))) {
			        // If the fairy's health reaches zero, remove it and play sound
			        if (collisionMng.isAliveAfterLastCol(fairyMng.getFairy(j)) == null) {
			            fairyMng.getFairy(j).playExplosionSound();
			            dropMng.spawnDrop(fairyMng.getFairy(j).getSpr().getX(),fairyMng.getFairy(j).getSpr().getY());
			            fairyMng.removeFairy(j);
			            j--;  // Adjust the index after removing a fairy
			            reimu.addScore(100);  // Increment the score
			        }
			    }
			}
		}
	}
	
	// Drawer and Collission manager for EnemyBullet objects
	public void enemyBulletsCollisionManager() {
		for (int i = 0; i < bulletMng.getEnemyBulletsSize(); i++) {
		    if (collisionMng.chkColReimuVsEBullet(bulletMng.getEnemyBullet(i), reimu)/* && !reimu.isShielded()*/) {bulletMng.removeEnemyBullet(i);}
		    if (reimu.shieldExists() && collisionMng.chkColShieldVsEBullet(bulletMng.getEnemyBullet(i), reimu)) {bulletMng.removeEnemyBullet(i);}
		}
	}
	
	// Drawer and Collission manager for Drop vs Reimu objects
	public void enemyDropsCollisionManager() {
		for (int i = 0; i < dropMng.getDropsSize(); i++) {
			if (collisionMng.chkColReimuVsDrop(dropMng.getDrop(i), reimu)) {
				dropMng.applyDropEffect(dropMng.getDrop(i), reimu);
	            dropMng.removeDrop(i);
			}
		}
	}
	
	// Drawer for Enemy objects
	public void fairyDrawer() {
		if (!fairyMng.isFairiesEmpty()) {
			fairyMng.fairiesDrawer(batch, scrWidth-360, scrHeight);
		}
		else if (fairyMng.isFairiesEmpty() && !levelMng.areWavesOver()) {
			System.out.println("Current Wave: "+levelMng.getCurrentLvlWave());
			fairyMng.fairySetup(levelMng.getFairiesCurrentWave(), levelMng.getFairyStartingPoint(), levelMng.getFairyIsShooting(),bulletMng, scrWidth, scrHeight);
			levelMng.changeCurrentWave();
		    levelMng.areWavesOver();
		    System.out.println("Fairies = "+fairyMng.getFairiesSize());
		}
	}
	
	public void bossDrawer() {
		if (fairyMng.isFairiesEmpty() && levelMng.areWavesOver() && areWeFightingBoss()) {
			if (!checkRewards) {
				applyRewards();
				System.out.println("Boss Health ="+bossMng.getBoss().getHealth());
				propMng.createBossHPBar(bossMng.getBoss().getHealth(), scrWidth-360, scrHeight);
				checkRewards = true;
			}
		 	//UNLEASH THE BOSS
			bossMng.getBoss().enemyRoutine(batch, scrWidth-360, scrHeight);
			propMng.drawBossHPBar(batch);
			//System.out.println("Boss Speed ="+bossMng.getBoss().getSpeed());
		}
	}
	
	public void applyRewards() {
		if (correctas == 0) {
			System.out.println("correctas == 0");
			return;
		}
		else if (correctas < 3) { // 1 o  2 correctas
			System.out.println("correctas < 3");
			reimu.addDamage(20);
		}
		else if (correctas < 4) { //3 correctas
			System.out.println("correctas < 4");
			reimu.oneUp();
			reimu.addDamage(20);
		}
		else if (correctas < 6) { //4 o 5 correctas
			System.out.println("correctas < 6");
			for (int i = 0; i < 2; i++) {reimu.oneUp();}
			reimu.addDamage(30);
			bossMng.lowerBossHealthNSpeed(random.nextInt(4, 6), random.nextInt(4, 6));
			System.out.println(bossMng.getBoss().getHealthChoice());
			System.out.println(bossMng.getBoss().getSpeedChoice());
			System.out.println("correctas post pantalla juego = "+correctas);
		}
		else {
			System.out.println("else");
			for (int i = 0; i < 3; i++) {reimu.oneUp();}
			reimu.addDamage(50);
			bossMng.lowerBossHealthNSpeed(random.nextInt(4, 6), random.nextInt(4, 6));
		}
	}
	
	/*
	 * FUNCIONES QUE RECIBEN OBJETO EnemyBullet O Bullet PARA AGREGARLOS AL ARRAYLIST QUE MANEJA SU EXISTENCIA EN PANTALLAJUEGO 
	 */
    public void setScore(int score) {reimu.setScore(score);}
    public void setFightBoss(boolean b) {this.fightBoss = b;}
    public void setCorrectas(int c) {correctas = c;}
    
    public int getReimuVidas() {return reimu.getVidas();}
    public int getScore() {return reimu.getScore();}
    public int getReimuDamage() {return reimu.getDamageBala();}
    public boolean isReimuDead() {return reimu.estaDestruido();}
    
    public boolean areWeFightingBoss() {return fightBoss;}
    public boolean isBossAlive() {return !(bossMng.getBoss() == null);}
    
    public boolean areWavesOver() {return levelMng.areWavesOver();}
    public boolean AreFairiesAlive() {return !(fairyMng.isFairiesEmpty());}
    
    public boolean readyToExercise() {return (!AreFairiesAlive() && levelMng.areWavesOver());}
    public void setExerciseDone(boolean s) {this.exerciseDone = s;}
    public boolean getExerciseState() { return exerciseDone; }
    
    public void disposeGOM() {
    	bulletMng.dispose();
    	fairyMng.dispose();
    }
}