package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.tribot.api.ChooseOption;
import org.tribot.api.Constants;
import org.tribot.api.Game;
import org.tribot.api.Inventory;
import org.tribot.api.EGW;
import org.tribot.api.NPCChat;
import org.tribot.api.Player;
import org.tribot.api.ScreenModels;
import org.tribot.api.Textures;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.Texture;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Fishing", name = "JJ's Karamja Fisher")
public class JJsKaramjaFisher extends Script implements Painting{
	private final Point[] spotToStiles = {new Point(2924, 3180), new Point(2924, 3179), new Point(2924, 3175), new Point(2918, 3172), new Point(2912, 3173), new Point(2906, 3172), new Point(2901, 3169), new Point(2901, 3162), new Point(2896, 3157), new Point(2894, 3152), new Point(2892, 3143), new Point(2887, 3141), new Point(2880, 3141), new Point(2874, 3143), new Point(2868, 3147), new Point(2862, 3148), new Point(2858, 3146), new Point(2852, 3143)},
			      		  stilesToSpot = {new Point(2852, 3143), new Point(2858, 3146), new Point(2862, 3148), new Point(2868, 3147), new Point(2874, 3143), new Point(2880, 3141), new Point(2887, 3141), new Point(2892, 3143), new Point(2894, 3152), new Point(2896, 3157), new Point(2901, 3162), new Point(2901, 3169), new Point(2906, 3172), new Point(2912, 3173), new Point(2918, 3172), new Point(2924, 3175), new Point(2924, 3179), new Point(2924, 3180)};
	private final long playerID = getPlayersID(),
			   		   spotID = 2704514198L,
			   		   stilesID = 3368455371L,
			   		   startTime = System.currentTimeMillis();
	private final int tunaTexture = 45488,
			          lobsterID = 10377361,
			          swordfishID = 1148772,
			          tunaID = 1094282,
			          minimiseTexture = 53103,
			          openTexture = 36897,
			          dropKey = 61;
	private final int[] ids = {tunaID, lobsterID, swordfishID};
	private final Rectangle tunaSlot = new Rectangle(445, 348, 32, 28),
							firstInvSlot = new Rectangle(563, 263, 32, 36);
	private final BufferedImage bg = getImage("http://i45.tinypic.com/xd809g.png");
	
	private int[][] invPrevious = getInventoryIDs();
	private int xpGained = 0,
			    lobstersCaught = 0,
			    tunaCaught = 0,
			    swordfishCaught = 0;
	private String fishName;
	private boolean dropTuna,
					startPaint = false,
					tunaOnActionBar = false;
	private STATUS status = STATUS.FISHING;

	// Draws the on-screen paint
	public void onPaint(Graphics g){
		if(startPaint){
			// FILLING CHATBOX
			g.drawImage(bg, 7, 395, null);
			//g.setColor(new Color(218, 200, 164));
			//g.fillRect(7, 395, 506, 128);
			
			// CALCULATIONS
			long timeRan = System.currentTimeMillis()-startTime;
			int xpH = toHour(xpGained, timeRan),
			    lobsterH = toHour(lobstersCaught, timeRan),
			    tunaH = toHour(tunaCaught, timeRan),
			    swordfishH = toHour(swordfishCaught, timeRan);
		
			// HEADER
			g.setColor(new Color(233, 179, 0));
			g.setFont(new Font ("Tahoma", Font.BOLD, 20));
			g.drawString("JJ's Karamja Fisher", 10, 415);
			
			// LEFT PART
			g.setFont(new Font ("Tahoma", Font.BOLD, 14));
			g.setColor(Color.WHITE);
			g.drawString("Running for: " + Timing.msToString(timeRan), 10, 435);
			g.drawString("Status: " + status, 10, 455);
			g.drawString("Fishing for: " + fishName, 10, 475);
			if(fishName.equals("Swordfish")){
				g.drawString("Dropping tuna: " + dropTuna, 10, 495);
				g.drawString("Gained: " + xpGained + " xp at " + xpH + " p/h", 10,  515);
			}else{
				g.drawString("Gained: " + xpGained + " xp at " + xpH + " p/h", 10,  495);
			}
			
			// RIGHT PART
			g.drawString("Caught: " + lobstersCaught + " lobsters at " + lobsterH + " p/h", 280, 430);
			g.drawString("Caught: " + tunaCaught + " tuna at " + tunaH + " p/h", 280, 470);
			g.drawString("Caught: " + swordfishCaught + " swordfish at " + swordfishH + " p/h", 280, 515);
		
			// DRAWING EXTRA STUFF BASED ON STATUS
			switch(status){
				case FISHING:
					// Draws the fishing spots on the mainscreen
					g.setColor(Color.GREEN);
					ScreenModel[] spots = ScreenModels.find(spotID);
					if(spots.length > 0){
						for(ScreenModel model : spots){
							g.drawPolygon(model.getEnclosedArea());
						}
					}
					break;
				
				case DROPPING_TUNA:
					// Drops the tuna in our inventory to make space for swordfish
					
					break;
				
				case WALKING_TO_STILES: 
					// Draws the path we are walking on the minimap
					Point[] stilesPath = pathToDraw(spotToStiles);
					if(stilesPath != null){
						if(stilesPath.length > 1){
							g.setColor(Color.RED);
							for(int i = 1; i < stilesPath.length; i++){
								g.drawLine(stilesPath[i].x, stilesPath[i].y, stilesPath[i-1].x, stilesPath[i-1].y);
								g.fillRect(stilesPath[i].x-1, stilesPath[i].y-1, 3, 3);
							}
						}
					}
					break;
					
				case CONVERTING:
					// Draws the boundaries of Stiles
					g.setColor(Color.BLUE);
					ScreenModel[] stiles = ScreenModels.find(stilesID);
					if(stiles.length > 0){
						for(ScreenModel model : stiles){
							g.drawPolygon(model.getEnclosedArea());
						}
					}
					break;
				
				case WALKING_TO_LOBSTERS:
					// Draws the path we are walking on the minimap
					Point[] lobsterPath = pathToDraw(stilesToSpot);
					if(lobsterPath != null){
						if(lobsterPath.length > 1){
							g.setColor(Color.RED);
							for(int i = 1; i < lobsterPath.length; i++){
								g.drawLine(lobsterPath[i].x, lobsterPath[i].y, lobsterPath[i-1].x, lobsterPath[i-1].y);
								g.fillRect(lobsterPath[i].x-1, lobsterPath[i].y-1, 3, 3);
							}
						}
					}
					break;
			}
		}
	}
	
	// Converts a certain detail in a certain timeframe to details/hour
	private int toHour(double detail, long time){
		return (int) ((3600000 * detail) / time);
	}
	
	// Attempts to get an image from an URL
	private BufferedImage getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }
	
	// Enum of possible statuses
	private enum STATUS{
		FISHING, WALKING_TO_STILES, CONVERTING, WALKING_TO_LOBSTERS, DROPPING_TUNA;
	}

	// Sets the status according to the player position & inventory.
	private void setStatus(){
		if(Inventory.isFull()){
			if(dropTuna){
				if(haveTuna()){
					status = STATUS.DROPPING_TUNA;
					return;
				}
			}
			if(ScreenModels.find(spotID).length > 0){
				status = STATUS.WALKING_TO_STILES;
			}else{
				if(ScreenModels.find(stilesID).length > 0){
					status = STATUS.CONVERTING;
				}
			}
		}else{
			if(ScreenModels.find(spotID).length > 0){
				status = STATUS.FISHING;
			}else{
				status = STATUS.WALKING_TO_LOBSTERS;
			}
		}
	}
	
	// Returns a random number between the boundaries
	private int randomRange(int aFrom, int aTo){
		return (aTo - Constants.RANDOM.nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	// Returns an array of point to draw
	private Point[] pathToDraw(Point[] path){
		Point myPos = EGW.getPosition(),
		  	  mmc = new Point(Constants.MMCX, Constants.MMCY);
		double nearest = 10000;
		int index = 0;
		
		// Getting the nearest position in the path
		for(int i = 0; i < path.length; i++){
			Point p = path[i];
			double distance = Math.abs(p.x-myPos.x) + Math.abs(p.y-myPos.y);
			if(distance < nearest){
				nearest = distance;
				index = i;
			}
		}
		index++;

		// Adding points to our arraylist
		ArrayList<Point> added = new ArrayList<Point>();
		added.add(mmc);
		for(int i = index; i < path.length; i++){
			Point p = path[i];
			double distance = Math.abs(p.x-myPos.x)*4 + Math.abs(p.y-myPos.y)*4;
			if(distance <= 100){
				added.add(new Point(mmc.x+((p.x-myPos.x)*4), mmc.y+((myPos.y-p.y)*4)));
			}
		}

		if(!added.isEmpty()){
			Point[] result = new Point[added.size()];
			added.toArray(result);
			return result;
		}
		
		return null;
	}
	
	// Attempts to find a fishing spot to cage for lobsters
	private boolean cageSpot(){
		ScreenModel[] spots = ScreenModels.findNearest(spotID);
		if(spots.length > 0){
			for(ScreenModel model : spots){
				int i = randomRange(0, model.points_x.length-1);
				Point p = new Point(model.points_x[i], model.points_y[i]);
				Mouse.move(p);
				if(Timing.waitUptext("Cage", 500)){
					if(fishName.equals("Swordfish") || fishName.equals("Tuna")){
						String uptext = Game.getUptext();
						if(uptext != null){
							Pattern pattern = Pattern.compile("\\d+");
							Matcher matcher = pattern.matcher(uptext);
							if(matcher.find()) {
								int options = Integer.parseInt(matcher.group());
								if(options <= 15){
									Mouse.click(3);
									if(Timing.waitChooseOption("Harpoon", 500)){
										sleep(2000, 3000);
										return true;
									}
								}
							}
						}
					}else{
						Mouse.click(1);
						sleep(2000, 3000);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// Converts lobster to notes at Stiles
	private boolean convertLobsters(){
		ScreenModel[] stiles = ScreenModels.find(stilesID);
		if(stiles.length > 0){
			int i = randomRange(0, stiles[0].points_x.length-1);
			Point p = new Point(stiles[0].points_x[i], stiles[0].points_y[i]);
			Mouse.move(p);
			if(Timing.waitUptext("Stiles", 500)){
				Mouse.click(3);
				if(Timing.waitChooseOption("Exchange", 500)){
					long t = System.currentTimeMillis();
					while(Timing.timeFromMark(t) < 5000){	
						String name = NPCChat.getName();
						if(name != null){
							if(name.contains("Stiles")){
								//NPCChat.clickContinue(true);
								return true;
							}
						}
						sleep(200, 400);
					}
				}
			}
		}
		return false;
	}
	
	// Attempts to grab our player ID
	private long getPlayersID(){
		ArrayList<Long> perhaps = new ArrayList<Long>();
		ScreenModel[] all = ScreenModels.getAll();
		for(ScreenModel model : all){
			if(model.base_point.x >= 247 && model.base_point.x <= 267){
				if(model.base_point.y >= 210 && model.base_point.y <= 239){
					perhaps.add(model.id);
				}
			}
		}
		
		if(!perhaps.isEmpty()){
			return perhaps.get(perhaps.size()-1);
		}
		
		return -1;
	}
	
	// Returns true if we have tuna in our inventory
	private boolean haveTuna(){
		return Inventory.find(tunaID).length > 0;
	}

	// Returns true if we are fishing
	private boolean areFishing(){
		return ScreenModels.find(playerID).length == 0;
	}
	
	// Returns a random point inside a rectangle
	private Point ptInRect(Rectangle r){
		return new Point(r.x + Constants.RANDOM.nextInt(r.width), r.y + Constants.RANDOM.nextInt(r.height));
	}
	
	// Returns true if the action bar is open
	private boolean isActionBarOpen(){
		return Textures.find(minimiseTexture).length > 0;
	}
	
	// Returns true if our action bar contains tuna on the correct hotkey "="
	private boolean tunaInCorrectSlot(){
		if(tunaOnActionBar){
			return true;
		}
		Texture[] tuna = Textures.find(tunaTexture);
		if(tuna.length > 0){
			double distance = Math.abs(tuna[0].x - tunaSlot.x) + Math.abs(tuna[0].y - tunaSlot.y);
			println("distance: " + distance);
			return distance <= 5;
		}
		return false;
	}

	// Returns true if we succesfully opened the action bar
	private boolean openActionBar(){
		Texture[] open = Textures.find(openTexture);
		if(open.length > 0){
			for(Texture t : open){
				Rectangle r = new Rectangle(t.x, t.y, t.width, t.height);
				Point p = ptInRect(r);
				Mouse.move(p);
				if(Timing.waitUptext("Expand", 500)){
					Mouse.click(1);
					sleep(500, 1000);
					return true;
				}
				
			}
		}
		
		return false;
	}
	
	// Attempts to add tuna to the action bar
	private boolean putTunaOnActionBar(){
		Texture[] tuna = Textures.find(tunaTexture);
		if(tuna.length > 0){
			Rectangle t = new Rectangle(tuna[0].x, tuna[0].y, tuna[0].width, tuna[0].height); 
			Point tunaLoc = ptInRect(t);
			Point slotLoc = ptInRect(tunaSlot);
			Mouse.drag(tunaLoc, slotLoc, 1);
			return true;
		}else{
			InventoryItem[] tunas = Inventory.find(tunaID);
			if(tunas.length > 0){
				int r = randomRange(0, tunas.length-1);
				Point p = new Point(tunas[r].x + randomRange(0, 30), tunas[r].y + randomRange(0, 30));
				Point slot = ptInRect(tunaSlot);
				Mouse.drag(p, slot, 1);
				return true;
			}
		}
		return false;
	}

	// Drops all tuna
	private boolean dropTuna(){
		if(!isActionBarOpen()){
			openActionBar();
		}
		
		if(!tunaInCorrectSlot()){
			tunaOnActionBar = putTunaOnActionBar();
		}
		
		while(Inventory.find(tunaID).length > 0){
			// Unwanted clicks, interface bothering us
			String[] chat = NPCChat.getOptions();
			for(String s : chat){
				if(s.contains("carry any more")){
					NPCChat.clickContinue(true);
				}
			}

			// Pressing the key to drop
			Keyboard.pressKey((char) dropKey);
			sleep(50, 100);
			Keyboard.releaseKey((char) dropKey);
			
			// Waiting a while
			sleep(200, 400);
		}
		return true;
	}
	
	// Check if run mode is on.
	private void checkRun(){
		if(!Game.isRunModeOn()){
			Game.setRunMode(true);
		}
	}
	
	// Walks a path
	private void walk(Point[] path){
		checkRun();
		EGW.walkPath(path);
		while(Player.isMoving()){
			sleep(200, 400);
		}
	}
	
	// Gets the Inventory IDs in an array from left -> right, up -> down
	private int[][] getInventoryIDs(){
		int[][] result = new int[4][7];
		InventoryItem[] all = Inventory.getAll();
		if(all.length > 0){
			for(InventoryItem item : all){
				int x = (item.x-firstInvSlot.x)/firstInvSlot.width;
				int y = (item.y-firstInvSlot.y)/firstInvSlot.height;
				result[x][y] = item.id;
				//println("x: " + x + " y : " + y);
			}
		}
		return result;
	}
	
	// Calculates the xp rates
	private void xpRate(){
		int[][] invNow = getInventoryIDs();
		
		for(int x = 0; x < invNow.length; x++){
			for(int y = 0; y < invNow.length; y++){
				if(invNow[x][y] != 0){
					if(invNow[x][y] != invPrevious[x][y]){
						//println("Not the same as before");
						for(int k = 0; k < ids.length; k++){
							if(invNow[x][y] == ids[k]){
								xpGained += 80+k*10;
								switch(k){
									case 0: tunaCaught++; break;
									case 1: lobstersCaught++; break;
									case 2: swordfishCaught++; break;
								}
							}
						}
					}
				}
			}
		}
		
		invPrevious = invNow;
	}
	
	
	// The mainloop, based on the status
	private void mainloop(){
		setStatus();
		checkRun();
		
		// Getting rid of open interfaces
		while(ChooseOption.isOpen()){
			Point p = new Point(randomRange(10, 515), randomRange(57, 309));
			Mouse.move(p);
			sleep(300, 600);
		}
		
		// Performing the task that we should do according to our status
		switch(status){
			case FISHING:
				if(!areFishing()){
					cageSpot();
				}
				xpRate();
				break;
			
			case DROPPING_TUNA:
				dropTuna();
				break;
				
			case CONVERTING:
				convertLobsters();
				break;
				
			case WALKING_TO_LOBSTERS:
				walk(stilesToSpot);
				break;
				
			case WALKING_TO_STILES:
				walk(spotToStiles);
				break;
		}
		
		sleep(500, 1000);
	}

	@Override
    public void run() {
		// Makes a reference to the GUI and creates it
		final JJsKaramjaFisherGUI gui = new JJsKaramjaFisherGUI();
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				gui.create();
			}
    	});
		
		// Waits until the user clicks on Start, then grabs the details
		while(!gui.isFinished()){
			sleep(100, 200);
		}
		fishName = gui.getFish();
		dropTuna = gui.areDropping();
		startPaint = true;

		// While we are logged in, repeat mainloop.
		Mouse.setSpeed(150);
		while(ScreenModels.getAll().length > 0){
			mainloop();
		}
    }
}