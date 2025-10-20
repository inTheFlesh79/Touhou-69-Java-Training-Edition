package BulletHellPatterns;

import com.badlogic.gdx.math.MathUtils;

import Enemies.EnemyBullet;

public class SlowTargetedPattern extends BulletHellPattern implements PlayerTrackingPattern {
    private float reimuX, reimuY;
	
    public SlowTargetedPattern() {
        speed = 80f; // default, just in case
        cantBullet = 1;
        angle = 0f;
        maxShootingTime = 6f;
        bulletGenInterval = 0.15f;
        currentBullet = 0;
    }
	
    public void setReimuCoords(float x, float y) {
        reimuX = x;
        reimuY = y;
    }

    @Override
    public void generateBulletInPattern(float x, float y, EnemyBullet bullet) {
        // pick a random speed between 80 and 100 for this bullet
        float currentSpeed = MathUtils.random(280f, 390f);

        // calculate velocity towards last known Reimu position
        float dx = reimuX - x;
        float dy = reimuY - y;
        float len = (float)Math.sqrt(dx * dx + dy * dy);

        if (len != 0) {
            float velX = (dx / len) * currentSpeed;
            float velY = (dy / len) * currentSpeed;
            bullet.setVelocityX(velX);
            bullet.setVelocityY(velY);
        }

        currentBullet++;
        if (currentBullet >= cantBullet) {
            currentBullet = 0;
        }
    }
}