package org.obduro.mole.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;

public class Heal extends Node {

	@Override
	public boolean activate() {
		System.out.println("Heal called");
		return Players.getLocal().getHealthPercent() <= 25;
	}

	@Override
	public void execute() {
		System.out.println("Going to heal");
		Item[] allItems = Inventory.getItems();
		for(Item item : allItems){
			String[] options = item.getWidgetChild().getActions();
			for(String s : options){
				if(s != null){
					if(s.contains("Eat")){
						System.out.println("Can eat food");
						if(item.getWidgetChild().interact("Eat")){
							Task.sleep(1000, 1500);
							return;
						}
					}
				}
			}
		}
	}
}