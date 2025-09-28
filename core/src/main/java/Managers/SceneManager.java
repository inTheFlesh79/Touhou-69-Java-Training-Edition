package Managers;

import java.util.ArrayList;

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
    
    public SceneManager(SpriteBatch batch, int scnChoice, float scrWidth, float scrHeight) {
        this.batch = batch;
        
        // Use viewport world dimensions, not raw Gdx graphics
        this.screenWidth = scrWidth;
        this.screenHeight = scrHeight;

        loadAllBg();
        pickLvlBg(scnChoice);
    }

    
    public void loadAllBg() {
    	Texture bg = new Texture("obscureBg.png");
    	allBackgrounds.add(bg);
    	Texture bg1 = new Texture("darkRidgeBg.png");
    	allBackgrounds.add(bg1);
    	Texture bg3 = new Texture("pinkForestBg.png");
    	allBackgrounds.add(bg3);
    	Texture bg4 = new Texture("redMarbleBg.png");
    	allBackgrounds.add(bg4);
    	Texture bg5 = new Texture("obscureSpellsBg.png");
    	allBackgrounds.add(bg5);
    }
    
    public void drawBg(float scrWidth, float scrHeight) {
    	scrollBg();
    }
    
    public void pickLvlBg(int choice) {
    	switch(choice) {
    		case 1:
    			background1 = allBackgrounds.get(0);
    			background2 = allBackgrounds.get(0);
    			break;
    		case 2:
    			background1 = allBackgrounds.get(1);
    			background2 = allBackgrounds.get(1);
    			break;
    		case 3:
    			background1 = allBackgrounds.get(2);
    			background2 = allBackgrounds.get(2);
    			break;
    		case 4:
    			background1 = allBackgrounds.get(3);
    			background2 = allBackgrounds.get(3);
    			break;
    	}
    }
    
    public void scrollBg() {
    	// Scroll background
		backgroundY -= backgroundVelocity;

		// If the background has fully scrolled down, reset the position
		if (backgroundY <= -screenHeight) {
		    backgroundY = 0;
		}
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