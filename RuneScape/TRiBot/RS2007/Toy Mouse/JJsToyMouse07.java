package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.TileObserver;
import java.util.ArrayList;

import org.tribot.api.Constants;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.api2007.Game;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Screen;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.EnumScript;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Agility", name = "JJ's Toy Mouse 2007")
public class JJsToyMouse07 extends Script implements Painting {

	private final long startTime = System.currentTimeMillis();
	private final int UNWINDED_MOUSE_ID = 7767,
					  WINDED_MOUSE_ID = 7769,
					  TOY_MOUSE_NPC_ID = 3597,
					  START_XP = Skills.getXP("Agility");
	
	private State scriptState = State.WIND_MOUSES;
	private int CURRENT_XP = START_XP;

	/** RANDOM EVENT INFORMATION **/
	// RANDOM NPCS
	private final int MYSTERIOUS_OLD_MAN = 410, // Talk-to Mysterious Old Man -> Click to continue
				      MR_MORDAUT = 6117,
				      DR_JEKYLL = 2540, // Talk-to Dr Jekyll -> Click continue, Click continue, Click continue, Click continue
				      MILES = 2537, // -> Talk to -> SOLVE
				      SANDWHICH_LADY = 3117,// Talk to -> Get pie
				      EVIL_CHICKEN = 2465,
				      SWARM = 411,
				      CAP_N_HAND = 2359, // Talk to, click continue
				      STRANGE_PLANT = 407, // 408 = attack PLANT
					  FREAKY_FORESTER = 2458,
					  SERGEANT_DAMIEN = 2790;
	private final int[] RANDOM_NPCS = {MYSTERIOUS_OLD_MAN, MR_MORDAUT, 
									   DR_JEKYLL, MILES, SANDWHICH_LADY,
									   EVIL_CHICKEN, SWARM, CAP_N_HAND,
									   STRANGE_PLANT, FREAKY_FORESTER,
					  				   SERGEANT_DAMIEN};
	// RANDOM ITEMS
	private final int STRANGE_BOX_ID = 3062;
	// RANDOMS OBJECTS
	private final int SIT_UP_MARKER_ID = 10068,
					  RUN_MARKER_ID = 10069,
					  PUSH_UP_MARKER_ID = 10070,
					  STAR_JUMP_MARKER_ID = 10071;
	
	@Override
	public void onPaint(Graphics g) {
		g.setColor(new Color(60, 60, 60));
		g.fillRect(4, 4, 175, 240);
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's Toy Mouse", 10, 20);
		g.drawString("Running for: " + Timing.msToString(Timing.timeFromMark(startTime)), 10, 40);
		g.drawString("State: " + scriptState, 10, 60);
		
		g.drawString("Starting xp: " + START_XP, 10, 100);
		g.drawString("Current xp: " + CURRENT_XP, 10, 120);
		int xpGained = CURRENT_XP-START_XP;
		int xpPerHour = (int) (xpGained / ((System.currentTimeMillis()-startTime) / 3600000D));
		g.drawString("Xp gained: " + xpGained + " (" + xpPerHour + "/h)", 10, 140);
		
		g.drawString("Random detected? " + randomDetected(), 10, 180);
		g.drawString("Mouses on ground? " + mousesOnGround(), 10, 200);
		g.drawString("Mouses on floor? " + mousesOnFloor(), 10, 220);
		g.drawString("All mouses winded? " + allMousesWinded(), 10, 240);
		
		if(scriptState.equals(State.RANDOM_EVENT)){
			g.setFont(new Font("Verdana", Font.BOLD, 38));
			g.drawString("PLEASE SOLVE THE RANDOM EVENT!", 0, 200);
		}
	}
	
	public enum State{
		WIND_MOUSES, RELEASE_MOUSES, LOOT_MOUSES, 
		PICK_UP_MOUSES, RANDOM_EVENT;
	}
	
	public State getState(){
		if(randomDetected())
			return State.RANDOM_EVENT;
		else if(mousesOnGround())
			return State.LOOT_MOUSES;
		else if(mousesOnFloor())
			return State.PICK_UP_MOUSES;
		else if (!allMousesWinded())
			return State.WIND_MOUSES;
		else
			return State.RELEASE_MOUSES;
	}
	
	private boolean mousesOnGround(){
		return GroundItems.findNearest(UNWINDED_MOUSE_ID).length > 0;
	}
	
	private boolean mousesOnFloor(){
		return NPCs.findNearest(TOY_MOUSE_NPC_ID).length > 0;
	}
	
	private boolean allMousesWinded(){
		return Inventory.find(UNWINDED_MOUSE_ID).length == 0 && Inventory.find(WINDED_MOUSE_ID).length > 0;
	}
	
	private boolean windMouses(){
		RSItem[] unwinded = Inventory.find(UNWINDED_MOUSE_ID);
		int count = 0;
		if(unwinded != null && unwinded.length > 0)
			for(RSItem mouse : unwinded)
				if(mouse.click("Wind")){
					int winded = Inventory.find(WINDED_MOUSE_ID).length;
					long t = System.currentTimeMillis();
					while(Timing.timeFromMark(t) < General.random(1000, 2000)){
						if(Inventory.find(WINDED_MOUSE_ID).length > winded){
							count++;
							break;
						}
						sleep(100, 200);
					}
				}
		return unwinded != null && count == unwinded.length;
	}
	
	private boolean releaseMouses(){
		RSItem[] winded = Inventory.find(WINDED_MOUSE_ID);
		int count = 0;
		for(RSItem mouse : winded)
			if(mouse.click("Release")){
				count++;
			}
		return winded != null && count == winded.length;
	}
	
	private boolean waitPickup(){
		int invCount = Inventory.getAll().length;
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < General.random(100, 200)){
			if(Inventory.getAll().length > invCount)
				return true;
			sleep(20, 60);
			if(Player.isMoving() || Player.getAnimation() != -1)
				t = System.currentTimeMillis();
		}
		return false;
	}
	
	private boolean pickUpMouses(){
		CURRENT_XP = Skills.getXP("Agility");
		int items = Inventory.getAll().length;
		RSNPC[] mouses = NPCs.findNearest(TOY_MOUSE_NPC_ID);

		if(mouses != null && mouses.length > 0){
			mouses[0].setClickHeight(General.random(-85, -95));
			if(mouses[0].click("Pick-up"))
				return waitPickup();
		}
	
		return Inventory.getAll().length - items == mouses.length; 
	}
	
	private boolean lootMouses(){
		RSGroundItem[] mouses = GroundItems.findNearest(UNWINDED_MOUSE_ID);
		if(mouses != null && mouses.length > 0)
			if(mouses[0].click("Take"))
				return waitPickup();
		return false;
	}
	
	private boolean randomDetected(){
		RSNPC[] all = NPCs.getAll();
		if(all != null && all.length > 0)
			for(RSNPC npc : all){
				int npcID = npc.getID();
				for(int id : RANDOM_NPCS)
					if(npcID == id)
						return true;
			}
		return false;
	}

	@Override
	public void run() {
		println("JJ's Toy Mouse has been started!");
		Mouse.setSpeed(140);

		while(true){
			scriptState = getState();
			switch(scriptState){
				case RANDOM_EVENT:
					sleep(500, 1000);
					break;
			
				case LOOT_MOUSES:
					lootMouses();
					break;
					
				case PICK_UP_MOUSES:
					pickUpMouses();
					break;
					
				case RELEASE_MOUSES:
					releaseMouses();
					break;
					
				case WIND_MOUSES:
					windMouses();
					break;
			
			}
			sleep(50, 100);
		}

	}

	
}
