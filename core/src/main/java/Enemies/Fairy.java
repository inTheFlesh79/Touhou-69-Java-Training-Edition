package Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import Managers.BulletManager;

import java.util.Random;

public class Fairy extends Enemy implements EnemyTools{
	private static final Random random = new Random();
	private float targetX, targetY;
	private BulletManager bulletMng;
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
		
		initHitbox();
		
		maxIdleTime = 3f;
		
		targetX = firstTargetX;
	    targetY = firstTargetY;
	    
	    this.bulletMng = bulletMng;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		animationTime += deltaTime;
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true); // Loop animation
        spr.setRegion(currentFrame);
		spr.draw(batch);		
	}
	
	public void update(float scrWidth, float scrHeight) {
		//System.out.println("Fairy Speed = "+this.getSpeed());
		//System.out.println("Fairy Health = "+this.getHealth());
		enemyMovement(scrWidth, scrHeight);
		shootBulletHellPattern();
		updateHitbox(); // Only for collision with Reimu
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
	
	
	public void shootingLogic(float deltaTime) {
	    if (isShooting) {
	        shootingTime += deltaTime;
	        bulletGenTimer += deltaTime;
	        if (bulletGenTimer >= bulletMng.getBHP(bhpChoice).getBulletGenInterval()) {
	            bulletGenTimer = 0;
	            //System.out.println("This Fairy is Allowed to Sound When Shooting? "+isShootSoundAllowed);
	            //System.out.println("SOUND PLAYED!")
	            bulletMng.generateEBullets(bhpChoice, spr.getX(), spr.getY(), false);
	            if (isShootSoundAllowed) {shootingSound.play(0.2f);}
	        }
	        if (shootingTime >= bulletMng.getBHP(bhpChoice).getMaxShootingTime()) {
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
	public void enemyMovement(float scrWidth, float scrHeight) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (firstSpawn) {
			fairyTrack(deltaTime);
            
            if (fairyInTarget()) {
            	//System.out.println("First Time in Movement");
                firstSpawn = false;
                this.setSpeed(defaultSpeed);
            }
		    
		}
		else {
			if (!inTrack) {
				idleTime += deltaTime;
			}
            
            if (idleTime >= maxIdleTime) {
            	//System.out.println("In Movement");
            	inTrack = true;
                selectNewArea(scrWidth, scrHeight); 
                
            }
            
            fairyTrack(deltaTime);
            
            if (fairyInTarget()) {
            	//System.out.println("Fairy reached target. Selecting new area...");
                inTrack = false;
                this.setSpeed(defaultSpeed);
            }
            outOfBounds(scrWidth, scrHeight);
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
	    
	    //System.out.println("Fairy Position - X: " + currentX + ", Y: " + currentY);
	    //System.out.println("Target Position - X: " + targetX + ", Y: " + targetY);
	    //System.out.println("Distance to Target: " + distance);	

	    if (distance > 2.0f) {
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
	
	public void selectNewArea(float scrWidth, float scrHeight) {
	    int area = random.nextInt(3);
	    while (area == currentArea) {
	        area = random.nextInt(3);
	    }
	    currentArea = area;

	    switch (currentArea) {
	        case 0:
	            targetX = 48 + (random.nextFloat() * (scrWidth * 0.33f)); // left third
	            break;
	        case 1:
	            targetX = (scrWidth * 0.33f) + random.nextFloat() * (scrWidth * 0.33f); // middle
	            break;
	        case 2:
	            targetX = ((scrWidth * 0.66f) + random.nextFloat() * (scrWidth * 0.33f)) - 58; // right third
	            break;
	    }

	    targetY = scrHeight * 0.7f + random.nextFloat() * (scrHeight * 0.25f); // 70–95% height
	    idleTime = 0f;
	}
	
	public void dispose() {
	    spriteSheet.dispose();
	}
	
	public Rectangle getBoundingRectangle() {return spr.getBoundingRectangle();}
	public void setMaxShootingTime(float mst) {this.maxShootingTime = mst;}
	public void setBulletGenInterval(float bgi) {this.bulletGenInterval = bgi;}
	public void setIsShootSoundAllowed(boolean b) {isShootSoundAllowed = b;}
	public void setTarget(float x, float y) {
		targetX = x;
		targetY = y;
	}
	
	public boolean getIsShootSoundAllowed() {return isShootSoundAllowed;}
	public void getSpawnSpeedChoice() {}
	public float getTargetX() {return targetX;}
	public float getTargetY() {return targetY;}
}