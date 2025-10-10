package BulletHellPatterns;

import Enemies.EnemyBullet;

public interface TwoStagePattern {
	public void applyMultiStage(EnemyBullet eb);
	public boolean hasDeceleration();
}
