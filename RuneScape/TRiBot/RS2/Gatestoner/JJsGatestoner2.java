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
import org.tribot.api.NPCChat;
import org.tribot.api.Player;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Text;
import org.tribot.api.Timing;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.TextChar;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Magic", name = "JJ's Gatestoner")
public class JJsGatestoner2 extends Script implements Painting{
	final int stoneID = 258812;
	final long stairsID = 3559546211L, dungeonID = 2365910824L, startTime = System.currentTimeMillis();
	final double gateStoneXp = 43.5;
	int gateStone = 0, xpHr = 0, fix = 0;
	boolean repeat = true;
		
	public void onPaint(Graphics g){
		g.setColor(new Color(8, 72, 0));
        g.fill3DRect(0, 321, 275, 67, true);
        
        g.setColor(new Color(253, 238, 0));
        g.setFont(new Font ("Verdana", Font.BOLD, 11));
        g.drawString("JJ's Gatestoner V2", 5, 336);
        
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
		 	sleep(200, 300);
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
	

	private boolean waitItem(int itemID, int maxTime){
		long Q = System.currentTimeMillis();
		long max = Q + randomRange((int)(maxTime*0.9), (int)(maxTime*1.1));
		while (System.currentTimeMillis() < max){
			if (Inventory.find(itemID).length > 0){
				return true;
			}
		}
		return false;
	}
	
	private boolean dropGatestone(){
		FTab(GameTab.TABS.INVENTORY);
		fixOption();
		
		if (waitItem(stoneID, 1000)){		
			InventoryItem[] gatestone = Inventory.find(stoneID);
			if (gatestone.length > 0){
				gatestone[0].click("Drop");
				return true;
			}
		}
		
		return false;
	}
	
	private void setupSpellbook(){
		Point combat = new Point(595, 491), teleport = new Point(614, 491), misc = new Point(637, 491), 
				skill = new Point(656, 491), combatSpells = new Point(703, 491);
		Point[] enabled = {combat, teleport, misc, skill};

		for (int i = 0; i < enabled.length; i++){
			ColourPoint[] C = Screen.findColours(new Color(255, 255, 255), enabled[i].x, enabled[i].y, enabled[i].x, enabled[i].y, new Tolerance(15));
			if (C.length > 0){
				if (i == 1 || i == 3){
					continue;
				}
				Point MP = new Point(585 + (i*21) + randomRange(0, 15), 495 + randomRange(0, 10));
				Mouse.move(MP);
				if (Timing.waitUptext("Filter", 1000)){
					Mouse.click(1);
					sleep(1000, 1500);
				}
			}else{
				if (i == 0 || i == 2){
					continue;
				}
				Point MP = new Point(585 + (i*21) + randomRange(0, 15), 495 + randomRange(0, 10));
				Mouse.move(MP);
				if (Timing.waitUptext("Filter", 1000)){
					Mouse.click(1);
					sleep(1000, 1500);
				}
			}
		}
		
		ColourPoint[] D = Screen.findColours(new Color(255, 255, 255), combatSpells.x, combatSpells.y, combatSpells.x, combatSpells.y, new Tolerance(15));
		if (D.length < 1){
			Mouse.move(randomRange(700, 710), randomRange(497, 507));
			if (Timing.waitUptext("Sort", 1000)){
				Mouse.click(1);
				sleep(1000, 1500);
			}
		}
	}
		
	private boolean makeGatestone(){
		FTab(GameTab.TABS.MAGIC);
		fixOption();
		
		int x1 = 610, y1 = 268, x2 = 630, y2 = 289;
		ColourPoint[] CP = Screen.findColours(new Color(28, 235, 199), x1, y1, x2, y2, new Tolerance(15));
		
		if (CP.length > 0){
			Point GP = new Point(randomRange(x1, x2), randomRange(y1, y2));
			Mouse.click(GP, 3);
			if (Timing.waitChooseOption("Quick", 500)){
				gateStone++;
				xpHr = (int)((gateStone * gateStoneXp) * 3600000 / (System.currentTimeMillis() - startTime));
				sleep(100, 200);
				return true;
			}
				
		}else{
			setupSpellbook();
		}

		return false;
	}
	
	private boolean randomSelectOptionAt(Point coord, String uptext, String option, int leftChance, int rightChance, int maxWait){
    	int totalChance = leftChance + rightChance;
    	Mouse.move(coord);
    	if (Timing.waitUptext(uptext, (maxWait/2))){
        	if ((new Random().nextInt(totalChance)) < leftChance){
        		Mouse.click(coord, 1);
        		return true;
        	}else{
        		Mouse.click(coord, 3);
        		if (Timing.waitChooseOption(option, (maxWait/2))){
        			ChooseOption.select(option);
        			return true;
        		}else{
        			return false;
        		}
        	}
    	}else{
    		return false;
    	}
    }
	
	private void waitUntilNotMoving(){
        /* Player.isMoving() does not work correctly right now.
        sleep(200, 400);
		while (Player.isMoving()){
			sleep(500, 1000);
		}
		*/
		sleep(4000, 5000);
    }
	
	private boolean leaveDungeon(){
		Point DP = new Point(randomRange(617, 638), randomRange(225, 250)); // Dung tab
		Mouse.move(DP);
		if (Timing.waitUptext("Party", 500)){
			Mouse.click(DP, 1);
			sleep(250, 500);
		
			Point LP = new Point(randomRange(555, 728), randomRange(369, 394));
			Mouse.move(LP);
			if (Timing.waitUptext("Leave-party", 500)){
				Mouse.click(LP, 1);
				sleep(500, 1000);

				NPCChat.clickContinue(true);
				
				String[] options = NPCChat.getOptions();
					if (options.length > 0)
						for (int i = 0; i < options.length; i++)
							if (options[i].contains("Yes")){
								NPCChat.selectOption("Yes", true);
								sleep(2500, 3500);
								return true;
							}
			}
		}
		return false;
	}
	
	private boolean toStairs(){
		int lowest = 2000;
		Point SP = new Point(0, 0);
		ColourPoint[] upstairs = Screen.findColours(new Color(65, 65, 65), 585, 110, 650, 160, new Tolerance(10));
		
		if (upstairs.length > 0){
			for (int i = 0; i < upstairs.length; i++){
				int sum = upstairs[i].point.x + upstairs[i].point.y;
				if (sum < lowest){
					lowest = sum;
					SP = new Point(upstairs[i].point.x + randomRange(-5, 5), upstairs[i].point.y + randomRange(-5, 5));
				}
			}
			
			if (SP != new Point(0, 0)){
				Mouse.move(SP);
				Mouse.click(1);
				waitUntilNotMoving();
				return true;
			}
		}
		return false;
	}
	
	private boolean climbStairs(){
		ScreenModel[] stairs = ScreenModels.find(stairsID);
		if (stairs.length > 0){
			Point SP = new Point(stairs[0].points[0].x + randomRange(10, 15), stairs[0].points[0].y - randomRange(5, 10));
			if (randomSelectOptionAt(SP, "Jump-down", "Jump-down", 5, 2, 1000)){
				waitUntilNotMoving();
				return true;
			}
		}
		return false;
	}
	
	private boolean enterDungeon(){
		ScreenModel[] entrance = ScreenModels.find(dungeonID);
		if (entrance.length > 0){
			Point DP = new Point(entrance[0].base_point.x + randomRange(-5, 5), entrance[0].base_point.y + randomRange(-5, 5));
			if (randomSelectOptionAt(DP, "Climb-down", "Climb-down", 5, 2, 1000)){
				waitUntilNotMoving();
				sleep(1000, 2000);
				return true;
			}
		}
		return false;
	}
	
	private boolean needParty(){
		String[] text = NPCChat.getOptions();
		if (text.length > 0)
			for (int i = 0; i < text.length; i++)
				if (text[i].contains("must be in a party"))
					return true;
		return false;
	}
	
	private boolean makeParty(){
		NPCChat.clickContinue(true);
		String name = NPCChat.getName();
		if (name.contains("start")){
			String[] options = NPCChat.getOptions();
			if (options.length > 0)
				for (int i = 0; i < options.length; i++)
					if (options[i].contains("Yes")){
						NPCChat.selectOption("Yes", true);
						sleep(2000, 2500);
						return true;
					}
		}
		return false;
	}
	
	private boolean selectFloor(){
		TextChar[] text = Text.findCharsInArea(78, 328, 45, 12, true);
		String sentence = "";
		if (text.length > 0){
			for (int i = 0; i < text.length; i++)
				sentence = sentence + String.valueOf(text[i].character);
			//println(sentence);
			if (sentence.contains("oor")){
				Point CP = new Point(randomRange(177, 243), randomRange(322, 344));
				if (randomSelectOptionAt(CP, "Confirm", "Confirm", 3, 1, 1000)){
					sleep(1500, 2000);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean selectComplexity(int complexity){
		TextChar[] text = Text.findCharsInArea(202, 59, 114, 12, true);
		String sentence = "";
		if (text.length > 0){
			for (int i = 0; i < text.length; i++)
				sentence = sentence + String.valueOf(text[i].character);
			//println(sentence);
			if (sentence.contains("lex")){
				Point CP = new Point(randomRange(37 + ((complexity-1) * 38), 46 + ((complexity-1) * 38)), randomRange(97, 104));
				Mouse.move(CP);
				if (Timing.waitUptext("complexity", 500)){
					Mouse.click(1);
					sleep(500, 1000);
					Point FP = new Point(randomRange(177, 243), randomRange(322, 344));
					if (randomSelectOptionAt(FP, "Confirm", "Confirm", 3, 1, 1000)){
						sleep(2500, 3000);
						return true;
					}
						
				}
			}
		}
		return false;
	}
	
	private boolean startNewFloor(){
		if (leaveDungeon()){
			if (toStairs()){
				if (climbStairs()){
					if (enterDungeon()){
						if (needParty()){
							if (makeParty()){
								if (!enterDungeon()){
									println("STOP!!");
								}
							}
						}
									
						if (selectFloor()){
							if (selectComplexity(4)){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public void onStart() {
		println("Thanks for using JJ's Gatestoner.");
		println("If you have any problems you can post them in this topic: http://tribot.org/forums/showthread.php?tid=372");
		Mouse.setSpeed(120);
    }
	
	public void onStop() {
        println("Thanks for using JJ's Gatestoner");
        println("Script has ran for: " + Timing.msToString(System.currentTimeMillis() - startTime));
        println("We have made " + gateStone + " gatestones for " + (gateStoneXp * gateStone) + " experience");
        println("That is " + xpHr + " magic experience per hour!");
    }
	
	public void setupMessage(){
		println("You haven't set up TRiBot properly");
		println("Click on Bot Debug and place the files in the correct destinations");
		println("Then restart TRiBot and select OpenGL Mode");
		println("Once you have done that, you can succesfully run the script!");
	}

	@Override
    public void run() {
		onStart();
		if (enterDungeon()){
			if (needParty()){
				if (makeParty())
					if (!enterDungeon()){
						println("Error, we didn't make a party properly. Leave the party and restart the script!");
					}
			}
			if (selectFloor()){
				if (selectComplexity(4)){
					while (repeat){
						while (makeGatestone()){
				    		dropGatestone();
				    	}
						if (startNewFloor()){
							repeat = true;
						}else{
							repeat = false;
						}
					}
				}
			}
		}else{
			setupMessage();
		}
		onStop();
    }
}