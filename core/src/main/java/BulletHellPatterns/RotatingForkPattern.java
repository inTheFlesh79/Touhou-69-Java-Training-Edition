package BulletHellPatterns;

import java.util.Random;

import Enemies.EnemyBullet;

/**
 * RotatingForkPattern (revised)
 * - 4 tridents (3 bullets each) arranged perpendicular to each other (12 bullets total)
 * - The whole assembly rotates slowly around the Boss, but rotation only advances
 *   after a full 12-bullet cycle so each trident's 3 prongs are emitted aligned.
 * - applyMultiStage randomizes velocity (haywire) when the stage changes.
 */
public class RotatingForkPattern extends BulletHellPattern implements TwoStagePattern {
    private final Random rnd = new Random();

    private float spreadAngle;      // angle between center bullet and side bullets
    private float rotationOffset;   // global rotation applied to all 4 tridents
    private float rotationSpeed;    // how much to rotate per full cycle (tweak)
    private final float[] baseGroupAngles; // 4 base angles (0, PI/2, PI, 3PI/2)
    
    // second-stage speed range
    private float tsSpeedMin = 100f;
    private float tsSpeedMax = 130f;

    public RotatingForkPattern() {
        // 4 tridents × 3 bullets each = 12 bullets
        this.cantBullet = 12;
        this.speed = 260f;        // initial (stage 1) bullet speed
        this.spreadAngle = (float) (Math.PI / 9.0); // ~20 degrees separation between prongs
        this.angle = 0f;
        this.maxShootingTime = 6.0f;
        this.bulletGenInterval = 0.18f;
        this.currentBullet = 0;

        this.rotationOffset = 0f;
        // small rotation per cycle — you can reduce for slower rotation
        this.rotationSpeed = 0.06f;

        this.baseGroupAngles = new float[] {
            0f,
            (float)(Math.PI / 2.0),
            (float)(Math.PI),
            (float)(3.0 * Math.PI / 2.0)
        };

        this.isDecelerated = true;
        this.decelerationRate = 300f;
    }

    /**
     * Generate one bullet in the rotating fork (trident) pattern.
     * Important: rotationOffset is NOT advanced here per bullet — it's advanced
     * only when the full cantBullet cycle completes (so prongs are straight).
     */
    @Override
    public void generateBulletInPattern(float x, float y, EnemyBullet eb) {
        // Determine which group (trident) and which prong inside that group
        int groupIndex = currentBullet / 3;        // 0..3
        int prongIndex = currentBullet % 3;        // 0..2 (left, center, right)
        
        // base angle for this group (perpendicular arrangement) + global rotation
        float groupBase = baseGroupAngles[groupIndex] + rotationOffset;
        
        // prong offset: prongIndex 0 => -spread, 1 => 0, 2 => +spread
        float prongOffset = (prongIndex - 1) * spreadAngle;
        float direction = groupBase + prongOffset;

        // compute velocity components for stage 1
        float velX = (float) Math.cos(direction) * speed;
        float velY = (float) Math.sin(direction) * speed;
        eb.setVelocityX(velX);
        eb.setVelocityY(velY);

        // increment currentBullet and wrap-around
        currentBullet++;
        if (currentBullet >= cantBullet) {
            currentBullet = 0;
            // Only advance rotation after the whole cycle finished so the 3 prongs
            // of each trident are emitted aligned (no spiral).
            rotationOffset += rotationSpeed;
            if (rotationOffset > Math.PI * 2) rotationOffset -= (float)(Math.PI * 2);
        }
    }

    /**
     * Second stage: random velocity (haywire).
     */
    @Override
    public void applyMultiStage(EnemyBullet eb) {
        float randAngle = (float) (rnd.nextFloat() * Math.PI * 2.0);
        float randSpeed = tsSpeedMin + rnd.nextFloat() * (tsSpeedMax - tsSpeedMin);

        float velX = (float) Math.cos(randAngle) * randSpeed;
        float velY = (float) Math.sin(randAngle) * randSpeed;

        eb.setVelocityX(velX);
        eb.setVelocityY(velY);
    }

    @Override
    public boolean hasDeceleration() {
        return isDecelerated;
    }

    // setters to tweak behavior
    public void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }
    public void setSpreadAngle(float spreadAngle) { this.spreadAngle = spreadAngle; }
    public void setStageOneSpeed(float speed) { this.speed = speed; }
    public void setStageTwoSpeedRange(float min, float max) { this.tsSpeedMin = min; this.tsSpeedMax = max; }
}

