package Factory;

import Enemies.Boss;
import Enemies.Fairy;
import Managers.BulletManager;

public interface EnemyFactory {
	void setCurrentBulletManager(BulletManager bulletMng);
	Boss craftBoss(int bossTx);
	Fairy craftFairy(int spawnPointX, int spawnPointY, int firstTargetX, int firstTargetY, boolean IsShooting);
	
}
