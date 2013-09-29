package scripts.jjswillowchopper;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSObjectDefinition;
import org.tribot.api2007.types.RSTile;

public class Navigation {

	// Willows
	private final RSArea willowArea = new RSArea(new RSTile(2959, 3190), new RSTile(2975, 3200));
	// Store
	private final RSArea storeArea = new RSArea(new RSTile(2946, 3212), new RSTile(2950, 3218));
	private final RSArea storeWalkArea = new RSArea(new RSTile(2948, 3213), new RSTile(2949, 3216));
	// Door
	private final RSTile doorTile = new RSTile(2950, 3214);
	private final RSTile doorWalkTile = new RSTile(2952, 3214);
	
	/**
	 * Checks if we are inside the willow area
	 * @return True if inside area
	 */
	public boolean insideWillowArea() {
		return willowArea.contains(Player.getPosition());
	}
	
	/**
	 * Checks if we are inside the store area
	 * @return True if inside area
	 */
	public boolean insideStoreArea() {
		return storeArea.contains(Player.getPosition());
	}
	
	/**
	 * Sleeps until the RSCharacter is idle
	 */
	private boolean waitUntilIdle(){
		while(Player.isMoving() || Player.getAnimation() != -1){
			General.sleep(50, 150);
		}
		return true;
	}
	
	/**
	 * Attempts to navigate from our current position to the goal position
	 * @param goal Goal position
	 * @return True if navigated succesfully
	 */
	private boolean navigateToTile(RSTile goal){
		RSTile[] path = Walking.generateStraightPath(goal);
		return Walking.walkPath(path) && waitUntilIdle();
	}
	
	/**
	 * Checks if we can enter the store
	 * @return True if we can enter
	 */
	public boolean canEnterStore() {
		return PathFinding.canReach(storeWalkArea.getRandomTile(), true);
	}
	
	/**
	 * Navigates to the store door
	 * @return True if navigated to door
	 */
	public boolean navigateToDoor() {
		return navigateToTile(doorWalkTile);
	}
	
	/**
	 * Checks if we can open the door
	 * @return True if can open
	 */
	public boolean canOpenDoor() {
		return Player.getPosition().distanceTo(doorTile) <= 4;
	}
	
	/**
	 * Returns an array of RSObject doors
	 * @return RSObject[] doors
	 */
	private RSObject[] getDoors() {
		return Objects.findNearest(25, new Filter<RSObject>() {
			@Override
			public boolean accept(RSObject obj) {
				if(obj != null){
					RSObjectDefinition def = obj.getDefinition();
					return def != null && "Door".equals(def.getName());
				}
				return false;
			}
		});
	}

	/**
	 * Opens the store door
	 * @return True if opened door
	 */
	public boolean openDoor() {
		RSObject[] doors = getDoors();
		for(RSObject door : doors){
			if(doorTile.distanceTo(door) <= 3 && door.isOnScreen()){
				RSModel model = door.getModel();
				if(model != null && DynamicClicking.clickRSModel(model, "Open")){
					return waitUntilIdle();
				}
			}
		}
		return false;
	}
	
	/**
	 * Navigates to the store area
	 * @return True if navigated to area
	 */
	public boolean navigateToStore() {
		return navigateToTile(storeWalkArea.getRandomTile());
	}
	
	/**
	 * Navigates to the willow area
	 * @return True if navigated to area
	 */
	public boolean navigateToWillows() {
		return navigateToTile(willowArea.getRandomTile());
	}

	
}
