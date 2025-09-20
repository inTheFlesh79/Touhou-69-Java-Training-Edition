package Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import Managers.BulletManager;
import Managers.FairyManager;

import java.util.Random;

public class Fairy extends Enemy implements EnemyTools{
	private float deltaTime = Gdx.graphics.getDeltaTime();
	private static final Random random = new Random();
	private float targetX, targetY; 
	private BulletManager bulletMng;
	private FairyManager fairyMng = new FairyManager();
	private boolean isShootSoundAllowed = false;

	public Fairy (float initialPosX, float initialPosY, int firstTargetX, int firstTargetY, BulletManager bulletMng, TextureRegion[][] spriteRegions) {
		explosionSound.setVolume(1,0.3f);
		shootingSound.setVolume(1,0.14f);
		int randomRow = random.nextInt(4);//Elige entre 4 estilos de Fairy distintos
		
		TextureRegion[] animationFrames = new TextureRegion[4];
    	for (int i = 0; i < 4; i++) {
    		TextureRegion currentSprite = spriteRegions[randomRow][i];
            animationFrames[i] = currentSprite;
        }
    	animation = new Animation<TextureRegion>(0.1f, animationFrames);
    	spr = new Sprite(animationFrames[0]);
    	//spr.setPosition(initialPosX, initialPosY);
		spr.setBounds(initialPosX, initialPosY, 48, 48);
		
		maxIdleTime = 3.0f;
		
		targetX = firstTargetX;
	    targetY = firstTargetY;
	    
	    this.bulletMng = bulletMng;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		animationTime += deltaTime;
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true); // Loop animation
        spr.setRegion(currentFrame);
		spr.draw(batch);		
	}
	
	public void update() {
		//System.out.println("Fairy Speed = "+this.getSpeed());
		//System.out.println("Fairy Health = "+this.getHealth());
		enemyMovement();
		shootBulletHellPattern();
	}
	
	/* FUNCIONES RELACIONADAS AL DISPARO DE PATRONES DE BALA DEL FAIRY
	 * 
	 * 
	 * 
	 * */
	public void shootBulletHellPattern() {
		float deltaTime = Gdx.graphics.getDeltaTime();
	    if (firstSpawn) {
	    	shootingLogic(deltaTime);
	    } 
	    else {
	    	shootingLogic(deltaTime);
	    }
	}
	
	public void generateEBullets() {
		for (int i = 0; i < bulletPattern.getCantBullet(); i++) {
			EnemyBullet generatedEBullet = bulletMng.craftEnemyBullet(spr.getX() + 16, spr.getY() + 16);
			bulletPattern.generateBulletInPattern(spr.getX() + 16, spr.getY() + 16, generatedEBullet);
            bulletMng.addEnemyBullets(generatedEBullet);
	    }
	}
	
	public void shootingLogic(float deltaTime) {

	    if (isShooting) {
	        shootingTime += deltaTime;
	        bulletGenTimer += deltaTime;

	        if (bulletGenTimer >= bulletPattern.getBulletGenInterval()) {
	            bulletGenTimer = 0;
	            //System.out.println("This Fairy is Allowed to Sound When Shooting? "+isShootSoundAllowed);
	            //System.out.println("SOUND PLAYED!")
	            generateEBullets();
	            if (isShootSoundAllowed) {shootingSound.play(0.2f);}
	             // mark that this fairy fired
	        }
	        if (shootingTime >= bulletPattern.getMaxShootingTime()) {
	            isShooting = false;
	            shootingTime = 0f;
	        }
	    } else {
	        noShootingCooldown += deltaTime;
	        if (noShootingCooldown >= 3.0f) {
	            isShooting = true;
	            
	            noShootingCooldown = 0f;
	        }
	    }
	    
	}

	
	/* FUNCIONES RELACIONADAS AL MOVIMIENTO DEL FAIRY
	 * 
	 * enemyMovement, fairyInTarget, fairyTrack, outOfBounds, selectNewArea
	 * 
	 * */

	@Override
	public void enemyMovement() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (firstSpawn) {
			fairyTrack(deltaTime);
            
            if (fairyInTarget()) {
            	//System.out.println("First Time in Movement");
                firstSpawn = false;
                fairyMng.manageSpeed(this, this.getSpeedChoice());
            }
		    
		}
		else {
			if (!inTrack) {
				idleTime += deltaTime;
			}
            
            if (idleTime >= maxIdleTime) {
            	//System.out.println("In Movement");
            	inTrack = true;
                selectNewArea(); 
                
            }
            
            fairyTrack(deltaTime);
            
            if (fairyInTarget()) {
            	//System.out.println("Fairy reached target. Selecting new area...");
                inTrack = false;
                fairyMng.manageSpeed(this, this.getSpeedChoice());
            }
            outOfBounds();
        }
        
	}
	
	private boolean fairyInTarget() {
		float distanceX = Math.abs(spr.getX() - targetX);
		float distanceY = Math.abs(spr.getY() - targetY);
		return distanceX < 10.0f && distanceY < 10.0f;
	}

	
	public void fairyTrack(float deltaTime) {
		float currentX = spr.getX();
	    float currentY = spr.getY();
	    float directionX = targetX - currentX;
	    float directionY = targetY - currentY;
	    
	    float distance = (float) Math.sqrt(directionX * directionX + directionY * directionY);
	    
	    /*
	    System.out.println("Fairy Position - X: " + currentX + ", Y: " + currentY);
	    System.out.println("Target Position - X: " + targetX + ", Y: " + targetY);
	    System.out.println("Distance to Target: " + distance);
	    */

	    if (distance > 5.0f) {
	        directionX /= distance;
	        directionY /= distance;

	        speed *= 0.99f;
	        if (speed < 100.0f) speed = 100.0f;

	        spr.setX(currentX + directionX * speed * deltaTime);
	        spr.setY(currentY + directionY * speed * deltaTime);
	    } 
	    else {
	        spr.setX(targetX);
	        spr.setY(targetY);
	    }
	}
	
	public void selectNewArea() {
		int area = random.nextInt(3);
		while (area == currentArea) {
			area = random.nextInt(3);
		}
		currentArea = area;
		switch (currentArea) {
			case 0:
				//System.out.println("area 0");
				targetX = random.nextInt(32,399);
				break;
			case 1:
				//System.out.println("area 1");
				targetX = 400 + random.nextInt(799);
				break;
			case 2:
				//System.out.println("area 2");
				targetX = 800 + random.nextInt(340);;
				break;
		}
		
		targetY = 500 + random.nextInt(120);
		idleTime = 0f;
	}

	public void outOfBounds() {
		if (spr.getX() < 0) spr.setX(0);
        if (spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) spr.setX(Gdx.graphics.getWidth() - spr.getWidth());
        if (spr.getY() < 0) spr.setY(0);
        if (spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) spr.setY(Gdx.graphics.getHeight() - spr.getHeight());
	}
	
	public void dispose() {
	    spriteSheet.dispose();
	}
	
	public Rectangle getBoundingRectangle() {return spr.getBoundingRectangle();}
	public void setMaxShootingTime(float mst) {this.maxShootingTime = mst;}
	public void setBulletGenInterval(float bgi) {this.bulletGenInterval = bgi;}
	public void setIsShootSoundAllowed(boolean b) {isShootSoundAllowed = b;}
	
	public boolean getIsShootSoundAllowed() {return isShootSoundAllowed;}
	public void getSpawnSpeedChoice() {}
}