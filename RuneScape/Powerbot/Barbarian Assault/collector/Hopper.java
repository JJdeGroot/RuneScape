package org.obduro.barbassault.collector;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class Hopper extends Node {
	
	private final int hopperID = 20267; // Scene entity

	@Override
	public boolean activate() {
		return Inventory.isFull();
	}

	@Override
	public void execute() {
		System.out.println("Inv is full, hopper activated");
		SceneObject hopper = SceneEntities.getNearest(hopperID);
		if(hopper != null){
			if(!hopper.isOnScreen()){
				System.out.println("Need to walk!");
				hopper.getLocation().clickOnMap();
			}else{
				System.out.println("Going to load!");
				hopper.interact("Load");
				Task.sleep(2000);
			}
		}else{
			System.out.println("Hopper = null!");
		}
		
	}
	
	
	

}
