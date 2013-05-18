package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import org.tribot.api.ChooseOption;
import org.tribot.api.Player;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.Inventory;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.MinimapIcon;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.Minimap;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;


    @ScriptManifest(authors = { "J J" }, category = "Fishing", name = "JJ's Fly Fisher")
    
    public class JJsFlyFisher2 extends Script implements Painting {
    	final Random Randomizer = new Random();
    	final long FishSpotID = 2704514198L, AnimatingID = 1506862769L;
    	final int FishID = 323884, FishSymbolID = 8292;
    	final Point mmMiddle = new Point(627, 135);
    	public boolean Fishing, Moving;
    	public String status;
    	public int e, xOffset, yOffset;
    	long StartTime = 0;

        public void onPaint(Graphics g) {
            g.setColor(Color.CYAN);
            g.drawString("JJ's Fly Fisher", 5, 100);
            g.setColor(Color.WHITE);
            g.drawString("Time Running: " + Timing.msToString(System.currentTimeMillis() - StartTime), 5, 120);
            g.drawString("Status: " + status, 5, 140);
        }
            
        public void onStop() {
            println("Thanks for using JJ's Fly Fisher");
            println("Script has ran for: " + Timing.msToString(System.currentTimeMillis() - StartTime));
        }

	    public boolean onStart() {
	    	// General information to start everything up.
	        println("JJ's Fly Fisher has been started!");
	        status = "Starting up";
	        Mouse.setSpeed(150);
	        StartTime  = System.currentTimeMillis();
	            return true;
	    }
	    
	    public boolean goodtogo() {
	    	if (Minimap.findIcons(FishSymbolID).length >= 2){
            	return true;
            }else{
            	return false;
            }
	    }
	    
        public void mainloop() {
            // Checking which action to perform.
            if (ScreenModels.find(FishSpotID).length == 0){
            	status = "Moving";				// No visible fishing spots, changing to new location
            }else{
            	if (Inventory.find(FishID).length >= 27){
            		status = "Dropping";		// Full inventory, dropping fish
            	}else{
            		if (ScreenModels.find(AnimatingID).length > 0){
            			status = "Fishing";		// We are fishing, time to sleep
            		}else{
            			status = "FishSpot";	// Fishing spots have moved, need to click on a new spot
            		}
            	}
            }
            
            // Performing the action
            if (status == "Fishing"){
            	sleep(1000, 4000);
            }

            if (status == "FishSpot"){
            	ScreenModel[] fishspots = ScreenModels.find(FishSpotID);
                if (fishspots.length > 0) {
                    // Clicking on the fishing spot, if there are two then randomly picks one
                	if (fishspots.length >= 2){
                		e = Randomizer.nextInt(2);
                	}else{
                		e = 0;
                	}
                	Point FS = new Point(fishspots[e].points[0].x - Randomizer.nextInt(20), fishspots[e].points[0].y - Randomizer.nextInt(20));
	            	
                	if (FS.y < 160 || FS.y > 430){
	            		status = "Moving";
	            	}else{
		                	Mouse.move(FS);
		
		                    switch(Randomizer.nextInt(7)){
		                        case 0: case 1: Mouse.click(FS, 3);
		                                        int waiting = 0;                     
		                                        while(!ChooseOption.isOptionValid("Lure") && waiting < 10){
		                                             sleep(50, 150);
		                                             waiting++;
		                                        }
		                                        if (waiting < 10){
		                                            ChooseOption.select("Lure");
		                                        }
		                                        break;
		                        default: Mouse.click(FS, 1);
		                    }
		                    
		                    // Waiting until the character stops walking.
		                	while (Player.isMoving()){
		                		sleep(50, 150);
		                	}
	                	}
	                }
            }
            
            if (status == "Moving"){
            	// Walking to another fishing spot by using the symbol.
                MinimapIcon[] fishsymbol = Minimap.findIcons(FishSymbolID);
                if (fishsymbol.length == 2){
                	// Calculating the fishing spot the farthest away because the fish will be there.
                	int Symbol0 = Math.abs(fishsymbol[0].x-mmMiddle.x) + Math.abs(fishsymbol[0].y-mmMiddle.y);
                	int Symbol1 = Math.abs(fishsymbol[1].x-mmMiddle.x) + Math.abs(fishsymbol[1].y-mmMiddle.y);
                	if (Symbol0 > Symbol1){
                		Point SS = new Point(fishsymbol[0].x + Randomizer.nextInt(5), fishsymbol[0].y + Randomizer.nextInt(5));
                        Mouse.move(SS);
                        Mouse.click(SS, 1);
                	}else{
                		Point SS = new Point(fishsymbol[1].x + Randomizer.nextInt(5), fishsymbol[1].y + Randomizer.nextInt(5));
                        Mouse.move(SS);
                        Mouse.click(SS, 1);
                	}
                	
                	// Waiting until the character stops walking.
                	while (Player.isMoving()){
                		sleep(50, 150);
                	}
                }
            }
            
            if (status == "Dropping"){
            	InventoryItem[] fishes = Inventory.find(FishID);
            	int[] droporder = {0, 4, 8, 12, 16, 20, 24, 1, 5, 9, 13, 17, 21, 25, 2, 6, 10, 14, 18, 22, 26, 3, 7, 11, 15, 19, 23};
            	
	            for(int i = 0; i < fishes.length; i++){
	            	// Moves the mouse roughly to the middle of the item.
	            	xOffset = Randomizer.nextInt(7);
	            	yOffset = Randomizer.nextInt(7);
	            	Point FS = new Point(fishes[droporder[i]].x + 10 + xOffset, fishes[droporder[i]].y + 13 + yOffset);
	            	Mouse.move(FS);
	            	Mouse.click(FS, 3);

	            	// Selects the drop option.
                	xOffset = Randomizer.nextInt(15);
	            	yOffset = Randomizer.nextInt(5);
                	Point DR = new Point(Mouse.getPos().x - 7 + xOffset, ChooseOption.getPosition().y + 45 + yOffset);
                	Mouse.move(DR);
                	Mouse.click(DR, 1);
	            }
            }
        }
        
        @Override
	    public void run() {
            if(onStart()){
            	while(goodtogo()){
            		mainloop();
            	}
            	onStop();
            }
	    }
    }