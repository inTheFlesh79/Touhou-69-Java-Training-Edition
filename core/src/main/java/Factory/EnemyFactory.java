package Factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Enemies.Boss;
import Enemies.Fairy;
import Managers.BulletManager;

public interface EnemyFactory {
	void setCurrentBulletManager(BulletManager bulletMng);
	Boss craftBoss(int bossTx, float scrW, float scrH);
	Fairy craftFairy(int spawnPointX, int spawnPointY, int firstTargetX, int firstTargetY, boolean IsShooting, TextureRegion[][] spriteRegions);
	
}
