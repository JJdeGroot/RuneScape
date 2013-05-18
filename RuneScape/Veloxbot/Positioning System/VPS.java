import org.veloxbot.api.internals.concurrent.Task;
import org.veloxbot.api.methods.Calculations;
import org.veloxbot.api.methods.Environment;
import org.veloxbot.api.methods.Game;
import org.veloxbot.api.methods.Mouse;
import org.veloxbot.api.methods.Walking;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Author : J J
 */

public class VPS {
    private final static Point MMc = new Point(627, 135);
    private static int[][][] mapData = null;
    private static double tolerance = 0.3;

    /**
     * Changes the loaded map data to the input data
     *
     * @param mapData The new map data information
     */
    public static void setMapData(final int[][][] mapData) {
        VPS.mapData = mapData;
    }

    /**
     * Changes the tolerance to the input
     *
     * @param tolerance The new tolerance
     */
    public static void setTolerance(final double tolerance) {
        VPS.tolerance = tolerance;
    }

    /**
     * Returns the map data that has been set up
     *
     * @return an int[][][] containing the loaded map data in the following format: [X][Y][R, G, B])
     */
    public static int[][][] getMapData() {
        return mapData;
    }

    /**
     * Returns the tolerance that has been set up
     *
     * @return the tolerance thas has been set up
     */
    public static double getTolerance() {
        return tolerance;
    }
    
    private static double getCompassAngle() {
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
    
    private static double getRads(){
    	return Math.toRadians(360 - getCompassAngle());
    }

	private static Point rotateAround(int x, int y, Point center, double rads){ 
		return new Point((int) Math.round(center.x + (Math.cos(rads) * (x - center.x) - Math.sin(rads) * (y - center.y))), 
				         (int) Math.round(center.y + (Math.sin(rads) * (x - center.x) + Math.cos(rads) * (y - center.y))));
	}
	
	 /**
     * Returns a BufferedImage of the minimap, rotated to face north
     *
     * @return a 100x100 BufferedImage
     */
	public static BufferedImage getRotatedMiniMap(){
		final double radians = getRads();
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

	 /**
     * Returns a BufferedImage of the minimap
     *
     * @return a 100x100 BufferedImage
     */
    public static BufferedImage getMiniMap() {
        return Game.getImage().getSubimage(MMc.x - 50, MMc.y - 50, 100, 100);
    }

    /**
     * Returns a BufferedImage of the full world map
     * Place the map in the Storage directory of VeloxBot
     *
     * @return a BufferedImage of the world map
     */
    public static BufferedImage getWorldMap() {
        final String location = Environment.getStorageDirectory() + File.separator + "worldmap.png";
        try {
            return ImageIO.read(new File(location));
        } catch (IOException e) {
            System.out.println("Worldmap not found, please place it in the Storage directory of VeloxBot : worldmap.png");
            System.out.println("Full error:");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a BufferedImage of the map of choice
     * place the map in the Storage directory of VeloxBot
     *
     * @param name the name of the map you want to load
     * @return a BufferedImage of the map
     */
    public static BufferedImage getMap(final String name) {
        final String location = Environment.getStorageDirectory() + "\\"+ name + ".png";
        System.out.println(location);
        try {
            return ImageIO.read(new File(location));
        } catch (IOException e) {
            System.out.println("Map not found, please place it in the Storage directory of VeloxBot");
            System.out.println("Full error:");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a BufferedImage from an online website
     *
     * @param link the online location of the map
     * @return a BufferedImage of the map
     */
    public static BufferedImage getOnlineMap(final String link) {
        try {
            return ImageIO.read(new URL(link));
        } catch (IOException e) {
            System.out.println("Couldn't load the map from the web");
            System.out.println("Full error:");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a BufferedImage from an online website
     *
     * @param image the image you want to convert
     * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
     */
    public static int[][][] getColorBoxes(BufferedImage image) {
        int[][][] result = new int[image.getWidth() / 5][image.getHeight() / 5][3];

        for (int x = 0; x <= image.getWidth() - 5; x += 5) {
            for (int y = 0; y <= image.getHeight() - 5; y += 5) {
                int R = 0, G = 0, B = 0;

                for (int i = x; i < x + 5; i++) {
                    for (int j = y; j < y + 5; j++) {
                        Color c = new Color(image.getRGB(i, j));
                        R = R + c.getRed();
                        G = G + c.getGreen();
                        B = B + c.getBlue();
                    }
                }

                result[x / 5][y / 5][0] = R;
                result[x / 5][y / 5][1] = G;
                result[x / 5][y / 5][2] = B;
            }
        }

        return result;
    }

    /**
     * Returns an int[][][] containing minimap data
     *
     * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
     */
    public static int[][][] getMiniMapData() {
        return getColorBoxes(getMiniMap());
    }
    
    /**
     * Returns an int[][][] containing minimap data
     * The minimap has been rotated to face North
     *
     * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
     */
    public static int[][][] getRotatedMiniMapData() {
        return getColorBoxes(getRotatedMiniMap());
    }    
    

    /**
     * Returns an int[][][] containing the worldmap data
     *
     * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
     */
    public static int[][][] getWorldMapData() {
        return getColorBoxes(getWorldMap());
    }

    /**
     * Returns an int[][][] containing the data of the map of choice
     *
     * @param name the name of your map
     * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
     */
    public static int[][][] getMapData(final String name) {
        return getColorBoxes(getMap(name));
    }

    /**
     * Returns an int[][][] containg the data of the online map of choice
     *
     * @param link the link to your map
     * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
     */
    public static int[][][] getOnlineMapData(final String link) {
        return getColorBoxes(getOnlineMap(link));
    }

    /**
     * Returns a BufferedImage from an online website
     *
     * @return your location as a Point
     */
    public static Point getPosition() {
        final int[][][] mmData = getRotatedMiniMapData();
        int highestMatch = 0,
                mmWidth = mmData.length,
                mmHeight = mmData[0].length,
                wmWidth = mapData.length,
                wmHeight = mapData[0].length;
        final double min = 1 - tolerance,
                max = 1 + tolerance;
        Point p = new Point(0, 0);
        for (int x = 0; x < wmWidth - mmWidth; x++) {
            for (int y = 0; y < wmHeight - mmHeight; y++) {
                int matches = 0;

                for (int i = 0; i < mmWidth; i++) {
                    for (int j = 0; j < mmHeight; j++) {
                        // Getting worldmap and minimap Red values
                        int wmR = mapData[x + i][y + j][0],
                                mmR = mmData[i][j][0];
                        // Check if red matches
                        if (wmR > mmR * min && wmR < mmR * max) {
                            // Red matches so we continue with Green values
                            int wmG = mapData[x + i][y + j][1],
                                    mmG = mmData[i][j][1];
                            // Check if green matches
                            if (wmG > mmG * min && wmG < mmG * max) {
                                // Green matches so we continue with Blue values
                                int wmB = mapData[x + i][y + j][2],
                                        mmB = mmData[i][j][2];
                                // Check if blue matches
                                if (wmB > mmB * min && wmB < mmB * max) {
                                    // RGB matched within the accuracy so we have a match
                                    matches++;
                                }
                            }
                        }
                    }
                }
                if (matches > highestMatch) {
                    highestMatch = matches;
                    p = new Point(x, y);
                }
            }
        }
        return new Point(p.x * 5 + mmWidth * 5 / 2, p.y * 5 + mmWidth * 5 / 2);
    }

    private static double getDifference(final Point p1, final Point p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) + Math.pow(Math.abs(p1.y - p2.y), 2));
    }

    /**
     * Checks if a Point is on the minimap
     *
     * @param p the Point that you want to check
     * @return true if the Point is on the minimap
     */
    public static boolean isOnMiniMap(final Point p) {
        return getDifference(p, MMc) <= 75;
    }

    /**
     * Converts a Point from the setup map to a Point on the minimap
     * NOTE: The converted Point could be off the minimap
     *
     * @param p the Point that you want to convert
     * @return a converted Point
     */
    public static Point posToMM(final Point p) {
        final Point myPos = getPosition();
        return rotateAround(MMc.x + p.x - myPos.x, MMc.y + p.y - myPos.y, MMc, getRads());
    }

    /**
     * Converts a Point from the setup map to a Point on the minimap
     * based on the input location
     * NOTE: The converted Point could be off the minimap
     *
     * @param p the Point that you want to convert
     * @return a converted Point
     */
    public static Point posToMM(final Point myPos, final Point p) {
        return rotateAround(MMc.x + p.x - myPos.x, MMc.y + p.y - myPos.y, MMc, getRads());
    }

    /**
     * Converts a Point from the minimap to a Point on the setup map
     * NOTE: The converted Point could be off the map
     *
     * @param p the Point that you want to convert
     * @return a converted Point
     */
    public static Point mmToPos(final Point p) {
        if (isOnMiniMap(p)) {
        	Point rP = rotateAround(p.x, p.y, MMc, getRads()),
                  myPos = getPosition();
        	//System.out.println("Rotated P: " + rP + " myPos: " + myPos);
            return new Point(myPos.x + rP.x - MMc.x, myPos.y + rP.y - MMc.y);
        }
        return null;
    }

    /**
     * Converts a Point from the minimap to a Point on the setup map
     * based on the input location
     * NOTE: The converted Point could be off the map
     *
     * @param p the Point that you want to convert
     * @return a converted Point
     */
    public static Point mmToPos(final Point myPos, final Point p) {
        if (isOnMiniMap(p)) {
        	Point rP = rotateAround(p.x, p.y, MMc, getRads());
        	return new Point(myPos.x + rP.x - MMc.x, myPos.y + rP.y - MMc.y);
        }
        return null;
    }

    /**
     * Attempts to click a Point from the loaded map on the minimap
     *
     * @param p the Point that want to click on
     * @return true if the Point has been clicked
     */
    public static boolean clickOn(final Point p) {
        Point goal = posToMM(p);
        if (isOnMiniMap(goal)) {
            Mouse.click(goal, true);
            return true;
        }
        return false;
    }

    /**
     * Returns the distance to the flag, -1 if the flag has not been detected
     *
     * @return the distance as a double
     */
    public static double flagDistance() {
        final Point F = Walking.getFlag();
        if (F != null) {
            return getDifference(F, MMc);
        }
        return -1;
    }

    private static void FFlag(final Point p) {
        final Point goalPos = mmToPos(p);
        Point myPos = getPosition();
        final long t = System.currentTimeMillis();
        while (getDifference(goalPos, myPos) > 10) {
            if (timeFromMark(t) > 5000) {
                break;
            }
            Task.sleep(100, 300);
            myPos = getPosition();
        }
    }

    private static void waitUntilNotMoving() {
        Task.sleep(500, 1000);
        while (Walking.isWalking()) {
            Task.sleep(100, 300);
        }
    }

    /**
     * Attempts to click on the given Point and waits until we have
     * stopped moving
     * NOTE: VPS only works if the Point is on the Minimap
     *
     * @param p the Point that you want to walk to
     * @return a converted Point
     */
    public static boolean walkTo(final Point p) {
        if (clickOn(p)) {
            waitUntilNotMoving();
            return true;
        }
        return false;
    }

    private static long timeFromMark(final long T) {
        return System.currentTimeMillis() - T;
    }

    /**
     * Attempts to blind walk to a given location while the
     * maximum time has not elapsed.
     *
     * @param p           the Point that you want to walk to
     * @param maximumTime the maximum amount of time in ms
     * @return true if we managed to walk to P
     */
    public static boolean blindWalkTo(final Point p, final int maximumTime) {
        boolean reached = false;
        Point goalOnMM = posToMM(p);
        final long T = System.currentTimeMillis();
        while (!reached /*&& Context.script != null*/) {
            if (isOnMiniMap(goalOnMM)) {
                Mouse.click(goalOnMM, true);
                return true;
            }
            
            if (timeFromMark(T) > maximumTime) {
                return false;
            }
            
            final double rads = getRads();
            double nearest = 100000;
            Point bestPoint = null;
            
            // Loop through the whole minimap to find the best point
            for (int x = MMc.x - 75; x <= MMc.x + 75; x++) {
                for (int y = MMc.y - 75; y <= MMc.y + 75; y++) {
                    Point temp = rotateAround(x, y, MMc, rads);
                    if (isOnMiniMap(temp)) {
                        double difference = getDifference(temp, goalOnMM);
                        if (difference < nearest) {
                            nearest = difference;
                            bestPoint = temp;
                        }
                    }
                }
            }
            
            Mouse.click(bestPoint, true);
            FFlag(bestPoint);
            goalOnMM = posToMM(p);
        }
        return false;
    }

    /**
     * Attempts to walk the path that has been input
     *
     * @param path the path that should be walked
     * @return true if we managed to walk to the final Point
     */
    public static boolean walkPath(final Point[] path) {
        boolean reached = false;
        while (!reached /*&& Context.script != null*/) {
            Point myPos = getPosition();
            int size = path.length - 1,
                fails = 0;
            for (int i = size; i >= 0; i--) {
                Point mm = posToMM(myPos, path[i]);
                if (isOnMiniMap(mm)) {
                    Mouse.click(mm, true);
                    if (i == size) {
                        waitUntilNotMoving();
                        return true;
                    }
                    FFlag(mm);
                    break;
                } else {
                    fails++;
                }
            }
            if (fails >= size + 1) {
                break;
            }
        }
        return false;
    }
}