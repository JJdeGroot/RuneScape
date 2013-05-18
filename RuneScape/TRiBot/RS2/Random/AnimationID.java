package scripts;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JOptionPane;

import org.tribot.api.types.ScreenModel;
import org.tribot.api.ScreenModels;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

	@ScriptManifest(authors = { "J J" }, category = "Test", name = "Fishing tests")
	public class AnimationID extends Script implements Painting {
		
		private long animationID;
		
		public void onPaint(Graphics g) {
            g.setColor(Color.CYAN);
            g.drawString("Animation ID: " + animationID, 5, 100);
            g.setColor(Color.WHITE);
            
        }
		
		private void getAnimationID() {
			ScreenModel[] ids = ScreenModels.getAll();
	        for (int i=0; i<ids.length; i++){
	        	if (ids[i].points[0].x > 255 && ids[i].points[0].x < 275){
	        		if (ids[i].points[0].y > 190 && ids[i].points[0].y < 210){
	        			animationID = ids[i].id;
	        			println("Animation ID: " + animationID);
	        			println("Animation ID location: " + ids[i].points[0]);
	        			break;
	        		}
	        	}
	        }
		}

		@Override
		public void run() {
	        getAnimationID();
	        sleep(5000);
		}
	}
