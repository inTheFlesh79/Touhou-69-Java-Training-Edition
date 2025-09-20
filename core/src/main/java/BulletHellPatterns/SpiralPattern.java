package BulletHellPatterns;

import java.util.Random;

import Enemies.EnemyBullet;

public class SpiralPattern extends BulletHellPattern{
    private float angleTilt;
    
	public void generateBulletInPattern(float x, float y, EnemyBullet bullet) {
        float bulletVelocityX = (float) Math.cos(angle) * speed;
        float bulletVelocityY = (float) Math.sin(angle) * speed;
        //System.out.println("Angle: " + angle + ", VelocityX: " + bulletVelocityX + ", VelocityY: " + bulletVelocityY);
        bullet.setVelocityX(bulletVelocityX);
        bullet.setVelocityY(bulletVelocityY);
        angle += angleTilt;
        maxShootingTime = 3.0f;
        bulletGenInterval = 0.25f;
	}
	
	public SpiralPattern() {
		speed = 200f;
		int[] cantBulletOptions = {6};
		float[] angleTilts = {0.8f};
		Random random = new Random();
		this.cantBullet = cantBulletOptions[random.nextInt(cantBulletOptions.length)];
		this.angleTilt = angleTilts[random.nextInt(angleTilts.length)];
	}
	
}
