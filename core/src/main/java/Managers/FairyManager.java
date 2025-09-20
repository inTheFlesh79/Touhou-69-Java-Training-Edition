package Managers;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import BulletHellPatterns.BulletHellPattern;
import BulletHellPatterns.CirclePattern;
import BulletHellPatterns.DynamicSpiralPattern;
import BulletHellPatterns.ForkPattern;
import BulletHellPatterns.SpiralPattern;
import Enemies.Enemy;
import Enemies.Fairy;
import Enemies.FairySpawn;
import Factory.EnemyFactory;
import Factory.TouhouEnemyFactory;

public class FairyManager extends EnemyManager{
	private EnemyFactory eFactory = new TouhouEnemyFactory();
	private int[] spawnSpeedOptions = {1600, 1700};
	private int[] speedOptions = {600,650};
	private int[] healthOptions = {2, 10, 18};
	private Random random = new Random();
	private ArrayList<Fairy> fairies = new ArrayList<>();
	private Texture spriteSheet;           // shared
    private TextureRegion[][] spriteRegions; // shared
	
	public FairyManager() {
		spriteSheet = new Texture(Gdx.files.internal("Fairies.png"));
		spriteRegions = TextureRegion.split(spriteSheet, 32, 32);
		bhpType.add(new SpiralPattern());
		bhpType.add(new DynamicSpiralPattern());
		bhpType.add(new CirclePattern());
		bhpType.add(new ForkPattern());
	}
	
	public void fairiesDrawer(SpriteBatch batch) {
	    for (int i = 0; i < fairies.size(); i++) {
	        if (i == 0) {fairies.get(i).setIsShootSoundAllowed(true);}
	        fairies.get(i).enemyRoutine(batch);
	    }
	}
	
	public void fairySetup(int cantFairiesCurrentWave, FairySpawn spawn, boolean isShooting, BulletManager bulletMng) {
		for (int i = 0; i < cantFairiesCurrentWave; i++) {
			eFactory.setCurrentBulletManager(bulletMng);
			Fairy f = eFactory.craftFairy(spawn.getSpawnX(), spawn.getSpawnY(), spawn.getTargetX(), spawn.getTargetY(), isShooting, spriteRegions);
        	fairies.add(f);
		}
		
		// manage the fairies elements with FairyManager
		int bhpChoice = random.nextInt(getBhpTypeSize());
        int speedChoice = random.nextInt(getCantSpeedOptions());
        int spawnSpeedChoice = random.nextInt(getCantSpawnSpeedOptions());
        int healthChoice = random.nextInt(getCantHealthOptions());
        
        for (int i = 0; i < cantFairiesCurrentWave; i++) {  // Include index 0 as valid
        	fairies.get(i).setSpeedChoice(speedChoice);
        	System.out.println("Fairy"+i);
        	manageSpawnSpeed(fairies.get(i), spawnSpeedChoice);
            manageBHPType(fairies.get(i), bhpChoice);
            manageHealth(fairies.get(i), healthChoice);
        }
        
		// update for next function call/new wave
		//levelMng.changeCurrentWave();
		//levelMng.areWavesOver();
		System.out.println("Fairies = "+fairies.size());
	}
	@Override
	public void manageBHPType(Enemy e, int choice) {
		BulletHellPattern bhpChosen = bhpType.get(choice);
		e.setBulletPattern(bhpChosen);
	}
	
	@Override
	public void manageSpeed(Enemy e, int choice) {
		e.setSpeed(speedOptions[choice]);
	}
	
	@Override
	public void manageHealth(Enemy e, int choice) {
		e.setHealth(healthOptions[choice]);
	}
	
	public void manageSpawnSpeed(Enemy e, int spawnSpeedChoice) {
		e.setSpeed(spawnSpeedOptions[spawnSpeedChoice]);
	}
	
	public int getCantSpeedOptions() {return speedOptions.length;}
	public int getCantHealthOptions() {return healthOptions.length;}
	public int getCantSpawnSpeedOptions() {return spawnSpeedOptions.length;}
	public int getFairiesSize() {return fairies.size();}
	public Fairy getFairy(int choice) {return fairies.get(choice);}
	public boolean isFairiesEmpty() {return fairies.isEmpty();}
	public void removeFairy(int choice) {fairies.remove(choice);}
	
	public void dispose() {spriteSheet.dispose();}
}