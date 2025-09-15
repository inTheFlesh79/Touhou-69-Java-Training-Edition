package Enemies;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import BulletHellPatterns.BulletHellPattern;
import Managers.BossManager;
import Managers.GameObjectManager;

public class Boss extends Enemy implements EnemyTools{
	//private static Boss bossInstance;
	private static final Random random = new Random();
	
	private boolean changeBHP = true;
	private float targetX, targetY; 
	private GameObjectManager gameMng;
	private BossManager bossMng = new BossManager();
	private ArrayList<BulletHellPattern> bossBHPlist = new ArrayList<>();
	
	public Boss (float initialPosX, float initialPosY, GameObjectManager gameMng) {
		spriteSheet = new Texture(Gdx.files.internal("allBossesSpriteSheet.png"));
		spriteRegions = TextureRegion.split(spriteSheet, 64, 78);
		
		int bossChoice = random.nextInt(4);
		bossSelectionAndAnimation(bossChoice);
		
		speedChoice = random.nextInt(bossMng.getCantSpeedOptions());
		int healthChoice = random.nextInt(bossMng.getCantHealthOptions());
		bossMng.manageSpeed(this, speedChoice);
		bossMng.manageHealth(this, healthChoice);
		bossMng.manageBHPType(this,0);
		
    	spr.setPosition(initialPosX, initialPosY);
		//spr.setBounds(initialPosX, initialPosY, 48, 48);
		
		maxIdleTime = 5.0f;
		
		targetX = 600 - 16;
	    targetY = 600;
	    
	    this.gameMng = gameMng;
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
	public void update() {
		System.out.println("Boss Speed = "+this.getSpeed());
		enemyMovement();
		shootBulletHellPattern();
	}
	
	/* FUNCIONES RELACIONADAS AL MOVIMIENTO DEL Boss
	 * 
	 * enemyMovement, bossInTarget, bossTrack, outOfBounds, selectNewArea
	 * 
	 * */
	@Override
	public void enemyMovement() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (firstSpawn) {
		    
			bossTrack(deltaTime);
            
            if (bossInTarget()) {
            	//System.out.println("First Time in Movement");
                firstSpawn = false;
                bossMng.manageSpeed(this, this.getSpeedChoice());
            }
		    
		}
		else {
			if (!inTrack) {
				System.out.println();
				idleTime += deltaTime;
			}
            
            if (idleTime >= maxIdleTime) {
            	//System.out.println("In Movement");
            	inTrack = true;
                selectNewArea(); 
                
            }
            
            bossTrack(deltaTime);
            
            if (bossInTarget()) {
            	//System.out.println("Fairy reached target. Selecting new area...");
                inTrack = false;
                bossMng.manageSpeed(this, this.getSpeedChoice());
            }
            outOfBounds();
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
	public void selectNewArea() {
		int area = random.nextInt(5);
		while (area == currentArea) {
			area = random.nextInt(5);
		}
		
		currentArea = area;
		switch (currentArea) {
			case 0:
				//System.out.println("area 0");
				targetX = random.nextInt(100,240);
				break;
			case 1:
				//System.out.println("area 1");
				targetX = 240 + random.nextInt(240);
				break;
			case 2:
				//System.out.println("area 2");
				targetX = 480 + random.nextInt(240);;
				break;
			case 3:
				targetX = 720 + random.nextInt(240);
				break;
			case 4:
				targetX = 960 + random.nextInt(150);
				break;
		}
		
		targetY = 500 + random.nextInt(120);
		idleTime = 0f;
	}

	@Override
	public void shootBulletHellPattern() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (changeBHP) {
			bhpChoice = random.nextInt(bossBHPlist.size());
			bulletPattern = bossBHPlist.get(bhpChoice);
			changeBHP = false;
		}
		
	    if (firstSpawn) {
	        isShooting = false;
	    } 
	    else {
	        // If Fairy is shooting, generate the bullet pattern and update shooting time
	        if (isShooting) {
	        	
	            shootingTime += deltaTime;
	            bulletGenTimer += deltaTime;
	            
	            if (bulletGenTimer >= bulletPattern.getBulletGenInterval()) {
		            for (int i = 0; i < bulletPattern.getCantBullet(); i++) {
		            	bulletGenTimer = 0;
		            	EnemyBullet generatedEBullet = bulletPattern.generateBulletInPattern(spr.getX()+16, spr.getY()+16);
		            	gameMng.agregarEnemyBullets(generatedEBullet);
		            }
	            }
	            
	            // If the shooting time exceeds max, stop shooting and start cooldown
	            if (shootingTime >= bulletPattern.getMaxShootingTime()) {
	            	//bulletPattern.setAngle(0);
	                isShooting = false;
	                shootingTime = 0f;  // Reset shooting time to zero for the next cooldown phase
	            }
	            
	        } 
	        else {
	            // Cooldown phase: wait for 3 seconds
	            noShootingCooldown += deltaTime;

	            // Once cooldown of 3 seconds has passed, enable shooting again
	            if (noShootingCooldown >= 1.5f) {
	                isShooting = true;  // Enable shooting again
	                noShootingCooldown = 0f;      // Reset idleTime after cooldown
	                changeBHP = true;
	            }
	        }
	    }
	}

	@Override
	public void outOfBounds() {
		if (spr.getX() < 0) spr.setX(0);
        if (spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) spr.setX(Gdx.graphics.getWidth() - spr.getWidth());
        if (spr.getY() < 0) spr.setY(0);
        if (spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) spr.setY(Gdx.graphics.getHeight() - spr.getHeight());
		
	}
	
	public void dispose() {
	    spriteSheet.dispose();
	    // Dispose of other resources
	}
	
	public void setBossBHPlist(BulletHellPattern bhp) {
		bossBHPlist.add(bhp);
	}
}
