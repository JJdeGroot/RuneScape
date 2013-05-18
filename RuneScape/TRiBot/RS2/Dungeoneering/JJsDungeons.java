package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.tribot.api.Minimap;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Dungeoneering", name = "JJ's Dungeons")
public class JJsDungeons extends Script implements Painting{
	
	String status = "starting up";
	final int MMX1 = 551, MMY1 = 59, MMXL = 151, MMYL = 151;
	boolean[][] map = new boolean[151][151];
	Point[] room;
	
	public void onPaint(Graphics g){
        g.setColor(Color.YELLOW);
        g.drawString("JJ's Dungeons", 5, 160);
        g.setColor(Color.WHITE);
        g.drawString("Status: " + status, 5, 175);
        g.setColor(Color.GREEN);
        g.drawOval(MMX1, MMY1, MMXL, MMYL);
        if (room.length > 0)
        	for (int i = 0; i < room.length; i++)
        		g.drawLine(room[i].x, room[i].y, room[i].x, room[i].y);
    }
	
	private void detectKeys(){
		final String[] keyNames = {"corner", "crescent", "diamond", "pentagon", 
								   "rectangle", "shield", "triangle", "wedge"};
		final long[] keyIDs = {4246339401L, 2709435617L, 885594529L, 3729250727L,
							   3299208923L, 3738855977L, 2009794115L, 2048859797L}; 
		final int[] grabbedKeys = {0, 0, 0, 0, 0, 0, 0, 0};
		
		// Looping through the results to know what keys have been found.
		for (int i = 0; i < keyIDs.length; i++){
			ScreenModel[] keySearch = ScreenModels.find(keyIDs[i]);
			if (keySearch.length > 0){
				grabbedKeys[i]++;
				status = "Found " + keyNames[i] + " key";
				println("Found " + keyNames[i] + " key");
			}
		}
	}
	
	private void detectDoors(){
		final String[] doorNames = {"locked corner", "locked crescent", "locked diamond", "locked pentagon",
									"locked rectangle", "locked shield", "locked triangle", "locked wedge", 
									"corner", "crescent", "diamond", "pentagon", "rectangle",
									"shield", "triangle", "wedge", "guardian", "normal"};
		final long[] doorIDs = {3567313268L, 3910435716L, 91358147L, 4331786L, 
								91358147L, 3787771282L, 2405656118L, 3875161726L,
								817797154L, 4100701787L, 951884599L, 2271324898L, 2916768302L, 
								15654456837L, 2283382981L, 820962017L, 2128416052L, 1103861357L};
		final int[] foundDoors = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
		// Looping through the results to know what doors have been found.
		for (int i = 0; i < doorIDs.length; i++){
			ScreenModel[] doorSearch = ScreenModels.find(doorIDs[i]);
			if (doorSearch.length > 0){
				if (i == 2 || i == 4)
					if (ScreenModels.find(doorIDs[i+8]).length < 1)
						continue;
				foundDoors[i]++;
				status = "Detected " + doorNames[i] + " door";
				println("Detected " + doorNames[i] + " door");
			}
		}
	}
	
	
	private Point[] getRoom(){
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