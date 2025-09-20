package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Managers.MusicManager;
import Managers.SceneManager;
import Managers.GameObjectManager;

public class PantallaJuego implements Screen {
	private Touhou game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;//batch
	private int nivel;
	private int cantCorrectas = -1;
	private float exerciseTimer = 0f;
	private boolean waitingForExercise = false;
	private boolean correctasSet = false;
	private boolean isPaused = false;
	
	// Manager de Personajes con comportamientos dinamicos
	private GameObjectManager gameMng;
	// Manager para controlar musica
    private MusicManager musicMng;
    // Manager para controlar la imagen de fondo del juego
    private SceneManager sceneMng;
    
	public PantallaJuego(int nivel, int vidas, int score, int power) {
		game = Touhou.getInstance();
		batch = game.getBatch();
		gameMng = new GameObjectManager(batch, nivel, vidas, score, power);
		sceneMng = new SceneManager(batch, nivel);
		musicMng = new MusicManager(nivel);
		this.nivel = nivel;
		gameMng.setScore(score);
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 1200, 800);
	}
    
	public void dibujaHUD() {
		CharSequence str = "Lives: "+ gameMng.getReimuVidas() +" Level: "+nivel;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+ gameMng.getScore(), Gdx.graphics.getWidth()-250, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2+70, 30);
		game.getFont().draw(batch, "Power:"+gameMng.getReimuDamage(), Gdx.graphics.getWidth()/2-225, 30);
	}
	
	@Override
	public void render(float delta) {
		//System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		if (correctasSet == false && cantCorrectas > -1) {
			gameMng.setCorrectas(cantCorrectas);
			System.out.println("correctas = "+cantCorrectas);
			correctasSet = true;
		}
		
		sceneMng.drawBg();
		dibujaHUD();
		gameMng.update(); // Maneja los objetos actuales en Pantalla
		cooldownBeforeExercise(delta);//Does a cooldown before exercising of 5 seconds

		if (!gameMng.areWavesOver() && !musicMng.isPlayingFairyTheme()) {
			musicMng.pickFairiesLvlMusic();
		}
		else if (!gameMng.AreFairiesAlive() && gameMng.areWavesOver() && !musicMng.isPlayingBossTheme() && !waitingForExercise){
			musicMng.pickBossLvlMusic();
		}
		
		//SUJETO A CAMBIO: REGISTRAR PROGRESO
		if (gameMng.isReimuDead()) {
			musicMng.stopBossMusic();
			musicMng.stopFairiesMusic();
			if (gameMng.getScore() > game.getHighScore())
				game.setHighScore(gameMng.getScore());
			Screen ss = new PantallaGameOver();
			ss.resize(1200, 800);
			game.setScreen(ss);
			gameMng.disposeGOM();
			dispose();
		}
		batch.end();
		
		//checkear si debemos pasar al siguiente nivel
	    levelManagement();
	}
	
	public void levelManagement() {
		if (!gameMng.isBossAlive()) {
			musicMng.stopBossMusic();
			if ((nivel+1) < 5) {
				Screen ss = new PantallaJuego(nivel+1, gameMng.getReimuVidas(), gameMng.getScore(), gameMng.getReimuDamage());
				ss.resize(1200, 800);
				game.setScreen(ss);
				gameMng.disposeGOM();
				dispose();
			}
			else {
				Screen ss = new PantallaFinal();
				ss.resize(1200, 800);
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
		
		// OPCION DE DESARROLLADOR (se elimina)
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			game.setScreen(new PantallaEjercicios(game, musicMng, this));
		}
		
		// VERIFICAR SI ESTA LISTO PARA EJERCITAR
		if (waitingForExercise && exerciseTimer >= 5f && !gameMng.areWeFightingBoss()) {
			musicMng.stopFairiesMusic();
			musicMng.stopBossMusic();
			game.setScreen(new PantallaEjercicios(game, musicMng, this));
			System.out.println("correctas en Pantalla Juego? = "+cantCorrectas);
			gameMng.setFightBoss(true);
			// ANTES DE SALIR CAMBIAR ESTADO DE CONTROL PARA NO REPETIR PANTALLA
			gameMng.setExerciseDone(true);
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
	public void resize(int width, int height) {}

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