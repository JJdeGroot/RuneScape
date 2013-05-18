package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import org.tribot.api.Banking;
import org.tribot.api.Constants;
import org.tribot.api.EGW;
import org.tribot.api.Inventory;
import org.tribot.api.Minimap;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Crafting", name = "JJ's Gold Bracelet Maker")
public class JJsBraceletMaker extends Script implements Painting{
	final long bankerID = 2076007400L,
			   bankboothID = 1341970251L,
			   furnaceID = 2086087509L;
	final int goldBarID = 261751;
	final Rectangle bankArea = new Rectangle(3269, 3170, 4, 2),
					furnaceArea = new Rectangle(3275, 3188, 2, 2);

	// Draws the paint
	public void onPaint(Graphics g){
		g.setColor(Color.WHITE);
		Point P = EGW.getPosition();
		g.drawString("Location: (" + P.x + ", " + P.y + ")", 565, 270);
	}
	
	public void getScreenshot(){
		BufferedImage img = Screen.captureDebugImage();
		Graphics g = img.getGraphics();
		g.drawString("Test123", 0, 0);
		
        try {
        	File file = new File("C:/");
            ImageIO.write(img, "png", file);
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	
	// Calculates a random number between two numbers.
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	// Calculates the real difference between two points.
	public double getDifference(Point P1, Point P2){
		return Math.sqrt(Math.pow(Math.abs(P1.x-P2.x), 2) + Math.pow(Math.abs(P1.y-P2.y), 2));
	}
	
	// Generates a random point in a rectangle
	public Point randomPtInRect(Rectangle rect){
		int x = randomRange(rect.x, rect.x+rect.width);
		int y = randomRange(rect.y, rect.y+rect.height);
		return new Point(x, y);
	}
	
	// Opens the bank
	private boolean openBank(){
		long ID;
		String uptext;
		int r = randomRange(0, 1);
		if(r == 0){
			ID = bankerID;
			uptext = "Banker";
		}else{
			ID = bankboothID;
			uptext = "Bank booth";
		}
		
		ScreenModel[] bank = ScreenModels.findNearest(ID);
		if(bank.length > 0){
			Point P = new Point(bank[0].base_point.x + randomRange(-8, 8), bank[0].base_point.y + randomRange(-15, -5));
			Mouse.move(P);
			if(Timing.waitUptext(uptext, 500)){
				if(uptext.contains("Banker")){
					Mouse.click(3);
					if(Timing.waitChooseOption("Bank Banker", 500)){
						return true;
					} 
				}else{
					Mouse.click(1);
					return true;
				}
			}
		}
		
		return false;
	}
	
	// Withdraws gold bars
	private boolean withdrawGoldBars(){
		ColourPoint[] goldColors = Screen.findColours(new Color(190, 150, 30), 25, 140, 495, 340, new Tolerance(30, 25, 15));
		if(goldColors.length > 0){
			int r = randomRange(0, goldColors.length-1);
			Point P = goldColors[r].point;
			Mouse.move(P);
			Mouse.click(3);
			if(Timing.waitChooseOption("Withdraw-All Gold bar", 500)){
				return true;
			}
		}
		
		return false;
	}
	
	// Waits until our distance is close to point where the flag is based on EGW
	private boolean FFlag(Point P, int timeout){
		long start = System.currentTimeMillis();
		P = EGW.MMToPos(P);
		
		Point myPos = EGW.getPosition();
		while(getDifference(myPos, P) > randomRange(3, 6)){
			if(Timing.timeFromMark(start) > timeout){
				return false;
			}
			
			sleep(100, 200);
			myPos = EGW.getPosition();
		}
		
		return true;
	}
	
	// Waits until we stopped moving
	private void waitUntilNotMoving(){
		long t = System.currentTimeMillis();
		int matches = 0;
		while(Timing.timeFromMark(t) < 10000){
			Point oldPos = EGW.getPosition();
			sleep(100, 200);
			Point newPos = EGW.getPosition();
			double D = getDifference(oldPos, newPos);
			if(D > 1){
				matches = 0;
			}else{
				matches++;
			}
			if(matches > 5){
				break;
			}
		}
	}

	// Blind walks to a certain point
	private boolean blindWalkTo(Point P){
		Point MMc = new Point(Constants.MMCX, Constants.MMCY),
			  goal = EGW.posToMM(P),
			  myPos = EGW.getPosition(),
			  myPosMM = EGW.posToMM(myPos);
		
		double D = getDifference(goal, myPosMM);
		
		if(D > 10){
			long T = System.currentTimeMillis();

			while(Timing.timeFromMark(T) < 10000){
				while(!Minimap.isOnMinimap(goal)){
					// Generating the best possible point
					double SD = 10000;
					Point BP = new Point(MMc.x + randomRange(-50, 50), MMc.y+ randomRange(-50, 50));
					
					for(int x = MMc.x-75; x < MMc.x+75; x++){
						for(int y = MMc.y-75; y < MMc.y+75; y++){
							Point W = new Point(x, y);
							if(Minimap.isOnMinimap(W)){
								D = getDifference(goal, W);
								if(D < SD){
									SD = D;
									BP = W;
								}
							}
						}
					}
					
					// Moving to the best point
					Mouse.move(BP);
					Mouse.click(1);
					FFlag(BP, 10000);
					
					// Updating our locations/goals
					myPos = EGW.getPosition();
					myPosMM = EGW.posToMM(myPos);
					goal = EGW.posToMM(P);
				}
				
				Mouse.move(goal);
				Mouse.click(1);
				waitUntilNotMoving();
			}
		}
		
		return true;
	}
	
	// Clicks on the furnace
	private boolean clickOnFurnace(){
		ScreenModel[] furnace = ScreenModels.findNearest(furnaceID);
		if(furnace.length > 0){
			Point P = new Point(furnace[0].base_point.x + randomRange(-10, 10), furnace[0].base_point.y + randomRange(-10, 10));
			Mouse.move(P);
			if(Timing.waitUptext("Furnace", 500)){
				Mouse.click(1);
				return true;
			}
		}
		return false;
	}
	
	// Walks to the bank
	private boolean toBank(){
		Point P = randomPtInRect(bankArea);
		return blindWalkTo(P);
	}
	
	// Walks to the furnace
	private boolean toFurnace(){
		Point P = randomPtInRect(furnaceArea);
		return blindWalkTo(P);
	}
	
	// Clicks on a gold bar
	private boolean useGoldBar(){
		InventoryItem[] bars = Inventory.find(goldBarID);
		if(bars.length > 0){
			int r = randomRange(0, bars.length-1);
			Point P = new Point(bars[r].x+15+randomRange(-10, 10), bars[r].y+15+randomRange(-10, 10));
			Mouse.move(P);
			Mouse.click(1);
			return true;
		}
		return false;
	}
	
	// Checks if we are at the bracelet screen
	private boolean braceletScreen(){
		return Screen.findColours(new Color(70, 60, 50), 25, 112, 494, 368, new Tolerance(10)).length > 500;
	}

	// Clicks on bracelets
	private boolean clickOnBracelets(){
		Mouse.moveBox(123, 330, 141, 342);
		Mouse.click(3);
		return Timing.waitChooseOption("Make All", 500);
	}
	
	// Waits until we have no gold bars left
	private void waitUntilNoGoldBars(){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < 90000){
			if(Inventory.find(goldBarID).length == 0){
				return;
			}
			sleep(200, 400);
		}
	}
	
	// Handles the making of the bracelets
	private boolean doBracelets(){
		if(toFurnace()){
			if(useGoldBar()){
				if(clickOnFurnace()){
					long t = System.currentTimeMillis();
					while(Timing.timeFromMark(t) < 5000){
						if(braceletScreen()){
							if(clickOnBracelets()){
								waitUntilNoGoldBars();
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	// Handles the banking process
	private boolean doBanking(){
		if(toBank()){
			if(openBank()){
				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < 5000){
					if(Banking.isBankScreenOpen()){
						if(withdrawGoldBars()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	// MAINLOOP
	@Override
    public void run() {
    	println("JJ's Gold Bracelet Maker");
    	Mouse.setSpeed(175);
    	getScreenshot();
    	
    	//toBank();
    	//openBank();
    	//sleep(2000, 3000);
    	//withdrawGoldBars();
    	//toFurnace();
    	//useGoldBar();
    	//clickOnFurnace();
    	//blindWalkTo(new Point(3273, 3189));
    }
}