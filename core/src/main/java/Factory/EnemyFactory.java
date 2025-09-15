package Factory;

import Enemies.Boss;
import Enemies.Fairy;
import Managers.GameObjectManager;

public interface EnemyFactory {
	void setCurrentObjectManager(GameObjectManager gameMng);
	Boss craftBoss();
	Fairy craftFairy(int spawnPointX, int spawnPointY, int firstTargetX, int firstTargetY, boolean IsShooting);
}
