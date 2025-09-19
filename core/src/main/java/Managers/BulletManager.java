package Managers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Enemies.EnemyBullet;

public class BulletManager {
	public BulletManager () {}
	
	private ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
	
	public void enemyBulletsDrawer(SpriteBatch batch) {
		for (int i = 0; i < enemyBullets.size(); i ++) {
			 EnemyBullet eb = enemyBullets.get(i); 
			 if (eb.isDestroyed()) {
				 //System.out.println(eb.isDestroyed());
				 eb.dispose();
				 enemyBullets.remove(eb);
				 i--;
			 }
			 eb.draw(batch);
			 eb.update();
		}
	}
	
	public int getEnemyBulletsSize() { return enemyBullets.size(); }
	public EnemyBullet getEnemyBullet(int choice) { return enemyBullets.get(choice); }
	public boolean isEnemyBulletsEmpty() { return enemyBullets.isEmpty(); }
	public void removeEnemyBullet(int choice) { enemyBullets.remove(choice);}
	public void addEnemyBullets(EnemyBullet eb) {enemyBullets.add(eb);}
}
