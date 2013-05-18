package org.obduro.agilityarena.nodes;

import java.util.ArrayList;

import org.obduro.agilityarena.utils.General;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.wrappers.node.Item;

public class Restore extends Node {
	private final int[] agilityPotionIDs = {3032, 3034, 3036, 3038};
	private final int[] superRestoreIDs = {3024, 3026, 3028, 3031};
	private final int[] summerPieIDs = {7218, 7220};
	private final int[] agilFlaskIDs = {23411, 23413, 23415, 23417, 23419, 23421};
	private final int[] restoreFlaskIDs = {23399, 23401, 23403, 23405, 23407, 23409};

	@Override
	public boolean activate() {
		return Skills.getTopLevels()[16] < 40;
	}

	private boolean restoreAgility(){
		ArrayList<Integer> potionList = new ArrayList<Integer>();
		for(int i : agilityPotionIDs)
			potionList.add(i);
		for(int i : superRestoreIDs)
			potionList.add(i);
		for(int i : agilFlaskIDs)
			potionList.add(i);
		for(int i : restoreFlaskIDs)
			potionList.add(i);
		
		for(int i : potionList){
			Item potion = Inventory.getItem(i);
			if(potion != null && potion.getWidgetChild().interact("Drink")){
				Task.sleep(500, 1000);
				return true;
			}
		}
		
		Item pie = Inventory.getItem(summerPieIDs);
		if(pie != null && pie.getWidgetChild().interact("Eat")){
			Task.sleep(500, 1000);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void execute() {
		System.out.println("Restore node -> activated");
		
		if(!restoreAgility()){
			System.out.println("You have no items to restore your agility, resting.");
			if(!General.isResting()){
				System.out.println("Not resting yet, going to rest!");
				General.setRest();
			}else{
				System.out.println("Agility level is still lower than 40, resting");
				Task.sleep(2000, 3000);
			}
		}
	}
	
}
