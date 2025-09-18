package puppy.code;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Enemies.Pregunta;


public class PantallaResultados implements Screen {

	private Touhou game;
	private Screen pantallaAnterior;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;
    private int correctas;

    public PantallaResultados(ArrayList<Pregunta> preguntasRonda, int ronda, Screen pantallaAnterior) {
        game = Touhou.getInstance();
        this.pantallaAnterior = pantallaAnterior;
        this.batch = game.getBatch();
        this.correctas = 0;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;
        skin.add("default", buttonStyle);

        shapeRenderer = new ShapeRenderer();

        // titulo pantalla
        Label titulo = new Label("¡Felicitaciones por superar la ronda " + ronda + "!", skin);
        titulo.setPosition(480, 740);
        stage.addActor(titulo);

        // posiciones para labels de preguntas y resultados
        float startY = 650;    // posición inicial vertical
        float rectHeight = 40; // más pequeño
        float rectWidth = 300; // más angosto
        float spacing = 15;

        // centrar columnas
        float totalWidth = rectWidth * 2 + 40; // 2 columnas + separación
        float startX = (1200 - totalWidth) / 2f;
        float col1X = startX;
        float col2X = startX + rectWidth + 40;

        for (int i = 0; i < 6; i++) {
            float y = startY - i * (rectHeight + spacing);

            // escribir Pregunta X a la izquierda
            Label lblPregunta = new Label("Pregunta " + (i + 1), skin);
            lblPregunta.setWidth(rectWidth);
            lblPregunta.setAlignment(com.badlogic.gdx.utils.Align.center);
            lblPregunta.setPosition(col1X, y + rectHeight / 2 - lblPregunta.getHeight() / 2);
            stage.addActor(lblPregunta);

            // escribir resultado de Pregunta X a la derecha
            String estado;
            Texture texResultado = null;
            if (i >= preguntasRonda.size() || preguntasRonda.get(i).getRespondidaCorrecta() == null) {
                estado = "No respondida";
            } else if (preguntasRonda.get(i).getRespondidaCorrecta()) {
                estado = "Correcta";
                texResultado = new Texture(Gdx.files.internal("correcta.png"));
            } else {
                estado = "Incorrecta";
                texResultado = new Texture(Gdx.files.internal("incorrecta.png"));
            }

            Label lblResultado = new Label(estado, skin);
            lblResultado.setWidth(rectWidth);
            lblResultado.setAlignment(com.badlogic.gdx.utils.Align.center);
            lblResultado.setPosition(col2X, y + rectHeight / 2 - lblResultado.getHeight() / 2);
            stage.addActor(lblResultado);
            
            // icono de resultado
            if (texResultado != null) {
                Image imgResultado = new Image(texResultado);
                imgResultado.setSize(32, 32); // escalas a 32x32
                imgResultado.setPosition(col2X + rectWidth - 100, y + rectHeight / 2 - 16);
                stage.addActor(imgResultado);
            }
        }
        
        // mostrar recompensas obtenidas
        mostrarRecompensas(preguntasRonda);

        // mostrar texto de volver
        TextButton btnVolver = new TextButton("Volver", skin);
        btnVolver.setPosition(1050, 65);
        stage.addActor(btnVolver);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
		// actualizar
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

        float startY = 650;
        float rectHeight = 40;
        float rectWidth = 300;
        float spacing = 15;

        float totalWidth = rectWidth * 2 + 40;
        float startX = (1200 - totalWidth) / 2f;
        float col1X = startX;
        float col2X = startX + rectWidth + 40;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < 6; i++) {
            float y = startY - i * (rectHeight + spacing);

            // dibujar columna izquierda de rectangulos
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(col1X, y, rectWidth, rectHeight);

            // dibujar columna derecha de rectangulos
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(col2X, y, rectWidth, rectHeight);
        }

        shapeRenderer.end();

        // dibujar bordes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        for (int i = 0; i < 6; i++) {
            float y = startY - i * (rectHeight + spacing);
            shapeRenderer.rect(col1X, y, rectWidth, rectHeight);
            shapeRenderer.rect(col2X, y, rectWidth, rectHeight);
        }

        shapeRenderer.end();
        
        float recompensasY = 300;
        float centerX = 1200 / 2f;
        float rectWidthRec = 250;
        float rectHeightRec = 40;
        float spacingY = 60;
        
        // dibujar rectangulos dependiendo de las recompensas obtenidas
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        switch(correctas) {
        	case 2: {
        		shapeRenderer.setColor(Color.DARK_GRAY);
        		float x = centerX - rectWidthRec / 2f;
        		float y = recompensasY - spacingY;
        		shapeRenderer.rect(x, y, rectWidthRec, rectHeightRec);
        		break;
        	}
        	case 3: {
        		shapeRenderer.setColor(Color.DARK_GRAY);
        		float x1 = centerX - rectWidthRec - 20;
        		float x2 = centerX + 20;
        		float y = recompensasY - spacingY;
        		shapeRenderer.rect(x1, y, rectWidthRec, rectHeightRec);
        		shapeRenderer.rect(x2, y, rectWidthRec, rectHeightRec);
        		break;
        	}
        	case 4:
        	case 5:
        	case 6: {
        		shapeRenderer.setColor(Color.DARK_GRAY);
        		float x1 = centerX - rectWidthRec - 20;
        		float x2 = centerX + 20;
        		float yTop = recompensasY - spacingY;
        		float yBottom = recompensasY - 2 * spacingY - rectHeightRec;
        		shapeRenderer.rect(x1, yTop, rectWidthRec, rectHeightRec);
        		shapeRenderer.rect(x2, yTop, rectWidthRec, rectHeightRec);
        		shapeRenderer.rect(centerX - rectWidthRec / 2f, yBottom, rectWidthRec, rectHeightRec);
        		break;
        	}
        }
        shapeRenderer.end();
        
        // efecto de cambio de color sobre el boton de Volver
        float btnX = 1000;
        float btnY = 50;
        float btnWidth = 150;
        float btnHeight = 50;

        float mouseX = Gdx.input.getX();
        float mouseY = 800 - Gdx.input.getY();

        if (mouseX >= btnX && mouseX <= btnX + btnWidth && mouseY >= btnY && mouseY <= btnY + btnHeight) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GRAY); // color al pasar el mouse
            shapeRenderer.rect(btnX, btnY, btnWidth, btnHeight);
            shapeRenderer.end();

            if (Gdx.input.isTouched()) {
                game.setScreen(pantallaAnterior);
                dispose();
            }
        } else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE); // color normal
            shapeRenderer.rect(btnX, btnY, btnWidth, btnHeight);
            shapeRenderer.end();
        }

        stage.act(delta);
        stage.draw();
        
        batch.end();
    }

	@Override
	public void show() {}

	@Override
	public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.dispose();
    }
	
	private void mostrarRecompensas(ArrayList<Pregunta> preguntasRonda) {
	    // conteo de preguntas correctas
	    for (Pregunta p : preguntasRonda) {
	        if (p.getRespondidaCorrecta() != null && p.getRespondidaCorrecta()) correctas++;
	    }

	    // posiciones para labels de recompensas
	    float recompensasY = 300;
	    float centerX = 1200 / 2f;
	    float spacingY = 60;
	    float rectWidth = 250;
	    float rectHeightRec = 40;

	    // mostrar titulo de recompensas
	    Label lblRecompensasTitulo = new Label("¡HAS OBTENIDO LAS SIGUIENTES RECOMPENSAS!", skin);
	    lblRecompensasTitulo.setAlignment(com.badlogic.gdx.utils.Align.center);
	    lblRecompensasTitulo.setPosition(centerX - 180, recompensasY);
	    stage.addActor(lblRecompensasTitulo);

	    
	    switch (correctas) {
	        case 2: {
	            // 1 recompensa centrada
	            float x = centerX - rectWidth / 2f;
	            float y = recompensasY - spacingY;

	            Label r = new Label("Incremento de 20 de Poder", skin);
	            r.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r.setWidth(rectWidth);
	            r.setPosition(x, y + rectHeightRec / 2 - r.getHeight() / 2);
	            stage.addActor(r);
	            break;
	        }
	        case 3: {
	            // 2 recompensas: lado a lado arriba
	            float x1 = centerX - rectWidth - 20;
	            float x2 = centerX + 20;
	            float y = recompensasY - spacingY;

	            Label r1 = new Label("1 Vida", skin);
	            r1.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r1.setWidth(rectWidth);
	            r1.setPosition(x1, y + rectHeightRec / 2 - r1.getHeight() / 2);
	            stage.addActor(r1);

	            Label r2 = new Label("Incremento de 20 de Poder", skin);
	            r2.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r2.setWidth(rectWidth);
	            r2.setPosition(x2, y + rectHeightRec / 2 - r2.getHeight() / 2);
	            stage.addActor(r2);
	            break;
	        }
	        case 4:
	        case 5:
	        case 6: {
	            // 3 recompensas: 2 arriba, 1 abajo centrada
	            float x1 = centerX - rectWidth - 20;
	            float x2 = centerX + 20;
	            float yTop = recompensasY - spacingY;
	            float yBottom = recompensasY - 2 * spacingY - rectHeightRec;

	            Label r1 = new Label(correctas == 6 ? "3 Vidas" : "2 Vidas", skin);
	            r1.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r1.setWidth(rectWidth);
	            r1.setPosition(x1, yTop + rectHeightRec / 2 - r1.getHeight() / 2);
	            stage.addActor(r1);

	            Label r2 = new Label(correctas == 6 ? "Incremento de 50 de Poder" : "Incremento de 30 de Poder", skin);
	            r2.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r2.setWidth(rectWidth);
	            r2.setPosition(x2, yTop + rectHeightRec / 2 - r2.getHeight() / 2);
	            stage.addActor(r2);

	            Label r3 = new Label("Reducción de Dificultad del Jefe Final", skin);
	            r3.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r3.setWidth(rectWidth);
	            r3.setPosition(centerX - rectWidth / 2f, yBottom + rectHeightRec / 2 - r3.getHeight() / 2);
	            stage.addActor(r3);
	            break;
	        }
	        
	    }
	    shapeRenderer.end();
	}
	
}
