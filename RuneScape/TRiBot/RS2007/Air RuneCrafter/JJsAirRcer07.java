package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.EnumScript;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "RuneCrafting", name = "JJ's Air RCer 07 v3") 
public class JJsAirRcer07 extends EnumScript<JJsAirState> implements Painting {

	private final int PURE_ESS_ID = 7936,
				      NORMAL_ESS_ID = 1436,
					  AIR_RUNE_ID = 556,
					  AIR_TALISMAN_ID = 1438,
					  AIR_RUINS_ID = 12726,
					  AIR_PORTAL_ID = 10047,
					  AIR_ALTAR_ID = 10060,
					  BANK_BOOTH_ID = 11758,
					  START_XP = Skills.getXP("Runecrafting");
	private final RSTile AIR_PORTAL_TILE = new RSTile(2841, 4828, 0),
						 AIR_ALTAR_TILE = new RSTile(2843, 4832, 0);
	private final RSTile[] AIR_PATH = {new RSTile(3012, 3356, 0), new RSTile(3012, 3358, 0), new RSTile(3010, 3359, 0), new RSTile(3007, 3358, 0),  new RSTile(3006, 3355, 0), new RSTile(3006, 3352, 0), new RSTile(3007, 3349, 0), new RSTile(3007, 3346, 0), new RSTile(3007, 3344, 0), new RSTile(3007, 3341, 0), new RSTile(3007, 3338, 0), new RSTile(3007, 3335, 0), new RSTile(3007, 3332, 0), new RSTile(3006, 3329, 0), new RSTile(3006, 3326, 0), new RSTile(3006, 3323, 0), new RSTile(3005, 3320, 0), new RSTile(3002, 3318, 0), new RSTile(3000, 3315, 0), new RSTile(2999, 3312, 0), new RSTile(2997, 3309, 0), new RSTile(2995, 3307, 0), new RSTile(2993, 3304, 0), new RSTile(2991, 3302, 0), new RSTile(2991, 3299, 0), new RSTile(2991, 3295, 0),new RSTile(2989, 3295, 0), new RSTile(2986, 3295, 0)};
	private final long START_TIME = System.currentTimeMillis();
	
	private JJsAirState STATE = getState();
	private int CURRENT_XP = START_XP;
	
	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("JJ's Air RCer v3", 10, 20);
		g.drawString("Running for: " + Timing.msToString(Timing.timeFromMark(START_TIME)), 10, 40);
		g.drawString("State: " + STATE, 10, 60);
		
		g.drawString("Starting xp: " + START_XP, 10, 100);
		g.drawString("Current xp: " + CURRENT_XP, 10, 120);
		int xpGained = CURRENT_XP-START_XP;
		int xpPerHour = (int) (xpGained / ((System.currentTimeMillis()-START_TIME) / 3600000D));
		g.drawString("Xp gained: " + xpGained + " (" + xpPerHour + "/h)", 10, 140);
	}

	private JJsAirState getState(){
		if(Inventory.find(PURE_ESS_ID).length > 0){
			if(insideAltar()){
				if(atAltar()){
					return JJsAirState.CRAFT_RUNES;
				}else{
					return JJsAirState.WALK_TO_ALTAR;
				}
			}else{
				if(atRuins()){
					return JJsAirState.ENTER_RUINS;
				}else{
					return JJsAirState.WALK_TO_RUINS;
				}
			}
		}else{
			if(atBank()){
				if(Banking.isBankScreenOpen()){
					if(Inventory.find(AIR_RUNE_ID).length > 0){
						return JJsAirState.DEPOSIT_RUNES;
					}else{
						return JJsAirState.WITHDRAW_ESSENCE;
					}
				}else{
					return JJsAirState.OPEN_BANK;
				}
			}else{
				if(insideAltar()){
					if(atPortal()){
						return JJsAirState.ENTER_PORTAL;
					}else{
						return JJsAirState.WALK_TO_PORTAL;
					}
				}else{
					return JJsAirState.WALK_TO_BANK;
				}
			}
		}
	}
	
	private boolean atBank(){
		return Objects.find(7, BANK_BOOTH_ID).length > 0;
	}
	
	private boolean insideAltar(){
		return Objects.find(20, AIR_PORTAL_ID, AIR_ALTAR_ID).length > 0;
	}
	
	private boolean atRuins(){
		return Objects.find(7, AIR_RUINS_ID).length > 0;
	}
	
	private boolean atAltar(){
		return Objects.find(7, AIR_ALTAR_ID).length > 0;
	}
	
	private boolean atPortal(){
		return Objects.find(7, AIR_PORTAL_ID).length > 0;
	}

	private boolean waitBankscreen(int time){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < time){
			if(Banking.isBankScreenOpen()){
				println("Bank is open!");
				return true;
			}
			sleep(50, 150);
		}
		return false;
	}
	
	private boolean openBank(){
		if(Banking.openBankBooth() || Banking.openBankBanker()){
			return waitBankscreen(General.random(2000, 3000));
		}
		return false;
	}
	
	private boolean withdrawEssence(){
		if(Banking.isBankScreenOpen()){
			RSItem[] ess = Banking.find(PURE_ESS_ID);
			if(ess == null || ess.length == 0){
				ess = Banking.find(NORMAL_ESS_ID);
			}
		
			if(ess != null && ess.length > 0 && ess[0].click("Withdraw All")){
				int invCount = Inventory.getAll().length;
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < General.random(2000, 3000)){
					if(Inventory.getAll().length > invCount){
						return true;
					}
					sleep(50, 150);
				}
			}
		}
		
		return false;
	}
	
	private boolean depositRunes(){
		println("Deposit runes");
		if(Banking.isBankScreenOpen()){
			println("Bank is open @ Airs");
			RSItem[] runes = Inventory.find(AIR_RUNE_ID);
			if(runes != null && runes.length > 0 && runes[0].click("Store All")){
				int invCount = Inventory.getAll().length;
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < General.random(2000, 3000)){
					if(Inventory.getAll().length < invCount){
						return true;
					}
					sleep(50, 150);
				}
			}
		}
		
		return false;
	}
	
	private boolean waitEnterRuins(){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < General.random(4000, 5000)){
			if(Objects.findNearest(15, AIR_RUINS_ID).length == 0){
				sleep(1000, 1500);
				return true;
			}
			sleep(50, 150);
			if(Player.isMoving()){
				t = System.currentTimeMillis();
			}
		}
		return false;
	}
	
	private boolean enterRuins(){
		RSItem[] talisman = Inventory.find(AIR_TALISMAN_ID);
		if(talisman != null && talisman.length > 0){
			String uptext = Game.getUptext();
			System.out.println("Uptext: " + uptext);
			if(uptext == null || !uptext.contains("Use Air talisman")){
				talisman[0].click("Use");
			}
			
			RSObject[] ruins = Objects.findNearest(15, AIR_RUINS_ID);
			if(ruins != null && ruins.length > 0 && ruins[0].isOnScreen() && DynamicClicking.clickRSObject(ruins[0], "Use")){
				return waitEnterRuins();
			}
		}else{
			RSObject[] ruins = Objects.findNearest(15, AIR_RUINS_ID);
			if(ruins != null && ruins.length > 0 && ruins[0].isOnScreen() && DynamicClicking.clickRSObject(ruins[0], "Enter")){
				return waitEnterRuins();
			}
		}
		
		return false;
	}
	
	private boolean enterPortal(){
		RSObject[] portal = Objects.findNearest(15, AIR_PORTAL_ID);
		if(portal != null && portal.length > 0 && portal[0].isOnScreen() && DynamicClicking.clickRSObject(portal[0], "Use")){
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(2000, 3000)){
				if(Objects.findNearest(15, AIR_PORTAL_ID).length == 0){
					return true;
				}
				sleep(50, 150);
			}
		}
		return false;
	}
	
	private boolean craftRunes(){
		RSObject[] altar = Objects.findNearest(15, AIR_ALTAR_ID);
		if(altar != null && altar.length > 0 && altar[0].isOnScreen() && DynamicClicking.clickRSObject(altar[0], "Craft")){
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(1500, 2000)){
				if(Inventory.find(PURE_ESS_ID).length == 0 && Player.getAnimation() == -1){
					return true;
				}
				sleep(50, 150);
				if(Player.getAnimation() != -1 || Player.isMoving()){
					t = System.currentTimeMillis();
				}
			}
		}
		return false;
	}
	
	private boolean waitUntilNotMoving(){
		sleep(1000, 1500);
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < General.random(1500, 2500)){
			if(Player.isMoving()){
				t = System.currentTimeMillis();
			}else{
				return true;
			}
		}
		return false;
	}
	
	private boolean walkToTile(RSTile tile){
		tile = new RSTile(tile.getX() + General.random(-2, 2), tile.getY() + General.random(-2, 2), tile.getPlane());
		if(Walking.walkTo(tile)){
			waitUntilNotMoving();
			return true;
		}
		return false;
	}

	private boolean walkPath(RSTile[] tiles, boolean reverse){
		if(reverse){
			RSTile[] reversed = new RSTile[tiles.length];
			for(int i = tiles.length-1; i >= 0; i--){
				reversed[tiles.length-1-i] = tiles[i];
			}
			tiles = reversed;
		}
		
		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
		if(Walking.walkPath(tiles)){
			while(Player.isMoving()){
				sleep(50, 150);
			}
		}
		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
	
		return false;
	}

	@Override
	public JJsAirState getInitialState() {
		Mouse.setSpeed(General.random(135, 145));
	
		if(Math.abs(Camera.getCameraAngle() - 100) > 5){
			Camera.setCameraAngle(100);
		}
		
		if(Math.abs(Camera.getCameraRotation() - 137) > 5){
			Camera.setCameraRotation(137);
		}
		
		return getState();
	}

	@Override
	public JJsAirState handleState(JJsAirState state) {
		STATE = state;
		CURRENT_XP = Skills.getXP("Runecrafting");
		
		switch(STATE){
			case CRAFT_RUNES:
				craftRunes();
				break;
				
			case DEPOSIT_RUNES:
				depositRunes();
				break;
				
			case ENTER_PORTAL:
				enterPortal();
				break;
				
			case ENTER_RUINS:
				enterRuins();
				break;
				
			case OPEN_BANK:
				if(!openBank()){
					walkToTile(AIR_PATH[0]);
				}
				break;
				
			case WALK_TO_ALTAR:
				walkToTile(AIR_ALTAR_TILE);
				break;
				
			case WALK_TO_BANK:
				walkPath(AIR_PATH, true);
				break;
				
			case WALK_TO_PORTAL:
				walkToTile(AIR_PORTAL_TILE);
				break;
				
			case WALK_TO_RUINS:
				walkPath(AIR_PATH, false);
				break;
				
			case WITHDRAW_ESSENCE:
				withdrawEssence();
				break;
		}
		
		return getState();
	}

}
