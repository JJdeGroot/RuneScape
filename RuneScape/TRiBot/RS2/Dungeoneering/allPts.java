package scripts;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Test", name = "All points")
public class allPts extends Script{
	
	private void allPoints(){
		//ScreenModel[] all = ScreenModels.addCaptureModelPoints(4032083377L);

		long ID = 4390934L;

		Point[][] visible;

		
		
		ScreenModel[] rope = ScreenModels.find(ID);
		if (rope.length > 0){
			visible = new Point[rope.length][];	
			for (int i = 0; i < rope.length; i++){
				visible[i] = rope[i].getVisiblePoints();
				println("Base point " + i);
				Mouse.move(rope[i].base_point);
				sleep(2000);
				if (visible[i].length > 0)
					for (int j = 0; j < visible[i].length; j++){
						println("Visible points @ Rope #" + i + "/" + j + ": " + visible[i][j]); 
						Mouse.move(visible[i][j]);
						sleep(2000);
					}
			}
		}


	}
	

	@Override
	public void run() {
		allPoints();
	}

}
