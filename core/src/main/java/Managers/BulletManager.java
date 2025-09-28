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
	private ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
	private ArrayList<Bullet> reimuBullets = new ArrayList<>();
	private Texture enemyBulletSheet;           // shared
    private TextureRegion[][] enemyBulletRegions; // shared
    private Reimu player;
	
	public BulletManager () {
		enemyBulletSheet = new Texture(Gdx.files.internal("bulletTypes.png"));
        enemyBulletRegions = TextureRegion.split(enemyBulletSheet, 24, 30);
        bhpTypes.add(new SpiralPattern());
        bhpTypes.add(new DynamicSpiralPattern());
      	bhpTypes.add(new CirclePattern());
      	bhpTypes.add(new ForkPattern());
      	bhpTypes.add(new TargetedPattern());
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
	
	public void generateEBullets(int bhpChoice, float x, float y) {
		BulletHellPattern pattern = bhpTypes.get(bhpChoice);
		
	    if (pattern instanceof TargetedPattern && player != null) {
	    	((TargetedPattern) pattern).setReimuCoords(player.getSpr().getX() + 16, player.getSpr().getY() + 24);
	    }
	    
		for (int i = 0; i < bhpTypes.get(bhpChoice).getCantBullet(); i++) {
			EnemyBullet generatedEBullet = craftEnemyBullet(x + 20, y + 16);
			pattern.generateBulletInPattern(x + 16, y + 16, generatedEBullet);
            addEnemyBullets(generatedEBullet);
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
	public BulletHellPattern getBHP(int choice) {return bhpTypes.get(choice);}
	
	public void setReimu(Reimu r) {player = r;}
	public TextureRegion[][] getEnemyBulletSprSheet() {return enemyBulletRegions;}
	public void dispose() {enemyBulletSheet.dispose();}
}