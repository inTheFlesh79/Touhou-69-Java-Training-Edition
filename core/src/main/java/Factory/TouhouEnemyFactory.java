package Factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Enemies.Boss;
import Enemies.Fairy;
import Managers.BulletManager;

public class TouhouEnemyFactory implements EnemyFactory{
	private BulletManager bulletMng;
	
	public TouhouEnemyFactory() {}
	
	@Override
	public void setCurrentBulletManager(BulletManager bulletMng) {
		this.bulletMng = bulletMng;
	}
	
	@Override
	public Fairy craftFairy(int spawnPointX, int spawnPointY, int firstTargetX, int firstTargetY, boolean IsShooting, TextureRegion[][] spriteRegions) {
		Fairy newFairy = new Fairy(spawnPointX, spawnPointY, firstTargetX, firstTargetY, bulletMng, spriteRegions);
		newFairy.setIsShooting(IsShooting);
		return newFairy;
	}

	@Override
	public Boss craftBoss(int bossTx) {
		Boss newBoss = new Boss((Gdx.graphics.getWidth()/2) - 16, 932, bulletMng, bossTx);
		return newBoss;
	}
	
}