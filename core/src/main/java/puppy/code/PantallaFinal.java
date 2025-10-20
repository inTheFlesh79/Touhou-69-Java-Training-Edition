package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaFinal implements Screen {

    private Touhou game;
    private Viewport viewport;
    private Texture background;
    private Music endingMusic;

    public PantallaFinal() {
        game = Touhou.getInstance();
        viewport = game.getViewport(); // ✅ Use shared viewport

        // Load background image
        background = new Texture(Gdx.files.internal("gameEndingBg.png"));

        // Load and play ending music
        endingMusic = Gdx.audio.newMusic(Gdx.files.internal("Credits Theme - Eternal Dream.ogg"));
        endingMusic.setLooping(true);
        endingMusic.setVolume(0.7f);
        endingMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // Update viewport and camera
        viewport.apply();
        game.getBatch().setProjectionMatrix(viewport.getCamera().combined);

        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.getBatch().end();

        // Detect input to return to menu
        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            Screen menuScreen = new PantallaMenu();
            menuScreen.resize((int) viewport.getWorldWidth(), (int) viewport.getWorldHeight());
            game.setScreen(menuScreen);
            dispose();
        }
    }

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
        if (background != null) background.dispose();
        if (endingMusic != null) {
            endingMusic.stop();
            endingMusic.dispose();
        }
    }
}
