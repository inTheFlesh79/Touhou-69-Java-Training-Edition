package Managers;

import java.util.Random;

import BulletHellPatterns.BulletHellPattern;
import BulletHellPatterns.CirclePattern;
import BulletHellPatterns.DynamicSpiralPattern;
import BulletHellPatterns.ForkPattern;
import BulletHellPatterns.SpiralPattern;
import Enemies.Boss;
import Enemies.Enemy;

public class BossManager extends EnemyManager{
	private final Random random = new Random();
	private float[] speedOptions = {800,900,1000,400,550,590};
	private int[] healthOptions = {600,800,1000,25, 27, 30};
	private Boss boss;
	
	public BossManager() {
		bhpType.add(new SpiralPattern());
		bhpType.add(new DynamicSpiralPattern());
		bhpType.add(new CirclePattern());
		bhpType.add(new ForkPattern());
	}
	
	public void createBoss(Boss b) {
		boss = b;
		int speedChoice = random.nextInt(getCantSpeedOptions()-3);
		int healthChoice = random.nextInt(getCantHealthOptions()-3);
		manageSpeed(b, speedChoice);
		manageHealth(b, healthChoice);
		manageBHPType(b,0);
	}

	@Override
	public void manageBHPType(Enemy e, int choice) {
		if (e instanceof Boss) {
			Boss boss = (Boss) e;
			for (choice = 0; choice < bhpType.size() ; choice++) {
				BulletHellPattern bhpChosen = bhpType.get(choice);
				boss.setBossBHPlist(bhpChosen);
			}
		}
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
	
	public void lowerBossHealthNSpeed(int hChoice, int sChoice) {
		int health  = healthOptions[hChoice];
		float speed = (float) speedOptions[sChoice];
		boss.lowerBossHealthNSpeed(health, speed);
	}
	
	public void destroyBoss() {boss = null;}
}
