package Managers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {
	private int stageThemeChoice;
	private boolean isPlayingFairyTheme = false;
    private boolean isPlayingBossTheme = false;
	private ArrayList<Music> bossThemes;
	private ArrayList<Music> fairiesThemes;
	private Music lastPausedTrack = null;
	private Music mainMenu;
	
	private Sound correct;
	private Sound incorrect;
	private Sound pause;
	
	public MusicManager() {
		bossThemes = new ArrayList<>();
		fairiesThemes = new ArrayList<>();
		// sonidos correcto e incorrecto
		correct = Gdx.audio.newSound(Gdx.files.internal("correcto.ogg"));
        incorrect = Gdx.audio.newSound(Gdx.files.internal("incorrecto.ogg"));
        
        // pause menu;
        pause = Gdx.audio.newSound(Gdx.files.internal("pauseSound.ogg"));
        
        mainMenu = Gdx.audio.newMusic(Gdx.files.internal("Imperishable Night - 1 - Eiyashou ~ Eastern Night.ogg"));
		
		// fairies
		fairiesThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Imperishable Night - 2 - Genshi no Yoru ~ Ghostly Eyes.ogg"))));
		fairiesThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Imperishable Night - 8 - Eiya no Mukui ~ Imperishable Night.ogg"))));
		fairiesThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Imperishable Night - 13 - Voyage 1969.ogg"))));
		fairiesThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Imperishable Night - 17 - Extend Ash ~ Houraijin.ogg"))));
		
		// bosses
		bossThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Imperishable Night - 3 - Shunshun Shuugetsu ~ Mooned Insect.ogg"))));
		bossThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Imperishable Night - 10 - Koi-iro Master Spark.ogg"))));
		bossThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Imperishable Night - 14 - Sennen Gensoukyou ~ History of the Moon.ogg"))));
		bossThemes.add((Music) (Gdx.audio.newMusic(Gdx.files.internal("Imperishable Night - 18 - Tsuki Made Todoke, Fushi no Kemuri.ogg"))));
	}
	
	public void setupManager(int level) {
		level -=1;
		stageThemeChoice = level;
	}
	
	public void pickFairiesLvlMusic() {
		switch (stageThemeChoice) {
			case 0:
				playFairiesMusic();
			case 1:
				playFairiesMusic();
			case 2:
				playFairiesMusic();
			case 3:
				playFairiesMusic();
				
		}
	}
	
	public void pickBossLvlMusic() {
		switch (stageThemeChoice) {
			case 0:
				playBossMusic();
			case 1:
				playBossMusic();
			case 2:
				playBossMusic();
			case 3:
				playBossMusic();
		}
	}
	
	public void playFairiesMusic() {
		Music track = fairiesThemes.get(stageThemeChoice);
		track.setVolume(0.4f);  // 40% volume
	    track.play();
		loopFairiesMusic();
		setPlayingFairyTheme(true);
	}
	
	public void playBossMusic() {
		stopFairiesMusic();
		setPlayingFairyTheme(false);
		Music track = bossThemes.get(stageThemeChoice);
		track.setVolume(0.4f);  // 40% volume
	    track.play();
		loopBossMusic();
		setPlayingBossTheme(true);
	}
	
	public void playMainMenu() {
		mainMenu.setVolume(0.4f);  // 40% volume
	    mainMenu.play();
	    mainMenu.setLooping(true);
	}
	
	public void stopMainMenu() {
		mainMenu.stop();
	}
	
	public void pauseMusic() {
	    if (bossThemes.get(stageThemeChoice).isPlaying()) {
	        lastPausedTrack = bossThemes.get(stageThemeChoice);
	        lastPausedTrack.pause();
	    } else if (fairiesThemes.get(stageThemeChoice).isPlaying()) {
	        lastPausedTrack = fairiesThemes.get(stageThemeChoice);
	        lastPausedTrack.pause();
	    }
	}

	public void unpauseMusic() {
	    if (lastPausedTrack != null) {
	        lastPausedTrack.play();
	        lastPausedTrack = null; // reset so it doesn’t accidentally replay later
	    }
	}
	
	// metodos para reproducir sonidos
	public void playCorrect() {
        correct.play(1.0f); // volumen = 1.0 (máximo)
    }

    public void playIncorrect() {
        incorrect.play(1.0f);
    }
    
    public void playPause() {
    	pause.play();
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
	
	public void resetMusicMng() {
		isPlayingFairyTheme = false;
	    isPlayingBossTheme = false;
	    lastPausedTrack = null;
	}
	
	public void dispose() {
		for (int i = 0; i < bossThemes.size(); i++) {
			bossThemes.get(i).dispose();
			fairiesThemes.get(i).dispose();
    	}
	}
}