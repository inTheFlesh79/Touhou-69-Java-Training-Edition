package Managers;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import Enemies.Boss;
import Enemies.Enemy;
import Enemies.EnemyBullet;
import Enemies.Fairy;
import Enemies.FairySpawn;
import Factory.EnemyFactory;
import Factory.TouhouEnemyFactory;
import Reimu.Bullet;
import Reimu.Drop;
import Reimu.Reimu;
import puppy.code.PantallaJuego;

public class GameObjectManager {
    private int score;
    private float deltaTime;
    private Sound explosionSound;
	
	// Valores para el manejo dinamico de la cantidad total de personajes tipo Fairy
	private int currentNumFairies;
	//private boolean currentNumFairiesManaged = false;
	
	// Personajes y Objetos de Personaje
	private SpriteBatch batch;
	private Reimu reimu;
	private Boss boss;
	private EnemyFactory eFactory = new TouhouEnemyFactory();
	
	// Managers de Personajes con comportamientos dinamicos (en este caso: Fairy)
	private FairyManager fairyManager = new FairyManager();
	private LevelManager levelMng = new LevelManager();
	private DropManager dropMng = new DropManager();
	
	private ArrayList<Fairy> fairies = new ArrayList<>();
	private ArrayList<Bullet> reimuBullets = new ArrayList<>();
	private ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
	private ArrayList<Drop> enemyDrops = new ArrayList<>();
	
	
	private Random random = new Random();
	// ESTADO PARA CONTROLAR PANTALLA PREGUNTAS
	private boolean exerciseDone = false;
	private boolean fightBoss = false;
	
	public GameObjectManager(SpriteBatch batch, int nivel, int vidas, int score, PantallaJuego juego) {
		this.batch = batch;
		//this.juego = juego;
		levelMng.setCurrentLevel(nivel);
		
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("ATTACK3.mp3"));
		explosionSound.setVolume(1,0.5f);
		
		//crear a Reimu ^_^
		reimu = new Reimu(Gdx.graphics.getWidth()/2-50,30,
				Gdx.audio.newSound(Gdx.files.internal("DEAD.mp3")), 
				new Texture(Gdx.files.internal("Rocket2.png")), 
				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        reimu.setVidas(vidas);
        
        eFactory.setCurrentObjectManager(this);
        gameSetup();
	}
	
	public void update() {
		reimuBulletsDrawer();
		enemyBulletsDrawer();
		fairiesAndBossDrawerUpdater();
		enemyDropsDrawer();
		enemyDropsCollisionManager();
		//SUJETO A CAMBIO (UX): ELIMINAR BALAS Y RESPAWN
		if (!reimu.estaHerido()) {
			reimuBulletsCollisionManager();
			enemyBulletsCollisionManager();
		}
		reimu.draw(batch, this);
		//System.out.println("Are Fairy Waves Over = "+levelMng.areWavesOver());
	}
	
	public void gameSetup() {
		levelMng.whatLevelIsIt();
		System.out.println("Current Wave: "+levelMng.getCurrentLvlWave());
		//crear Fairies
	    fairySetup();
        
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
		 }
	}
	
	public void reimuBulletsCollisionManager() {
		// COLISION DE BOSS VS BULLETS
		for (int i = 0; i < reimuBullets.size(); i++) {
			Bullet bullet = reimuBullets.get(i);
			bullet.update();
			
			if (bullet.checkCollision(boss)) {
			    //System.out.println("Boss Health: "+boss.getHealth());
				if (boss.getHealth() <= 0) {
					explosionSound.play();
					boss = null;
					reimuBullets.clear();
					
					//SUJETO A CAMBIO: AGREGAR NUEVAS INSTANCIAS DE SCORE 
					score += 1000;
					break;
				}
			}
		
			// COLISION DE FAIRIES VS BULLETS
			for (int j = 0; j < fairies.size(); j++) {
				
			    if (bullet.checkCollision(fairies.get(j))) {
			        // If the fairy's health reaches zero, remove it and play sound
			        if (fairies.get(j).getHealth() <= 0) {
			            explosionSound.play();
			            agregarEnemyDrops(fairies.get(j));
			            fairies.remove(j);
			            currentNumFairies--;  // Decrement the current number of fairies
			            j--;  // Adjust the index after removing a fairy
			            score += 100;  // Increment the score
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
	
	// SUJETO A CAMBIO: ELIMINAR BALAS CUANDO OUT OF BOUNDS
	public void enemyBulletsDrawer() {
		for (int i = 0; i < enemyBullets.size(); i ++) {
			 EnemyBullet eb = enemyBullets.get(i); 
			 if (eb.isDestroyed()) {
				 //System.out.println(eb.isDestroyed());
				 eb.dispose();
				 enemyBullets.remove(eb);
				 i--;
				 
			 }
			 eb.draw(batch);
		 }
	}
	
	public void enemyBulletsCollisionManager() {
		for (int i = 0; i < enemyBullets.size(); i++) {
		    // System.out.println("Bullet index: " + i);
		    EnemyBullet eb = enemyBullets.get(i);
		    // System.out.println("");
		    eb.update();
		    if (reimu.checkCollision(eb)) {
		        enemyBullets.remove(i);
		    }
		}
	}
	
	// Drawer and Collission manager for Drop vs Reimu objects
	public void enemyDropsDrawer() {
		for (int i = 0; i < enemyDrops.size(); i++) {
			Drop d = enemyDrops.get(i);
			if (d.isDestroyed()) {
				d.dispose();
				enemyDrops.remove(i);
				i--;
			}
			d.draw(batch);
		}
	}
	
	public void enemyDropsCollisionManager() {
		for (int i = 0; i < enemyDrops.size(); i++) {
			Drop d = enemyDrops.get(i);
			d.update();
			if (reimu.checkCollision(d)) {
				if (dropMng.isScoreDrop(d)) {
					score+=500;
				}
				else {
					score+=100;
				}
				enemyDrops.remove(i);
			}
		}
			
	}
	
	// Drawer for Enemy objects
	public void fairiesAndBossDrawerUpdater() {
		if (!fairies.isEmpty()) {
		    // Draw and update all fairies that have been managed
	        for (int i = 0; i < currentNumFairies; i++) {
	        	fairies.get(i).enemyRoutine(batch);
	        }
		}
		else if (fairies.isEmpty() && !levelMng.areWavesOver()){
			System.out.println("Current Wave: "+levelMng.getCurrentLvlWave());
			fairySetup();
		}
		else if (fairies.isEmpty() && levelMng.areWavesOver()) {
			// SUJETO A CAMBIO IMPORTANTISIMO: AGREGAR PANTALLA CON PREGUNTAS Y PAUSAR EL JUEGO MOMENTANEAMENTE
			System.out.println("Boss vivo? "+isBossAlive());
		 	//UNLEASH THE BOSS
			boss.enemyRoutine(batch);
		}
	}
	
	public void fairySetup() {
		int cantFairiesCurrentWave = levelMng.getFairiesCurrentWave();
		for (int i = 0; i < cantFairiesCurrentWave; i++) {
			// create only the amount of fairies needed for the current wave
			FairySpawn spawn = levelMng.getFairyStartingPoint();
			boolean IsShooting = levelMng.getFairyIsShooting();
			Fairy f = eFactory.craftFairy(spawn.getSpawnX(), spawn.getSpawnY(), spawn.getTargetX(), spawn.getTargetY(), IsShooting);
        	fairies.add(f);
		}
		
		currentNumFairies = fairies.size();
		
		// manage the fairies elements with FairyManager
		int bhpChoice = random.nextInt(fairyManager.getBhpTypeSize());
        int speedChoice = random.nextInt(fairyManager.getCantSpeedOptions());
        int spawnSpeedChoice = random.nextInt(fairyManager.getCantSpawnSpeedOptions());
        int healthChoice = random.nextInt(fairyManager.getCantHealthOptions());
        
        for (int i = 0; i < cantFairiesCurrentWave; i++) {  // Include index 0 as valid
        	fairies.get(i).setSpeedChoice(speedChoice);
        	System.out.println("Fairy"+i);
        	fairyManager.manageSpawnSpeed(fairies.get(i), spawnSpeedChoice);
            fairyManager.manageBHPType(fairies.get(i), bhpChoice);
            fairyManager.manageHealth(fairies.get(i), healthChoice);
        }
        
		// update for next function call/new wave
		levelMng.changeCurrentWave();
		levelMng.areWavesOver();
		System.out.println("Fairies = "+fairies.size());
	}
	
	/*
	 * FUNCIONES QUE RECIBEN OBJETO EnemyBullet O Bullet PARA AGREGARLOS AL ARRAYLIST QUE MANEJA SU EXISTENCIA EN PANTALLAJUEGO 
	 */
	
    public boolean agregarReimuBullets(Bullet bb) {return reimuBullets.add(bb);}
    public void agregarEnemyBullets(EnemyBullet eb) {enemyBullets.add(eb);}
    
    // ARREGLAR CHANCES
    public void agregarEnemyDrops(Enemy fairy) {
        //float chance = MathUtils.random();
        Drop d = new Drop();
        d = dropMng.addDrop(fairy.getSpr().getX(), fairy.getSpr().getY());
        enemyDrops.add(d);
        d = dropMng.addExtraDrop(fairy.getSpr().getX(), fairy.getSpr().getY());
        enemyDrops.add(d);
    }
    
    public void setScore(int score) {this.score = score;}
    public void setFightBoss(boolean b) {this.fightBoss = b;}
    public void setDeltaTime(float dt) {this.deltaTime = dt;}
    
    public int getReimuVidas() {return reimu.getVidas();}
    public int getScore() {return score;}
    public boolean areWeFightingBoss() {return fightBoss;}
    
    public boolean isBossAlive() {
    	if (boss == null) {
    		return false;
    	}
    	return true;
    }
    
    public boolean AreFairiesAlive() {
    	if (fairies.isEmpty()) {
    		return false;
    	}
    	return true;
    }
    
    public boolean areWavesOver() {return levelMng.areWavesOver();}
    
    public boolean isReimuDead() {return reimu.estaDestruido();}
    public int getReimuDamage() {return reimu.getDamageBala();}
    
    public boolean readyToExercise() {return (!AreFairiesAlive() && levelMng.areWavesOver());}
    
    public void setExerciseDone(boolean s) {
    	this.exerciseDone = s;
    }
}