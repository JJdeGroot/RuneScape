package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.tribot.api2007.Game;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.EnumScript;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.jjsherblore.Method;
import scripts.jjsherblore.State;
import scripts.jjsherblore.GUI;

@ScriptManifest(authors = { "J J" }, category = "Herblore", name = "JJ's Herblore", description = "Have the items you need visible in the bank.<br>Choose your method and item id(s) in the GUI and hit run.<br><br>Have a pestle and mortar in your inventory if grinding")
/** 
 * JJ's Herblore features
 * Mixing items
 * 		Vial of water + herb -> Unfinished
 * 		Unfinished potion + secondary -> Finished
 * Grinding items
 * Cleaning herbs
 */
public class JJsHerblore extends EnumScript<State> implements Painting {

	private final long startTime = System.currentTimeMillis();
	private final Rectangle inventory_area = new Rectangle(525, 206, 757-525, 461-206);
	private final int mortarID = 233;
	private final int potionInterface = 309;
	private final int makeIndex = 6;
	private final int mixAnimation = 393;
	private final int startXp = Skills.getXP("Herblore");
	
	private Method method;
	private int item1ID;
	private int item2ID;
	private int itemsDone = 0;
	private int currentXp = startXp;

	private GUI gui;
	private boolean guiInitialized = false;
	private State state;
	
	/// GENERAL \\\
	/** Waits for something to happen in the inventory */
	private boolean waitInventory(boolean increase, int ms){
		long t = System.currentTimeMillis();
		int startCount = Inventory.getAll().length;
		while(Timing.timeFromMark(t) < ms){
			int count = Inventory.getAll().length;
			if(increase ? count > startCount : count < startCount){
				return true;
			}
			sleep(20, 80);
		}
		return false;
	}
	
	/** Deposits all items into the bank */
	private boolean depositAll(){
		ArrayList<Integer> deposited = new ArrayList<Integer>();
		RSItem[] items = Inventory.getAll();
		for(RSItem item : items){
			// Check if we are grinding which means don't deposit the pestle & mortar
			if(method.equals(Method.GRINDING) && item.getID() == mortarID){
				continue;
			}
			// Check if we haven't deposited this item yet
			if(!deposited.contains(item.getID())){
				int count = Inventory.getCount(item.getID());
				String option = "Store " + (count > 10 ? "All" : count > 5 ? 10 : count == 1 ? 1 : 5);
				if(item.click(option)){
					//System.out.println("Added id #" + item.getID());
					deposited.add(item.getID());
					waitInventory(false, General.random(1500, 2000));
				}
			}
		}
		
		return Inventory.getAll().length == 0;
	}

	/** Checks if we are mixing or grinding */
	private boolean areAnimating(){
		long t = System.currentTimeMillis();
		int startCount = Inventory.getAll().length;
		while(Timing.timeFromMark(t) < General.random(7500, 1250)){
			if(Player.getAnimation() == mixAnimation || Inventory.getAll().length < startCount){
				return true;
			}
			sleep(20, 80);
		}
		return false;
	}
	
	/// UNFINISHED POTIONS \\\
	/** Withdraws the items required to mix */
	private boolean withdrawSupplies(){
		switch(method){
			case GRINDING:
				itemsDone+=27;
				RSItem[] items = Banking.find(item1ID);
				return items.length > 0 && items[0].click("Withdraw All") && waitInventory(true, General.random(1500, 2000));
				
			case MIXING:
				itemsDone+=14;
				RSItem[] items1 = Banking.find(item1ID);
				RSItem[] items2 = Banking.find(item2ID);
				if(items1.length > 0 && items2.length > 0){
					if(General.random(0, 1) == 1){
						return Banking.withdrawItem(items1[0], 14) && items2[0].click("Withdraw All") && waitInventory(true, General.random(1500, 2000));
					}else{
						return Banking.withdrawItem(items2[0], 14) && items1[0].click("Withdraw All") && waitInventory(true, General.random(1500, 2000));
					}
				}
				break;
				
			case CLEANING:
				itemsDone+=28;
				RSItem[] herbs = Banking.find(item1ID);
				return herbs.length > 0 && herbs[0].click("Withdraw All") && waitInventory(true, General.random(1500, 2000));

		}
		return false;
	}
	
	/** Checks if we can mix ingredients */
	private boolean canMake(){
		switch(method){
			case GRINDING:
				return Inventory.getCount(item1ID) > 0 && Inventory.getCount(mortarID) > 0;
			case MIXING:
				return Inventory.getCount(item1ID) > 0 && Inventory.getCount(item2ID) > 0;	
			case CLEANING:
				return Inventory.getCount(item1ID) > 0;
		}
		return false;
	}
	
	/** If mixing: clicks item1 and then item2 else uses pestle and mortar on item1 */
	private boolean useItems(){
		switch(method){
			case GRINDING:
				RSItem[] mortars = Inventory.find(mortarID);
				RSItem[] items = Inventory.find(item1ID);
			
				if(mortars.length > 0 && items.length > 0){
					if(items.length != 27){
						Mouse.setSpeed(General.random(500, 600));
					}
					// Check if we need to fix a wrong click
					String uptext = Game.getUptext();
					if(uptext != null && uptext.startsWith("Use ")){
						mortars[0].click();
						waitInvChange();
					}
					return mortars[0].click() && items[items.length-1].click();
				}
				break;
				
			case MIXING:
				RSItem[] items1 = Inventory.find(item1ID);
				RSItem[] items2 = Inventory.find(item2ID);
				if(items1.length > 0 && items2.length > 0){
					int r1 = General.random(0, items1.length-1);
					int r2 = General.random(0, items2.length-1);
					if(General.random(0, 1) == 1){
						return items1[r1].click() && items2[r2].click() && waitInterface(General.random(1500, 2500));
					}else{
						return items2[r2].click() && items1[r1].click() && waitInterface(General.random(1500, 2500));
					}
				}
				break;
				
			case CLEANING:
				RSItem[] herbs = Inventory.find(item1ID);
				if(herbs.length > 0){
					for(int i = 0; i < herbs.length; i++){
						if(i > 0 && Mouse.getSpeed() < 500){
							Mouse.setSpeed(General.random(500, 600));
						}
						herbs[i].click();
					}
				}
				
				openBank();
				break;
		}

		return false;
	}
	
	/** Waits for one or more inventory items to change */
	private boolean waitInvChange(){
		RSItem[] startItems = Inventory.getAll();
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < General.random(600, 1200)){
			RSItem[] items = Inventory.getAll();
			if(startItems.length == 0 || items.length == 0 || startItems.length != items.length){
				return true;
			}
			for(int i = 0; i < startItems.length; i++){
				if(startItems[i].getID() != items[i].getID()){
					return true;
				}
			}
			sleep(20, 80);
		}
		return false;
	}
	
	/** Checks if the "make potion" interface is open */
	private boolean haveInterface(){
		return Interfaces.get(potionInterface, makeIndex) != null;
	}
	
	/** Waits a certain amount of time for the interface to open */
	private boolean waitInterface(int ms){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < ms){
			if(haveInterface()){
				return true;
			}
			sleep(20, 80);
		}
		return false;
	}
	
	/** Clicks the "Make All" option in the interface */
	private boolean makeAll(){
		RSInterfaceChild makeChild = Interfaces.get(potionInterface, makeIndex);
		if(makeChild != null && makeChild.click("Make All")){
			sleep(1000, 2000);
			return true;
		}
		return false;
	}
	
	/** Checks if we need to deposit any items */
	private boolean needDeposit(){
		RSItem[] items = Inventory.getAll();
		for(RSItem item : items){
			int id = item.getID();
			if(id != mortarID && id != item1ID && id != item2ID){
				return true;
			}
		}
		return false;
	}
	
	/** Opens the bank */
	private boolean openBank(){
		// Fix if we misclicked
		String uptext = Game.getUptext();
		if(uptext != null && uptext.startsWith("Use ")){
			Point p = new Point(General.random(inventory_area.x, inventory_area.width+inventory_area.x), General.random(inventory_area.y, inventory_area.height+inventory_area.y));
			Mouse.click(p, 1);
		}
		
		// Open the bank
		if(General.random(0, 1) == 1){
			return Banking.openBankBanker() || Banking.openBankBooth();
		}else{
			return Banking.openBankBooth() || Banking.openBankBanker();
		}
	}

	/// MAINLOOP \\\
	@Override
	public void onPaint(Graphics g) {
		long timeRan = System.currentTimeMillis() - startTime;
		double multiplier = timeRan / 3600000D;
		int xpGained = currentXp - startXp;
		int xpHr = (int) (xpGained / multiplier);
		int madeHr = (int) (itemsDone / multiplier);
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's Herblore", 10, 100);
		g.drawString("Running for: " + Timing.msToString(timeRan), 10, 120);
		g.drawString("State: " + state, 10, 140);
		g.drawString("Gained " + xpGained + " herblore xp (" + xpHr + "/h)", 10, 170);
	
		if(method != null){
			g.drawString("Method: " + method, 10, 220);
			switch(method){
				case GRINDING: 
					g.drawString("Grinded " + itemsDone + " items (" + madeHr + "/h)", 10, 190);
					g.drawString("Item ID: " + item1ID, 10, 240);
					break;
					
				case CLEANING:
					g.drawString("Cleaned " + itemsDone + " herbs (" + madeHr + "/h)", 10, 190);
					g.drawString("Herb ID: " + item1ID, 10, 240);
					break;
					
				case MIXING:
					g.drawString("Mixed " + itemsDone + " potions (" + madeHr + "/h)", 10, 190);
					g.drawString("Item #1 ID: " + item1ID, 10, 240);
					g.drawString("Item #2 ID: " + item2ID, 10, 260);
					break;
			}
		}
	}

	/** Returns the current state */
	private State getState(){
		if(method == null){
			return State.GUI;
		}else if(canMake()){
			if(Banking.isBankScreenOpen()){
				return State.CLOSING_BANK;
			}else{
				switch(method){
					case GRINDING: case CLEANING: 
						// Placeholder for now
						break;
						
					case MIXING:
						if(haveInterface()){
							return State.CLICK_INTERFACE;
						}else if(areAnimating()){
							return State.MIXING;
						}
				}
				return State.CLICKING_ITEMS;
			}
		}else{
			if(!Banking.isBankScreenOpen()){
				return State.OPENING_BANK;
			}else if(needDeposit()){
				return State.DEPOSITING;
			}else{
				return State.WITHDRAWING;
			}
		}
	}

	@Override
	public State getInitialState() {
		// Spawning GUI
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				gui = new GUI();
				guiInitialized = true;
				gui.setVisible(true);
			}
    	});
		
		// Waiting for it to finish
		while(true){
			if(guiInitialized && !gui.isVisible()){
				break;
			}
			sleep(40, 80);
		}
		
		// Finishing
		method = gui.getMethod();
		item1ID = gui.getItem1ID();
		item2ID = gui.getItem2ID();
	
		return getState();
	}

	@Override
	public State handleState(State state) {
		this.state = state;
		currentXp = Skills.getXP("Herblore");
		Mouse.setSpeed(General.random(150, 170));
		
		switch(state){
			case GUI:
				sleep(20, 60);
			case CLICKING_ITEMS:
				useItems();
				break;
			case CLICK_INTERFACE:
				makeAll();
			case CLOSING_BANK:
				Banking.close();
				break;
			case DEPOSITING:
				depositAll();
				break;
			case MIXING:
				sleep(50, 100);
				break;
			case OPENING_BANK:
				openBank();
				break;
			case WITHDRAWING:
				withdrawSupplies();
				break;
		}
		
		sleep(20, 60);
		return getState();
	}

}
