package Managers;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import Enemies.Enemy;
import Enemies.Fairy;
import Enemies.FairySpawn;
import Factory.EnemyFactory;
import Factory.TouhouEnemyFactory;

public class FairyManager extends EnemyManager{
	private EnemyFactory eFactory = new TouhouEnemyFactory();
	private int[] spawnSpeedOptions = {1600, 1700};
	private int[] speedOptions = {450,550};
	private int[] healthOptions = {30,35,40};
	private Random random = new Random();
	private ArrayList<Fairy> fairies = new ArrayList<>();
    private ArrayList<Fairy> pendingFairies = new ArrayList<>();
    private float spawnDelay = 0.35f; // seconds between spawns
    private float spawnTimer = 0f;
	private int spawnIndex = 0;        // total planned this wave
    private float baseSpacing = 12f;    // world units; we scale this from scrWidth in setup
	private Texture spriteSheet;           // shared
    private TextureRegion[][] spriteRegions; // shared
	
	public FairyManager() {
		spriteSheet = new Texture(Gdx.files.internal("Fairies.png"));
		spriteRegions = TextureRegion.split(spriteSheet, 32, 32);
	}
	
    // called from GOM	 each frame: fairyMng.fairiesDrawer(batch, scrWidth, scrHeight);
    public void fairiesDrawer(SpriteBatch batch, float scrWidth, float scrHeight) {
        float delta = Gdx.graphics.getDeltaTime();

        // spawn additional pending fairies according to spawnDelay
        if (!pendingFairies.isEmpty()) {
            spawnTimer += delta;
            while (spawnTimer >= spawnDelay && !pendingFairies.isEmpty()) {
                spawnNext(scrWidth, scrHeight);
                spawnTimer = 0f;
            }
        }

        // update + draw active fairies; remove destroyed ones
        for (int i = 0; i < fairies.size(); i++) {
            Fairy f = fairies.get(i);
            if (i == 0) f.setIsShootSoundAllowed(true);
            f.enemyRoutine(batch, scrWidth, scrHeight);
            //System.out.println("tp? ="+f.isTargetedPattern());
        }
    }
	
    // --- called from GOM (modified to receive scrWidth/scrHeight) ---
    public void fairySetup(int cantFairiesCurrentWave, FairySpawn spawn, boolean isShooting, BulletManager bulletMng, float scrWidth, float scrHeight) {
        pendingFairies.clear();
        fairies.clear(); // optional: clear old wave entities if you want to wipe previous wave

        // scale spacing relative to viewport so spread looks consistent across resolutions
        baseSpacing = Math.max(30f, scrWidth * 0.0125f); // ~1.25% of world width or 8 units min

        // create pending fairies (do not add to active list yet)
        for (int i = 0; i < cantFairiesCurrentWave; i++) {
            eFactory.setCurrentBulletManager(bulletMng);
            Fairy f = eFactory.craftFairy(spawn.getSpawnX(), spawn.getSpawnY(), spawn.getTargetX(), spawn.getTargetY(), isShooting, spriteRegions);
            pendingFairies.add(f);
        }

        // apply manager settings for the whole wave (same as before)
        int bhpChoice = random.nextInt(bulletMng.getBhpArrSize()	);
        int speedChoice = random.nextInt(getCantSpeedOptions());
        int spawnSpeedChoice = random.nextInt(getCantSpawnSpeedOptions());
        int healthChoice = random.nextInt(getCantHealthOptions());

        for (Fairy f : pendingFairies) {
            f.setSpeedChoice(speedChoice);
            manageSpawnSpeed(f, spawnSpeedChoice);
            f.setBhpChoice(bhpChoice);
            manageHealth(f, healthChoice);
        }

        // reset spawn counters/timers and spawn the first fairy immediately
        spawnIndex = 0;
        spawnTimer = 0f;

        // spawn first one immediately so GOM sees non-empty manager
        if (!pendingFairies.isEmpty()) {
            spawnNext(scrWidth, scrHeight);
        }
    }
    
    // internal spawn helper: pops one from pending and adds to active with an offset target
    private void spawnNext(float scrWidth, float scrHeight) {
        if (pendingFairies.isEmpty()) return;

        Fairy f = pendingFairies.remove(0);

        // compute direction from spawn -> target in world units
        Vector2 dir = new Vector2(f.getTargetX() - f.getSpr().getX(), f.getTargetY() - f.getSpr().getY());
        if (dir.len() == 0f) dir.set(0, 1); // fallback up
        dir.nor();

        // perpendicular vector (left/right)
        Vector2 perp = new Vector2(-dir.y, dir.x);

        // alternating left/right spacing that grows with index: 0, +1, -1, +2, -2, ...
        int pair = spawnIndex / 2 + 1;
        int sign = (spawnIndex % 2 == 0) ? 1 : -1;
        float magnitude = pair * baseSpacing;

        Vector2 offset = new Vector2(perp).scl(sign * magnitude);

        // apply offset to target so the fairy flies slightly offset from the main target
        float newTargetX = f.getTargetX() + offset.x;
        float newTargetY = f.getTargetY() + offset.y;
        f.setTarget(newTargetX, newTargetY);

        // finally add to active list
        fairies.add(f);
        spawnIndex++;
    }
	
	@Override
	public void manageSpeed(Enemy e, int choice) {
		e.setDefaultSpeed(speedOptions[choice]);
		e.setSpeed(speedOptions[choice]);
	}
	
	@Override
	public void manageHealth(Enemy e, int choice) {
		e.setHealth(healthOptions[choice]);
	}
	
	public void manageSpawnSpeed(Enemy e, int spawnSpeedChoice) {
		e.setSpeed(spawnSpeedOptions[spawnSpeedChoice]);
		e.setDefaultSpeed(speedOptions[spawnSpeedChoice]);
		System.out.println("def speed fairy"+e.getDefSpeed());
		System.out.println("current speed fairy"+e.getSpeed());
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