package Reimu;

public class ShieldDrop extends Drop {
	public ShieldDrop(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		hitbox = spr.getBoundingRectangle();
		spr.setRegion(spriteRegions[0][4]);
	}
}