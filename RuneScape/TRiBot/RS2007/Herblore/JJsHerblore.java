package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

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
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Herblore", name = "JJ's Herblore", description = "Place the two items you need in spot #1 and spot #2 of your inventory. If you want to make unfinished potions, place vial of water in spot #1 or spot #2 and the herb in the other spot. Same story for other potions: place the unfinished potion in spot #1 or spot #2 and the ingredient in the other spot")
public class JJsHerblore extends Script implements Painting {

	private final long startTime = System.currentTimeMillis();
	private final int potionInterface = 309;
	private final int makeIndex = 6;
	private final int mixAnimation = 393;
	private final int startXp = Skills.getXP("Herblore");
	
	private int item1 = 97;
	private int item2 = 1975;
	private int mixed = 0;
	private int currentXp = startXp;
	
	private enum State {
		DEPOSITING, WITHDRAWING, CLOSING_BANK, CLICKING_ITEMS, 
		CLICK_MIX_INTERFACE, MIXING, OPENING_BANK;
	}
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
			if(!deposited.contains(item.getID())){
				int count = Inventory.getCount(item.getID());
				String option = "Store " + (count > 10 ? "All" : count > 5 ? 10 : count == 1 ? 1 : 5);
				if(item.click(option)){
					System.out.println("Added id #" + item.getID());
					deposited.add(item.getID());
					waitInventory(false, General.random(1500, 2000));
				}
			}
		}
		
		return Inventory.getAll().length == 0;
	}

	/** Checks if we are mixing */
	private boolean areMixing(){
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
		RSItem[] items1 = Banking.find(item1);
		RSItem[] items2 = Banking.find(item2);
		if(items1.length > 0 && items2.length > 0){
			if(General.random(0, 1) == 1){
				if(Banking.withdrawItem(items1[0], 14) && items2[0].click("Withdraw All")){
					return waitInventory(true, General.random(1500, 2000));
				}
			}else if(Banking.withdrawItem(items2[0], 14) && items1[0].click("Withdraw All")){
				return waitInventory(true, General.random(1500, 2000));
			}
		}
		return false;
	}
	
	/** Checks if we can mix ingredients */
	private boolean canMake(){
		return Inventory.getCount(item1) > 0 && Inventory.getCount(item2) > 0;
	}
	
	/** Clicks item1 and then item2 */
	private boolean useItems(){
		RSItem[] items1 = Inventory.find(item1);
		RSItem[] items2 = Inventory.find(item2);
		if(items1.length > 0 && items2.length > 0){
			int r1 = General.random(0, items1.length-1);
			int r2 = General.random(0, items2.length-1);
			return items1[r1].click() && items2[r2].click() && waitInterface(General.random(1500, 2500));
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
	private boolean makePotions(){
		RSInterfaceChild makeChild = Interfaces.get(potionInterface, makeIndex);
		if(makeChild != null && makeChild.click("Make All")){
			sleep(1000, 2000);
			return true;
		}
		return false;
	}

	/// MAINLOOP \\\
	@Override
	public void onPaint(Graphics g) {
		long timeRan = System.currentTimeMillis() - startTime;
		double multiplier = timeRan / 3600000D;
		int xpGained = currentXp - startXp;
		int xpHr = (int) (xpGained / multiplier);
		int mixedHr = (int) (mixed / multiplier);
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's Herblore", 10, 100);
		g.drawString("Running for: " + Timing.msToString(timeRan), 10, 120);
		g.drawString("State: " + state, 10, 140);
		
		g.drawString("Gained " + xpGained + " herblore xp (" + xpHr + "/h)", 10, 170);
		g.drawString("Mixed " + mixed + " potions (" + mixedHr + "/h)", 10, 190);
	}
	
	/** Returns the current state */
	private State getState(){
		if(canMake()){
			if(Banking.isBankScreenOpen()){
				return State.CLOSING_BANK;
			}else if(haveInterface()){
				return State.CLICK_MIX_INTERFACE;
			}else if(areMixing()){
				return State.MIXING;
			}else{
				return State.CLICKING_ITEMS;
			}
		}else{
			if(!Banking.isBankScreenOpen()){
				return State.OPENING_BANK;
			}else if(Inventory.getAll().length > 0){
				return State.DEPOSITING;
			}else{
				return State.WITHDRAWING;
			}
		}
	}

	@Override
	public void run() {
		Mouse.setSpeed(General.random(150, 170));
		
		// Getting required item ID's
		RSItem[] items = Inventory.getAll();
		if(items.length < 2){
			println("Place the two required items in your inventory!");
			stopScript();
		}else{
			item1 = items[0].getID();
			item2 = items[1].getID();
		}
		
		// Dynamic loop
		while(true){
			state = getState();
			currentXp = Skills.getXP("Herblore");
			
			switch(state){
				case CLICKING_ITEMS:
					useItems();
					break;
				case CLICK_MIX_INTERFACE:
					makePotions();
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
					Banking.openBankBanker();
					break;
				case WITHDRAWING:
					withdrawSupplies();
					mixed+=14;
					break;
			}
			
			sleep(20, 40);
		}
	}

}
