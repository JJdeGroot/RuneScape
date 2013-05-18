package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.tribot.togl.Receiver;
import org.tribot.api.Screen;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Test", name = "MMtoMS 2")
public class MMtoMSv2 extends Script implements Painting {
	final int MMX1 = 551, MMY1 = 59, MMXL = 151, MMYL = 151;
	int[][][] MSBoxes = new int[13][8][];
	int[][][] MMBoxes = new int[13][8][];
	ColourPoint[] item, npc, door;
	
	boolean[][] map = new boolean[151][151];
	Point[] room;
	
	public void onPaint(Graphics g){
		g.drawString("JJs Dungeons", 50, 100);
		
		
		if (MSBoxes[0][0].length > 0){
			g.setColor(Color.GRAY);
			for (int i = 0; i < MSBoxes.length; i++)
				for (int j = 0; j < MSBoxes[i].length; j++)
					g.drawRect(MSBoxes[i][j][0], MSBoxes[i][j][1], MSBoxes[i][j][2]-MSBoxes[i][j][0], MSBoxes[i][j][3]-MSBoxes[i][j][1]);
			g.drawLine(100, 100, 200, 200);
		}
		
		g.drawString("TEST", 75, 125);
		
		
		if (MMBoxes[0][0].length > 0){
			g.setColor(Color.RED);
			for (int i = 0; i < MMBoxes.length; i++)
				for (int j = 0; j < MSBoxes[i].length; j++)
					g.drawRect(MMBoxes[i][j][0], MMBoxes[i][j][1], MMBoxes[i][j][2]-MMBoxes[i][j][0], MMBoxes[i][j][3]-MMBoxes[i][j][1]);
		}

		/*
		if (NPCBoxes.length > 0){
			g.setColor(Color.YELLOW);
			for (int i = 0; i < NPCBoxes.length; i+=4)
				g.drawRect(NPCBoxes[i+0], NPCBoxes[i+1], NPCBoxes[i+2]-NPCBoxes[i+0], NPCBoxes[i+3]-NPCBoxes[i+1]);
		}
		*/
			
		/*
		if (item.length > 0){
			g.setColor(Color.PINK);
			for (int i = 0; i < item.length; i++)
				g.drawLine(item[i].point.x, item[i].point.y, item[i].point.x, item[i].point.y);
		}

		if (npc.length > 0){
			g.setColor(Color.GREEN);
			for (int i = 0; i < npc.length; i++)
				g.drawLine(npc[i].point.x, npc[i].point.y, npc[i].point.x, npc[i].point.y);
		}

		if (door.length > 0){
			g.setColor(Color.ORANGE);
			for (int i = 0; i < door.length; i++)
				g.drawLine(door[i].point.x, door[i].point.y, door[i].point.x, door[i].point.y);
		}
		*/
			
		
	}


	/**
	private Point MMtoMS(Point P){ // Credits to Narcle
		int X = P.x - 627;
		int Y = P.y - 85 - 50;
		double yM = 36.0;
		double Yh = Math.abs(Y + yM);
		
		P.x = (int)(263.5 + (X * 9.75 / (12.8 - (Yh / yM * 3.15))) * 9.75);
		P.y = (int)(182.0 + Y * (6.3 + ((Yh / yM) * 2.4)) + 50);
		return P;
	}
	**/
	
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
		
		return stack.toArray(new Point[stack.size()]);
	}
	
	private Point MMtoMS(Point P){
		int	MSX1  = 4, MSY1 = 54, MSX2 = 515, MSY2 = 387,
			MMX1 = 602, MMY1 = 118, MMX2 = 654, MMY2 = 150;

		for (int i = 0; i <= 12; i++)
			for (int j = 0; j <= 7; j++){
				MMBoxes[i][j] = new int[] { (MMX2 - MMX1) / 13 * i + MMX1,
	       			    (MMY2 - MMY1) / 7 * j + MMY1,
	       			    (MMX2 - MMX1) / 13 * i + MMX1 + ((MMX2 - MMX1) / 13),
	       			    (MMY2 - MMY1) / 7 * j + MMY1 + ((MMY2 - MMY1) / 7) };
				
				if (P.x > MMBoxes[i][j][0])
					if (P.y > MMBoxes[i][j][1])
						if (P.x < MMBoxes[i][j][2])
							if (P.y < MMBoxes[i][j][3]){
								MSBoxes[i][j] = new int[] { (MSX2 - MSX1) / 14 * i + MSX1 + 22,
					       			    (MSY2 - MSY1) / 9 * j + MSY1 + 16,
					       			    (MSX2 - MSX1) / 14 * i + MSX1 + 22 + ((MSX2 - MSX1) / 14),
					       			    (MSY2 - MSY1) / 9 * j + MSY1 + 16 + ((MSY2 - MSY1) / 9) };
								
								return new Point((MSBoxes[i][j][0] + MSBoxes[i][j][2])/2, (MSBoxes[i][j][1] + MSBoxes[i][j][3])/2); 
							}
			}
		return null;
	}
	
	private boolean findStuff(){
		ArrayList<Point> npcList = new ArrayList<Point>();
		ArrayList<Point> itemList = new ArrayList<Point>();
		ArrayList<Point> doorList = new ArrayList<Point>();
		Color npc = new Color(235, 250, 10);
		Color item = new Color(230, 0, 0);
		Color door = new Color(225, 10, 10);
		Point[] coords = getRoom();
		
		for (int i = 0; i < coords.length; i++){
			int x = coords[i].x;
		    int y = coords[i].y;
			if (Screen.findColour(npc, x, y, x, y, new Tolerance(15)) != null){
				npcList.add(new Point(x, y));
				continue;
			}
				
			if (Screen.findColour(item, x, y, x, y, new Tolerance(25)) != null){
				itemList.add(new Point(x, y));
				continue;
			}
			
			if (Screen.findColour(door, x, y, x, y, new Tolerance(25)) != null){
				doorList.add(new Point(x, y));
				continue;
			}
		}
		
		println("Npc's found at:");
		println("size: " + npcList.size());
		if (!npcList.isEmpty()){
			int i = 0;
			while (i < npcList.size()){
				for (int j = 0; j < npcList.size(); j++){
					int dist = (Math.abs(npcList.get(i).x - npcList.get(j).x)) + (Math.abs(npcList.get(i).y - npcList.get(j).y));
					if (dist < 3 && dist != 0){
						npcList.remove(i);
						i--;
						break;
					}
				}
				i++;
			}
		}
		println("size: " + npcList.size());
		
		
		println("Items found at:");
		println("size: " + itemList.size());
		if (!itemList.isEmpty()){
			int i = 0;
			while (i < itemList.size()){
				for (int j = 0; j < itemList.size(); j++){
					int dist = (Math.abs(itemList.get(i).x - itemList.get(j).x)) + (Math.abs(itemList.get(i).y - itemList.get(j).y));
					if (dist < 3 && dist != 0){
						itemList.remove(i);
						i--;
						break;
					}
				}
				i++;
			}
		}
		println("size: " + itemList.size());
		
		println("Doors found at:");
		println("size: " + doorList.size());
		if (!doorList.isEmpty()){
			int i = 0;
			while (i < doorList.size()){
				for (int j = 0; j < doorList.size(); j++){
					int dist = (Math.abs(doorList.get(i).x - doorList.get(j).x)) + (Math.abs(doorList.get(i).y - doorList.get(j).y));
					if (dist < 3 && dist != 0){
						doorList.remove(i);
						i--;
						break;
					}
				}
				i++;
			}
		}
		println("size: " + doorList.size());
		for (int i = 0; i < doorList.size(); i++)
			println(doorList.get(i));
		
		if (!npcList.isEmpty() || !itemList.isEmpty() || !doorList.isEmpty())
			return true;

		return false;
	}
	
	private void findDotColors(){
		ArrayList<Point> npcList = new ArrayList<Point>();
		ArrayList<Point> itemList = new ArrayList<Point>();
		ArrayList<Point> doorList = new ArrayList<Point>();
		
		item = Screen.findColours(new Color(230, 0, 0), 552, 55, 706, 213, new Tolerance(25));
		if (item.length > 0)
			for (int i = 0; i < item.length; i++)
				if (item[i].colour.getBlue() <= 5)
					if (item[i].colour.getGreen() <= 5)
						itemList.add(item[i].point);
		println("Item length: " + item.length);
		println("ItemList length: " + itemList.size());
		
			
		npc = Screen.findColours(new Color(235, 250, 10), 552, 55, 706, 213, new Tolerance(15));

		println("NPC length: " + npc.length);
		println("NPCList length: " + npcList.size());
	
		door =  Screen.findColours(new Color(225, 10, 10), 552, 55, 706, 213, new Tolerance(25));
		if (door.length > 0)
			for (int i = 0; i < door.length; i++)
				if (door[i].colour.getBlue() >= 5 && door[i].colour.getBlue() <= 15)
					if (door[i].colour.getGreen() >= 5 && door[i].colour.getGreen() <= 15)
						doorList.add(door[i].point);
		println("Door length: " + door.length);
		println("DoorList length: " + doorList.size());
	}
		
	@Override
    public void run(){
		long start = System.currentTimeMillis();
		
		//findDotColors();
		//findStuff();
		
		println(MSBoxes.length);
		MMtoMS(new Point(0, 0));
		if (MSBoxes[0][0].length > 0)
			println("hallo");
		
		
		
		
		println("Took: " + (long)(System.currentTimeMillis()-start) + " milliseconds");
	}


}
