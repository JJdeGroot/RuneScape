package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Random;

import org.tribot.api.Constants;
import org.tribot.api.Game;
import org.tribot.api.GameTab;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Textures;
import org.tribot.api.Timing;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.Texture;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Minigames", name = "JJ's Mage Training Arena")
public class JJsMTA2 extends Script implements Painting{
	// Declaring variables
	final long startTime = System.currentTimeMillis(),
			   mazeWallID = 244323339L,
			   telegrabAnimationID = 1277201166L,
			   statueID = 320196375L;
	final int telegrabID = 57178,
			  clickedTeleGrabID = 12242;
	
	String status = "starting up";	
	Point a = new Point(0, 0),
		  b = new Point(0, 0),
		  c = new Point(0, 0),
		  d = new Point(0, 0);

	// Draws the on-screen paint
	public void onPaint(Graphics g){
        g.setColor(new Color(31, 31, 31));
		g.fill3DRect(0, 54, 195, 45, true);
		
		g.setFont(new Font ("Verdana", Font.BOLD, 15));
		g.setColor(new Color(0, 95, 0));
        g.drawString("JJ's Mage Train Arena", 5, 68);

        g.setFont(new Font ("Verdana", Font.PLAIN, 9));
        g.setColor(new Color(240, 135, 0));
        g.drawString("Running for: " + Timing.msToString(System.currentTimeMillis() - startTime), 5, 80);
        g.drawString("Status: " + status, 5, 90);

        g.setColor(new Color(255, 50, 50));
		g.drawLine(a.x, a.y, c.x, c.y);
		g.drawLine(b.x, b.y, d.x, d.y);
		drawTrapezium(new Trapezium(a, b, c, d), g);
    }
	
	// Defines the trapezium type
	public class Trapezium{
		private Point nw, ne, se, sw;
		
		public Trapezium(Point northwest, Point northeast, Point southeast, Point southwest)
		{
			nw = northwest;
			ne = northeast;
			se = southeast;
			sw = southwest;
		}
	}

	// Draws the maze trapezium on screen
	private void drawTrapezium(Trapezium t, Graphics g){
		Point nw = t.nw,
			  ne = t.ne,
			  se = t.se,
			  sw = t.sw;

		//nw -> ne
		g.drawLine(nw.x, nw.y, ne.x, ne.y);

		//ne -> se
		g.drawLine(ne.x, ne.y, se.x, se.y);
		
		//sw -> se
		g.drawLine(sw.x, sw.y, se.x, se.y);
		
		//nw -> sw
		g.drawLine(nw.x, nw.y, sw.x, sw.y);
	}
	
	// Defines the maze where we are located
	private int defineMaze(){
		ScreenModel[] count = ScreenModels.find(mazeWallID);
		switch(count.length){
			case 49: return 1;
			case 59: return 6;
			case 62: return 4;
			case 74: return 7;
			case 86: return 2;
			case 87: return 9;
			case 92: return 3;
			case 101: return 10;
			case 111: return 5;
			case 116: return 8;
		}
		return -1;
	}

	// The directions
	public enum Direction{
		 NORTH, EAST, SOUTH, WEST;
	}
	
	// Returns the solution of the maze in Direction[] format
	private Direction[] getMazeSolution(){
		int mazeNumber = defineMaze();
		Direction[] solution = {};

		if (mazeNumber > 0){
			status = "solving maze #" + mazeNumber;
			println("We are at maze number #" + mazeNumber);
			//println("Solution of the maze: ");
			switch(mazeNumber){
				case 1: solution = new Direction[] {Direction.NORTH, 
													Direction.WEST, 
													Direction.SOUTH, 
													Direction.EAST, 
													Direction.NORTH, 
													Direction.WEST, 
													Direction.SOUTH};
								   break;
				case 2: solution = new Direction[] {Direction.SOUTH,
												    Direction.WEST,
												    Direction.NORTH,
												    Direction.WEST,
												    Direction.SOUTH,
												    Direction.WEST,
												    Direction.NORTH};
								   break;
				case 3: solution = new Direction[] {Direction.NORTH,
													Direction.EAST,
													Direction.SOUTH,
													Direction.WEST,
													Direction.SOUTH,
													Direction.WEST,
													Direction.NORTH};
								   break;
				case 4: solution = new Direction[] {Direction.WEST,
													Direction.SOUTH,
													Direction.EAST,
													Direction.NORTH,
													Direction.WEST,
													Direction.SOUTH,
													Direction.EAST,
													Direction.NORTH};
								   break;
				case 5: solution = new Direction[] {Direction.WEST,
													Direction.NORTH,
													Direction.EAST,
													Direction.NORTH,
													Direction.WEST,
													Direction.NORTH,
													Direction.WEST,
													Direction.NORTH};
								   break;
				case 6: solution = new Direction[] {Direction.SOUTH,
													Direction.WEST,
													Direction.SOUTH,
													Direction.EAST,
													Direction.SOUTH,
													Direction.WEST,
													Direction.SOUTH,
													Direction.EAST};
								   break;
				case 7: solution = new Direction[] {Direction.WEST,
													Direction.NORTH,
													Direction.WEST,
													Direction.SOUTH,
													Direction.WEST,
													Direction.SOUTH,
													Direction.EAST,
													Direction.NORTH,
													Direction.EAST};
								   break;
				case 8: solution = new Direction[] {Direction.EAST,
													Direction.NORTH,
													Direction.WEST,
													Direction.NORTH,
													Direction.EAST,
													Direction.NORTH,
													Direction.WEST,
													Direction.NORTH};
								   break;
				case 9: solution = new Direction[] {Direction.SOUTH,
													Direction.WEST,
													Direction.NORTH,
													Direction.WEST,
													Direction.NORTH,
													Direction.EAST,
													Direction.SOUTH,
													Direction.WEST,
													Direction.SOUTH,
													Direction.EAST};
								    break;
				case 10: solution = new Direction[] {Direction.SOUTH,
													 Direction.EAST,
													 Direction.SOUTH,
													 Direction.WEST,
													 Direction.SOUTH,
													 Direction.EAST,
													 Direction.NORTH,
													 Direction.EAST,
													 Direction.SOUTH,
													 Direction.WEST};
									break;
			}

			/*
			if (solution.length > 0){
				for (int i = 0; i < solution.length; i++){
					println(solution[i]);
				}
			}
			*/
		}
		
		return solution;
	}
	
	// Calculates the outsides of the maze trapezium ingame
	private Trapezium getOutsides(){
		Point MSc = new Point(Constants.MSCX, Constants.MSCY);
		int n = MSc.y, 
			s = MSc.y;
	
		ScreenModel[] walls = ScreenModels.find(mazeWallID);
		for (int i = 0; i < walls.length; i++){
			Point P = new Point(walls[i].points[0]);
			if (P.y < n)
				n = P.y;
			
			if (P.y > s)
				s = P.y;
		}
		
		Point NW = MSc,
			  NE = MSc,	
			  SE = MSc,
			  SW = MSc;

		for (int i = 0; i < walls.length; i++){
			Point P = new Point(walls[i].points[0]);
			if (Math.abs(P.y - n) < 10){
				if (P.x < NW.x)
					NW = P;
				else if (P.x > NE.x)
					NE = P;
			}
			
			if (Math.abs(P.y - s) < 10){
				if (P.x > SE.x)
					SE = P;
				else if (P.x < SW.x)
					SW = P;
			}
		}

		//println("NW: " + NW + " NE: " + NE + " SE : " + SE + " SW: " + SW);
		a = NW;
		b = NE;
		c = SE;
		d = SW;
		return new Trapezium(a, b, c, d);
	}
	
	// Returns a random number between aFrom and aTo
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }

	// Calculates a random point in a trapezium with the desired direction
	private Point calculateRandomPosition(Trapezium t, Direction d)	{
		double a = 0;
		double b = 0;
		int r = 0,
			x = 0,
			y = 0;

		switch(d){
			case NORTH: println("North");
						a = (t.ne.y-t.nw.y)/(t.ne.x-t.nw.x);
						b = t.ne.y - (a*t.ne.x);
						r = randomRange(t.nw.x, t.ne.x);
						y = randomRange(-2, 8);
						break;
			case EAST: println("East");
					   a = (t.se.y-t.ne.y)/(t.se.x-t.ne.x);
					   b = t.ne.y - (a*t.ne.x);
					   r = randomRange(t.ne.x+2, t.se.x);
					   x = randomRange(0, 10);
					   break;
			case SOUTH: println("South");
					    a = (t.se.y-t.sw.y) / (t.se.x - t.sw.x);
					    b = t.se.y - (a*t.se.x);
					    r = randomRange(t.sw.x, t.se.x);
					    y = randomRange(5, 15);
					    break;
			case WEST: println("West");
					   a = (t.sw.y-t.nw.y)/(t.sw.x-t.nw.x);
					   b = t.nw.y - (a*t.nw.x);
					   r = randomRange(t.sw.x, t.nw.x-2);
					   x = randomRange(-10, 0);
					   break;
		}

		//println("a: " + a + ", b: " + b + " r: " + r);
		Point P = new Point(r, (int)(a*r+b));
		return new Point(P.x + x, P.y + y);
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
	
	// Selects telegrab in the magic spellbook
	public boolean selectTelegrab(){
		if (!Game.isUptext("Cast Telekinetic Grab")){
			FTab(TABS.MAGIC);
			Texture[] grab = Textures.find(telegrabID);
			if (grab.length > 0){
				Point P = new Point(grab[0].x + randomRange(0, 15), grab[0].y + randomRange(0, 15));
				Mouse.move(P);
				if (Timing.waitUptext("Telekinetic Grab", 500)){
					Mouse.click(1);
					return true;
				}
			}
		}else{
			return true;
		}
		return false;
	}
	
	// Returns a boolean if the player is moving
	public boolean isMoving(){
		Point MMc = new Point(Constants.MMCX, Constants.MMCY);
		int pixelShift = Screen.getPixelShift(MMc.x - 70, MMc.y - 70, MMc.x + 70, MMc.y + 70, 250);
		//println("Pixelshift: " + pixelShift);
		if (pixelShift > 100){
			return true;
		}
		return false;
	}
	
	// Waits until the player is not moving, random chance of selecting telegrab
	public void waitUntilNotMoving(){
		sleep(1000, 1500);
		while (isMoving()){
			switch(randomRange(1, 5)){
				case 5: selectTelegrab();
			}
			sleep(50, 100);
		}
	}
	
	// Tracks the statue to know when we can make our next move
	public void trackStatue(){
		Point oldPos = new Point(Constants.MSCX, Constants.MSCY);
		int matches = 0;
		
		sleep(2000, 3000);
		while(ScreenModels.find(telegrabAnimationID).length > 0){
			//println("Telegrab visible");
			sleep(250, 500);
		}
		
		long maxTime = System.currentTimeMillis() + 10000;
		while(System.currentTimeMillis() < maxTime){
			ScreenModel[] statue = ScreenModels.find(statueID);
			if (statue.length > 0){
				Point newPos = statue[0].base_point;
				double dif = Math.abs(newPos.x-oldPos.x) + Math.abs(newPos.y-oldPos.y);
				if (dif < 5){
					matches++;
				}else{
					matches = 0;
				}
				oldPos = newPos;
				if (matches > 10){
					break;
				}
			}
			sleep(100, 200);
		}
	}
	
	// Clicks on the statue
	public boolean clickStatue(){
		ScreenModel[] statue = ScreenModels.find(statueID);
		if (statue.length > 0){
			Point P = new Point(statue[0].base_point);
			Mouse.move(P);
			if (Timing.waitUptext("Guardian", 500)){
				Mouse.click(1);
			}
		}
		return false;
	}
	
	// Solves the whole maze
	private boolean solveMaze(){
		Direction[] solution = getMazeSolution();
		if (solution.length > 0){
			getOutsides();
			for (int i = 0; i < solution.length; i++){
				Point P = calculateRandomPosition(new Trapezium(a, b, c, d), solution[i]);
				Mouse.move(P);
				Mouse.click(1);
				waitUntilNotMoving();
				selectTelegrab();
				clickStatue();
				trackStatue();
			}
		}
		return false;
	}
	
	
	@Override
    public void run(){
		solveMaze();
		
    }
}