package org.obduro.barbassault.healer;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Poison extends Node {

	private final int[] monsterIDs = {5239, 5241, 5242, 5244, 5245, 5246};
					    
	private final int machineID = 20243,
					  tofuID = 10539,
					  wormsID = 10540,
					  meatID = 10541;
	
	@Override
	public boolean activate() {
		return NPCs.getNearest(monsterIDs) != null;
	}
	
	private String getFoodName(){
		WidgetChild food = Widgets.get(488, 34);
		if(food != null & food.validate()){
			return food.getText();
			
		}
		return null;
	}
	
	private int getFoodID(){
		String name = getFoodName();
		if(name != null){
			String[] splitted = name.toLowerCase().split("\\s+");
			String foodName = splitted[splitted.length-1];
			
			System.out.println("FoodName: " + foodName);
			
			if(foodName.contains("tofu")){
				return tofuID;
			}else if(foodName.contains("worm")){
				return wormsID;
			}else if(foodName.contains("meat")){
				return meatID;
			}
		}

		return -1;
	}
	
	private boolean poisonMonster(){
		NPC monster = NPCs.getNearest(monsterIDs);
		
		if(monster != null && monster.isOnScreen()){
			System.out.println("Monster on screen!");
			int id = getFoodID();
			if(id != -1){
				System.out.println("Food is NOT -1 (ID: " + id + ")");
				Item food = Inventory.getItem(id);
				int invCount = Inventory.getCount();
				if(food.getWidgetChild().click(true)){
					if(monster.click(true)){
						Timer timer = new Timer(1000);
						while(timer.isRunning() && Inventory.getCount() == invCount && monster != null && monster.isOnScreen() && monster.getHealthPercent() > 0){
							if(Players.getLocal().isMoving()){
								timer.reset();
							}
							Task.sleep(100, 200);
						}
					}
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void execute() {
		System.out.println("Poison --> Execute!");
		poisonMonster();
	}
	
	
	
	
	
	
}
