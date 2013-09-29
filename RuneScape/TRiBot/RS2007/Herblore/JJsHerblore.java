package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

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

import scripts.jjsherblore.HerbStatsThread;
import scripts.jjsherblore.Method;
import scripts.jjsherblore.State;
import scripts.jjsherblore.GUI;

@ScriptManifest(authors = { "J J" }, category = "Herblore", name = "JJ's Herblore", description = "Have the items you need visible in the bank.<br>Choose your method and item id(s) in the GUI and hit run.<br><br>Have a pestle and mortar in your inventory if grinding")
/** 
 * JJ's Herblore features
 * Mixing items together
 * Grinding items into dust
 * Cleaning herbs
 */
public class JJsHerblore extends EnumScript<State> implements Painting {

	private final long startTime = System.currentTimeMillis();
	private final int startXp = Skills.getXP(Skills.SKILLS.HERBLORE);
	private final Rectangle inventory_area = new Rectangle(525, 206, 757-525, 461-206);
	private final int mortarID = 233,
				      potionInterface = 309,
				      makeIndex = 6,
				      mixAnimation = 393;
	// GUI
	private GUI gui = new GUI();
	private Method method;
	private int item1ID;
	private int item2ID;
	private State state = State.GUI;
	
	// Statistics
	private long lastTime = startTime;
	private int currentXp = startXp;
	private int mixed = 0,
			    grinded = 0,
			    cleaned = 0,
			    decanted = 0;
	// Comitted statistics
	private int comittedRunTime = 0,
				comittedXP = 0,
				comittedMixed = 0,
				comittedCleaned = 0,
				comittedDecanted = 0,
				comittedGrinded = 0;

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
				String option = "Deposit-" + (count > 10 ? "All" : count > 5 ? 10 : count == 1 ? 1 : 5);
				if(item.click(option)){
					deposited.add(item.getID());
					switch(method){
						case CLEANING:
							cleaned += count;
							break;
						case DECANTING:
							decanted += count/2;
							break;
						case GRINDING:
							grinded += count;
							break;
						case MIXING:
							mixed += count;
							break;
					}
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
		while(Timing.timeFromMark(t) < General.random(1250, 1750)){
			if(Player.getAnimation() == mixAnimation || Inventory.getAll().length < startCount){
				return true;
			}
			sleep(20, 80);
		}
		return false;
	}

	/** Withdraws the items required to mix */
	private boolean withdrawSupplies(){
		switch(method){
			case GRINDING:
				RSItem[] grinded = Banking.find(item1ID);
				return grinded.length > 0 && grinded[0].click("Withdraw-All") && waitInventory(true, General.random(1500, 2000));
				
			case MIXING:
				RSItem[] items1 = Banking.find(item1ID);
				RSItem[] items2 = Banking.find(item2ID);
				if(items1.length > 0 && items2.length > 0){
					if(General.random(0, 1) == 1){
						return Banking.withdrawItem(items1[0], 14) && Banking.withdrawItem(items2[0], 14) && waitInventory(true, General.random(1500, 2000));
					}else{
						return Banking.withdrawItem(items2[0], 14) && Banking.withdrawItem(items1[0], 14) && waitInventory(true, General.random(1500, 2000));
					}
				}
				break;
				
			case CLEANING:
				RSItem[] herbs = Banking.find(item1ID);
				return herbs.length > 0 && herbs[0].click("Withdraw-All") && waitInventory(true, General.random(1500, 2000));

			case DECANTING:
				RSItem[] pots = Banking.find(item1ID);
				return pots.length > 0 && pots[0].click("Withdraw-All") && waitInventory(true, General.random(1500, 2000));
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
			case DECANTING:
				return Inventory.getCount(item1ID) >= 2;
		}
		return false;
	}
	
	/** Handles the mixing */
	private boolean handleMixing(int id1, int id2){
		RSItem[] items1 = Inventory.find(id1);
		RSItem[] items2 = Inventory.find(id2);
		if(items1.length > 0 && items2.length > 0){
			int r1 = General.random(0, items1.length-1);
			int r2 = General.random(0, items2.length-1);
			if(General.random(0, 1) == 1){
				return items1[r1].click("Use") && items2[r2].click("Use") && waitInterface(General.random(1500, 2500));
			}else{
				return items2[r2].click("Use") && items1[r1].click("Use") && waitInterface(General.random(1500, 2500));
			}
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
				handleMixing(item1ID, item2ID);
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
			
			case DECANTING:
				RSItem[] pots = Inventory.find(item1ID);
				if(pots.length >= 2){
					for(int i = 0; i < pots.length-1; i+=2){
						if(pots[i].click("Use")){
							 pots[i+1].click("Use");
						}
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
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's Herblore", 10, 100);
		g.drawString("Running for: " + Timing.msToString(timeRan), 10, 120);
		g.drawString("State: " + state, 10, 140);
		g.drawString("Gained " + xpGained + " herblore xp (" + xpHr + "/h)", 10, 170);
	
		if(method != null){
			g.drawString("Method: " + method, 10, 220);
			switch(method){
				case GRINDING: 
					int grindedHr = (int) (grinded / multiplier);
					g.drawString("Grinded " + grinded + " items (" + grindedHr + "/h)", 10, 190);
					g.drawString("Item ID: " + item1ID, 10, 240);
					break;
					
				case CLEANING:
					int cleanedHr = (int) (cleaned / multiplier);
					g.drawString("Cleaned " + cleaned + " herbs (" + cleanedHr + "/h)", 10, 190);
					g.drawString("Herb ID: " + item1ID, 10, 240);
					break;
					
				case MIXING:
					int mixedHr = (int) (mixed / multiplier);
					g.drawString("Mixed " + mixed + " potions (" + mixedHr + "/h)", 10, 190);
					g.drawString("Item #1 ID: " + item1ID, 10, 240);
					g.drawString("Item #2 ID: " + item2ID, 10, 260);
					break;
					
				case DECANTING:
					int decantedHr = (int) (decanted / multiplier);
					g.drawString("Decanted " + decanted + " potions (" + decantedHr + "/h)", 10, 190);
					g.drawString("Potion ID: " + item1ID, 10, 240);
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
				if(method.equals(Method.MIXING)){
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
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					gui.setVisible(true);
				}
			});
		} catch (Exception e) {
			println("Failed to spawn GUI, error log in Bot Debug");
			e.printStackTrace();
		}

		// Waiting for it to finish
		while(!gui.isDone()){
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
		currentXp = Skills.getXP(Skills.SKILLS.HERBLORE);
		Mouse.setSpeed(General.random(125, 135));
		
		// Statistics
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastTime > 60000){ // 1 minute
			// Calculate values
			long nowRunTime = ((currentTime - startTime) / 1000) - comittedRunTime;
			int nowXp = currentXp - startXp - comittedXP;
			int nowMixed = mixed - comittedMixed;
			int nowCleaned = cleaned - comittedCleaned;
			int nowDecanted = decanted - comittedDecanted;
			int nowGrinded = grinded - comittedGrinded;
			
			// New comitted values
			comittedRunTime += nowRunTime;
			comittedXP += nowXp;
			comittedMixed += nowMixed;
			comittedCleaned += nowCleaned;
			comittedDecanted += nowDecanted;
			comittedGrinded += nowGrinded;

			// Create seperate thread
			String parameters = "runtime=" + nowRunTime + "&xp=" + nowXp + "&mixed=" + nowMixed + "&cleaned=" + nowCleaned + "&decanted=" + nowDecanted + "&grinded=" + nowGrinded;			
			HerbStatsThread thread = new HerbStatsThread("http://obduro.org/scriptdata/jjsherblore.php", parameters);
			thread.start();
			
			// Update commit time
			lastTime = currentTime;
		}
		
		// Handle state
		switch(state){
			case GUI:
				sleep(20, 60);
				break;
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
