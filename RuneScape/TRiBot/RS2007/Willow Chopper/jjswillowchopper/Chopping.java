package scripts.jjswillowchopper;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSObjectDefinition;

public class Chopping {

	/**
	 * Finds willow tree's by name and sorts them by distance
	 * @return RSObject[] containing willow tree's
	 */
	private RSObject[] getTrees() {
		return Objects.findNearest(25, new Filter<RSObject>() {
			@Override
			public boolean accept(RSObject obj) {
				if(obj != null){
					RSObjectDefinition def = obj.getDefinition();
					return def != null && "Willow".equals(def.getName());
				}
				return false;
			}
		});
	}
	
	/**
	 * Checks if there is atleast one tree available to be chopped
	 * @return True if can chop
	 */
	public boolean canChopTree(){
		RSObject[] trees = getTrees();
		for(RSObject tree : trees){
			if(tree.isOnScreen() && Player.getPosition().distanceTo(tree) <= 10){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if we are chopping
	 * @return True if chopping
	 */
	public boolean areChopping(){
		return Player.getAnimation() != -1;
	}
	
	/**
	 * Waits a certain amount of ms until we are chopping
	 * @param ms Maximum time to wait
	 * @return True if chopping in time
	 */
	private boolean waitChopping(int ms){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < ms){
			if(areChopping()){
				return true;
			}else if(Player.isMoving()){
				t = System.currentTimeMillis();
			}
			General.sleep(20, 60);
		}
		return false;
	}
	
	/**
	 * Attemps to click on a tree
	 * @return True if clicked succesfully
	 */
	public boolean chopTree(){
		RSObject[] trees = getTrees();
		for(RSObject tree : trees){
			if(tree.isOnScreen() && PathFinding.canReach(tree, true)){
				// Turn camera to tree sometimes
				if(General.random(1, 5) == 3){
					Camera.turnToTile(tree);
				}
				// Click tree
				RSModel model = tree.getModel();
				if(model != null && DynamicClicking.clickRSModel(model, "Chop down")){
					return waitChopping(General.random(2000, 3000));
				}
			}
		}
		return false;
	}
	
	
}
