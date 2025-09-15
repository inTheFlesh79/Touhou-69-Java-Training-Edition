package Managers;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
	private int stageThemeChoice;
	private boolean isPlayingFairyTheme = false;
    private boolean isPlayingBossTheme = false;
	private ArrayList<Music> bossThemes;
	private ArrayList<Music> fairiesThemes;
	private Random random = new Random();
	
	public MusicManager() {
		bossThemes = new ArrayList<>();
		fairiesThemes = new ArrayList<>();
		
		// fairies
		
		fairiesThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Ghostly Eyes.mp3"))));
		fairiesThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Night Bird.mp3"))));
		fairiesThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Old World.mp3"))));
		fairiesThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Imperishable Night.mp3"))));
		
		// bosses
		
		bossThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Mooned Insect.mp3"))));
		bossThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Mou Uta Shika Kikoenai.mp3"))));
		bossThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Plain Asia.mp3"))));
		bossThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Dream Battle.mp3"))));
		
		stageThemeChoice = random.nextInt(bossThemes.size());
	}
	
	public void playFairiesMusic() {
		fairiesThemes.get(stageThemeChoice).play();
		loopFairiesMusic();
		setPlayingFairyTheme(true);
	}
	
	public void playBossMusic() {
		stopFairiesMusic();
		setPlayingFairyTheme(false);
		bossThemes.get(stageThemeChoice).play();
		loopBossMusic();
		setPlayingBossTheme(true);
	}
	
	public void stopFairiesMusic() {fairiesThemes.get(stageThemeChoice).stop();}
	public void stopBossMusic() {bossThemes.get(stageThemeChoice).stop();}
	public void loopFairiesMusic() {fairiesThemes.get(stageThemeChoice).setLooping(true);}
	public void loopBossMusic() {bossThemes.get(stageThemeChoice).setLooping(true);}
	
	public boolean isPlayingFairyTheme() {return isPlayingFairyTheme;}
	public void setPlayingFairyTheme(boolean playingFairyTheme) { this.isPlayingFairyTheme = playingFairyTheme;}
	
	public boolean isPlayingBossTheme() {return isPlayingBossTheme;} 
	public void setPlayingBossTheme(boolean playingBossTheme) { this.isPlayingBossTheme = playingBossTheme;}
	
	public int getSizeFairiesTheme() {return fairiesThemes.size();}
	public int getSizeBossTheme() {return bossThemes.size();}
	
	public void dispose() {
		for (int i = 0; i < bossThemes.size(); i++) {
			bossThemes.get(i).dispose();
			fairiesThemes.get(i).dispose();
    	}
	}
}