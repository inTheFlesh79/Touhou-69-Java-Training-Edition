package Reimu;

public class ScoreDrop extends Drop {
	public ScoreDrop(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		hitbox = spr.getBoundingRectangle();
		spr.setRegion(spriteRegions[0][1]);
	}
}

