package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Random;

import org.tribot.api.GameTab.TABS;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.ChooseOption;
import org.tribot.api.GameTab;
import org.tribot.api.Inventory;
import org.tribot.api.Player;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Magic", name = "JJ's Gatestoner")
public class JJsGatestoner extends Script implements Painting{
	final int stoneID = 258812;
	final long stairsID = 3559546211L, dungeonID = 2365910824L, startTime = System.currentTimeMillis();
	final double gateStoneXp = 43.5;
	int gateStone = 0, xpHr = 0;
		
	public void onPaint(Graphics g){
		g.setColor(new Color(8, 72, 0));
        g.fill3DRect(0, 321, 275, 67, true);
        
        g.setColor(new Color(253, 238, 0));
        g.setFont(new Font ("Verdana", Font.BOLD, 11));
        g.drawString("JJ's Gatestoner", 5, 336);
        
        g.setFont(new Font ("Verdana", Font.PLAIN, 11));
        g.setColor(new Color(87, 247, 0));
        g.drawString("Time running: " + Timing.msToString(System.currentTimeMillis() - startTime), 5, 351);
        g.drawString("Made " + gateStone + " gatestones for " + (gateStoneXp * gateStone) + " experience", 5, 366);
        g.drawString("That is " + xpHr + " magic experience per hour", 5, 381);
    }
	
	private boolean FTab(TABS Tab){
		char FKey;

		switch(Tab){
			case INVENTORY: FKey = (char)KeyEvent.VK_F1; break;
			case EQUIPMENT: FKey = (char)KeyEvent.VK_F2; break;
			case PRAYERS: FKey = (char)KeyEvent.VK_F3; break;
			case MAGIC: FKey = (char)KeyEvent.VK_F4; break;
			case COMBAT: FKey = (char)KeyEvent.VK_F5; break;
			default: return false;
		}
		
		if (Character.isDefined(FKey)){
			Keyboard.pressKey(FKey);
		 	sleep(100, 200);
		 	Keyboard.releaseKey(FKey);
		 	sleep(50, 150);
		 	return true;
		}
		
		return false;
	}
	
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	private void fixOption(){
    	while (ChooseOption.getPosition() != null){
    		Point MP = new Point(Mouse.getPos());
    		Mouse.move(MP.x + randomRange(-50, 50), MP.y + randomRange(-50, 50));
    	}
	}
	
	private boolean dropGatestone(){
		FTab(GameTab.TABS.INVENTORY);
		fixOption();
		
		InventoryItem[] gatestone = Inventory.find(stoneID);
		if (gatestone.length > 1){
			for (int i = 0; i < gatestone.length; i++){
				if (gatestone[i].avg_g >= 28){
					gatestone[i].click("Drop");
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean makeGatestone(){
		FTab(GameTab.TABS.MAGIC);
		fixOption();
		
		int x1 = 659, y1 = 269, x2 = 677, y2 = 288;
		ColourPoint[] CP = Screen.findColours(new Color(28, 235, 199), x1, y1, x2, y2, new Tolerance(15));
		
		if (CP.length > 0){
			Point GP = new Point(randomRange(x1, x2), randomRange(y1, y2));
			Mouse.click(GP, 3);
			if (Timing.waitChooseOption("Quick", 500)){
				gateStone++;
				xpHr = (int)((gateStone * gateStoneXp) * 3600000 / (System.currentTimeMillis() - startTime));
				return true;
			}
				
		}

		return false;
	}
	
	private boolean leaveDungeon(){
		// Dung tab, using quest tab does not work well
		Point DP = new Point(randomRange(617, 638), randomRange(225, 250));
		Mouse.move(DP);
		if (Timing.waitUptext("Party", 500)){
			Mouse.click(DP, 1);
			sleep(500, 1000);
			
			Point LP = new Point(randomRange(555, 728), randomRange(369, 394));
			Mouse.move(LP);
			if (Timing.waitUptext("Leave-party", 500)){
				Mouse.click(LP, 1);
				sleep(1500, 2500);
				
				Keyboard.pressKey((char)KeyEvent.VK_SPACE);
			 	sleep(50, 150);
			 	Keyboard.releaseKey((char)KeyEvent.VK_SPACE);
			 	sleep(1000, 1500);
			 	
			 	Keyboard.typeString("1");
			 	sleep(2000, 3000);

				return true;
			}
		}

		return false;
	}
	
	private boolean climbStairs(){
		ScreenModel[] stairs = ScreenModels.find(stairsID);
		if (stairs.length > 0){
			Point SP = new Point(stairs[0].points[0].x + randomRange(5, 25), stairs[0].points[0].y + randomRange(-5, 5));
			Mouse.move(SP);
			if (Timing.waitUptext("Jump-down", 500)){
				Mouse.click(SP, 1);
				sleep(250, 500);
				while (Player.isMoving())
					sleep(100, 300);
				sleep(1000, 1500);
				return true;
			}
		}else{
			Point MP = new Point(randomRange(590, 610), randomRange(120, 140));
			Mouse.move(MP);
			Mouse.click(MP, 1);
			sleep(250, 500);
			while (Player.isMoving())
				sleep(100, 300);
			if (climbStairs())
				return true;
		}

		
		return false;
	}
	
	private boolean enterDungeon(){
		ScreenModel[] dungeon = ScreenModels.find(dungeonID);
		if (dungeon.length > 0){
			Point DP = new Point(dungeon[0].base_point.x + randomRange(-10, 10), dungeon[0].base_point.y + randomRange(-10, 10));
			Mouse.move(DP);
			if (Timing.waitUptext("Climb-down", 500)){
				Mouse.click(DP, 1);
				sleep(250, 500);
				while (Player.isMoving())
					sleep(100, 300);
				return true;
			}
		}
		
		return false;
	}
	
	private void makeParty(){
		Keyboard.pressKey((char)KeyEvent.VK_SPACE);
	 	sleep(50, 150);
	 	Keyboard.releaseKey((char)KeyEvent.VK_SPACE);
	 	sleep(1000, 1500);
	 	
	 	Keyboard.typeString("1");
	 	sleep(2000, 3000);
	}
	
	private boolean selectFloor(){
		Point CP = new Point(randomRange(175, 244), randomRange(320, 344));
		Mouse.move(CP);
		if (Timing.waitUptext("Confirm", 500)){
			Mouse.click(CP, 1);
			sleep(1000, 1500);
			
			CP = new Point(randomRange(175, 244), randomRange(320, 344));
			Mouse.move(CP);
			if (Timing.waitUptext("Confirm", 500)){
				Mouse.click(CP, 1);
				sleep(1000, 1500);
				
				Keyboard.typeString("1");
				sleep(2000, 3000);
				
				return true;
			}
		}
		return false;
	}
	
	private boolean exitDungeon(){
		if (leaveDungeon())
			if (climbStairs())
				return true;
		return false;
	}
	
	private boolean newDungeon(){
		if (enterDungeon()){
			makeParty();
			if (enterDungeon())
				if (selectFloor())
					return true;
			
		}
		return false;
	}
	

	@Override
    public void run() {
		Mouse.setSpeed(110);
    	while (newDungeon()){
	    	while (makeGatestone()){
	    		dropGatestone();
	    	}
	    	if (!exitDungeon())
	    		stopScript();
	    }
    }
}