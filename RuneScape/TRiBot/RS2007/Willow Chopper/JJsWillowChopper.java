package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

import scripts.jjswillowchopper.ChopStatsThread;
import scripts.jjswillowchopper.Navigation;
import scripts.jjswillowchopper.Chopping;
import scripts.jjswillowchopper.Randoms;
import scripts.jjswillowchopper.State;
import scripts.jjswillowchopper.Store;

/**
 * JJ's Willow Chopper
 * Chops willows in Rimmington and sells the logs to the general store.
 * @author JJ
 */
@ScriptManifest(authors = { "J J" }, category = "Woodcutting", name = "JJ's Willow Chopper Remake")
public class JJsWillowChopper extends Script implements Painting, MessageListening07 {

	// Statistics
	private final long startTime = System.currentTimeMillis();
	private final int startXP = Skills.getXP(SKILLS.WOODCUTTING);
	private int currentXP = startXP;
	private int startLvl = Skills.getActualLevel(SKILLS.WOODCUTTING);
	private int currentLvl= startLvl;
	// Submitted statistics
	private long lastCommitTime = startTime,
				 committedRunTime = 0;
	private int committedXP = 0;
	// Paint
	private final Font font = new Font("Verdana", Font.PLAIN, 15);
	private State state;
	// Methods
	private Chopping chopping;
	private Navigation navigation;
	private Randoms randoms;
	private Store store;

	@Override
	public void serverMessageReceived(String message) {
		if(message != null){
			if(message.contains("You need an axe to chop down this tree")){
				println("Unfortunately your axe has been broken, stopping script!");
				stopScript();
			}else if(message.contains("A bird's nest falls out of the tree")){
				println("Bird nest fell out of the tree!!");
			}
		}
	}

	/** Unused interface methods */
	public void clanMessageReceived(String name, String message) {}
	public void personalMessageReceived(String name, String message) {}
	public void playerMessageReceived(String name, String message) {}
	public void tradeRequestReceived(String name) {}

	@Override
	public void onPaint(Graphics g) {
		// Statistics
		long timeRan = System.currentTimeMillis() - startTime;
		int xpGained = currentXP - startXP;
		int xpPerHour = (int) (xpGained / (timeRan / 3600000D));
		int lvlsGained = currentLvl-startLvl;
		
		// Drawing
		Graphics2D g2d = (Graphics2D) g;
		drawShadowText(g2d, font, "JJ's Willow Chopper Remake", 10, 50);
		drawShadowText(g2d, font, "Running for: " + Timing.msToString(timeRan), 10, 70);
		drawShadowText(g2d, font, "State: " + state, 10, 90);
		drawShadowText(g2d, font, "Gained " + xpGained + " xp (" + xpPerHour + " xp/h)", 10, 110);
		drawShadowText(g2d, font, "WC level: " + currentLvl + " (+" + lvlsGained + ")", 10, 130);
	}
	
	/** Draws shadowed text */
	private void drawShadowText(Graphics2D g2d, Font font, String text, int x, int y){
		TextLayout textLayout = new TextLayout(text, font, g2d.getFontRenderContext());
	    
		g2d.setPaint(Color.BLACK);
	    textLayout.draw(g2d, x+1, y+1);

	    g2d.setPaint(Color.WHITE);
	    textLayout.draw(g2d, x, y);
	}

	/**
	 * Retrieves the current state based on the game state
	 * @return Current state
	 */
	private State getState(){
		if(randoms.haveEvilTree()){ // Evil tree random
			return State.EVIL_TREE;
		}else if(Inventory.isFull()){ // Full inventory
			if(navigation.insideStoreArea()){ // In store area
				if(store.isStoreOpen()){ // Sell willows
					return State.SELL_WILLOWS;
				}else{ // Open general store
					return State.TRADE_CUSTOMER;
				}
			}else{ // Walk to store area
				if(navigation.canEnterStore()){ // Can enter store
					return State.WALK_TO_STORE;
				}else{ // Can't enter store
					if(navigation.canOpenDoor()){ // Open door
						return State.OPEN_DOOR;
					}else{ // Walk to door
						return State.WALK_TO_DOOR;
					}
				}
			}
		}else if(navigation.insideWillowArea() && chopping.canChopTree()){ // Can chop a willow
			if(chopping.areChopping()){ // Chopping
				return State.CHOPPING;
			}else{ // Click willow
				return State.CLICK_WILLOW;
			}
		}else{ // Walk to willows
			return State.WALK_TO_WILLOWS;
		}
	}

	@Override
	public void run() {
		println("JJ's Willow Chopper has been started!");
		Mouse.setSpeed(General.random(120, 140));
	
		// Methods
		chopping = new Chopping();
		navigation = new Navigation();
		randoms = new Randoms();
		store = new Store();
		
		// Script loop
		while(true){
			// Calculations
			state = getState();
			currentXP = Skills.getXP(SKILLS.WOODCUTTING);
			currentLvl = Skills.getActualLevel(SKILLS.WOODCUTTING);
			
			// Script statistics
			long currentTime = System.currentTimeMillis();
			if(currentTime - lastCommitTime > 60000){ // Been 1 minute since commit
				// Values since last commit time
				long nowRunTime = ((currentTime - startTime)/1000) - committedRunTime;
				int nowXP = currentXP - startXP - committedXP;
				
				// New committed values
				committedRunTime += nowRunTime;
				committedXP += nowXP;
				lastCommitTime = currentTime;
				
				// Submit statistics on new thread
				String parameters = "runtime=" + nowRunTime + "&xp=" + nowXP + "&level=" + currentLvl;			
				ChopStatsThread thread = new ChopStatsThread("http://obduro.org/scriptdata/jjswillowchopper.php", parameters);
				thread.start();
			}

			// Handle state
			switch(state){
				case CHOPPING:
					sleep(50, 150);
					break;
					
				case CLICK_WILLOW:
					chopping.chopTree();
					break;
				
				case EVIL_TREE:
					if(chopping.chopTree()){
						sleep(4000, 6000);
					}
					break;
					
				case OPEN_DOOR:
					navigation.openDoor();
					break;
					
				case SELL_WILLOWS:
					store.sellWillows();
					break;
					
				case TRADE_CUSTOMER:
					store.tradeCustomer();
					break;
				
				case TREE_SPIRIT:
					// Handled by TRiBot
					break;
					
				case WALK_TO_DOOR:
					navigation.navigateToDoor();
					break;
					
				case WALK_TO_STORE:
					navigation.navigateToStore();
					break;
					
				case WALK_TO_WILLOWS:
					navigation.navigateToWillows();
					break;
			}

			sleep(20, 60);
		}
	}


}
