package Levels;

import com.badlogic.gdx.math.Vector2;

import Enemies.FairySpawn;

public class LevelOne extends Level {
    private Vector2 spawnPointWave1;
    private Vector2 firstTarXYWave1;
    private boolean shootsFirstW1 = true;
    private float waveSpawnCooldown1 = 5f;
    private int bhpWave1 = 4; //4

    private Vector2 spawnPointWave2;
    private Vector2 firstTarXYWave2;
    private boolean shootsFirstW2 = false;
    private float waveSpawnCooldown2 = 0f;
    private int bhpWave2 = 8; //6

    private Vector2 spawnPointWave3;
    private Vector2 firstTarXYWave3;
    private boolean shootsFirstW3 = true;
    private float waveSpawnCooldown3 = 1.5f;
    private int bhpWave3 = 2;

    private Vector2 spawnPointWave4;
    private Vector2 firstTarXYWave4;
    private boolean shootsFirstW4 = true;
    private float waveSpawnCooldown4 = 1f;
    private int bhpWave4 = 4;
    
    private Vector2 spawnPointWave5;
    private Vector2 firstTarXYWave5;
    private boolean shootsFirstW5 = true;
    private float waveSpawnCooldown5 = 0f;
    private int bhpWave5 = 2;
    
    private Vector2 spawnPointWave6;
    private Vector2 firstTarXYWave6;
    private boolean shootsFirstW6 = false;
    private float waveSpawnCooldown6 = 1.5f;
    private int bhpWave6 = 4;
    
    private Vector2 spawnPointWave7;
    private Vector2 firstTarXYWave7;
    private boolean shootsFirstW7 = true;
    private float waveSpawnCooldown7 = 1f;
    private int bhpWave7 = 2;

    public LevelOne(float scrWidth, float scrHeight) {
        this.levelId = 0;
        this.bossChoice = 0;

        // Updated counts to include waves 5..7
        this.cantFairies = 15;   // sum of fairiesByWave below
        this.cantWaves = 7;
        this.fairiesByWave = new int[] {1,3,3,2,1,2,2};

        // initialize Vectors using the provided world width/height
        spawnPointWave1 = new Vector2(scrWidth - 16f, 932f);
        firstTarXYWave1 = new Vector2(48f, 600f);

        spawnPointWave2 = new Vector2(630f, 1032f);
        firstTarXYWave2 = new Vector2(800f, 600f);

        spawnPointWave3 = new Vector2(scrWidth / 2f - 408f, 1032f);
        firstTarXYWave3 = new Vector2(460f, 600f);

        spawnPointWave4 = new Vector2(scrWidth - 16f, 932f);
        firstTarXYWave4 = new Vector2(830f, 720f);

        spawnPointWave5 = new Vector2(412, 1200);
        firstTarXYWave5 = new Vector2(412, 600);

        spawnPointWave6 = new Vector2(scrWidth / 2f + 200f, 1032f);
        firstTarXYWave6 = new Vector2(520f, 650f);

        spawnPointWave7 = new Vector2(scrWidth - 16f, 932f);
        firstTarXYWave7 = new Vector2(160f, 680f);
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
            case 4:
                return new FairySpawn(
                    (int) spawnPointWave5.x, (int) spawnPointWave5.y,
                    (int) firstTarXYWave5.x, (int) firstTarXYWave5.y
                );
            case 5:
                return new FairySpawn(
                    (int) spawnPointWave6.x, (int) spawnPointWave6.y,
                    (int) firstTarXYWave6.x, (int) firstTarXYWave6.y
                );
            case 6:
                return new FairySpawn(
                    (int) spawnPointWave7.x, (int) spawnPointWave7.y,
                    (int) firstTarXYWave7.x, (int) firstTarXYWave7.y
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
            case 4: return shootsFirstW5;
            case 5: return shootsFirstW6;
            case 6: return shootsFirstW7;
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
            default: return 0;
        }
    }
}