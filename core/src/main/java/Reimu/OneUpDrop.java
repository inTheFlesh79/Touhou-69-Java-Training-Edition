package Reimu;

public class OneUpDrop extends Drop {
	public OneUpDrop(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		hitbox = spr.getBoundingRectangle();
		spr.setRegion(spriteRegions[0][5]);
	}
}
