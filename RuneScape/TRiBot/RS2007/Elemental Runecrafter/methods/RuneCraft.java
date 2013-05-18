package scripts.jjsrunecrafter.methods;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;

import scripts.jjsrunecrafter.types.Altars;
import scripts.jjsrunecrafter.types.Pouches;
import scripts.jjsrunecrafter.types.Runes;
import scripts.jjsrunecrafter.types.Essences;


public class RuneCraft {

	private Runes chosenRunes;
	private Altars chosenAltar;
	private Pouches[] pouches;
	private boolean usingTiara;
	
	public RuneCraft(Runes runes, Altars altar, Pouches[] pouches, boolean usingTiara){
		this.chosenRunes = runes;
		this.chosenAltar = altar;
		this.pouches = pouches;
		this.usingTiara = usingTiara;
	}
	
	public int getCraftedRunes(){
		RSItem[] runes = Inventory.find(chosenRunes.getRunesID());
		if(runes != null && runes.length > 0){
			return runes[0].getStack();
		}
		return 0;
	}
	
	public boolean emptyPouches(){
		if(pouches != null && pouches.length > 0){
			for(Pouches pouch : pouches){
				if(28-Inventory.getAll().length < pouch.getSize()){
					craftRunes(false);
				}
				
				RSItem[] invPouch = Inventory.find(pouch.getID());
				if(invPouch != null && invPouch.length > 0 && invPouch[0].click("Empty")){
					System.out.println("Emptied " + pouch + " POUCH");
					General.sleep(100, 200);
				}
			}
		}
		
		return false;
	}
	
	public boolean craftRunes(boolean usingPouches){
		if(usingPouches){
			craftRunes(false);
			emptyPouches();
			craftRunes(false);
		}

		RSObject[] altar = Objects.findNearest(15, chosenAltar.getAltarID());
		if(altar != null && altar.length > 0 && altar[0].isOnScreen() && DynamicClicking.clickRSObject(altar[0], "Craft-rune")){
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(1500, 2500)){
				RSItem[] normalEss = Inventory.find(Essences.NORMAL.getID());
				RSItem[] pureEss = Inventory.find(Essences.PURE.getID());
				if(Player.getAnimation() == -1 && (normalEss == null ||  normalEss.length == 0 && pureEss == null || pureEss.length == 0)){
					return true;
				}
				General.sleep(50, 100);
				if(Player.isMoving() || Player.getAnimation() != -1){
					t = System.currentTimeMillis();
				}
			}
		}

		return false;
	}
	
	public boolean enterRuins(){
		RSObject[] ruins = Objects.find(20, chosenAltar.getRuinsID());
		if(ruins != null && ruins.length > 0 && ruins[0].isOnScreen()){
			if(!usingTiara){
				RSItem[] talisman = Inventory.find(chosenRunes.getTalismanID());
				if(talisman != null && talisman.length > 0){
					talisman[0].click("Use");
				}
			}
			
			if(DynamicClicking.clickRSObject(ruins[0], 1)){
				Mouse.click(1);
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < General.random(1500, 2500)){
					ruins = Objects.find(20, chosenAltar.getRuinsID());
					if(ruins == null || ruins.length == 0){
						return true;
					}
					General.sleep(50, 100);
					if(Player.isMoving() || Player.getAnimation() != -1){
						t = System.currentTimeMillis();
					}
				}
			}
		}
		return false;
	}
	
	public boolean exitPortal(){
		RSObject[] portal = Objects.findNearest(20, chosenAltar.getPortalID());
		if(portal != null && portal.length > 0 && portal[0].isOnScreen() && DynamicClicking.clickRSObject(portal[0], "Use")){
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(1500, 2500)){
				portal = Objects.findNearest(20, chosenAltar.getPortalID());
				if(portal == null || portal.length == 0){
					return true;
				}
				General.sleep(50, 100);
				if(Player.isMoving() || Player.getAnimation() != -1){
					t = System.currentTimeMillis();
				}
			}
		}
		return false;
	}
	
}
