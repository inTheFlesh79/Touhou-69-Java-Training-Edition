package puppy.code;

import com.badlogic.gdx.Gdx;
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
	private int ronda;
	private int cantFairies;
	
	// Manager de Personajes con comportamientos dinamicos
	private GameObjectManager gameMng;
	// Manager para controlar musica
    private MusicManager musicMng = new MusicManager();
    // Manager para controlar la imagen de fondo del juego
    private SceneManager sceneMng;
    
	public PantallaJuego(int ronda, int vidas, int score, int cantFairies) {
		game = Touhou.getInstance();
		batch = game.getBatch();
		gameMng = new GameObjectManager(batch, ronda, vidas, score, cantFairies, this);
		sceneMng = new SceneManager(batch);
		
		this.ronda = ronda;
		gameMng.setScore(score);
		this.cantFairies = cantFairies;
		
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 1200, 800);
	}
    
	public void dibujaHUD() {
		CharSequence str = "Lives: "+ gameMng.getReimuVidas() +" Round: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+ gameMng.getScore(), Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	
	@Override
	public void render(float delta) {
		System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		sceneMng.drawBg();
		dibujaHUD();
		gameMng.update();// Maneja los objetos actuales en Pantalla
		
		if (gameMng.AreFairiesAlive() && !musicMng.isPlayingFairyTheme()) {
			musicMng.playFairiesMusic();
		}
		//SUJETO A CAMBIO: ORDEN DE CANCIONES POR NUEVA SCREEN DE APRENDIZAJEf
		else if (!gameMng.AreFairiesAlive() && !musicMng.isPlayingBossTheme()){
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
		
		//checkear si debemos pasar al siguiente nivel
	    levelManagement();
	}
	
	//SUJETO A CAMBIO: MANEJA LOS NIVELES Y DEBE DEFINIR BIEN EN QUE NIVEL VA Y MANTENER ORDEN DE SPRITES
	public void levelManagement() {
		if (!gameMng.isBossAlive()) {
			gameMng.changeLevel();
			musicMng.stopBossMusic();
			//Screen ss = new PantallaJuego(ronda+1, gameMng.getReimuVidas(), gameMng.getScore(), cantFairies + 2);
			//ss.resize(1200, 800);
			//game.setScreen(ss);
			
	    }
	}
    
    
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