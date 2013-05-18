package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.tribot.api.EGW;
import org.tribot.api.Game;
import org.tribot.api.GameTab;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.Inventory;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.Text;
import org.tribot.api.types.TextChar;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Combat", name = "JJ's Fighter")
public class JJsFighter extends Script implements Painting {
	final long spiderID = 1957548091L,
			   startTime = System.currentTimeMillis();
	final String NoTarget = "NooTaarrgeet";
	final int maxLP = 4400,
			  xpPerKill = 129;
			
	int foodID = 153184,
	    kills = 0;
	
	// Draws the progress report on the screen
	public void onPaint(Graphics g){
		g.setColor(new Color(92, 92, 92));
		g.fill3DRect(5, 75, 150, 80, true);
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's Fighter", 10, 90);
		g.drawString("Running for " + Timing.msToString(System.currentTimeMillis() - startTime), 10, 110);
		g.drawString("Kills: " + kills, 10, 130);
		g.drawString("Xp gained: " + (kills*xpPerKill), 10, 150);
	}
	
	// Returns a boolean whether we attacked a spider or not
	private boolean attackSpider(){
		ScreenModel[] spiders = ScreenModels.findNearest(spiderID);
		if(spiders.length > 0){
			Point P = new Point(spiders[0].base_point.x, spiders[0].base_point.y);
			Mouse.move(P);
			if(Timing.waitUptext("Giant Spider", 200)){
				Mouse.click(1);
				return true;
			}
		}
		
		return false;
	}
	
	// Returns a boolean whether we are in combat or not
	private boolean inCombat(){
		if(GameTab.getOpen() != TABS.COMBAT){
			GameTab.open(TABS.COMBAT);
			sleep(500, 1000);
		}
		
		TextChar[] text = Text.findCharsInArea(597, 271, 60, 10, true);
		String s = "";
		for(int i = 0; i < text.length; i++){
			s = s + text[i].character;
		}
		return !s.equals(NoTarget);
	}
	
	// Waits until we are not moving anymore
	private void waitUntilNotMoving(){
		boolean moving = true;
		sleep(1000, 1500);	
		
		while(moving){
			Point startPos = EGW.getPosition();
			sleep(400, 600);
			Point curPos = EGW.getPosition();

			double difference = Math.abs(startPos.x-curPos.x) + Math.abs(startPos.y-curPos.y);
			if(difference < 1){
				moving = false;
			}
		}
		
		//println("We have stopped moving");
	}

	// Returns true if we have less than 25% of our maximum lifepoints
	private boolean lowHealth(){
		int lp = Game.getHitpoints();
		double percentage = (double) lp / maxLP * 100;

		//println(lp);
		//println(hpLvl);
		//println(percentage);
		
		return percentage < 25;
	}
	
	// Eats food, returns false if we are out of food
	private boolean eatFood(){
		if(GameTab.getOpen() != TABS.INVENTORY){
			GameTab.open(TABS.INVENTORY);
			sleep(500, 1000);
		}
		
		InventoryItem[] food = Inventory.find(foodID);
		if(food.length > 0){
			Point P = new Point(food[0].x+15, food[0].y+15);
			Mouse.move(P);
			Mouse.click(1);
			sleep(1500, 2000);
			return true;
		}else{
			InventoryItem[] all = Inventory.getAll();
			for(int i = 0; i < all.length; i++){
				Point T = new Point(all[i].x+15, all[i].y+15);
				Mouse.move(T);
				if(Timing.waitUptext("Eat", 250)){
					foodID = all[i].id;
					Mouse.click(1);
					sleep(1500, 2000);
					return true;
				}
			}
		}
	
		return false;
	}
	
	// Checks if we are still logged in
	private boolean areLoggedIn(){
		return ScreenModels.getAll().length > 0;
	}

	@Override
	public void run() {
		final JJsFighterGUI g = new JJsFighterGUI();
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				g.makeGUI();
			}
    	});
		while(!g.isFinished()){
			sleep(200, 400);
		}

		
		
		/*
		boolean haveFood = true;
		Mouse.setSpeed(130);
		
		while(areLoggedIn() && haveFood){
			if(attackSpider()){
				waitUntilNotMoving();
				long t = System.currentTimeMillis();
				while(inCombat() && Timing.timeFromMark(t) < 30000){
					if(lowHealth()){
						if(!eatFood()){
							println("Out of food, stopping script");
							haveFood = false;
							break;
						}
						attackSpider();
					}
					sleep(200, 400);
				}
				sleep(2500, 3000);
				kills++;
			}
		}
		*/
		
	}
}