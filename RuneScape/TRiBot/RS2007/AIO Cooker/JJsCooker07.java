package scripts;

import java.awt.Color;
import java.awt.Graphics;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
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

import scripts.jjscooker07.GUI;

@ScriptManifest(authors = { "J J" }, category = "Cooking", name = "JJ's AIO Cooker")
public class JJsCooker07 extends Script implements Painting {

	private final int BANKER_ID = 2271,
					  FIRE_ID = 2732,
					  COOKING_ID = 897,
					  START_XP = Skills.getXP("Cooking");
	private final long START_TIME = System.currentTimeMillis();
	
	private GUI gui;	
	private boolean guiInitialized = false,
			        guiFinished = false;
	private int RAW_FOOD_ID = -1,
				CURRENT_XP = START_XP;
	private State SCRIPT_STATE = State.GUI;
	
	@Override
	public void onPaint(Graphics g) {
		long runtime = System.currentTimeMillis()-START_TIME;
		int xpGained = CURRENT_XP-START_XP;
		int xpPerHour = (int) (xpGained / (runtime / 3600000D));
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's AIO Cooker", 5, 50);
		g.drawString("Running for: " + Timing.msToString(runtime), 5, 70);
		g.drawString("State: " + SCRIPT_STATE, 5, 90);
		g.drawString("Xp gained: " + xpGained + " (" + xpPerHour + "/h)", 5, 110);
		g.drawString("Selected food ID: " + RAW_FOOD_ID, 5, 130);
	}
	
	public enum State {
		GUI, WALK_TO_BANKER, OPEN_BANK, DEPOSIT_ALL,
		WITHDRAW_RAW_FOOD, WALK_TO_FIRE, CLOSE_BANK,
		SELECT_FOOD, USE_FOOD_ON_FIRE, COOKING, SELECT_COOK_ALL;
	}
	
	public State getState(){
		if(!guiFinished){
			return State.GUI;
		}else{
			if(Inventory.find(RAW_FOOD_ID).length > 0){
				if(Banking.isBankScreenOpen()){
					return State.CLOSE_BANK;
				}else{
					if(haveCookAllScreen()){
						return State.SELECT_COOK_ALL;
					}else{
						if(Objects.find(8, FIRE_ID).length > 0){
							if(areCooking()){
								return State.COOKING;
							}else{
								if(uptextHasUse()){
									return State.USE_FOOD_ON_FIRE;
								}else{
									return State.SELECT_FOOD;
								}
							}
						}else{
							return State.WALK_TO_FIRE;
						}
					}
				}
			}else{
				if(Banking.isBankScreenOpen()){
					if(invHasJunk()){
						return State.DEPOSIT_ALL;
					}else{
						return State.WITHDRAW_RAW_FOOD;
					}
				}else{
					if(nearBanker()){
						return State.OPEN_BANK;
					}else{
						return State.WALK_TO_BANKER;
					}
				}
			}
		}
	}
	
	private boolean uptextHasUse(){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < General.random(200, 400)){
			String[] options = ChooseOption.getOptions();
			if(options != null && options.length == 1 && options[0].contains("Cancel")){
				println("Option equals cancel");
				return true;
			}
			/*
			String uptext = Game.getUptext();
			println("Uptext: " + uptext);
			if(uptext != null && uptext.contains("Use")){
				println("Uptext contains Use!");
				return true;
			}
			*/
			sleep(20, 40);
		}
		return false;
	}
	
	private boolean haveCookAllScreen(){
		return Interfaces.get(307, 3) != null;
	}
	
	private boolean nearBanker(){
		RSNPC[] bankers = NPCs.findNearest(BANKER_ID);
		if(bankers != null && bankers.length > 0){
			return bankers[0].getPosition().distanceTo(Player.getPosition()) < 4;
		}
		return false;
	}
	
	private boolean invHasJunk(){
		RSItem[] all = Inventory.getAll();
		if(all != null && all.length > 0)
			for(RSItem item : all)
				if(item.getID() != RAW_FOOD_ID)
					return true;
		return false;
	}
	
	private boolean areCooking(){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < General.random(1200, 1600)){
			if(Player.getAnimation() == COOKING_ID){
				return true;
			}
		}
		return false;
	}
	
	private boolean clickFood(){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < General.random(2000, 3000)){
			RSItem[] raw = Inventory.find(RAW_FOOD_ID);
			if(raw != null && raw.length > 0){
				int r = General.random(0, raw.length-1);
				if(raw[r].hover()){
					sleep(50, 200);
					Mouse.click(1);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean useFoodOnFire(){
		RSObject[] fire = Objects.find(10, FIRE_ID);
		if(fire != null && fire.length > 0 && DynamicClicking.clickRSObject(fire[0], 3)){
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(500, 1000)){
				if(ChooseOption.isOptionValid("-> Fire") && ChooseOption.select("-> Fire")){
					break;
				}
				sleep(40, 80);
			}

			long tt = System.currentTimeMillis();
			while(Timing.timeFromMark(tt) < General.random(1500, 2500)){
				if(haveCookAllScreen()){
					return true;
				}
				sleep(40, 80);
				if(Player.isMoving()){
					t = System.currentTimeMillis();
				}
			}
		}
		
		return false;
	}
	
	private boolean openBank(){
		RSNPC[] bankers = NPCs.find(BANKER_ID);
		if(bankers != null && bankers.length > 0 && bankers[0].isOnScreen() && DynamicClicking.clickRSNPC(bankers[0], "Bank")){
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(1500, 2500)){
				if(Banking.isBankScreenOpen()){
					return true;
				}
				sleep(50, 150);
				if(Player.isMoving()){
					t = System.currentTimeMillis();
				}
			}
		}
		return false;
	}
	
	private boolean selectCookAll(){
		RSInterfaceChild cook = Interfaces.get(307, 3);
		if(cook != null && !cook.isHidden() && cook.click("Cook All")){
			sleep(500, 1500);
			return true;
		}
		return false;
	}
	
	private boolean depositJunk(){
		RSItem[] all = Inventory.getAll();
		if(all != null && all.length > 0)
			for(RSItem item : all)
				if(item.getID() != RAW_FOOD_ID){
					int count = Inventory.getAll().length;
					if(item.click("Store All")){
						long t = System.currentTimeMillis();
						while(Timing.timeFromMark(t) < General.random(2000, 3000)){
							if(Inventory.getAll().length < count){
								return true;
							}
							sleep(50, 150);
						}
						return true;
					}
				}
		return false;
	}
	
	private boolean withdrawFood(){
		RSItem[] food = Banking.find(RAW_FOOD_ID);
		int count = Inventory.getAll().length;
		if(food != null && food.length > 0 && food[0].click("Withdraw All")){
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(2000, 3000)){
				if(Inventory.getAll().length > count){
					return true;
				}
				sleep(50, 150);
			}
		}

		return false;
	}
	
	private boolean walkToTile(RSTile tile){
		tile = new RSTile(tile.getX() + General.random(-2, 2), tile.getY() + General.random(-2, 2));
		if(Walking.walkTo(tile)){
			Camera.turnToTile(tile);
			while(Player.isMoving()){
				if(Player.getPosition().distanceTo(tile) <= 1){
					return true;
				}
				sleep(50, 150);
			}
			return true;
		}
		return false;
	}
	
	private boolean walkToFire(){
		RSObject[] fire = Objects.find(20, FIRE_ID);
		if(fire != null && fire.length > 0){
			return walkToTile(fire[0].getPosition());
		}
		return false;
	}
	
	private boolean walkToBanker(){
		RSNPC[] banker = NPCs.find(20, BANKER_ID);
		if(banker != null && banker.length > 0){
			return walkToTile(banker[0].getPosition());
		}
		return false; 
	}	
	
	public void onStart(){
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				gui = new GUI();
				gui.setVisible(true);
				guiInitialized = true;
			}
    	});


		while(!guiFinished){
			if(guiInitialized && gui.isStartPressed()){
				break;
			}
			sleep(100, 200);
		}

		RAW_FOOD_ID = gui.getID();
		println("Selected food ID: " + RAW_FOOD_ID);
		guiFinished = true;
		
	}
	
	@Override
	public void run() {
		println("JJ's Cooker started");
		Mouse.setSpeed(General.random(130, 140));
		onStart();

		while(true){
			SCRIPT_STATE = getState();
			CURRENT_XP = Skills.getXP("Cooking");
			
			switch(SCRIPT_STATE){
				case SELECT_FOOD:
					clickFood();
					break;
			
				case COOKING:
					sleep(100, 900);
					break;
					
				case DEPOSIT_ALL:
					depositJunk();
					break;
					
				case OPEN_BANK:
					openBank();
					break;
					
				case USE_FOOD_ON_FIRE:
					useFoodOnFire();
					break;
					
				case WALK_TO_BANKER:
					walkToBanker();
					break;
					
				case WALK_TO_FIRE:
					walkToFire();
					break;
					
				case WITHDRAW_RAW_FOOD:
					withdrawFood();
					break;
					
				case SELECT_COOK_ALL:
					selectCookAll();
					break;
					
				case GUI:
					sleep(50, 100);
					break;
					
				case CLOSE_BANK:
					Banking.close();
					break;
			}
			
			sleep(20, 60);
		}

	}

}
