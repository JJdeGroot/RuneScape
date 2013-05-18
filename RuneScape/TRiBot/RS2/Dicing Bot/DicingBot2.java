package scripts;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.tribot.api.NPCChat;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Dicing", name = "Dicing Bot")
public class DicingBot2 extends Script{
	String temp1 = "sfdsfdsfsd";
	long temp1time = System.currentTimeMillis() + 30000;
	String temp2 = "sfsffdsfdsfsldflsdf";
	long temp2time = System.currentTimeMillis() + 30000;
	
	
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	private String getNameRoll(String text){
		String[] temp = text.split(":");
		if (temp.length > 0){
			String[] temp2 = temp[0].split("\\] ");
			if (temp2.length > 0){
				if (!temp2[1].isEmpty()){
					return temp2[1];
				}
			}
		}
		
		return "";
	}
	
	private void checkForRoll(){
		String[] text = NPCChat.getOptions();
		
		if (text.length >= 7){
			for (int i = 7; i < text.length; i++){
				if (text[i].contains("!Roll")){
					if (!text[i].contains(temp1)){
						int roll = 0;
						switch(randomRange(0, 3)){
							case 0: case 1: case 2: roll = randomRange(2, 7); break;
							case 3: case 4: roll = randomRange(8, 12); break;
						}
						String name = getNameRoll(text[i]);
						if (!name.isEmpty()){
							if (roll != 0){
								typeFast(name + " rolled " + roll);
								temp1 = name;
								temp1time = System.currentTimeMillis() + 10000;
								break;
							}
						}
					}else{
						if (System.currentTimeMillis() > temp1time){
							temp1 = "dfdsfdsfdsf";
						}
					}
				}
			}
		}
	}
	
	private String[] getDetailsDuel(String text){
		String[] empty = {""};
		String[] temp = text.split("-");
		if (temp.length >= 4){
			String[] result = {temp[1], temp[2], temp[3]};
			return result;
		}
		
		return empty;
	}
	
	private void checkForDuel(){
		long maxTime = System.currentTimeMillis() + 30000;
		int roll1 = 0, roll2 = 0;
		String[] text = NPCChat.getOptions();
		
		if (text.length > 7){
			if (text[7].contains("!Dd")){
				String[] details = getDetailsDuel(text[7]);
				if (details.length >= 3){
					ArrayList<String> names = new ArrayList<String>();
					names.add(details[0]);
					names.add(details[1]);
					while (System.currentTimeMillis() < maxTime && !names.isEmpty()){
						String[] chatbox = NPCChat.getOptions();
						if (chatbox.length >= 7){
							for (int i = 7; i < chatbox.length; i++){
								if (!chatbox[i].contains(temp2)){
									if (!names.isEmpty()){
										if (roll1 == 0){
											if (chatbox[i].toLowerCase().contains(details[0].toLowerCase()) && chatbox[i].contains("!Roll")){
												roll1 = randomRange(2, 12);
												typeFast(details[0] + " rolled " + roll1);
												names.remove(0);
												temp2 = details[0];
												temp2time = System.currentTimeMillis() + 10000;
											}
										}
										
										if (roll2 == 0){
											if (chatbox[i].toLowerCase().contains(details[1].toLowerCase()) && chatbox[i].contains("!Roll")){
												roll2 = randomRange(2, 12);
												typeFast(details[1] + " rolled " + roll2);
												names.remove(0);
												temp2 = details[1];
												temp2time = System.currentTimeMillis() + 10000;
											}
										}
									}
								}else{
									if (System.currentTimeMillis() > temp1time){
										temp2 = "asdadasdasdasd";
									}
								}
							}
						}
						
					}

				}
				
				if (roll1 == 0 || roll2 == 0){
					typeFast("Dice Duel has timed out");
				}
				
				if (roll1 != 0 && roll2 != 0){
					if (roll1 == roll2){
						typeFast("Both rolled " + details[0] + ": tie");
					}else{
						if (roll1 > roll2){
							typeFast(details[0] + " won " + (2 * Integer.parseInt(details[2])) + "M!");
						}else{
							typeFast(details[1] + " won " + (2 * Integer.parseInt(details[2])) + "M!");
						}
					}
				}
			}
		}
	}
	
	private void typeFast(String text){
		Keyboard.typeString(text);
		Keyboard.pressEnter();
	}
	
	
	@Override
    public void run() {
		boolean repeat = true;
		while (repeat){
			switch(randomRange(0, 100)){
			case 0: Mouse.move(new Point(500 + randomRange(0, 25), 300 + randomRange(0, 25)));
					Mouse.click(1);
					break;
			}
			checkForRoll();
    		checkForDuel();
		}
    }
}