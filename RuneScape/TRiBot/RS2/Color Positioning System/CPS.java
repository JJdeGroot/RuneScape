package scripts;

import java.awt.Point;

import org.tribot.api.GameTab;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.Text;
import org.tribot.api.types.TextChar;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Test", name = "Levels")
public class CPS extends Script{
	
	public enum SKILLS{
		ATTACK, STRENGTH,DEFENCE, RANGED, PRAYER, 
	    MAGIC, RUNECRAFTING, CONSTRUCTION, DUNGEONEERING, CONSTITUTION,
        AGILITY, HERBLORE, THIEVING, CRAFTING, FLETCHING, 
        SLAYER, HUNTER, MINING, SMITHING, FISHING, 
        COOKING, FIREMAKING, WOODCUTTING, FARMING, SUMMONING
	}
	
	private int getCurrentLevel(SKILLS skill){
		SKILLS[] skills = SKILLS.values();
		Point start = new Point(574, 263);
		int xJump = 63, yJump = 28, width = 3, height = 9;
		int[] levels = new int[27];
	
		if (GameTab.getOpen() != TABS.STATS){
			GameTab.open(TABS.STATS);
			sleep(250, 500);
		}

		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				TextChar[] text = Text.findCharsInArea(start.x + i * xJump, start.y + j * yJump, 20, 9, true);
				if (text.length > 0){
					char[] chars = new char[text.length];
					for (int k = 0; k < text.length; k++){
						chars[k] = text[k].character;
					}
					String level = new String(chars);
					try {
	        	    	levels[i * height + j] = Integer.parseInt(level);
		            } catch (NumberFormatException e) {
		                levels[i * height + j] = -1;
		            }
				}
			}
		}
		
		for (int i = 0; i < skills.length; i++){
			if (skills[i].equals(skill)){
				if (i > 16){
					return levels[i+1];
				}
				return levels[i];
			}
		}
		
		return -1;
	}
	
	private int getBaseLevel(SKILLS skill){
		SKILLS[] skills = SKILLS.values();
		Point start = new Point(590, 275);
		int xJump = 63, yJump = 28, width = 3, height = 9;
		int[] levels = new int[27];
	
		if (GameTab.getOpen() != TABS.STATS){
			GameTab.open(TABS.STATS);
			sleep(250, 500);
		}

		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				TextChar[] text = Text.findCharsInArea(start.x + i * xJump, start.y + j * yJump, 20, 9, true);
				if (text.length > 0){
					char[] chars = new char[text.length];
					for (int k = 0; k < text.length; k++){
						chars[k] = text[k].character;
					}
					String level = new String(chars);
					try {
	        	    	levels[i * height + j] = Integer.parseInt(level);
		            } catch (NumberFormatException e) {
		                levels[i * height + j] = -1;
		            }
				}
			}
		}
		
		for (int i = 0; i < skills.length; i++){
			if (skills[i].equals(skill)){
				if (i > 16){
					return levels[i+1];
				}
				return levels[i];
			}
		}
		
		return -1;
	}
	
	private int[] getAllCurrentLevels(){
		Point start = new Point(574, 263);
		int xJump = 63, yJump = 28, width = 3, height = 9;
		int[] levels = new int[27];
	
		if (GameTab.getOpen() != TABS.STATS){
			GameTab.open(TABS.STATS);
			sleep(250, 500);
		}

		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				TextChar[] text = Text.findCharsInArea(start.x + i * xJump, start.y + j * yJump, 20, 9, true);
				if (text.length > 0){
					char[] chars = new char[text.length];
					for (int k = 0; k < text.length; k++){
						chars[k] = text[k].character;
					}
					String level = new String(chars);
					try {
	        	    	levels[i * height + j] = Integer.parseInt(level);
		            } catch (NumberFormatException e) {
		                levels[i * height + j] = -1;
		            }
				}
			}
		}
	
		return levels;
	}
	
	private int[] getAllBaseLevels(){
		Point start = new Point(590, 275);
		int xJump = 63, yJump = 28, width = 3, height = 9;
		int[] levels = new int[27];
	
		if (GameTab.getOpen() != TABS.STATS){
			GameTab.open(TABS.STATS);
			sleep(250, 500);
		}

		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				TextChar[] text = Text.findCharsInArea(start.x + i * xJump, start.y + j * yJump, 20, 9, true);
				if (text.length > 0){
					char[] chars = new char[text.length];
					for (int k = 0; k < text.length; k++){
						chars[k] = text[k].character;
					}
					String level = new String(chars);
					try {
	        	    	levels[i * height + j] = Integer.parseInt(level);
		            } catch (NumberFormatException e) {
		                levels[i * height + j] = -1;
		            }
				}
			}
		}

		return levels;
	}
	
	private int getTotalLevel(){
		if (GameTab.getOpen() != TABS.STATS){
			GameTab.open(TABS.STATS);
			sleep(250, 500);
		}
		
		TextChar[] text = Text.findCharsInArea(690, 496, 35, 9, true);
		char[] chars = new char[text.length];
		for (int i = 0; i < text.length; i++){
			chars[i] = text[i].character;
		}
		String level = new String(chars);
		try {
	    	return Integer.parseInt(level);
        } catch (NumberFormatException e) {
            return -1;
        }
	}
	
	private int getLevelFromLevels(int[] levels, SKILLS skill){
		SKILLS[] skills = SKILLS.values();
		for (int i = 0; i < skills.length; i++){
			if (skills[i].equals(skill)){
				if (i > 16){
					return levels[i+1];
				}
				return levels[i];
			}
		}
		
		return -1;
	}

	@Override
    public void run() {
    	println("test");
    	/*
    	int bAttackLevel = getBaseLevel(SKILLS.ATTACK);
        println("Base attack level: " + bAttackLevel); 
        
        int cAttackLevel = getCurrentLevel(SKILLS.ATTACK);
        println("Current attack level: " + cAttackLevel); 
        
        int[] allBaseLevels = getAllBaseLevels();
    	for (int i = 0; i < allBaseLevels.length; i++){
    		println("Base level " + i + ": " + allBaseLevels[i]);
    	}
    	
    	int[] allCurrentLevels = getAllCurrentLevels();
    	for (int i = 0; i < allCurrentLevels.length; i++){
    		println("Current level " + i + ": " + allCurrentLevels[i]);
    	}
    	
    	int totalLevel = getTotalLevel();
    	println("Total level: " + totalLevel);
    	*/
    	
    	int miningLvl = getLevelFromLevels(getAllBaseLevels(), SKILLS.MINING);
    	println("Mining level: " + miningLvl);
    	
    }
}