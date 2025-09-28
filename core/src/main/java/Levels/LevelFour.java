package Levels;

import com.badlogic.gdx.math.Vector2;

import Enemies.FairySpawn;

public class LevelFour extends Level {

    private Vector2 spawnPointWave1;
    private Vector2 firstTarXYWave1;
    private boolean shootsFirstW1 = true;

    private Vector2 spawnPointWave2;
    private Vector2 firstTarXYWave2;
    private boolean shootsFirstW2 = false;

    private Vector2 spawnPointWave3;
    private Vector2 firstTarXYWave3;
    private boolean shootsFirstW3 = true;

    private Vector2 spawnPointWave4;
    private Vector2 firstTarXYWave4;
    private boolean shootsFirstW4 = false;

    private Vector2 spawnPointWave5;
    private Vector2 firstTarXYWave5;
    private boolean shootsFirstW5 = false;

    private Vector2 spawnPointWave6;
    private Vector2 firstTarXYWave6;
    private boolean shootsFirstW6 = true;

    private Vector2 spawnPointWave7;
    private Vector2 firstTarXYWave7;
    private boolean shootsFirstW7 = true;

    public LevelFour(float scrWidth, float scrHeight) {
        this.levelId = 3;
        this.bossChoice = 3;
        this.cantFairies = 18;
        this.cantWaves = 7;
        this.fairiesByWave = new int [] {2,4,2,4,3,2,1};

        spawnPointWave1 = new Vector2(scrWidth - 16, 932);
        firstTarXYWave1 = new Vector2(1000, 740);

        spawnPointWave2 = new Vector2(0, 932);
        firstTarXYWave2 = new Vector2(150, 890);

        spawnPointWave3 = new Vector2((scrWidth / 2f) - 16, 932);
        firstTarXYWave3 = new Vector2(584, 800);

        spawnPointWave4 = new Vector2(scrWidth - 16, 932);
        firstTarXYWave4 = new Vector2(198, 700);

        spawnPointWave5 = new Vector2(scrWidth - 16, 932);
        firstTarXYWave5 = new Vector2(780, 600);

        spawnPointWave6 = new Vector2(scrWidth - 16, 932);
        firstTarXYWave6 = new Vector2(430, 790);

        spawnPointWave7 = new Vector2(scrWidth - 16, 932);
        firstTarXYWave7 = new Vector2(1060, 650);
    }

    @Override
    public FairySpawn getCoordsCurrentWave(int currentWave) {
        switch (currentWave) {
            case 0: return new FairySpawn((int)spawnPointWave1.x, (int)spawnPointWave1.y,
                                          (int)firstTarXYWave1.x, (int)firstTarXYWave1.y);
            case 1: return new FairySpawn((int)spawnPointWave2.x, (int)spawnPointWave2.y,
                                          (int)firstTarXYWave2.x, (int)firstTarXYWave2.y);
            case 2: return new FairySpawn((int)spawnPointWave3.x, (int)spawnPointWave3.y,
                                          (int)firstTarXYWave3.x, (int)firstTarXYWave3.y);
            case 3: return new FairySpawn((int)spawnPointWave4.x, (int)spawnPointWave4.y,
                                          (int)firstTarXYWave4.x, (int)firstTarXYWave4.y);
            case 4: return new FairySpawn((int)spawnPointWave5.x, (int)spawnPointWave5.y,
                                          (int)firstTarXYWave5.x, (int)firstTarXYWave5.y);
            case 5: return new FairySpawn((int)spawnPointWave6.x, (int)spawnPointWave6.y,
                                          (int)firstTarXYWave6.x, (int)firstTarXYWave6.y);
            case 6: return new FairySpawn((int)spawnPointWave7.x, (int)spawnPointWave7.y,
                                          (int)firstTarXYWave7.x, (int)firstTarXYWave7.y);
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