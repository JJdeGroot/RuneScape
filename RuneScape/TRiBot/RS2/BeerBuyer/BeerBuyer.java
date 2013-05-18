package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import org.tribot.api.GameTab;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.Screen;
import org.tribot.api.TPS;
import org.tribot.api.Inventory;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.NPCChat;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Money Making", name = "JJ's Beer Buyer")
public class BeerBuyer extends Script implements Painting{
	String choice = "0", // Most profit = 0, Asgarnian = 1, Wizard Mind = 2, Dwarven = 3;
		   status = "starting up";
	int attempt = 0,
	    maxAttempt = 10,
	    beers = 0,
	    profit = 0;
	final long startTime = System.currentTimeMillis();
	
	public void onPaint(Graphics g) {
		g.setColor(new Color(31, 31, 31));
		g.fill3DRect(0, 54, 185, 85, true);
		
		g.setFont(new Font ("Verdana", Font.BOLD, 15));
		g.setColor(new Color(0, 95, 0));
        g.drawString("JJ's Beer Buyer", 5, 68);

        g.setFont(new Font ("Verdana", Font.PLAIN, 9));
        g.setColor(new Color(240, 135, 0));
        g.drawString("Running for: " + Timing.msToString(System.currentTimeMillis() - startTime), 5, 80);
        g.drawString("Status: " + status, 5, 90);
        g.drawString("Beers collected: " + beers, 5, 100);
        g.drawString("Beers per hour: " + toHour(beers), 5, 110);
        g.drawString("Profit made: " + (beers*profit) + " gp", 5, 120);
        g.drawString("Profit per hour: " + toHour(beers*profit) + " gp", 5, 130);
    }
	
	private String beerChoice(){
		if (choice.contains("1"))
			return "asgarnian ales";
		else if (choice.contains("2"))
			return "wizard mind bombs";
		else if (choice.contains("3"))
			return "dwarven stouts";
		
		return "beers";
	}
	
	private String setBeer(){
		if (choice.contains("0")){
			int asgarnian = 1905,
				wizard = 1907,
				stout = 1913;
			
			try {
				int aPrice = getPrice(asgarnian),
					wPrice = getPrice(wizard),
					sPrice = getPrice(stout);
				int best = -1,
					highest = 0;
				
				int[] prices = {aPrice, wPrice, sPrice};
				
				for (int i = 0; i < prices.length; i++){
					if (prices[i] >= highest){
						highest = prices[i];
						best = i;
					}
				}
				
				if (best != -1){
					switch(best){
						case 0: profit = aPrice - 3;
								return "1";
						case 1: profit = wPrice - 3;
								return "2";
						case 2: profit = sPrice - 3;
								return "3";
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			return choice;
		}
		
		return "2";
	}
	
	// Gets the GE price, credit to random people on the internet for the code idea
	public int getPrice(int id) throws IOException { 
        String price;
        URL url = new URL("http://open.tip.it/json/ge_single_item?item=" + id);
        URLConnection con = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            if (line.contains("mark_price")){
        		int start = line.indexOf("mark_price") + 13;
        		int end = line.indexOf("daily_gp") - 3;
        	    price = line.substring(start, end);
        	    price = price.replace(",", "");
   
        	    try {
        	    	return Integer.parseInt(price);
	            } catch (NumberFormatException e) {
	                return -1;
	            }
            }
        }
        return -1;
	}
	
	// Converts to xp per hour
	private int toHour(double detail){
		long difference = System.currentTimeMillis() - startTime;
		double xpH = (3600000 * detail) / difference;

		return (int)xpH;
	}

	// Generates a random number including negative.
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	// Returns the nearest point of the ID
	public Point getNearestPoint(long id){
		Point MSc = new Point(259, 220);
        ScreenModel[] find = ScreenModels.find(id);
        ArrayList<Integer> distList = new ArrayList<Integer>();
        ArrayList<Point> coordList = new ArrayList<Point>();
        
        if (find.length > 0){
	        for (int i = 0; i < find.length; i++){
                distList.add((int) Math.sqrt(Math.pow(Math.abs(MSc.x - find[i].base_point.x), 2) + Math.pow(Math.abs(MSc.y - find[i].base_point.y), 2)));
                coordList.add(new Point(find[i].base_point.getLocation()));
	        }
	        
	        if (!distList.isEmpty() && !coordList.isEmpty()){
	            int nearest = 1000;
	            int t = 0;
	            
	            for (int i = 0; i < distList.size(); i++){
	                if (distList.get(i) < nearest){
	                    nearest = distList.get(i);
	                    t = i;
	                }
	            }
	
	            return coordList.get(t);
	        }
        }
        
        return null;
    }
	
	// Checks if the player is moving
	public boolean isMoving(){
		Point MMc = new Point(627, 135);
		int pixelShift = Screen.getPixelShift(MMc.x - 40, MMc.y - 40, MMc.x + 40, MMc.y + 40, 250);
		//println("Pixelshift: " + pixelShift);
		if (pixelShift > 150){
			return true;
		}
		
		return false;
	}
	
	// Waits until we are not moving
	private void waitUntilNotMoving(){
    	sleep(600, 800);
		while (isMoving()){
			sleep(200, 400);
		}
    }
	
	// BANKING
	private boolean waitBoxScreen(long time){
		long maxTime = System.currentTimeMillis() + time;
		while (System.currentTimeMillis() < maxTime){
			ColourPoint[] counts = Screen.findColours(new Color(60, 50, 45), 94, 108, 439, 313, new Tolerance(20));
			if (counts.length > 2000){
				return true;
			}
		}
		
		return false;
	}
	
	private boolean depositAll(){
		ScreenModel[] box = ScreenModels.find(1518040783L);
		if (box.length > 0){
			Point P = new Point(box[0].base_point.x + randomRange(-3, 3), box[0].base_point.y + randomRange(-3, 3));
			Mouse.move(P);
			if (Timing.waitUptext("deposit box", 1000)){
				Mouse.click(1);
				if (waitBoxScreen(5000)){
					Point BP = new Point(298 + randomRange(0, 25), 320 + randomRange(0, 16));
					Mouse.move(BP);
					Mouse.click(1);
					return true;
				}
			}else{
				attempt++;
				if (attempt < maxAttempt){
					if (depositAll()){
						attempt = 0;
					}
				}
			}
		}
		
		return false;
	}
	
	// WALKING
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
                Mouse.move(P);
                Mouse.click(1);
                waitUntilNotMoving();
                if ((System.currentTimeMillis() - A) < B)
                    return true;
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
                Mouse.move(bestPoint);
                Mouse.click(1);
                waitUntilNotMoving();
                       
               
                Point coord = TPS.MMToPos(bestPoint);
                int dist = Math.abs(coord.x - pos.x) + Math.abs(coord.y - pos.y);
                if ((dist < 5) && (System.currentTimeMillis() - A) < B){
                	return true;
                }
            }
        }

        return false;
    }

	// to the bank
	private boolean toBank(){
		if (blindWalkTo(new Point(randomRange(3722, 3730), randomRange(5336, 5342)), 15000)){
			return true;
		}
		return false;
	}
	
	// to the beers
	private boolean toBeers(){
		if (blindWalkTo(new Point(randomRange(3769, 3773), randomRange(5329, 5338)), 15000)){
			return true;
		}
		return false;
	}
	
	// BEER BUYING
	private boolean talkToSeller(){
		long ID = 2955713843L;

		Point P = getNearestPoint(ID);
		if (P != null){
			Point BP = new Point(P.x + randomRange(-5, 5), P.y - randomRange(5, 15));
			Mouse.move(BP);
			if (Timing.waitUptext("Emily", 200) || Timing.waitUptext("Kaylee", 200)){
				Mouse.click(1);
				waitUntilNotMoving();
				return true;
			}else{
				attempt++;
				if (attempt < maxAttempt){
					if (talkToSeller()){
						attempt = 0;
					}
				}
			}
		}

		return false;
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
	
	private boolean inChat(){
		String[] options = NPCChat.getOptions();
		for (int i = 0; i < options.length; i++){
			//println(options[i]);
			if (options[i].contains("What")){
				//println("In chat");
				return true;
			}
		}
		return false;
	}
	
	private boolean talkingToKaylee(){
		String name = NPCChat.getName();
		if (name.contains("Kaylee")){
			return true;
		}
		return false;
	}
	
	private boolean waitChatOption(String option, long time){
		long maxTime = System.currentTimeMillis() + time;
	
		while(System.currentTimeMillis() < maxTime){
			String[] options = NPCChat.getOptions();
			if (options.length > 0){
				for (int i = 0; i < options.length; i++){
					if (options[i].contains(option)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void space(){
		char space = (char) KeyEvent.VK_SPACE;
		Keyboard.pressKey(space);
		sleep(200, 400);
	}
	
	private boolean waitContinue(long time){
		long maxTime = System.currentTimeMillis() + time;
		
		while(System.currentTimeMillis() < maxTime){
			ColourPoint[] pts = Screen.findColours(new Color(40, 75, 40), 227, 513, 293, 522, new Tolerance(40));
			if (pts.length > 200){
				//println("Found continue");
				return true;
			}
		}
		
		return false;
	}
	
	private void buyBeers(){
		space();
		
		long maxTime = System.currentTimeMillis() + 1500;
		
		while (!inChat() && System.currentTimeMillis() < maxTime){
			sleep(25);
		}

		if (talkingToKaylee()){
			//println("Talking to Kaylee");
			space();
			if (waitChatOption("dwarves", 1000)){
				Keyboard.typeString("1");
				if (waitContinue(1000)){
					space();
					if (waitChatOption("Asgarnian", 1000)){
						space();
						if (waitChatOption("feel", 1000)){
							Keyboard.typeString(choice);
							if (waitContinue(1000)){
								space();
								beers++;
							}
						}
					}
				}
			}
		}else{
			//println("Talking to Emily");
			if (waitChatOption("ales are", 1000)){
				Keyboard.typeString("1");
				if (waitContinue(1000)){
					space();
					if (waitChatOption("Asgarnian", 1000)){
						space();
						if (waitChatOption("dwarves", 1000)){
							Keyboard.typeString(choice);
							if (waitContinue(1000)){
								space();
								beers++;
							}
						}
					}
				}
			}
		}
	}
	
	@Override
    public void run() {
		// Setting up the paint etc.
		Mouse.setSpeed(120);
		choice = setBeer();

		// MAINLOOP
		while(attempt < maxAttempt){
			// Banking
			if (fullInv()){
				status = "walking to bank";
				toBank();
				status = "depositing beers";
				if (depositAll()){
					// Beer buying
					status = "walking to pub";
					toBeers();
					while (!fullInv() && talkToSeller()){
						status = "buying " + beerChoice();
						buyBeers();
					}
				}
			}else{
				// Beer buying
				status = "walking to pub";
				toBeers();
				while (!fullInv() && talkToSeller()){
					status = "buying " + beerChoice();
					buyBeers();
				}
			}
		}
    }
}