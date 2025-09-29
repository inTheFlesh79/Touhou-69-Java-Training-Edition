package Levels;

import com.badlogic.gdx.math.Vector2;

import Enemies.FairySpawn;

public class LevelOne extends Level {
    private Vector2 spawnPointWave1;
    private Vector2 firstTarXYWave1;
    private boolean shootsFirstW1 = true;

    private Vector2 spawnPointWave2;
    private Vector2 firstTarXYWave2;
    private boolean shootsFirstW2 = false;

    private Vector2 spawnPointWave3;
    private Vector2 firstTarXYWave3;
    private boolean shootsFirstW3 = false;

    private Vector2 spawnPointWave4;
    private Vector2 firstTarXYWave4;
    private boolean shootsFirstW4 = false;

    public LevelOne(float scrWidth, float scrHeight) {
        this.levelId = 0;
        this.bossChoice = 0;
        this.cantFairies = 10;
        this.cantWaves = 4;
        this.fairiesByWave = new int[] {1,3,3,2};

        // initialize Vectors using the provided world width/height
        spawnPointWave1 = new Vector2(scrWidth - 16f, 932f);
        firstTarXYWave1 = new Vector2(48f, 600f);

        spawnPointWave2 = new Vector2(230f, 1032f);
        firstTarXYWave2 = new Vector2(866f, 600f);

        spawnPointWave3 = new Vector2(scrWidth / 2f - 408f, 1032f);
        firstTarXYWave3 = new Vector2(460f, 600f);

        spawnPointWave4 = new Vector2(scrWidth - 16f, 932f);
        firstTarXYWave4 = new Vector2(108f, 720f);
    }

	@Override
	public FairySpawn getCoordsCurrentWave(int currentWave) {
	    switch (currentWave) {
	        case 0:
	            return new FairySpawn(
	                (int) spawnPointWave1.x, (int) spawnPointWave1.y,
	                (int) firstTarXYWave1.x, (int) firstTarXYWave1.y
	            );
	        case 1:
	            return new FairySpawn(
	                (int) spawnPointWave2.x, (int) spawnPointWave2.y,
	                (int) firstTarXYWave2.x, (int) firstTarXYWave2.y
	            );
	        case 2:
	            return new FairySpawn(
	                (int) spawnPointWave3.x, (int) spawnPointWave3.y,
	                (int) firstTarXYWave3.x, (int) firstTarXYWave3.y
	            );
	        case 3:
	            return new FairySpawn(
	                (int) spawnPointWave4.x, (int) spawnPointWave4.y,
	                (int) firstTarXYWave4.x, (int) firstTarXYWave4.y
	            );
	        default:
	            return new FairySpawn(600, 600, 600, 600);
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