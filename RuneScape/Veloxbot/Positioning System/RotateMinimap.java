import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.veloxbot.api.ActiveScript;
import org.veloxbot.api.Manifest;
import org.veloxbot.api.methods.Calculations;
import org.veloxbot.api.methods.Game;
import org.veloxbot.api.methods.Mouse;

@Manifest(authors = {"J J"}, version = 1.0, name = "Rotation test", description = "Rotates the minimap")
public class RotateMinimap extends ActiveScript {
   final private Point MMc = new Point(627, 135);

   private double getCompassAngle() {
        final Point[] points = Game.getPointsWithColor(Game.INNER_COMPASS_BOUNDS, new Color(0, 0, 1));
        if (points != null && points.length > 0) {
            final Polygon polygon = new Polygon();
            for (final Point p : points) {
                polygon.addPoint(p.x, p.y);
            }
            if (polygon.npoints > 0) {
                final Point compass = new Point(Game.INNER_COMPASS_BOUNDS.x + Game.INNER_COMPASS_BOUNDS.width / 2, Game.INNER_COMPASS_BOUNDS.y + Game.INNER_COMPASS_BOUNDS.height / 2);
                final Rectangle polybounds = polygon.getBounds();
                final Point ncenter = new Point(polybounds.x + polybounds.width / 2, polybounds.y + polybounds.height / 2);
                return Calculations.getAngle(ncenter, compass);
            }
        }
        return -1;
    }

	private Point rotateAround(int x, int y, Point center, double rads){ 
		return new Point((int) Math.round(center.x + (Math.cos(rads) * (x - center.x) - Math.sin(rads) * (y - center.y))), 
				         (int) Math.round(center.y + (Math.sin(rads) * (x - center.x) + Math.cos(rads) * (y - center.y))));
	}

	private BufferedImage rotateMinimap(){
		final double radians = Math.toRadians(360 - getCompassAngle());
		final BufferedImage game = Game.getImage();
		final int xStart =MMc.x-50,
				  yStart = MMc.y-50;
		BufferedImage rotated = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		
		for(int x = xStart; x < MMc.x+50; x++){
			for(int y = yStart; y < MMc.y+50; y++){
				final Point p = rotateAround(x, y, MMc, radians);
				rotated.setRGB(x-xStart, y-yStart, game.getRGB(p.x, p.y));
			}
		}
		game.flush();
		
		return rotated;
	}
	
	@Override
    public void onRepaint(final Graphics g) {
		g.drawImage(rotateMinimap(), 0, 0,  null);
	}

	@Override
	public long loop() {
		System.out.println(Game.INNER_COMPASS_BOUNDS);
		
		/*
		VPS.setMapData(VPS.getMapData("plankmap"));
		long t = System.currentTimeMillis();
		while(System.currentTimeMillis() < t+5000){
			Mouse.move(VPS.posToMM(new Point(73, 343)));
			sleep(200);
		}
		/*
		System.out.println("My pos: " + VPS.getPosition());
		System.out.println("Point (73, 343) on MM : " + VPS.posToMM(new Point(73, 343)));
		System.out.println("MM to pos (584, 122) : " + VPS.mmToPos(new Point(584, 122)));
		*/
		return -1;
	}
}