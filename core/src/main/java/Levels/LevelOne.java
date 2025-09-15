package Levels;

import com.badlogic.gdx.Gdx;

import Enemies.FairySpawn;

public class LevelOne extends Level {
	
	private int[] spawnPointWave1 = {(Gdx.graphics.getWidth()) - 16, 932};
	private int[] firstTarXYWave1 = {48, 600};
	private boolean shootsFirstW1 = false;
	
	private int[] spawnPointWave2 = {0, 932};
	private int[] firstTarXYWave2 = {1141,600};
	private boolean shootsFirstW2 = false;
	
	private int[] spawnPointWave3 = {(Gdx.graphics.getWidth()/2) - 16, 932};
	private int[] firstTarXYWave3 = {584,600};
	private boolean shootsFirstW3 = true;
	
	private int[] spawnPointWave4 = {(Gdx.graphics.getWidth()) - 16, 932};
	private int[] firstTarXYWave4 = {48,600};
	private boolean shootsFirstW4 = true;
	
	public LevelOne() {
		this.cantFairies = 10;
		this.cantWaves = 4;
		this.fairiesByWave = new int [] {2,3,3,2};
	}
	
	@Override
	public FairySpawn getCoordsCurrentWave(int currentWave) {
	    switch (currentWave) {
	        case 0: return new FairySpawn(spawnPointWave1[0], spawnPointWave1[1], firstTarXYWave1[0], firstTarXYWave1[1]);
	        case 1: return new FairySpawn(spawnPointWave2[0], spawnPointWave2[1], firstTarXYWave2[0], firstTarXYWave2[1]);
	        case 2: return new FairySpawn(spawnPointWave3[0], spawnPointWave3[1], firstTarXYWave3[0], firstTarXYWave3[1]);
	        case 3: return new FairySpawn(spawnPointWave4[0], spawnPointWave4[1], firstTarXYWave4[0], firstTarXYWave4[1]);
	        default: return new FairySpawn(600,600,600,600);
	    }
	}
	
	@Override
	public boolean getShootsFirstCurrrentWave(int currentWave) {
		switch (currentWave) {
			case 0: return shootsFirstW1;
			case 1: return shootsFirstW2;
			case 2: return shootsFirstW3;
			case 3: return shootsFirstW4;
			default: return true;
		}
	}
}
