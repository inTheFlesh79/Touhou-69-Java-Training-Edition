package BulletHellPatterns;

import Enemies.EnemyBullet;

public class DynamicSpiralPattern extends BulletHellPattern {
	
    public DynamicSpiralPattern() {
    	speed = 300f;
        cantBullet = 4;
        angle = 0f;
        maxShootingTime = 3.0f;
        bulletGenInterval = 0.1f;
        currentBullet = 0;
    }

    @Override
    public void generateBulletInPattern(float x, float y, EnemyBullet bullet) {
        float direction = angle + (float) (2 * Math.PI * currentBullet / cantBullet);
        float bulletVelocityX = (float) Math.cos(direction) * speed;
        float bulletVelocityY = (float) Math.sin(direction) * speed;

        bullet.setVelocityX(bulletVelocityX);
        bullet.setVelocityY(bulletVelocityY);
        
        angle += 0.065f;
        currentBullet ++;
        
        if (currentBullet >= cantBullet) {
            currentBullet = 0;
        }
        //System.out.println("CurrBullet = "+currentBullet);
    }
}