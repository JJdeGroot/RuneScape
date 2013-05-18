package scripts;

import java.awt.Point;

import org.tribot.api.input.Mouse;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Test", name = "mm to ms")
public class MinimapToMainscreen extends Script{
	
	private Point MMtoMS(Point P){
		Point MMc = new Point(627, 135), MSc = new Point(259, 220);
		
		double xDist = (MMc.x - P.x);
		double yDist = (MMc.y - P.y);
				
		//North: 	9,68x ^ 0,896
		//South	13,52x^0,878
		//East/west	10,8x + 4,7
		
		if (xDist > 0){
			//West
			P.x = (int)(MSc.x - ((10.8 * xDist) + 4.7));
		}else{
			//East
			P.x = (int)(MSc.x + ((10.8 * xDist) + 4.7));
		}
		
		if (yDist > 0){
			// North
			P.y = MSc.y - (int) Math.pow(9.68 * yDist, 0.896);
		}else{
			// South
			P.y = MSc.y + (int) Math.pow(13.52 * yDist, 0.878);
		}
		
		return P;
	}
	
	@Override
    public void run() {
    	println("test");
    	Mouse.move(MMtoMS(new Point(610, 124)));
    	
    }
}