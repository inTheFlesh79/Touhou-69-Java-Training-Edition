package BulletHellPatterns;

import Enemies.EnemyBullet;

public abstract class BulletHellPattern {
	protected int currentBullet;
	protected float speed;
	protected int cantBullet;
	protected float angle = 0f;
	protected float maxShootingTime;
	protected float bulletGenInterval;
    
    public abstract void generateBulletInPattern(float x, float y, EnemyBullet eb);
    
    public void setSpeed(float speed) {this.speed = speed;}
    public void setAngle(float angle) {this.angle = angle;}
    
    public int getCantBullet() {return this.cantBullet;}
    public float getMaxShootingTime() {return this.maxShootingTime;}
    public float getBulletGenInterval() {return this.bulletGenInterval;}
}