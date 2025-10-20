package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Managers.MusicManager;
import Managers.SceneManager;
import Sessions.SessionDataManager;
import Managers.GameObjectManager;

// NEW imports for fonts & layout
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class PantallaJuego implements Screen {
	private Touhou game;
    private OrthographicCamera screenCamera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private HUD hud;
    private boolean difficultyChange = false;
    private int nivel;
    private int cantCorrectas = -1;
    private int cantFallas = -1;
    private float exerciseTimer = 0f;
    private boolean waitingForExercise = false, correctasSet = false, isPaused = false, retryBuff = false, txtLvlStart, txtLvlEnd = false;
    // Managers
    private GameObjectManager gameMng;
    private MusicManager musicMng;
    private SceneManager sceneMng;

    // ---------- NEW: font + timing fields ----------
    private BitmapFont fontStart;     // used for level-start text (big)
    private BitmapFont fontEndBig;    // used for boss-defeated large blinking line
    private BitmapFont fontEndSmall;  // used for the second line under it
    private GlyphLayout layout;

    private float lvlStartTimer = 0f;
    private final float LVL_START_DURATION = 4.5f;

    private float lvlEndTimer = 0f;
    private final float LVL_END_DURATION = 5f;

	public PantallaJuego(int nivel, int vidas, int score, int power) {
		Touhou.setNivel(nivel);
	    game = Touhou.getInstance();
	    batch = game.getBatch();
	    screenCamera = game.getCamera();
	    viewport = game.getViewport();
	    hud = new HUD(batch, viewport);
	    gameMng = new GameObjectManager(batch, viewport, nivel, vidas, score, power);
	    sceneMng = new SceneManager(batch, nivel, viewport.getWorldWidth() - 360, viewport.getWorldHeight());
	    musicMng = Touhou.getInstance().getMusicMng();
	    musicMng.setupManager(nivel);
	    this.nivel = nivel;
	    gameMng.setScore(score);
	    txtLvlStart = true;

	    // create fonts based on viewport size
	    layout = new GlyphLayout();
	    createFonts();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    viewport.apply();
	    batch.setProjectionMatrix(screenCamera.combined);
	    
	    if (correctasSet == false && cantCorrectas > -1 && cantFallas > -1) {
			gameMng.setCorrectas(cantCorrectas);
			gameMng.setIntentosFallidos(cantFallas);
			System.out.println("correctas = "+cantCorrectas);
			correctasSet = true;
		}
	    
	    batch.begin();
	    
	    sceneMng.drawBg(viewport.getWorldWidth() - 360, viewport.getWorldHeight());
	    gameMng.update();
	    if (retryBuff) {gameMng.applyRewards(); retryBuff = false;}
	    
	    cooldownBeforeExercise(delta);//Does a cooldown before exercising of 5 seconds
	    musicSetup();
		
		//SUJETO A CAMBIO: REGISTRAR PROGRESO
		if (gameMng.isReimuDead()) {gameOver();}
		if (txtLvlStart) {levelText(true, delta);}
		if (txtLvlEnd && !gameMng.isBossAlive()) {levelText(false, delta);}
		batch.end();
		if (!gameMng.getHardMode() && !difficultyChange) {hud.setHardMode(false); difficultyChange = true;}
		hud.drawHUD(gameMng.getReimuVidas(), nivel, gameMng.getScore(), gameMng.getReimuDamage());
		
		//checkear si debemos pasar al siguiente nivel
	    levelManagement();
	}
	
	public void levelManagement() {
		if (!gameMng.isBossAlive() && !txtLvlEnd) {
			musicMng.stopBossMusic();
			if ((nivel+1) < 5) {
				Screen ss = new PantallaJuego(nivel+1, gameMng.getReimuVidas(), gameMng.getScore(), gameMng.getReimuDamage());
				ss.resize(1280, 960);
				game.setScreen(new PantallaHint(game, ss));
				gameMng.disposeGOM();
				dispose();
			}
			else {
				SessionDataManager.getInstance().getCurrentSession().setFinishedLevels(nivel);
				SessionDataManager.getInstance().setScore(gameMng.getScore());
				SessionDataManager.getInstance().finalizeAndSaveSession(SessionDataManager.getInstance().getCurrentSession().getPlayerTag());
				game.setHighScore(gameMng.getScore());
				Screen ss = new PantallaFinal();
				ss.resize(1280, 960);
				game.setScreen(ss);
				gameMng.disposeGOM();
				dispose();
			}
	    }
		
		// verificar si se presionó la tecla de pausa (ESC)
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !isPaused) {
		    isPaused = true;
		    musicMng.pauseMusic();
		    musicMng.playPause();
		    game.setScreen(new PantallaPausa(game, this)); // pass "this" to resume later
		}
		
		// VERIFICAR SI ESTA LISTO PARA EJERCITAR
		if (waitingForExercise && exerciseTimer >= 5f && !gameMng.areWeFightingBoss()) {
			musicMng.stopFairiesMusic();
			musicMng.stopBossMusic();
			game.setScreen(new PantallaEjercicios(game, musicMng, this, false));
			System.out.println("correctas en Pantalla Juego? = "+cantCorrectas);
			gameMng.setFightBoss(true);
			// ANTES DE SALIR CAMBIAR ESTADO DE CONTROL PARA NO REPETIR PANTALLA
			gameMng.setExerciseDone(true);
		}
	}

	public void musicSetup() {
	    boolean fairyCondition = !gameMng.areWavesOver() && !musicMng.isPlayingFairyTheme();
	    boolean bossCondition = !gameMng.AreFairiesAlive() && gameMng.areWavesOver() && 
	                            !musicMng.isPlayingBossTheme() && !waitingForExercise;
	    if (fairyCondition) {musicMng.pickFairiesLvlMusic();} 
	    else if (bossCondition) {musicMng.pickBossLvlMusic();}
	}
	
	public void cooldownBeforeExercise(float delta) {
		if (gameMng.readyToExercise() && !gameMng.areWeFightingBoss() && !waitingForExercise) {
		    waitingForExercise = true;
		    exerciseTimer = 0f; // reset timer
		}
		if (waitingForExercise) {exerciseTimer += delta;}
	}
	
	public void gameOver() {
		SessionDataManager.getInstance().getCurrentSession().setFinishedLevels(nivel);
		SessionDataManager.getInstance().setScore(gameMng.getScore());
		musicMng.stopBossMusic();
		musicMng.stopFairiesMusic();
		if (gameMng.getScore() > game.getHighScore())
			game.setHighScore(gameMng.getScore());
		Screen ss = new PantallaGameOver();
		ss.resize(1280, 960);
		game.setScreen(ss);
		gameMng.disposeGOM();
		dispose();
	}
	
	public void levelText(boolean lvlStart, float delta) {
	    // compute available left area width and center
	    float leftWidth = viewport.getWorldWidth() - 360f; // 920 in default 1280 window
	    float centerX = leftWidth / 2f;
	    float centerY = viewport.getWorldHeight() / 2f;

	    if (lvlStart) {
	        // Fade-in 1f, hold 2.5f, fade-out 1f -> total 4.5f
	        lvlStartTimer += delta;
	        float t = lvlStartTimer;

	        final float fadeIn = 1f;
	        final float hold   = 2.5f;
	        final float fadeOut = 1f;

	        float alpha = 0f;
	        if (t <= fadeIn) {
	            // fade in (0 -> 1 over 1s)
	            alpha = t / fadeIn;
	        } else if (t <= (fadeIn + hold)) {
	            // hold fully visible
	            alpha = 1f;
	        } else if (t <= (fadeIn + hold + fadeOut)) {
	            // fade out (1 -> 0 over 1s)
	            alpha = 1f - ((t - (fadeIn + hold)) / fadeOut);
	        } else {
	            alpha = 0f;
	        }

	        // clamp alpha
	        if (alpha < 0f) alpha = 0f;
	        if (alpha > 1f) alpha = 1f;

	        // compose text: "LEVEL X - <Name>"
	        String title = "LEVEL " + nivel + " - " + getLevelName(nivel);
	        layout.setText(fontStart, title);
	        float textWidth = layout.width;
	        float textHeight = layout.height;

	        // draw with alpha (font color white, border black was set on generation)
	        fontStart.setColor(1f, 1f, 1f, alpha);
	        fontStart.draw(batch, title, centerX - textWidth / 2f, centerY + textHeight / 2f);

	        // finish condition: switch to end text when the full sequence is done
	        if (lvlStartTimer >= LVL_START_DURATION) {
	            txtLvlStart = false;
	            txtLvlEnd = true; // start showing end text after start text finishes (as you had)
	            lvlStartTimer = 0f;
	            // ensure end timer is reset so it begins its hold/blink timing fresh
	            lvlEndTimer = 0f;
	        }
	    }
	    else {
	    	// end text: blinking for LVL_END_DURATION
			lvlEndTimer += delta;
			// blinking: use sin to flip visibility
			float blinkFreq = 3f; // tweakable (higher = faster)
			boolean visible = Math.sin(lvlEndTimer * blinkFreq * Math.PI) > 0f;
			String primary, secondary;
			if ((nivel+1) < 5) {
				primary = "BOSS DEFEATED!!! YOU GAINED +1000 SCORE!!!";
				secondary = "NEXT LEVEL INCOMING!";
			}
			else {
				primary = "GAME FINISHED!!! THANKS FOR PLAYING THIS DEMO :D";
				secondary = "YOU ARE  T H I S  CLOSE TO GETTING DECIMAS!";
			}

			// primary
			layout.setText(fontEndBig, primary);
			float pW = layout.width;
			float pH = layout.height;
			if (visible) {
				fontEndBig.setColor(1f, 1f, 1f, 1f);
				fontEndBig.draw(batch, primary, centerX - pW / 2f, centerY + pH / 2f);
			}

			// secondary below primary
			layout.setText(fontEndSmall, secondary);
			float sW = layout.width;
			fontEndSmall.setColor(1f, 1f, 1f, 1f);
			float padding = 8f;
			fontEndSmall.draw(batch, secondary, centerX - sW / 2f, centerY - pH / 2f - padding);

			// end condition: stop showing text and allow levelManagement to proceed
			if (lvlEndTimer >= LVL_END_DURATION) {
				txtLvlEnd = false;
				lvlEndTimer = 0f;
				// now levelManagement will see bossDefeatedHandled true and txtLvlEnd false and will transition
			}
	    }
	}
	
	public void setPaused(boolean paused) { this.isPaused = paused;	}
	public void setCorrectas(int c) {cantCorrectas = c;}
	public void setIntentosFallidos(int i) {cantFallas = i;}
	public void setRetryBuff(boolean bool) {retryBuff = bool;}

	public MusicManager getMusicManager() {return musicMng;}
	
	@Override
	public void show() {}
	@Override
	public void resize(int width, int height) {
	    viewport.update(width, height, true);
	    createFonts();
	}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void hide() {}
	@Override
	public void dispose() {
		sceneMng.dispose();
		musicMng.dispose();
		// dispose fonts
		if (fontStart != null) fontStart.dispose();
		if (fontEndBig != null) fontEndBig.dispose();
		if (fontEndSmall != null) fontEndSmall.dispose();
	}
	
	// ---------- HELPER METHODS FOR FONTS & LEVEL NAMES ----------
	private void createFonts() {
		// dispose previous fonts if present
		if (fontStart != null) { fontStart.dispose(); fontStart = null; }
		if (fontEndBig != null) { fontEndBig.dispose(); fontEndBig = null; }
		if (fontEndSmall != null) { fontEndSmall.dispose(); fontEndSmall = null; }

		// sizes relative to viewport world height so resizing keeps proportions
		int bigSize = Math.max(16, (int)(viewport.getWorldHeight() * 0.05f));   // ~10% of height
		int endBigSize = Math.max(20, (int)(viewport.getWorldHeight() * 0.03f)); // slightly smaller
		int endSmallSize = Math.max(14, (int)(viewport.getWorldHeight() * 0.03f));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("thFont.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.borderColor = Color.BLACK; // outlined black
		parameter.borderWidth = Math.max(1, (int)(bigSize * 0.06f)); // relative border width
		parameter.color = Color.WHITE; // default color (we still set alpha when drawing)
		parameter.size = bigSize;
		fontStart = generator.generateFont(parameter);

		// font for end primary
		parameter.borderWidth = Math.max(1, (int)(endBigSize * 0.06f));
		parameter.size = endBigSize;
		fontEndBig = generator.generateFont(parameter);

		// font for end secondary
		parameter.borderWidth = Math.max(1, (int)(endSmallSize * 0.06f));
		parameter.size = endSmallSize;
		fontEndSmall = generator.generateFont(parameter);

		generator.dispose();
	}

	private String getLevelName(int lvl) {
		// Provide real names for your levels here. Default fallback provided.
		switch (lvl) {
			case 1: return "GREEN VOID";
			case 2: return "SILVER CREEK";
			case 3: return "MARBLE REALM";
			case 4: return "THE PURPLE SIDE...";
			default: return "UNKNOWN";
		}
	}
}