package org.obduro.barbassault.game;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Call extends Node {

	private final WidgetChild healerCall = Widgets.get(488, 36),
							  collectorCall = Widgets.get(486, 6),
							  attackerCall = Widgets.get(485, 6);
	private final WidgetChild[] callWidgets = {healerCall, collectorCall,
											   attackerCall};
	
	private final int[] hornIDs = {10526, 10527, 10528, 10529, 10530, // HEALER
							       10516, 10517, 10518, 10519, 10520, // ATTACKER
							       10538, // DEFENDER
								   10560}; // COLLECTOR
	private String text;
	
	
	@Override
	public boolean activate() {
		System.out.println("Call node trying to activate");
		
		for(WidgetChild wc : callWidgets){
			if(wc != null && wc.validate()){
				System.out.println("Call widget validated");
				if(wc.getTextColor() == 16316664){
					System.out.println("We need to call!!");
					String text = wc.getText().toLowerCase();
					this.text = text;
					System.out.println("Call text: " + this.text);
					return true;
				}
			}
		}
	
		return false;
	}
	
	public boolean call(){
		// Opening inventory
		if(!Tabs.INVENTORY.isOpen()){
			if(Tabs.INVENTORY.open()){
				Timer timer = new Timer(1500);
				while(timer.isRunning() && !Tabs.INVENTORY.isOpen()){
					Task.sleep(50, 150);
				}
			}
		}

		// Calling the right option
		Item horn = Inventory.getItem(hornIDs);
		if(horn != null){
			System.out.println("Horn in inv!");
			
			String[] splitted = text.toLowerCase().split("\\s+");
			//String option = splitted[splitted.length-1];
			String option = splitted[0];
		
			System.out.println("Option to select: " + option);
			
			if(horn.getWidgetChild().interact(option)){
				Task.sleep(500, 750);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void execute() {
		System.out.println("Call --> Execute!");
		call();
	}
	
}
