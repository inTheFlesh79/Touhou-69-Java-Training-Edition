package BulletHellPatterns;

import Enemies.EnemyBullet;

public class HeptaSprayPattern extends BulletHellPattern {
    private float spreadAngle;          // total spread across the 7 bullets
    private float baseAngleLeft;        // center direction for left spray
    private float baseAngleRight;       // center direction for right spray
    private int volleySeed;             // local pseudo-time counter, not shared across fairies

    public HeptaSprayPattern() {
        this.cantBullet = 7;            // 7 bullets per burst
        this.speed = 300f;
        this.spreadAngle = (float) Math.toRadians(30); // total spread = 30°
        this.baseAngleLeft = (float) Math.toRadians(255);  // down-left (~225°)
        this.baseAngleRight = (float) Math.toRadians(285); // down-right (~315°)
        this.angle = 0f;

        this.maxShootingTime = 5.0f;
        this.bulletGenInterval = 0.25f; // time between bursts
        this.currentBullet = 0;
        this.isDecelerated = false;

        this.volleySeed = 0;
    }

    @Override
    public void generateBulletInPattern(float x, float y, EnemyBullet eb) {
        // Alternate based on per-origin hash and local volley seed
        boolean shootLeft = ((int)((x * 17 + y * 37 + volleySeed * 91)) % 2 == 0);
        float centerAngle = shootLeft ? baseAngleLeft : baseAngleRight;

        // Compute bullet offset angle
        int index = currentBullet;
        float step = spreadAngle / (cantBullet - 1);
        float direction = centerAngle - (spreadAngle / 2f) + index * step;

        // Set bullet velocity
        float velX = (float) Math.cos(direction) * speed;
        float velY = (float) Math.sin(direction) * speed;
        eb.setVelocityX(velX);
        eb.setVelocityY(velY);

        // Advance bullet index; increment seed after each full volley
        currentBullet++;
        if (currentBullet >= cantBullet) {
            currentBullet = 0;
            volleySeed++;
        }
    }
}
