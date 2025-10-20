package Levels;

import com.badlogic.gdx.math.Vector2;

import Enemies.FairySpawn;

public class LevelFour extends Level {

    private Vector2 spawnPointWave1;
    private Vector2 firstTarXYWave1;
    private boolean shootsFirstW1 = true;
    private float waveSpawnCooldown1 = 5f;
    private int bhpWave1 = 0;

    private Vector2 spawnPointWave2;
    private Vector2 firstTarXYWave2;
    private boolean shootsFirstW2 = true;
    private float waveSpawnCooldown2 = 0f;
    private int bhpWave2 = 6;

    private Vector2 spawnPointWave3;
    private Vector2 firstTarXYWave3;
    private boolean shootsFirstW3 = true;
    private float waveSpawnCooldown3 = 0.25f;
    private int bhpWave3 = 1;

    private Vector2 spawnPointWave4;
    private Vector2 firstTarXYWave4;
    private boolean shootsFirstW4 = false;
    private float waveSpawnCooldown4 = 1.5f;
    private int bhpWave4 = 0;

    private Vector2 spawnPointWave5;
    private Vector2 firstTarXYWave5;
    private boolean shootsFirstW5 = false;
    private float waveSpawnCooldown5 = 1f;
    private int bhpWave5 = 2;

    private Vector2 spawnPointWave6;
    private Vector2 firstTarXYWave6;
    private boolean shootsFirstW6 = true;
    private float waveSpawnCooldown6 = 0f;
    private int bhpWave6 = 3;

    private Vector2 spawnPointWave7;
    private Vector2 firstTarXYWave7;
    private boolean shootsFirstW7 = true;
    private float waveSpawnCooldown7 = 0.25f;
    private int bhpWave7 = 5;
    
    private Vector2 spawnPointWave8;
    private Vector2 firstTarXYWave8;
    private boolean shootsFirstW8 = true;
    private float waveSpawnCooldown8 = 1f;
    private int bhpWave8 = 1;
    
    private Vector2 spawnPointWave9;
    private Vector2 firstTarXYWave9;
    private boolean shootsFirstW9 = true;
    private float waveSpawnCooldown9 = 0.5f;
    private int bhpWave9 = 1;
    
    private Vector2 spawnPointWave10;
    private Vector2 firstTarXYWave10;
    private boolean shootsFirstW10 = true;
    private float waveSpawnCooldown10 = 1.5f;
    private int bhpWave10 = 1;

    // --- NEW waves 11..24 (14 extra rounds) ---
    // NEW: manage this round
    private Vector2 spawnPointWave11;
    private Vector2 firstTarXYWave11;
    private boolean shootsFirstW11 = true;
    private float waveSpawnCooldown11 = 2f;
    private int bhpWave11 = 1;

    private Vector2 spawnPointWave12;
    private Vector2 firstTarXYWave12;
    private boolean shootsFirstW12 = true;
    private float waveSpawnCooldown12 = 0f;
    private int bhpWave12 = 7;

    private Vector2 spawnPointWave13;
    private Vector2 firstTarXYWave13;
    private boolean shootsFirstW13 = true;
    private float waveSpawnCooldown13 = 0.75f;
    private int bhpWave13 = 4;

    private Vector2 spawnPointWave14;
    private Vector2 firstTarXYWave14;
    private boolean shootsFirstW14 = true;
    private float waveSpawnCooldown14 = 0.25f;
    private int bhpWave14 = 7;

    private Vector2 spawnPointWave15;
    private Vector2 firstTarXYWave15;
    private boolean shootsFirstW15 = true;
    private float waveSpawnCooldown15 = 0.5f;
    private int bhpWave15 = 5;

    private Vector2 spawnPointWave16;
    private Vector2 firstTarXYWave16;
    private boolean shootsFirstW16 = true;
    private float waveSpawnCooldown16 = 1f;
    private int bhpWave16 = 7;

    private Vector2 spawnPointWave17;
    private Vector2 firstTarXYWave17;
    private boolean shootsFirstW17 = true;
    private float waveSpawnCooldown17 = 2f;
    private int bhpWave17 = 2;

    private Vector2 spawnPointWave18;
    private Vector2 firstTarXYWave18;
    private boolean shootsFirstW18 = true;
    private float waveSpawnCooldown18 = 0f;
    private int bhpWave18 = 6;

    private Vector2 spawnPointWave19;
    private Vector2 firstTarXYWave19;
    private boolean shootsFirstW19 = true;
    private float waveSpawnCooldown19 = 0f;
    private int bhpWave19 = 0;

    private Vector2 spawnPointWave20;
    private Vector2 firstTarXYWave20;
    private boolean shootsFirstW20 = true;
    private float waveSpawnCooldown20 = 0f;
    private int bhpWave20 = 1;

    private Vector2 spawnPointWave21;
    private Vector2 firstTarXYWave21;
    private boolean shootsFirstW21 = true;
    private float waveSpawnCooldown21 = 0f;
    private int bhpWave21 = 7;

    private Vector2 spawnPointWave22;
    private Vector2 firstTarXYWave22;
    private boolean shootsFirstW22 = true;
    private float waveSpawnCooldown22 = 0f;
    private int bhpWave22 = 7;

    private Vector2 spawnPointWave23;
    private Vector2 firstTarXYWave23;
    private boolean shootsFirstW23 = true;
    private float waveSpawnCooldown23 = 0f;
    private int bhpWave23 = 3;

    private Vector2 spawnPointWave24;
    private Vector2 firstTarXYWave24;
    private boolean shootsFirstW24 = true;
    private float waveSpawnCooldown24 = 0f;
    private int bhpWave24 = 5;

    public LevelFour(float scrWidth, float scrHeight) {
        this.levelId = 3;
        this.bossChoice = 3;

        // updated to include 14 extra rounds
        this.cantWaves = 24;
        this.fairiesByWave = new int [] {2,1,1,4, // 1 - 4
							             4,3,3,1, // 5 - 8
							             2,3,1,3, // 9 - 12
							             2,6,4,8, // 13 - 16
							             3,3,4,2, // 17 - 20
							             6,6,5,1}; // 21 - 24
        // recompute cantFairies as sum of array (currently equals existing total)
        this.cantFairies = 0;
        for (int v : this.fairiesByWave) this.cantFairies += v;

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

        spawnPointWave7 = new Vector2(400, 940);
        firstTarXYWave7 = new Vector2(120, 650);
        
        spawnPointWave8 = new Vector2(-600, 1200);
        firstTarXYWave8 = new Vector2(850, 650);
        
        spawnPointWave9 = new Vector2(scrWidth - 16, 1200);
        firstTarXYWave9 = new Vector2(200, 790);
        
        spawnPointWave10 = new Vector2(412, 1200);
        firstTarXYWave10 = new Vector2(412, 600);

        // NEW waves default positions (adjust as needed)
        spawnPointWave11 = new Vector2(300, 960);
        firstTarXYWave11 = new Vector2(200, 650);

        spawnPointWave12 = new Vector2(700, 960);
        firstTarXYWave12 = new Vector2(812, 780);

        spawnPointWave13 = new Vector2(300, 960);
        firstTarXYWave13 = new Vector2(440, 680);

        spawnPointWave14 = new Vector2(920, 960);
        firstTarXYWave14 = new Vector2(760, 720);

        spawnPointWave15 = new Vector2(120, 960);
        firstTarXYWave15 = new Vector2(360, 640);

        spawnPointWave16 = new Vector2(920, 960);
        firstTarXYWave16 = new Vector2(800, 820);

        spawnPointWave17 = new Vector2(1000, 700);
        firstTarXYWave17 = new Vector2(600, 700);

        spawnPointWave18 = new Vector2(-200, 820);
        firstTarXYWave18 = new Vector2(360, 820);

        spawnPointWave19 = new Vector2(550, 1200);
        firstTarXYWave19 = new Vector2(770, 790);

        spawnPointWave20 = new Vector2(412, 1150);
        firstTarXYWave20 = new Vector2(412, 750);

        spawnPointWave21 = new Vector2(220, 1008);
        firstTarXYWave21 = new Vector2(180, 680);

        spawnPointWave22 = new Vector2(750, 1008);
        firstTarXYWave22 = new Vector2(800, 660);

        spawnPointWave23 = new Vector2(scrWidth + 100, 810);
        firstTarXYWave23 = new Vector2(200, 810);

        spawnPointWave24 = new Vector2(420, 1008);
        firstTarXYWave24 = new Vector2(520, 740);
    }

    @Override
    public FairySpawn getCoordsCurrentWave(int currentWave) {
        switch (currentWave) {
            case 0:  return new FairySpawn((int)spawnPointWave1.x, (int)spawnPointWave1.y,
                                          (int)firstTarXYWave1.x, (int)firstTarXYWave1.y);
            case 1:  return new FairySpawn((int)spawnPointWave2.x, (int)spawnPointWave2.y,
                                          (int)firstTarXYWave2.x, (int)firstTarXYWave2.y);
            case 2:  return new FairySpawn((int)spawnPointWave3.x, (int)spawnPointWave3.y,
                                          (int)firstTarXYWave3.x, (int)firstTarXYWave3.y);
            case 3:  return new FairySpawn((int)spawnPointWave4.x, (int)spawnPointWave4.y,
                                          (int)firstTarXYWave4.x, (int)firstTarXYWave4.y);
            case 4:  return new FairySpawn((int)spawnPointWave5.x, (int)spawnPointWave5.y,
                                          (int)firstTarXYWave5.x, (int)firstTarXYWave5.y);
            case 5:  return new FairySpawn((int)spawnPointWave6.x, (int)spawnPointWave6.y,
                                          (int)firstTarXYWave6.x, (int)firstTarXYWave6.y);
            case 6:  return new FairySpawn((int)spawnPointWave7.x, (int)spawnPointWave7.y,
                                          (int)firstTarXYWave7.x, (int)firstTarXYWave7.y);
            case 7:  return new FairySpawn((int)spawnPointWave8.x, (int)spawnPointWave8.y,
                                          (int)firstTarXYWave8.x, (int)firstTarXYWave8.y);
            case 8:  return new FairySpawn((int)spawnPointWave9.x, (int)spawnPointWave9.y,
                                          (int)firstTarXYWave9.x, (int)firstTarXYWave9.y);
            case 9:  return new FairySpawn((int)spawnPointWave10.x, (int)spawnPointWave10.y,
                                          (int)firstTarXYWave10.x, (int)firstTarXYWave10.y);
            case 10: return new FairySpawn((int)spawnPointWave11.x, (int)spawnPointWave11.y,
                                          (int)firstTarXYWave11.x, (int)firstTarXYWave11.y);
            case 11: return new FairySpawn((int)spawnPointWave12.x, (int)spawnPointWave12.y,
                                          (int)firstTarXYWave12.x, (int)firstTarXYWave12.y);
            case 12: return new FairySpawn((int)spawnPointWave13.x, (int)spawnPointWave13.y,
                                          (int)firstTarXYWave13.x, (int)firstTarXYWave13.y);
            case 13: return new FairySpawn((int)spawnPointWave14.x, (int)spawnPointWave14.y,
                                          (int)firstTarXYWave14.x, (int)firstTarXYWave14.y);
            case 14: return new FairySpawn((int)spawnPointWave15.x, (int)spawnPointWave15.y,
                                          (int)firstTarXYWave15.x, (int)firstTarXYWave15.y);
            case 15: return new FairySpawn((int)spawnPointWave16.x, (int)spawnPointWave16.y,
                                          (int)firstTarXYWave16.x, (int)firstTarXYWave16.y);
            case 16: return new FairySpawn((int)spawnPointWave17.x, (int)spawnPointWave17.y,
                                          (int)firstTarXYWave17.x, (int)firstTarXYWave17.y);
            case 17: return new FairySpawn((int)spawnPointWave18.x, (int)spawnPointWave18.y,
                                          (int)firstTarXYWave18.x, (int)firstTarXYWave18.y);
            case 18: return new FairySpawn((int)spawnPointWave19.x, (int)spawnPointWave19.y,
                                          (int)firstTarXYWave19.x, (int)firstTarXYWave19.y);
            case 19: return new FairySpawn((int)spawnPointWave20.x, (int)spawnPointWave20.y,
                                          (int)firstTarXYWave20.x, (int)firstTarXYWave20.y);
            case 20: return new FairySpawn((int)spawnPointWave21.x, (int)spawnPointWave21.y,
                                          (int)firstTarXYWave21.x, (int)firstTarXYWave21.y);
            case 21: return new FairySpawn((int)spawnPointWave22.x, (int)spawnPointWave22.y,
                                          (int)firstTarXYWave22.x, (int)firstTarXYWave22.y);
            case 22: return new FairySpawn((int)spawnPointWave23.x, (int)spawnPointWave23.y,
                                          (int)firstTarXYWave23.x, (int)firstTarXYWave23.y);
            case 23: return new FairySpawn((int)spawnPointWave24.x, (int)spawnPointWave24.y,
                                          (int)firstTarXYWave24.x, (int)firstTarXYWave24.y);
            default: return new FairySpawn(600,600,600,600);
        }
    }
	
    @Override
    public boolean getShootsFirstCurrrentWave(int currentWave) {
        switch (currentWave) {
            case 0:  return shootsFirstW1;
            case 1:  return shootsFirstW2;
            case 2:  return shootsFirstW3;
            case 3:  return shootsFirstW4;
            case 4:  return shootsFirstW5;
            case 5:  return shootsFirstW6;
            case 6:  return shootsFirstW7;
            case 7:  return shootsFirstW8;
            case 8:  return shootsFirstW9;
            case 9:  return shootsFirstW10;
            case 10: return shootsFirstW11;
            case 11: return shootsFirstW12;
            case 12: return shootsFirstW13;
            case 13: return shootsFirstW14;
            case 14: return shootsFirstW15;
            case 15: return shootsFirstW16;
            case 16: return shootsFirstW17;
            case 17: return shootsFirstW18;
            case 18: return shootsFirstW19;
            case 19: return shootsFirstW20;
            case 20: return shootsFirstW21;
            case 21: return shootsFirstW22;
            case 22: return shootsFirstW23;
            case 23: return shootsFirstW24;
            default: return true;
        }
    }

    @Override
    public float getWaveSpawnCooldown(int currentWave) {
        switch (currentWave) {
            case 0:  return waveSpawnCooldown1;
            case 1:  return waveSpawnCooldown2;
            case 2:  return waveSpawnCooldown3;
            case 3:  return waveSpawnCooldown4;
            case 4:  return waveSpawnCooldown5;
            case 5:  return waveSpawnCooldown6;
            case 6:  return waveSpawnCooldown7;
            case 7:  return waveSpawnCooldown8;
            case 8:  return waveSpawnCooldown9;
            case 9:  return waveSpawnCooldown10;
            case 10: return waveSpawnCooldown11;
            case 11: return waveSpawnCooldown12;
            case 12: return waveSpawnCooldown13;
            case 13: return waveSpawnCooldown14;
            case 14: return waveSpawnCooldown15;
            case 15: return waveSpawnCooldown16;
            case 16: return waveSpawnCooldown17;
            case 17: return waveSpawnCooldown18;
            case 18: return waveSpawnCooldown19;
            case 19: return waveSpawnCooldown20;
            case 20: return waveSpawnCooldown21;
            case 21: return waveSpawnCooldown22;
            case 22: return waveSpawnCooldown23;
            case 23: return waveSpawnCooldown24;
            default: return 0f;
        }
    }

    @Override
    public int getBhpCurrentWave(int currentWave) {
        switch (currentWave) {
            case 0:  return bhpWave1;
            case 1:  return bhpWave2;
            case 2:  return bhpWave3;
            case 3:  return bhpWave4;
            case 4:  return bhpWave5;
            case 5:  return bhpWave6;
            case 6:  return bhpWave7;
            case 7:  return bhpWave8;
            case 8:  return bhpWave9;
            case 9:  return bhpWave10;
            case 10: return bhpWave11;
            case 11: return bhpWave12;
            case 12: return bhpWave13;
            case 13: return bhpWave14;
            case 14: return bhpWave15;
            case 15: return bhpWave16;
            case 16: return bhpWave17;
            case 17: return bhpWave18;
            case 18: return bhpWave19;
            case 19: return bhpWave20;
            case 20: return bhpWave21;
            case 21: return bhpWave22;
            case 22: return bhpWave23;
            case 23: return bhpWave24;
            default: return 0;
        }
    }
}
