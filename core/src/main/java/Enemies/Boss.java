package Enemies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Managers.BulletManager;

public class Boss extends Enemy implements EnemyTools{
	private static final Random random = new Random();
	private boolean changeBHP = true, multiStage = false, shiftSet = false;
	private float targetX, targetY; 
	private BulletManager bulletMng;
	
	public Boss (float initialPosX, float initialPosY, BulletManager bulletMng, int bossChoice, float scrW, float scrH) {
		spriteSheet = new Texture(Gdx.files.internal("allBossesSpriteSheet.png"));
		spriteRegions = TextureRegion.split(spriteSheet, 64, 78);
		explosionSound.setVolume(1,0.3f);
		shootingSound.setVolume(1,0.2f);
		bossSelectionAndAnimation(bossChoice);
		
    	spr.setPosition(initialPosX, initialPosY);
    	initHitbox();
		//spr.setBounds(initialPosX, initialPosY, 48, 48);
		
		maxIdleTime = 15.0f;
		
		// Use proportional values based on viewport
	    targetX = scrW / 2f - spr.getWidth() / 2f;
	    targetY = scrH - 300f; // boss starts ~65% up screen
	    
	    this.bulletMng = bulletMng;
	}
	
	public void bossSelectionAndAnimation(int bossChoice) {
		switch (bossChoice) {
			case 0:
				//Elige Keime
				TextureRegion[] animationFrames0 = new TextureRegion[4];
		    	for (int i = 0; i < 4; i++) {
		    		TextureRegion currentSprite = spriteRegions[0][i];
		            animationFrames0[i] = currentSprite;
		        }
		    	animation = new Animation<TextureRegion>(0.1f, animationFrames0);
		    	spr = new Sprite(animationFrames0[0]);
				break;
			case 1:
				//Elige Reisen
				TextureRegion[] animationFrames1 = new TextureRegion[4];
		    	for (int i = 0; i < 4; i++) {
		    		TextureRegion currentSprite = spriteRegions[3][i];
		            animationFrames1[i] = currentSprite;
		        }
		    	animation = new Animation<TextureRegion>(0.1f, animationFrames1);
		    	spr = new Sprite(animationFrames1[0]);
				break;
			case 2:
				//Elige Mokou
				TextureRegion[] animationFrames2 = new TextureRegion[4];
		    	for (int i = 0; i < 4; i++) {
		    		TextureRegion currentSprite = spriteRegions[5][i];
		            animationFrames2[i] = currentSprite;
		        }
		    	animation = new Animation<TextureRegion>(0.1f, animationFrames2);
		    	spr = new Sprite(animationFrames2[0]);
				break;
			case 3:
				//Elige Sakuya
				TextureRegion[] animationFrames3 = new TextureRegion[4];
		    	for (int i = 0; i < 4; i++) {
		    		TextureRegion currentSprite = spriteRegions[8][i];
		            animationFrames3[i] = currentSprite;
		        }
		    	animation = new Animation<TextureRegion>(0.1f, animationFrames3);
		    	spr = new Sprite(animationFrames3[0]);
				break;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		animationTime += deltaTime;
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true); // Loop animation
        spr.setRegion(currentFrame);
		spr.draw(batch);
		
	}

	@Override
	public void update(float scrWidth, float scrHeight) {
		enemyMovement(scrWidth, scrHeight);
		shootBulletHellPattern();
		updateHitbox(); // Only for collision with Reimu
	}
	
	/* FUNCIONES RELACIONADAS AL MOVIMIENTO DEL Boss
	 * 
	 * enemyMovement, bossInTarget, bossTrack, outOfBounds, selectNewArea
	 * 
	 * */
	@Override
	public void enemyMovement(float scrWidth, float scrHeight) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (firstSpawn) {
			bossTrack(deltaTime);
            if (bossInTarget()) {
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
            
            bossTrack(deltaTime);
            
            if (bossInTarget()) {
            	//System.out.println("Fairy reached target. Selecting new area...");
                inTrack = false;
                this.setSpeed(defaultSpeed);
            }
            outOfBounds(scrWidth, scrHeight);
        }
	}
	
	private boolean bossInTarget() {
		float distanceX = Math.abs(spr.getX() - targetX);
		float distanceY = Math.abs(spr.getY() - targetY);
		return distanceX < 10.0f && distanceY < 10.0f;
	}
	
	public void bossTrack(float deltaTime) {
		float currentX = spr.getX();
	    float currentY = spr.getY();
	    float directionX = targetX - currentX;
	    float directionY = targetY - currentY;
	    float distance = (float) Math.sqrt(directionX * directionX + directionY * directionY);
	    /*
	    System.out.println("Boss Position - X: " + currentX + ", Y: " + currentY);
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
	

	@Override
	public void selectNewArea(float scrWidth, float scrHeight) {
	    int area = random.nextInt(3);
	    while (area == currentArea) {
	        area = random.nextInt(3);
	    }
	    currentArea = area;

	    switch (currentArea) {
	        case 0:
	            // Left third, but start after a fixed 85px margin
	            targetX = 85 + random.nextFloat() * (scrWidth * 0.33f - 85);
	            break;
	        case 1:
	            // Middle third (fully proportional)
	            targetX = (scrWidth * 0.33f) + random.nextFloat() * (scrWidth * 0.33f);
	            break;
	        case 2:
	            // Right third, but clamp with a fixed 58px offset
	            targetX = (scrWidth * 0.66f) + random.nextFloat() * (scrWidth * 0.33f - 85);
	            break;
	    }

	    // Y between ~65% and 90% of screen height
	    targetY = (scrHeight * 0.65f) + random.nextFloat() * (scrHeight * 0.25f);
	    idleTime = 0f;
	}


	@Override
	public void shootBulletHellPattern() {
	    if (firstSpawn) {
	        isShooting = false;
	    } 
	    else {
	    	changePattern();
	        if (isShooting) {
	        	if (!multiStage) {
	        		singleStageShooting();
	        	}
	        	else {
	        		multiStageShooting();
	        	}
	            
	        } 
	        else {
	        	shootingCooldown();
	        }
	    }
	}
	
	public void multiStageShooting() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		shootingTime += deltaTime;
        bulletGenTimer += deltaTime;
        
        if (bulletGenTimer >= bulletMng.getTSBHP(bhpChoice).getBulletGenInterval()) {
        	bulletGenTimer = 0f;
        	bulletMng.generateEBullets(bhpChoice, spr.getX() + 16, spr.getY() + 16, true);
            shootingSound.play(0.25f);
        	
        }
        
		if (shootingTime >= bulletMng.getTSBHP(bhpChoice).getMaxShootingTime()) {
			if (bulletMng.getTSBHP(bhpChoice).hasDeceleration()) {
        		isShooting = false;
	            shootingTime = 0f;
			}
			else {
				if (!shiftSet) {
	        		bulletMng.setShiftBullets(true);
	        		shiftSet = true;
	        	}
	    		isShooting = false;
	            shootingTime = 0f;
			}
		}
	}
	
	public void singleStageShooting() {	
		float deltaTime = Gdx.graphics.getDeltaTime();
		shootingTime += deltaTime;
        bulletGenTimer += deltaTime;
        
        if (bulletGenTimer >= bulletMng.getBHP(bhpChoice).getBulletGenInterval()) {
        	bulletGenTimer = 0;
        	bulletMng.generateEBullets(bhpChoice, spr.getX() + 16, spr.getY() + 16, false);
            shootingSound.play(0.25f);
        }
        
        if (shootingTime >= bulletMng.getBHP(bhpChoice).getMaxShootingTime()) {
    		isShooting = false;
            shootingTime = 0f;
        }
	}
	
	public void shootingCooldown() {
		if (multiStage && bulletMng.getTSBHP(bhpChoice).hasDeceleration()) {
			float decelRate = bulletMng.getTSBHP(bhpChoice).getDecelerationRate(); // gets called multiple times but whatever
			if (bulletMng.isDecelerationOver()) {
				if (!shiftSet) {
					bulletMng.setShiftBullets(true);
					shiftSet = true;
				}
				else {
					float deltaTime = Gdx.graphics.getDeltaTime();
	    	        noShootingCooldown += deltaTime;
	    	        if (noShootingCooldown >= 3.0f /*has to be something else*/) {
	    	            isShooting = true;  // Enable shooting again
	    	            noShootingCooldown = 0f;      // Reset idleTime after cooldown
	    	            changeBHP = true;
	    	            if (multiStage) {multiStage = false;}
	    	            else {multiStage = true;}
	    	        }
				}
        	}
			else {
				bulletMng.decelerateBullets(decelRate);
			}
		}
		else {
			float deltaTime = Gdx.graphics.getDeltaTime();
	        noShootingCooldown += deltaTime;
	        if (noShootingCooldown >= 3.0f /*has to be something else*/) {
	            isShooting = true;  // Enable shooting again
	            noShootingCooldown = 0f;      // Reset idleTime after cooldown
	            changeBHP = true;
	            if (multiStage) {multiStage = false;}
	            else {multiStage = true;}
	        }
		}
		
	}
	
	public void changePattern() {
		if (changeBHP) {
			if (multiStage) {
				bhpChoice = random.nextInt(bulletMng.getTsBhpArrSize());
				changeBHP = false;
				shiftSet = false;
			}
			else {
				bhpChoice = random.nextInt(bulletMng.getBhpArrSize());
				changeBHP = false;
			}
		}
	}
	
	public void lowerBossHealthNSpeed(int health, float speed) {
		setHealth(health);
		setSpeed(speed);
		setDefaultSpeed(speed);
		System.out.println("Default Speed = "+defaultSpeed);
		System.out.println("Speed = "+speed);
	}
	
	public void setMultiStage(boolean bool) {multiStage = bool;}
	public boolean getMultiStage() {return multiStage;}
	
	public void dispose() {spriteSheet.dispose();}
}