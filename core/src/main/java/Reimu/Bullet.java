package Reimu;
            
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
	private int xSpeed;
	private int ySpeed;
	private boolean destroyed = false;
	private Sprite spr;
	private int bulletDamage;
	    
    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx, int bulletDamage) {
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.bulletDamage = bulletDamage;
    }
    
    public void update(float scrWidth, float scrHeight) {
        spr.setPosition(spr.getX()+xSpeed, spr.getY()+ySpeed);
        outOfBounds(scrWidth, scrHeight);
    }
    
    public void outOfBounds(float scrWidth, float scrHeight) {
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > scrWidth) {destroyed = true;}
        if (spr.getY() < 0 || spr.getY() + spr.getHeight() > scrHeight) {destroyed = true;}
    }
    
    public void draw(SpriteBatch batch) {spr.draw(batch);}
    
    public boolean isDestroyed() {return destroyed;}
    public void setDestroyed(boolean b) {destroyed = b;}
    
    public Sprite getSpr() {return spr;}
    public int getBulletDmg() {return bulletDamage;}
}