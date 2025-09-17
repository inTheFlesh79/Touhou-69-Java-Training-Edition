package Factory;

import com.badlogic.gdx.Gdx;

import Enemies.Boss;
import Enemies.Fairy;
import Managers.GameObjectManager;
import Reimu.Drop;
import Reimu.ScoreDrop;

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
	public Boss craftBoss(int bossTx) {
		Boss newBoss = new Boss((Gdx.graphics.getWidth()/2) - 16, 932, gameMng, bossTx);
		return newBoss;
	}
	
	public Drop generateDrop(float x, float y) {
        //float chance = MathUtils.random(); // returns float between 0 and 1

        //if (chance < 0.05f) {
            return new ScoreDrop(x,y); // 60% chance
        //}// else if (chance < 0.85f) {
            //return new OneUpDrop(x, y); // 25% chance
        //} else {
            //return new BombDrop(x, y); // 15% chance
        //}
        //return null;
    }
}
