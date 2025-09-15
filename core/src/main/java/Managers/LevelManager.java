package Managers;

import Enemies.FairySpawn;
import Levels.*;

public class LevelManager {
	
	private Level currentLvl;
	private int currentLvlWave = 0; // valores entre 0 a cantidad maxima - 1 de oleadas de un nivel
	private boolean areWavesOver;
	
	//private final Random random = new Random();
	
	public LevelManager() {}
	
	public void setCurrentLevel(int level) {
		switch(level) {
			case 1:
				System.out.println("ElegiLevelOne");
				currentLvl = new LevelOne();
				areWavesOver = false;
				break;
			case 2:
				System.out.println("ElegiLevelTwo");
				currentLvl = new LevelTwo();
				areWavesOver = false;
				break;
			case 3:
				break;
			case 4:
				break;
		}
	}
	
	public void changeCurrentWave() {currentLvlWave++;}
	public void resetCurrentWave() {currentLvlWave = 0;}
	
	public boolean areWavesOver() {
		if (currentLvlWave < currentLvl.getCantWaves()) {
			return areWavesOver;
		}
		else {
			areWavesOver = true;
			resetCurrentWave();
			return areWavesOver;
		}
	}
	
	public FairySpawn getFairyStartingPoint() {return currentLvl.getCoordsCurrentWave(currentLvlWave);}
	
	public boolean getFairyIsShooting() {return currentLvl.getShootsFirstCurrrentWave(currentLvlWave);}
	
	public int getLvlFairies() {return currentLvl.getCantFairies();}
	public int getLvlWaves() {return currentLvl.getCantWaves();}
	public int getFairiesCurrentWave() {return currentLvl.getCantFairiesSpecificWave(currentLvlWave);}
	public int getCurrentLvlWave() {return currentLvlWave;}
	public boolean getAreWavesOverState() {return areWavesOver;} 
	public void whatLevelIsIt() {
		if (currentLvl instanceof LevelOne) {
		    System.out.println("Level 1");
		}
		else {
			System.out.println("Level 2");
		}

		
	}
}