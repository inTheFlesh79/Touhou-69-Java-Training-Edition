package BulletHellPatterns;

import Enemies.EnemyBullet;

public class TargetedCirclePattern extends BulletHellPattern implements TwoStagePattern, PlayerTrackingPattern {
	private float reimuX, reimuY;
	private float tsSpeed;
	
	public TargetedCirclePattern() {
        cantBullet = 42;  // Cantidad de balas por círculo
        angle = 0;        // Ángulo inicial
        speed = 120f;     // Velocidad baja para un movimiento lento
        maxShootingTime = 4.5f;
        bulletGenInterval = 0.25f;
        currentBullet = 0;
        isDecelerated = true;
        
        tsSpeed = 300f;
	}
	
	@Override
	public void setReimuCoords(float x, float y) {
        reimuX = x;
        reimuY = y;
    }
	
	@Override
	public void generateBulletInPattern(float x, float y, EnemyBullet eb) {
        float direction = angle + (float) (2 * Math.PI * currentBullet / cantBullet);
        
        float bulletVelocityX = (float) Math.cos(direction) * speed;
        float bulletVelocityY = (float) Math.sin(direction) * speed;

        eb.setVelocityX(bulletVelocityX);
        eb.setVelocityY(bulletVelocityY);
        
        angle += 0.02f;
        currentBullet++;
        
        if (currentBullet >= cantBullet) {
            currentBullet = 0;
        }
        //System.out.println("CurrBullet = "+currentBullet);	
	}
	
	@Override
	public void applyMultiStage(EnemyBullet eb) {
		// calculate velocity towards last known Reimu position
        float dx = reimuX - eb.getX();
        float dy = reimuY - eb.getY();
        float len = (float)Math.sqrt(dx * dx + dy * dy);

        if (len != 0) {
            float velX = (dx / len) * tsSpeed;
            float velY = (dy / len) * tsSpeed;
            eb.setVelocityX(velX);
            eb.setVelocityY(velY);
        }
	}
	
	@Override
	public boolean hasDeceleration() {return isDecelerated;}
}