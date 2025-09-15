package Factory;

import com.badlogic.gdx.Gdx;

import Enemies.Boss;
import Enemies.Fairy;
import Managers.GameObjectManager;

public class TouhouEnemyFactory implements EnemyFactory{
	private GameObjectManager gameMng;
	
	public TouhouEnemyFactory() {}
	
	@Override
	public void setCurrentObjectManager(GameObjectManager gameMng) {
		this.gameMng = gameMng;
	}
	
	@Override
	public Fairy craftFairy(int spawnPointX, int spawnPointY, int firstTargetX, int firstTargetY, boolean IsShooting) {
		Fairy newFairy = new Fairy(spawnPointX, spawnPointY, firstTargetX, firstTargetY, gameMng);
		newFairy.setIsShooting(IsShooting);
		return newFairy;
	}

	@Override
	public Boss craftBoss() {
		Boss newBoss = new Boss((Gdx.graphics.getWidth()/2) - 16, 932, gameMng);
		return newBoss;
	}
}
