package Reimu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
	private int xSpeed;
	private int ySpeed;
	private boolean destroyed = false;
	private Sprite spr;
	private int bulletDamage = 200;
	    
    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
    
    public void update() {
        spr.setPosition(spr.getX()+xSpeed, spr.getY()+ySpeed);
        if (spr.getX() < 0 || spr.getX()+spr.getWidth() > Gdx.graphics.getWidth()) {
            destroyed = true;
        }
        if (spr.getY() < 0 || spr.getY()+spr.getHeight() > Gdx.graphics.getHeight()) {
        	destroyed = true;
        }
        
    }
    
    public void draw(SpriteBatch batch) {
    	spr.draw(batch);
    }
    
    public boolean isDestroyed() {return destroyed;}
	
    public void setDestroyed(boolean b) {destroyed = b;}
    
    public Sprite getSpr() {return spr;}
    public int getBulletDmg() {return bulletDamage;}
}