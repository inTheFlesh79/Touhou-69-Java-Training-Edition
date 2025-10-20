package BulletHellPatterns;

import java.util.Random;

import Enemies.EnemyBullet;

public class ConvergingSpiralPattern extends BulletHellPattern implements TwoStagePattern, PlayerTrackingPattern {

    private float centerX, centerY;
    private float playerX, playerY;
    private float spiralStrength = 0.9f; // rotation intensity
    private float inwardSpeed = 80f;    // pull strength
    private float spawnRadius;
    private Random rnd = new Random();
    private boolean vortexInitialized = false;
    private final float SCREEN_WIDTH = 920f;
    private final float SCREEN_HEIGHT = 960f;
    private float playerPullStrength = 0.3f; // How strongly Reimu gets pulled

    public ConvergingSpiralPattern() {
        this.cantBullet = 5;          // many simultaneous bullets
        this.speed = 100f;
        this.maxShootingTime = 5.0f;   // long pull duration
        this.bulletGenInterval = 0.08f;
        this.isDecelerated = false;
    }

    @Override
    public void generateBulletInPattern(float bossX, float bossY, EnemyBullet eb) {
        // Initialize vortex center at boss position on first call
        if (!vortexInitialized) {
            this.centerX = bossX;
            this.centerY = bossY;
            
            // Spawn radius from screen edges
            this.spawnRadius = Math.max(SCREEN_WIDTH, SCREEN_HEIGHT) * 0.1f;
            vortexInitialized = true;
        }

        // Random spawn position around screen edges (outside, moving inward)
        float angle = (float) (rnd.nextFloat() * Math.PI * 2.0);
        float spawnX = centerX + (float) Math.cos(angle) * spawnRadius;
        float spawnY = centerY + (float) Math.sin(angle) * spawnRadius;

        // Ensure spawn position is within extended screen bounds (allow some outside)
        spawnX = Math.max(-50f, Math.min(SCREEN_WIDTH + 50f, spawnX));
        spawnY = Math.max(-50f, Math.min(SCREEN_HEIGHT + 50f, spawnY));

        eb.setX(spawnX);
        eb.setY(spawnY);

        // Direction from spawn point to vortex center (inward)
        float dirToCenter = (float) Math.atan2(centerY - spawnY, centerX - spawnX);
        
        // Strong inward velocity toward center with spiral component
        float baseInwardSpeed = this.inwardSpeed;
        float distToCenter = (float) Math.sqrt(
            (centerX - spawnX) * (centerX - spawnX) + 
            (centerY - spawnY) * (centerY - spawnY)
        );
        
        // Increase speed for bullets further away to make convergence more dramatic
        if (distToCenter > 300f) {
            baseInwardSpeed *= 1.8f;
        }

        // Add tangential (spiral) component
        float spiralAngle = dirToCenter + (float) (Math.PI / 2.0f);
        float velX = (float) (Math.cos(dirToCenter) * baseInwardSpeed
                            + Math.cos(spiralAngle) * (baseInwardSpeed * spiralStrength));
        float velY = (float) (Math.sin(dirToCenter) * baseInwardSpeed
                            + Math.sin(spiralAngle) * (baseInwardSpeed * spiralStrength));

        eb.setVelocityX(velX);
        eb.setVelocityY(velY);

        currentBullet++;
        if (currentBullet >= cantBullet) currentBullet = 0;
    }

    @Override
    public void applyMultiStage(EnemyBullet eb) {
        // Strong pull toward center that increases as bullets get closer
        float dx = centerX - eb.getX();
        float dy = centerY - eb.getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist < 500f) {
            // Stronger pull factor that increases as distance decreases
            float pullStrength = 1.0f + (300f / Math.max(dist, 40f));
            eb.setVelocityX(eb.getVelocityX() + dx * 0.08f * pullStrength);
            eb.setVelocityY(eb.getVelocityY() + dy * 0.08f * pullStrength);
        }
    }

    @Override
    public void setReimuCoords(float x, float y) {
        playerX = x;
        playerY = y;
        
        // Apply pull force to player (Reimu)
        applyPlayerPull();
    }

    private void applyPlayerPull() {
        // Calculate direction from player to vortex center
        float dx = centerX - playerX;
        float dy = centerY - playerY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        
        // Apply pull force to player (this would need to be implemented in your player movement system)
        if (dist < 600f && dist > 50f) {
            // The pull strength increases as player gets closer to center
            float pullForce = playerPullStrength * (400f / Math.max(dist, 80f));
            
            // This would typically modify player velocity or position
            // In your game, you might have something like:
            // player.addVelocity(dx * pullForce * 0.01f, dy * pullForce * 0.01f);
            // or modify player position directly if they don't resist
        }
    }

    @Override
    public boolean hasDeceleration() {
        return isDecelerated;
    }
    
    // Method to get vortex center for visual effects or player pull implementation
    public float getVortexCenterX() {
        return centerX;
    }
    
    public float getVortexCenterY() {
        return centerY;
    }
    
    public float getPlayerPullStrength() {
        return playerPullStrength;
    }
    
    public void setPlayerPullStrength(float strength) {
        this.playerPullStrength = strength;
    }
}
