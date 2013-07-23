package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextLayout;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

/**
 * JJ's Willow Chopper
 * Chops willows in Rimmington and sells the logs
 * to the general store.
 * 
 * @author JJ
 */

@ScriptManifest(authors = { "J J" }, category = "Woodcutting", name = "JJ's Willow Chopper")
public class JJsWillowChopper extends Script implements Painting {

	// Chopping related
	private final Rectangle willow_area = new Rectangle(2958, 3188, 14, 14);
	private final int[] willow_ids = {23724, 23722, 23723};
	private final int willow_id = 1519;
	// Shop related
	private final Rectangle store_area = new Rectangle(2947, 3212, 3, 6);
	private final int[] customer_ids = {2536, 2535};
	private final int store_interface = 300;
	private final int store_title_index = 76;
	// Random event related
	private final int[] nest_ids = {5073, 5074};
	// Script states
	private enum State {
		CHOPPING, CLICK_WILLOW, WALK_TO_WILLOWS, RELOCATE_TO_WILLOWS,
		CLICK_CUSTOMER, SELLING_WILLOWS, WALK_TO_STORE;
	}
	// Paint related
	private final long start_time = System.currentTimeMillis();
	private final int start_xp = Skills.getXP("Woodcutting");
	private int current_xp = start_xp;
	private int start_level = Skills.getActualLevel("Woodcutting");
	private int current_level = start_level;
	private State state;
	
	
	/// WALKING METHODS \\\
	/**
	 * Checks if we inside the area
	 * @param r the area
	 * @return true if at willows
	 */
	private boolean inArea(Rectangle r){
		RSTile myTile = Player.getPosition();
		return r.contains(myTile.getX(), myTile.getY());
	}
	
	/**
	 * Sleeps until the RSCharacter is idle
	 */
	private void waitUntilIdle(){
		while(Player.isMoving() || Player.getAnimation() != -1){
			sleep(50, 150);
		}
	}
	
	/**
	 * Walks a generated path to a random spot in the area 
	 * using blind walk/offset.
	 * 
	 * @param r the area
	 * @return true if walked inside area
	 */
	private boolean walkToArea(Rectangle r){
		RSTile[] straight_path = Walking.generateStraightPath(new RSTile(General.random(r.x, r.x+r.width), General.random(r.y, r.y+r.height)));
		RSTile[] path = Walking.randomizePath(straight_path, General.random(0, 2), 1);
		if(Walking.walkPath(path)){
			waitUntilIdle();
		}
		
		return inArea(r);
	}
	
	private boolean relocateWillows(){
		RSObject[] trees = Objects.findNearest(30, willow_ids);
		if(trees.length > 0){
			int r = General.random(0, trees.length-1);
			RSTile loc = trees[r].getPosition();
			if(willow_area.contains(loc.getX(), loc.getY()) && 
			   Walking.blindWalkTo(loc)){
				return true;
			}
		}
		return false;
	}

	
	/// CHOPPING RELATED METHODS \\\
	/**
	 * Checks if we are currently chopping
	 * @return true if chopping
	 */
	private boolean areChopping(){
		int animation = Player.getAnimation();
		return animation > 850 && animation < 900;
	}
	
	/**
	 * Waits a certain amount of time until we are chopping
	 * @param ms time to wait
	 * @return true if chopping within time
	 */
	private boolean waitChopping(int ms){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < ms){
			if(areChopping()){
				return true;
			}else if(Player.isMoving()){
				t = System.currentTimeMillis();
			}
			sleep(ms/20, ms/10);
		}
		return false;
	}
	
	/**
	 * Checks if there are any visible trees
	 * @return true if visible trees available
	 */
	private boolean haveVisibleTrees(){
		RSObject[] trees = Objects.findNearest(10, willow_ids);
		for(RSObject tree : trees){
			if(tree.isOnScreen()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Attempts to click the chop down option from a willow tree
	 * @return true if clicked
	 */
	private boolean chopTree(){
		RSObject[] trees = Objects.findNearest(10, willow_ids);
		for(RSObject tree : trees){
			if(tree.isOnScreen() && 
			   DynamicClicking.clickRSObject(tree, "Chop down") &&
			   waitChopping(General.random(2000, 3000))){
				return true;
			}
		}
		return false;
	}

	
	/// STORE RELATED METHODS \\\
	/**
	 * Checks if the General store screen is open
	 * @return true if open
	 */
	private boolean isStoreOpen(){
		RSInterfaceChild titleInterface = Interfaces.get(store_interface, store_title_index);
		return titleInterface != null && titleInterface.getText().contains("Rimmington General Store");
	}
	
	/**
	 * Waits a certain amount of time for the store screen to open
	 * @param ms amount of time to wait in ms
	 * @return true if store opened in time
	 */
	private boolean waitStoreOpen(int ms){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < ms){
			if(isStoreOpen()){
				return true;
			}else if(Player.isMoving()){
				t = System.currentTimeMillis();
			}
			sleep(ms/20, ms/10);
		}
		return false;
	}
	
	/**
	 * Attempts to trade a General store customer
	 * @return true if traded
	 */
	private boolean tradeCustomer(){
		RSNPC[] customers = NPCs.find(customer_ids);
		for(RSNPC customer : customers){
			if(customer.isOnScreen() && 
			   DynamicClicking.clickRSModel(customer.getModel(), "Trade") && 
			   waitStoreOpen(General.random(2000, 3000))) {
				return true;
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
			sleep(ms/20, ms/10);
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
			if(Inventory.find(willow_id).length < startAmount){
				return true;
			}
			sleep(ms/20, ms/10);
		}
		return false;
	}

	/**
	 * Sells the willow logs to the general store
	 * @return true if sold all
	 */
	private boolean sellWillows(){
		RSItem[] willows = Inventory.find(willow_id);
		
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
			willows = Inventory.find(willow_id);
		}
		
		return willows.length == 0;
	}

	
	/// MAINLOOP \\\
	@Override
	public void onPaint(Graphics g) {
		long timeRan = System.currentTimeMillis() - start_time;
		int xpGained = current_xp - start_xp;
		int xpPerHour = (int) (xpGained / (timeRan / 3600000D));
		int lvlsGained = current_level - start_level;
		
		Graphics2D g2d = (Graphics2D) g;
		Font font = new Font("Verdana", Font.PLAIN, 15);
		shadowedText(g2d, font, "JJ's Willow Chopper", 10, 50);
		shadowedText(g2d, font, "Running for: " + Timing.msToString(timeRan), 10, 70);
		shadowedText(g2d, font, "State: " + state, 10, 90);
		shadowedText(g2d, font, "Gained " + xpGained + " xp (" + xpPerHour + " xp/h)", 10, 110);
		shadowedText(g2d, font, "WC level: " + current_level + " (+" + lvlsGained + ")", 10, 130);
	}
	
	/** Draws shadowed text */
	private void shadowedText(Graphics2D g2d, Font font, String text, int x, int y){
		TextLayout textLayout = new TextLayout(text, font, g2d.getFontRenderContext());
	    
		g2d.setPaint(Color.BLACK);
	    textLayout.draw(g2d, x+1, y+1);

	    g2d.setPaint(Color.WHITE);
	    textLayout.draw(g2d, x, y);
	}

	/**
	 * Retrieves the current state based on the game state
	 * @return current state
	 */
	private State getState(){
		if(Inventory.isFull()){
			if(!inArea(store_area)){
				return State.WALK_TO_STORE;
			}else if(!isStoreOpen()){
				return State.CLICK_CUSTOMER; 
			}else{
				return State.SELLING_WILLOWS;
			}
		}else{
			if(!areChopping()){
				if(!inArea(willow_area)){
					return State.WALK_TO_WILLOWS;
				}else if(!haveVisibleTrees()){
					return State.RELOCATE_TO_WILLOWS;
				}else{
					return State.CLICK_WILLOW;
				}
			}else{
				return State.CHOPPING;
			}
		}
	}

	@Override
	public void run() {
		println("JJ's Willow Chopper has been started!");
		Mouse.setSpeed(General.random(120, 140));
		
		while(true){
			state = getState();
			current_xp = Skills.getXP("Woodcutting");
			current_level = Skills.getActualLevel("Woodcutting");
			
			switch(state){
				case CHOPPING:
					sleep(200, 400);
					break;
				case CLICK_CUSTOMER:
					tradeCustomer();
					break;
				case CLICK_WILLOW:
					chopTree();
					break;
				case RELOCATE_TO_WILLOWS:
					relocateWillows();
					break;
				case SELLING_WILLOWS:
					sellWillows();
					break;
				case WALK_TO_STORE:
					walkToArea(store_area);
					break;
				case WALK_TO_WILLOWS:
					walkToArea(willow_area);
					break;
			}
		}
	}

}
