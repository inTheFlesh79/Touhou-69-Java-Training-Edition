package Managers;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Props.EnemyExplosionRing;
import Props.EnemySparkle;
import Props.HealthBar;

public class PropManager {
	private HealthBar bossHPBar;
	private ArrayList<EnemyExplosionRing> explosionRings;
    private ArrayList<EnemySparkle> sparkles;
	
	private Texture fairyMiscSheet; 
	private TextureRegion[][] sparkleRegions, explosionRingRegions;
	
	public PropManager() {
		fairyMiscSheet = new Texture(Gdx.files.internal("fairyMisc.png"));
		explosionRingRegions = TextureRegion.split(fairyMiscSheet, 64, 64);
        sparkleRegions = TextureRegion.split(fairyMiscSheet, 16, 16);
        
        explosionRings = new ArrayList<>();
        sparkles = new ArrayList<>();
	}
	
	
	// Draws all the props required for the game
	public void drawProps(SpriteBatch batch) {
		updateFairyExplosion();
		drawFairyExplosion(batch);
		drawSparkles(batch);
	}
	
	// --- Enemy Explosion prop ---
    public void createExplosionRing(float fairyX, float fairyY) {
        explosionRings.add(new EnemyExplosionRing(explosionRingRegions[0][3], fairyX, fairyY));
    }

    public void createSparkles(float fairyX, float fairyY) {
        int count = 4 + (int)(Math.random() * 2); // 4 or 5
        for (int i = 0; i < count; i++) {
            int idx = (int)(Math.random() * 4);   // pick [4][0..3]
            sparkles.add(new EnemySparkle(sparkleRegions[4][idx], fairyX, fairyY));
        }
    }
    
    public void updateFairyExplosion() {
        // Explosion rings
    	float delta = Gdx.graphics.getDeltaTime();
        Iterator<EnemyExplosionRing> ringIt = explosionRings.iterator();
        while (ringIt.hasNext()) {
            EnemyExplosionRing r = ringIt.next();
            r.update(delta);
            if (!r.isAlive()) {
                ringIt.remove();
            }
        }

        // Sparkles
        Iterator<EnemySparkle> spIt = sparkles.iterator();
        while (spIt.hasNext()) {
            EnemySparkle s = spIt.next();
            s.update(delta);
            if (!s.isAlive()) {
                spIt.remove();
            }
        }
    }
    
    public void drawFairyExplosion(SpriteBatch batch) {
        for (EnemyExplosionRing r : explosionRings) {
            r.drawRing(batch);
        }
    }

    public void drawSparkles(SpriteBatch batch) {
        for (EnemySparkle s : sparkles) {
            s.draw(batch);
        }
    }
	
	// --- Health Bar ---
	public void createBossHPBar(int defaultHealth, float scrWidth, float scrHeight) {
		bossHPBar = new HealthBar(defaultHealth,scrWidth,scrHeight,650, 12, 4);}
	public void drawBossHPBar(SpriteBatch batch) {bossHPBar.draw(batch);}
	public HealthBar getBossHPBar() {return bossHPBar;}
}