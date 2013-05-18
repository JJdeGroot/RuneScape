package scripts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.util.Util;

@ScriptManifest(authors = { "TRiLeZ / J J" }, category = "Dump", name = "Dump Minimaps")
public class DumpMaps extends Script{
	
	public void dumpMaps() {
        long t = System.currentTimeMillis();
       
        final int[][] data = org.tribot.api.Minimap.getRGBData();

        final BufferedImage bi = new BufferedImage(data.length, data[0].length,
                        BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < bi.getWidth(); x++)
                for (int y = 0; y < bi.getHeight(); y++)
                        bi.setRGB(x, y, data[x][y]);

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
		dumpMaps();
    	
    }
}