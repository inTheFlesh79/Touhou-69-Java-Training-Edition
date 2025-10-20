package Levels;

import com.badlogic.gdx.math.Vector2;

import Enemies.FairySpawn;

public class LevelTwo extends Level {
    private Vector2 spawnPointWave1;
    private Vector2 firstTarXYWave1;
    private boolean shootsFirstW1 = true;
    private float waveSpawnCooldown1 = 5f;
    private int bhpWave1 = 4;

    private Vector2 spawnPointWave2;
    private Vector2 firstTarXYWave2;
    private boolean shootsFirstW2 = false;
    private float waveSpawnCooldown2 = 1f;
    private int bhpWave2 = 2;

    private Vector2 spawnPointWave3;
    private Vector2 firstTarXYWave3;
    private boolean shootsFirstW3 = true;
    private float waveSpawnCooldown3 = 0.25f;
    private int bhpWave3 = 6;

    private Vector2 spawnPointWave4;
    private Vector2 firstTarXYWave4;
    private boolean shootsFirstW4 = false;
    private float waveSpawnCooldown4 = 0.25f;
    private int bhpWave4 = 2;
    
    private Vector2 spawnPointWave5;
    private Vector2 firstTarXYWave5;
    private boolean shootsFirstW5 = true;
    private float waveSpawnCooldown5 = 1.5f;
    private int bhpWave5 = 5;
    
    private Vector2 spawnPointWave6;
    private Vector2 firstTarXYWave6;
    private boolean shootsFirstW6 = true;
    private float waveSpawnCooldown6 = 0.5f;
    private int bhpWave6 = 0;

    private Vector2 spawnPointWave7;
    private Vector2 firstTarXYWave7;
    private boolean shootsFirstW7 = true;
    private float waveSpawnCooldown7 = 1.25f;
    private int bhpWave7 = 6;

    private Vector2 spawnPointWave8;
    private Vector2 firstTarXYWave8;
    private boolean shootsFirstW8 = false;
    private float waveSpawnCooldown8 = 0.75f;
    private int bhpWave8 = 1;

    private Vector2 spawnPointWave9;
    private Vector2 firstTarXYWave9;
    private boolean shootsFirstW9 = true;
    private float waveSpawnCooldown9 = 2f;
    private int bhpWave9 = 3;

    private Vector2 spawnPointWave10;
    private Vector2 firstTarXYWave10;
    private boolean shootsFirstW10 = false;
    private float waveSpawnCooldown10 = 0.25f;
    private int bhpWave10 = 1;

    private Vector2 spawnPointWave11;
    private Vector2 firstTarXYWave11;
    private boolean shootsFirstW11 = true;
    private float waveSpawnCooldown11 = 1f;
    private int bhpWave11 = 4;

    public LevelTwo(float scrWidth, float scrHeight) {
        this.levelId = 1;
        this.bossChoice = 1;
        this.cantWaves = 11;
        this.fairiesByWave = new int [] {2,3,1,
        								2,4,2,
        								2,2,4,
        								2,3};
        // Original spawn setup
        spawnPointWave1 = new Vector2(115, 1200);
        firstTarXYWave1 = new Vector2(230, 620);

        spawnPointWave2 = new Vector2(750, 1200);
        firstTarXYWave2 = new Vector2(600, 630);

        spawnPointWave3 = new Vector2(412, 1400);
        firstTarXYWave3 = new Vector2(412, 600);

        spawnPointWave4 = new Vector2(300, 1400);
        firstTarXYWave4 = new Vector2(108, 600);
        
        spawnPointWave5 = new Vector2(780, 1400);
        firstTarXYWave5 = new Vector2(830, 660);
        
        spawnPointWave6 = new Vector2(400, 1400);
        firstTarXYWave6 = new Vector2(248, 870);

        // New waves 7–11 (simple layout, feel free to adjust)
        spawnPointWave7 = new Vector2(560, 1200);
        firstTarXYWave7 = new Vector2(660, 600);

        spawnPointWave8 = new Vector2(scrWidth - 400, 1100);
        firstTarXYWave8 = new Vector2(220, 720);

        spawnPointWave9 = new Vector2(412, 1200);
        firstTarXYWave9 = new Vector2(412, 640);

        spawnPointWave10 = new Vector2(scrWidth / 2f, 1300);
        firstTarXYWave10 = new Vector2(320, 600);

        spawnPointWave11 = new Vector2(scrWidth - 360, 1200);
        firstTarXYWave11 = new Vector2(790, 700);
    }

    @Override
    public FairySpawn getCoordsCurrentWave(int currentWave) {
        switch (currentWave) {
            case 0:  return new FairySpawn((int)spawnPointWave1.x, (int)spawnPointWave1.y, (int)firstTarXYWave1.x, (int)firstTarXYWave1.y);
            case 1:  return new FairySpawn((int)spawnPointWave2.x, (int)spawnPointWave2.y, (int)firstTarXYWave2.x, (int)firstTarXYWave2.y);
            case 2:  return new FairySpawn((int)spawnPointWave3.x, (int)spawnPointWave3.y, (int)firstTarXYWave3.x, (int)firstTarXYWave3.y);
            case 3:  return new FairySpawn((int)spawnPointWave4.x, (int)spawnPointWave4.y, (int)firstTarXYWave4.x, (int)firstTarXYWave4.y);
            case 4:  return new FairySpawn((int)spawnPointWave5.x, (int)spawnPointWave5.y, (int)firstTarXYWave5.x, (int)firstTarXYWave5.y);
            case 5:  return new FairySpawn((int)spawnPointWave6.x, (int)spawnPointWave6.y, (int)firstTarXYWave6.x, (int)firstTarXYWave6.y);
            case 6:  return new FairySpawn((int)spawnPointWave7.x, (int)spawnPointWave7.y, (int)firstTarXYWave7.x, (int)firstTarXYWave7.y);
            case 7:  return new FairySpawn((int)spawnPointWave8.x, (int)spawnPointWave8.y, (int)firstTarXYWave8.x, (int)firstTarXYWave8.y);
            case 8:  return new FairySpawn((int)spawnPointWave9.x, (int)spawnPointWave9.y, (int)firstTarXYWave9.x, (int)firstTarXYWave9.y);
            case 9:  return new FairySpawn((int)spawnPointWave10.x, (int)spawnPointWave10.y, (int)firstTarXYWave10.x, (int)firstTarXYWave10.y);
            case 10: return new FairySpawn((int)spawnPointWave11.x, (int)spawnPointWave11.y, (int)firstTarXYWave11.x, (int)firstTarXYWave11.y);
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
			case 8: return shootsFirstW9;
			case 9: return shootsFirstW10;
			case 10: return shootsFirstW11;
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
			case 8: return waveSpawnCooldown9;
			case 9: return waveSpawnCooldown10;
			case 10: return waveSpawnCooldown11;
			default: return 0f;
		}
	}

    public int getBhpCurrentWave(int currentWave) {
        switch (currentWave) {
            case 0: return bhpWave1;
            case 1: return bhpWave2;
            case 2: return bhpWave3;
            case 3: return bhpWave4;
            case 4: return bhpWave5;
            case 5: return bhpWave6;
            case 6: return bhpWave7;
            case 7: return bhpWave8;
            case 8: return bhpWave9;
            case 9: return bhpWave10;
            case 10: return bhpWave11;
            default: return 0;
        }
    }
}