package Reimu;

public class PowerDrop extends Drop {
	public PowerDrop(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		hitbox = spr.getBoundingRectangle();
		spr.setRegion(spriteRegions[0][2]);
	}
}