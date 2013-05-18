package scripts.jjsgoplite.utils;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import org.tribot.api.Camera;
import org.tribot.api.Constants;
import org.tribot.api.Game;
import org.tribot.api.GameTab;
import org.tribot.api.Inventory;
import org.tribot.api.Minimap;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.TPS;
import org.tribot.api.Textures;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.Texture;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.api.types.generic.CustomRet_0P;

import scripts.jjsgoplite.enums.Team;

public class General {
	private final static int greenGeneratorID = 1118054,
			                 yellowGeneratorID = 1490917;
	private final static Color npcColor = new Color(235, 235, 10);
	private final static Point mmc = new Point(Constants.MMCX, Constants.MMCY);
	
	
	// Returns a random number
	public static int randomRange(int aFrom, int aTo){
		return aTo - Constants.RANDOM.nextInt(Math.abs(aTo - aFrom) + 1);
    }

	// Calculates the real difference between two points using the Pythagoras
	public static double getDifference(Point P1, Point P2){
		return Math.sqrt(Math.pow(Math.abs(P1.x-P2.x), 2) + Math.pow(Math.abs(P1.y-P2.y), 2));
	}
	
	// Generates a random point in a rectangle
	public static Point randomPointInRectangle(Rectangle rect){
		return new Point(randomRange(rect.x, rect.x+rect.width), randomRange(rect.y, rect.y+rect.height));
	}
	
	// Checks if a certain point is in a rectangle
	public static boolean pointInRect(Point p, Rectangle rect){
		return p.x >= rect.x && p.x <= rect.x+rect.width &&
			   p.y >= rect.y && p.y <= rect.y+rect.height;
	}
	
	// Checks if we are near a certain point
	public static boolean nearPointInRect(Point p, Rectangle rect){
		int x1 = rect.x, y1 = rect.y, x2 = rect.x+rect.width, y2 = rect.y+rect.height; 
		Point m = new Point((x1+x2)/2, (y1+y2)/2);
		return getDifference(p, m) <= 10;
	}

	// Waits until we stopped moving
	public static void waitUntilNotMoving(){
		int matches = 0;
		Task.sleep(500, 1500);
		
		while(matches < 30){
			//System.out.println("Matches: " + matches);
			Point oldPos = TPS.getRelativePosition();
			Task.sleep(20, 60);
			Point newPos = TPS.getRelativePosition();
			double difference = getDifference(oldPos, newPos);
			if(difference > 0){
				matches = 0;
			}else{
				matches++;
			}
		}
	}
	
	// Opens the chosen tab
	public static void getTab(TABS tab){
		if(GameTab.getOpen() != tab){
			GameTab.open(tab);
		}
	}
	
	// Returns the team we are in
	public static Team getTeam(){
		getTab(TABS.INVENTORY);
		
		if(Inventory.find(greenGeneratorID).length > 0){
			return Team.GREEN;
		}else if(Inventory.find(yellowGeneratorID).length > 0){
			return Team.YELLOW;
		}
		
		return Team.UNDEFINED;
	}
	
	// Returns true if we are in a team
	public static boolean inTeam(){
		return !getTeam().equals(Team.UNDEFINED);
	}
	
	// Returns a CustomRet_0P of the nearest ScreenModel for DynamicClicking
	public static CustomRet_0P<ScreenModel> getScreenModel(final long... ids){
		return new CustomRet_0P<ScreenModel>() {
            @Override
            public ScreenModel ret() {
                ScreenModel[] models = ScreenModels.findNearest(ids);
                if (models.length > 0){
                	return models[0];
                }else{
                	return null;
                }
            }
        };
	}
	
	// Tries to set the NPC texture
	public static int getNPCTexture(){
		ColourPoint[] find = Screen.findColours(npcColor, Constants.MMCX-75, Constants.MMCY-75, Constants.MMCX+75, Constants.MMCY+75, new Tolerance(25));
		
		if(find.length > 0){
			Texture[] all = Textures.getAll();
			for(int i = 0; i < find.length; i++){
				for(int j = 0; j < all.length; j++){
					Point p = new Point(all[j].x, all[j].y);
					if(General.getDifference(p, find[i].point) < 5){
						//System.out.println("NPC id: " + all[j].crc16);
						return all[j].crc16;
					}
				}
			}
		}
		
		return -1;
	}
	
	// Checks if we are logged in
	public static boolean areLoggedIn(){
		return ScreenModels.getAll().length > 0;
	}
	
	// Handles run	
	public static void handleRun(){
		// Setting run mode on if it's off.
		if(!Game.isRunModeOn()){
			Game.setRunMode(true);
		}
		
		// Resting if needed
		if(Game.getEnergy() < 30){
			Mouse.clickBox(716, 152, 728, 167, 3);
			if(Timing.waitChooseOption("Rest", 500)){
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < 5000){
					if(Game.getEnergy() > 30){
						break;
					}
					Task.sleep(50, 100);
				}
			}
		}
	}
	
	// Gets the angle between two points
	private static double getAngle(Point p1, Point p2){
		return Math.toDegrees(Math.atan2(p1.y - p2.y, p1.x - p2.x)) + 94;
	}

	// Turns to a Point on the minimap
	public static void turnToMinimapPoint(Point p){
		if(Minimap.isOnMinimap(p)){
			//double degrees = getAngle(p, mmc) -  Minimap.getRotationAngle();
			double degrees = 360 - Minimap.getRotationAngle() - getAngle(p, mmc);
			System.out.println("MM Degrees: " + degrees);
			Camera.setRotation((int)Math.round(degrees));
		}
	}
	
	// Turns to a Point on the mainscreen
	public static void turnToMainscreenPoint(Point p){
		if(Screen.isInViewport(p)){
			//int degrees = (int)Math.abs(getAngle(p, new Point(Constants.MSCX, Constants.MSCY)) - Minimap.getRotationAngle());
			double degrees = Math.abs(Minimap.getRotationAngle() - getAngle(p, mmc));
			System.out.println("MS degrees: " + degrees);
			Camera.setRotation((int) degrees);
		}
	}

}