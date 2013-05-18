package scripts;

import org.tribot.api.Inventory;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;



@ScriptManifest(authors = { "J J" }, category = "Hunter", name = "JJ's Bird Hunter")
public class JJsBirdHunter extends Script{
	final int snareID = 183438;
	final long caughtTrapID = 2362996405L, normalTrapID = 768133178L, fallenTrapID = 4247570198L;
	
	private void setupSnare(){
		InventoryItem[] snares = Inventory.find(snareID);
		if (snares.length > 0){
			snares[0].click("Lay");
		}
	}
	
	private void checkTraps(){
		ScreenModel[] traps = ScreenModels.find(caughtTrapID, normalTrapID);
		if (traps.length > 0){
			for (int i = 0; i < traps.length; i++){
				if (caughtTrapID == traps[i].id){
					println("Caught a bird");
					Mouse.click(traps[i].base_point, 1);
				}else{
					println("Haven't caught yet");
					Mouse.move(traps[i].base_point);
					if (Timing.waitUptext("3 more options", 500)){
						println("Trap is still fine");
					}else{
						println("Trap has failed");
						Mouse.click(traps[i].base_point, 1);
					}
				}
			}
		}else{
			ScreenModel[] fallenTraps = ScreenModels.find(fallenTrapID);
			if (fallenTraps.length > 0){
				println("Trap has fallen, time to set it back up");
				Mouse.click(fallenTraps[0].base_point, 3);
				Timing.waitChooseOption("Lay", 500);
			}else{
				println("Time to set up a trap");
				setupSnare();
			}
			
		}
		
	}
	
	@Override
    public void run() {
		//setupSnare();
		checkTraps();
    }
	

}
