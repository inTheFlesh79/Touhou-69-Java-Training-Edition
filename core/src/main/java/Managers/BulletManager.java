package Managers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Enemies.EnemyBullet;
import Reimu.Bullet;

public class BulletManager {
	private ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
	private ArrayList<Bullet> reimuBullets = new ArrayList<>();
	
	private Texture enemyBulletSheet;           // shared
    private TextureRegion[][] enemyBulletRegions; // shared
	
	public BulletManager () {
		enemyBulletSheet = new Texture(Gdx.files.internal("bulletTypes.png"));
        enemyBulletRegions = TextureRegion.split(enemyBulletSheet, 24, 30);
	}
	
	public void enemyBulletsDrawer(SpriteBatch batch) {
		for (int i = 0; i < enemyBullets.size(); i ++) {
			 EnemyBullet eb = enemyBullets.get(i); 
			 if (eb.isDestroyed()) {
				 
				 enemyBullets.remove(eb);
				 i--;
				 
				System.out.println(eb.isDestroyed());
				System.out.println("Removed from EBullets ArrayList");
			 }
			 eb.draw(batch);
			 eb.update();
		}
	}
	
	public void reimuBulletsDrawer(SpriteBatch batch) {
		for (int i = 0; i < reimuBullets.size(); i ++) {
			 Bullet b = reimuBullets.get(i); 
			 if (b.isDestroyed()) {
				 reimuBullets.remove(b);
				 i--;
			 }
			 b.draw(batch);
			 b.update();
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
	
	public TextureRegion[][] getEnemyBulletSprSheet() {return enemyBulletRegions;}
	public void dispose() {enemyBulletSheet.dispose();}
}