package scripts;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Random;

import org.tribot.api.Minimap;
import org.tribot.api.NPCChat;
import org.tribot.api.ChooseOption;
import org.tribot.api.Player;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Text;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.TextChar;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Test", name = "Dung Test")
public class NewDung extends Script{
	final long stairsID = 3559546211L, dungeonID = 2365910824L;
	
	public int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
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
        sleep(500, 750);
		while (Player.isMoving()){
			sleep(50, 100);
		}
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
								sleep(2000, 3000);
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
				sleep(300, 600);
				return true;
			}
		}
		return false;
	}
	
	private boolean climbStairs(){
		ScreenModel[] stairs = ScreenModels.find(stairsID);
		if (stairs.length > 0){
			Point SP = new Point(stairs[0].points[0].x + randomRange(10, 20), stairs[0].points[0].y - randomRange(5, 15));
			if (randomSelectOptionAt(SP, "Jump-down", "Jump-down", 5, 2, 1000)){
				waitUntilNotMoving();
				sleep(500, 1000);
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
						sleep(1000, 1500);
						return true;
					}
		}
		return false;
	}
	
	private boolean selectFloor(){
		TextChar[] text = Text.findCharsInArea(221, 59, 80, 12, true);
		String sentence = "";
		if (text.length > 0){
			for (int i = 0; i < text.length; i++)
				sentence = sentence + String.valueOf(text[i].character);
			println(sentence);
			if (sentence.contains("oor")){
				Point CP = new Point(randomRange(177, 243), randomRange(322, 344));
				if (randomSelectOptionAt(CP, "Confirm", "Confirm", 3, 1, 1000)){
					sleep(1000, 1500);
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
			println(sentence);
			if (sentence.contains("lex")){
				Point CP = new Point(randomRange(37 + ((complexity-1) * 38), 46 + ((complexity-1) * 38)), randomRange(97, 104));
				Mouse.move(CP);
				if (Timing.waitUptext("complexity", 500)){
					Mouse.click(1);
					sleep(500, 1000);
					Point FP = new Point(randomRange(177, 243), randomRange(322, 344));
					if (randomSelectOptionAt(FP, "Confirm", "Confirm", 3, 1, 1000)){
						sleep(2000, 3000);
						return true;
					}
						
				}
			}
		}
		return false;
	}
	
	private boolean needPrestige(){
		int floor = 0;
		int dunglvl = 42;
		TextChar[] prestige = Text.findCharsInArea(640, 410, 22, 13, true);
		String sentence = "";
		
		if (prestige.length > 0){
			for (int i = 0; i < prestige.length; i++)
				sentence = sentence + String.valueOf(prestige[i].character);
			floor = Integer.parseInt(sentence);
			if (floor != 0 && dunglvl != 0)
				if (floor >= (Math.ceil((double)dunglvl/2)))
					return true;
		}
		return false;
	}
	
	private boolean doPrestige(){
		String[] sentence, options;

		Point PP = new Point(randomRange(683, 728), randomRange(489, 506)); // prestige tab
		Mouse.move(PP);
		if (Timing.waitUptext("Reset", 500)){
			Mouse.click(PP, 1);
			sleep(500, 750);
			
			sentence = NPCChat.getOptions();
			if (sentence.length > 0){
				for (int i = 0; i < sentence.length; i++)
					if (sentence[i].contains("rog")){
						NPCChat.clickContinue(true);
						
						options =  NPCChat.getOptions();
						if (options.length > 0)
							for (int j = 0; j < options.length; j++)
								if (options[j].contains("Yes")){
									NPCChat.selectOption("Yes", true);
									sleep(500, 1000);
									return true;
								}
					}
			}
		}
		return false;
	}
	
	private boolean startNewFloor(){
		if (leaveDungeon())
			if (toStairs())
				if (climbStairs())
					if (enterDungeon()){
						if (needParty())
							if (makeParty())
								if (!enterDungeon()){
									println("STOP!!");
								}
									
						if (selectFloor())
							if (selectComplexity(4))
								return true;
					}
		return false;
	}
	

	
	@Override
    public void run() {
		//long start = System.currentTimeMillis();
		
		/* Prestige & Start new floor
		if (needPrestige()){
			if (doPrestige())
				if (!startNewFloor())
					println("STOP!!");
		}
		*/
		
		while (startNewFloor()){
			
		}
		

		
			
		
		//println("Took: " + (long)(System.currentTimeMillis()-start) + " milliseconds");
    }
}