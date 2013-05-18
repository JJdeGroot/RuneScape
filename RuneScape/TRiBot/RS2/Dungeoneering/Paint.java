package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import org.tribot.api.Screen;
import org.tribot.script.Script;

public class Paint extends Script {
	public BufferedImage getMapImage() {
        BufferedImage temp = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
 		Point firstMinimapRoom = new Point(199, 156);

        // Gathering room locations        
 		for (int i = 0; i < temp.getWidth(); i++){
 	        for (int j = 0; j < temp.getHeight(); j++){
 	        	Point P = new Point(i + firstMinimapRoom.x, j + firstMinimapRoom.y);
 	        	Color color = Screen.getColourAt(P);
 	        	temp.setRGB(i, j, color.getRGB());
 	        }
         }

        return temp;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
