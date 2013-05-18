package scripts.jjsgopvip.methods;

import java.awt.Point;
import java.util.ArrayList;

import org.tribot.api.DynamicClicking;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.generic.TranslatableString;

import scripts.jjsgopvip.enums.Team;
import scripts.jjsgopvip.utils.General;
import scripts.jjsgopvip.utils.Task;

public class Attract {
	// ScreenModel ids
	private final static long greenOrbID = 3832666545L,
						 	  yellowOrbID = 455074826L;
	// English, German, French, Portuguese
	private final static TranslatableString greenAttractString = new TranslatableString("Attract Green orb", "Anziehen: Grüne Kugel", "Attirer Orbe vert", "Atrair Orbe verde"),
						 					yellowAttractString = new TranslatableString("Attract Yellow orb", "Anziehen: Gelbe Kugel", "Attirer Orbe jaune", "Atrair Orbe amarela");

	// Gets the orb ID from our team
	private static long getOrbID(){
		Team myTeam = General.getTeam();
		switch(myTeam){
			case GREEN: return greenOrbID; 
			case YELLOW: return yellowOrbID;
			default: return -1;
		}
	}
	
	// Gets the orb text from our team
	private static String getOrbText(){
		Team myTeam = General.getTeam();
		switch(myTeam){
			case GREEN: return greenAttractString.getTranslation();
			case YELLOW: return yellowAttractString.getTranslation();
			default: return "Walk";
		}
	}

	// Attract the orb of our team
	public static boolean attractOrb(){
		int attempts = 0;
		while(attempts < 5){
			if(DynamicClicking.clickScreenModel(General.getScreenModel(getOrbID()), getOrbText())){
				General.waitUntilNotMoving();
				Task.sleep(1000, 2000);
				return true;
			}
			attempts++;
		}
		return false;
	}
	
	// Returns true if there are any visible orbs
	public static boolean hasVisibleOrbs(){
		ScreenModel[] orbs = ScreenModels.findNearest(getOrbID());
		if(orbs.length > 0){
			for(ScreenModel orb : orbs){
				if(Screen.isInViewport(orb.base_point)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	// Generates a random point on one of the orbs
	private static Point getOrbPoint(){
		ScreenModel[] orbs = ScreenModels.findNearest(getOrbID());
		ArrayList<Point> orbPoints = new ArrayList<Point>();
		
		// Generating good points
		if(orbs.length > 0){
			Point m = new Point(orbs[0].points[0]);
			for(int j = 0; j < orbs[0].points_x.length; j++){
				Point p = new Point(orbs[0].points_x[j], orbs[0].points_y[j]);
				double difference = General.getDifference(m, p);
				if(difference < 10){
					if(Screen.isInViewport(p)){
						orbPoints.add(p);
					}
				}
			}
		}
		
		// Picking a random point
		if(!orbPoints.isEmpty()){
			int r = General.randomRange(0, orbPoints.size()-1);
			return orbPoints.get(r);
		}
		
		return null;
	}
	
	// Tracks the movement of the nearest orb
	private static boolean isOrbMoving(){
		long t = System.currentTimeMillis();
		Point start = getOrbPoint();
		if(start != null){
			while(Timing.timeFromMark(t) < 2000){
				Task.sleep(100, 200);
				Point end = getOrbPoint();
				if(end != null){
					double difference = General.getDifference(start, end);
					if(difference > 25){
						return true;
					}
				}else{
					break;
				}
			}
		}
		
		return false;
	}

	// Attract the orb of our team
	public static boolean attractOrb2(){
		Long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < 3000){
			if(hasVisibleOrbs()){
				if(!Timing.waitUptext(getOrbText(), 100)){
					Point p = getOrbPoint();
					Mouse.move(p);
				}else{
					Mouse.click(1);
					General.waitUntilNotMoving();
					long tt = System.currentTimeMillis();
					while(isOrbMoving() && Timing.timeFromMark(tt) < 5000){
						System.out.println("Orb has moved");
						Task.sleep(250, 750);
					}
					return true;
				}
			}
		}
		
		return false;
	}

	

}
