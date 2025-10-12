package Levels;

import com.badlogic.gdx.math.Vector2;

import Enemies.FairySpawn;

public class LevelThree extends Level {
    private Vector2 spawnPointWave1;
    private Vector2 firstTarXYWave1;
    private boolean shootsFirstW1 = true;
    private float waveSpawnCooldown1 = 3f;

    private Vector2 spawnPointWave2;
    private Vector2 firstTarXYWave2;
    private boolean shootsFirstW2 = true;
    private float waveSpawnCooldown2 = 0f;

    private Vector2 spawnPointWave3;
    private Vector2 firstTarXYWave3;
    private boolean shootsFirstW3 = false;
    private float waveSpawnCooldown3 = 2f;

    private Vector2 spawnPointWave4;
    private Vector2 firstTarXYWave4;
    private boolean shootsFirstW4 = true;
    private float waveSpawnCooldown4 = 0.5f;

    private Vector2 spawnPointWave5;
    private Vector2 firstTarXYWave5;
    private boolean shootsFirstW5 = false;
    private float waveSpawnCooldown5 = 1f;
    
    private Vector2 spawnPointWave6;
    private Vector2 firstTarXYWave6;
    private boolean shootsFirstW6 = true;
    private float waveSpawnCooldown6 = 0f;
    
    private Vector2 spawnPointWave7;
    private Vector2 firstTarXYWave7;
    private boolean shootsFirstW7 = false;
    private float waveSpawnCooldown7 = 1f;
    
    private Vector2 spawnPointWave8;
    private Vector2 firstTarXYWave8;
    private boolean shootsFirstW8 = true;
    private float waveSpawnCooldown8 = 3f;

    public LevelThree(float scrWidth, float scrHeight) {
        this.levelId = 2;
        this.bossChoice = 2;
        this.cantFairies = 13;
        this.cantWaves = 8;
        this.fairiesByWave = new int [] {1,2,4,4,2,2,3,1};

        spawnPointWave1 = new Vector2(412, 1200);
        firstTarXYWave1 = new Vector2(412, 600);

        spawnPointWave2 = new Vector2(230, 1400);
        firstTarXYWave2 = new Vector2(230, 700);

        spawnPointWave3 = new Vector2(48, 1200);
        firstTarXYWave3 = new Vector2(870, 660);

        spawnPointWave4 = new Vector2(920, 1200);
        firstTarXYWave4 = new Vector2(48, 600);

        spawnPointWave5 = new Vector2(-360, 680);
        firstTarXYWave5 = new Vector2(360, 860);
        
        spawnPointWave6 = new Vector2(scrWidth - 48, 850);
        firstTarXYWave6 = new Vector2(760, 850);
        
        spawnPointWave7 = new Vector2(scrWidth - 16, 1200);
        firstTarXYWave7 = new Vector2(260, 660);
        
        spawnPointWave8 = new Vector2(412, 1200);
        firstTarXYWave8 = new Vector2(412, 710);
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
            case 7: return new FairySpawn((int)spawnPointWave8.x, (int)spawnPointWave8.y,
                    					  (int)firstTarXYWave8.x, (int)firstTarXYWave8.y);
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
			case 7: return shootsFirstW8;
			default: return true;
		}
	}
	
	@Override
	public float getWaveSpawnCooldown(int currentWave) {
		switch (currentWave) {
			case 0: return waveSpawnCooldown1;
			case 1: return waveSpawnCooldown2;
			case 2: return waveSpawnCooldown3;
			case 3: return waveSpawnCooldown4;
			case 4: return waveSpawnCooldown5;
			case 5: return waveSpawnCooldown6;
			case 6: return waveSpawnCooldown7;
			case 7: return waveSpawnCooldown8;
			default: return 0f;
		}
	}
}