package scripts;

import java.awt.Point;
import java.util.Random;

import org.tribot.api.ChooseOption;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.TPS;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Money Making", name = "Yanille Sand Gatherer")
public class SandGatherer extends Script{
	
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
    
    private boolean randomSelectOptionAt(Point coord, String upText, String option, int leftChance, int rightChance, int maxWait){
    	int totalChance = leftChance + rightChance;
    	Mouse.move(coord);
    	if (Timing.waitUptext(upText, (maxWait/2))){
        	if ((new Random().nextInt(totalChance)) < leftChance){
        		Mouse.click(coord, 1);
        		return true;
        	}else{
        		Mouse.click(coord, 3);
        		if (Timing.waitChooseOption(option, (maxWait/2))){
        			ChooseOption.select(option);
        			return true;
        		}else{
        			return false;
        		}
        	}
    	}else{
    		return false;
    	}
    }
    
    // Checks if the player is moving
 	public boolean isMoving(){
 		Point MMc = new Point(627, 135);
 		int pixelShift = Screen.getPixelShift(MMc.x - 20, MMc.y - 20, MMc.x + 20, MMc.y + 20, 100);
 		println("Pixelshift: " + pixelShift);
 		if (pixelShift > 100){
 			return true;
 		}
 		
 		return false;
 	}
 	
 	// Waits until we are not moving
 	private void waitUntilNotMoving(){
     	sleep(750, 1250);
 		while (isMoving()){
 			sleep(50, 100);
 		}
     }
 	
 	// Blindly walks
 	private boolean blindWalkTo(Point pos, int maxTime) {
        long A = System.currentTimeMillis();
        long B = A + maxTime;
        Point MMc = new Point(627, 135);
        int radius = 75;

        while ((Math.abs(TPS.getPosition().x - pos.x) + Math.abs(TPS.getPosition().y - pos.y)) > 10 && (System.currentTimeMillis() - A) < B ) {
            Point myPos = TPS.getPosition();
            int xDist = pos.x - myPos.x;
            int yDist = pos.y - myPos.y;
            int absX = Math.abs(xDist);
            int absY = Math.abs(yDist);

            if (absX <= 50 && absY <= 50) {
                Point P = TPS.posToMM(pos);
                Mouse.move(P);
                Mouse.click(1);
                waitUntilNotMoving();
                if ((System.currentTimeMillis() - A) < B)
                    return true;
            }

            Point[] TPA = new Point[(absX + 1) * (absY + 1)];
            for (int i = 0; i < absX; i++) {
                for (int j = 0; j < absY; j++) {
                    if (xDist < 0) {
                        if (yDist < 0)
                            TPA[i * j + j] = new Point(myPos.x - i, myPos.y - j);
                        else
                            TPA[i * j + j] = new Point(myPos.x - i, myPos.y + j);
                    } else {
                        if (yDist < 0)
                            TPA[i * j + j] = new Point(myPos.x + i, myPos.y - j);
                        else
                            TPA[i * j + j] = new Point(myPos.x + i, myPos.y + j);
                    }
                }
            }

            int far = 0;
            Point bestPoint = new Point(0, 0);
            for (int i = 0; i < TPA.length; i++) {
                if (TPA[i] != null) {
                    Point P = new Point(TPA[i].x, TPA[i].y);
                    xDist = Math.abs(myPos.x - P.x);
                    yDist = Math.abs(myPos.y - P.y);
                    if ((xDist + yDist) > far) {
                        if (xDist <= radius) {
                            if (yDist <= radius) {
                                P = TPS.posToMM(P);
                                double dist = Math.pow(MMc.x - P.x, 2)
                                                + Math.pow(MMc.y - P.y, 2);
                                if (dist < (Math.pow(radius, 2))) {
                                        far = (xDist + yDist);
                                        bestPoint = P;
                                }
                            }
                        }
                    }
                }
            }

            if (bestPoint != new Point(0, 0)) {
                Mouse.move(bestPoint);
                Mouse.click(1);
                waitUntilNotMoving();
                       
               
                Point coord = TPS.MMToPos(bestPoint);
                int dist = Math.abs(coord.x - pos.x) + Math.abs(coord.y - pos.y);
                if ((dist < 5) && (System.currentTimeMillis() - A) < B){
                	return true;
                }
            }
        }

        return false;
    }
	
	private boolean toBank(){
		// 2395, 6440 -> 2399, 6452
		Point P = new Point(randomRange(2395, 2399), randomRange(6440, 6452));
		if (blindWalkTo(P, 30000)){
			return true;
		}
		return false;
	}
	
	private boolean toSand(){
		// 2114, 6395 -> 2121, 6400
		Point P = new Point(randomRange(2114, 2121), randomRange(6395, 6400));
		if (blindWalkTo(P, 30000)){
			return true;
		}
		return false;
	}
	
	
	
	@Override
    public void run() {
    	toBank();
    	//toSand();
    }
}