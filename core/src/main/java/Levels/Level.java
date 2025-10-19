package Levels;

import Enemies.FairySpawn;

public class Level implements LevelFeatures {
	protected int levelId;
	protected int bossChoice;
	protected int cantWaves;
	protected int cantFairies;
	protected int[] fairiesByWave;
	
	// To be Override
	public FairySpawn getCoordsCurrentWave(int currentWave) {
	    return null;
	}
	
	// To be Override
	public boolean getShootsFirstCurrrentWave(int currentWave) {
		return true;
	}
	
	// To be Override
	public float getWaveSpawnCooldown(int currentWave) {
		return 0;
	}
	
	public int getBhpCurrentWave(int currentWave) {
		return 0;
	}
	
	public int getCantFairies() {return cantFairies;}
	public int getCantWaves() {return cantWaves;}
	public int getCantFairiesSpecificWave(int wave) {return fairiesByWave[wave];}
	public int getLvlId() {return levelId;}
}
