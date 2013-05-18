import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.veloxbot.api.ActiveScript;
import org.veloxbot.api.Manifest;
import org.veloxbot.api.methods.Game;
import org.veloxbot.api.methods.Mouse;
import org.veloxbot.api.methods.Walking;

@Manifest(authors = {"J J"}, version = 2.0, name = "JJ's Walking", description = "Walking system")
public class VPS2 extends ActiveScript {
	final Point MMc = new Point(627, 135);
	int[][][] mapData = null;
	double tolerance = 0.3;
	
	public void setMapData(int[][][] mapData){
		this.mapData = mapData;
	}
	
	public void setTolerance(double tolerance){
		this.tolerance = tolerance;
	}

	private BufferedImage getMinimap(){
		return Game.getImage().getSubimage(MMc.x-50, MMc.y-50, 100, 100);
	}
	
	private BufferedImage getWorldmap(){
		String location = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "\\VeloxBot\\Storage\\worldmap.png";
	    BufferedImage image = null;
        try {
        	image = ImageIO.read(new File(location));
        } catch (IOException e) {
                System.out.println("Please place the world map in the Storage directory of VeloxBot");
                System.out.println("Error:");
                e.printStackTrace();
        }
        return image;
	}
	
	private BufferedImage getMap(String number){
		String location = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "\\VeloxBot\\Storage\\" + number + ".png";
	    BufferedImage image = null;
        try {
        	image = ImageIO.read(new File(location));
        } catch (IOException e) {
                System.out.println("Please place the map in the Storage directory of VeloxBot");
                System.out.println("Error:");
                e.printStackTrace();
        }
        return image;
	}
	
	private int[][][] getColorBoxes(BufferedImage image){
		int[][][] result = new int[image.getWidth()/5][image.getHeight()/5][3];
		
		for(int x = 0; x < image.getWidth(); x+=5){
			for(int y = 0; y < image.getHeight(); y+=5){
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
	
	private int[][][] getMinimapData(){
		return getColorBoxes(getMinimap());
	}
	
	private int[][][] getWorldmapData(){
		return getColorBoxes(getWorldmap());
	}
	
	private int[][][] getMapData(String number){
		return getColorBoxes(getMap(number));
	}
	
	private Point getPosition(int[][][] mmData, int[][][] wmData, double tolerance){
		int highestMatch = 0,
			mmWidth = mmData.length,
            mmHeight = mmData[0].length,
            wmWidth = wmData.length,
            wmHeight = wmData[0].length;
		double min = 1 - tolerance, 
			   max = 1 + tolerance;
		Point P = new Point(0, 0);

		for(int x = 0; x < wmWidth-mmWidth; x++){
            for(int y = 0; y < wmHeight-mmHeight; y++){
                int matches = 0;
                
                for(int i = 0; i < mmWidth; i++){
                    for(int j = 0; j < mmHeight; j++){
                        // Getting worldmap and minimap Red values
                        int wmR = wmData[x+i][y+j][0],
                            mmR = mmData[i][j][0];
                        // Check if red matches
                        if(wmR > mmR*min && wmR < mmR*max){
                            // Red matches so we continue with Green values
                            int wmG = wmData[x+i][y+j][1],
                                mmG = mmData[i][j][1];
                                // Check if green matches
                            if(wmG > mmG*min && wmG < mmG*max){
                                // Green matches so we continue with Blue values
                                int wmB = wmData[x+i][y+j][2],
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
	
	private boolean isOnMinimap(Point P){
		return getDifference(P, MMc) <= 75;
	}
	
	private Point posToMM(Point P){
		Point myPos = getPosition(getMinimapData(), mapData, tolerance);
		return new Point(MMc.x+P.x-myPos.x, MMc.y+P.y-myPos.y);
	}
	
	private Point posToMM(Point myPos, Point P){
		return new Point(MMc.x+P.x-myPos.x, MMc.y+P.y-myPos.y);
	}
	
	private Point mmToPos(Point P){
		if(isOnMinimap(P)){
			Point myPos = getPosition(getMinimapData(), mapData, tolerance);
			return new Point(myPos.x+P.x-MMc.x , myPos.y+P.y-MMc.y);
		}
		return null;
	}
	
	private Point mmToPos(Point myPos, Point P){
		if(isOnMinimap(P)){
			return new Point(myPos.x+P.x-MMc.x , myPos.y+P.y-MMc.y);
		}
		return null;
	}

	private boolean clickOn(Point P){
		Point goal = posToMM(P);
		if(isOnMinimap(goal)){
			Mouse.click(goal, true);
			return true;
		}
		return false;
	}
	
	private double flagDistance(){
		Point F = Walking.getFlag();
		return getDifference(F, MMc);
	}
	
	private void FFlag(Point P){
		Point goalPos = mmToPos(P),
			  myPos = getPosition(getMinimapData(), mapData, tolerance);
		long t = System.currentTimeMillis();
		while(getDifference(goalPos, myPos) > 10){
			if(timeFromMark(t) > 5000){
				break;
			}
			sleep(100, 300);
			myPos = getPosition(getMinimapData(), mapData, tolerance);
			
		}
	}
	
	private void waitUntilNotMoving(){
		sleep(500, 1000);
		while(Walking.isWalking()){
			sleep(100, 300);
		}
	}

	private boolean walkTo(Point P){
		if(clickOn(P)){
			waitUntilNotMoving();
			return true;
		}
		return false;
	}
	
	private long timeFromMark(long T){
		return System.currentTimeMillis() - T;
	}
	
	private boolean blindWalkTo(Point P, int maximumTime){
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
	
	private boolean walkPath(Point[] path){
		ArrayList<Point> pathList = new ArrayList<Point>();
		for(int i = 0; i < path.length; i++){
			pathList.add(path[i]);
		}

		while(!pathList.isEmpty()){
			Point myPos = getPosition(getMinimapData(), mapData, tolerance);
			int size = pathList.size()-1,
				fails = 0;
			System.out.println("Size: " + size);
			for(int i = size; i >= 0; i--){
				Point mm = posToMM(myPos, pathList.get(i));
				System.out.println("MM: " + mm + " index: " + i);
				sleep(1000);
				if(isOnMinimap(mm)){
					Mouse.click(mm, true);
					if(i != size){
						FFlag(mm);
						for(int j = 0; j <= i; j++){
							pathList.remove(j);
						}
					}else{
						waitUntilNotMoving();
						return true;
					}
				}else{
					fails++;
				}
			}
			System.out.println("Fails: " + fails);
			if(fails >= size+1){
				System.out.println("Fails = size");
				break;
			}
		}
		return false;		
	}
	
	/*
	@Override
    public void onRepaint(final Graphics g) {
		g.drawImage(getMinimap(), 5, 120,  null);
		g.drawString("Calculating...", 5, 130);
		g.drawString("My position: " + getPosition(getMapData("9_9"), getMinimapData(), 0.3), 5, 210);
		g.drawImage(getMap("9_9"), 150, 150, null);
	}
	*/

	@Override
	public long loop() {
		System.out.println("---------------------------");
		setMapData(getMapData("9_9"));
		//getPosition(getMinimapData(), mapData, tolerance);
		//clickOn(new Point(292, 156));
		//System.out.println(mmToPos(new Point(632, 185)));
		//walkTo(new Point(292, 156));
		//blindWalkTo(new Point(45, 165), 30000);
		Point[] path = {new Point(307, 235), new Point(349, 226), new Point(350, 160), new Point(319, 134)};
		walkPath(path);
		System.out.println("---------------------------");
		return -1;
	}
}