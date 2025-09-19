package Managers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import Reimu.Drop;
import Reimu.OneUpDrop;
import Reimu.PowerDrop;
import Reimu.Reimu;
import Reimu.ScoreDrop;
import Reimu.ShieldDrop;

public class DropManager {
	private ArrayList<Drop> drops = new ArrayList<>();
	
	public DropManager () {}
	
	public void drawDrops(SpriteBatch batch) {
		for (int i = 0; i < drops.size(); i++) {
			Drop d = drops.get(i);
			if (d.isDestroyed()) {
				d.dispose();
				drops.remove(i);
				i--;
			}
			d.draw(batch);
			d.update();
		}
	}
	
	public void spawnDrop(float x, float y) {
        //float chance = MathUtils.random();
        Drop d = new Drop();
        d = addDrop(x, y);
        drops.add(d);
        d = addExtraDrop(x, y);
        drops.add(d);
    }
	
	public Drop craftDrop(float x, float y, int choice) {
        switch (choice) {
        	case 1:
        		return new ScoreDrop(x,y);
        	case 2:
        		return new PowerDrop(x,y);
        	case 3:
        		return new OneUpDrop(x,y);
        	case 4:
        		return new ShieldDrop(x,y);
        	default:
        		return new ScoreDrop(x,y);
        }
	}
	
	public Drop addDrop(float x, float y) {
		float chance = MathUtils.random();
		if (chance < 0.5) {
    		return craftDrop(x+20, y-15, 1);
        }
        else {
        	return craftDrop(x-20, y-15, 1);
        }
	}
	
	public Drop addExtraDrop(float x, float y) {
		float chance = MathUtils.random();
		if (chance < 0.6) { // ScoreDrop
        	return craftDrop(x, y+15, 1);
        }
        else if (chance < 0.85) { // PowerDrop
        	return craftDrop(x, y+15, 2);
        }
        else if (chance < 0.9){ // OneUpDrop
        	return craftDrop(x, y+15, 3);
        }
        else { // ShieldDrop
        	return craftDrop(x, y+15, 4);
        }
	}
	
	public int dropBehavior(Drop d) {
		if (d instanceof ScoreDrop) {
			return 1;
		}
		else if (d instanceof ShieldDrop) {
			return 2;
		}
		else if (d instanceof OneUpDrop) {
			return 3;
		}
		else if (d instanceof PowerDrop) {
			return 4;
		}
		else {
			return 1;
		}
	}
	
	public void applyDropEffect(Drop drop, Reimu reimu) {
	    switch (dropBehavior(drop)) {
	        case 1: // ScoreDrop
	            reimu.addScore(500);
	            break;
	        case 2: // ShieldDrop
	            reimu.craftShield();
	            reimu.setShielded(true);
	            reimu.addScore(100);
	            break;
	        case 3: // OneUpDrop
	            reimu.oneUp();
	            reimu.addScore(100);
	            break;
	        case 4: // PowerDrop
	            reimu.addDamage(10);
	            reimu.addScore(100);
	            break;
	        default:
	            reimu.addScore(500);
	            break;
	    }
	}
	public boolean isScoreDrop(Drop d) {return d instanceof ScoreDrop;}
	public boolean isShieldDrop(Drop d) {return d instanceof ShieldDrop;}
	
	public int getDropsSize() {return drops.size();}
	public Drop getDrop(int choice) {return drops.get(choice);}
	
	public void removeDrop(int choice) {drops.remove(choice);}
}