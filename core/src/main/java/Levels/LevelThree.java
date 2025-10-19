package Levels;

import com.badlogic.gdx.math.Vector2;

import Enemies.FairySpawn;

public class LevelThree extends Level {
    private Vector2 spawnPointWave1;
    private Vector2 firstTarXYWave1;
    private boolean shootsFirstW1 = true;
    private float waveSpawnCooldown1 = 3f;
    private int bhpWave1 = 1;

    private Vector2 spawnPointWave2;
    private Vector2 firstTarXYWave2;
    private boolean shootsFirstW2 = true;
    private float waveSpawnCooldown2 = 0f;
    private int bhpWave2 = 0;

    private Vector2 spawnPointWave3;
    private Vector2 firstTarXYWave3;
    private boolean shootsFirstW3 = false;
    private float waveSpawnCooldown3 = 2f;
    private int bhpWave3 = 4;

    private Vector2 spawnPointWave4;
    private Vector2 firstTarXYWave4;
    private boolean shootsFirstW4 = true;
    private float waveSpawnCooldown4 = 0.5f;
    private int bhpWave4 = 5;

    private Vector2 spawnPointWave5;
    private Vector2 firstTarXYWave5;
    private boolean shootsFirstW5 = false;
    private float waveSpawnCooldown5 = 1f;
    private int bhpWave5 = 2;
    
    private Vector2 spawnPointWave6;
    private Vector2 firstTarXYWave6;
    private boolean shootsFirstW6 = true;
    private float waveSpawnCooldown6 = 0f;
    private int bhpWave6 = 1;
    
    private Vector2 spawnPointWave7;
    private Vector2 firstTarXYWave7;
    private boolean shootsFirstW7 = true;
    private float waveSpawnCooldown7 = 1f;
    private int bhpWave7 = 4;
    
    private Vector2 spawnPointWave8;
    private Vector2 firstTarXYWave8;
    private boolean shootsFirstW8 = true;
    private float waveSpawnCooldown8 = 3f;
    private int bhpWave8 = 6;

    // --- NEW waves 9..16 (eight extra rounds) ---
    // NEW: manage this round
    private Vector2 spawnPointWave9;
    private Vector2 firstTarXYWave9;
    private boolean shootsFirstW9 = true;
    private float waveSpawnCooldown9 = 1f;
    private int bhpWave9 = 2;

    private Vector2 spawnPointWave10;
    private Vector2 firstTarXYWave10;
    private boolean shootsFirstW10 = true;
    private float waveSpawnCooldown10 = 0.5f;
    private int bhpWave10 = 6;

    private Vector2 spawnPointWave11;
    private Vector2 firstTarXYWave11;
    private boolean shootsFirstW11 = false;
    private float waveSpawnCooldown11 = 0f;
    private int bhpWave11 = 1;

    private Vector2 spawnPointWave12;
    private Vector2 firstTarXYWave12;
    private boolean shootsFirstW12 = true;
    private float waveSpawnCooldown12 = 0.75f;
    private int bhpWave12 = 5;

    private Vector2 spawnPointWave13;
    private Vector2 firstTarXYWave13;
    private boolean shootsFirstW13 = true;
    private float waveSpawnCooldown13 = 0f;
    private int bhpWave13 = 3;

    private Vector2 spawnPointWave14;
    private Vector2 firstTarXYWave14;
    private boolean shootsFirstW14 = true;
    private float waveSpawnCooldown14 = 0f;
    private int bhpWave14 = 4;

    private Vector2 spawnPointWave15;
    private Vector2 firstTarXYWave15;
    private boolean shootsFirstW15 = true;
    private float waveSpawnCooldown15 = 1f;
    private int bhpWave15 = 1;

    private Vector2 spawnPointWave16;
    private Vector2 firstTarXYWave16;
    private boolean shootsFirstW16 = true;
    private float waveSpawnCooldown16 = 0f;
    private int bhpWave16 = 5;

    public LevelThree(float scrWidth, float scrHeight) {
        this.levelId = 2;
        this.bossChoice = 2;

        // updated to include 8 extra rounds; fairiesByWave appended with zeros for new waves
        this.cantWaves = 16;
        this.fairiesByWave = new int [] {1,2,4,4,
							             2,2,3,1, // original 8
							             4,2,1,3,
							             3,6,3,4};  // new 8 (fill later)
        // update cantFairies to the sum of fairiesByWave (currently 19)
        this.cantFairies = 0;
        for (int v : this.fairiesByWave) this.cantFairies += v;

        spawnPointWave1 = new Vector2(412, 1200);
        firstTarXYWave1 = new Vector2(412, 600);

        spawnPointWave2 = new Vector2(230, 1200);
        firstTarXYWave2 = new Vector2(230, 700);

        spawnPointWave3 = new Vector2(48, 1200);
        firstTarXYWave3 = new Vector2(870, 660);

        spawnPointWave4 = new Vector2(920, 1008);
        firstTarXYWave4 = new Vector2(48, 600);

        spawnPointWave5 = new Vector2(-360, 680);
        firstTarXYWave5 = new Vector2(360, 860);
        
        spawnPointWave6 = new Vector2(scrWidth - 48, 850);
        firstTarXYWave6 = new Vector2(760, 850);
        
        spawnPointWave7 = new Vector2(scrWidth - 16, 1200);
        firstTarXYWave7 = new Vector2(260, 660);
        
        spawnPointWave8 = new Vector2(412, 1200);
        firstTarXYWave8 = new Vector2(412, 710);

        // New waves default positions (adjust later if needed)
        spawnPointWave9 = new Vector2(100, 1250);
        firstTarXYWave9 = new Vector2(320, 650);

        spawnPointWave10 = new Vector2(scrWidth - 100, 1300);
        firstTarXYWave10 = new Vector2(740, 700);

        spawnPointWave11 = new Vector2(200, 1400);
        firstTarXYWave11 = new Vector2(330, 680);

        spawnPointWave12 = new Vector2(scrWidth-300, 720);
        firstTarXYWave12 = new Vector2(620, 860);

        spawnPointWave13 = new Vector2(548, 1200);
        firstTarXYWave13 = new Vector2(380, 640);

        spawnPointWave14 = new Vector2(scrWidth, 1008);
        firstTarXYWave14 = new Vector2(660, 890);

        spawnPointWave15 = new Vector2(412, 1100);
        firstTarXYWave15 = new Vector2(412, 700);

        spawnPointWave16 = new Vector2(scrWidth / 4f, 1008);
        firstTarXYWave16 = new Vector2(360, 680);
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
            case 11: return new FairySpawn((int)spawnPointWave12.x, (int)spawnPointWave12.y, (int)firstTarXYWave12.x, (int)firstTarXYWave12.y);
            case 12: return new FairySpawn((int)spawnPointWave13.x, (int)spawnPointWave13.y, (int)firstTarXYWave13.x, (int)firstTarXYWave13.y);
            case 13: return new FairySpawn((int)spawnPointWave14.x, (int)spawnPointWave14.y, (int)firstTarXYWave14.x, (int)firstTarXYWave14.y);
            case 14: return new FairySpawn((int)spawnPointWave15.x, (int)spawnPointWave15.y, (int)firstTarXYWave15.x, (int)firstTarXYWave15.y);
            case 15: return new FairySpawn((int)spawnPointWave16.x, (int)spawnPointWave16.y, (int)firstTarXYWave16.x, (int)firstTarXYWave16.y);
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
			case 11: return shootsFirstW12;
			case 12: return shootsFirstW13;
			case 13: return shootsFirstW14;
			case 14: return shootsFirstW15;
			case 15: return shootsFirstW16;
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
			case 11: return waveSpawnCooldown12;
			case 12: return waveSpawnCooldown13;
			case 13: return waveSpawnCooldown14;
			case 14: return waveSpawnCooldown15;
			case 15: return waveSpawnCooldown16;
			default: return 0f;
		}
	}

    @Override
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
            case 11: return bhpWave12;
            case 12: return bhpWave13;
            case 13: return bhpWave14;
            case 14: return bhpWave15;
            case 15: return bhpWave16;
            default: return 0;
        }
    }
}