package BulletHellPatterns;

import java.util.Random;

import Enemies.EnemyBullet;

public class PredictionShotPattern extends BulletHellPattern implements PlayerTrackingPattern {
    private float playerX, playerY;
    private float playerVelX, playerVelY;
    private float lastPlayerX, lastPlayerY;
    private Random rnd = new Random();
    private float predictionTime = 0.8f; // How far ahead to predict

    public PredictionShotPattern() {
        this.cantBullet = 8;
        this.speed = 280f;
        this.maxShootingTime = 6.0f;
        this.bulletGenInterval = 0.3f;
        this.isDecelerated = false;
        this.currentBullet = 0;
        
        this.playerX = 0f;
        this.playerY = 0f;
        this.playerVelX = 0f;
        this.playerVelY = 0f;
        this.lastPlayerX = 0f;
        this.lastPlayerY = 0f;
    }

    @Override
    public void generateBulletInPattern(float x, float y, EnemyBullet eb) {
        // Calculate predicted player position
        float predictedX = playerX + (playerVelX * predictionTime);
        float predictedY = playerY + (playerVelY * predictionTime);
        
        // Add some randomness to make it less perfect
        predictedX += (rnd.nextFloat() - 0.5f) * 40f;
        predictedY += (rnd.nextFloat() - 0.5f) * 40f;
        
        // Direction from spawn point to predicted position
        float angle = (float) Math.atan2(predictedY - y, predictedX - x);
        
        eb.setVelocityX((float) Math.cos(angle) * speed);
        eb.setVelocityY((float) Math.sin(angle) * speed);
        
        currentBullet++;
        if (currentBullet >= cantBullet) currentBullet = 0;
    }

    @Override
    public void setReimuCoords(float x, float y) {
        // Calculate player velocity based on position change
        playerVelX = (x - lastPlayerX) * 0.5f;
        playerVelY = (y - lastPlayerY) * 0.5f;
        
        lastPlayerX = playerX;
        lastPlayerY = playerY;
        playerX = x;
        playerY = y;
    }
}
