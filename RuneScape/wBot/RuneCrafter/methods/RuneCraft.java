package org.obduro.runecrafter.methods;

import org.obduro.runecrafter.Timer;
import org.obduro.runecrafter.types.Altar;
import org.obduro.runecrafter.types.Pouch;
import org.obduro.runecrafter.types.Essence;

import bot.script.methods.Inventory;
import bot.script.methods.Methods;
import bot.script.methods.Objects;
import bot.script.methods.Players;
import bot.script.util.Random;
import bot.script.wrappers.GameObject;
import bot.script.wrappers.Item;

public class RuneCraft {

	private Altar chosenAltar;
	private Pouch[] pouches;
	private boolean usingTiara;
	
	public RuneCraft(Altar altar, Pouch[] pouches, boolean usingTiara){
		this.chosenAltar = altar;
		this.pouches = pouches;
		this.usingTiara = usingTiara;
	}
	
	public boolean emptyPouches(){
		if(pouches != null && pouches.length > 0){
			for(Pouch p : pouches){
				if(28-Inventory.getItems().length < p.getSize()){
					craftRunes(false);
				}
				
				Item pouch = Inventory.getItem(p.getID());
				if(pouch != null && pouch.interact("Empty")){
					System.out.println("Emptied " + p + " POUCH");
					Methods.sleep(100, 200);
				}
			}
		}
		
		return false;
	}
	
	public boolean craftRunes(boolean usingPouches){
		if(usingPouches){
			craftRunes(false);
			emptyPouches();
		}
		
		GameObject altar = Objects.getNearest(chosenAltar.getAltarID());
		if(altar != null && altar.isVisible() && altar.interact("Craft-rune")){
			Timer timer = new Timer(Random.nextInt(1500, 2500));
			while(timer.isRunning()){
				if(Players.getLocal().getAnimation() == -1 && Inventory.getItem(Essence.NORMAL.getID()) == null && Inventory.getItem(Essence.PURE.getID()) == null){
					return true;
				}
				Methods.sleep(50, 100);
				if(Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1){
					timer.reset();
				}
			}
		}

		return false;
	}
	
	public boolean enterRuins(){
		GameObject ruins = Objects.getNearest(chosenAltar.getRuinsID());
		if(ruins != null && ruins.isVisible()){
			if(!usingTiara){
				Item talisman = Inventory.getItem(chosenAltar.getTalismanID());
				if(talisman != null){
					talisman.interact("Use");
				}
			}
			
			if(ruins.interact("Mysterious ruins")){
				Timer timer = new Timer(Random.nextInt(1500, 2500));
				while(timer.isRunning()){
					if(Objects.getNearest(chosenAltar.getRuinsID()) == null){
						return true;
					}
					Methods.sleep(50, 100);
					if(Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1){
						timer.reset();
					}
				}
			}
		}
		return false;
	}
	
	public boolean exitPortal(){
		GameObject portal = Objects.getNearest(chosenAltar.getPortalID());
		if(portal != null && portal.isVisible() && portal.interact("Use")){
			Timer timer = new Timer(Random.nextInt(1500, 2500));
			while(timer.isRunning()){
				if(Objects.getNearest(chosenAltar.getRuinsID()) != null){
					return true;
				}
				Methods.sleep(50, 100);
				if(Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1){
					timer.reset();
				}
			}
		}
		return false;
	}
	
}
