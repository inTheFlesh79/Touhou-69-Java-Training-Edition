package Managers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import BulletHellPatterns.*;
import Enemies.EnemyBullet;
import Reimu.Bullet;
import Reimu.Reimu;

public class BulletManager {
	private ArrayList<BulletHellPattern> bhpTypes = new ArrayList<>();
	private ArrayList<BulletHellPattern> tsBhpTypes = new ArrayList<>();
	private ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
	private ArrayList<Bullet> reimuBullets = new ArrayList<>();
	private Texture enemyBulletSheet;           // shared
    private TextureRegion[][] enemyBulletRegions; // shared
    private Reimu player;
    private int currentTSBhpChoice;
    private boolean shiftBullets = false;
	
	public BulletManager () {
		enemyBulletSheet = new Texture(Gdx.files.internal("bulletTypes.png"));
        enemyBulletRegions = TextureRegion.split(enemyBulletSheet, 24, 30);
        bhpTypes.add(new SpiralPattern()); // 0
        bhpTypes.add(new DynamicSpiralPattern()); // 1
      	bhpTypes.add(new CirclePattern()); // 2
      	bhpTypes.add(new ForkPattern()); // 3
      	bhpTypes.add(new TargetedPattern()); // 4
      	bhpTypes.add(new FastTargetedPattern()); // 5
      	bhpTypes.add(new HeptaSprayPattern()); // 6
      	bhpTypes.add(new SlowTargetedPattern()); //7
      	bhpTypes.add(new PredictionShotPattern());
      	
      	tsBhpTypes.add(new TargetedCirclePattern());
      	tsBhpTypes.add(new RandomHexaSpiralPattern());
      	tsBhpTypes.add(new RotatingForkPattern());
      	tsBhpTypes.add(new DualOrbitPattern());
      	tsBhpTypes.add(new ConvergingSpiralPattern());
	}
	
	public void enemyBulletsDrawer(SpriteBatch batch, float scrWidth, float scrHeight) {
		for (int i = 0; i < enemyBullets.size(); i ++) {
			 EnemyBullet eb = enemyBullets.get(i); 
			 if (eb.isDestroyed()) {
				enemyBullets.remove(eb);
				i--;
				//System.out.println(eb.isDestroyed());
				//System.out.println("Removed from EBullets ArrayList");
			 }
			 eb.draw(batch);
			 eb.update(scrWidth, scrHeight);
		}
		if (shiftBullets) {System.out.println("shiftbullets");shiftBullets();}
		
	}
	
	public void reimuBulletsDrawer(SpriteBatch batch, float scrWidth, float scrHeight) {
		for (int i = 0; i < reimuBullets.size(); i ++) {
			 Bullet b = reimuBullets.get(i); 
			 if (b.isDestroyed()) {
				 reimuBullets.remove(b);
				 i--;
			 }
			 b.draw(batch);
			 b.update(scrWidth, scrHeight);
		 }
	}
	
	public void generateEBullets(int bhpChoice, float x, float y, boolean multiStage) {
	    if (multiStage) {
	    	BulletHellPattern pattern = tsBhpTypes.get(bhpChoice);
	    	currentTSBhpChoice = bhpChoice;
	    	for (int i = 0; i < pattern.getCantBullet(); i++) {
		    	EnemyBullet genEBullet = craftEnemyBullet(x + 20, y + 16);
		    	genEBullet.setTwoStagedBullet(true);
		    	pattern.generateBulletInPattern(x + 16, y + 16, genEBullet);
		        addEnemyBullets(genEBullet);
		        System.out.println("Shift bullets for ID " + i + " (" + enemyBullets.size() + " bullets total)");
	    	}
	    }
	    else {
	    	BulletHellPattern pattern = bhpTypes.get(bhpChoice);
	    	if (pattern instanceof PlayerTrackingPattern) {
		    	((PlayerTrackingPattern) pattern).setReimuCoords(player.getX(),player.getY());
		    }
	    	// --- Generate new bullets ---
		    for (int i = 0; i < pattern.getCantBullet(); i++) {
		        EnemyBullet genEBullet = craftEnemyBullet(x + 20, y + 16);
		        pattern.generateBulletInPattern(x + 16, y + 16, genEBullet);
		        addEnemyBullets(genEBullet);
		    }
	    }
	}
	
	public EnemyBullet craftEnemyBullet(float x, float y) {return new EnemyBullet(x, y, enemyBulletRegions);}
	
	public int getEnemyBulletsSize() { return enemyBullets.size(); }
	public EnemyBullet getEnemyBullet(int choice) { return enemyBullets.get(choice); }
	public boolean isEnemyBulletsEmpty() { return enemyBullets.isEmpty(); }
	public void removeEnemyBullet(int choice) { enemyBullets.remove(choice);}
	public void addEnemyBullets(EnemyBullet eb) {enemyBullets.add(eb);}
	public void clearEnemyBullets() {enemyBullets.clear();}
	
	public int getReimuBulletsSize() { return reimuBullets.size(); }
	public Bullet getReimuBullet(int choice) { return reimuBullets.get(choice); }
	public boolean isReimuBulletsEmpty() { return reimuBullets.isEmpty(); }
	public void removeReimuBullet(int choice) {reimuBullets.remove(choice);}
	public void addReimuBullets(Bullet b) {reimuBullets.add(b);}
	public void clearReimuBullets() {reimuBullets.clear();}
	
	//BHP ArrayList Methods
	public int getBhpArrSize() {return bhpTypes.size();}
	public int getTsBhpArrSize() {return tsBhpTypes.size();}
	public BulletHellPattern getBHP(int choice) {return bhpTypes.get(choice);}
	public BulletHellPattern getTSBHP (int choice) {return tsBhpTypes.get(choice);}
	public float getBHPMaxShootingTime(int bhpChoice) {return bhpTypes.get(bhpChoice).getMaxShootingTime();}
	
	public void setReimu(Reimu r) {player = r;}
	public TextureRegion[][] getEnemyBulletSprSheet() {return enemyBulletRegions;}
	public void dispose() {enemyBulletSheet.dispose();}
	
	// MultiStage Pattern Management 
	public void setTSBhpChoice(int bhpChoice) {currentTSBhpChoice = bhpChoice;}
	public void setShiftBullets (boolean bool) {shiftBullets = bool;}
	public boolean getShiftBullets() {return shiftBullets;}
	public void shiftBullets() {
		TwoStagePattern pattern = (TwoStagePattern) tsBhpTypes.get(currentTSBhpChoice);
		for (int i = 0; i < enemyBullets.size(); i ++) {
		    if (pattern instanceof PlayerTrackingPattern) {
		    	((PlayerTrackingPattern) pattern).setReimuCoords(player.getX(),player.getY());
		    }
			EnemyBullet eb = enemyBullets.get(i);
			if (eb.getTwoStagedBullet()) {
				pattern.applyMultiStage(eb);
			} 
		}
		shiftBullets = false;
	}
	
	public void bulletShiftingHandler() {
		TwoStagePattern pattern = (TwoStagePattern) tsBhpTypes.get(currentTSBhpChoice);
		if (pattern.hasDeceleration()) {
			
		}
			if (shiftBullets) {shiftBullets();}
	}
	
	public void decelerateBullets(float decelRate) {
	    final float dt = Gdx.graphics.getDeltaTime();
	    if (dt <= 0f) return;

	    // Tunable: how many units of speed are removed per second
	    final float DECEL_RATE = decelRate; // tweak to taste
	    final float STOP_THRESHOLD = 1f; // below this speed we consider it stopped

	    for (int i = 0; i < enemyBullets.size(); i++) {
	        EnemyBullet eb = enemyBullets.get(i);
	        // skip bullets that are not flagged as two-staged
	        // (you said bullets already have getTwoStagedBullet())
	        if (!eb.getTwoStagedBullet()) continue;

	        float vx = eb.getVelocityX();
	        float vy = eb.getVelocityY();
	        float speed = (float) Math.sqrt(vx * vx + vy * vy);

	        if (speed <= STOP_THRESHOLD) {
	            // stop the bullet completely
	            eb.setVelocityX(0f);
	            eb.setVelocityY(0f);
	            eb.setStopped(true);
	            continue;
	        }

	        // reduce speed by DECEL_RATE * dt (keeps direction)
	        float newSpeed = speed - DECEL_RATE * dt;
	        if (newSpeed <= 0f) {
	            eb.setVelocityX(0f);
	            eb.setVelocityY(0f);
	            eb.setStopped(true);
	        } else {
	            float nx = vx / speed;
	            float ny = vy / speed;
	            eb.setVelocityX(nx * newSpeed);
	            eb.setVelocityY(ny * newSpeed);
	        }
	    }
	}
	
	public boolean isDecelerationOver() {
		for (int i = 0; i < enemyBullets.size(); i++) {
			EnemyBullet eb = enemyBullets.get(i);
			if (eb.getTwoStagedBullet() && !eb.getStopped()) {return false;}
		}
		return true;
	}
}