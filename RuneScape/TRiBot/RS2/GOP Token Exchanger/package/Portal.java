package scripts.jjsgoptokenexchanger;

import java.awt.Point;

import org.tribot.api.DynamicClicking;

public class Portal {
	
	private static final long portalToBank = 1220408498L,//2845049235L,
							  portalToGOP = 3689793898L;
	private static final Point gopExitLoc = new Point(1697, 5463),
							   gopEntranceLoc = new Point(3103, 3151);
	
	public static boolean enterPortalToBank(){
		if(DynamicClicking.clickScreenModel(Generic.getScreenModel(portalToBank), "Enter")){
			Generic.waitUntilNotMoving();
			return true;
		}
		return false;
	}
	
	public static boolean walkToGopEntrancePortal(){
		return Generic.walkTo(gopEntranceLoc);
	}
	
	public static boolean enterPortalToGOP(){
		if(DynamicClicking.clickScreenModel(Generic.getScreenModel(portalToGOP), "Enter")){
			Generic.waitUntilNotMoving();
			return true;
		}
		return false;
	}
	
	public static boolean walkToGopExitPortal(){
		return Generic.walkTo(gopExitLoc);
	}

	
}
