import java.awt.Graphics;

import org.veloxbot.api.ActiveScript;
import org.veloxbot.api.Manifest;

@Manifest(authors = {""}, version = 1.0, name = "", description = "")
public class Default extends ActiveScript {
	@Override
    public void onRepaint(final Graphics g) {
		g.drawString("Paint", 5, 130);
	}

	@Override
	public long loop() {
		
		
		return -1;
	}
}