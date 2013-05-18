import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.veloxbot.api.ActiveScript;
import org.veloxbot.api.Manifest;

@Manifest(authors = {"J J"}, version = 1.0, name = "JJ's Planker", description = "Makes planks at the Varrock Sawmill")
public class WalkingTest extends ActiveScript {
	// VARIABLES
	VPS4 walk;
	final Point bankLocation = new Point(73, 403),
		        sawmillLocation = new Point(275, 118);
	final Point[] path = {new Point(74, 370), new Point(108, 372),
						  new Point(136, 365), new Point(162, 364),
						  new Point(191, 367), new Point(204, 335),
						  new Point(197, 309), new Point(208, 279),
						  new Point(208, 254), new Point(239, 235),
						  new Point(243, 198), new Point(258, 170),
						  new Point(261, 145), new Point(276, 119)};
	
	String status = "Starting up";

	// METHODS	
	private boolean walkToSawmill(){
		status = "Walking to the sawmill";
		return walk.walkPath(path);
	}
	
	private boolean walkToBank(){
		status = "Walking to the bank";

		Point[] path = new Point[this.path.length];
		for(int i = this.path.length-1; i >= 0; i--){
			path[i-this.path.length-1] = this.path[i];
		}
		
		return walk.walkPath(path);
	}
	
	// ON START
	public boolean onStart(){
		walk = new VPS4();
		walk.setMapData(walk.getOnlineMapData("http://i47.tinypic.com/2wr2j5v.png"));
		return true;
	}
	
	// PAINTING
	@Override
    public void onRepaint(final Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("JJ's Planker", 350, 75);
		g.drawString("Status: " + status, 350, 90);
	}

	// MAINLOOP
	@Override
	public long loop() {
		System.out.println("Script started");
		if(walkToSawmill()){
			status = "Waiting...";
			sleep(5000);
			if(walkToBank()){
				return -1;
			}
		}
		System.out.println("Script ended");
		return -1;
	}
}