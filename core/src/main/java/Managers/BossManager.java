package Managers;

import java.util.Random;
import Enemies.Boss;
import Enemies.Enemy;

public class BossManager extends EnemyManager{
	private final Random random = new Random();
	private float[] speedOptions = {700,650,750,300,350,400};
	private int[] healthOptions = {36000,38000,40000,17500, 18500, 19000};
	private float[] idleTimeOptions = {10f, 5f};
	private Boss boss;
	
	public BossManager() {}
	
	public void createBoss(Boss b) {
		boss = b;
		int speedChoice = random.nextInt(getCantSpeedOptions()-3);
		int healthChoice = random.nextInt(getCantHealthOptions()-3);
		manageSpeed(b, speedChoice);
		manageHealth(b, healthChoice);
	}

	@Override
	public void manageSpeed(Enemy e, int choice) {
		e.setDefaultSpeed(speedOptions[choice]);
		e.setSpeed(speedOptions[choice]);
	}

	@Override
	public void manageHealth(Enemy e, int choice) {
		e.setHealth(healthOptions[choice]);
	}
	
	public int getCantSpeedOptions() {return speedOptions.length;}
	public int getCantHealthOptions() {return healthOptions.length;}
	public Boss getBoss() {return boss;}
	
	public boolean lowerBossHealthNSpeed(int hChoice, int sChoice) {
		int health  = healthOptions[hChoice];
		float speed = (float) speedOptions[sChoice];
		boss.lowerBossHealthNSpeed(health, speed, idleTimeOptions[0]);
		return false;
	}
	
	public void destroyBoss() {boss = null;}
}
