package Levels;

import com.badlogic.gdx.Gdx;

import Enemies.FairySpawn;

public class LevelFour extends Level {
	private int[] spawnPointWave1 = {(Gdx.graphics.getWidth()) - 16, 932};
	private int[] firstTarXYWave1 = {1000, 740};
	private boolean shootsFirstW1 = true;
	
	private int[] spawnPointWave2 = {0, 932};
	private int[] firstTarXYWave2 = {150,890};
	private boolean shootsFirstW2 = false;
	
	private int[] spawnPointWave3 = {(Gdx.graphics.getWidth()/2) - 16, 932};
	private int[] firstTarXYWave3 = {584,800};
	private boolean shootsFirstW3 = true;
	
	private int[] spawnPointWave4 = {(Gdx.graphics.getWidth()) - 16, 932};
	private int[] firstTarXYWave4 = {198,700};
	private boolean shootsFirstW4 = false;
	
	private int[] spawnPointWave5 = {(Gdx.graphics.getWidth()) - 16, 932};
	private int[] firstTarXYWave5 = {780,600};
	private boolean shootsFirstW5 = false;
	
	private int[] spawnPointWave6 = {(Gdx.graphics.getWidth()) - 16, 932};
	private int[] firstTarXYWave6 = {430,790};
	private boolean shootsFirstW6 = true;
	
	private int[] spawnPointWave7 = {(Gdx.graphics.getWidth()) - 16, 932};
	private int[] firstTarXYWave7 = {1060,650};
	private boolean shootsFirstW7 = true;
	
	public LevelFour() {
		this.levelId = 3;
		this.bossChoice = 3;
		this.cantFairies = 18;
		this.cantWaves = 7;
		this.fairiesByWave = new int [] {2,4,2,4,3,2,1};
	}
	
	@Override
	public FairySpawn getCoordsCurrentWave(int currentWave) {
	    switch (currentWave) {
	        case 0: return new FairySpawn(spawnPointWave1[0], spawnPointWave1[1], firstTarXYWave1[0], firstTarXYWave1[1]);
	        case 1: return new FairySpawn(spawnPointWave2[0], spawnPointWave2[1], firstTarXYWave2[0], firstTarXYWave2[1]);
	        case 2: return new FairySpawn(spawnPointWave3[0], spawnPointWave3[1], firstTarXYWave3[0], firstTarXYWave3[1]);
	        case 3: return new FairySpawn(spawnPointWave4[0], spawnPointWave4[1], firstTarXYWave4[0], firstTarXYWave4[1]);
	        case 4: return new FairySpawn(spawnPointWave5[0], spawnPointWave5[1], firstTarXYWave5[0], firstTarXYWave5[1]);
	        case 5: return new FairySpawn(spawnPointWave6[0], spawnPointWave6[1], firstTarXYWave6[0], firstTarXYWave6[1]);
	        case 6: return new FairySpawn(spawnPointWave7[0], spawnPointWave7[1], firstTarXYWave7[0], firstTarXYWave7[1]);
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
			case 4: return shootsFirstW5;
			case 5: return shootsFirstW6;
			case 6: return shootsFirstW7;
			default: return true;
		}
	}
	

}
