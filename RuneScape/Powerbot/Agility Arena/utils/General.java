package org.obduro.agilityarena.utils;

import java.awt.Point;
import java.util.Set;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.HintArrow;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class General {
	final private static int[] dispenserIDs = {3608, 3581};
	final private static int[] platformIDs = {3608, 3581, 3618};
	final private static int ticketID = 2996;
	final private static Tile nwDispenser = new Tile(2761, 9590, 3);
	
	final public static long startTime = System.currentTimeMillis();
	final public static int startTickets = countTickets();
	
	//public static Tile lastTag = new Tile(0, 0, 0);
	//public static long lastTagTime = System.currentTimeMillis();
	public static int tags = 0;
	
	// FROM THE GUI
	public static boolean bankingEnabled = false;
	public static int healPercentage = 15;

	public static final Obstacle[][] grid =
		{{Obstacle.PLATFORM, Obstacle.BLADE, Obstacle.PLATFORM, Obstacle.FLOOR_SPIKES, Obstacle.PLATFORM, Obstacle.HAND_HOLDS, Obstacle.PLATFORM, Obstacle.PILLARS, Obstacle.PLATFORM},
		{Obstacle.BALANCING_LEDGE, Obstacle.EMPTY, Obstacle.LOG_BALANCE, Obstacle.EMPTY, Obstacle.ROPE_SWING, Obstacle.EMPTY, Obstacle.PLANK, Obstacle.EMPTY, Obstacle.BALANCING_LEDGE},
		{Obstacle.PLATFORM, Obstacle.PRESSURE_PAD, Obstacle.PLATFORM, Obstacle.MONKEY_BARS, Obstacle.PLATFORM, Obstacle.BALANCING_ROPE, Obstacle.PLATFORM, Obstacle.FLOOR_SPIKES, Obstacle.PLATFORM},
		{Obstacle.LOW_WALL, Obstacle.EMPTY, Obstacle.SPINNING_BLADE, Obstacle.EMPTY, Obstacle.STAT_DARTS, Obstacle.EMPTY, Obstacle.SPINNING_BLADE, Obstacle.EMPTY, Obstacle.MONKEY_BARS},
		{Obstacle.PLATFORM, Obstacle.BALANCING_ROPE, Obstacle.PLATFORM, Obstacle.SPINNING_BLADE, Obstacle.PLATFORM, Obstacle.LOW_WALL, Obstacle.PLATFORM, Obstacle.BLADE, Obstacle.PLATFORM},
		{Obstacle.HAND_HOLDS, Obstacle.EMPTY, Obstacle.BLADE, Obstacle.EMPTY, Obstacle.PILLARS, Obstacle.EMPTY, Obstacle.STAT_DARTS, Obstacle.EMPTY, Obstacle.HAND_HOLDS},
		{Obstacle.PLATFORM, Obstacle.LOG_BALANCE, Obstacle.PLATFORM, Obstacle.STAT_DARTS, Obstacle.PLATFORM, Obstacle.MONKEY_BARS, Obstacle.PLATFORM, Obstacle.BALANCING_ROPE, Obstacle.PLATFORM},
		{Obstacle.PLANK, Obstacle.EMPTY, Obstacle.PRESSURE_PAD, Obstacle.EMPTY, Obstacle.FLOOR_SPIKES, Obstacle.EMPTY, Obstacle.PRESSURE_PAD, Obstacle.EMPTY, Obstacle.BALANCING_LEDGE},
		{Obstacle.PLATFORM, Obstacle.ROPE_SWING, Obstacle.PLATFORM, Obstacle.PILLARS, Obstacle.PLATFORM, Obstacle.LOW_WALL, Obstacle.PLATFORM, Obstacle.LOG_BALANCE, Obstacle.PLATFORM}};

	public static void setHealPercentage(int value){
		healPercentage = value;
	}

	// Counts the amount of tickets in your inventory
	public static int countTickets(){
		Item ticket = Inventory.getItem(ticketID);
		if(ticket != null){
			return ticket.getStackSize();
		}
		return 0;
	}

	// Waits until the player has stopped moving/animating
	public static void waitUntilIdle(){
		int count = 0;
		Tile lastTile = General.getPlayerTile();
		Task.sleep(500, 1000);
		
		while(count <= 5){
			Task.sleep(200, 400);
			
			// Sleeps while animating or moving
			if(Players.getLocal().getAnimation() != -1 || !General.getPlayerTile().equals(lastTile) ){
				System.out.println("Animation != -1 or moved spots");
				count = 0;
				Task.sleep(500, 1000);
			}else{
				// Checks if we are near the dispenser so not moving
				Tile dispenser = getDispenserTile();
				Tile myPos = getPlayerTile();
				if(dispenser != null){
					if(myPos.distance(dispenser) <= 5){
						System.out.println("Distance <= 5");
						count+=2;
					}
				}
			}
			
			count++;
			System.out.println("Count: " + count);
			lastTile = General.getPlayerTile();
		}
	}
	
	// Returns the location of the arrow as a tile
	public static Tile getArrowTile(){
		Set<HintArrow> arrows = Game.getHintArrows();
		for(HintArrow arrow : arrows){
			return arrow.getLocation();
		}
		return null;
	}
	
	// Returns the location of the player as a tile
	public static  Tile getPlayerTile(){
		return Players.getLocal().getLocation();
	}
	
	// Returns the location of the nearest ticket dispenser
	public static Tile getDispenserTile(){
		SceneObject dispenser = SceneEntities.getNearest(dispenserIDs);
		if(dispenser != null){
			return dispenser.getLocation();
		}
		return null;
	}
	
	// Returns a string with distance
	public static String getPath(){
		Point arrow = getArrowGridLocation();
		Point platform = getPlatformGridLocation();
		
		if(arrow != null && platform != null){
			
			int width = arrow.x-platform .x;
			int height = arrow.y-platform .y;
			
			return width + " horizontal, " + height + " vertical";
		}
		return "Unavailable";
	}
	
	// Returns the location relative to the grid
	public static Point getGridLocation(Tile tile){
		if(tile != null){
			return new Point((tile.getX()-nwDispenser.getX())/11, (nwDispenser.getY()-tile.getY())/11);
		}else{
			return null;
		}
	}
	
	// Returns the middle tile of the platform we are on
	public static Tile getPlatformTile(){
		SceneObject platform = SceneEntities.getNearest(platformIDs);
		if(platform != null){
			return platform.getLocation();
		}
		return null;
	}

	// Returns the platform tile as a grid
	public static Point getPlatformGridLocation(){
		Tile platform = getPlatformTile();
		if(platform != null){
			return getGridLocation(platform.getLocation());
		}
		return null;
	}
	
	// Returns the dispenser location relative to the grid
	public static Point getArrowGridLocation(){
		return getGridLocation(getArrowTile());
	}

	// Returns a format like [0][1]
	public static String formatToGrid(Point p){
		if(p != null){
			return "[" + p.x + "][" + p.y + "]";
		}else{
			return "[][]";
		}
	}
	
	// Converts a 5x5 grid to 9x9 grid loc
	public static Point convertLocation(Point p){
		return new Point(p.x*2, p.y*2);
	}

	// Finds the nearest object with the input id, then performs an action
	public static boolean performAction(int id, String action){
		SceneObject obj = SceneEntities.getNearest(id);
		if(obj!= null){
			Tile tile = obj.getLocation();
			if(!tile.isOnScreen()){
				walkToTile(tile);
				Camera.turnTo(tile);
			}
			
			if(obj.interact(action)){
				waitUntilIdle();
				return true;
			}else{
				Camera.turnTo(obj);
			}
		}
		return false;
	}

	// Finds the nearest of all ids, then attempts to perform an action
	public static boolean performAction(int[] ids, String action){
		SceneObject obj = SceneEntities.getNearest(ids);
		if(obj!= null){
			Tile tile = obj.getLocation();
			if(!tile.isOnScreen()){
				walkToTile(tile);
			}
			
			if(obj.interact(action)){
				waitUntilIdle();
				return true;
			}else{
				walkToTile(tile);
			}
		}
		return false;
	}
	
	// Identify the green box so we know we are getting tickets
	public static boolean haveGreenBox(){
		return Settings.get(1613) == 15 || Settings.get(1613) == 6;
		//7 = not tagged
		/*
		BufferedImage img = Environment.getScreenBuffer();
		Color redColor = new Color(27, 238, 17);
		
		WidgetChild topBox = Widgets.get(5, 0);
		Rectangle r = topBox.getBoundingRectangle();
		
		for(int x = r.x; x < r.width+r.x; x++){
			for(int y = r.y; y < r.height+r.y; y++){
				Color c = new Color(img.getRGB(x, y));
				int greenDistance = Math.abs(c.getGreen()-redColor.getGreen());
				if(greenDistance < 10){
					System.out.println("Found green color");
					return true;
				}
			}
		}
		
		return false;
		*/
	}
	
	// Walks to the tile using the minimap.
	public static boolean walkToTile(Tile tile){
		Mouse.click(tile.getMapPoint(), true);
		return true;
	}
	
	// Converts data to data per hour
	public static int toHour(int detail){
		return (int)(detail / ((double)(System.currentTimeMillis()-startTime) / 3600000));
	}
	
	// Generates a direction towards the arrow
	public static Point generateDirection(int xDif, int yDif){
		int total = Math.abs(xDif) + Math.abs(yDif);
		if(total == 0){
			return new Point(0, 0);
		}
		
		if(Random.nextInt(0, total) >= Math.abs(xDif)){
			System.out.println("Got Y");
			if(yDif < 0){
				return new Point(0, -1); // north
			}else{
				return new Point(0, 1); // south
			}
		}else{
			System.out.println("Got X");
			if(xDif < 0){
				return new Point(-1, 0); // west
			}else{
				return new Point(1, 0); //east
			}
		}
	}
	
	// Returns true if the player is resting
	public static boolean isResting(){
		return Widgets.get(750, 2).getTextureId() == 1794;
	}
	
	// Sets rest to true	
	public static boolean setRest(){
		return Widgets.get(750, 5).interact("Rest");
	}

	// Walks toward the point of choice based on the grid system
	public static void walkTowards(Point goalPos){
		System.out.println("Wander activated!!");
		
		Point myPos = General.getPlatformGridLocation();
		Point p = General.generateDirection(goalPos.x - myPos.x, goalPos.y - myPos.y);

		Point myConvertedPos = General.convertLocation(myPos);
		Point onGrid = new Point(myConvertedPos.x+p.x, myConvertedPos.y+p.y);
		
		Obstacle obstacle = General.grid[onGrid.x][onGrid.y];
		System.out.println("Obstacle: " + obstacle);

		if(obstacle != Obstacle.PLATFORM && obstacle != Obstacle.EMPTY){
			System.out.println("Wandering towards the middle");
			Traverser traverser = new Traverser(obstacle);
			traverser.traverse();
		}else{
			System.out.println("Waiting for arrow to move...");
			Task.sleep(1000, 2000);
		}
	}

}
