package Managers;

import Enemies.Enemy;

public abstract class EnemyManager {
	public abstract void manageSpeed(Enemy e, int choice);
	public abstract void manageHealth(Enemy e, int choice);
}