package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Random;

import org.tribot.api.Banking;
import org.tribot.api.ChooseOption;
import org.tribot.api.Constants;
import org.tribot.api.DTMs;
import org.tribot.api.Game;
import org.tribot.api.GameTab;
import org.tribot.api.Inventory;
import org.tribot.api.Minimap;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.TPS;
import org.tribot.api.Timing;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.DTM;
import org.tribot.api.types.colour.DTMPoint;
import org.tribot.api.types.colour.DTMSubPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Collecting", name = "Nightshade collector")
public class NightShade extends Script implements Painting{
	final long bankID = 2550191772L, shadeID = 4259706229L, caveID = 2404500358L, startTime = System.currentTimeMillis();
	final int ringInvID = 2322499, shadeInvID = 164311;
	int charges = 0, collectedShades = 0, attempt = 0;
	String status = "Starting up";
	boolean goodToGo = true;
	
	public void onPaint(Graphics g) {
		g.setColor(new Color(31, 31, 31));
		g.fill3DRect(0, 54, 215, 75, true);
		
		g.setFont(new Font ("Verdana", Font.BOLD, 15));
		g.setColor(new Color(87, 0, 145));
		g.drawString("JJ's Nightshade Collector", 5, 75);
		
		g.setFont(new Font ("Verdana", Font.PLAIN, 9));
        g.setColor(new Color(5, 156, 7));
		g.drawString("Time Running: " + Timing.msToString(System.currentTimeMillis() - startTime), 5, 90);
		g.drawString("Status: " + status, 5, 100);
		g.drawString("Nightshades collected: " + collectedShades, 5, 110);
		g.drawString("That's " + toHour(collectedShades) + " nightshades per hour", 5, 120);
	}
	
	// Converts to xp per hour
	private int toHour(double detail){
		long difference = System.currentTimeMillis() - startTime;
		double xpH = (3600000 * detail) / difference;

		return (int)xpH;
	}
	
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
    
    private boolean randomSelectOptionAt(Point coord, String upText, String option, int leftChance, int rightChance, int maxWait){
    	int totalChance = leftChance + rightChance;
    	Mouse.move(coord);
    	if (Timing.waitUptext(upText, (maxWait/2))){
        	if ((new Random().nextInt(totalChance)) < leftChance){
        		Mouse.click(coord, 1);
        		return true;
        	}else{
        		Mouse.click(coord, 3);
        		if (Timing.waitChooseOption(option, (maxWait/2))){
        			ChooseOption.select(option);
        			return true;
        		}else{
        			return false;
        		}
        	}
    	}else{
    		return false;
    	}
    }
    
    // Checks if the player is moving
 	public boolean isMoving(){
 		Point MMc = new Point(627, 135);
 		int pixelShift = Screen.getPixelShift(MMc.x - 40, MMc.y - 40, MMc.x + 40, MMc.y + 40, 250);
 		println("Pixelshift: " + pixelShift);
 		if (pixelShift > 100){
 			return true;
 		}
 		
 		return false;
 	}
 	
 	// Waits until we are not moving
 	private void waitUntilNotMoving(){
     	sleep(1000, 1500);
 		while (isMoving()){
 			sleep(50, 100);
 		}
     }
 	
 	// Blindly walks
 	private boolean blindWalkTo(Point pos, int maxTime) {
        long A = System.currentTimeMillis();
        long B = A + maxTime;
        Point MMc = new Point(627, 135);
        int radius = 75;

        while ((Math.abs(TPS.getPosition().x - pos.x) + Math.abs(TPS.getPosition().y - pos.y)) > 10 && (System.currentTimeMillis() - A) < B ) {
            Point myPos = TPS.getPosition();
            int xDist = pos.x - myPos.x;
            int yDist = pos.y - myPos.y;
            int absX = Math.abs(xDist);
            int absY = Math.abs(yDist);

            if (absX <= 50 && absY <= 50) {
                Point P = TPS.posToMM(pos);
                if (P.x > 549 && P.x < 704 && P.y > 57 && P.y < 202){
	                Mouse.move(P);
	                Mouse.click(1);
	                
	                Point PP = TPS.MMToPos(P);
	                
	                int dif = Math.abs(TPS.getPosition().x - PP.x) + Math.abs(TPS.getPosition().y - PP.y);
	                while(dif > randomRange(5, 15)){
	                	boolean wait = true;
	                	
	                	sleep(50, 100);
	                	dif = Math.abs(TPS.getPosition().x - PP.x) + Math.abs(TPS.getPosition().y - PP.y);
	                	
	                	switch(Constants.RANDOM.nextInt(1000)){
	                		case 0: Mouse.click(1); wait = false; break;
	                	}
	                	
	                	if (!wait){
	                		break;
	                	}
	                }
	
	                if ((System.currentTimeMillis() - A) < B){
	                    break;
	                }
                }
            }

            Point[] TPA = new Point[(absX + 1) * (absY + 1)];
            for (int i = 0; i < absX; i++) {
                for (int j = 0; j < absY; j++) {
                    if (xDist < 0) {
                        if (yDist < 0)
                            TPA[i * j + j] = new Point(myPos.x - i, myPos.y - j);
                        else
                            TPA[i * j + j] = new Point(myPos.x - i, myPos.y + j);
                    } else {
                        if (yDist < 0)
                            TPA[i * j + j] = new Point(myPos.x + i, myPos.y - j);
                        else
                            TPA[i * j + j] = new Point(myPos.x + i, myPos.y + j);
                    }
                }
            }

            int far = 0;
            Point bestPoint = new Point(0, 0);
            for (int i = 0; i < TPA.length; i++) {
                if (TPA[i] != null) {
                    Point P = new Point(TPA[i].x, TPA[i].y);
                    xDist = Math.abs(myPos.x - P.x);
                    yDist = Math.abs(myPos.y - P.y);
                    if ((xDist + yDist) > far) {
                        if (xDist <= radius) {
                            if (yDist <= radius) {
                                P = TPS.posToMM(P);
                                double dist = Math.pow(MMc.x - P.x, 2)
                                                + Math.pow(MMc.y - P.y, 2);
                                if (dist < (Math.pow(radius, 2))) {
                                        far = (xDist + yDist);
                                        bestPoint = P;
                                }
                            }
                        }
                    }
                }
            }

            if (bestPoint != new Point(0, 0)) {
            	if (bestPoint.x > 549 && bestPoint.x < 704 && bestPoint.y > 57 && bestPoint.y < 202){
	                Mouse.move(bestPoint);
	                Mouse.click(1);
	                Point PP = TPS.MMToPos(bestPoint);
	                
	                int dif = Math.abs(TPS.getPosition().x - PP.x) + Math.abs(TPS.getPosition().y - PP.y);
	                while(dif > randomRange(20, 40)){
	                	sleep(50, 100);
	                	dif = Math.abs(TPS.getPosition().x - PP.x) + Math.abs(TPS.getPosition().y - PP.y);
	                }
	
	                Point coord = TPS.MMToPos(bestPoint);
	                int dist = Math.abs(coord.x - pos.x) + Math.abs(coord.y - pos.y);
	                if ((dist < 5) && (System.currentTimeMillis() - A) < B){
	                	break;
	                }
            	}
            }
        }
        
        sleep(500, 1000);
        Point P = TPS.getPosition();
        int dif = Math.abs(P.x - pos.x) + Math.abs(P.y - pos.y);
        if (dif < 10){
        	return true;
        }

        return false;
    }
 	
 	// Switches to the desired Tab
 	private boolean FTab(TABS Tab){
 		char FKey;
 		
 		if (GameTab.getOpen() != Tab){
 			switch(Tab){
 				case INVENTORY: FKey = (char)KeyEvent.VK_F1; break;
 				case EQUIPMENT: FKey = (char)KeyEvent.VK_F2; break;
 				case PRAYERS: FKey = (char)KeyEvent.VK_F3; break;
 				case MAGIC: FKey = (char)KeyEvent.VK_F4; break;
 				case COMBAT: FKey = (char)KeyEvent.VK_F5; break;
 				default: return false;
 			}
 			
 			if (Character.isDefined(FKey)){
 				Keyboard.pressKey(FKey);
 			 	sleep(50, 100);
 			 	Keyboard.releaseKey(FKey);
 			 	sleep(250, 500);
 			 	return true;
 			}
 		}
 		
 		return false;
 	}
 	
	// Walks to the Skavid cave
	private boolean toSkavid(){
		status = "Running to cave";
		Point P = new Point(randomRange(2033, 2046), randomRange(6531, 6535));
		if (blindWalkTo(P, 30000)){
			return true;
		}
		return false;
	}
	
	// Teleports to CW
	private boolean toCW(){
		status = "Teleporting to CW";
		FTab(TABS.EQUIPMENT);
		InventoryItem[] ring = Inventory.find(ringInvID); 
		if (ring.length > 0){
			Point P = new Point(ring[0].x + 15 + randomRange(-10, 10), ring[0].y + 15 + randomRange(-10, 10));
			Mouse.move(P);
			if (Timing.waitUptext("duelling", 1000)){
				String uptext = Game.getUptext();
				for (char c: uptext.toCharArray()){
			        if (Character.isDigit(c)){
			        	String nr = String.valueOf(c);
			        	charges = Integer.parseInt(nr);
			        	break;
			        }
			    }
					
				println("Charges: " + charges);
				
				Mouse.click(3);
				if (Timing.waitChooseOption("Castle Wars", 1000)){
					sleep(4000, 5000);
					return true;
				}
			}
		}
		return false;
	}
	
	// Walks to the chest
	private boolean toBank(){
		status = "Running to bank";
		Point P = new Point(randomRange(1717, 1722), randomRange(6481, 6490));
		if (blindWalkTo(P, 30000)){
			return true;
		}
		return false;
	}
	
	// Waits until the bankscreen is visible
	private boolean waitBankscreen(long time){
		status = "Waiting for bankscreen";
		long maxTime = System.currentTimeMillis() + time;
		while (System.currentTimeMillis() < maxTime){
			if (Banking.isBankScreenOpen()){
				sleep(200, 400);
				return true;
			}
			sleep(10, 50);
		}
		return false;
	}
	
	// Opens the cw bank
	private boolean openBank(){
		status = "Opening bank";
		ScreenModel[] bank = ScreenModels.find(bankID);
		if (bank.length > 0){
			Point P = new Point(bank[0].base_point.x + randomRange(-5, 5), bank[0].base_point.y + randomRange(-5, 5));
			if (randomSelectOptionAt(P, "Bank chest", "bank", 1, 0, 1000)){
				return true;
			}else{
				attempt++;
				if (attempt < 0){
					if (openBank()){
						attempt = 0;
					}
				}
			}
		}
		return false;
	}
	
	// Deposits night shades
	private boolean deposit(){
		int MSX1 = 4, MSX2 = 515, MSY1 = 54, MSY2 = 387;
		
		status = "Depositing nightshades";
		if (waitBankscreen(5000)){
			InventoryItem[] shade = Inventory.find(shadeInvID);
			if (shade.length > 0){
				int r = randomRange(0, shade.length-1);
				Point P = new Point(shade[r].x + 15 + randomRange(-10, 10), shade[r].y + 15 + randomRange(-10, 10));
				if (!randomSelectOptionAt(P, "nightshade", "All", 0, 1, 1000)){
					ChooseOption.select("Deposit");
				}
			}
			if (charges <= 1){
				status = "Withdrawing duel ring";
	
				DTMPoint DTM_PT_0 = new DTMPoint(new Color(217, 188, 31), new Tolerance(20, 20, 20));
				DTMSubPoint[] DTM_PTS_0 = { new DTMSubPoint(new ColourPoint(new Point(2, -6), new Color( 17, 119, 20)), new Tolerance(20, 20, 20),1), new DTMSubPoint(new ColourPoint(new Point(-4, 10), new Color( 188, 163, 27)), new Tolerance(20, 20, 20),1)};
				DTM ring = new DTM(DTM_PT_0, DTM_PTS_0);
	
				
				Point[] matches = DTMs.find_simple(ring, MSX1, MSY1, MSX2, MSY2);
				if (matches.length > 0){
					Point W = new Point(matches[0].x + randomRange(-10, 10), matches[0].y + 5 + randomRange(-10, 10));
					if (randomSelectOptionAt(W, "Ring of duelling", "Withdraw-1", 1, 0, 1000)){
						return true;
					}
				}else{
					goodToGo = false;
				}
			}
		}
		return false;
	}
	
	// Equips duel ring
	private boolean equipRing(){
		status = "Equiping duel ring";
		InventoryItem[] ring = Inventory.find(ringInvID);
		if (ring.length > 0){
			Point P = new Point(ring[0].x + 15 + randomRange(-10, 10), ring[0].y + 15 + randomRange(-10, 10));
			if (randomSelectOptionAt(P, "Ring of duelling", "Equip", 1, 0, 1000)){
				return true;
			}
		}
		return false;
	}
	
	private boolean enterCave(){
		status = "Entering cave";
		ScreenModel[] cave = ScreenModels.find(caveID);
		if (cave.length > 0){
			Point P = new Point(cave[0].base_point.x + randomRange(-10, 10), cave[0].base_point.y + randomRange(-10, 10));
			if (randomSelectOptionAt(P, "Cave entrance", "Enter", 2, 1, 1000)){
				sleep(1500, 3000);
				return true;
			}else{
				attempt++;
				if (attempt < 0){
					if (enterCave()){
						attempt = 0;
					}
				}
			}
		}
		return false;
	}
	
  private void toPos(Point pos){
	  	Point MMc = new Point(627, 135);
	  
		Point myPos = TPS.getRelativePosition();
		println("My position: " + myPos);
		int xDifference = pos.x - myPos.x,
			yDifference = pos.y - myPos.y;
		double difference = Math.abs(xDifference) + Math.abs(yDifference);
	
		if (difference > 10){
			Point P = new Point(MMc.x + xDifference, MMc.y + yDifference);
			if (Minimap.isOnMinimap(P)){
				Mouse.move(P);
				Mouse.click(1);
			}else{
				P = new Point(MMc.x + xDifference/2, MMc.y + yDifference/2);
				Mouse.move(P);
				Mouse.click(1);
				waitUntilNotMoving();
				toPos(pos);
			}
			waitUntilNotMoving();
		}
	}
	
	private void toShade(){
		status = "Running to nightshades";
		Point P = TPS.getRelativePosition();
		Point shade = new Point(P.x, P.y+20);
		toPos(shade);
	}
	
	private boolean fullInv(){
		if (GameTab.getOpen() != TABS.INVENTORY){
			GameTab.open(TABS.INVENTORY);
			sleep(500, 1000);
		}
		
		
		if (Inventory.getAll().length >= 27){
			return true;
		}
		return false;
	}
	
	private boolean collectShades(long time){
		status = "Collecting nightshades";
		long maxTime = System.currentTimeMillis() + time,
		     grabTime = System.currentTimeMillis();
		
		while (System.currentTimeMillis() < maxTime && !fullInv()){
			ScreenModel[] shade = ScreenModels.find(shadeID);
			if (shade.length > 0){
				Point P = new Point(shade[0].base_point.x + randomRange(-5, 5), shade[0].base_point.y + randomRange(-5, 5));
				if (randomSelectOptionAt(P, "Cave nightshade", "Take", 3, 1, 1000)){
					grabTime = System.currentTimeMillis();
					sleep(1000, 2000);
					collectedShades++;
				}
			}
			sleep(250, 750);
			
			if ((System.currentTimeMillis() - grabTime) > 120000){
				return false;
			}
		}
		
		if (System.currentTimeMillis() < maxTime){
			return true;
		}
		return false;
	}
	
	@Override
    public void run() {
		println("JJ's Nightshade collector started!");

		while(goodToGo){		
			// BANKING
			if (toCW()){
				if (toBank()){
					if (openBank()){
						deposit();
					}
				}
			}
	    	
			// COLLECTING
	    	if (toSkavid()){
		    	if (charges <= 1){
		    		equipRing();
		    	}
		    	if (enterCave()){
		    		toShade();
		    		if (!collectShades(3000000)){
		    			goodToGo = false;
		    		}
		    	}
	    	}
		}
	}
}