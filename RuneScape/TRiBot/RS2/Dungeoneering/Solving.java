package scripts;

import java.awt.Color;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tribot.api.Minimap;
import org.tribot.api.Textures;
import org.tribot.api.DTMs;
import org.tribot.api.Screen;
import org.tribot.api.TPS;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.Texture;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.DTM;
import org.tribot.api.types.colour.DTMPoint;
import org.tribot.api.types.colour.DTMSubPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;

public class Solving extends Script {
	Paint paint = new Paint();
	General gen = new General();
	
	BufferedImage smallMap;
	
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
	final String[] doorNames = {"normal", "guardian"};
	final String[] keyDoorNames = {"corner", "crescent", "diamond", "pentagon", 
								   "rectangle", "shield", "triangle", "wedge"};
	final int[] foundDoors = new int[16];
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
			   FROZEN_DUNGEON_EXIT_LADDER = 3271922553L,
			   FROZEN_END_DUNGEON_LADDER = 760195575L;
	final long[] FROZEN_LOCKED_DOORS = {FROZEN_LOCKED_CORNER_DOOR, FROZEN_LOCKED_CRESCENT_DOOR, 
								  		FROZEN_LOCKED_DIAMOND_DOOR, FROZEN_LOCKED_PENTAGON_DOOR, 
								  		FROZEN_LOCKED_RECTANGLE_DOOR, FROZEN_LOCKED_SHIELD_DOOR,  
								  		FROZEN_LOCKED_TRIANGLE_DOOR,  FROZEN_LOCKED_WEDGE_DOOR};
	final long[] FROZEN_KEY_DOORS = {FROZEN_CORNER_DOOR, FROZEN_CRESCENT_DOOR, FROZEN_DIAMOND_DOOR, 
								 FROZEN_PENTAGON_DOOR, FROZEN_RECTANGLE_DOOR, FROZEN_SHIELD_DOOR, 
								 FROZEN_TRIANGLE_DOOR, FROZEN_WEDGE_DOOR};
	final long[] FROZEN_DOORS = {FROZEN_NORMAL_DOOR, FROZEN_GUARDIAN_DOOR};
	final long[] FROZEN_LADDERS = {FROZEN_DUNGEON_EXIT_LADDER, FROZEN_END_DUNGEON_LADDER};
	
	// OTHERS
	int itemID = -1;

	public Object map(){
		Object map[] = new Object[4];
		map[0] = 10;
		map[1] = "First room";
		map[2] = "Cleared";
		map[3] = "Boss room";
		
		return map;
		
	}
	
	public Point getMapRoom(){
		Point P = TPS.getRelativePosition();
		//println("Relative position: " + P);
		
		Point firstRoom = new Point(-140, -110);
		int roomSize = 60, roomDistance = 4;
		for (int i = 0; i <= 3; i++){
			for (int j = 0; j <= 3; j++){
				int[] coords = { firstRoom.x + i * (roomSize + roomDistance),
						         firstRoom.y + j * (roomSize + roomDistance),
						         firstRoom.x + i * (roomSize + roomDistance) + roomSize,
						         firstRoom.y + j * (roomSize + roomDistance) + roomSize };
				if (P.x >= coords[0]){
					if (P.x <= coords[2]){
						if (P.y >= coords[1]){
							if (P.y <= coords[3]){
								//int room = (i * 4 + j);
								//println("Room: " + room);
								return new Point(i, j);
							}
						}
					}
				
				}
			}
		}
		
		return null;
	}
	
	public Point getMapPos(){
		Point P = TPS.getRelativePosition();
		Point PP = new Point((P.x/2) + 456, (P.y/2) + 448);

		return PP;
	}
	
	public boolean getMap(){
		// Checking if the map is open
		Color mapColor = new Color(154, 138, 103);
		Tolerance mapTolerance = new Tolerance(10);
		int mapCount = Screen.findColours(mapColor, 122, 79, 401, 358, mapTolerance).length;
		if (mapCount < 10000){
			Point MP = new Point(gen.randomRange(531, 553), gen.randomRange(181, 201));
			Mouse.move(MP);
			if (Timing.waitUptext("Map", 500)){
				Mouse.click(1);
				int wait = 0;
				while (Screen.findColours(mapColor, 122, 79, 401, 358, mapTolerance).length < 10000 && wait <= 10){
					sleep(50, 100);
					wait++;
				}
				sleep(100, 200);
			}else{
				println("Failed to open the map");
				return false;
			}
		}
		
		// Making a debug image
		smallMap = paint.getMapImage();
	
		// Setting up multiple DTM's 
		DTMPoint DTM_PT_0 = new DTMPoint(new Color(210, 199, 111), new Tolerance(20, 20, 20));
		DTMSubPoint [] DTM_PTS_0 = { new DTMSubPoint(new ColourPoint(new Point(5, -4), new Color( 226, 223, 163)), new Tolerance(20, 20, 20),2), new DTMSubPoint(new ColourPoint(new Point(10, -1), new Color( 200, 185, 114)), new Tolerance(20, 20, 20),2), new DTMSubPoint(new ColourPoint(new Point(5, 6), new Color( 218, 207, 124)), new Tolerance(30, 30, 30),2)};
		DTM unknown = new DTM(DTM_PT_0, DTM_PTS_0);

		DTMPoint DTM_PT_1 = new DTMPoint(new Color(149, 145, 105), new Tolerance(5, 5, 5));
		DTMSubPoint [] DTM_PTS_1 = { new DTMSubPoint(new ColourPoint(new Point(-6, 2), new Color( 51, 39, 21)), new Tolerance(5, 5, 5),1), new DTMSubPoint(new ColourPoint(new Point(1, -6), new Color( 51, 39, 21)), new Tolerance(5, 5, 5),1), new DTMSubPoint(new ColourPoint(new Point(3, 4), new Color( 74, 53, 24)), new Tolerance(5, 5, 5),1)};
		DTM stairs = new DTM(DTM_PT_1, DTM_PTS_1);
		
		DTMPoint DTM_PT_2 = new DTMPoint(new Color(135, 34, 0), new Tolerance(5, 5, 5));
		DTMSubPoint [] DTM_PTS_2 = { new DTMSubPoint(new ColourPoint(new Point(-2, -1), new Color( 135, 34, 0)), new Tolerance(5, 5, 5),1), new DTMSubPoint(new ColourPoint(new Point(0, 2), new Color( 135, 34, 0)), new Tolerance(5, 5, 5),1), new DTMSubPoint(new ColourPoint(new Point(2, -1), new Color( 135, 34, 0)), new Tolerance(5, 5, 5),1)};
		DTM player = new DTM(DTM_PT_2, DTM_PTS_2);
		
		DTMPoint DTM_PT_3 = new DTMPoint(new Color(63, 20, 13), new Tolerance(5, 5, 5));
		DTMSubPoint [] DTM_PTS_3 = { new DTMSubPoint(new ColourPoint(new Point(-5, 3), new Color( 88, 27, 18)), new Tolerance(5, 5, 5),1), new DTMSubPoint(new ColourPoint(new Point(1, 13), new Color( 88, 27, 18)), new Tolerance(5, 5, 5),1), new DTMSubPoint(new ColourPoint(new Point(6, 2), new Color( 102, 31, 21)), new Tolerance(5, 5, 5),1)};
		DTM boss = new DTM(DTM_PT_3, DTM_PTS_3);
		
		DTM[] specifics = {unknown, stairs, player, boss};
		
		// Gathering all boxes were rooms are
		Point firstMapRoom = new Point(199, 156);
		int roomSize = 27, roomDistance = 5;
		int[][] roomsMap = new int[16][];
		for (int i = 0; i <= 3; i++){
	        for (int j = 0; j <= 3; j++){
	        	roomsMap[(i*4) + j] = new int[] {firstMapRoom.x + i * (roomSize + roomDistance),
						        			  	firstMapRoom.y + j * (roomSize + roomDistance),
						        			  	firstMapRoom.x + i * (roomSize + roomDistance) + roomSize,
						        			  	firstMapRoom.y + j * (roomSize + roomDistance) + roomSize};
	        }
        }

		// Checking if there is something special about the room
		Color roomColor = new Color(168, 129, 56);
		Tolerance roomTolerance = new Tolerance(14, 17, 11);
		int check = 0;
		for (int i = 0; i < roomsMap.length; i++){
			int x1 = roomsMap[i][0],y1 = roomsMap[i][1],x2 = roomsMap[i][2],y2 = roomsMap[i][3];
			int identify = 0;
			for (int j = 0; j < specifics.length; j++){
				Point[] detect = DTMs.find_simple(specifics[j], x1, y1, x2, y2);
				if (detect.length > 0){
					identify++;
					check++;
					Point room = new Point(i / 4, i - ((i / 4) + 1));
					switch(j){
						case 0: println("Room (" + room.x + ", " + room.y + ") = Unknown"); break;
						case 1: println("Room (" + room.x + ", " + room.y + ") Stairs room"); break;
						case 2: println("Room (" + room.x + ", " + room.y + ") Player room"); break;
						case 3: println("Room (" + room.x + ", " + room.y + ") Boss room"); break;
					}
				}else{
					if (identify == 0){
						if (j == (specifics.length - 1)){
							ColourPoint[] colors = Screen.findColours(roomColor, x1, y1, x2, y2, roomTolerance);
							if (colors.length > 50){
								check++;
								println("Room " + i + " = Normal room");
							}
						}
					}
				}
			}
		}
		
		if (check > 0){
			Point CM = new Point(gen.randomRange(409, 422), gen.randomRange(69, 82));
			Mouse.move(CM);
			if (Timing.waitUptext("Close", 500)){
				Mouse.click(1);
				sleep(500, 1000);
				return true;
			}
		}
		
		return false;
	}
		
	public String[] getAllDoors(){
		int[][] data = org.tribot.api.Minimap.getRGBData();
		Color doorColor = new Color(240, 5, 7);
		Point north = new Point(143, 147),
				  east = new Point(172, 175),
				  south = new Point(143, 204),
				  west = new Point(115, 175);
		Point[] directions = {north, east, south, west},
				nextRooms = {new Point(0, -7), new Point(7, 0), new Point(0, 7), new Point(-7, 0)};
		String[] roomNames = {"north", "east", "south", "west"};
		ArrayList<String> found = new ArrayList<String>();

		for (int i = 0; i <= 3; i++){
			for (int j = 0; j <= 3; j++){
				for (int k = 0; k < directions.length; k++){
					// If there is a door the colors match, then check in the direction if there is a door.
					int x = directions[k].x + i*64,
						y = directions[k].y + j*64;
					Color mapColor = new Color(data[x][y]);
					
					if (Screen.coloursMatch(doorColor, mapColor, new Tolerance(10))){
						found.add(roomNames[k]);
						Color nextRoom = new Color(data[x + nextRooms[k].x][y + nextRooms[k].y]);
						if (Screen.coloursMatch(doorColor, nextRoom, new Tolerance(10))){
							println("Found an opened " + roomNames[k] + " door in room (" + i + ", " + j + ")");
						}else{
							println("Found an unopened " + roomNames[k] + " door in room (" + i + ", " + j + ")");
						}
					}
				}
			}
		}
		
		String[] result = found.toArray(new String[found.size()]);
		if (result.length > 0){
			return result;
		}

		return null;
	}
	
	public Point rotatePoint(Point P, float angle){
		/*
		 * If you rotate point (px, py) around point (ox, oy) by angle theta you'll get:

			p'x = cos(theta) * (px-ox) - sin(theta) * (py-oy) + ox
			p'y = sin(theta) * (px-ox) + cos(theta) * (py-oy) + oy
        
		 */
		
		Point center = new Point(627, 135);
		double rad = (angle / 360 * 2 * Math.PI);
		double Px = Math.cos(rad) * (P.x - center.x) - Math.sin(rad) * (P.y - center.y); 
		double Py = Math.sin(rad) * (P.x - center.x) + Math.cos(rad) * (P.y - center.y);

		Point PP = new Point(center.x + (int)Px, center.y + (int)Py);
		return PP;

	}
	
	public Point mmDoor(String location){
		String[] locations = {"north", "east", "south", "west"};
		int loc = -1;
		for (int i = 0; i < locations.length; i++){
			if (locations[i].equals(location)){
				loc = i;
				break;
			}
		}
		
		println("Loc: " + loc);
		
		if (loc != -1){
			Point[] coords = gen.getRoom();
			float angle = Minimap.getRotationAngle();
			Point[] rcoords = new Point[coords.length];

			for (int i = 0; i < coords.length; i++){
				rcoords[i] = rotatePoint(coords[i], angle);
			}
			
			Point temp = new Point(627, 135);
			for (int i = 0; i < rcoords.length; i++){
				switch(loc){
					case 0: if (rcoords[i].y < temp.y){
								temp.y = rcoords[i].y;
				   			}
				   			break;
				    case 1: if (rcoords[i].x > temp.x){
						        temp.x = rcoords[i].x;
			   			    }
				   		    break;
				    case 2: if (rcoords[i].y > temp.y){
					   		    temp.y = rcoords[i].y;
					        }
				            break;
				    case 3: if (rcoords[i].x < temp.x){
					   			temp.x = rcoords[i].x;
					        }
				            break;
			    }
		    }
	
			ArrayList<Point> pts = new ArrayList<Point>();
			for (int j = 0; j < rcoords.length; j++){
				switch(loc){
					case 0: case 2: if (Math.abs(rcoords[j].y - temp.y) <= 5){
										pts.add(coords[j]);
									}
									break;
					case 1: case 3: if (Math.abs(rcoords[j].x - temp.x) <= 5){
										pts.add(coords[j]);
									}
									break;
				}
			}
			
			int size = pts.size(),
			    sumX = 0, 
			    sumY = 0;
			
			for (int k = 0; k < size; k++){
				sumX = sumX + pts.get(k).x;
				sumY = sumY + pts.get(k).y;
			}

			//println("SumX: " + sumX);
			//println("SumY: " + sumY);
			//println("Size: " + size);
			
			Point d = new Point(sumX / size, sumY / size);
			return d;
		}	
		
		return null;
	}
	
	public String[] randomDoors(){
		String[] locations = {"north", "east", "south", "west"};
    	ArrayList<String> options = new ArrayList<String>();
    	for (int i = 0; i < locations.length; i++){
    		Point room = getMapRoom();
    		if (getDoors(room, locations[i])){
    			options.add(locations[i]);
    		}
    	}
    	
    	if (!options.isEmpty()){
    		int size = options.size();
	    	String[] randomOrder = new String[size];
	    	for (int i = 0; i < randomOrder.length; i++){
	    		boolean match = false;
	    		int random = gen.randomRange(0, size-1);
	    		String option = options.get(random);
	    		
	    		for (int j = 0; j < randomOrder.length; j++){
	    			if (randomOrder[j] == option){
	   	    			i--;
	    				match = true;
	    				break;
	    			}
	    		}
	    		
	    		if (!match){
	    			randomOrder[i] = option;
	    		}
	    	}
	    	
	    	/*
	    	for (int i = 0; i < randomOrder.length; i++){
	    		println(i + ": " + randomOrder[i]);
	    	}
	    	 */
	
	    	return randomOrder;
    	}
    	return null;
	}
	
	public boolean getDoors(Point room, String location){
		int[][] data = org.tribot.api.Minimap.getRGBData();
		Color doorColor = new Color(240, 5, 7);
		Point north = new Point(143, 147),
			  east = new Point(172, 175),
			  south = new Point(143, 204),
			  west = new Point(115, 175);
		Point[] directions = {north, east, south, west},
				nextRooms = {new Point(0, -7), new Point(7, 0), new Point(0, 7), new Point(-7, 0)};
		String[] roomNames = {"north", "east", "south", "west"};
		
		for (int i = 0; i < roomNames.length; i++){
			if (roomNames[i].contains(location)){
				int x = directions[i].x + room.x*64,
					y = directions[i].y + room.y*64;
				Color mapColor = new Color(data[x][y]);
				
				// If there is a door the colors match, then check in the direction if there is a door.
				if (Screen.coloursMatch(doorColor, mapColor, new Tolerance(20))){
					Color nextRoom = new Color(data[x + nextRooms[i].x][y + nextRooms[i].y]);
					if (Screen.coloursMatch(doorColor, nextRoom, new Tolerance(20))){
						println("Found an opened " + roomNames[i] + " door in room (" + room.x + ", " + room.y + ")");
						return true;
					}else{
						println("Found an unopened " + roomNames[i] + " door in room (" + room.x + ", " + room.y + ")");
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public void updateKeys(){
		Texture[] all = Textures.getAll();
		for (int i = 0; i < all.length; i++){
			if (all[i].x > 5 && all[i].x < 60){
				if (all[i].y > 110 && all[i].y < 135){
					println("C16: "+ all[i].crc16);
					//println("C32: "+ all[i].crc32);
				}
			}
		}

	}
	
    public static int[] toIntArray(List<Integer> list) {  
	    int[] intArray = new int[list.size()];  
	    int i = 0;  
	       
	    for (Integer integer : list)  
	    intArray[i++] = integer;  
	      
	    return intArray;  
    }  
	
    
    public Point[] getPath(Point endRoom){
		Point startRoom = getMapRoom(),
		      tempPos = startRoom,
			  testPos = startRoom;
		String[] directions = {"north", "east", "south", "west"};
		Point[] mod = {new Point(0, -1), new Point(1, 0),
				       new Point(0, 1), new Point(-1, 0)};

		//println("Start room: " + startRoom);
		//println("End room: " + endRoom);
		
		ArrayList<Point> route = new ArrayList<Point>();
		ArrayList<Point> checked = new ArrayList<Point>();
		ArrayList<Integer> doors  = new ArrayList<Integer>();
		checked.add(startRoom);
		while (!(tempPos.x == endRoom.x && tempPos.y == endRoom.y)){
			// Check where the doors are
			doors.clear();
			for (int i = 0; i < directions.length; i++){
				if (getDoors(tempPos, directions[i])){
					doors.add(i);
				}
			}
			
			//sleep(2000);
			//println("Found doors: " + doors.size());
			//println("Checked size: " + checked.size());
			
			// Check if there are any doors
			if (!doors.isEmpty()){
				for (int j = 0; j < doors.size(); j++){
					boolean beenBefore = false,
							stuck = false;
					int direction = doors.get(j);
					
					println("Temp pos: " + tempPos);
					
					testPos = new Point(tempPos.x + mod[direction].x, tempPos.y + mod[direction].y);
					
					if (checked.size() > 0){
						for (int k = 0; k < checked.size(); k++){
							if (checked.get(k).x == testPos.x && checked.get(k).y == testPos.y){
								beenBefore = true;
								if (j == doors.size()-1){
									stuck = true;
									int last = route.size()-1;
									if (last >= 0){
										tempPos = route.get(last-1);
										route.remove(last);
									}else{
										tempPos = startRoom;
										route.clear();
									}
								break;
								}
							}
						}
					}
					
					if (stuck){
						break;
					}
					
					if (beenBefore){
						continue;
					}
					
					tempPos = testPos;
					checked.add(tempPos);
					route.add(tempPos);
					break;
				}
			}else{
				return null;
			}
		}
		
		
		println("The correct path to get from (" + startRoom.x + ", " + startRoom.y + ") to (" + endRoom.x + ", " + endRoom.y + ")");
		Point[] path = route.toArray(new Point[route.size()]);
		
		if (path.length > 0){
			for (int i = 0; i < path.length; i++){
				println(path[i]);
			}
		}
		
		return path;
	}
    
    public boolean[][] makeMaze(){
		// Making the maze
		String[] directions = {"north", "east", "south", "west"};
		Point [] modifier81 = {new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0)};
		boolean[][] maze = new boolean[9][9];
		
		for (int i = 0; i <= 3; i++){
			for (int j = 0; j <= 3; j++){
				Point room16 = new Point(i, j);
				Point room81 = new Point(i*2+1, j*2+1);
				for (int k = 0; k < directions.length; k++){
					Point check81 = new Point(room81.x +  modifier81[k].x, room81.y + modifier81[k].y);

					if (getDoors(room16, directions[k])){
						maze[room81.x][room81.y] = true;
						maze[check81.x][check81.y] = true;
					}
				}
			}
		}
		
		return maze;
    }
    
    public void solve(Point end16){
    	boolean[][] maze = makeMaze();
    	
    	// Getting some positions
		Point start16 = getMapRoom(),
			  start81 = new Point(start16.x*2+1, start16.y*2+1),
			  end81 = new Point(end16.x*2+1, end16.y*2+1),
			  currentPos = start81,
			  tempPos = start81,
			  testPos = start81;
	
		println("Startpos 16 :" + start16);
		println("Startpos 81: " + start81);
		println("Endpos 16: " + end16);
		println("Endpos 81: " + end81);
		
		ArrayList<Point> checked = new ArrayList<Point>();
		ArrayList<Point> path = new ArrayList<Point>();
		Point[] mod = {new Point(0, -1), new Point(1, 0),
				       new Point(0, 1), new Point(-1, 0)};
		
		checked.add(start81);
		while (!(tempPos.x == end81.x && tempPos.y == end81.y)){	
			for (int i = 0; i < mod.length; i++){
				sleep(2500);
				boolean beenBefore = false;
				Point check = new Point(tempPos.x + mod[i].x, tempPos.y + mod[i].y);
				
				println("Current pos : " + tempPos);
				println("i = " + i + " and checking: " + check);
				
				// Check if the Point check has been checked yet
				if (checked.size() > 0){
					println("Checked: " + checked.size());
					for (int j = 0; j < checked.size(); j++){
						if (checked.get(j).x == check.x && checked.get(j).y == check.y){
							println("We have been here before");
							beenBefore = true;
							break;
						}
					}
				}
				
				if (beenBefore){
					continue;
				}
				
				checked.add(check);
				
				// If the Point check has not been checked and if it is not a wall, move there.
				println(check + " has not been checked");
				if (maze[check.x][check.y]){
					println("Found opened door at " + check);
					tempPos = check;
					path.add(tempPos);
					break;
				}else{
					if (i== 3){
						println("Back to start");
						//tempPos = start81;
						int last = path.size()-1;
						path.remove(last);
						tempPos = path.get(last-1);
						//path.clear();
					}
				}
			}
		}
		
		println("reached destination!!");
		
		println("Path length: " + path.size());
		for (int i = 0; i < path.size(); i++){
			println(path.get(i));
		}
    }
		
    public boolean findKeysOnMM(){
    	Point[] coords = gen.getRoom();
    	Texture[] all = Textures.getAll();
    	boolean found = false;
    	Point P = null;
    	
    	for (int i = 0; i < coords.length; i++){
    		if (!found){
	    		for (int j = 0; j < all.length; j++){
	    			if (all[j].crc16 == itemID){
		    			P = new Point(all[j].x, all[j].y);
		    			if (coords[i].x == P.x && coords[i].y == P.y){
		    				println("Found key");
		    				found = true;
		    				break;
		    			}
	    			}
	    		}
    		}
    	}
    	
    	if (found){
    		P = new Point(P.x + gen.randomRange(-3, 3), P.y + gen.randomRange(-3, 3));
    		Mouse.click(P, 1);
    		gen.waitUntilNotMoving();
    		return true;
    	}
    	
    	return false;
    }
		

    public boolean detectKeys(){
		// Looping through the results to know what keys have been found.
		for (int i = 0; i < KEYS.length; i++){
			ScreenModel[] keySearch = ScreenModels.find(KEYS[i]);
			if (keySearch.length > 0){
				println("Found " + keyNames[i] + " key");
				return true;
			}
		}
		return false;
	}
	
	public boolean pickUpKeys(){
		for (int i = 0; i < KEYS.length; i++){
			ScreenModel[] keySearch = ScreenModels.find(KEYS[i]);
			if (keySearch.length > 0){
				gen.fixRightClick();
				Point KP = new Point(keySearch[0].base_point.x + gen.randomRange(-5, 5), keySearch[0].base_point.y + gen.randomRange(-5, 5));
				if (gen.rightClicktAt(KP, "key", 1000)){
					grabbedKeys[i]++;
					gen.waitUntilNotMoving();
					return true;
				}else{
					pickUpKeys();
				}
			}
		}
		return false;
	}
	
	public boolean haveKeys(){
		for (int i = 0; i < grabbedKeys.length; i++){
			if (grabbedKeys.length > 0){
				return true;
			}
		}
		return false;
	}
	
	public boolean detectLockedDoors(){
		long[] doorIDs = FROZEN_LOCKED_DOORS;

		// Looping through the results to know what doors have been found.
		for (int i = 0; i < doorIDs.length; i++){
			ScreenModel[] doorSearch = ScreenModels.find(doorIDs[i]);
			if (doorSearch.length > 0){
				println("Detected locked" + doorNames[i] + " door");
				foundDoors[i+8]++;
				return true;
			}
		}
		return false;
	}
	
	public boolean openNearestDoor(){
		//long[][] doorIDs = {FROZEN_DOORS, FROZEN_LOCKED_DOORS, FROZEN_KEY_DOORS};
		int length1 = FROZEN_DOORS.length,
			length2 = FROZEN_LOCKED_DOORS.length,
			length3 = FROZEN_KEY_DOORS.length,
			totalLength = length1 + length2 + length3;

		long[] doorIDs = new long[totalLength];
		for (int i = 0; i < length1; i++){
			doorIDs[i] = FROZEN_DOORS[i];
		}
		for (int i = 0; i < length2; i++){
			doorIDs[i+length1] = FROZEN_LOCKED_DOORS[i];
		}
		for (int i = 0; i < length3; i++){
			doorIDs[i+length1+length2] = FROZEN_KEY_DOORS[i];
		}

		long ID = gen.getNearestID(doorIDs);
		
		for (int i = 0; i < FROZEN_LOCKED_DOORS.length; i++){
			if (ID == FROZEN_LOCKED_DOORS[i]){
				println("locked door");
				if (openLockedDoor()){
					if (openKeyDoor()){
						return true;
					}
				}
			}
		}
		
		for (int i =0; i < FROZEN_KEY_DOORS.length; i++){
			if (ID == FROZEN_KEY_DOORS[i]){
				println("Key door");
				if (openKeyDoor()){
					return true;
				}
			}
			
		}
		
		for (int i = 0; i < FROZEN_DOORS.length; i++){
			if (ID == FROZEN_DOORS[i]){
				println("Normal door");
				if (openDoor()){
					return true;
				}
			}
		}
	
		return false;
	}

	public boolean detectDoors(){
		long[] doorIDs = FROZEN_DOORS;

		// Looping through the results to know what doors have been found.
		for (int i = 0; i < doorIDs.length; i++){
			ScreenModel[] doorSearch = ScreenModels.find(doorIDs[i]);
			if (doorSearch.length > 0){
				println("Detected " + doorNames[i] + " door");
				foundDoors[i]++;
				return true;
			}
		}
		return false;
	}
	
	public boolean detectKeyDoors(){
		long[] doorIDs = FROZEN_KEY_DOORS;

		// Looping through the results to know what doors have been found.
		for (int i = 0; i < doorIDs.length; i++){
			ScreenModel[] doorSearch = ScreenModels.find(doorIDs[i]);
			if (doorSearch.length > 0){
				println("Detected " + keyDoorNames[i] + " door");
				foundDoors[i]++;
				return true;
			}
		}
		return false;
	}
	
	public boolean openLockedDoor(){
		long[] doorIDs = FROZEN_LOCKED_DOORS;
		
		// Checking if we have the right key
		for (int i = 0; i < doorIDs.length; i++){
			ScreenModel[] doorSearch = ScreenModels.find(doorIDs[i]);
			if (doorSearch.length > 0){
				for (int j = 0; j < doorSearch.length; j++){
					println("Detected locked" + keyDoorNames[i] + " door");

					int key = -1;
					switch(i){
						case 3: key = 0; break;
						case 5: key = 1; break;
						case 7: key = 2; break;
						case 9: key = 3; break;
						case 11: key = 4; break;
						case 13: key = 5; break;
						case 15: key = 6; break;
						case 17: key = 7; break;
					}
					
					println(key);
					println(grabbedKeys[5]);
					
					if (key != -1){
						if (grabbedKeys[key] > 0){
							println("We can open it");
							Point DP = new Point(doorSearch[j].points[0]);
							if (gen.randomSelectOptionAt(DP, "Unlock", "Unlock", 0, 1, 1000)){
								return true;
							}else{
								openLockedDoor();
							}
						}else{
							println("We cant open it");
						}
					}
				}
			}
		}

		return false;
	}
	
	public boolean openKeyDoor(){
		long[] doorIDs = FROZEN_KEY_DOORS;
		
		long id = gen.getNearestID(doorIDs);
		if (id != -1){
			Point P = gen.getNearestPoint(id);
			if (P != null){
				if (gen.randomSelectOptionAt(P, "Open", "Open", 2, 1, 1000)){
					return true;
				}else{
					openKeyDoor();
				}
			}
		}

		return false;
	}
	
	public boolean openDoor(){
		long[] doorIDs = FROZEN_DOORS;
		
		long id = gen.getNearestID(doorIDs);
		if (id != -1){
			Point P = gen.getNearestPoint(id);
			if (P != null){
				if (gen.randomSelectOptionAt(P, "Enter", "Enter", 2, 1, 1000)){
					return true;
				}else{
					openDoor();
				}
			}
		}

		return false;
	}
	
	
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
