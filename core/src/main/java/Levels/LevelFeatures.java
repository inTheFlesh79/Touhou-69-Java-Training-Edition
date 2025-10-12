package Levels;

import Enemies.FairySpawn;

public interface LevelFeatures {
	public FairySpawn getCoordsCurrentWave(int currentWave);
	public boolean getShootsFirstCurrrentWave(int currentWave);
	public float getWaveSpawnCooldown(int currentWave);
}
