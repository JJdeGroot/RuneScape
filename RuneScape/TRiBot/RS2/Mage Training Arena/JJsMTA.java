package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.tribot.api.Constants;
import org.tribot.api.ScreenModels;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Minigames", name = "JJ's Mage Training Arena")
public class JJsMTA extends Script implements Painting
{
	final long mazeWallID = 244323339L,
			   telegrabAnimationID = 1277201166L;
	Point a = new Point(0, 0),
		  b = new Point(0, 0),
		  c = new Point(0, 0),
		  d = new Point(0, 0);
	Point[] pa = {new Point(0, 0), new Point(10, 10)};

	public void onPaint(Graphics g)
	{
        g.setColor(Color.YELLOW);
        g.drawString("JJ's MTA", 5, 75);
        g.drawString("Status: ", 5, 90);

        g.setColor(new Color(255, 50, 50));
		g.drawLine(a.x, a.y, c.x, c.y);
		g.drawLine(b.x, b.y, d.x, d.y);
		drawTrapezium(new Trapezium(a, b, c, d), g);
		
		g.setColor(Color.BLUE);
		for (int i = 0; i < pa.length; i++){
			g.drawLine(pa[i].x, pa[i].y, pa[i].x, pa[i].y);
		}
    }
	
	public class Trapezium
	{
		private Point nw, ne, se, sw;
		
		public Trapezium(Point northwest, Point northeast, Point southeast, Point southwest)
		{
			nw = northwest;
			ne = northeast;
			se = southeast;
			sw = southwest;
		}
	}

	private void drawTrapezium(Trapezium t, Graphics g)
	{
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
	
	private int defineMaze()
	{
		ScreenModel[] count = ScreenModels.find(mazeWallID);
		switch(count.length)
		{
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
	
	
	
	public enum Direction
	{
		 NORTH, EAST, SOUTH, WEST;
	}
	
	private boolean solveMaze()
	{
		int mazeNumber = defineMaze();

		if (mazeNumber > 0)
		{
			println("We are at maze number #" + mazeNumber);
			println("Solution of the maze: ");
			Direction[] solution = {};
			switch(mazeNumber)
			{
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
													Direction.SOUTH,
													Direction.EAST,
													Direction.NORTH,
													Direction.WEST,
													Direction.SOUTH,
													Direction.EAST,
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

			if (solution.length > 0)
			{
				for (int i = 0; i < solution.length; i++)
				{
					println(solution[i]);
				}
			}
		}
		
		return false;
		
	}
	
	private Trapezium getOutsides()
	{
		Point MSc = new Point(Constants.MSCX, Constants.MSCY);
		int n = MSc.y, 
			s = MSc.y;
	
		ScreenModel[] walls = ScreenModels.find(mazeWallID);
		for (int i = 0; i < walls.length; i++)
		{
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

		for (int i = 0; i < walls.length; i++)
		{
			Point P = new Point(walls[i].points[0]);
			if (Math.abs(P.y - n) < 10)
			{
				if (P.x < NW.x)
					NW = P;
				else if (P.x > NE.x)
					NE = P;
			}
			
			if (Math.abs(P.y - s) < 10)
			{
				if (P.x > SE.x)
					SE = P;
				else if (P.x < SW.x)
					SW = P;
			}
		}

		println("NW: " + NW + " NE: " + NE + " SE : " + SE + " SW: " + SW);
		a = NW;
		b = NE;
		c = SE;
		d = SW;
		return new Trapezium(a, b, c, d);
	}
	
	private int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }

	private Point calculatePosition(Trapezium t, Direction d){
		println("pos");
		switch(d)
		{
			case NORTH: println("North"); 
						Point P = new Point(randomRange(t.nw.x, t.sw.x), randomRange(t.nw.y, t.sw.y));
						Point PP = new Point(P.x + randomRange(-5, 5), P.y + randomRange(-5, 5));
						return PP;
			case EAST: println("East"); break;
			case SOUTH: println("South"); break;
			case WEST: println("West"); break;
		}
		
		return null;
	}
	
	private void arrayBetweenPoints()
	{
		Point P1 = new Point(10, 10);
		Point P2 = new Point(20, 40);
		ArrayList<Point> pts = new ArrayList<Point>();
		int xDist = Math.abs(P1.x - P2.x);
		int yDist = Math.abs(P1.y - P2.y);
		
		while(!(P2.y == P1.y && P2.x == P1.x)){
			double verhouding = 1;
			
			if (yDist > xDist){
				verhouding = yDist/xDist;
			}else{
				verhouding = xDist/yDist;
			}
			
			println("Verhouding: " + verhouding);
			sleep(1000);
			
			if (P2.x - P1.x != 0){
				if (P2.x > P1.x){
					pts.add(new Point(P2.x, P2.y));
					P2 = new Point(P2.x-1, P2.y);
				}else{
					pts.add(new Point(P1.x, P1.y));
					P1 = new Point(P1.x-1, P1.y);
				}
			}
			
			if (P2.y - P1.y != 0){
				if (P2.y > P1.y){
					pts.add(new Point(P2.x, P2.y));
					P2 = new Point(P2.x, P2.y-1);
				}else{
					pts.add(new Point(P1.x, P1.y));
					P1 = new Point(P1.x, P1.y-1);
				}
			}
		}

		//for (int i = 0; i < pts.size(); i++){
			//println("i : " + i + " & result: " + pts.get(i));
			//sleep(100);
		//}
		
		pa = pts.toArray(new Point[pts.size()]);
	}
	
	@Override
    public void run()
	{
		//solveMaze();
		//getOutsides();
		//Point P = calculatePosition(new Trapezium(a, b, c, d), Direction.NORTH);
		//Mouse.move(P);
		arrayBetweenPoints();
	
		//sleep(5000000);
		//Mouse.move(new Point(373, 302));
		//Mouse.click(1);
		
		/*
		#1: West = 
		 
		 
		 
		*/
    }
}