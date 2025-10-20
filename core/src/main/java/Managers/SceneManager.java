package Managers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;

public class SceneManager {
    private ArrayList<Texture> allBackgrounds = new ArrayList<Texture>();
    private Texture background1;
    private Texture background2;
    private float backgroundY = 0;
    private float backgroundVelocity = 4;
    private float screenHeight;
    private float screenWidth;
    private SpriteBatch batch;
    private int bgChoice;

    // --- Special assets for bgChoice == 4 ---
    private Texture starsBg;
    private Texture moonAsset;
    private float swayTime = 0f;
    private float moonTime = 0f;
    private float moonRotation = 0f;

    public SceneManager(SpriteBatch batch, int scnChoice, float scrWidth, float scrHeight) {
        this.batch = batch;
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
        Texture bg3 = new Texture("redMarbleBg.png");
        allBackgrounds.add(bg3);
        Texture bg4 = new Texture("voidBg.png");
        allBackgrounds.add(bg4);
        Texture bg5 = new Texture("obscureSpellsBg.png");
        allBackgrounds.add(bg5);
    }

    public void drawBg(float scrWidth, float scrHeight) {
        if (bgChoice == 4) {
            rotateBg();
        } else {
            scrollBg();
        }
    }

    public void pickLvlBg(int choice) {
        bgChoice = choice;
        switch (choice) {
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

                if (starsBg == null)
                    starsBg = new Texture("starsBg.png");
                if (moonAsset == null)
                    moonAsset = new Texture("moonAsset.png");
                break;
        }
    }

    public void scrollBg() {
        backgroundY -= backgroundVelocity;

        if (backgroundY <= -screenHeight) {
            backgroundY = 0;
        }

        batch.draw(background1, 0, backgroundY, screenWidth, screenHeight);
        batch.draw(background2, 0, backgroundY + screenHeight, screenWidth, screenHeight);
    }

    public void rotateBg() {
        float delta = Gdx.graphics.getDeltaTime();
        swayTime += 0.3f * delta;
        moonTime += delta;
        moonRotation += 3f * delta; // very slow spin on its own axis

        // --- Swaying stars background ---
        float swayAmplitude = 100f;
        float swayOffset = MathUtils.sin(swayTime) * swayAmplitude;

        float bgWidth = screenWidth;
        float bgHeight = screenHeight;
        float drawX = -swayAmplitude + swayOffset;
        float drawWidth = bgWidth + swayAmplitude * 2;

        batch.draw(starsBg, drawX, 0, drawWidth, bgHeight);

        // --- Moon bobbing + true self-rotation ---
        float moonBob = MathUtils.sin(moonTime * 2f) * 5f; // gentle up-down
        float moonWidth = moonAsset.getWidth();
        float moonHeight = moonAsset.getHeight();
        float moonScale = 1.6f;

        // The moon sits in the lower-left corner (partially below screen)
        float moonX = -40f;
        float moonY = -moonHeight * 0.3f + moonBob;

        // Compute rotation origin based on the scaled texture's true center
        float originX = (moonWidth * moonScale) / 2f;
        float originY = (moonHeight * moonScale) / 2f;

        // Draw the moon so that it rotates around its own center
        batch.draw(
            moonAsset,
            moonX - originX + (moonWidth * moonScale) / 2f, // corrected so it stays in place
            moonY - originY + (moonHeight * moonScale) / 2f,
            originX, originY,          // rotation origin (moon center)
            moonWidth, moonHeight,
            moonScale, moonScale,
            moonRotation,              // slow spin
            0, 0,
            moonAsset.getWidth(), moonAsset.getHeight(),
            false, false
        );
    }

    public void dispose() {
        for (int i = 0; i < allBackgrounds.size(); i++) {
            allBackgrounds.get(i).dispose();
        }
        if (starsBg != null)
            starsBg.dispose();
        if (moonAsset != null)
            moonAsset.dispose();
    }
}
