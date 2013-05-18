package scripts;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import org.tribot.api.Screen;
import org.tribot.api.NPCChat;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Random Events", name = "Molly")
public class Molly extends Script{
	
	final long doorID = 3072665158L,
			   grabberID = 2497587316L,
			   operatingMachineID = 1184286808L;
	final long[] notMolly = {2787683410L, 2590718638L, 752464649L};
	long mollyID;
	int attempt = 0, maxAttempt = 10;
	
	public int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	private long getMollyID(){
		ScreenModel[] all = ScreenModels.getAll();
		for (int i = 0; i < all.length; i++){
			Point P = all[i].base_point;
			if (P.x >= 240 && P.x <= 280){
				if (P.y >= 240 && P.y <= 280){
					for (int j = 0; j < notMolly.length; j++){
						if (all[i].id == notMolly[j]){
							continue;
						}
					}
					return all[i].id;
				}
			}
		}
		return -1;
	}
	
	private boolean clickScreenModel(long modelID, int xOffset, int yOffset, String uptext){
		ScreenModel[] find = ScreenModels.find(modelID);
		if (find.length > 0){
			Point P = new Point(find[0].base_point.x + xOffset, find[0].base_point.y + yOffset);
			Mouse.move(P);
			if (Timing.waitUptext(uptext, 1000)){
				Mouse.click(1);
				return true;
			}
		}
		return false;
	}
	
	private boolean openDoor(){
		if (clickScreenModel(doorID, randomRange(15, 25), randomRange(-3, 3), "Open Door")){
			sleep(3000, 4000);
			return true;
		}else{
			if (attempt < maxAttempt){
				attempt++;
				openDoor();
			}
		}
		return false;
	}
	
	private boolean openMachine(){
		if (clickScreenModel(operatingMachineID, randomRange(-5, 5), randomRange(-5, 5), "Use Control panel")){
			sleep(4000, 6000);
			return true;
		}else{
			if (attempt < maxAttempt){
				attempt++;
				openMachine();
			}
		}
		return false;
	}
	
	private void moveTo(String direction){
		String[] directions = {"up", "right", "down", "left"};
		Point up = new Point(643, 319),
			  right = new Point(713, 382),
			  down = new Point(643, 450),
			  left = new Point(574, 382);
		Point[] places = {up, right, down, left};
		
		for (int i = 0; i < directions.length; i++){
			if (direction.equals(directions[i])){
				Point P = new Point(places[i].x + randomRange(-5, 5), places[i].y + randomRange(-5, 5));
				Mouse.move(P);
				Mouse.click(1);
				sleep(100, 200);
			}
		}
	}
	
	private void clickGrab(){
		Point P = new Point(700 + randomRange(-10, 10), 474 + randomRange(-10, 10));
		Mouse.move(P);
		Mouse.click(1);
	}
	
	private boolean waitOnSuccess(long time){
		long maxTime = System.currentTimeMillis() + time;
		while(System.currentTimeMillis() < maxTime){
			String[] options = NPCChat.getOptions();
			if (options.length > 0){
				for (int i = 0; i < options.length; i++){
					if (options[i].contains("Well done")){
						return true;
					}
				}
			}
			sleep(500, 1000);
		}
		
		return false;
	}
	
	private boolean operateMachine(long time){
		long maxTime = System.currentTimeMillis() + time;
		boolean catched = false;
		
		
		while(System.currentTimeMillis() < maxTime && !catched){
			Point twin = ScreenModels.find(mollyID)[0].base_point,
				  grabber = ScreenModels.find(grabberID)[0].base_point;
			int xDist = twin.x - grabber.x,
			    yDist = twin.y - grabber.y;
			double dist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
			
			//println("x-dist: " + xDist);
			//println("y-dist: " + yDist);
			//println("Dist: " + dist);
			
			if (dist > 10){
				double horizontal = Math.round(xDist/30),
					   vertical = Math.round(yDist/15);
				
				// HORIZONTAL CHECK
				if (horizontal < 0){
					//println("Move " + horizontal + " to the left");
					for (int i = 0; i < Math.abs(horizontal); i++){
						moveTo("left");
					}
				}else{
					//println("Move " + horizontal + " to the right");
					for (int i = 0; i < Math.abs(horizontal); i++){
						moveTo("right");
					}
				}
				
				// VERTICAL CHECK
				if (vertical < 0){
					//println("Move " + vertical + " up");
					for (int i = 0; i < Math.abs(vertical); i++){
						moveTo("up");
					}
				}else{
					//println("Move " + vertical + " down");
					for (int i = 0; i < Math.abs(vertical); i++){
						moveTo("down");
					}
				}
			}else{
				println("We can grab Molly!");
				//clickGrab();
				return true;
				/*
				if (waitOnSuccess(10000)){
					catched = true;
					return true;
				}
				*/
			}
		}
		
		if (!catched){
			return false;
		}else{
			return true;
		}
	}
	
	private void mollyTalk(){
		boolean finished = false;
		
		if (clickScreenModel(mollyID, randomRange(-5, 5), randomRange(-5, 5), "Talk-to Molly")){
			sleep(3000, 4000);
			
			while(!finished){
				ColourPoint[] pts = Screen.findColours(new Color(40, 75, 40), 227, 513, 293, 522, new Tolerance(40));
				if (pts.length > 10){
					NPCChat.clickContinue(true);
				}else{
					finished = true;
					return;
				}
			}
		}
	}
	
	@Override
    public void run() {
		mollyID = getMollyID();
		println("Molly ID: " + mollyID);
		if (mollyID != -1){
			if (openDoor()){
				if (openMachine()){
					if (operateMachine(600000)){
						if (openDoor()){
							mollyTalk();
						}
					}
				}
			}
		}
	}
}