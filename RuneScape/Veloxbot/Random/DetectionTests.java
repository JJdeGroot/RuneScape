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

@Manifest(authors = {"J J"}, version = 1.0, name = "Detection Test", description = "Some tests")
public class DetectionTests extends ActiveScript {
	final Color bankerColor = new Color(55, 48, 72);
	final int bankerTolerance = 10;
	
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
	
	
	
	@Override
    public void onRepaint(final Graphics g) {
		g.drawString("Paint", 5, 130);
		final Point[] bankerPoints = Game.getPointsWithColors(Game.VIEWPORT, 10, new Color(51, 45, 68));
		for(int i = 0; i < bankerPoints.length; i++){
			g.drawLine(bankerPoints[i].x, bankerPoints[i].y, bankerPoints[i].x, bankerPoints[i].y);
		}
	}

	@Override
	public long loop() {
		System.out.println("Start");
		openBank();
		
		System.out.println("End");
		return -1;
	}
}