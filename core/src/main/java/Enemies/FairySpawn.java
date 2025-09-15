package Enemies;

public class FairySpawn {
    private int spawnX, spawnY;
    private int targetX, targetY;

    public FairySpawn(int spawnX, int spawnY, int targetX, int targetY) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.targetX = targetX;
        this.targetY = targetY;
    }
    public int getSpawnX() {return spawnX;}
    public int getSpawnY() {return spawnY;}
    public int getTargetX() {return targetX;}
    public int getTargetY() {return targetY;}
}
