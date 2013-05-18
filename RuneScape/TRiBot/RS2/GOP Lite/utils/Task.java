package scripts.jjsgoplite.utils;

import org.tribot.api.Game;
import org.tribot.api.ScreenModels;

import scripts.jjsgoplite.enums.Location;
import scripts.jjsgoplite.enums.State;
import scripts.jjsgoplite.methods.Attract;
import scripts.jjsgoplite.methods.Portal;
import scripts.jjsgoplite.methods.RuneCraft;
import scripts.jjsgoplite.methods.Walking;

public class Task {
	
	private static Location lastLocation = Location.LOBBY;
	
	public static Location getLocation(){
		return lastLocation;
	}

	// Sleeps for a random time between min & max
	public static void sleep(int min, int max){
		try { 
			Thread.sleep(min, max);
		} catch (final InterruptedException ignored) {}
	}
	
	// Gets a new task based on what's happening in game
	public static State getState(){
		if(!Game.isLoading() && ScreenModels.getAll().length < 1){
			return State.LOGGED_OUT;
		}else{
			Location myLoc = Walking.getLocation();
			lastLocation = myLoc;
			
			if(myLoc.equals(Location.LOBBY)){
				if(!General.inTeam()){
					return State.JOIN_TEAM;
				}else if(!Portal.isOpen()){
					return State.WAIT_FOR_PORTAL;
				}else{
					return State.ENTER_PORTAL;
				}
			}else if(RuneCraft.needToCraft()){
				return State.CRAFT_RUNES;
			}else if(Portal.isOpen()){
					return State.ENTER_PORTAL;
			}else if(Attract.hasVisibleOrbs()){
				return State.ATTRACT_ORBS;
			}else{
				return State.WALK_TO_ORBS;
			}
		}
	}
}