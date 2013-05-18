package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import org.tribot.api.ChooseOption;
import org.tribot.api.Player;
import org.tribot.api.ScreenModels;
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
    
    public class JJsFlyFisher extends Script implements Painting {
    	final Random Randomizer = new Random();
    	final long FishSpotID = 2704514198L, AnimatingID = 1506862769L;
    	final int FishID = 323884, FishSymbolID = 8292, MouseSpeed = 115, MMX1 = 550, MMY1 = 58, MMX2 = 704 , MMY2 = 212;
    	final Point mmMiddle = new Point(627, 135);
    	public boolean Fishing, Moving;
    	public String Status;
    	
    	
        public void onPaint(Graphics g) {
            g.setColor(Color.WHITE);
            g.drawString("JJ's Fly Fisher", 5, 75);
            g.drawString("Status: " + Status, 5, 100);
            
        }
            
    		@Override
            public void run() {
                // General information
                println("JJ's Fly Fisher has been started!");
                Mouse.setSpeed(MouseSpeed);

                // Checking which action to perform.
                if (ScreenModels.find(FishSpotID).length == 0){
                	Status = "Moving";				// No visible fishing spots, changing to new location
                }else{
                	if (Inventory.find(FishID).length >= 27){
                		Status = "Dropping";		// Full inventory, dropping fish
                	}else{
                		if (ScreenModels.find(AnimatingID).length > 0){
                			Status = "Fishing";		// We are fishing, time to sleep
                		}else{
                			Status = "FishSpot";	// Fishing spots have moved, need to click on a new spot
                		}
                	}
                }
                
                println(Status);
                
                if (Status == "Moving"){
                	// Walking to another fishing spot by using the symbol.
                    MinimapIcon[] fishsymbol = Minimap.findIcons(FishSymbolID);
                    if (fishsymbol.length >= 2){
                    	// Calculating the fishing spot the furthest away because the fish will be there.
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
                    }else{
                    	println("fish symbols arent visible, terminate script");
                    }
                }
               
                if (Status == "Dropping"){
                	println("Our inventory is full, time to drop.");
                	InventoryItem[] fishes = Inventory.find(FishID);
            		
		            for(int i = 0; i < fishes.length; i++){
		            	fishes[i].click("Drop");
		            }
                }
                
                if (Status == "Fishing"){
                	println("We are fishing");
                	sleep(1000, 4000);
                }
                
                if (Status == "FishSpot"){
                	println("We need to fish again");
                	ScreenModel[] fishspots = ScreenModels.find(FishSpotID);
                    if (fishspots.length > 0) {
                        // Clicking on the fishing spot.
                    	Point FS = new Point(fishspots[0].points[0].x - Randomizer.nextInt(35), fishspots[0].points[0].y - Randomizer.nextInt(35));
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
                    }
                }
                
                
                
                

                 

         
                
                /** STUFFFFFFFFFFF
                // While the inventory isn't full and while the player is animating, sleep.
                int InvFish = Inventory.find(FishID).length;
                ScreenModel[] Animating = ScreenModels.find(AnimatingID);
        		while (Inventory.find(FishID).length < 27 && ScreenModels.find(AnimatingID).length > 0){
                	println("We are currently fishing and the inventory isn't full yet.");
                	sleep(1000, 4000);
                }
                
                // If we aren't fishing anymore but if the inventory isn't full, click on fish spot.
                if (InvFish < 27 && Animating.length < 1){
                	// Detecting the fishing spot and starting the fishing.
                    ScreenModel[] fishspots = ScreenModels.find(FishSpotID);
                    if (fishspots.length > 0) {
                        println("We have found the fishing spot, time to fish!");
                        Point FS = new Point(fishspots[0].points[0].x - Randomizer.nextInt(35), fishspots[0].points[0].y - Randomizer.nextInt(35));
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
                    } else {
                        println("We haven't found the fishing spot.");
                        // Walking to another fishing spot by using the symbol.
                        MinimapIcon[] fishsymbol = Minimap.findIcons(FishSymbolID);
                        if (fishsymbol.length >= 2){
                        	println("We have found a fishing symbol, moving there.");
                        	// Calculating the fishing spot the furthest away because the fish will be there.
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
                        }else{
                        	println("We haven't been able to find two fishing symbols.");
                        }
                    }
                }
                
                // If the inventory is full, drop all fish.
                if (Inventory.find(FishID).length >= 27){
		            println("Our inventory is full of fish, time to drop!");
		            InventoryItem[] fishes = Inventory.find(FishID);
		
		            for(int i = 0; i < fishes.length; i++){
		            	fishes[i].click("Drop");
		            }
                }
                */
                
            }
    }