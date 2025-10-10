package Levels;

import com.badlogic.gdx.math.Vector2;

import Enemies.FairySpawn;

public class LevelFour extends Level {

    private Vector2 spawnPointWave1;
    private Vector2 firstTarXYWave1;
    private boolean shootsFirstW1 = true;

    private Vector2 spawnPointWave2;
    private Vector2 firstTarXYWave2;
    private boolean shootsFirstW2 = true;

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
    
    private Vector2 spawnPointWave8;
    private Vector2 firstTarXYWave8;
    private boolean shootsFirstW8 = true;
    
    private Vector2 spawnPointWave9;
    private Vector2 firstTarXYWave9;
    private boolean shootsFirstW9 = true;
    
    private Vector2 spawnPointWave10;
    private Vector2 firstTarXYWave10;
    private boolean shootsFirstW10 = true;

    public LevelFour(float scrWidth, float scrHeight) {
        this.levelId = 3;
        this.bossChoice = 3;
        this.cantFairies = 18;
        this.cantWaves = 10;
        this.fairiesByWave = new int [] {2,1,1,4,4,3,3,1,2,3};
        
        // Create Mirror Spawning for this one
        spawnPointWave1 = new Vector2(230, 1400);
        firstTarXYWave1 = new Vector2(230, 700);

        spawnPointWave2 = new Vector2(412, 1200);
        firstTarXYWave2 = new Vector2(412, 600);

        spawnPointWave3 = new Vector2(860, 1200);
        firstTarXYWave3 = new Vector2(860, 700);

        spawnPointWave4 = new Vector2(360, 1200);
        firstTarXYWave4 = new Vector2(690, 760);

        spawnPointWave5 = new Vector2(scrWidth - 16, 1200);
        firstTarXYWave5 = new Vector2(412, 760);

        spawnPointWave6 = new Vector2(-600, 860);
        firstTarXYWave6 = new Vector2(412, 860);

        spawnPointWave7 = new Vector2(scrWidth - 16, 1200);
        firstTarXYWave7 = new Vector2(120, 650);
        
        spawnPointWave8 = new Vector2(-600, 1200);
        firstTarXYWave8 = new Vector2(850, 650);
        
        spawnPointWave9 = new Vector2(scrWidth - 16, 1200);
        firstTarXYWave9 = new Vector2(200, 790);
        
        spawnPointWave10 = new Vector2(412, 1200);
        firstTarXYWave10 = new Vector2(412, 600);
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
            case 8: return new FairySpawn((int)spawnPointWave9.x, (int)spawnPointWave9.y,
                    					  (int)firstTarXYWave9.x, (int)firstTarXYWave9.y);
            case 9: return new FairySpawn((int)spawnPointWave10.x, (int)spawnPointWave10.y,
                    					  (int)firstTarXYWave10.x, (int)firstTarXYWave10.y);
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
			default: return true;
		}
	}
	
	//IN CONSTRUCTION
	public float getWaveCooldown(int currentWave) {
		return 3f;
	}
}