package Reimu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScoreDrop extends Drop {
	
	public ScoreDrop(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		hitbox = spr.getBoundingRectangle();
		spr.setRegion(spriteRegions[0][1]);
	}
}

