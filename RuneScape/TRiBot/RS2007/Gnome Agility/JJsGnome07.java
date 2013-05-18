package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.TileObserver;
import java.util.ArrayList;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.api2007.Game;
import org.tribot.api2007.Player;
import org.tribot.api2007.Screen;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Agility", name = "JJ's Gnome Agility 07")
public class JJsGnome07 extends Script implements Painting {

	private final Color cyanColor = new Color(15, 213, 203);
	private final Tolerance cyanTol = new Tolerance(25);
	private final Rectangle uptextArea = new Rectangle(8, 8, 200, 12);
	private final Point msc = new Point(258, 169);
		
	@Override
	public void onPaint(Graphics g) {
		g.setColor(new Color(60, 60, 60));
		g.fillRect(4, 4, 275, 100);
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's Gnome Agility", 10, 20);
		g.drawString("My position: " + Player.getPosition(), 10, 40);
		g.drawString("Animation ID: " + Player.getAnimation(), 10, 60);
		g.drawString("Uptext: " + Game.getUptext(), 10, 80);		
		
		Obstacle ob = getNearestObstacle();
		g.drawString("Nearest obstacle: " + ob, 10, 100);
		g.setColor(Color.RED);
		Point[] pts = ob.getPoints();
		for(Point p : pts){
			g.drawLine(p.x, p.y, p.x, p.y);
		}
	}
	
	private enum Obstacle {
		BALANCING_LOG(new RSTile(2474, 3436, 0), "Walk-across Log balance", new Color(102, 60, 9), 4),
		OBSTACLE_NET_UP(new RSTile(2474, 3426, 0), "Climb-over Obstacle net", new Color(130, 106, 88), 15),
		BRANCH_UP(new RSTile(2473, 3423, 0), "Climb Tree branch", new Color(131, 107, 60), 6), 
		BALANCING_ROPE(new RSTile(2477, 3420, 0), "Walk-on Balancing rope", new Color(161, 131, 92), 14),
		BRANCH_DOWN(new RSTile(2485, 3419, 0), "Climb-down Tree branch", new Color(131, 107, 60), 6), 
		OBSTACLE_NET_OVER(new RSTile(2487, 3425, 0), "Climb-over Obstacle net", new Color(137, 118, 93), 14),
		PIPE(new RSTile(2487, 3430, 0), "Squeeze-through Obstacle pipe", new Color(113, 106, 104), 21);
		
		private final Rectangle gamescreen = new Rectangle(4, 4, 511, 333);
	
		private final RSTile location;
		private final String uptext;
		private final Color color;
		private final int tolerance;
		
		private Obstacle(RSTile location, String uptext, Color color, int tolerance){
			this.location = location;
			this.uptext = uptext;
			this.color = color;
			this.tolerance = tolerance;
		}
		
		public RSTile getLocation(){
			return location;
		}
		
		public String getUptext(){
			return uptext;
		}
		
		public Point[] getPoints(){
			ArrayList<Point> matches = new ArrayList<Point>();
			BufferedImage img = Screen.getGameImage();
			Tolerance tol = new Tolerance(tolerance);
		
			for(int x = gamescreen.x; x < gamescreen.x+gamescreen.width; x++){
				for(int y = gamescreen.y; y < gamescreen.y+gamescreen.height; y++){
					Color c = new Color(img.getRGB(x, y));
					if(org.tribot.api.Screen.coloursMatch(color, c, tol)){
						matches.add(new Point(x, y));
					}
				}
			}
			
			return matches.toArray(new Point[matches.size()]);
		}
	}
	
	private Obstacle getNearestObstacle(){
		Obstacle[] obstacles = Obstacle.values();
		
		RSTile myPos = Player.getPosition();
		double nearest = myPos.distanceToDouble(obstacles[0].getLocation());
		int index = 0;
		
		for(int i = 1; i < obstacles.length; i++){
			double distance = obstacles[i].getLocation().distanceToDouble(myPos);
			if(distance < nearest){
				nearest = distance;
				index = i;
			}
		}
		
		return obstacles[index];
	}
	
	private Point[] getCyanUptextPoints(){
		ArrayList<Point> matches = new ArrayList<Point>();
		BufferedImage img = Screen.getGameImage();
		
		for(int x = uptextArea.x; x < uptextArea.x+uptextArea.width; x++){
			for(int y = uptextArea.y; y < uptextArea.y+uptextArea.height; y++){
				Color c = new Color(img.getRGB(x, y));
				if(org.tribot.api.Screen.coloursMatch(cyanColor, c, cyanTol)){
					matches.add(new Point(x, y));
				}
			}
		}
		
		return matches.toArray(new Point[matches.size()]);
	}

	private boolean waitUptext(String text, long time){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < time){
			String uptext = Game.getUptext();
			if(uptext != null){
				if(uptext.contains(text)){
					println("Found uptext: " + text);
					return true;
				}
			}
			sleep(10, 20);
		}
		
		return false;
	}

	private boolean interact(Obstacle obstacle){
		ArrayList<Point> ptList = new ArrayList<Point>();
		Point[] pts = obstacle.getPoints();
		
		for(Point p : pts){
			double distance = p.distance(msc.x, msc.y);
			if(distance > 25 && distance < 75){
				ptList.add(p);
			}
		}
	
		if(ptList.size() > 0){
			long t = System.currentTimeMillis();
			int attempts = 0;
			while(attempts < 5 && Timing.timeFromMark(t) < 5000){
				int r = General.random(0, ptList.size()-1);
				Point p = ptList.get(r);
				Mouse.move(p);
				if(waitUptext(obstacle.getUptext(), 200)){
					Mouse.click(1);
					return true;
				}
				attempts++;
			}
		}
		
		return false;
	}
	
	/*
	 * LOCATIONS:
	 * Log balance: 2484, 3436, 0
	 * Net up: 2471, 3426, 0 -> 2476, 3426, 0
	 * Branch up: 2473, 3423, 0
	 * Rope: 2477, 3420, 0
	 * Branch down: 2485, 3419, 0
	 * Obstacle net: 2483, 3425, 0 -> 2488, 3425, 0
	 * Pipe left: 2484, 3430, 0
	 * Pipe right: 2487, 3430, 0
	 *  
	 *  
	 * COLORS
	 *
	 * 	 * 
	 * Log balance color: 104, 64, 12 with 9 tol
	 * Net up: 130, 106, 88 with 15 tol
	 * Branch up: 132, 109, 61 with 9 tol
	 * Rope: 161, 131, 92 with 14 tol
	 * Branch down: 131, 107, 60 with 6 tol
	 * Obstacle net: 137, 118, 93 with 14 tol
	 * Pipe: 113, 106, 104 with 21 tol
	 */
	
	

	@Override
	public void run() {
		println("Hello world");
		interact(getNearestObstacle());
		sleep(5000);
		
	}

	
	
	
	
}
