package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.tribot.api.Constants;
import org.tribot.api.DTMs;
import org.tribot.api.Minimap;
import org.tribot.api.Player;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.DTM;
import org.tribot.api.types.colour.DTMPoint;
import org.tribot.api.types.colour.DTMSubPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Dungeoneering", name = "JJ's Dungeons")
public class JJsDungeons2 extends Script implements Painting{
	// FLOORS //
	int FLOOR = -1;
	final int FROZEN = 0,
			  ABANDONED = 1,
			  FURNISHED = 2,
			  OCCULT = 3,
			  WARPED = 4;

	// KEYS //
	final String[] keyNames = {"corner", "crescent", "diamond", "pentagon", 
			   				   "rectangle", "shield", "triangle", "wedge"};
	final int[] grabbedKeys = new int[8];
	final long CORNER_KEY = 4246339401L,
			   CRESCENT_KEY = 2709435617L,
			   DIAMOND_KEY = 885594529L,
			   PENTAGON_KEY = 3729250727L,
			   RECTANGLE_KEY = 3299208923L,
			   SHIELD_KEY = 3738855977L,
			   TRIANGLE_KEY = 2009794115L,
			   WEDGE_KEY = 2048859797L;
	final long[] KEYS = {CORNER_KEY, CRESCENT_KEY, DIAMOND_KEY, PENTAGON_KEY,
						 RECTANGLE_KEY, SHIELD_KEY, TRIANGLE_KEY, WEDGE_KEY};
	
	// FROZEN DOORS //
	final String[] doorNames = {"normal", "guardian", "corner", "locked corner",
								"crescent", "locked crescent", "diamond", "locked diamond",
								"pentagon", "locked pentagon", "rectangle", "locked rectangle",
								"shield", "locked shield", "triangle", "locked triangle",
								"wedge", "locked wedge"};
	final int[] foundDoors = new int[18];
	final long FROZEN_NORMAL_DOOR = 1103861357,
			   FROZEN_GUARDIAN_DOOR = 2128416052L,
			   FROZEN_CORNER_DOOR = 817797154L,
			   FROZEN_LOCKED_CORNER_DOOR = 3567313268L,
			   FROZEN_CRESCENT_DOOR = 4100701787L,
			   FROZEN_LOCKED_CRESCENT_DOOR = 3910435716L,
			   FROZEN_DIAMOND_DOOR = 951884599L,
			   FROZEN_LOCKED_DIAMOND_DOOR = 91358147L,
			   FROZEN_PENTAGON_DOOR = 2271324898L,
			   FROZEN_LOCKED_PENTAGON_DOOR = 4331786L,
			   FROZEN_RECTANGLE_DOOR = 2916768302L,
			   FROZEN_LOCKED_RECTANGLE_DOOR = 91358147L,
			   FROZEN_SHIELD_DOOR = 15654456837L,
			   FROZEN_LOCKED_SHIELD_DOOR = 3787771282L,
			   FROZEN_TRIANGLE_DOOR = 2283382981L,
			   FROZEN_LOCKED_TRIANGLE_DOOR = 2405656118L,
			   FROZEN_WEDGE_DOOR = 820962017L,
			   FROZEN_LOCKED_WEDGE_DOOR = 3875161726L,
			   FROZEN_END_DUNGEON_LADDER = 760195575L;
	final long[] FROZEN_DOORS = {FROZEN_NORMAL_DOOR, FROZEN_GUARDIAN_DOOR, FROZEN_CORNER_DOOR, 
								 FROZEN_LOCKED_CORNER_DOOR, FROZEN_CRESCENT_DOOR, FROZEN_LOCKED_CRESCENT_DOOR, 
								 FROZEN_DIAMOND_DOOR, FROZEN_LOCKED_DIAMOND_DOOR, FROZEN_PENTAGON_DOOR, 
								 FROZEN_LOCKED_PENTAGON_DOOR, FROZEN_RECTANGLE_DOOR, FROZEN_LOCKED_RECTANGLE_DOOR, 
								 FROZEN_SHIELD_DOOR, FROZEN_LOCKED_SHIELD_DOOR, FROZEN_TRIANGLE_DOOR, 
								 FROZEN_LOCKED_TRIANGLE_DOOR, FROZEN_WEDGE_DOOR, FROZEN_LOCKED_WEDGE_DOOR, 
								 FROZEN_END_DUNGEON_LADDER};
	
	// FROZEN MONSTERS //
	final long[] FROZEN_FORGOTTEN_MAGE = {3561310148L, 1326036789L, 1597212118L, 1681796192L, 1969115135L, 3744374570L, 1969115135L,
										  3925667173L};
	final long[] FROZEN_FORGOTTEN_RANGER = {3545007793L, 542885332L, 2843750077L, 831055358L, 3545007793L, 594794251L, 1167093962L,
											1085661053L};
	final long[] FROZEN_FORGOTTEN_WARRIOR = {1397758839L, 4007478432L, 1271086756L, 3364414828L, 3555221883L, 3202713522L, 674224873L, 
											 2958639544L, 3539948262L, 1802395580L, 3509779262L, 1185561219L, 3790085060L, 110017228L,
											 18653500308L, 2049146926L, 2422936825L, 3053080153L, 3367617953L, 616848619L, 57600827L, 
											 83473977L, 1250647707L, 1856568072L, 548461974L, 3990261294L, 1692443119L, 2768752823L,
											 105900281L, 1115415981L, 4175087512L, 3538258775L};
	final long[] FROZEN_GIANT_RAT = {2122423973L, 14161673L, 2958331695L, 153836687L, 252909303L, 1541225756L, 2958331695L};
	final long FROZEN_HYDRA = 236354172L,
			   FROZEN_ICE_ELEMENTAL = 2846886962L,
			   FROZEN_ICE_GIANT = 2637943098L,
			   FROZEN_ICE_SPIDER = 1253199262L,
			   FROZEN_ICE_WARRIOR = 1589511840L,
			   FROZEN_ICE_ICEFIEND = 3463791966L,
			   FROZEN_MYSTERIOUS_SHADE = 4255391196L,
			   FROZEN_PROTOMASTYX = 2459750624L,
			   FROZEN_THROWER_TROLL = 699355667L;
	final long[][] FROZEN_MONSTERS = {FROZEN_FORGOTTEN_MAGE, FROZEN_FORGOTTEN_RANGER, FROZEN_FORGOTTEN_WARRIOR, FROZEN_GIANT_RAT,
									  {FROZEN_HYDRA, FROZEN_ICE_ELEMENTAL, FROZEN_ICE_GIANT, FROZEN_ICE_SPIDER, 
									   FROZEN_ICE_WARRIOR, FROZEN_ICE_ICEFIEND, FROZEN_MYSTERIOUS_SHADE, FROZEN_PROTOMASTYX, 
									   FROZEN_THROWER_TROLL}};
	
	// FROZEN BOSSES //
	final long FROZEN_GLUTTONOUS_BEHEMOTH = 2081911231L,
			   FROZEN_BEHEMOTH_FOOD_SOURCE = 1561638623L,
			   FROZEN_ASTEA_FROSTWEB = 2889928543L,
			   FROZEN_ICY_BONES = 3076383209L,
			   FROZEN_LUMINESCENT_ICEFIEND = 4157604468L,
			   FROZEN_ICEFIEND_ICE_PARTICLE = 1360992063L,
			   FROZEN_ICEFIEND_IN_ICE = 3090192007L,
			   FROZEN_PLANEFREEZER_LAKHRAHNAZ = 16807696L,
			   FROZEN_TO_KASH_THE_BLOODCHILLER = 159919581L;
	final long[] FROZEN_BOSSES = {FROZEN_GLUTTONOUS_BEHEMOTH, FROZEN_BEHEMOTH_FOOD_SOURCE, FROZEN_ASTEA_FROSTWEB, 
								  FROZEN_ICY_BONES, FROZEN_LUMINESCENT_ICEFIEND, FROZEN_ICEFIEND_ICE_PARTICLE, 
								  FROZEN_ICEFIEND_IN_ICE, FROZEN_PLANEFREEZER_LAKHRAHNAZ, FROZEN_TO_KASH_THE_BLOODCHILLER};

	String status = "starting up";
	Point[] room;
	
	private Object map(){
		Object map[] = new Object[4];
		map[0] = 10;
		map[1] = "First room";
		map[2] = "Cleared";
		map[3] = "Boss room";
		
		return map;
		
	}
	Object object[] = new Object[4];
	
	private boolean detectProtection(){
		int MSX1 = Constants.MSX1, MSY1 = Constants.MSY1, MSX2 = Constants.MSX2, MSY2 = Constants.MSY2;

		DTMPoint DTM_PT_0 = new DTMPoint(new Color(65, 64, 64), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_0 = { new DTMSubPoint(new ColourPoint(new Point(2, -8), new Color( 249, 247, 0)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(7, -3), new Color( 249, 247, 0)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(-8, 8), new Color( 189, 188, 191)), new Tolerance(10, 10, 10),1)};
		DTM protectMelee = new DTM(DTM_PT_0, DTM_PTS_0);
		
		DTMPoint DTM_PT_1 = new DTMPoint(new Color(63, 45, 10), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_1 = { new DTMSubPoint(new ColourPoint(new Point(5, -7), new Color( 36, 123, 9)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(-9, 9), new Color( 131, 45, 7)), new Tolerance(10, 10, 10),1)};
		DTM protectRange = new DTM(DTM_PT_1, DTM_PTS_1);
		
		DTMPoint DTM_PT_2 = new DTMPoint(new Color(131, 45, 7), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_2 = { new DTMSubPoint(new ColourPoint(new Point(-7, 7), new Color( 249, 247, 0)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(7, -7), new Color( 249, 247, 0)), new Tolerance(10, 10, 10),1)};
		DTM protectMage = new DTM(DTM_PT_2, DTM_PTS_2);
		
		DTM[] protect = {protectMelee, protectRange, protectMage};
		
		for (int i = 0; i < protect.length; i++){
			Point[] detect = DTMs.find_simple(protect[i], MSX1, MSY1, MSX2, MSY2);
			if (detect.length > 0){
				switch(i){
					case 0: println("Detected protect from melee"); break;
					case 1: println("Detected protect from range"); break;
					case 2: println("Detected protect from mage"); break;
				}
			}
		}
		
		return false;
	}
	
	private boolean inFight(){
		DTMPoint DTM_PT_0 = new DTMPoint(new Color(210, 210, 211), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_0 = { new DTMSubPoint(new ColourPoint(new Point(-8, 8), new Color( 180, 85, 0)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(3, -4), new Color( 97, 97, 94)), new Tolerance(10, 10, 10),1)};
		DTM hit = new DTM(DTM_PT_0, DTM_PTS_0);
		
		DTMPoint DTM_PT_1 = new DTMPoint(new Color(9, 131, 186), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_1 = { new DTMSubPoint(new ColourPoint(new Point(-3, -6), new Color( 8, 117, 170)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(3, 4), new Color( 9, 124, 178)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(8, -9), new Color( 253, 253, 254)), new Tolerance(10, 10, 10),1)};
		DTM shield = new DTM(DTM_PT_1, DTM_PTS_1);

		return false;
	}
	
	private void waitUntilNotMoving(){
		sleep(250, 500);
		while (Player.isMoving()){
			sleep(100, 200);
		}
		sleep(5000);
	}
	
	private boolean findNPCs(){
		Point MMc = new Point(627, 135);
		Color npc = new Color(235, 250, 10);
		ArrayList<Point> npcList = new ArrayList<Point>();
		Point[] coords = getRoom();
		
		for (int i = 0; i < coords.length; i++){
			int x = coords[i].x;
		    int y = coords[i].y;
			if (Screen.findColour(npc, x, y, x, y, new Tolerance(15)) != null){
				npcList.add(new Point(x, y));
			}
		}

		if (!npcList.isEmpty()){
			status = "Finding monsters";
			int nearest = 1000;
			int best = 0;
			for (int i = 0; i < npcList.size(); i++){
				int x = npcList.get(i).x;
			    int y = npcList.get(i).y;
			    int dist = Math.abs(x - MMc.x) + Math.abs(y - MMc.y);
			    if (dist < nearest){
			    	nearest = dist;
			    	if (dist >= 20){
			    		best = i;
			    	}else{
			    		best = -1;
			    	}
			    }
			}
			
			if (best != -1){
				Point MP = new Point(npcList.get(best).x + randomRange(-5, 5), npcList.get(best).y + randomRange(-5, 5));
				Mouse.move(MP);
				Mouse.click(1);
				waitUntilNotMoving();
			}
			killMonsters();
			
			
			
		}
		return false;
	}
	
	private void killMonsters(){
		Point MSc = new Point(259, 220);
		for (int i = 0; i < FROZEN_MONSTERS.length; i++){
			ScreenModel[] monsters = ScreenModels.find(FROZEN_MONSTERS[i]);
			if (monsters.length > 0){
				status = "Killing a monster";
				println("detected monsters");
		
				int nearest = 1000;
				int best = -1;
				for (int j = 0; j < monsters.length; j++){
					int distance = Math.abs(monsters[j].base_point.x - MSc.x) + Math.abs(monsters[j].base_point.y - MSc.y); 
					if (distance < nearest){
						nearest = distance;
						best = j;
					}
				}
					
				if (best != -1){
					Point MP = new Point(monsters[best].base_point.x + randomRange(-5, 5), monsters[best].base_point.y + randomRange(-5, 5));
					Mouse.move(MP);
					if (Timing.waitUptext("Attack", 500)){
						Mouse.click(1);
						
						long temp = monsters[best].id;
						int length = ScreenModels.find(temp).length;
						while (ScreenModels.find(temp).length > (length-1)){
							sleep(50, 150);
							switch(randomRange(0, 100)){
							case 0: ScreenModel[] find = ScreenModels.find(temp);
									if (find.length > 0){
										Point TP = find[0].base_point;
										Mouse.move(TP);
										if (Timing.waitUptext("Attack", 500)){
											Mouse.click(1);
										}
									}
							}
						}
						
						break;
					}
					
				}
			}
		}
	}
	
	public void onPaint(Graphics g){
        g.setColor(Color.YELLOW);
        g.drawString("JJ's Dungeons", 5, 160);
        g.setColor(Color.WHITE);
        g.drawString("Status: " + status, 5, 175);
        g.setColor(Color.GREEN);
        if (room.length > 0)
        	for (int i = 0; i < room.length; i++)
        		g.drawLine(room[i].x, room[i].y, room[i].x, room[i].y);
    }
	
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	private int floorType(int FLOOR){
		if (FLOOR <= 11)
			return FROZEN;
		else if (FLOOR <= 17)
			return ABANDONED;
		else if (FLOOR <= 29)
			return FURNISHED;
		else if (FLOOR <= 47)
			return OCCULT;
		else if (FLOOR <= 60)
			return WARPED;
		return -1;
	}
	
	private boolean detectKeys(){
		// Looping through the results to know what keys have been found.
		for (int i = 0; i < KEYS.length; i++){
			ScreenModel[] keySearch = ScreenModels.find(KEYS[i]);
			if (keySearch.length > 0){
				grabbedKeys[i]++;
				status = "Found " + keyNames[i] + " key";
				println("Found " + keyNames[i] + " key");
				return true;
			}
		}
		return false;
	}
	
	private void detectDoors(){
		long[] doorIDs = new long[8];
		
		if (FLOOR != -1){
			switch (floorType(FLOOR)){
				case FROZEN: doorIDs = FROZEN_DOORS;
				case ABANDONED: //doorIDs = ABANDONED_DOORS;
				case FURNISHED: //doorIDs = FURNISHED_DOORS;
				case OCCULT: //doorIDs = OCCULT_DOORS;
				case WARPED: //doorIDs = WARPED_DOORS;
				default: stopScript();
			}
		}

		// Looping through the results to know what doors have been found.
		for (int i = 0; i < doorIDs.length; i++){
			ScreenModel[] doorSearch = ScreenModels.find(doorIDs[i]);
			if (doorSearch.length > 0){
				if (ScreenModels.find(doorIDs[i+1]).length > 0){
					foundDoors[i]++;
					status = "Detected " + doorNames[i] + " door";
					println("Detected " + doorNames[i] + " door");
				}
			}
		}
	}
	
	
	private Point[] getRoom(){
		boolean[][] map = new boolean[151][151];
		int MMX1 = 551, MMY1 = 59, MMXL = 151, MMYL = 151;
		Color A = new Color(0, 0, 0);
		Color B = new Color(0, 0, 0);

		// Get all pixels that aren't black from the minimap, store in a 2d boolean array
		for (int i = 0; i < map.length; i++){
			for (int j = 0; j < map[i].length; j++){
				double dist = Math.pow(MMX1 + (MMXL/2) - (i + MMX1), 2) + Math.pow(MMY1 + (MMYL/2) - (j + MMY1), 2);
				if (dist < (Math.pow(MMXL/2, 2))){
					B = Screen.getColourAt(new Point(i + MMX1, j + MMY1));
					if (!Screen.coloursMatch(A, B, new Tolerance(10))){
						map[i][j] = true;
					}
				}
			}
		}
		
		// Flood-fill to get the current room
		ArrayList<Point> stack = new ArrayList<Point>();
		stack.add(new Point(627, 135));
		int[][] offset = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
		int i = 0;
		
		while (stack.size() > i){
			Point convert = new Point(stack.get(i).x-MMX1, stack.get(i).y-MMY1);

			for (int j = 0; j < offset.length; j++){
				Point PT = new Point(convert.x + offset[j][0], convert.y + offset[j][1]);
				if (map[PT.x][PT.y]){
					stack.add(new Point(PT.x + MMX1, PT.y + MMY1));
					map[PT.x][PT.y]= false; 
				}
			}
			
			i++;
		}
		
		room = stack.toArray(new Point[stack.size()]);
		
		return room;
	}
	
	
	private void gluttonousBehemoth(){
		long bossID = 2081911231L, foodSourceID = 1561638623L;
		Point MSC = new Point(259, 220);
		
		ScreenModel[] boss = ScreenModels.find(bossID);
		if (boss.length > 0){
			ScreenModel[] food = ScreenModels.find(foodSourceID);
			if (food.length > 0){
				int foodDist = Math.abs(MSC.x - food[0].base_point.x) + Math.abs(MSC.y - food[0].base_point.y);
				println("Food dist: " + foodDist);
				if (foodDist < 110){
					Point BP = new Point(boss[0].base_point);
					Mouse.move(BP);
					if (Timing.waitUptext("behemoth", 500)){
						Mouse.click(BP, 1);
					}
				}else{
					println("We need to move near the food source!");
					Point FP = new Point(food[0].base_point);
					Mouse.click(FP, 1);
				}
			}
		}
	}
	
	private void icyBones(){
		long bossID = 3076383209L;
		
		ScreenModel[] boss = ScreenModels.find(bossID);
		if (boss.length > 0){
			Point BP = new Point(boss[0].base_point);
			Mouse.move(BP);
			if (Timing.waitUptext("bones", 500)){
				Mouse.click(BP, 1);
			}
		}
	}
	
	@Override
    public void run() {
    	println("JJ's Dungeons test");
    	long start = System.currentTimeMillis();
    	
    	//detectProtection();
    	
    	//killMonsters();
    	findNPCs();
    	
    	
    	//detectKeys();
    	//detectDoors();
    	//getRoom();
    	//gluttonousBehemoth();
    	//icyBones();
    	
    	
    	//Mouse.move(MMtoMS(new Point(629, 142)));
    	//sleep(20000);
    	
    	
    	println("Took: " + (long)(System.currentTimeMillis()-start) + " milliseconds");
    }
}