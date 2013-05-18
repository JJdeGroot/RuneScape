package org.obduro.mole.nodes;

import org.obduro.mole.utils.LocalPlayer;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;

public class Pray extends Node {

	@Override
	public boolean activate() {
		System.out.println("Pray called");
		return LocalPlayer.lowPrayer();
	}

	@Override
	public void execute() {
		System.out.println("Going to restore prayer");
		Item[] allItems = Inventory.getItems();
		for(Item item : allItems){
			String name = item.getName();
			if(name.contains("Prayer") || name.contains("Restore")){
				System.out.println("Can restore prayer");
				if(item.getWidgetChild().interact("Drink")){
					Task.sleep(1000, 1500);
					return;
				}
			}
		}
	}
}
