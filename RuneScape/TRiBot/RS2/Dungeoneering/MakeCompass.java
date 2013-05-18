package scripts;

import java.awt.Point;
import java.awt.event.KeyEvent;

import org.tribot.api.Minimap;
import org.tribot.api.ScreenModels;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Test", name = "Make compass")
public class MakeCompass extends Script{
	
	private boolean makeCompass(double angle){
		char key = KeyEvent.VK_ENTER;
		double difference = angle - Minimap.getRotationAngle();
		println(difference);
		if (Math.abs(difference) > 2){
			if (difference < 0)
				key = (char) KeyEvent.VK_LEFT;
			else
				key = (char) KeyEvent.VK_RIGHT;
	
					Keyboard.pressKey(key);
					
				while (!(Minimap.getRotationAngle() > (angle-2) && Minimap.getRotationAngle() < (angle + 2))){
				}
				
				Keyboard.releaseKey(key);
				println(Minimap.getRotationAngle());
		}

		return false;
	}
	
	private void eastOf(){
		ScreenModel[] smuggler = ScreenModels.find(3543694631L);
		if (smuggler.length > 0){
			Point P = smuggler[0].base_point;
			println("Smuggler location: " + P);
			float angle = Minimap.getRotationAngle();
			println("Rotation angle: " + angle);
			
			double rad = (angle / 360 * 2 * Math.PI);
			println("Rad: " + rad);
			
			
			double x = Math.cos(rad) * 30;
			double y = Math.sin(rad) * 30 * -1;
			
	
			println("X: " + x);
			println("Y: " + y);
			
			Point PP = new Point(P.x + (int)x, P.y + (int)y);
			
			Mouse.move(PP);
		}
	}
	
	private void northOf(){
		ScreenModel[] smuggler = ScreenModels.find(3543694631L);
		if (smuggler.length > 0){
			Point P = smuggler[0].base_point;
			println("Smuggler location: " + P);
			float angle = Minimap.getRotationAngle();
			println("Rotation angle: " + angle);
			
			double rad = (angle / 360 * 2 * Math.PI);
			println("Rad: " + rad);
			
			
			double x = Math.sin(rad) * 40;
			double y = Math.cos(rad) * 40 * -1;
			
			println("X: " + x);
			println("Y: " + y);
			
			Point PP = new Point(P.x + (int)x, P.y + (int)y);
			
			Mouse.move(PP);
		}
	}
	
	private void southOf(){
		ScreenModel[] smuggler = ScreenModels.find(3543694631L);
		if (smuggler.length > 0){
			Point P = smuggler[0].base_point;
			println("Smuggler location: " + P);
			float angle = Minimap.getRotationAngle();
			println("Rotation angle: " + angle);
			
			double rad = (angle / 360 * 2 * Math.PI);
			println("Rad: " + rad);
			
			
			double x = Math.sin(rad) * 40;
			double y = Math.cos(rad) * 40;
			
			println("X: " + x);
			println("Y: " + y);
			
			Point PP = new Point(P.x + (int)x, P.y + (int)y);
			
			Mouse.move(PP);
		}
	}
	
	private void westOf(){
		ScreenModel[] smuggler = ScreenModels.find(3543694631L);
		if (smuggler.length > 0){
			Point P = smuggler[0].base_point;
			println("Smuggler location: " + P);
			float angle = Minimap.getRotationAngle();
			println("Rotation angle: " + angle);
			
			double rad = (angle / 360 * 2 * Math.PI);
			println("Rad: " + rad);
			
			
			double x = Math.cos(rad) * 40 * -1;
			double y = Math.sin(rad) * 40;
			
			println("X: " + x);
			println("Y: " + y);
			
			Point PP = new Point(P.x + (int)x, P.y + (int)y);
			
			Mouse.move(PP);
		}
	}
	
	
	
	@Override
    public void run() {
		//makeCompass(15);
		//northOf();
		//eastOf();
		southOf();
		//westOf();

		
		
		
    }
}