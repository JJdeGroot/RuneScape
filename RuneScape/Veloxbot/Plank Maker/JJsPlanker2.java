import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.veloxbot.api.ActiveScript;
import org.veloxbot.api.Manifest;
import org.veloxbot.api.methods.Bank;
import org.veloxbot.api.methods.Game;
import org.veloxbot.api.methods.Menu;
import org.veloxbot.api.methods.Mouse;
import org.veloxbot.api.methods.OCR;

@Manifest(authors = {"J J"}, version = 1.1, name = "JJ's Planker", description = "Makes planks at the Varrock Sawmill")
public class JJsPlanker2 extends ActiveScript {
	// VARIABLES
	final Point bankLocation = new Point(73, 403),
		        sawmillLocation = new Point(275, 118);
	final Point[] pathToSawmill = {new Point(74, 370), new Point(108, 372),
						  new Point(136, 365), new Point(162, 364),
						  new Point(191, 367), new Point(204, 335),
						  new Point(197, 309), new Point(208, 279),
						  new Point(208, 254), new Point(239, 235),
						  new Point(243, 198), new Point(258, 170),
						  new Point(261, 145), new Point(276, 119)},
				 pathToBank = reversePath(pathToSawmill);
	final Color bankerColor = new Color(55, 48, 72);
	final int bankerTolerance = 10;
	
	String status = "Starting up";

	// METHODS	
	private Point[] reversePath(Point[] path){
		Point[] reversedPath = new Point[path.length];
        for(int i = 0; i < reversedPath.length; i++) {
            reversedPath[i] = path[path.length-i-1];
         }
        return reversedPath;
	}
	
	
	private boolean walkToSawmill(){
		status = "Walking to the sawmill";
		return VPS.walkPath(pathToSawmill);
	}
	
	private boolean walkToBank(){
		status = "Walking to the bank";
		return VPS.walkPath(pathToBank);
    }
	
	private double getDistance(final Point p1, final Point p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) + Math.pow(Math.abs(p1.y - p2.y), 2));
    }
	
	private Point getBanker(){
		final Point[] bankerPoints = Game.getPointsWithColors(Game.VIEWPORT, bankerTolerance, bankerColor);

		if(bankerPoints.length > 0){
			int highestMatches = 0;
			Point P = null;
			
			ArrayList<Point> points = new ArrayList<Point>();
			for(int i = 0; i < bankerPoints.length; i++){
				points.add(bankerPoints[i]);
			}

			for(int i = 0; i < points.size(); i++){
				Point q = points.get(i);
				int totalX = q.x,
				    totalY = q.y,
				    matches = 1;

				for(int j = 0; j < points.size(); j++){
					if(getDistance(q, points.get(j)) <= 15){
						totalX = totalX + points.get(j).x;
						totalY = totalY + points.get(j).y;
						
						matches++;
						points.remove(j);
					}
				}
				
				if(matches > highestMatches){
					highestMatches = matches;
					P = new Point(totalX/matches, totalY/matches);
				}
			}
			
			if(highestMatches >= 30){
				return P;
			}
		}

		return null;
	}
	
	private boolean waitUptext(String uptext, int time){
		long maxTime = System.currentTimeMillis() + time;
		while(System.currentTimeMillis() < maxTime){
			if(OCR.uptextContains(uptext)){
				return true;
			}
			sleep(time/100, time/50);
		}
		return false;
	}
	
	private boolean waitOption(String option, int time){
		long maxTime = System.currentTimeMillis() + time;
		while(System.currentTimeMillis() < maxTime){
			if(Menu.interact(option, true)){
				return true;
			}
			sleep(time/100, time/50);
		}
		return false;
	}
	
	private boolean waitBankScreen(int time){
		long maxTime = System.currentTimeMillis() + time;
		while(System.currentTimeMillis() < maxTime){
			if(Bank.isOpen()){
				return true;
			}
			sleep(time/100, time/50);
		}
		return false;
	}

	private boolean openBank(){
		Point P = getBanker();
		
		if(P != null){
			Mouse.move(P);
			if(waitUptext("Banker", 500)){
				Mouse.click(false);
				if(waitOption("Bank Banker", 500)){
					if(waitBankScreen(2000)){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	// ON START
	public boolean onStart(){
		VPS.setMapData(VPS.getOnlineMapData("http://i47.tinypic.com/2wr2j5v.png"));
		return true;
	}
	
	// PAINTING
	@Override
    public void onRepaint(final Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("JJ's Planker", 350, 75);
		g.drawString("Status: " + status, 350, 90);
		Point myPos = VPS.getPosition();
		g.drawString("Location: (" + myPos.x + ", " + myPos.y + ")", 350, 105);
		g.drawImage(VPS.getRotatedMiniMap(), 150, 150, null);
	}

	// MAINLOOP
	@Override
	public long loop() {
		System.out.println("Script started");
		if(walkToSawmill()){
			status = "Waiting 5 secs...";
			sleep(5000);
			if(walkToBank()){
				openBank();
				return 1;
			}
		}
		System.out.println("Script ended");
		return -1;
	}
}