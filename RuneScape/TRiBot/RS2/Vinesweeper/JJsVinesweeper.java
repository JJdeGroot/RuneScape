package scripts;

import java.awt.Color;
import java.awt.Graphics;

import org.tribot.api.ScreenModels;
import org.tribot.api.types.ScreenModel;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Farming", name = "Vinesweeper")
public class JJsVinesweeper extends Script implements Painting{
	final long holeID = 4193989216L, mudID = 3345636330L;
	final long[] holess = {holeID, mudID};
	
	
	public void onPaint(Graphics g){
		ScreenModel[] holes = ScreenModels.find(holess);
		if (holes.length > 0){
			for (int i = 0; i < holes.length; i++){
				g.setColor(Color.WHITE);
				g.drawRect(holes[i].base_point.x, holes[i].base_point.y, 5, 5);
			}
		}
    }
	
	private void findHoles(){
		ScreenModel[] holes = ScreenModels.find(holeID);
		if (holes.length > 0){
			for (int i = 0; i < holes.length; i++){
				//println(i + ": (" + holes[i].base_point.x + ", " + holes[i].base_point.y + ")");
				//sleep(200);
			}
		}
		
					
	}
	
	@Override
    public void run() {
    	findHoles();
    	sleep(10000);
	}
}