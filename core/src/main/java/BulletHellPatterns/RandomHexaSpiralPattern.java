package BulletHellPatterns;

import java.util.Random;
import Enemies.EnemyBullet;

public class RandomHexaSpiralPattern extends BulletHellPattern implements TwoStagePattern {
    private final Random rnd = new Random();

    private float rotationOffset;     // global rotation angle
    private float rotationSpeed;      // how much each volley rotates
    private float armAngles[];        // base angles for 6 arms

    private float tsSpeedMin = 110f;
    private float tsSpeedMax = 150f;

    public RandomHexaSpiralPattern() {
        this.cantBullet = 6;              // one bullet per arm per tick
        this.speed = 260f;                // stage 1 speed
        this.angle = 0f;
        this.maxShootingTime = 5.0f;
        this.bulletGenInterval = 0.1f;   // tighter spacing → smoother spiral
        this.currentBullet = 0;

        this.rotationOffset = 0f;
        this.rotationSpeed = 0.025f;       // tweak for faster or slower spin

        this.armAngles = new float[6];
        for (int i = 0; i < 6; i++) {
            armAngles[i] = (float) (i * Math.PI / 3.0); // every 60°
        }

        this.isDecelerated = true;
        this.decelerationRate = 200f;
    }

    @Override
    public void generateBulletInPattern(float x, float y, EnemyBullet eb) {
        // Determine which arm fires this bullet
        int armIndex = currentBullet;
        float direction = armAngles[armIndex] + rotationOffset;

        float velX = (float) Math.cos(direction) * speed;
        float velY = (float) Math.sin(direction) * speed;
        eb.setVelocityX(velX);
        eb.setVelocityY(velY);

        // advance arm
        currentBullet++;
        if (currentBullet >= cantBullet) {
            currentBullet = 0;
            rotationOffset += rotationSpeed; // rotate entire formation per volley
            if (rotationOffset > Math.PI * 2) rotationOffset -= (float) (Math.PI * 2);
        }
    }

    @Override
    public void applyMultiStage(EnemyBullet eb) {
        float randAngle = (float) (rnd.nextFloat() * Math.PI * 2.0);
        float randSpeed = tsSpeedMin + rnd.nextFloat() * (tsSpeedMax - tsSpeedMin);
        eb.setVelocityX((float) Math.cos(randAngle) * randSpeed);
        eb.setVelocityY((float) Math.sin(randAngle) * randSpeed);
    }

    @Override
    public boolean hasDeceleration() {
        return isDecelerated;
    }

    public void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }
    public void setStageOneSpeed(float speed) { this.speed = speed; }
    public void setStageTwoSpeedRange(float min, float max) { this.tsSpeedMin = min; this.tsSpeedMax = max; }
}

