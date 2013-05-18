import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.veloxbot.api.ActiveScript;
import org.veloxbot.api.Manifest;
import org.veloxbot.api.internals.Context;

@Manifest(authors = {"J J"}, version = 1.0, name = "JJ's Planker", description = "Makes planks at the Varrock Sawmill")
public class JJsPlanker extends ActiveScript {
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
		//VPS.blindWalkTo(new Point(276, 123), 30000);
		//VPS.blindWalkTo(new Point(73, 401), 30000);
		//sleep(5000);
		
		System.out.println("Script started");
		//if(Context.script != null){
			if(walkToSawmill()){
				status = "Waiting 5 secs...";
				sleep(5000);
				if(walkToBank()){
					return 1;
				}
			}
		//}
		System.out.println("Script ended");
		return -1;
	}
}