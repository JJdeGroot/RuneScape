package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.JJsToyMouse07.State;

@ScriptManifest(authors = { "J J" }, category = "Combat", name = "JJ's Goblin Killer 07")
public class JJsGoblin07 extends Script implements Painting {

	private final int[] GOBLIN_IDS = {4407, 4408, 4409};
	private final int FULL_CAKE_ID = 1891,
					  TWO_THIRD_CAKE_ID = 1893,
					  ONE_THIRD_CAKE_ID = 1895,
					  LOBSTER_ID = 379,
					  START_XP = Skills.getXP("Strength");
	private final Rectangle GOBLIN_AREA = new Rectangle(1857, 5213, 20, 20);
	private final int[] CAKE_IDS = {ONE_THIRD_CAKE_ID, TWO_THIRD_CAKE_ID, FULL_CAKE_ID};
	private final int SALMON_ID = 329;
	private final long startTime = System.currentTimeMillis();
	
	private State scriptState;
	private double hpPercentage;
	private int CURRENT_XP = START_XP;
	
	@Override
	public void onPaint(Graphics g) {
		g.setColor(new Color(60, 60, 60));
		g.fillRect(4, 35, 190, 100);
		
		long runTime = System.currentTimeMillis() - startTime;
		int xpGained = CURRENT_XP-START_XP;
		int xpPerHour = (int) (xpGained / (runTime / 3600000.0));
	
		g.setColor(Color.WHITE);
		g.drawString("JJ's Goblin Killer", 5, 50);
		g.drawString("Running for: " + Timing.msToString(runTime), 5, 70);
		g.drawString("Script state: " + scriptState, 5, 90);
		g.drawString("Str xp gained: " + xpGained + " (" + xpPerHour + "/h)", 5, 110);
		g.drawString("Health percentage: " + (int) hpPercentage, 5, 130);
	}
	
	public enum State {
		ATTACK_MONSTER, IN_COMBAT, EAT_FOOD;
	}
	
	private State getState(){
		if(hpPercentage < General.random(30, 40)){
			return State.EAT_FOOD;
		}else{
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(2000, 2500)){
				if(Player.getAnimation() == 390){
					return State.IN_COMBAT;
				}
			}
			return State.ATTACK_MONSTER;
		}
	}
	
	private boolean inRect(int x, int y, Rectangle r){
		return x >= r.x && y >= r.y && x <= r.x+r.width && y <= r.y+r.height;
	}

	private double getHpPercentage(){
		return Skills.getCurrentLevel("Hitpoints") * 1.0 / Skills.getActualLevel("Hitpoints") * 100;
	}
	
	private boolean attackMinotaur(){
		RSNPC[] npcs = NPCs.findNearest(GOBLIN_IDS);
		ArrayList<RSNPC> goblinList = new ArrayList<RSNPC>();
		for(RSNPC npc : npcs){
			for(int id : GOBLIN_IDS){
				if(npc.getID() == id){
					RSTile loc = npc.getPosition();
					if(inRect(loc.getX(), loc.getY(), GOBLIN_AREA)){
						if(!npc.isInCombat()){
							goblinList.add(npc);
						}
					}
				}
			}
		}
		
		if(goblinList.size() > 0){
			RSNPC goblin = goblinList.get(0);
			
			// CHECKING IF WE NEED TO WALK
			if(!goblin.isOnScreen()){
				if(Walking.walkTo(goblin.getPosition())){
					sleep(1000, 2000);
					long t = System.currentTimeMillis();
					while(Timing.timeFromMark(t) < General.random(1500, 2000)){
						if(Player.isMoving()){
							t = System.currentTimeMillis();
						}else{
							sleep(1500, 2000);
							break;
						}
						sleep(50, 150);
					}
				}
				
			}
			
			// ATTACKING
			if(DynamicClicking.clickRSNPC(goblin, "Attack")){
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < General.random(2000, 3000)){
					if(Player.getRSPlayer().isInCombat()){
						return true;
					}
					sleep(50, 150);
					if(Player.isMoving() || Player.getAnimation() != -1){
						t = System.currentTimeMillis();
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean eatFood(){
		RSItem[] food = Inventory.find(LOBSTER_ID );
		if(food != null && food.length > 0){
			int r = General.random(0, food.length-1);
			if(food[r].click("Eat")){
				int startHP = Skills.getCurrentLevel("Hitpoints");
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < General.random(2000, 3000)){
					int currentHP = Skills.getCurrentLevel("Hitpoints");
					if(currentHP > startHP){
						return true;
					}
					sleep(200, 400);
				}
			}
		}
		return false;
	}

	@Override
	public void run() {
		Mouse.setSpeed(150);
		
		while(true){
			CURRENT_XP = Skills.getXP("Strength");
			hpPercentage = getHpPercentage();
			scriptState = getState();
		
			switch(scriptState){
			
				case ATTACK_MONSTER:
					attackMinotaur();
					break;
					
				case EAT_FOOD:
					eatFood();
					break;
					
				case IN_COMBAT:
					sleep(50, 100);
					break;
				
			}
			
			sleep(50, 100);
		}
		
	}

	
	
}
