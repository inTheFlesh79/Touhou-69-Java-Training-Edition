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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Enemies.Pregunta;
import Managers.MusicManager;
import Sessions.SessionDataManager;
import Sessions.TestRound;


public class PantallaResultados implements Screen {
	private Touhou game;
	private PantallaJuego pantallaAnterior;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private FitViewport viewport;
	private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;
    private int correctas;
    private MusicManager musicMng;
    private boolean retryAndBuff;
	private TestRound tempTestRound;
    
    public PantallaResultados(ArrayList<Pregunta> preguntasRonda, int ronda, int intentosFallidos, MusicManager musicMng, PantallaJuego pantallaAnterior, boolean retryAndBuff) {
        game = Touhou.getInstance();
        this.pantallaAnterior = pantallaAnterior;
        this.batch = game.getBatch();
        this.correctas = 0;
        this.musicMng = musicMng;
        this.retryAndBuff = retryAndBuff;

        camera = game.getCamera();
        viewport = game.getViewport();
        
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        BitmapFont font = game.getFont();
		font.getData().setScale(0.75f);
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
        
        tempTestRound = SessionDataManager.getInstance().createNewTestRound(preguntasRonda);

        // posiciones para labels de preguntas y resultados
        float startY = 780;    // posición inicial vertical
        float rectHeight = 48; // más pequeño
        float rectWidth = 320; // más angosto
        float spacing = 15;

        // centrar columnas
        float totalWidth = rectWidth * 2 + 48; // 2 columnas + separación
        float startX = (1280 - totalWidth) / 2f;
        float col1X = startX;
        float col2X = startX + rectWidth + 48;

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
                imgResultado.setPosition(col2X + rectWidth - 100, y + rectHeight / 2 - 17);
                stage.addActor(imgResultado);
            }
        }
        
        // mostrar recompensas obtenidas
        mostrarRecompensas(preguntasRonda, ronda);

        // mostrar texto de boton segun sea el caso
        TextButton btnVolver;
        if (correctas < 4) { btnVolver = new TextButton("Retry", skin); }
        else { btnVolver = new TextButton("Volver", skin); }
        btnVolver.setPosition(1120, 78);
        stage.addActor(btnVolver);
        
        Label titulo;
        // titulo pantalla
        if (correctas < 4) {
        	titulo = new Label("Lo sentimos! No cumpliste el minimo de respuestas correcta Debes volver a repetir la ronda de preguntas", skin);
        	titulo.setPosition(330, 870);
        	
        } else {
        	titulo = new Label("Felicitaciones por superar la ronda " + ronda + "!", skin);
        	titulo.setPosition(450, 870);
        }
        titulo.setWidth(650);
        titulo.setWrap(true);
        stage.addActor(titulo);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
        camera.update();
		// actualizar
        viewport.apply();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

        float startY = 780;
        float rectHeight = 48;
        float rectWidth = 320;
        float spacing = 15;

        float totalWidth = rectWidth * 2 + 48;
        float startX = (1280 - totalWidth) / 2f;
        float col1X = startX;
        float col2X = startX + rectWidth + 48;

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
        
        // RECTANGULOS RECOMPENSAS
        float recompensasY = 268;
        float centerX = 1280 / 2f;
        float rectWidthRec = 427;
        float rectHeightRec = 48;
        float spacingY = 60;
        
        // dibujar rectangulos dependiendo de las recompensas obtenidas SOLO si tiene al menos 4 correctas
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (correctas >= 4) {
        	switch (Touhou.getIntentosRonda()) {
	        	case 0:
	        	case 1: {
	        		shapeRenderer.setColor(Color.DARK_GRAY);
	        		float x1 = centerX - rectWidthRec - 22;
	        		float x2 = centerX + 22;
	        		float yTop = recompensasY - spacingY;
	        		float yBottom = recompensasY - 2 * spacingY - rectHeightRec;
	        		shapeRenderer.rect(x1, yTop, rectWidthRec, rectHeightRec);
	        		shapeRenderer.rect(x2, yTop, rectWidthRec, rectHeightRec);
	        		shapeRenderer.rect(centerX - rectWidthRec / 2f, yBottom, rectWidthRec, rectHeightRec);
	        		break;
	        	}
	        	case 2: {
	        		shapeRenderer.setColor(Color.DARK_GRAY);
	        		float x1 = centerX - rectWidthRec - 22;
	        		float x2 = centerX + 22;
	        		float y = recompensasY - spacingY;
	        		shapeRenderer.rect(x1, y, rectWidthRec, rectHeightRec);
	        		shapeRenderer.rect(x2, y, rectWidthRec, rectHeightRec);
	        		break;
	        	}
	        	case 3: {
	        		shapeRenderer.setColor(Color.DARK_GRAY);
	        		float x = centerX - rectWidthRec / 2f;
	        		float y = recompensasY - spacingY;
	        		shapeRenderer.rect(x, y, rectWidthRec, rectHeightRec);
	        		break;
	        	}
	        }
        }
        shapeRenderer.end();
        
        // efecto de cambio de color sobre el boton de Volver
        float btnX = 1062;
        float btnY = 56;
        float btnWidth = 160;
        float btnHeight = 60;

        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mouse);
        float mouseX = mouse.x;
        float mouseY = mouse.y;

        if (mouseX >= btnX && mouseX <= btnX + btnWidth && mouseY >= btnY && mouseY <= btnY + btnHeight) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GRAY); // color al pasar el mouse
            shapeRenderer.rect(btnX, btnY, btnWidth, btnHeight);
            shapeRenderer.end();

            if (Gdx.input.isTouched()) {
                if (correctas < 4) {
                	// detener musica para repetir ronda
                	musicMng.stopFairiesMusic();
        			musicMng.stopBossMusic();
        			// aumentar intentos fallidos
        			//Touhou.setIntentosRonda(Touhou.getIntentosRonda() + 1);
                    // repetir ejercicios con la misma categoría
                    game.setScreen(new PantallaEjercicios(game, musicMng, pantallaAnterior, retryAndBuff));
                } else {
                	SessionDataManager.getInstance().addTestRound(tempTestRound);
                	// pasar categoria ya que tuvo al menos 4 buenas
                	Touhou.pasarCategoria();
                	// resetear contador de intentos
                	pantallaAnterior.setIntentosFallidos(Touhou.getIntentosRonda());
                	
                	Touhou.setIntentosRonda(0);
                	Touhou.addRondasCompletadas();
                	
                	// si es retry+buff no será boss fight
                	if (!retryAndBuff) {musicMng.playBossMusic();}
                	else {pantallaAnterior.setRetryBuff(true);}
                    // volver al juego normal
                	if (Touhou.getRondasCompletadas() == 3) {
                		pantallaAnterior.setCorrectas(correctas);
                		game.setScreen(new PantallaCodigo(Touhou.getInstance(), pantallaAnterior));
                	}
                	else {
                		pantallaAnterior.setCorrectas(correctas);
                		game.setScreen(pantallaAnterior);
                	}
                }
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
		ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.dispose();
    }
	
	private void mostrarRecompensas(ArrayList<Pregunta> preguntasRonda, int ronda) {
	    // conteo de preguntas correctas
	    for (Pregunta p : preguntasRonda) {
	        if (p.getRespondidaCorrecta() != null && p.getRespondidaCorrecta()) correctas++;
	    }

	    // posiciones para labels
	    float recompensasY = 280;
	    float centerX = 1280 / 2f;
	    float spacingY = 72;
	    float rectWidth = 267;
	    float rectHeightRec = 48;

	    // mostrar titulo de recompensas
	    if (correctas >= 4 && Touhou.getIntentosRonda() < 4) {
	    	Label lblRecompensasTitulo = new Label("HAS OBTENIDO LAS SIGUIENTES RECOMPENSAS!", skin);
		    lblRecompensasTitulo.setAlignment(com.badlogic.gdx.utils.Align.center);
		    lblRecompensasTitulo.setPosition(centerX - 270, recompensasY);
		    stage.addActor(lblRecompensasTitulo);
	    }

	    // mostrar titulo de intentos fallidos
	    Label intentosFallidos = new Label("INTENTOS FALLIDOS: " + Touhou.getIntentosRonda(), skin);
	    intentosFallidos.setAlignment(com.badlogic.gdx.utils.Align.center);
	    intentosFallidos.setPosition(centerX - 140, recompensasY + 100);
	    stage.addActor(intentosFallidos);

	    if (correctas >= 4 && correctas < 5) {
	    	cuatroCorrectas(centerX,rectWidth,recompensasY,spacingY, rectHeightRec);
	    }
	    else if (correctas == 5) {
	    	cincoCorrectas(centerX,rectWidth,recompensasY,spacingY, rectHeightRec);
	    }
	    else if (correctas == 6){
	    	seisCorrectas(centerX,rectWidth,recompensasY,spacingY, rectHeightRec);
	    }
	    shapeRenderer.end();
	}
	
	public void cuatroCorrectas(float centerX, float rectWidth, float recompensasY, float spacingY, float rectHeightRec) {
		switch (Touhou.getIntentosRonda()) {
		    case 0:
	    	case 1: {
		    	// 3 recompensas: 2 arriba, 1 abajo centrada
	            float x1 = centerX - rectWidth - 104;
	            float x2 = centerX + 104;
	            float yTop = recompensasY - spacingY;
	            float yBottom = recompensasY - 2 * spacingY - rectHeightRec;
	
	            Label r1 = new Label(Touhou.getIntentosRonda() == 1 ? "1 Vida" : "2 Vidas", skin);
	            r1.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r1.setWidth(rectWidth);
	            r1.setPosition(x1, yTop + rectHeightRec / 2 - r1.getHeight() / 2);
	            stage.addActor(r1);
	
	            Label r2 = new Label(Touhou.getIntentosRonda() == 1 ? "Incremento de 20 de Poder" : "Incremento de 30 de Poder", skin);
	            r2.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r2.setWidth(rectWidth);
	            r2.setPosition(x2, yTop + rectHeightRec / 2 - r2.getHeight() / 2);
	            stage.addActor(r2);
	
	            Label r3 = new Label("Reduccion de Dificultad del Jefe Final", skin);
	            r3.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r3.setWidth(rectWidth);
	            r3.setPosition(centerX - rectWidth / 2f, (yBottom + rectHeightRec / 2 - r3.getHeight() / 2)+10);
	            stage.addActor(r3);
	            break;
		    }
	        case 2: {
	        	float x1 = centerX - rectWidth - 104;
	            float x2 = centerX + 104;
	            float y = recompensasY - spacingY;
	
	            Label r1 = new Label("Reduccion de Dificultad del Jefe Final", skin);
	            r1.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r1.setWidth(rectWidth);
	            r1.setPosition(x1, y + rectHeightRec / 2 - r1.getHeight() / 2);
	            stage.addActor(r1);
	
	            Label r2 = new Label("Incremento de 10 de Poder", skin);
	            r2.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r2.setWidth(rectWidth);
	            r2.setPosition(x2, y + rectHeightRec / 2 - r2.getHeight() / 2);
	            stage.addActor(r2);
	            break;
	        }
	        case 3: {
	        	// 1 recompensa centrada
	            float x = centerX - rectWidth / 2f;
	            float y = recompensasY - spacingY;
	
	            Label r = new Label("Incremento de 5 de Poder", skin);
	            r.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r.setWidth(rectWidth);
	            r.setPosition(x, y + rectHeightRec / 2 - r.getHeight() / 2);
	            stage.addActor(r);
	            break;
	        }
		}
	}
	
	public void cincoCorrectas(float centerX, float rectWidth, float recompensasY, float spacingY, float rectHeightRec) {
		switch (Touhou.getIntentosRonda()) {
		    case 0:
	    	case 1: {
		    	// 3 recompensas: 2 arriba, 1 abajo centrada
	            float x1 = centerX - rectWidth - 104;
	            float x2 = centerX + 104;
	            float yTop = recompensasY - spacingY;
	            float yBottom = recompensasY - 2 * spacingY - rectHeightRec;
	
	            Label r1 = new Label(Touhou.getIntentosRonda() == 1 ? "2 Vidas" : "3 Vidas", skin);
	            r1.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r1.setWidth(rectWidth);
	            r1.setPosition(x1, yTop + rectHeightRec / 2 - r1.getHeight() / 2);
	            stage.addActor(r1);
	
	            Label r2 = new Label(Touhou.getIntentosRonda() == 1 ? "Incremento de 30 de Poder" : "Incremento de 40 de Poder", skin);
	            r2.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r2.setWidth(rectWidth);
	            r2.setPosition(x2, yTop + rectHeightRec / 2 - r2.getHeight() / 2);
	            stage.addActor(r2);
	
	            Label r3 = new Label("Reduccion de Dificultad del Jefe Final", skin);
	            r3.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r3.setWidth(rectWidth);
	            r3.setPosition(centerX - rectWidth / 2f, (yBottom + rectHeightRec / 2 - r3.getHeight() / 2)+10);
	            stage.addActor(r3);
	            break;
		    }
	        case 2: {
	        	float x1 = centerX - rectWidth - 104;
	            float x2 = centerX + 104;
	            float y = recompensasY - spacingY;
	
	            Label r1 = new Label("Reduccion de Dificultad del Jefe Final", skin);
	            r1.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r1.setWidth(rectWidth);
	            r1.setPosition(x1, y + rectHeightRec / 2 - r1.getHeight() / 2);
	            stage.addActor(r1);
	
	            Label r2 = new Label("Incremento de 15 de Poder", skin);
	            r2.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r2.setWidth(rectWidth);
	            r2.setPosition(x2, y + rectHeightRec / 2 - r2.getHeight() / 2);
	            stage.addActor(r2);
	            break;
	        }
	        case 3: {
	        	// 1 recompensa centrada
	            float x = centerX - rectWidth / 2f;
	            float y = recompensasY - spacingY;
	
	            Label r = new Label("Incremento de 10 de Poder", skin);
	            r.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r.setWidth(rectWidth);
	            r.setPosition(x, y + rectHeightRec / 2 - r.getHeight() / 2);
	            stage.addActor(r);
	            break;
	        }
		}
	}
	
	public void seisCorrectas(float centerX, float rectWidth, float recompensasY, float spacingY, float rectHeightRec) {
		switch (Touhou.getIntentosRonda()) {
		    case 0:
	    	case 1: {
		    	// 3 recompensas: 2 arriba, 1 abajo centrada
	    		float x1 = centerX - rectWidth - 104;
	            float x2 = centerX + 104;
	            float yTop = recompensasY - spacingY;
	            float yBottom = recompensasY - 2 * spacingY - rectHeightRec;
	
	            Label r1 = new Label(Touhou.getIntentosRonda() == 1 ? "3 Vidas" : "4 Vidas", skin);
	            r1.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r1.setWidth(rectWidth);
	            r1.setPosition(x1, yTop + rectHeightRec / 2 - r1.getHeight() / 2);
	            stage.addActor(r1);
	
	            Label r2 = new Label(Touhou.getIntentosRonda() == 1 ? "Incremento de 40 de Poder" : "Incremento de 50 de Poder", skin);
	            r2.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r2.setWidth(rectWidth);
	            r2.setPosition(x2, yTop + rectHeightRec / 2 - r2.getHeight() / 2);
	            stage.addActor(r2);
	
	            Label r3 = new Label("Reduccion de Dificultad del Jefe Final", skin);
	            r3.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r3.setWidth(rectWidth);
	            r3.setPosition(centerX - rectWidth / 2f, (yBottom + rectHeightRec / 2 - r3.getHeight() / 2)+10);
	            stage.addActor(r3);
	            break;
		    }
	        case 2: {
	        	float x1 = centerX - rectWidth - 104;
	            float x2 = centerX + 104;
	            float yTop = recompensasY - spacingY;
	            float yBottom = recompensasY - 2 * spacingY - rectHeightRec;
	
	            Label r1 = new Label("1 Vida", skin);
	            r1.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r1.setWidth(rectWidth);
	            r1.setPosition(x1, yTop + rectHeightRec / 2 - r1.getHeight() / 2);
	            stage.addActor(r1);
	
	            Label r2 = new Label("Incremento de 20 de Poder", skin);
	            r2.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r2.setWidth(rectWidth);
	            r2.setPosition(x2, yTop + rectHeightRec / 2 - r2.getHeight() / 2);
	            stage.addActor(r2);
	
	            Label r3 = new Label("Reduccion de Dificultad del Jefe Final", skin);
	            r3.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r3.setWidth(rectWidth);
	            r3.setPosition(centerX - rectWidth / 2f, (yBottom + rectHeightRec / 2 - r3.getHeight() / 2)+10);
	            stage.addActor(r3);
	            break;
	        	
	        }
	        case 3: {
	        	// 1 recompensa centrada
	            float x = centerX - rectWidth / 2f;
	            float y = recompensasY - spacingY;
	
	            Label r = new Label("Incremento de 15 de Poder", skin);
	            r.setAlignment(com.badlogic.gdx.utils.Align.center);
	            r.setWidth(rectWidth);
	            r.setPosition(x, y + rectHeightRec / 2 - r.getHeight() / 2);
	            stage.addActor(r);
	            break;
	        }
		}
	}
}