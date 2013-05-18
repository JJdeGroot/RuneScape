package scripts.jjsgopvip.utils;

import org.tribot.api.Login;

import scripts.jjsgopvip.enums.Location;
import scripts.jjsgopvip.enums.State;
import scripts.jjsgopvip.methods.Attract;
import scripts.jjsgopvip.methods.Portal;
import scripts.jjsgopvip.methods.RuneCraft;
import scripts.jjsgopvip.methods.Walking;

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
		// Checking if we need to restart
		if(Login.getLoginState() != Login.LOGINSTATE.IN_GAME){
			return State.LOGIN;
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