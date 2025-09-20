package Managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Props.HealthBar;

public class PropManager {
	private HealthBar bossHPBar; 
	
	public PropManager() {}
	
	public void createBossHPBar(int defaultHealth) {bossHPBar = new HealthBar(defaultHealth, 650, 12, 4);}
	public void drawBossHPBar(SpriteBatch batch) {bossHPBar.draw(batch);}
	public HealthBar getBossHPBar() {return bossHPBar;}
}
