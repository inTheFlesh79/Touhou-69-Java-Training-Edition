package Managers;

import com.badlogic.gdx.math.MathUtils;

import Reimu.Drop;
import Reimu.OneUpDrop;
import Reimu.PowerDrop;
import Reimu.ScoreDrop;
import Reimu.ShieldDrop;

public class DropManager {
	public DropManager () {}
	
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
		if (chance < 0.3) { // ScoreDrop
        	return craftDrop(x, y+15, 1);
        }
        else if (chance < 0.5) { // PowerDrop
        	return craftDrop(x, y+15, 2);
        }
        else if (chance < 0.6){ // OneUpDrop
        	return craftDrop(x, y+15, 3);
        }
        else {
        	return craftDrop(x, y+15, 4);
        }
	}
	
	
	
	public boolean isScoreDrop(Drop d) {return d instanceof ScoreDrop;}
}
