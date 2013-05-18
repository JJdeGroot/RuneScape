package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.tribot.api.Minimap;
import org.tribot.api.Textures;
import org.tribot.api.ScreenModels;
import org.tribot.api.TPS;
import org.tribot.api.DTMs;
import org.tribot.api.Screen;
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
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.util.Util;

@ScriptManifest(authors = { "J J" }, category = "Dungeoneering", name = "Dung V5")
public class Dungeoneering extends Script implements Painting{
	Monsters monsters = new Monsters();
	General gen = new General();
	Bosses bosses = new Bosses();
	Solving solving = new Solving();
	Paint paint = new Paint();

	String status = "starting up";

	public void onPaint(Graphics g){
        g.setColor(Color.YELLOW);
        g.drawString("JJ's Dungeons", 5, 160);
        g.setColor(Color.WHITE);
        g.drawString("Status: " + status, 5, 175);
        
        // Getting the room we are in
        Point MR = solving.getMapRoom();
        if (MR != null){
        	g.drawString("Room: (" + MR.x + ", " + MR.y + ")", 5, 190);
        }
        
        // Drawing the map image
        g.drawImage(solving.smallMap, 384, 395, null);
        
        //Drawing the position on the map
        Point MP = solving.getMapPos();
        if (MP != null){
        	g.drawRect(MP.x - 1, MP.y - 1, 2, 2);
        }
        
        // Drawing the room numbers on the map
        for (int i = 0; i <= 3; i++){
	        for (int j = 0; j <= 3; j++){
	        	g.drawString("(" + i + ", " + j + ")", 377 + j * 32 + 10, 395 + i * 32 + 15);
	        }
        }
        
        // Drawing rotation
        Point[] theRoom = gen.getRoom();
        float theAngle = Minimap.getRotationAngle();
        for (int i = 0; i < theRoom.length; i++){
        	Point PPP = solving.rotatePoint(theRoom[i], theAngle);
        	g.drawLine(PPP.x, PPP.y, PPP.x, PPP.y);
		}
        
        /*
        // Drawing the path on the map
        Point[] thePath = solving.getPath(new Point(1, 0));
        if (thePath.length > 0){
        	for (int k = 0; k < (thePath.length-1); k++){
        		int x1 = 215 + thePath[k].x * 30;
        		int y1 = 173 + thePath[k].y * 30;
        		int x2 = (thePath[k+1].x - thePath[k].x) * 30 + x1;
        		int y2 = (thePath[k+1].y - thePath[k].y) * 30 + y1;
        		g.drawLine(x1, y1, x2, y2);
        	}
        }
        */
    }
	
	public void dumpMaps(int choice) {
        long t = System.currentTimeMillis();
       
        final int[][] data = org.tribot.api.Minimap.getRGBData();

        final BufferedImage bi = new BufferedImage(data.length, data[0].length,
                        BufferedImage.TYPE_INT_RGB);
        
        // Gathering room locations        
        int roomSize = 59, roomDistance = 5;
 		Point firstMinimapRoom = new Point(114, 146);
 		int[][] roomsMM = new int[25][];
 		for (int i = 0; i <= 3; i++){
 	        for (int j = 0; j <= 3; j++){
 	        	roomsMM[(i*4) + j] = new int[] {firstMinimapRoom.x + i * (roomSize + roomDistance),
					 	        			    firstMinimapRoom.y + j * (roomSize + roomDistance),
					 	        			    firstMinimapRoom.x + i * (roomSize + roomDistance) + roomSize,
					 	        			    firstMinimapRoom.y + j * (roomSize + roomDistance) + roomSize};
 	        }
         }

        for (int x = 0; x < bi.getWidth(); x++){
                for (int y = 0; y < bi.getHeight(); y++){
                		Point P = new Point(x, y);
                		if (P.x >= roomsMM[choice][0]){
                			if (P.x <= roomsMM[choice][2]){
                				if (P.y >= roomsMM[choice][1]){
                					if (P.y <= roomsMM[choice][3]){
                						bi.setRGB(x, y, data[x][y]);
                					}
                				}
                			}
                		}
                        
                }
        }

        File file = new File(Util.getAppDataDirectory(), t + ".png");

        try {
                ImageIO.write(bi, "png", file);
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
	}
	
	private void tester(){
		/*
		Texture[] all = Textures.getAll();
		for (int i = 0; i < all.length; i++){
			println("CRC 16 @ " + i + ": " + all[i].crc16);
			println("CRC 32 @ " + i + ": " + all[i].crc32);
		}
		*/
		
		Texture[] npc = Textures.find(24327);
		if (npc.length > 0){
			println("Found NPC dot at: " + npc[0].x + ", " + npc[0].y);
		}
		
		/*
		ScreenModel[] test = ScreenModels.getAll();
				//ScreenModels.find(1969115135L, 1338366387L, 2846886962L, 346379166L, 2846886962L, 142551560L, 3543694631L, 2048859797L, 1589511840L, 674224873L, 236354172L, 1681796192L); 
		for (int i = 0; i <test.length; i++){
			if (test[i].element_count > 1000){
				println("ID: " + test[i].id);
				println("elements: " + test[i].element_count);
				println("ID2: " + test[i].id2);
				Mouse.move(test[i].base_point);
				sleep(2000);
				
			}
		}
		*/

	}
	
	private void MMtoMS(){
		Point MMc = new Point(627, 135),
			  MSc = new Point(259, 220);
		int npcID = 24327;
		
		Texture[] npc = Textures.find(npcID);
		if (npc.length > 0){
			for (int i = 0; i < npc.length; i++){
				println("Found NPC dot at: " + npc[i].x + ", " + npc[i].y);
				Point MS = new Point((npc[i].x - MMc.x) * 9 + 13 + MSc.x, (npc[i].y - MMc.y) * 6 + 41 + MSc.y);
				println("Search: " + MS);
				
				ScreenModel[] all = ScreenModels.getAll();
				if (all.length > 0){
					for (int j = 0; j < all.length; j++){
						if (all[j].element_count > 1000){
							if (all[j].base_point.x > MS.x-50 && all[j].base_point.x < MS.x+50){
								if (all[j].base_point.y > MS.y-50 && all[j].base_point.y < MS.y+50){
									Point P = all[j].base_point;
									Mouse.move(P);
									if (Timing.waitUptext("Attack", 10)){
										println("Attack!");
										break;
									}
									//sleep(3000);
								}
							}
						}
					}
				}
				
				//9x+13
				//6x + 41
				
				
			}
		}
	}
	
	@Override
    public void run() {
    	println("JJ's Dungeons test");
    	long start = System.currentTimeMillis();
    	//solving.getLocation();
    	
    	//solving.getMap();
    	//solving.getMapPos();
    	
    	//solving.getMap();
    	//solving.getAllDoors();
    	
    	
    	//solving.getPath(new Point(0, 1));
    	//bosses.icefiend();
    	//bosses.astea();
    	
    	//solving.solve(new Point(3, 3));
    	//solving.getPath(new Point(0, 2));
    	
    	//tester();
    	
    	//MMtoMS();
    	
    	
    	//solving.findKeysOnMM();
    	
    	//solving.getPath(new Point(1, 3));
    	
    	/*
    	// MAINLOOP
    	// 1. Start a new Dungeon on complexity 4
    	
    	
    	// 2. Grab monster & texture ID
    	if (monsters.npcID == -1){
    		monsters.npcID = gen.getTexture("Npc");
    	}
    	
    	if (solving.itemID == -1){
    		solving.itemID = gen.getTexture("Item");
    	}
    	
    	// 3. Pick up the key in the starting room if there is one
    	if (solving.detectKeys()){
    		solving.pickUpKeys();
    	}

    	// 4. Move to a random door and check if we can open it.
    	String[] doors = solving.randomDoors();
    	for (int i = 0; i < doors.length; i++){
    		println(doors[i]);
    	}
    	for (int i = 0; i < doors.length; i++){
    		Point P = solving.mmDoor(doors[i]);
    		Mouse.move(P);
    		Mouse.click(1);
    		gen.waitUntilNotMoving();
    		if (solving.openNearestDoor()){
    			break;
    		}
    	}
    	*/
    	
    	bosses.icyBones();
  
    	
    	
    	
    	
    	
    	
    	
   
    	/*
    	String[] locations = {"north", "east", "south", "west"};
    	for (int i = 0; i < locations.length; i++){
    		Point room = solving.getMapRoom();
    		if (solving.getDoors(room, locations[i])){
    			println("found " + locations[i] + " door");
    			Point P = solving.mmDoor(locations[i]);
    			println(locations[i] + ": " + P);
    			if (P != null){
    				Mouse.move(P);
    				Mouse.click(1);
    				break;
     			}
        	}else{
        		println("No " + locations[i] + " door found");
        	}
    	}
    	*/
    	
    	/*
    	//String[] locations = {"north", "east", "south", "west"};
    	ArrayList<String> options = new ArrayList<String>();
    	for (int i = 0; i < locations.length; i++){
    		Point room = solving.getMapRoom();
    		if (solving.getDoors(room, locations[i])){
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
	    	
	    	for (int i = 0; i < randomOrder.length; i++){
	    		println(i + ": " + randomOrder[i]);
	    	}
    	}
    	*/
    	
    	
    	
    	
	    	
    	
    	
    	
    	
    	/*
    	String[] doorLocs = solving.getAllDoors();
    	if (doorLocs.length > 0){
    		for (int i = 0; i < doorLocs.length; i++){
    			println(doorLocs[i]);
    			Point P = solving.mmDoor(doorLocs[i]);
    			if (P != null){
    				println(doorLocs[i] + ": " + P);
    			}
    		}
    	}
    	*/
    	
    	
    	
    	
    	
    	
   	
    	/*
    	solving.updateKeys();
    	
    	// Finding keys on MM, walking there and picking up
    	if (solving.findKeysOnMM()){
    		solving.pickUpKeys();
    	}
    	
  
    	// Finding monsters and killing them
    	while (monsters.findMonsters()){
    		monsters.walkToMonsters();
    		monsters.killMonsters();
    	}
    	*/

    	
    	
    	//solving.getDoors(new Point(0, 1), "east");
    	
    	//println(solving.getDoors(new Point(0, 1), "east"));
    	//solving.getPath(new Point(2, 1));
    	//solving.getAllDoors();

    	//test();
    	
    	//solving.getAllDoors();
    	//solving.getMap();

    	//sleep(5000000);

    	/* map etc
    	DPS();
    	dumpMaps(1);
    	*/
 
    	
    	/*
    	if (monsters.walkToMonsters()){
    		monsters.killMonsters();
    	}
    	*/
    	//bosses.behemoth();
    	
    	/*
    	if (solving.detectKeys()){
    		solving.pickUpKeys();
    	}
    	
    	if (solving.detectLockedDoors()){
    		solving.grabbedKeys[5]++;
    		if (solving.haveKeys()){
    			solving.openLockedDoor();
    		}
    	}
    	
    	if (solving.detectDoors()){
    		solving.openDoor();
    	}
    	*/


    	
    	println("Took: " + (long)(System.currentTimeMillis()-start) + " milliseconds");
    }
}