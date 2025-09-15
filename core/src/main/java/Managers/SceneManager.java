package Managers;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SceneManager {
	private ArrayList<Texture> allBackgrounds = new ArrayList<Texture>();
	private Texture background1;
    private Texture background2;
    private float backgroundY = 0;
    private float backgroundVelocity = 4;
    private float screenHeight;
    private float screenWidth;
    private SpriteBatch batch;
    
    private Random random = new Random();
    private int backgroundChoice;
    
    public SceneManager (SpriteBatch batch) {
    	this.batch = batch;
    	
    	screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        
        loadAllBg();
        backgroundChoice = random.nextInt(allBackgrounds.size());
        //background1 = new Texture("obscureBg.png");
        //background2 = new Texture("obscureBg.png");
        background1 = allBackgrounds.get(backgroundChoice);
        background2 = allBackgrounds.get(backgroundChoice);
    }
    
    public void loadAllBg() {
    	Texture bg = new Texture("obscureBg.png");
    	allBackgrounds.add(bg);
    	Texture bg1 = new Texture("darkRidgeBg.png");
    	allBackgrounds.add(bg1);
    	Texture bg2 = new Texture("cherryBlossomBg.png");
    	allBackgrounds.add(bg2);
    	Texture bg3 = new Texture("pinkForestBg.png");
    	allBackgrounds.add(bg3);
    	Texture bg4 = new Texture("redMarbleBg.png");
    	allBackgrounds.add(bg4);
    	Texture bg5 = new Texture("obscureSpellsBg.png");
    	allBackgrounds.add(bg5);
    }
    
    public void drawBg() {
    	scrollBg();
    }
    
    public void scrollBg() {
    	// Scroll background
		backgroundY -= backgroundVelocity;

		// If the background has fully scrolled down, reset the position
		if (backgroundY <= -screenHeight) {
		    backgroundY = 0;
		}
		// Draw the scrolling background
    	// Draw the scrolling background
		batch.draw(background1, 0, backgroundY, screenWidth, screenHeight);
		batch.draw(background2, 0, backgroundY + screenHeight, screenWidth, screenHeight);
    }
    
    public void dispose() {
    	for (int i = 0; i < allBackgrounds.size(); i++) {
    		allBackgrounds.get(i).dispose();
    	}
    }
}