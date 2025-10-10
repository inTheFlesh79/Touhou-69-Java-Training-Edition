package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PantallaMenu implements Screen {
	private Touhou game;
	private OrthographicCamera camera;
	private final FitViewport screenViewport;
	private Texture arrows;
	private Texture shift;
	private Texture space;
	
	public PantallaMenu() {
		game = Touhou.getInstance();
		arrows = new Texture(Gdx.files.internal("Arrow_Keys.png") );
		shift = new Texture (Gdx.files.internal("Shift_Key.png"));
		space = new Texture(Gdx.files.internal("Space_Key.png"));
		camera = Touhou.getInstance().getCamera();
		screenViewport = Touhou.getInstance().getViewport();
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		//camera.update();
		screenViewport.apply();
		game.getBatch().setProjectionMatrix(camera.combined);
		
		game.getBatch().begin();
		
		game.getBatch().draw(arrows, 54, 480, 400, 200);
		game.getBatch().draw(shift, 107, 348, 200, 100);
		game.getBatch().draw(space, 107, 216, 400, 100);
		game.getFont().draw(game.getBatch(), "Usa las flechas para moverte. ", 587, 516);
		game.getFont().draw(game.getBatch(), "Presiona SHIFT para disminuir tu movimiento ", 587, 320);
		game.getFont().draw(game.getBatch(), "Presiona SPACE para disparar ", 587, 252);
		game.getFont().draw(game.getBatch(), "Bienvenido a Touhou 69: Absolutely Trash Night !", 342, 940);
		game.getFont().draw(game.getBatch(), "Controles: ", 54, 840);
		game.getFont().draw(game.getBatch(), "Pincha en cualquier lado o presiona cualquier tecla para comenzar ...", 54, 60);
	
		game.getBatch().end();

		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaJuego(1,2,0,1000);
			game.setScreen(ss);
			ss.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			dispose();
		}
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		screenViewport.update(width, height, true);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}
}