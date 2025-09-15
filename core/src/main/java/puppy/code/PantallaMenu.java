package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {
	private Touhou game;
	private OrthographicCamera camera;
	private Texture arrows;
	private Texture shift;
	private Texture space;
	public PantallaMenu() {
		game = Touhou.getInstance();
		arrows = new Texture(Gdx.files.internal("Arrow_Keys.png") );
		shift = new Texture (Gdx.files.internal("Shift_Key.png"));
		space = new Texture(Gdx.files.internal("Space_Key.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1200, 800);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);
		
		game.getBatch().begin();
		
		game.getBatch().draw(arrows, 50, 400, 400, 200);
		game.getBatch().draw(shift, 100, 290, 200, 100);
		game.getBatch().draw(space, 100, 180, 400, 100);
		game.getFont().draw(game.getBatch(), "Usa las flechas para moverte. ", 550, 430);
		game.getFont().draw(game.getBatch(), "Presiona SHIFT para disminuir tu movimiento ", 550, 320);
		game.getFont().draw(game.getBatch(), "Presiona SPACE para disparar ", 550, 210);
		game.getFont().draw(game.getBatch(), "Bienvenido a Touhou 69: Absolutely Trash Night !", 300, 750);
		game.getFont().draw(game.getBatch(), "Controles: ", 50, 700);
		game.getFont().draw(game.getBatch(), "Pincha en cualquier lado o presiona cualquier tecla para comenzar ...", 50, 50);
	
		game.getBatch().end();

		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaJuego(1,1000,0,6);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
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
		// TODO Auto-generated method stub
		
	}
   
}