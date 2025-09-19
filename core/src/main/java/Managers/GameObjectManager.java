package Managers;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Enemies.Boss;
import Factory.EnemyFactory;
import Factory.TouhouEnemyFactory;
import Reimu.Bullet;
import Reimu.Reimu;

public class GameObjectManager {
    private int correctas;
    
	// Personajes y Objetos de Personaje
	private SpriteBatch batch;
	private Reimu reimu;
	private Boss boss;
	private EnemyFactory eFactory = new TouhouEnemyFactory();
	
	// Managers de Personajes con comportamientos dinamicos (en este caso: Fairy)
	private FairyManager fairyMng = new FairyManager();
	private LevelManager levelMng = new LevelManager();
	private DropManager dropMng = new DropManager();
	private CollisionManager collisionMng = new CollisionManager();
	private BulletManager bulletMng = new BulletManager();
	
	private ArrayList<Bullet> reimuBullets = new ArrayList<>();
	
	private Random random = new Random();
	// ESTADO PARA CONTROLAR PANTALLA PREGUNTAS
	private boolean exerciseDone = false;
	private boolean fightBoss = false;
	private boolean checkRewards = false;
	
	public GameObjectManager(SpriteBatch batch, int nivel, int vidas, int score, int power) {
		this.batch = batch;
		levelMng.setCurrentLevel(nivel);
		//crear a Reimu ^_^
		reimu = new Reimu(Gdx.graphics.getWidth()/2-50,30,
				Gdx.audio.newSound(Gdx.files.internal("DEAD.ogg")), 
				new Texture(Gdx.files.internal("Rocket2.png")), 
				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        reimu.setVidas(vidas);
        reimu.setDamage(power);
        reimu.setScore(score);
        
        eFactory.setCurrentBulletManager(bulletMng);
        gameSetup();
	}                     
	
	public void update() {
		reimuBulletsDrawer();
		enemyBulletsDrawer();
		fairyDrawer();
		//fairiesAndBossDrawerUpdater();
		bossDrawer();
		dropMng.drawDrops(batch);
		enemyDropsCollisionManager();
		
		if (reimu.shieldExists() && !reimu.shieldExpired()) {
			reimu.drawShield(batch);
		}
		else {
			reimu.setShielded(false);
			reimu.removeShield();
		}
		
		//SUJETO A CAMBIO (UX): ELIMINAR BALAS Y RESPAWN
		if (!reimu.isHurt()) {
			reimuBulletsCollisionManager();
			enemyBulletsCollisionManager();
		}
		reimu.draw(batch, this);
	}
	
	public void gameSetup() {
		levelMng.whatLevelIsIt();
		System.out.println("Current Wave: "+levelMng.getCurrentLvlWave());
		//crear Fairies
	    fairyMng.fairySetup(levelMng.getFairiesCurrentWave(), levelMng.getFairyStartingPoint(), levelMng.getFairyIsShooting(), bulletMng);
	    levelMng.changeCurrentWave();
	    levelMng.areWavesOver();
        //crear boss
        boss = eFactory.craftBoss(levelMng.getLvlId());
        //System.out.println("Boss = "+isBossAlive());
	}
	
	/*
	 * FUNCIONES DE DRAW, UPDATE Y MANEJO DE OBJETOS EN PANTALLAJUEGO
	 * 
	 */
	
	public void reimuBulletsDrawer() {
		//dibujar balas
		for (int i = 0; i < reimuBullets.size(); i ++) {
			 Bullet b = reimuBullets.get(i); 
			 if (b.isDestroyed()) {
				 //System.out.println(b.isDestroyed());
				 reimuBullets.remove(b);
				 i--;
				 
			 }
			 b.draw(batch);
			 b.update();
		 }
	}
	
	public void reimuBulletsCollisionManager() {
		// COLISION DE BOSS VS BULLETS
		for (int i = 0; i < reimuBullets.size(); i++) {
			Bullet bullet = reimuBullets.get(i);
			if (collisionMng.chkColEnemyVsBullet(bullet, boss)) {
			    //System.out.println("Boss Health: "+boss.getHealth());
				if (collisionMng.isAliveAfterLastCol(boss) == null) {
					boss = null;
					reimuBullets.clear();
					reimu.addScore(1000);
					break;
				}
			}
		
			// COLISION DE FAIRIES VS BULLETS
			for (int j = 0; j < fairyMng.getFairiesSize(); j++) {
			    if (collisionMng.chkColEnemyVsBullet(bullet,fairyMng.getFairy(j))) {
			        // If the fairy's health reaches zero, remove it and play sound
			        if (collisionMng.isAliveAfterLastCol(fairyMng.getFairy(j)) == null) {
			            fairyMng.getFairy(j).playExplosionSound();
			            dropMng.spawnDrop(fairyMng.getFairy(j).getSpr().getX(),fairyMng.getFairy(j).getSpr().getY());
			            fairyMng.removeFairy(j);
			            fairyMng.reduceCurrentNumFairies();  // Decrement the current number of fairies
			            j--;  // Adjust the index after removing a fairy
			            reimu.addScore(100);  // Increment the score
			        }
			    }
			    
			    if (bullet.isDestroyed()) {
					reimuBullets.remove(i);
					i--;  // Adjust index after removing bullet
					break;  // Exit bullet loop after removal to avoid index issues
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
	
	public void enemyBulletsDrawer() {bulletMng.enemyBulletsDrawer(batch);}
	
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
			fairyMng.fairiesDrawer(batch);
		}
		else if (fairyMng.isFairiesEmpty() && !levelMng.areWavesOver()) {
			System.out.println("Current Wave: "+levelMng.getCurrentLvlWave());
			fairyMng.fairySetup(levelMng.getFairiesCurrentWave(), levelMng.getFairyStartingPoint(), levelMng.getFairyIsShooting(),bulletMng);
			levelMng.changeCurrentWave();
		    levelMng.areWavesOver();
		    System.out.println("Fairies = "+fairyMng.getFairiesSize());
		}
	}
	
	public void bossDrawer() {
		if (fairyMng.isFairiesEmpty() && levelMng.areWavesOver()) {
			// SUJETO A CAMBIO IMPORTANTISIMO: AGREGAR PANTALLA CON PREGUNTAS Y PAUSAR EL JUEGO MOMENTANEAMENTE
			if (!checkRewards) {
				applyRewards();
				checkRewards = true;
				System.out.println("semen");
			}
			//System.out.println("Boss vivo? ");
		 	//UNLEASH THE BOSS
			boss.enemyRoutine(batch);
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
			System.out.println("correctas < 5");
			for (int i = 0; i < 2; i++) {reimu.oneUp();}
			reimu.addDamage(30);
			boss.lowerBossHealthNSpeed(random.nextInt(4, 6), random.nextInt(4, 6));
			//boss.setHealthChoice();
			//boss.setSpeedChoice();
			System.out.println(boss.getHealthChoice());
			System.out.println(boss.getSpeedChoice());
			System.out.println("Boss Health ="+boss.getHealth());
			System.out.println("Boss Speed ="+boss.getSpeed());
			System.out.println("correctas post pantalla juego = "+correctas);
			
		}
		else {
			System.out.println("else");
			for (int i = 0; i < 3; i++) {reimu.oneUp();}
			reimu.addDamage(50);
			boss.lowerBossHealthNSpeed(random.nextInt(4, 6), random.nextInt(4, 6));
		}
	}
	
	/*
	 * FUNCIONES QUE RECIBEN OBJETO EnemyBullet O Bullet PARA AGREGARLOS AL ARRAYLIST QUE MANEJA SU EXISTENCIA EN PANTALLAJUEGO 
	 */
	
    public boolean agregarReimuBullets(Bullet bb) {return reimuBullets.add(bb);}
    //public void agregarEnemyBullets(EnemyBullet eb) {enemyBullets.add(eb);}
    
    public void setScore(int score) {reimu.setScore(score);}
    public void setFightBoss(boolean b) {this.fightBoss = b;}
    public void setCorrectas(int c) {correctas = c;}
    
    public int getReimuVidas() {return reimu.getVidas();}
    public int getScore() {return reimu.getScore();}
    public int getReimuDamage() {return reimu.getDamageBala();}
    
    public boolean isReimuDead() {return reimu.estaDestruido();}
    public boolean areWeFightingBoss() {return fightBoss;}
    public boolean areWavesOver() {return levelMng.areWavesOver();}
    
    public boolean isBossAlive() {
    	if (boss == null) {
    		return false;
    	}
    	return true;
    }
    
    public boolean AreFairiesAlive() {
    	if (fairyMng.isFairiesEmpty()) {
    		return false;
    	}
    	return true;
    }
    
    public boolean readyToExercise() {return (!AreFairiesAlive() && levelMng.areWavesOver());}
    
    public void setExerciseDone(boolean s) {
    	this.exerciseDone = s;
    }
    public boolean getExerciseState() { return exerciseDone; }
}