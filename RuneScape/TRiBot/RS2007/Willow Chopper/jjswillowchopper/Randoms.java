package scripts.jjswillowchopper;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSNPC;

public class Randoms {

	private int[] nest_ids = {5073, 5074};

	/**
	 * Finds evil tree's by name and sorts them by distance
	 * @return RSNPC[] containing evil tree's
	 */
	private RSNPC[] getEvilTrees(){
		return NPCs.findNearest(NPCs.generateFilterGroup(new Filter<RSNPC>() {
			@Override
			public boolean accept(RSNPC npc) {
				return npc != null && "Willow".equals(npc.getName());
			}
		}));
	}
	
	/**
	 * Checks if there is an evil tree
	 * @return True if evil tree exists
	 */
	public boolean haveEvilTree() {
		return getEvilTrees().length > 0;
	}
	
	/**
	 * Finds tree spirits by name and sorts them by distance
	 * @return RSNPC[] containing tree spirits
	 */
	private RSNPC[] getTreeSpirits(){
		return NPCs.findNearest(NPCs.generateFilterGroup(new Filter<RSNPC>() {
			@Override
			public boolean accept(RSNPC npc) {
				return npc != null && "Tree spirit".equals(npc.getName());
			}
		}));
	}
	
	/**
	 * Checks if we have a tree spirit
	 * @return True if tree spirit attacks us
	 */
	public boolean haveTreeSpirit(){
		RSNPC[] spirits = getTreeSpirits();
		for(RSNPC spirit : spirits){
			if(spirit.isInteractingWithMe()){
				return true;
			}
		}
		return false;
	}
	
	
	
}
