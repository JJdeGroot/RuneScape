package scripts.jjswillowchopper;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;

public class Store {

	private final int store_interface = 300,
					  store_title_index = 76,
					  willowID = 1519;
	
	/**
	 * Finds store customers by name and sorts them by distance
	 * @return RSNPC[] containing customers
	 */
	private RSNPC[] getCustomers(){
		return NPCs.findNearest(NPCs.generateFilterGroup(new Filter<RSNPC>() {
			@Override
			public boolean accept(RSNPC npc) {
				if(npc != null){
					String name = npc.getName();
					return name != null && name.equals("Shop keeper") || name.equals("Shop assitant");
				}
				return false;
			}
		}));
	}
	
	/**
	 * Checks if the General store screen is open
	 * @return True if open
	 */
	public boolean isStoreOpen(){
		RSInterfaceChild titleInterface = Interfaces.get(store_interface, store_title_index);
		return titleInterface != null && "Rimmington General Store".equals(titleInterface.getText());
	}
	
	/**
	 * Waits a certain amount of time for the store screen to open
	 * @param ms amount of time to wait in ms
	 * @return True if store opened within time limit
	 */
	private boolean waitStoreOpen(int ms){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < ms){
			if(isStoreOpen()){
				return true;
			}else if(Player.isMoving()){
				t = System.currentTimeMillis();
			}
			General.sleep(ms/20, ms/10);
		}
		return false;
	}
	
	/**
	 * Attempts to trade the customer
	 * @return True if traded succesfully
	 */
	public boolean tradeCustomer(){
		RSNPC[] customers = getCustomers();
		for(RSNPC customer : customers){
			if(PathFinding.canReach(customer, true)){
				if(!customer.isOnScreen()){
					Walking.walkTo(customer);
					Camera.turnToTile(customer);
				}else{
					RSModel model = customer.getModel();
					if(model != null){
						if(DynamicClicking.clickRSModel(model, "Trade")){
							return waitStoreOpen(General.random(2000, 3000));
						}else{
							Camera.turnToTile(customer);
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Waits a certain amount of time for the right
	 * selling option to appear and click
	 * @param amount inventory willow amount
	 * @param ms time to wait
	 * @return true if clicked the right sell option
	 */
	private boolean waitSellOption(int amount, int ms){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < ms){
			String[] options = ChooseOption.getOptions();
			for(String option : options){
				if(option.contains("Sell willow")){
					int number = amount >= 10 ? 10 : 5;
					if(ChooseOption.select("Sell " + number + " willow")){
						return true;
					}
				}
			}
			General.sleep(ms/20, ms/10);
		}
		return false;
	}
	
	/**
	 * Generates a random optimal index to click
	 * @param length array length to generate for
	 * @return optimal random index
	 */
	private int generateIndex(int length){
		if(length > 20){
			return General.random(20, length-1);
		}else if(length > 10){
			return General.random(10, length-1);
		}else{
			return General.random(0, length-1);
		}
	}
	
	/**
	 * Waits a certain amount of time until we have
	 * no more willow logs
	 * @param startAmount starting amount
	 * @param ms time to wait
	 * @return true if current amount lower than start amount
	 */
	private boolean waitSellWillows(int startAmount, int ms){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < ms){
			if(Inventory.find(willowID).length < startAmount){
				return true;
			}
			General.sleep(ms/20, ms/10);
		}
		return false;
	}

	/**
	 * Sells the willow logs to the general store
	 * @return True if sold all logs
	 */
	public boolean sellWillows(){
		RSItem[] willows = Inventory.find(willowID);
		
		while(isStoreOpen() && willows.length > 0){
			// Check if we are hovering willow logs already
			String uptext = Game.getUptext();
			if(uptext != null && uptext.contains("willow")){
				Mouse.click(3);
				waitSellOption(willows.length, General.random(1500, 2000));
			}else{
				// Selling based on optimal index
				int index = generateIndex(willows.length);
				willows[index].click("Sell 10");
			}
			
			// Wait until we sell, update progress
			waitSellWillows(willows.length, General.random(2000, 3000));
			willows = Inventory.find(willowID);
		}
		
		return willows.length == 0;
	}
	
	
	
	
	
}
