package BulletHellPatterns;

import Enemies.EnemyBullet;

public class DynamicSpiralPattern extends BulletHellPattern {
	int DynamicSpiralCurrentBullet = 0;

    public DynamicSpiralPattern() {
    	speed = 300f;
        cantBullet = 4;
        angle = 0f;
        maxShootingTime = 3.0f;
        bulletGenInterval = 0.1f;
    }

    @Override
    public EnemyBullet generateBulletInPattern(float x, float y) {
        float direction = angle + (float) (2 * Math.PI * DynamicSpiralCurrentBullet / cantBullet);
        
        float bulletVelocityX = (float) Math.cos(direction) * speed;
        float bulletVelocityY = (float) Math.sin(direction) * speed;

        EnemyBullet bullet = new EnemyBullet(x, y, bulletVelocityX, bulletVelocityY);
        
        angle += 0.065f;
        
        DynamicSpiralCurrentBullet ++;
        
        if (DynamicSpiralCurrentBullet >= cantBullet) {
        	DynamicSpiralCurrentBullet = 0;
        }

        return bullet;
    }
}