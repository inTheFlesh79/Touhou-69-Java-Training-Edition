package Managers;

import com.badlogic.gdx.graphics.g2d.Sprite;

import Enemies.Enemy;
import Enemies.EnemyBullet;
import Reimu.Bullet;
import Reimu.Drop;
import Reimu.Reimu;

public class CollisionManager {
	
	public CollisionManager () {}
	
	public boolean chkColEnemyVsBullet(Bullet b, Enemy e) {
		if(b.getSpr().getBoundingRectangle().overlaps(e.getSpr().getBoundingRectangle())) {
            b.setDestroyed(true);
            e.setHealth(e.getHealth() - b.getBulletDmg());
            return true;
        }
        return false;
	}
	
	public boolean chkColReimuVsEBullet(EnemyBullet eb, Reimu r) {
		if(!r.isHurt() && eb.getHitbox().overlaps(r.getSprHitbox()) && !r.isShielded()){
            r.oneDown();;
            r.setHurt(true);;
  		    r.setTHerido(r.getTHeridoMax());
  		    r.playHurtSound();
            if (r.getVidas()<=0) 
          	    r.setDestroyed(true);
            return true;
        }
        return false;
	}
	
	public boolean chkColReimuVsDrop(Drop d, Reimu r) {
		return (d.getHitbox().overlaps(r.getSpr().getBoundingRectangle()));
	}
	
	public boolean chkColShieldVsEBullet(EnemyBullet eb, Reimu r) {
		return eb.getHitbox().overlaps(r.getShieldHitbox());
	}
	
	public Enemy isAliveAfterLastCol(Enemy e) {
		if (e.getHealth() <= 0) {
			e.playExplosionSound();
			return null;
		}
		return e;
	}
	
}
