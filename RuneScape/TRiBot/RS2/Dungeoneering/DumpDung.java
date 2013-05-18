package scripts;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.util.Util;

@ScriptManifest(authors = { "TRiLeZ / J J" }, category = "Dump", name = "Dump Minimaps")
public class DumpDung extends Script{
	
	public void dumpMaps(int choice) {
        long t = System.currentTimeMillis();
       
        final int[][] data = org.tribot.api.Minimap.getRGBData();

        final BufferedImage bi = new BufferedImage(data.length, data[0].length,
                        BufferedImage.TYPE_INT_RGB);
        
        // Gathering room locations        
        int roomSize = 59, roomDistance = 4;
 		Point room = new Point(113, 82);
 		int[][] rooms = new int[36][];
 		for (int i = 0; i <= 5; i++){
 	        for (int j = 0; j <= 5; j++){
 	        	rooms[(i*6) + j] = new int[] {room.x + i * (roomSize + roomDistance),
 				        					  room.y + j * (roomSize + roomDistance),
 				        					  room.x + i * (roomSize + roomDistance) + roomSize,
 				        					  room.y + j * (roomSize + roomDistance) + roomSize};
 	        }
         }

        for (int x = 0; x < bi.getWidth(); x++){
                for (int y = 0; y < bi.getHeight(); y++){
                		Point P = new Point(x, y);
                		if (P.x >= rooms[choice][0]){
                			if (P.x <= rooms[choice][2]){
                				if (P.y >= rooms[choice][1]){
                					if (P.y <= rooms[choice][3]){
                						bi.setRGB(x, y, data[x][y]);
                					}
                				}
                			}
                		}
                        
                }
        }

        File file = new File(Util.getAppDataDirectory(), t
                                        + ".png");

        try {
                ImageIO.write(bi, "png", file);
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
	}
	
	@Override
    public void run() {
		dumpMaps(4);
    	
    }
}