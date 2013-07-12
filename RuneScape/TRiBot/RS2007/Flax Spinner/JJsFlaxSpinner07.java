package scripts;

import java.awt.Color;
import java.awt.Graphics;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Screen;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.EnumScript;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Crafting", name = "JJ's Flax Spinner 2007")
public class JJsFlaxSpinner07 extends EnumScript<JJsFlaxState> implements Painting {

	private final RSTile BANK_TILE = new RSTile(3208, 3220, 2),
					     STAIRS_BANK_TILE = new RSTile(3205, 3209, 2),
					     STAIRS_SPIN_WHEEL_TILE = new RSTile(3205, 3209, 1),
					     SPIN_TILE = new RSTile(3209, 3213, 1),
					     DOOR_TILE = new RSTile(3207, 3214, 1),
					     STAIRS_COOK_TILE = new RSTile(3206, 3209, 0),
					     LUMBY_TILE = new RSTile(3222, 3218, 0);
	private final int FLAX_ID = 1779,
					  CLOSED_DOOR_ID = 2784,
					  SPINNING_WHEEL_ID = 9004,
					  STAIRS_COOK_ID = 2649,
					  STAIRS_WHEEL_ID = 2781,
					  STAIRS_BANK_ID = 1316,
					  CRAFTING_ANIMATION_ID = 894,
					  BOW_STRING_ID = 1777,
					  START_XP = Skills.getXP("Crafting"),
					  ROTATION = 270,
					  ANGLE = 100,
					  HOME_TELE_ANIMATION = 4847,
					  EVIL_CHICKEN_ID = 2466,
					  SWARM_ID = 1737;
	private final int[] STAIRS_IDS = {STAIRS_COOK_ID, STAIRS_WHEEL_ID},
				        RANDOM_IDS = {EVIL_CHICKEN_ID, SWARM_ID};
	private final long START_TIME = System.currentTimeMillis();
	
	private JJsFlaxState SCRIPT_STATE = getState();
	private int CURRENT_XP = START_XP;
	
	@Override
	public void onPaint(Graphics g) {
		// CALCULATIONS
		long timeRan = System.currentTimeMillis()-START_TIME;
		double multiplier = timeRan / 3600000D;

		int xpGained = CURRENT_XP-START_XP;
		int xpPerHour = (int) (xpGained / multiplier);
		
		int bsMade = xpGained / 15;
		int bsPerHour = (int) (bsMade / multiplier);
		
		int lvl = Skills.getLevelByXP(CURRENT_XP);
		int percent = Skills.getPercentToNextLevel("Crafting");
	
		// DRAWING
		g.setColor(new Color(60, 60, 60));
		g.fillRect(0, 295, 519, 75);
	
		g.setColor(Color.WHITE);
		g.drawString("JJ's Flax Spinner 2007", 5, 310);
		g.drawString("Running for: " + Timing.msToString(timeRan), 185, 310);
		g.drawString("State: " + SCRIPT_STATE, 335, 310);
		
		g.drawString("Xp gained: " + xpGained + " (" + xpPerHour + "/h)", 5, 330);
		g.drawString("Bowstrings made: " + bsMade + " (" + bsPerHour + "/h)", 335, 330);
	
		g.setColor(new Color(0, 200, 0));
		g.fillRect(105, 345, percent*3, 20);

		g.setColor(new Color(200, 0, 0));
		g.fillRect(105+percent*3, 345, 3*(100-percent), 20);
		
		g.setColor(Color.WHITE);
		g.drawString("Currently lvl " + lvl, 5, 360);
		g.drawString("lvl " + (lvl+1), 420, 360);
		
		g.setColor(Color.BLACK);
		g.drawString("" + percent + "%", 238, 360);
	}
	
	private JJsFlaxState getState(){
		if(haveCombatRandom()){
			return JJsFlaxState.COMBAT_RANDOM;
		}else if(Player.getPosition().getPlane() == 0){
			return JJsFlaxState.DEATH_WALK;
		}else if(haveFlax()){
			if(atBank()){
				return JJsFlaxState.BANK_TO_STAIRS;
			}else{
				if(atSpinWheel()){
					if(haveFlaxInterface()){
						return JJsFlaxState.SELECT_FLAX;
					}else{
						if(haveAmountInterface()){
							return JJsFlaxState.TYPE_AMOUNT;
						}else{
							if(areCrafting()){
								return JJsFlaxState.CRAFTING;
							}else{
								return JJsFlaxState.USE_SPIN_WHEEL;
							}
						}
					}
				}else{
					if(Player.getPosition().getPlane() != 1){
						if(atStairs()){
							return JJsFlaxState.CLIMB_STAIRS;
						}else{
							return JJsFlaxState.BANK_TO_STAIRS;
						}
					}else{
						if(isDoorClosed()){
							if(atDoor()){
								return JJsFlaxState.OPEN_DOOR;
							}else{
								return JJsFlaxState.WALK_TO_DOOR;
							}
						}else{
							return JJsFlaxState.STAIRS_TO_WHEEL;
						}
					}
				}
			}
		}else{
			if(Player.getPosition().getPlane() != 2){
				if(atStairs()){
					return JJsFlaxState.CLIMB_STAIRS;
				}else{
					if(isDoorClosed()){
						if(atDoor()){
							return JJsFlaxState.OPEN_DOOR;
						}else{
							return JJsFlaxState.WALK_TO_DOOR;
						}
					}else{
						return JJsFlaxState.WHEEL_TO_STAIRS;
					}
				}
			}else{
				if(Banking.isBankScreenOpen()){
					if(haveBowstring()){
						return JJsFlaxState.DEPOSIT_BOW_STRING;
					}else{
						if(Inventory.getAll().length == 0){
							return JJsFlaxState.WITHDRAW_FLAX;
						}else{
							return JJsFlaxState.DEPOSIT_JUNK;
						}
					}
				}else{
					if(atBank()){
						return JJsFlaxState.OPEN_BANK;
					}else{
						return JJsFlaxState.STAIRS_TO_BANK;
					}
				}
			}
		}
	}
	
	private boolean withdrawFlax(){
		if(Banking.isBankScreenOpen()){
			RSItem[] flax = Banking.find(FLAX_ID);
			int count = Inventory.getAll().length;
			if(flax != null && flax.length > 0 && flax[0].click("Withdraw All")){
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < General.random(1500, 2500)){
					if(Inventory.getAll().length > count){
						return true;
					}
					sleep(20, 80);
				}
			}
		}
		return false;
	}
	
	private boolean depositBowstring(){
		if(Banking.isBankScreenOpen()){
			RSItem[] string = Inventory.find(BOW_STRING_ID);
			int count = Inventory.getAll().length;
			if(string != null && string.length > 0){
				int r = General.random(0, string.length-1);
				if(string[r].click("Store All")){
					long t = System.currentTimeMillis();
					while(Timing.timeFromMark(t) < General.random(1500, 2500)){
						if(Inventory.getAll().length < count){
							return true;
						}
						sleep(20, 80);
					}
				}
			}
		}
		return false;
	}
	
	private boolean depositJunk(){
		if(Banking.isBankScreenOpen()){
			RSItem[] all = Inventory.getAll();
			if(all != null && all.length > 0 && all[0].click("Store All")){
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < General.random(1500, 2500)){
					if(Inventory.getAll().length < all.length){
						return true;
					}
					sleep(20, 80);
				}
			}
		}
		return false;
	}
	
	private void waitUntilIdle(RSTile tile){
		sleep(400, 800);
		long t = System.currentTimeMillis();
	
		while(Timing.timeFromMark(t) < General.random(400, 800)){
			if(Player.getPosition().distanceTo(tile) <= 1){
				break;
			}
			
			sleep(40, 80);
			
			if(Player.isMoving() || Player.getAnimation() != -1){
				t = System.currentTimeMillis();
			}
		}
	}
	
	private boolean atStairs(){
		RSTile myPos = Player.getPosition();
		return myPos.distanceTo(STAIRS_BANK_TILE) < 3;
	}
	
	private boolean walkToTile(RSTile tile){
		if(Walking.walkTo(tile)){
			waitUntilIdle(tile);
			return true;
		}
		return false;
	}
	
	private boolean walkToStairs(){
		int plane = Player.getPosition().getPlane();
		if(plane == 1){
			// At spinning wheel
			return walkToTile(STAIRS_SPIN_WHEEL_TILE);
		}else if(plane == 2){
			// At bank
			return walkToTile(STAIRS_BANK_TILE);
		}else if(plane == 0){
			// At cook
			return walkToTile(STAIRS_COOK_TILE);
		}
		return false;
	}
	
	private boolean walkToBank(){
		return walkToTile(BANK_TILE);
	}
	
	private boolean walkToDoor(){
		return walkToTile(DOOR_TILE);
	}
	
	private boolean walkToWheel(){
		return walkToTile(SPIN_TILE);
	}
	
	private boolean climbStairs(){
		int plane = Player.getPosition().getPlane();
		if(plane == 2){
			// Down
			RSObject[] stairs = Objects.findNearest(10, STAIRS_BANK_ID);
			if(stairs != null && stairs.length > 0 && DynamicClicking.clickRSObject(stairs[0], "down")){
				waitUntilIdle(stairs[0].getPosition());
				return true;
			}
		}else{
			// Up
			RSObject[] stairs = Objects.findNearest(10, STAIRS_IDS);
			if(stairs != null && stairs.length > 0 && DynamicClicking.clickRSObject(stairs[0], "up")){
				waitUntilIdle(stairs[0].getPosition());
				return true;
			}
		}
		return false;
	}
	
	private boolean atDoor(){
		RSTile myPos = Player.getPosition();
		return myPos.distanceTo(DOOR_TILE) < 3;
	}
	
	private boolean openDoor(){
		RSObject[] door = Objects.findNearest(15, CLOSED_DOOR_ID);
		if(door != null && door.length > 0){
			// Setting camera angle around 270 degrees if needed
			int currentRotation = Camera.getCameraRotation();
			if(Math.abs(ROTATION-currentRotation) > 10){
				Camera.setCameraRotation(General.random(ROTATION-5, ROTATION+5));
			}
			
			door[0].setClickHeight(General.random(300, 700));
			if(DynamicClicking.clickRSObject(door[0], "Open")){
				waitUntilIdle(door[0].getPosition());
				return true;
			}else{
				Camera.turnToTile(door[0].getPosition());
				openDoor();
			}
		}
		return false;
	}
	
	private boolean spinWheel(){
		RSObject[] wheel = Objects.findNearest(10, SPINNING_WHEEL_ID);
		if(wheel != null && wheel.length > 0 && DynamicClicking.clickRSObject(wheel[0], "Spin")){
			waitUntilIdle(wheel[0].getPosition());
			return true;
		}
		return false;
	}
	
	private boolean spinFlax(){
		RSInterfaceChild flax = Interfaces.get(459, 91);
		if(flax != null && !flax.isHidden() && flax.click("X")){
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(1500, 2000)){
				if(haveAmountInterface()){
					return true;
				}
				sleep(50, 150);
			}		
		}
		return false;
	}
	
	private boolean haveFlaxInterface(){
		RSInterfaceChild flax = Interfaces.get(459, 91);
		return flax != null && !flax.isHidden();
	}
	
	private boolean haveAmountInterface(){
		RSInterfaceChild amount = Interfaces.get(548, 94);
		if(amount != null && !amount.isHidden()){
			String txt = amount.getText();
			if(txt != null && txt.equals("*")){
				Color c = Screen.getColorAt(259, 428);
				Color b = new Color(0, 0, 128);
				return org.tribot.api.Screen.coloursMatch(b, c, new Tolerance(10));
			}
		}
		return false;
	}
	
	private boolean typeAmount(){
		if(haveAmountInterface()){
			sleep(20, 800);
			int r = General.random(1, 10);
			if(r <= 2){
				Keyboard.typeSend("28"); // 20% chance
			}else if(r <= 4){
				Keyboard.typeSend("333"); // 20% chance
			}else{
				Keyboard.typeSend("33"); // 60% chance
			}
			return true;
		}
		return false;
	}
	
	private boolean areCrafting(){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < General.random(1200, 1800)){
			if(Player.getAnimation() == CRAFTING_ANIMATION_ID){
				return true;
			}
			sleep(20, 80);
		}
		return false;
	}
	
	private boolean isDoorClosed(){
		RSObject[] doors = Objects.findNearest(15, CLOSED_DOOR_ID);
		if(doors != null && doors.length > 0)
			for(RSObject door : doors)
				if(door.getPosition().getY() < 3220)
					return true;
		return false;
	}
	
	private boolean haveFlax(){
		RSItem[] flax = Inventory.find(FLAX_ID);
		return flax != null && flax.length > 0;
	}
	
	private boolean haveBowstring(){
		RSItem[] string = Inventory.find(BOW_STRING_ID);
		return string != null && string.length > 0;
	}
	
	private boolean atBank(){
		RSTile myPos = Player.getPosition();
		return myPos.distanceTo(BANK_TILE) <= 3;
	}
	
	private boolean atSpinWheel(){
		RSTile myPos = Player.getPosition();
		return myPos.distanceTo(SPIN_TILE) <= 2;
	}
	
	private boolean haveCombatRandom(){
		RSNPC[] all = NPCs.getAll();
		for(int id : RANDOM_IDS){
			for(RSNPC npc : all){
				if(npc.getID() == id && npc.isInteractingWithMe()){
					System.out.println("We have a random event with ID: " + id);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean handleCombatRandom(){
		switch(SCRIPT_STATE){
			// EVADE BY WALKING AWAY TO NEXT LOC
			case DEPOSIT_BOW_STRING: case DEPOSIT_JUNK: case WITHDRAW_FLAX:	case OPEN_BANK:
				return walkToTile(STAIRS_BANK_TILE);

			// EVADE BY WALKING AWAY TO NEXT LOC
			case CRAFTING: 	case OPEN_DOOR: case SELECT_FLAX: case TYPE_AMOUNT: case USE_SPIN_WHEEL: case WALK_TO_DOOR:
				return walkToTile(STAIRS_SPIN_WHEEL_TILE);
			
			// EVADE BY WALKING TO RANDOM SPOT
			default: 
				RSTile myTile = Player.getPosition();
				RSTile evadeTile = new RSTile(myTile.getX() + General.random(-8, 8), myTile.getY() + General.random(-8, 8));
				return walkToTile(evadeTile);
		}
	}
	
	private boolean openBankBooth(){
		RSObject[] banks = Objects.findNearest(10, 18491);
		if(banks != null && banks.length > 0){
			RSObject bank = banks[0];
			if(DynamicClicking.clickRSObject(bank, "Bank")){
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < General.random(1200, 1800)){
					if(Banking.isBankScreenOpen()){
						return true;
					}
					sleep(20, 80);
				}
			}
		}
		return false;
	}
	
	private boolean handleDeathWalk(){
		RSObject[] stairs = Objects.findNearest(20, STAIRS_COOK_ID);
		if(stairs != null && stairs.length > 0){
			if(stairs[0].isOnScreen() && Player.getPosition().distanceTo(stairs[0].getPosition()) <= 5){
				println("Found stairs, climbing!");
				if(DynamicClicking.clickRSObject(stairs[0], "up")){
					waitUntilIdle(stairs[0].getPosition());
					return true;
				}
			}else{
				println("Found stairs, but can't interact yet, walking there!!");
				walkToTile(STAIRS_COOK_TILE);
				return true;
			}
		}else{
			RSTile[] path = Walking.generateStraightPath(STAIRS_COOK_TILE);
			println("Path length: " + path.length);
			if(path.length > 5){
				println("Not in lumbridge, trying to home tele!");
				
				if(!GameTab.getOpen().equals(TABS.MAGIC)){
					println("Opening magic spellbook!");
					GameTab.open(TABS.MAGIC);
					sleep(500, 1000);
				}
				
				RSInterfaceChild homeTele = Interfaces.get(192, 0);
				if(homeTele != null && !homeTele.isHidden() && homeTele.click("Cast")){
					println("Casted home tele");
					sleep(3000, 4000);
					
					if(Player.getAnimation() != HOME_TELE_ANIMATION){
						println("Failed to perform home teleport, ending script!");
						this.stopScript();
					}
					
					waitUntilIdle(LUMBY_TILE);
					return true;
				}
			}else if(Walking.walkPath(path)){
				println("In lumbridge, walking to stairs!");
				waitUntilIdle(path[path.length-1]);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public JJsFlaxState getInitialState() {
		println("Script has been started");
		Mouse.setSpeed(General.random(130, 150));
		
		// Setting angle fully up
		int currentAngle = Camera.getCameraAngle();
		if(Math.abs(ANGLE-currentAngle) > 10){
			Camera.setCameraAngle(ANGLE);
		}
		
		Walking.control_click = true;
		Walking.walking_timeout = General.random(4000, 6000);

		return getState();
	}	

	@Override
	public JJsFlaxState handleState(JJsFlaxState state) {
		SCRIPT_STATE = state;
		CURRENT_XP = Skills.getXP("Crafting");

		// Setting camera angle around 270 degrees
		int currentRotation = Camera.getCameraRotation();
		if(Math.abs(ROTATION-currentRotation) > 10){
			Camera.setCameraRotation(General.random(ROTATION-5, ROTATION+5));
		}
				
		switch(SCRIPT_STATE){
			case COMBAT_RANDOM:
				handleCombatRandom();
				break;
		
			case BANK_TO_STAIRS:
				walkToStairs();
				break;
				
			case CLIMB_STAIRS:
				climbStairs();
				break;
				
			case CRAFTING:
				sleep(200, 600);
				break;
				
			case DEPOSIT_BOW_STRING:
				depositBowstring();
				break;
				
			case OPEN_BANK:
				openBankBooth();
				break;
				
			case OPEN_DOOR:
				openDoor();
				break;
				
			case SELECT_FLAX:
				spinFlax();
				break;
				
			case STAIRS_TO_BANK:
				walkToBank();
				break;
				
			case STAIRS_TO_WHEEL:
				walkToWheel();
				break;
				
			case TYPE_AMOUNT:
				typeAmount();
				break;
				
			case USE_SPIN_WHEEL:
				spinWheel();
				break;
				
			case WHEEL_TO_STAIRS:
				walkToStairs();
				break;
				
			case WITHDRAW_FLAX:
				withdrawFlax();
				break;
				
			case WALK_TO_DOOR:
				walkToDoor();
				break;
			
			case DEPOSIT_JUNK:
				depositJunk();
				break;
			
			case DEATH_WALK:
				println("Starting death walk");
				handleDeathWalk();
				break;
		}
		
		sleep(20, 80);
		return getState();
	}
}
