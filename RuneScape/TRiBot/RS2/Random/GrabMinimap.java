package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.tribot.api.Minimap;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "J J" }, category = "Test", name = "Grab Minimap")
public class GrabMinimap extends Script implements Painting{
	RenderedImage Image = myMinimap();
	int[][] data = Minimap.getRGBData();
	
	  public void onPaint(Graphics g) {
		  if (data.length > 0){
			  for (int i = 0; i < data.length; i++){
				  for (int j = 0; j < data[i].length; j++){
					  g.setColor(new Color(data[i][j]));
					  g.drawLine(i, j, i, j);
				  }
			  }
		  }
      }
	

	public RenderedImage myMinimap() {
		int width = 512;
		int height = 512;
	    
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		
		int[][] data = Minimap.getRGBData();
		if (data.length > 0) {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					g2d.setColor(new Color(data[i][j]));
					g2d.drawLine(i, j, i, j);
				}
			}
		}
		
		g2d.dispose();
		
		return bufferedImage;
	}
	
	@Override
	public void run() {
		try {
			File file = new File("Z:\\Bots\\TRiBot\\Minimap\\GOP9.png");
			ImageIO.write(Image, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sleep(600000);
	}
}