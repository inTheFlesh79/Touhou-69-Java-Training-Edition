package Managers;

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
  		    r.setHurtTime(r.getMaxHurtTime());
  		    r.playHurtSound();
            if (r.getLives()<=0) 
          	    r.setDead(true);
            return true;
        }
        return false;
	}
	
	public boolean chkColReimuVsDrop(Drop d, Reimu r) {
		return (d.getHitbox().overlaps(r.getSpr().getBoundingRectangle()));
	}
	
	public boolean chkColReimuVsEnemy(Reimu r, Enemy e) {
		if(!r.isHurt() && r.getSprHitbox().overlaps(e.getCircleHitbox()) && !r.isShielded()){
			r.oneDown();;
            r.setHurt(true);;
  		    r.setHurtTime(r.getMaxHurtTime());
  		    r.playHurtSound();
            if (r.getLives()<=0) 
          	    r.setDead(true);
            return true;
		}
		return false;
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