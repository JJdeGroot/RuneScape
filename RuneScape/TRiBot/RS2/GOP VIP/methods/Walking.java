package scripts.jjsgopvip.methods;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.tribot.api.Constants;
import org.tribot.api.Minimap;
import org.tribot.api.Screen;
import org.tribot.api.TPS;
import org.tribot.api.Textures;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.Texture;
import org.tribot.api.types.colour.Tolerance;

import scripts.jjsgopvip.enums.Location;
import scripts.jjsgopvip.enums.Wizard;
import scripts.jjsgopvip.utils.General;

public class Walking {
	// All locations where we could be
	private final static Location[] locations = {Location.AIR_NORMAL, Location.AIR_GOP, 
					Location.MIND_NORMAL, Location.MIND_GOP, Location.WATER_NORMAL, 
					Location.WATER_GOP, Location.EARTH_NORMAL, Location.EARTH_GOP,
					Location.FIRE_NORMAL, Location.FIRE_GOP, Location.BODY_NORMAL, 
					Location.BODY_GOP, Location.CHAOS_GOP, Location.CHAOS_NORMAL,
					Location.NATURE_GOP, Location.NATURE_NORMAL, Location.LOBBY};
	// Black count for all area's
	final private static int airNormalBlack = 192220,
						  	 airGopBlack = 213912,
							 mindNormalBlack = 189738,
							 mindGopBlack = 245010,
							 waterNormalBlack = 159744,
							 waterGopBlack = 196608,
							 earthNormalBlack = 180990,
							 earthGopBlack = 237265,
							 fireNormalBlack = 174516,
							 fireGopBlack = 199468,
							 bodyNormalBlack = 223873,
							 bodyGopBlack = 250600,
							 chaosNormalBlack = 258040,
							 chaosGopBlack = 258175,
							 natureNormalBlack = 199412,
							 natureGopBlack = 206534,
							 gopBlack = 252371;
	// Colors
	final private static Color gopBlackCol = new Color(10, 10, 10),
							   gopLavaCol = new Color(195, 156, 17),
							   gopWaterCol = new Color(95, 106, 122);
	final private static Tolerance gopTol = new Tolerance(10, 10, 10);
	// All black counts together
	final private static int[] blackCounts = {airNormalBlack, airGopBlack, 
						   mindNormalBlack, mindGopBlack, waterNormalBlack, 
						   waterGopBlack, earthNormalBlack, earthGopBlack, 
						   fireNormalBlack, fireGopBlack, bodyNormalBlack, 
						   bodyGopBlack, chaosNormalBlack, chaosGopBlack,
						   natureNormalBlack, natureGopBlack, gopBlack};
	// Area's around the altar based on TPS relative position
	final private static Rectangle airAltarNormal = new Rectangle(-6, -36, 16, 16),
							airAltarGop = new Rectangle(-37, -2, 16, 16),
							mindAltarNormal = new Rectangle(-15, -66, 16, 16),
							mindAltarGop = new Rectangle(-13, -29, 16, 16),
							waterAltarNormal = new Rectangle(-40, -10, 16, 16),
							waterAltarGop = new Rectangle(-36, -9, 16, 16),
							earthAltarNormal = new Rectangle(-14, -65, 16, 16),
							earthAltarGop = new Rectangle(-13, -29, 16, 16),
							fireAltarNormal = new Rectangle(15, -18, 16, 16),
							fireAltarGop = new Rectangle(-50, -18, 16, 16),
							bodyAltarNormal = new Rectangle(-9, -25, 16, 16),
							bodyAltarGop = new Rectangle(-42, -26, 16, 16),
							chaosAltarNormal = new Rectangle(-27, -2, 18, 18),
							chaosAltarGop = new Rectangle(-27, -36, 18, 18),
							natureAltarNormal = new Rectangle(-22, -30, 16, 16),
							natureAltarGop = new Rectangle(-23, -31, 18, 18);
	// All areas around the altar together
	final private static Rectangle[] altarAreas = {airAltarNormal, airAltarGop, mindAltarNormal,
										    mindAltarGop, waterAltarNormal, waterAltarGop,
										    earthAltarNormal, earthAltarGop, fireAltarNormal,
										    fireAltarGop, bodyAltarNormal, bodyAltarGop,
										    chaosAltarNormal, chaosAltarGop,
										    natureAltarNormal, natureAltarGop};
	// Area's around the portal based on TPS relative position
	final private static Rectangle airPortalNormal = new Rectangle(-2, -15, 8, 8),
							airPortalGop = new Rectangle(-33, 26, 8, 8),
							mindPortalNormal = new Rectangle(-10, -19, 8, 8),
							mindPortalGop = new Rectangle(-9, 14, 8, 8),
							waterPortalNormal = new Rectangle(-18, 7, 8, 8),
							waterPortalGop = new Rectangle(-13, 10, 8, 8),
							earthPortalNormal = new Rectangle(-14, -10, 8, 8),
							earthPortalGop = new Rectangle(-19, 24, 8, 8),
							firePortalNormal = new Rectangle(-2, -14, 8, 8),
							firePortalGop = new Rectangle(-67, -14, 8, 8),
							bodyPortalNormal = new Rectangle(-9, 6, 8, 8),
							bodyPortalGop = new Rectangle(-42, 10, 8, 8),
							chaosPortalNormal = new Rectangle(15, 24, 6, 6),
							chaosPortalGop = new Rectangle(-16, 4, 6, 6),
							naturePortalNormal = new Rectangle(-10, 6, 6, 6),
							naturePortalGop = new Rectangle(-12, 0, 6, 6);
	// All area's around the portals together
	final private static Rectangle[] portalAreas = {airPortalNormal, airPortalGop, mindPortalNormal,
											 mindPortalGop, waterPortalNormal, waterPortalGop,
											 earthPortalNormal, earthPortalGop, firePortalNormal, 
											 firePortalGop, bodyPortalNormal, bodyPortalGop,
											 chaosPortalNormal, chaosPortalGop,
											 naturePortalNormal, naturePortalGop};
	// Minimap coords
	final private static int MMX1 = Constants.MMCX-74, MMX2 = Constants.MMCX+74,
							 MMY1 = Constants.MMCY-74, MMY2 = Constants.MMCY+74;
	final private static Point mmc = new Point(Constants.MMCX, Constants.MMCY);

	// Calculates the amount of black pixels on the cached minimap
	private static int countPixels(Color c1){
		int[][] data = Minimap.getRGBData();
		int count = 0;
		
		for(int x = 0; x < data.length; x++){
			for(int y = 0; y < data[x].length; y++){
				Color c2 = new Color(data[x][y]);
				if(Screen.coloursMatch(c1, c2, gopTol)){
					count++;
				}
			}
		}
		
		//System.out.println("Counted pixels with " + c1 + " color: " + count);
		return count;
	}
	
	
	// Returns our location defined as LOCATIONS enum
	public static Location getLocation(){ 
		
		int blackColors = countPixels(gopBlackCol);
		System.out.println("Counted " + blackColors + " black colors!");
		
		int lavaColors = countPixels(gopLavaCol);
		System.out.println("Counted " + lavaColors + " lava colors!");
		
		if(lavaColors > 25000){
			double gopDifference = Math.abs(blackColors-fireGopBlack),
				   normalDifference = Math.abs(blackColors-fireNormalBlack);
			if(gopDifference < normalDifference){
				return Location.FIRE_GOP;
			}else{
				return Location.FIRE_NORMAL;
			}
		}else{
			int waterColors = countPixels(gopWaterCol);
			System.out.println("Counted " + waterColors + " water colors!");
			
			if(waterColors > 25000){
				double gopDifference = Math.abs(blackColors-waterGopBlack),
					   normalDifference = Math.abs(blackColors-waterNormalBlack);
				if(gopDifference < normalDifference){
					return Location.WATER_GOP;
				}else{
					return Location.WATER_NORMAL;
				}
			}else{
				int smallest = Math.abs(blackColors-blackCounts[0]),
					position = 0;
				int[] ignore = {4, 5, 8, 9};

				for(int i = 1; i < blackCounts.length; i++){
					// Skipping fire and water altar!
					for(int j : ignore)
						if(i == j)
							continue;
					
					int difference = Math.abs(blackColors- blackCounts[i]);
					if(difference < smallest){
						smallest = difference;
						position = i;
					}
				}
				
				return locations[position];
			}
		}
	}
	
	// Gets the lobby middle
	public static Point getLobbyMiddle(){
		int[][] data = Minimap.getRGBData();
		Color gray = new Color(86, 83, 78);
		Tolerance tol = new Tolerance(20, 20, 20);
		
		for(int x = 0; x < data.length; x++){
			for(int y = 0; y < data[x].length; y++){
				Color c = new Color(data[x][y]);
				if(Screen.coloursMatch(c, gray, tol)){
					//System.out.println("C: (" + x + ", " + y + ")");
					return new Point(x-256+39, y-256+14); // Numbers: http://puu.sh/1USxK
				}
			}
		}
		
		return new Point(256, 256);
	}
	
	// Generates the portal location
	private static Point getLobbyPortal(){
		Point m = getLobbyMiddle();
		Point p = new Point(m.x + General.randomRange(-5, 5),
							m.y - General.randomRange(30, 35));
		return p;
	}
	
	// Returns the radials
	private static double getRads(){
    	return Math.toRadians(360 - Minimap.getRotationAngle());
    }

	// Rotates a point around a certain angle
	private static Point rotateAround(int x, int y, Point center, double rads){ 
		return new Point((int) Math.round(center.x + (Math.cos(rads) * (x - center.x) - Math.sin(rads) * (y - center.y))), 
				         (int) Math.round(center.y + (Math.sin(rads) * (x - center.x) + Math.cos(rads) * (y - center.y))));
	}

	// Converts a TPS point to a point on the minimap
	private static Point posToMM(Point P){
		Point myPos = TPS.getRelativePosition();
		Point p = new Point(Constants.MMCX+P.x-myPos.x, Constants.MMCY+P.y-myPos.y);
		return rotateAround(p.x, p.y, mmc, getRads());
	}

	// Walks to a point relative to the middle of the current cached minimap
	private static boolean blindwalk(Point p){
		boolean reached = false;
		Point onMM = posToMM(p);
		long t = System.currentTimeMillis();

		while(!reached){
			// Checking if our goal is on the minimap
			if(Minimap.isOnMinimap(onMM)){
				Point tps = TPS.getRelativePosition();
				double difference = General.getDifference(p, tps);
				if(difference <= 5){
					return true;
				}else{
					Mouse.move(onMM);
					Mouse.click(1);
					General.waitUntilNotMoving();
					return true;
				}
			}
			
			// Checking if blindwalk has taken too long
			if(Timing.timeFromMark(t) > 15000){
				System.out.println("Blindwalk took longer than 15 secs, ending");
				return false;
			}

			// Generating the best Point on the minimap possible
			double rads = getRads();
			double smallest = 100000;
			Point bestOnMM = new Point(Constants.MMCX, Constants.MMCY);
			for(int x = MMX1; x <= MMX2; x++){
				for(int y = MMY1; y <= MMY2; y++){
					Point q = rotateAround(x, y, mmc, rads);
					if(Minimap.isOnMinimap(q)){
						double difference = General.getDifference(q, onMM);
						if(difference < smallest){
							smallest = difference;
							bestOnMM = new Point(x, y);
						}
					}
				}
			}

			Mouse.move(bestOnMM);
			Mouse.click(1);
			General.waitUntilNotMoving();
			
			onMM = posToMM(p);
		}
		
		Point goal = TPS.posToMM(p);
		Mouse.move(goal);
		Mouse.click(1);
		General.waitUntilNotMoving();
		return true;
	}
	
	// Walks to the altar of the area we are at
	public static boolean walkToAltar(){
		Location myLocation = getLocation();

		if(!myLocation.equals(Location.LOBBY)){
			for(int i = 0; i < locations.length; i++){
				if(myLocation.equals(locations[i])){
					Point p = General.randomPointInRectangle(altarAreas[i]);
					return blindwalk(p);
				}
			}
		}
		
		return false;
	}

	// Walks to the portal of the area we are at
	public static boolean walkToPortal(){
		Location myLocation = getLocation();
		
		if(myLocation.equals(Location.LOBBY)){
			return blindwalk(getLobbyPortal());
		}else{
			for(int i = 0; i < locations.length; i++){
				if(myLocation.equals(locations[i])){
					//System.out.println("My loc: " + myLocation);
					Point p = General.randomPointInRectangle(portalAreas[i]);
					return blindwalk(p);
				}
			}
		}
		
		return false;
	}
	
	// Walks near the wizard of choice
	public static boolean walkToWizard(Wizard wizard){
		Location myLoc = getLocation();
		if(myLoc.equals(Location.LOBBY)){
			System.out.println("Walking to: " + wizard);
			Point m = getLobbyMiddle();
			Point p;
			switch(wizard){
				case WIZARD: 	p = new Point(m.x + General.randomRange(-35, 35),
											  m.y + General.randomRange(-15, 15));
							 	break;
				case ACANTHA: 	p = new Point(m.x - General.randomRange(15, 25), 
											  m.y - General.randomRange(15, 5));
							  	break;
				case VIEF: 		p = new Point(m.x + General.randomRange(15, 25), 
											  m.y + General.randomRange(15, 25));
						       	break;
				default: return false;
			}
			
			return blindwalk(p);
		}
		
		return false;
	}
	
	// Walks to a random spot in the lobby
	public static boolean walkToRandomSpotInLobby(){
		Location myLocation = getLocation();

		if(myLocation.equals(Location.LOBBY)){
			Point m = getLobbyMiddle();
			Point p = new Point(m.x + General.randomRange(-35, 35),
								m.y + General.randomRange(-35, 35));
			return blindwalk(p);
		}
		
		return false;
	}

	// Runs to a random point in the area
	public static boolean runToRandomPoint(){
		Point myPos = TPS.getRelativePosition(),
			  p = new Point(myPos.x + General.randomRange(-20, 20), 
					  	    myPos.y + General.randomRange(-20, 20));
		return blindwalk(p);
	}
	
	// Attempts to walk to orbs based on yellow dots
	public static boolean walkToOrbs(){
		int id = General.getNPCTexture();
		Location myLoc = getLocation();
		
		if(!myLoc.equals(Location.LOBBY)){
			if(id != -1){
				Texture[] npcs = Textures.find(id);
				
				if(npcs.length > 0){
					// Filtering out points with a distance > 20
					ArrayList<Point> points = new ArrayList<Point>();
					Point mmc = new Point(Constants.MMCX, Constants.MMCY);
					for(Texture t : npcs){
						Point p = new Point(t.x + t.width/2 + General.randomRange(-t.width, t.width), 
										    t.y + t.height / 2 + General.randomRange(-t.height, t.height));
						double distance = General.getDifference(p, mmc);
						if(distance > 30){
							if(Minimap.isOnMinimap(p)){
								points.add(p);
							}
						}
					}
					
					// Getting a random point out the list
					if(!points.isEmpty()){
						int r = General.randomRange(0, points.size()-1);
						Mouse.click(points.get(r), 1);
						General.waitUntilNotMoving();
						return true;
					}
				}
			}
			
			walkToAltar();	
		}
		
		return false;
	}

	// Walks a few steps towards the altar
	public static boolean walkTowardsAltar(){
		Location myLoc = getLocation();
		
		if(!myLoc.equals(Location.LOBBY)){
			for(int i = 0; i < locations.length; i++){
				if(myLoc.equals(locations[i])){
					Point p = General.randomPointInRectangle(altarAreas[i]);
					Point myPos = TPS.getRelativePosition();
	
					double difference = General.getDifference(p, myPos);
					if(difference > 10){
						int r = General.randomRange(7, 12);
						int amount = (int) Math.round(difference/r);
						Point q = new Point(myPos.x + (p.x-myPos.x)/amount, myPos.y + (p.y-myPos.y)/amount );
						return blindwalk(q);
					}else{
						return blindwalk(p);
					}
				}
			}
		}
		
		return false;
	}

	// Returns true if we are at the altar
	public static boolean atAltar(){
		Location myLoc = getLocation();

		if(!myLoc.equals(Location.LOBBY)){
			for(int i = 0; i < locations.length; i++){
				if(myLoc.equals(locations[i])){
					return General.nearPointInRect(TPS.getRelativePosition(), altarAreas[i]);
				}
			}
		}

		return false;
	}
	
	// Returns true if we are at the altar
	public static boolean atPortal(){
		Location myLoc = getLocation();

		if(myLoc.equals(Location.LOBBY)){
			Point p = getLobbyPortal();
			return General.getDifference(TPS.getRelativePosition(), p) <= 10;
		}else{
			for(int i = 0; i < locations.length; i++){
				if(myLoc.equals(locations[i])){
					return General.nearPointInRect(TPS.getRelativePosition(), portalAreas[i]);
				}
			}
		}
		
		return false;
	}
}