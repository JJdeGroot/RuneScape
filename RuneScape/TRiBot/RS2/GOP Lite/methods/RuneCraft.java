package scripts.jjsgoplite.methods;

import org.tribot.api.DynamicClicking;
import org.tribot.api.Inventory;
import org.tribot.api.NPCChat;
import org.tribot.api.Timing;

import scripts.jjsgoplite.utils.General;
import scripts.jjsgoplite.utils.Task;

public class RuneCraft {
	private static final int[] essenceIDs = {1030831, 1030831};
	private static final long[] altarIDs = {1874917287L, 75657907L, 315245776L, 2700071285L, 
											399779985L, 2273333984L,875657907L, 2273333984L, 
	     								    2188457835L, 1115721844L};
	
	private static final String altarString = "Craft-rune",
								yesString = "Yes";
								
	// Clicks the altar
	private static boolean clickAltar(){
		return DynamicClicking.clickScreenModel(General.getScreenModel(altarIDs), altarString);
	}
	
	// Returns true if we should craft runes
	public static boolean needToCraft(){
		if(Inventory.find(essenceIDs).length > 0){
			return Portal.isOpen() || Portal.waitForPortalText();
		}
		return false;
	}
	
	// Crafts runes if we have essence
	public static boolean craftRunes(){
		// Walking to altar
		if(!Walking.atAltar()){
			Walking.walkToAltar();
		}
		
		// Interacting with altar
		if(clickAltar()){
			General.waitUntilNotMoving();
			long t = System.currentTimeMillis();
			
			while(Timing.timeFromMark(t) < 3000){
				String[] options = NPCChat.getOptions();
				for(String option : options){
					if(option.contains(yesString)){
						NPCChat.selectOption(yesString, true);
						Task.sleep(1500, 2500);
						return true;
					}
				}
				Task.sleep(100, 200);
			}
		}

		return false;		
	}
}
