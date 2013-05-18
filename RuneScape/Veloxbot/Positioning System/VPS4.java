import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.veloxbot.api.methods.Game;
import org.veloxbot.api.methods.Mouse;
import org.veloxbot.api.methods.Walking;
import org.veloxbot.api.utils.Random;

public class VPS4 {
	final private Point MMc = new Point(627, 135);
	private int[][][] mapData = null;
	private double tolerance = 0.3;
	
	/**
	 * Changes the loaded map data to the input data
	 *
	 * @param  mapData The new map data information
	 */
	public void setMapData(int[][][] mapData){
		this.mapData = mapData;
	}
	
	/**
	 * Changes the tolerance to the input
	 *
	 * @param  tolerance The new tolerance
	 */	
	public void setTolerance(double tolerance){
		this.tolerance = tolerance;
	}
	
	/**
	 * Returns the map data that has been set up
	 * 
	 * @return an int[][][] containing the loaded map data in the following format: [X][Y][R, G, B])
	 */	
	public int[][][] getMapData(){
		return mapData;
	}
	
	/**
	 * Returns the tolerance that has been set up
	 *
	 * @return the tolerance thas has been set up
	 */
	public double getTolerance(){
		return tolerance;
	}
	
	/**
	 * Returns a 100x100 BufferedImage of the minimap
	 * Place the map in the Storage directory of VeloxBot
	 *
	 * @return a 100x100 BufferedImage of the minimap
	 */
	public BufferedImage getMinimap(){
		return Game.getImage().getSubimage(MMc.x-50, MMc.y-50, 100, 100);
	}
	
	/**
	 * Returns a BufferedImage of the full world map
	 * Place the map in the Storage directory of VeloxBot
	 *
	 * @return a BufferedImage of the world map
	 */
	public BufferedImage getWorldmap(){
		String location = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "\\VeloxBot\\Storage\\worldmap.png";
	    BufferedImage image = null;
        try {
        	image = ImageIO.read(new File(location));
        } catch (IOException e) {
                System.out.println("Worldmap not found, please place it in the Storage directory of VeloxBot");
                System.out.println("Full error:");
                e.printStackTrace();
        }
        return image;
	}
	
	/**
	 * Returns a BufferedImage of the map of choice 
	 * place the map in the Storage directory of VeloxBot
	 *
	 * @param  name the name of the map you want to load
	 * @return a BufferedImage of the map
	 */
	public BufferedImage getMap(String name){
		String location = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "\\VeloxBot\\Storage\\" + name + ".png";
	    BufferedImage image = null;
        try {
        	image = ImageIO.read(new File(location));
        } catch (IOException e) {
                System.out.println("Map not found, please place it in the Storage directory of VeloxBot");
                System.out.println("Full error:");
                e.printStackTrace();
        }
        return image;
	}
	
	/**
	 * Returns a BufferedImage from an online website
	 *
	 * @param  url the online location of the map
	 * @return a BufferedImage of the map
	 */
	public BufferedImage getOnlineMap(String link){
		BufferedImage image = null;
        try {
        	image = ImageIO.read(new URL(link));
        } catch (IOException e) {
                System.out.println("Couldn't load the map from the web");
                System.out.println("Full error:");
                e.printStackTrace();
        }
        return image;
	}
	
	/**
	 * Returns a BufferedImage from an online website
	 *
	 * @param image the image you want to convert
	 * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
	 */	
	public int[][][] getColorBoxes(BufferedImage image){
		int[][][] result = new int[image.getWidth()/5][image.getHeight()/5][3];
		
		for(int x = 0; x <= image.getWidth()-5; x+=5){
			for(int y = 0; y <= image.getHeight()-5; y+=5){
				int R = 0, G = 0, B = 0;
				
				for(int i = x; i < x+5; i++){
					for(int j = y; j < y+5; j++){
						Color c = new Color(image.getRGB(i, j));
						R = R + c.getRed();
						G = G + c.getGreen();
						B = B + c.getBlue();
					}
				}
				
				result[x/5][y/5][0] = R;
				result[x/5][y/5][1] = G;
				result[x/5][y/5][2] = B;
			}
		}
		
		return result;
	}
	
	/**
	 * Returns an int[][][] containing minimap data
	 *
	 * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
	 */	
	public int[][][] getMinimapData(){
		return getColorBoxes(getMinimap());
	}
	
	/**
	 * Returns an int[][][] containing the worldmap data
	 *
	 * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
	 */	
	public int[][][] getWorldmapData(){
		return getColorBoxes(getWorldmap());
	}
	
	/**
	 * Returns an int[][][] containing the data of the map of choice
	 *
	 * @param name the name of your map
	 * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
	 */	
	public int[][][] getMapData(String name){
		return getColorBoxes(getMap(name));
	}
	
	/**
	 * Returns an int[][][] containg the data of the online map of choice
	 *
	 * @param link the link to your map
	 * @return an int[][][] containing data in the following format: [X][Y][R, G, B])
	 */	
	public int[][][] getOnlineMapData(String link){
		return getColorBoxes(getOnlineMap(link));
	}
	
	/**
	 * Returns a BufferedImage from an online website
	 *
	 * @param mmData the minimap data
	 * @param wmData the worldmap data
	 * @param tolerance
	 * @return your location as a Point
	 */		
	public Point getPosition(){
		int[][][] mmData = getMinimapData();
		int highestMatch = 0,
			mmWidth = mmData.length,
            mmHeight = mmData[0].length,
            wmWidth = mapData.length,
            wmHeight = mapData[0].length;
		double min = 1 - tolerance, 
			   max = 1 + tolerance;
		Point P = new Point(0, 0);

		for(int x = 0; x < wmWidth-mmWidth; x++){
            for(int y = 0; y < wmHeight-mmHeight; y++){
                int matches = 0;
                
                for(int i = 0; i < mmWidth; i++){
                    for(int j = 0; j < mmHeight; j++){
                        // Getting worldmap and minimap Red values
                        int wmR = mapData[x+i][y+j][0],
                            mmR = mmData[i][j][0];
                        // Check if red matches
                        if(wmR > mmR*min && wmR < mmR*max){
                            // Red matches so we continue with Green values
                            int wmG = mapData[x+i][y+j][1],
                                mmG = mmData[i][j][1];
                                // Check if green matches
                            if(wmG > mmG*min && wmG < mmG*max){
                                // Green matches so we continue with Blue values
                                int wmB = mapData[x+i][y+j][2],
                                    mmB = mmData[i][j][2];
                                // Check if blue matches
                                if(wmB > mmB*min && wmB < mmB*max){
                                    // RGB matched within the accuracy so we have a match
                                    matches++;
                                }
                            }
                        }
                    }
                }
                
                if (matches > highestMatch){
                    highestMatch = matches;
                    P = new Point(x, y);
                }
            }
        }
		
		Point myPos = new Point(P.x*5+mmWidth*5/2, P.y*5+mmWidth*5/2);
		//System.out.println("My position: " + myPos);
		return myPos;
	}
	
	private double getDifference(Point P1, Point P2){
		return Math.sqrt(Math.pow(Math.abs(P1.x-P2.x), 2) + Math.pow(Math.abs(P1.y-P2.y), 2));
	}
	
	/**
	 * Checks if a Point is on the minimap
	 *
	 * @param P the Point that you want to check
	 * @return true if the Point is on the minimap
	 */	
	public boolean isOnMinimap(Point P){
		return getDifference(P, MMc) <= 75;
	}
	
	/**
	 * Converts a Point from the setup map to a Point on the minimap
	 * NOTE: The converted Point could be off the minimap
	 *
	 * @param P the Point that you want to convert
	 * @return a converted Point
	 */
	public Point posToMM(Point P){
		Point myPos = getPosition();
		return new Point(MMc.x+P.x-myPos.x, MMc.y+P.y-myPos.y);
	}
	
	/**
	 * Converts a Point from the setup map to a Point on the minimap
	 * based on the input location
	 * NOTE: The converted Point could be off the minimap
	 *
	 * @param P the Point that you want to convert
	 * @return a converted Point
	 */
	public Point posToMM(Point myPos, Point P){
		return new Point(MMc.x+P.x-myPos.x, MMc.y+P.y-myPos.y);
	}
	
	/**
	 * Converts a Point from the minimap to a Point on the setup map
	 * NOTE: The converted Point could be off the map
	 *
	 * @param P the Point that you want to convert
	 * @return a converted Point
	 */	
	public Point mmToPos(Point P){
		if(isOnMinimap(P)){
			Point myPos = getPosition();
			return new Point(myPos.x+P.x-MMc.x , myPos.y+P.y-MMc.y);
		}
		return null;
	}
	
	/**
	 * Converts a Point from the minimap to a Point on the setup map
	 * based on the input location
	 * NOTE: The converted Point could be off the map
	 *
	 * @param P the Point that you want to convert
	 * @return a converted Point
	 */	
	public Point mmToPos(Point myPos, Point P){
		if(isOnMinimap(P)){
			return new Point(myPos.x+P.x-MMc.x , myPos.y+P.y-MMc.y);
		}
		return null;
	}
	
	/**
	 * Attempts to click a Point from the loaded map on the minimap
	 *
	 * @param P the Point that want to click on
	 * @return true if the Point has been clicked
	 */	
	public boolean clickOn(Point P){
		Point goal = posToMM(P);
		if(isOnMinimap(goal)){
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
	public double flagDistance(){
		Point F = Walking.getFlag();
		if(F != null){
			return getDifference(F, MMc);
		}
		return -1;
	}
	
	private void sleep(int minimum, int maximum){
		try {
			Thread.sleep(Random.nextInt(minimum, maximum));
		} catch (InterruptedException e) {
			System.out.println("Failed to sleep");
			System.out.println("Full error:");
			e.printStackTrace();
		}
	}
	
	private void FFlag(Point P){
		Point goalPos = mmToPos(P),
			  myPos = getPosition();
		long t = System.currentTimeMillis();
		while(getDifference(goalPos, myPos) > 10){
			if(timeFromMark(t) > 5000){
				break;
			}
			sleep(100, 300);
			myPos = getPosition();
		}
	}
	
	private void waitUntilNotMoving(){
		sleep(500, 1000);
		while(Walking.isWalking()){
			sleep(100, 300);
		}
	}

	/**
	 * Attempts to click on the given Point and waits until we have 
	 * stopped moving
	 * NOTE: This only works if the Point is on the Minimap
	 *
	 * @param P the Point that you want to walk to
	 * @return a converted Point
	 */	
	public boolean walkTo(Point P){
		if(clickOn(P)){
			waitUntilNotMoving();
			return true;
		}
		return false;
	}
	
	private long timeFromMark(long T){
		return System.currentTimeMillis() - T;
	}
	
	/**
	 * Attempts to blind walk to a given location while the
	 * maximum time has not elapsed. 
	 *
	 * @param P the Point that you want to walk to
	 * @param maximumTime the maximum amount of time in ms
	 * @return true if we managed to walk to P
	 */	
	public boolean blindWalkTo(Point P, int maximumTime){
		boolean reached = false;
		Point goalOnMM = posToMM(P);
		long T = System.currentTimeMillis();

		while(!reached){
			if(isOnMinimap(goalOnMM)){
				Mouse.click(goalOnMM, true);
				return true;
			}
			
			if(timeFromMark(T) > maximumTime){
				return false;
			}

			double nearest = 100000;
			Point bestPoint = null;
			// Loop through the whole minimap to find the best point
			for(int x = MMc.x-75; x <= MMc.x+75; x++){
				for(int y = MMc.y-75; y <= MMc.y+75; y++){
					Point temp = new Point(x, y);
					if(isOnMinimap(temp)){
						double difference = getDifference(temp, goalOnMM);
						if(difference < nearest){
							nearest = difference;
							bestPoint = new Point(x, y);
						}
					}
				}
			}

			Mouse.click(bestPoint, true);
			FFlag(bestPoint);
			goalOnMM = posToMM(P);
		}
		
		return false;
	}
	
	/**
	 * Attempts to walk the path that has been input
	 *
	 * @param path the path that should be walked
	 * @return true if we managed to walk to the final Point
	 */	
	public boolean walkPath(Point[] path){
		boolean reached = false;
		while(!reached){
			Point myPos = getPosition();
			int size = path.length-1,
				fails = 0;
			for(int i = size; i >= 0; i--){
				Point mm = posToMM(myPos, path[i]);
				if(isOnMinimap(mm)){
					Mouse.click(mm, true);
					System.out.println("Clicked on path[" + i + "] : (" + path[i].x + ", " + path[i].y + ")");
					if(i == size){
						waitUntilNotMoving();
						return true;
					}
					FFlag(mm);
					break;
				}else{
					fails++;
				}
			}
			if(fails >= size+1){
				break;
			}
		}
		
		return false;		
	}
}