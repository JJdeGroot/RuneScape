package org.obduro.agilityarena.utils;

import java.util.ArrayList;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class Traverser {
	private final int[] plankIDs = {3570, 3571, 3572, 3573, 3574, 3575};
	private final int faultyPlankID = 3576;
	private final int[] spinningBladeTileIDs = {35872, 35873};
	private final int ropeSwingID = 3566;
	private final int pillarID = 3578;
	private final int pressurePadID = 3585;
	private final int ropeID = 3551;
	private final int wallID = 3565;
	private final int handholdID = 3583;
	private final int[] logIDs = {3553, 3557};
	private final int floorEventID = 3550;
	private final int aroundFloorEventID = 3598;
	private final int monkeyBarID = 3564;
	private final int[] ledgeIDs = {3559, 3561};
	private final int spikesID = 3582;

	private Obstacle obstacle;

	public Traverser(Obstacle obstacle){
		System.out.println("Made a new traverser for " + obstacle);
		this.obstacle = obstacle;
	}

	public boolean traverse(){
		boolean succesful = false;
		int attempts = 0;
		while(!succesful && attempts < 3){
			System.out.println("Succesful: " + succesful + " attempts: " + attempts);
			
			switch(obstacle){
				case BALANCING_LEDGE: succesful = acrossLedge(); break;
				case BALANCING_ROPE: succesful = walkOnRope(); break;
				case BLADE: succesful = evadeBlade(); break;
				case FLOOR_SPIKES: succesful = passSpikes(); break;
				case HAND_HOLDS: succesful = grabHandholds(); break;
				case LOG_BALANCE: succesful = passLog(); break;
				case LOW_WALL: succesful = climbWall(); break;
				case MONKEY_BARS: succesful = climbMonkeyBars(); break;
				case PILLARS: succesful = jumpPillars(); break;
				case PLANK: succesful = walkPlank(); break;
				case PRESSURE_PAD: succesful = passPressurePad(); break;
				case ROPE_SWING: succesful = swingRope(); break;
				case SPINNING_BLADE: succesful = jumpOverBlade(); break;
				case STAT_DARTS: succesful = evadeDarts(); break;
				default: break;
			}
			
			attempts++;
		}

		return succesful;
	}

	public boolean walkPlank(){
		// Finding the faulty planks
		SceneObject[] faultyPlanks = SceneEntities.getLoaded(faultyPlankID);
		ArrayList<Integer> rows = new ArrayList<Integer>();
		for(SceneObject plank : faultyPlanks){
			int y = plank.getLocation().getY();
			if(!rows.contains(y)){
				rows.add(y);
			}
		}

		// Finding the plank that isn't faulty
		SceneObject[] allPlanks = SceneEntities.getLoaded(plankIDs);
		Tile myPos = Players.getLocal().getLocation();
		SceneObject walkablePlank = null;
		double nearest = 1000;
		for(SceneObject plank : allPlanks){
			if(plank.isOnScreen()){
				if(!rows.contains(plank.getLocation().getY())){
					double distance = myPos.distance(plank);
					if(distance < nearest){
						nearest = distance;
						walkablePlank = plank;
						System.out.println(plank);
					}
				}
			}
		}
		
		// Checking if we found the right plank
		if(walkablePlank != null){
			System.out.println("Found plank");
			
			Tile tile = walkablePlank.getLocation();
			if(!tile.isOnScreen()){
				General.walkToTile(tile);
				Camera.turnTo(tile);
			}
			
			if(walkablePlank.interact("Walk-on")){
				General.waitUntilIdle();
				return true;
			}
		}else{
			SceneObject nearestPlank = SceneEntities.getNearest(plankIDs);
			if(nearestPlank != null){
				System.out.println("Walking to the plank");
				General.walkToTile(nearestPlank.getLocation());
			}
		}

		return false;
	}

	// Attempts to pass the pressure pad
	public boolean passPressurePad(){
		SceneObject pad = SceneEntities.getNearest(pressurePadID);
		if(pad != null){
			Tile tile = pad.getLocation();
			if(!tile.isOnScreen()){
				General.walkToTile(tile);
				Camera.turnTo(tile);
			}
			
			if(pad.getLocation().click(true)){
				General.waitUntilIdle();
				return true;
			}
		}
		
		return false;
	}

	// Attempts to evade the blade -> hard to time
	public boolean evadeBlade(){
		SceneObject[] nearby = SceneEntities.getLoaded(spinningBladeTileIDs);
		SceneObject nearest = SceneEntities.getNearest(spinningBladeTileIDs);
		for(int i = 0; i < nearby.length; i++){
			// Looking for the other side
			if(!nearby[i].getLocation().equals(nearest.getLocation())){
				double distance = nearby[i].getLocation().distance(nearest.getLocation());
				if(distance <= 5){
					// Filtered out the right one, need to spam click.
					if(!nearby[i].isOnScreen()){
						nearby[i].getLocation().clickOnMap();
						General.waitUntilIdle();
					}
					
					// Max 5 secs to spam click
					if(nearby[i].isOnScreen()){
						long start = System.currentTimeMillis();
						while(!Players.getLocal().isMoving() && System.currentTimeMillis() - start < 5000){
							nearby[i].click(true);
							Task.sleep(100, 200);
						}
					}
					
					General.waitUntilIdle();
					return true;
				}
			}
		}
		
		return false;
	}

	// Attempts to pass the floor spikes
	public boolean passSpikes(){
		return General.performAction(spikesID, "Walk");
	}
	
	// Attempts to use the rope swing
	public boolean swingRope(){
		return General.performAction(ropeSwingID, "Swing-on");
	}
	
	// Attempts to jump on the pillars
	public boolean jumpPillars(){
		return General.performAction(pillarID, "Jump-on");
	}
	
	// Walks across the rope
	public boolean walkOnRope(){
		return General.performAction(ropeID, "Walk-on");
	}
	
	// Climbs the crumbled wall
	public boolean climbWall(){
		return General.performAction(wallID, "Climb-over");
	}
	
	// Grabs the hand holds and passes the gap
	public boolean grabHandholds(){
		return General.performAction(handholdID, "Climb-across");
	}
	
	// Passes the log
	public boolean passLog(){
		return General.performAction(logIDs, "Walk-on");
	}
	
	// Climbs the monkey bars
	public boolean climbMonkeyBars(){
		return General.performAction(monkeyBarID, "Swing-across");
	}
	
	// Attempts to pass the ledge
	public boolean acrossLedge(){
		return General.performAction(ledgeIDs, "Walk-across");
	}

	// Clicks on an event spot (3550) with a number of surrounded 3598's
	public boolean clickEvent(int needed){
		// Getting the location of all events
		SceneObject[] events = SceneEntities.getLoaded(floorEventID);
		Tile myPos = General.getPlayerTile();

		// Looping through all possible spots that are nearby
		ArrayList<SceneObject> goodSpotList = new ArrayList<SceneObject>();
		for(SceneObject possibleSpot : events){
			double distance = possibleSpot.getLocation().distance(myPos);
			if(distance <= 10){
				Tile tile = possibleSpot.getLocation();
				int count = 0;
				
				// Checking for objects loaded around the possibleSpots
				for(int x = -2; x <= 2; x++){
					for(int y = -2; y <= 2; y++){
						Tile testTile = new Tile(tile.getX()+x, tile.getY()+y, tile.getPlane());
						SceneObject[] loadedOnTile = SceneEntities.getLoaded(testTile);
						for(SceneObject obj : loadedOnTile){
							if(obj.getId() == aroundFloorEventID){
								count++;
							}
						}
					}
				}
				
				// Clicking on the tile if it matches with our needed matches
				System.out.println("Count for " + tile + ": " + count);
				if(count == needed){
					goodSpotList.add(possibleSpot);
				}
			}
		}
		
		// Filtering out the nearest to the arrow in our list
		if(!goodSpotList.isEmpty()){
			Tile arrowPos = General.getArrowTile();
			double nearest = 10000;
			int index = 0;
			
			for(int i = 0; i < goodSpotList.size(); i++){
				Tile loc = goodSpotList.get(i).getLocation();
				double distance = loc.distance(arrowPos);
				System.out.println("Distance of GOOD SPOT: " + distance);
				if(distance < nearest){
					nearest = distance;
					index = i;
				}
			}
			
			// Finally interacting with the best tile.
			Tile tile = goodSpotList.get(index).getLocation();
			if(!tile.isOnScreen()){
				tile.clickOnMap();
				Camera.turnTo(tile);
				General.waitUntilIdle();
			}
			
			// Finally clicks there!
			if(tile.click(true)){
				General.waitUntilIdle();
				return true;
			}
		}
	
		return false;
	}
	
	// Attempts to evade the darts
	public boolean evadeDarts(){
		return clickEvent(8);
	}
	
	// Attempts to jump over the spinning blade
	public boolean jumpOverBlade(){
		return clickEvent(5);
	}
}