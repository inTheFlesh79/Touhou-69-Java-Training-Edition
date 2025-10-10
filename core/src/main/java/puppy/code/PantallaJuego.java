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
import Managers.GameObjectManager;

public class PantallaJuego implements Screen {
	private Touhou game;
    private OrthographicCamera screenCamera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private HUD hud;
    private int nivel;
    private int cantCorrectas = -1;
    private float exerciseTimer = 0f;
    private boolean waitingForExercise = false;
    private boolean correctasSet = false;
    private boolean isPaused = false;
    // Managers
    private GameObjectManager gameMng;
    private MusicManager musicMng;
    private SceneManager sceneMng;
    
	public PantallaJuego(int nivel, int vidas, int score, int power) {
	    game = Touhou.getInstance();
	    batch = game.getBatch();
	    screenCamera = game.getCamera();
	    viewport = game.getViewport();
	    hud = new HUD(batch, viewport);
	    gameMng = new GameObjectManager(batch, viewport, nivel, vidas, score, power);
	    sceneMng = new SceneManager(batch, nivel, viewport.getWorldWidth() - 360, viewport.getWorldHeight());
	    musicMng = new MusicManager(nivel);
	    this.nivel = nivel;
	    gameMng.setScore(score);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    viewport.apply();
	    batch.setProjectionMatrix(screenCamera.combined);
	    
	    // --- GAME AREA (0 → 920) ---
	    if (correctasSet == false && cantCorrectas > -1) {
			gameMng.setCorrectas(cantCorrectas);
			System.out.println("correctas = "+cantCorrectas);
			correctasSet = true;
		}
	    batch.begin();
	    sceneMng.drawBg(viewport.getWorldWidth() - 360, viewport.getWorldHeight());
	    gameMng.update();
	    cooldownBeforeExercise(delta);//Does a cooldown before exercising of 5 seconds
	    musicSetup();
		
		//SUJETO A CAMBIO: REGISTRAR PROGRESO
		if (gameMng.isReimuDead()) {
			musicMng.stopBossMusic();
			musicMng.stopFairiesMusic();
			if (gameMng.getScore() > game.getHighScore())
				game.setHighScore(gameMng.getScore());
			Screen ss = new PantallaGameOver(musicMng);
			ss.resize(1280, 960);
			game.setScreen(ss);
			gameMng.disposeGOM();
			dispose();
		}
		
		batch.end();
		hud.drawHUD(gameMng.getReimuVidas(), nivel, gameMng.getScore(), gameMng.getReimuDamage());

		//checkear si debemos pasar al siguiente nivel
	    levelManagement();
	}
	
	public void levelManagement() {
		if (!gameMng.isBossAlive()) {
			musicMng.stopBossMusic();
			if ((nivel+1) < 5) {
				Screen ss = new PantallaJuego(nivel+1, gameMng.getReimuVidas(), gameMng.getScore(), gameMng.getReimuDamage());
				ss.resize(1280, 960);
				game.setScreen(ss);
				gameMng.disposeGOM();
				dispose();
			}
			else {
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
		
		// OPCIONES DE DESARROLLADOR DEV (se eliminan)
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			musicMng.stopFairiesMusic();
			musicMng.stopBossMusic();
			game.setScreen(new PantallaEjercicios(game, musicMng, this, false));
			System.out.println("correctas en Pantalla Juego? = "+cantCorrectas);
			gameMng.setFightBoss(true);
			// ANTES DE SALIR CAMBIAR ESTADO DE CONTROL PARA NO REPETIR PANTALLA
			gameMng.setExerciseDone(true);
		}
		// DEV
		if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
			musicMng.stopFairiesMusic();
			musicMng.stopBossMusic();
			game.setScreen(new PantallaHint(game, this));
		}
		// DEV
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			musicMng.stopFairiesMusic();
			musicMng.stopBossMusic();
			game.setScreen(new PantallaCodigo(game, this));
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
		if (!gameMng.areWavesOver() && !musicMng.isPlayingFairyTheme()) {
			musicMng.pickFairiesLvlMusic();
		}
		else if (!gameMng.AreFairiesAlive() && gameMng.areWavesOver() && !musicMng.isPlayingBossTheme() && !waitingForExercise){
			musicMng.pickBossLvlMusic();
		}
	}
	
	public void cooldownBeforeExercise(float delta) {
		if (gameMng.readyToExercise() && !gameMng.areWeFightingBoss() && !waitingForExercise) {
		    waitingForExercise = true;
		    exerciseTimer = 0f; // reset timer
		}
		
		if (waitingForExercise) {exerciseTimer += delta;}
	}
	
	public void setPaused(boolean paused) {
	    this.isPaused = paused;
	}

	public MusicManager getMusicManager() {
	    return musicMng;
	}
	
	public void setCorrectas(int c) {cantCorrectas = c;}
	
	@Override
	public void show() {}

	@Override
	public void resize(int width, int height) {
	    viewport.update(width, height, true);
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
	}
}