package BulletHellPatterns;

import Enemies.EnemyBullet;

public class TargetedPattern extends BulletHellPattern {
    private float reimuX, reimuY;
	
	public TargetedPattern() {
	 	speed = 300f;
	    cantBullet = 1;
	    angle = 0f;
	    maxShootingTime = 1.75f;
	    bulletGenInterval = 0.25f;
	    currentBullet = 0;
	}
	
    public void setReimuCoords(float x, float y) {
        reimuX = x;
        reimuY = y;
    }

    @Override
    public void generateBulletInPattern(float x, float y, EnemyBullet bullet) {
        // calculate velocity towards last known Reimu position
        float dx = reimuX - x;
        float dy = reimuY - y;
        float len = (float)Math.sqrt(dx * dx + dy * dy);

        if (len != 0) {
            float velX = (dx / len) * speed;
            float velY = (dy / len) * speed;
            bullet.setVelocityX(velX);
            bullet.setVelocityY(velY);
        }

        currentBullet++;
        if (currentBullet >= cantBullet) {
            currentBullet = 0;
        }
    }
}
