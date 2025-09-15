package Managers;

import java.util.ArrayList;

import BulletHellPatterns.BulletHellPattern;
import Enemies.Enemy;

public abstract class EnemyManager {
	protected ArrayList<BulletHellPattern> bhpType = new ArrayList<>();
	
	public abstract void manageBHPType(Enemy e, int choice);
	public abstract void manageSpeed(Enemy e, int choice);
	public abstract void manageHealth(Enemy e, int choice);
	public int getBhpTypeSize() {return bhpType.size();}
}
