package scripts;
 
import java.awt.Point;
import java.util.Random;

import org.tribot.api.Constants;
import org.tribot.api.Inventory;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
 
@ScriptManifest(authors = { "J J" }, category = "Woodcutting", name = "JJ's Woodcutter")
public class JJsWoodcutter extends Script{
	final int method = 2; // 0 = dropping, 1 = bonfire, 2 = random
	final long[] willowIDs = {3561420067L, 3774714158L, 1241034410L, 516270165L};
	final int willowID = 242430;
	final long animatingID = 743622227L, bonfireID = 2057907316L,
			   flyingWillow = 1482987360L, bonfireAnimation = 1902096871L;
	
	public int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	// CHOPPING
	private void chopTree(){
		ScreenModel[] willow = ScreenModels.findNearest(willowIDs);
		if (willow.length > 0){
			willow[0].hover(new Point(-5, 5), new Point(-5, 5));
			sleep(50, 100);
			if (Timing.waitUptext("Chop down Willow", 500)){
				Mouse.click(1);
				sleep(2000, 3000);
			}
		}
	}

	private boolean isChopping(){
		ScreenModel[] animating = ScreenModels.find(animatingID);
		if (animating.length > 0){
			return true;
		}
		return false;
	}
	
	// DROPPING
	private void dropWillows(){
		InventoryItem[] willows = Inventory.find(willowID);
		if (willows.length > 0){
			for (int i = 0; i < willows.length; i++){
				Point P = new Point(willows[i].x + 15 + randomRange(-10, 10), willows[i].y + 15 + randomRange(-10, 10)); 
				Mouse.move(P);
				Mouse.click(3);
				Timing.waitChooseOption("Drop", 500);
			}
		}
	}
	
	private boolean fullInv(){
		InventoryItem[] all = Inventory.getAll();
		if (all.length > 27){
			return true;
		}
		return false;
	}
	
	// BONFIRE MAKING
	private boolean lightWillow(){
		InventoryItem[] willows = Inventory.find(willowID);
		if (willows.length > 0){
			println("We have " + willows.length + " willow logs");
			int random = randomRange(0, willows.length-1);
			println("Random number: " + random);
			Point P = new Point(willows[random].x + 15 + randomRange(-10, 10), willows[random].y + 15 + randomRange(-10, 10));
			Mouse.move(P);
			Mouse.click(3);
			if (Timing.waitChooseOption("Light", 500)){
				sleep(4000, 5000);
				return true;
			}
		}
		return false;
	}
	
	private boolean addWillow(){
		ScreenModel[] bonfire = ScreenModels.findNearest(bonfireID);
		if (bonfire.length > 0){
			bonfire[0].hover(new Point(-5, 5), new Point(-5, 5));
			if (Timing.waitUptext("2 more options", 500)){
				Mouse.click(3);
				if (Timing.waitChooseOption("Add-logs", 500)){
					return true;
				}
			}
		}
		return false;
	}
	
	private void finishAdding(){
		long[] animating = {flyingWillow, bonfireAnimation};
		long maxTime = System.currentTimeMillis() + 5000;
		
		while(System.currentTimeMillis() < maxTime){
			ScreenModel[] animation = ScreenModels.find(animating);
			if (animation.length > 0){
				maxTime = System.currentTimeMillis() + 3000;
				sleep(200, 400);
			}
			sleep(20, 40);
		}
	}

    @Override
    public void run() {
        println("JJ's Woodcutter has been started to run to infinity and beyond!");
        boolean infinite = true;
        
        while(infinite){
        	// While the inventory isn't full, chop the tree
	        while(!fullInv()){
	        	if(isChopping()){
	        		sleep(2000, 4000);
	        	}else{
	        		chopTree();
	        	}
	        }
	        
	        // Generating a random number if we are using the "Random" option
	        int tempMethod = -1;
	        switch(method){
	        	case 0: case 1: tempMethod = method;
	        				   break;
	        	case 2: tempMethod = randomRange(0, 1);
	        			break;
	        }
	        
	        println("Temp method: " + tempMethod);
	        
	        // Doing what the user has selected
	        switch(tempMethod){
		        case 0: println("Dropping");
		        		dropWillows();
		        		break;
		        case 1: println("Bonfire");
				        if (lightWillow()){
				        	println("We have lit a willow");
				        	if (addWillow()){
				        		println("We have added a willow");
				        		finishAdding();
				        	}
				        }
				        break;
	        }
        }
    }
}