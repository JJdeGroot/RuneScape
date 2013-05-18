package org.obduro.barbassault.collector;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Loot extends Node {

	@Override
	public boolean activate() {
		System.out.println("Loot called");
		return !Inventory.isFull();
	}
	
	public enum Egg{
		GREEN(10531, "Take Green"), RED(10532, "Take Red"), BLUE(10533, "Take Blue");
		
		final int id;
		
		private Egg(int id, String option){
			this.id = id;
		}
		
		public boolean onScreen(){
			GroundItem egg = GroundItems.getNearest(id);
			return egg != null && egg.isOnScreen();
		}
		
		public boolean navigate(){
			GroundItem[] eggs = GroundItems.getLoaded(id);
			if(eggs != null && eggs.length > 0){
				for(GroundItem egg : eggs){
					if(egg.getLocation().clickOnMap()){
						System.out.println("Clicked location on map");
						return true;
					}
				}
			}
			return false;
		}
		
		public boolean loot(){
			System.out.println("Id to loot: " + id);
			
			if(!onScreen()){
				navigate();
			}
			
			System.out.println("Where is attacker");
			whereIsAttacker();
			
			GroundItem egg = GroundItems.getNearest(id);
			System.out.println("Egg: " + egg);
			if(egg != null && egg.isOnScreen()){
				System.out.println("Egg is not null");
				if(egg.interact("Take", egg.getGroundItem().getName())){
					int invCount = Inventory.getCount();
					Timer timer = new Timer(500);
					while(timer.isRunning() && (Players.getLocal().isMoving() || Inventory.getCount() == invCount)){
						if(Players.getLocal().isMoving()){
							timer.reset();
						}
						Task.sleep(50, 100);
					}
					System.out.println("LOOTED?!");
					return Inventory.getCount() > invCount;
				}
			}
			
			return false;
		}
		
		public boolean whereIsAttacker(){
			
			/*
			Player[] players = Players.getLoaded();
			for(Player player : players){
				String name = player.getName();
				System.out.println("Player name: " + name);
				int[] appearance = player.getAppearance();
			}
			
			int capeID = Equipment.getAppearanceId(Slot.CAPE);
			System.out.println("cape ID: " + capeID);
			
			int[] myAppearance = Equipment.getAppearanceIds();
			for(int i = 0; i < myAppearance.length; i++){
				System.out.println("My Appearance #" + i + ": " + myAppearance[i]);
			}
			*/
			
			//Item myItem = Equipment.getItem(15453);
			//System.out.println("MyItem: " + myItem.getName());
			
			
			
			
			// Appearance #1 = cape
			
			return false;
		}
		
	}

	@Override
	public void execute() {
		System.out.println("Loot executed!");
		
		WidgetChild wc = Widgets.get(486, 4);
		if(wc.validate()){
			String txt = wc.getText().toLowerCase();
			System.out.println("Collector to loot text: " + txt);
			
			if(txt.contains("red")){
				System.out.println("Need to loot red eggs");
				Egg.RED.loot();
			}else if(txt.contains("green")){
				System.out.println("Need to loot green eggs");
				Egg.GREEN.loot();
			}else if(txt.contains("blue")){
				System.out.println("Need to loot blue eggs");
				Egg.BLUE.loot();
			}
		}
		
	}
	
	
}
