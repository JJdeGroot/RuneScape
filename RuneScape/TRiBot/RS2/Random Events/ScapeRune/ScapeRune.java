package scripts;

import java.awt.Point;

import org.tribot.api.GameTab;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.Inventory;
import org.tribot.api.Minimap;
import org.tribot.api.Player;
import org.tribot.api.Screen;
import org.tribot.api.TPS;
import org.tribot.api.NPCChat;
import org.tribot.api.Constants;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Random Events", name = "ScapeRune")
public class ScapeRune extends Script{
	Point startPos = TPS.getRelativePosition();
    String location = "none";
    int spotNumber = -1;
	
	final long servant = 1775479756L,
			   uncookingPot = 812075924L,
			   evilBob = 711901918L,
			   fishNet = 3743111315L,
			   northSpot = 3360696142L,
			   eastSpot = 1356743974L,
			   southSpot = 1038755496L,
			   westSpot = 4200260916L,
			   depositBox = 1297607174L,
			   fishSpotID = 2704514198L,
			   portal = 2461837727L;
    final long[] directions = {northSpot, eastSpot, southSpot, westSpot};
	final int fish = 265020,
			  fishNetInv = 243492;
    final String[] locations = {"north", "east", "south", "west"};
    final Point MMc = new Point(Constants.MMCX, Constants.MMCY),
    		    servantSpot = startPos,
    		    northFishSpot = new Point(startPos.x + 10, startPos.y - 45),
    		    eastFishSpot =  new Point(startPos.x + 65, startPos.y),
    		    southFishSpot = new Point(startPos.x, startPos.y + 50),
    		    westFishSpot = new Point(startPos.x - 60, startPos.y + 5),
    		    northNetSpot = new Point(startPos.x + 35, startPos.y - 30),
	    		eastNetSpot = northNetSpot,
				southNetSpot = new Point(startPos.x + 35, startPos.y + 30),
				westNetSpot = new Point(startPos.x - 30, startPos.y - 30),
    			depositBoxSpot = new Point(startPos.x + 20, startPos.y),
    			uncookSpot = new Point(startPos.x - 10, startPos.y);
    final Point[] fishSpots = {northFishSpot, eastFishSpot, southFishSpot, westFishSpot},
    		      netSpots = {northNetSpot, eastNetSpot, southNetSpot, westNetSpot};
 
    public boolean isMoving(){
		Point MMc = new Point(627, 135);
		int pixelShift = Screen.getPixelShift(MMc.x - 60, MMc.y - 60, MMc.x + 60, MMc.y + 60, 500);
		println("Pixelshift: " + pixelShift);
		if (pixelShift > 250){
			return true;
		}
		
		return false;
	}
    
    private void waitUntilNotMoving(){
    	sleep(500, 1000);
		while (isMoving()){
			sleep(50, 100);
		}
    }
    
    private void toPos(Point pos){
		Point myPos = TPS.getRelativePosition();
		println("My position: " + myPos);
		int xDifference = pos.x - myPos.x,
			yDifference = pos.y - myPos.y;
		double difference = Math.abs(xDifference) + Math.abs(yDifference);

		if (difference > 10){
			Point P = new Point(MMc.x + xDifference, MMc.y + yDifference);
			if (Minimap.isOnMinimap(P)){
				Mouse.move(P);
				Mouse.click(1);
			}else{
				P = new Point(MMc.x + xDifference/2, MMc.y + yDifference/2);
				Mouse.move(P);
				Mouse.click(1);
				waitUntilNotMoving();
				toPos(pos);
			}
			waitUntilNotMoving();
		}
	}

	private boolean servantTalk(){
		ScreenModel[] serv = ScreenModels.find(servant);
		if (serv.length > 0){
			Point P = new Point(serv[0].base_point.x + 3 - Constants.RANDOM.nextInt(6), serv[0].base_point.y - 15 + Constants.RANDOM.nextInt(10));
			Mouse.move(P);
			if (Timing.waitUptext("Servant", 1000)){
				Mouse.click(1);
				waitUntilNotMoving();

				ScreenModel[] all = null;
				while (NPCChat.getName().contains("Servant")){
					String text = NPCChat.getMessage();
					if (text.contains("That fishing spot")){
						sleep(6000, 8000);
						all = ScreenModels.getAll();
						break;
					}else{
						NPCChat.clickContinue(true);
					}
				}

				boolean found = false;
				if (all != null){
					for (int i = 0; i < all.length; i++){
						if (!found){
							for (int j = 0; j < directions.length; j++){
								if (all[i].id == directions[j]){
									println("FOUND: " + directions[j]);
									switch(j){
										case 0: location = "north"; break;
										case 1: location = "east"; break;
										case 2: location = "south"; break;
										case 3: location = "west"; break;
									}
									found = true;
									break;
								}
							}
						}else{
							break;
						}
					}
				}
				
				println("Location: " + location);
				if (!location.contains("none")){
					NPCChat.clickContinue(true);
					sleep(500, 1000);
					return true;
				}
			}
		}

		return false;
	}
	
	private boolean fullInv(){
		if (ScreenModels.getAll().length >= 25){
			return true;
		}
		return false;
	}
	
	private void bankItem(){
		toPos(depositBoxSpot);
		ScreenModel[] box = ScreenModels.find(depositBox);
		if (box.length > 0){
			Point P = new Point(box[0].base_point.x + 3 - Constants.RANDOM.nextInt(11), box[0].base_point.y + 3 - Constants.RANDOM.nextInt(11));
			Mouse.move(P);
			if (Timing.waitUptext("deposit box", 1000)){
				Mouse.click(1);
				waitUntilNotMoving();
				
				// Generating random boxes
				int[] random = new int[5];
				for (int i = 0; i < random.length; i++){
					random[i] = Constants.RANDOM.nextInt(28);
					for (int j = 0; j < random.length; j++){
						if (random[i] == random[j]){
							random[i] = Constants.RANDOM.nextInt(28);
						}
					}
				}
				
				Point first = new Point(122, 136);
				for (int k = 0; k < 2; k++){
					int row = random[k]/7,
						spot = random[k] - (row*7);
					Point L = new Point(first.x + spot * 50, first.y + row * 50);
					Mouse.move(L);
					Mouse.click(1);
					sleep(500, 1000);
				}
			}
		}
	}
	
	private void defineSpot(){
		for (int i = 0; i < locations.length; i++){
			if (locations[i].equals(location)){
				spotNumber = i;
				break;
			}
		}
	}
	
	private void toNetSpot(){
		toPos(netSpots[spotNumber]);
		waitUntilNotMoving();
	}
	
	private boolean grabNet(){
		ScreenModel[] net = ScreenModels.find(fishNet);
		if (net.length > 0){
			Point P = new Point(net[0].base_point.x + 3 - Constants.RANDOM.nextInt(6), net[0].base_point.y + 3 - Constants.RANDOM.nextInt(6));
			Mouse.move(P);
			if (Timing.waitUptext("fishing net", 1000)){
				Mouse.click(1);
				waitUntilNotMoving();

				// Check if we grabbed
				if (GameTab.getOpen() != TABS.INVENTORY){
					GameTab.open(TABS.INVENTORY);
					sleep(500, 1000);
				}
				
				if (Inventory.find(fishNetInv).length > 0){
					return true;
				}
			}else{
				grabNet();
			}
		}
		
		return false;
	}
	
	private void toFishSpot(){
		toPos(fishSpots[spotNumber]);
		waitUntilNotMoving();
	}
	
	private boolean fishFish(){
		ScreenModel[] fishSpot = ScreenModels.find(fishSpotID);
		if (fishSpot.length > 0){
			Point P = new Point(fishSpot[0].base_point.x + 3 - Constants.RANDOM.nextInt(6), fishSpot[0].base_point.y + 3 - Constants.RANDOM.nextInt(6));
			Mouse.move(P);
			if (Timing.waitUptext("fishing spot", 1000)){
				Mouse.click(1);
				waitUntilNotMoving();
				
				while(Inventory.find(fish).length < 1){
					sleep(100, 250);
					
					switch(Constants.RANDOM.nextInt(100)){
						case 0: fishFish();
					}
				}
				return true;
			}
		}
		
		return false;
	}
	
	private void toUncookSpot(){
		toPos(uncookSpot);
		waitUntilNotMoving();
	}
	
	private boolean uncookFish(){
		ScreenModel[] pot = ScreenModels.find(uncookingPot);
		if (pot.length > 0){
			InventoryItem[] inv = Inventory.find(fish);
			if (inv.length > 0){
				Point F = new Point(inv[0].x + 15 + 3 - Constants.RANDOM.nextInt(6), inv[0].y + 15 + 3 - Constants.RANDOM.nextInt(6));
				Mouse.move(F);
				Mouse.click(1);
			}else{
				toFishSpot();
				fishFish();
				toUncookSpot();
				uncookFish();
			}
			
			if (Timing.waitUptext("Use Fish", 1000)){
				Point P = new Point(pot[0].base_point.x + 3 - Constants.RANDOM.nextInt(6), pot[0].base_point.y + 3 - Constants.RANDOM.nextInt(6));
				Mouse.move(P);
				Mouse.click(1);
				waitUntilNotMoving();
				sleep(2000, 3000);
				return true;
			}
		}

		return false;
	}
	
	private boolean giveFish(){
		ScreenModel[] bob = ScreenModels.find(evilBob);
		if (bob.length > 0){
			InventoryItem[] inv = Inventory.find(fish);
			if (inv.length > 0){
				Point F = new Point(inv[0].x + 15 + 3 - Constants.RANDOM.nextInt(6), inv[0].y + 15 + 3 - Constants.RANDOM.nextInt(6));
				Mouse.move(F);
				Mouse.click(1);
			}
			
			if (Timing.waitUptext("Use Raw", 1000)){
				Point P = new Point(bob[0].base_point.x + 3 - Constants.RANDOM.nextInt(6), bob[0].base_point.y + 3 - Constants.RANDOM.nextInt(6));
				Mouse.move(P);
				Mouse.click(1);
				waitUntilNotMoving();
				return true;
			}
		}
		
		return false;
	}
	
	private void finish(){
		NPCChat.clickContinue(true);
		sleep(500, 1000);
		NPCChat.clickContinue(true);
		
		ScreenModel[] port = ScreenModels.find(portal);
		if (port.length > 0){
			Point P = new Point(port[0].base_point.x + 3 - Constants.RANDOM.nextInt(6), port[0].base_point.y + 3 - Constants.RANDOM.nextInt(6));
			Mouse.move(P);
			if (Timing.waitUptext("Enter", 1000)){
				Mouse.click(1);
				waitUntilNotMoving();
			}
		}
	}

	@Override
    public void run() {
		
		println(TPS.getRelativePosition());

		// START OF MAINLOOP
    	if (servantTalk()){
	    	if (fullInv()){
	    		bankItem();
	    	}
	    	defineSpot();
	    	toNetSpot();
	    	if (grabNet()){
	    		toFishSpot();
	    		if (fishFish()){
	    			toUncookSpot();
	    			if (uncookFish()){
	    				if (giveFish()){
	    					finish();
	    				}
	    			}
	    		}
	    	}
    	}
    	// END OF MAINLOOP
	
	}
}