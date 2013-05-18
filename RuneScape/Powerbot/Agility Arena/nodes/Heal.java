package org.obduro.agilityarena.nodes;

import org.obduro.agilityarena.JJsAgilityArena;
import org.obduro.agilityarena.utils.General;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;

public class Heal extends Node {

	@Override
	public boolean activate() {
		System.out.println("Eating percentage: " + General.healPercentage);
		return Players.getLocal().getHealthPercent() <= General.healPercentage;
	}
	
	// Attempt sto eat food
	private boolean eatFood(){
		Item[] items = Inventory.getItems();
		if(items != null && items.length > 0){
			for(Item item : items){
				if(item != null){
					String[] actions = item.getWidgetChild().getActions();
					if(actions != null && actions.length > 0){
						for(String action : actions){
							if(action != null && action.contains("Eat")){
								if(item.getWidgetChild().interact("Eat")){
									Task.sleep(1000, 2000);
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public void execute() {
		System.out.println("Heal node -> activated");

		if(!eatFood()){
			System.out.println("Out of food!");
			if(General.bankingEnabled){
				System.out.println("Banking enabled, trying to get food!");
			}else{
				System.out.println("Banking disabled, logging out!");
				Game.logout(true);
				JJsAgilityArena.forceStopScript();
			}
		}
	}
}