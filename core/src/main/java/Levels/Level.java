package Levels;

import Enemies.FairySpawn;

public class Level implements LevelFeatures {
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
	
	public int getCantFairies() {return cantFairies;}
	public int getCantWaves() {return cantWaves;}
	public int getCantFairiesSpecificWave(int wave) {return fairiesByWave[wave];}
}
