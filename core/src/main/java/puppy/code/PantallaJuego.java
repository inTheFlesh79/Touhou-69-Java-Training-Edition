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
	private int power;
	private int cantCorrectas = -1;
	private boolean correctasSet = false;
	
	// Manager de Personajes con comportamientos dinamicos
	private GameObjectManager gameMng;
	// Manager para controlar musica
    private MusicManager musicMng = new MusicManager();
    // Manager para controlar la imagen de fondo del juego
    private SceneManager sceneMng;
    
	public PantallaJuego(int nivel, int vidas, int score, int power) {
		game = Touhou.getInstance();
		batch = game.getBatch();
		gameMng = new GameObjectManager(batch, nivel, vidas, score, power);
		sceneMng = new SceneManager(batch);
		
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
		gameMng.update();// Maneja los objetos actuales en Pantalla
		
		if (!gameMng.areWavesOver() && !musicMng.isPlayingFairyTheme()) {
			musicMng.playFairiesMusic();
		}
		//SUJETO A CAMBIO: ORDEN DE CANCIONES POR NUEVA SCREEN DE APRENDIZAJE
		else if (!gameMng.AreFairiesAlive() && gameMng.areWavesOver() && !musicMng.isPlayingBossTheme()){
			musicMng.playBossMusic();
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
			dispose();
		}
		batch.end();
		
		//System.out.println("correctas en Pantalla Juego? = "+cantCorrectas);
		
		//checkear si debemos pasar al siguiente nivel
	    levelManagement();
	}
	
	//SUJETO A CAMBIO: MANEJA LOS NIVELES Y DEBE DEFINIR BIEN EN QUE NIVEL VA Y MANTENER ORDEN DE SPRITES
	public void levelManagement() {
		if (!gameMng.isBossAlive()) {
			musicMng.stopBossMusic();
			if ((nivel+1) < 5) {
				Screen ss = new PantallaJuego(nivel+1, gameMng.getReimuVidas(), gameMng.getScore(), gameMng.getReimuDamage());
				ss.resize(1200, 800);
				game.setScreen(ss);
			}
			else {
				Screen ss = new PantallaFinal();
				ss.resize(1200, 800);
				game.setScreen(ss);
			}
	    }
		
		// verificar si se presionó la tecla de pausa (ESC)
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.setScreen(new PantallaPausa(game, this));
		}
		
		// OPCION DE DESARROLLADOR (se elimina)
		// verificar si se presionó la tecla de ejercicios (P)
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			game.setScreen(new PantallaEjercicios(game, this));
		}
		
		// VERIFICAR SI ESTA LISTO PARA EJERCITAR
		if (gameMng.readyToExercise() && !gameMng.areWeFightingBoss()) {
			game.setScreen(new PantallaEjercicios(game, this));
			System.out.println("correctas en Pantalla Juego? = "+cantCorrectas);
			gameMng.setFightBoss(true);
			// ANTES DE SALIR CAMBIAR ESTADO DE CONTROL PARA NO REPETIR PANTALLA
			gameMng.setExerciseDone(true);
			System.out.println("cumgri");
		}
	}
	
	public void setCorrectas(int c) {cantCorrectas = c;}
    
    
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		sceneMng.dispose();
		musicMng.dispose();
	}
}