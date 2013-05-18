package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.tribot.api.Player;
import org.tribot.api.TPS;
import org.tribot.api.input.Mouse;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Walking", name = "Blindwalk")
public class WalkingTest extends Script implements Painting{
	public void onPaint(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Position: " + getMyPos(), 5, 15);
    }
	
	private Point getMyPos(){
		return TPS.getPosition();
	}
	
	
	private boolean blindWalkTo(Point pos, int maxTime) {
		long A = System.currentTimeMillis();
		long B = A + maxTime;
		Point MMc = new Point(627, 135);
		int radius = 75;

		while ( Math.abs(TPS.getPosition().x - pos.x + TPS.getPosition().y - pos.y) > 10 && (System.currentTimeMillis() - A) < B ) {
			Point myPos = TPS.getPosition();
			int xDist = pos.x - myPos.x;
			int yDist = pos.y - myPos.y;
			int absX = Math.abs(xDist);
			int absY = Math.abs(yDist);

			if (absX <= 50 && absY <= 50) {
				Point P = TPS.posToMM(pos);
				Mouse.move(P);
				Mouse.click(1);
				while (Player.isMoving())
					sleep(50, 150);
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
				sleep(100, 200);
				while (Player.isMoving()){
					sleep(50, 150);
				}
					
				
				Point coord = TPS.MMToPos(bestPoint);
				int dist = Math.abs(coord.x - pos.x) + Math.abs(coord.y - pos.y);
				if ((dist < 10) && (System.currentTimeMillis() - A) < B)
					return true;
			}
		}

		return false;
	}
	
	
	@Override
    public void run() {
    	//TPS.walkTo(new Point(5365, 6255));
		//blindWalkTo(new Point(5020, 6135)); // BANK
		if (blindWalkTo(new Point(5150, 5640), 60000)) // MINE
			println("Reached destination within time limit");
    }
}