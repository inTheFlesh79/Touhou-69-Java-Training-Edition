package BulletHellPatterns;

import Enemies.EnemyBullet;

public class DualOrbitPattern extends BulletHellPattern implements TwoStagePattern, PlayerTrackingPattern {
    private float innerRadius;
    private float outerRadius;
    private float innerRotationSpeed;
    private float outerRotationSpeed;
    private float currentInnerAngle;
    private float currentOuterAngle;

    private float reimuX;
    private float reimuY;

    private float tsSpeedMin = 760f;
    private float tsSpeedMax = 860f;

    public DualOrbitPattern() {
        this.cantBullet = 32; // 8 inner + 8 outer
        this.speed = 0f; // stationary while orbiting
        this.innerRadius = 185f;
        this.outerRadius = 370f;

        // Rotation directions
        this.innerRotationSpeed = (float) Math.toRadians(8);
        this.outerRotationSpeed = (float) Math.toRadians(-6);

        this.currentInnerAngle = 0f;
        this.currentOuterAngle = 0f;

        this.maxShootingTime = 5.0f;
        this.bulletGenInterval = 0.15f;
        this.currentBullet = 0;
        this.isDecelerated = true;
        this.decelerationRate = 200f;
    }

    @Override
    public void setReimuCoords(float x, float y) {
        this.reimuX = x;
        this.reimuY = y;
    }

    @Override
    public void generateBulletInPattern(float x, float y, EnemyBullet eb) {
        // Determine if bullet belongs to inner or outer ring
        boolean isInner = (currentBullet < cantBullet / 2);
        int index = currentBullet % (cantBullet / 2);

        // Compute base angle for the ring
        float angleStep = (float) (2 * Math.PI / (cantBullet / 2));
        float baseAngle = isInner ? currentInnerAngle : currentOuterAngle;
        float radius = isInner ? innerRadius : outerRadius;

        // Final angle for this bullet
        float bulletAngle = baseAngle + index * angleStep;

        // Set position based on orbit center (x, y)
        float bx = x + (float) Math.cos(bulletAngle) * radius;
        float by = y + (float) Math.sin(bulletAngle) * radius;
        eb.setX(bx);
        eb.setY(by);

        // No initial velocity — bullets orbit visually from position updates
        eb.setVelocityX(0);
        eb.setVelocityY(0);

        // Advance for next bullet
        currentBullet++;
        if (currentBullet >= cantBullet) {
            currentBullet = 0;
            // Increment orbital rotation after each volley
            currentInnerAngle += innerRotationSpeed;
            currentOuterAngle += outerRotationSpeed;

            // Normalize to 0–2π range
            if (currentInnerAngle > Math.PI * 2) currentInnerAngle -= (float) (Math.PI * 2);
            if (currentOuterAngle < -Math.PI * 2) currentOuterAngle += (float) (Math.PI * 2);
        }
    }

    @Override
    public void applyMultiStage(EnemyBullet eb) {
        // Re-aim toward player position
        float dx = reimuX - eb.getX();
        float dy = reimuY - eb.getY();
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len == 0f) return;

        dx /= len;
        dy /= len;

        // Randomized Stage 2 speed
        float stage2Speed = tsSpeedMin + (float) Math.random() * (tsSpeedMax - tsSpeedMin);
        eb.setVelocityX(dx * stage2Speed);
        eb.setVelocityY(dy * stage2Speed);
    }

    @Override
    public boolean hasDeceleration() {
        return isDecelerated;
    }

    // Optional configuration methods
    public void setRadii(float inner, float outer) {
        this.innerRadius = inner;
        this.outerRadius = outer;
    }

    public void setStageTwoSpeedRange(float min, float max) {
        this.tsSpeedMin = min;
        this.tsSpeedMax = max;
    }

    public void setRotationSpeeds(float innerSpeed, float outerSpeed) {
        this.innerRotationSpeed = innerSpeed;
        this.outerRotationSpeed = outerSpeed;
    }
}