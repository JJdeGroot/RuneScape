package scripts;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.tribot.api.Minimap;
import org.tribot.api.Textures;
import org.tribot.api.ChooseOption;
import org.tribot.api.Player;
import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.Texture;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;

public class General extends Script{
	public boolean isMoving(){
		Point MMc = new Point(627, 135);
		int pixelShift = Screen.getPixelShift(MMc.x - 50, MMc.y - 50, MMc.x + 50, MMc.y + 50, 250);
		println("Pixelshift: " + pixelShift);
		if (pixelShift > 25){
			return true;
		}
		
		return false;
	}
	
	public void waitUntilNotMoving(){
		sleep(750, 1000);
		while (isMoving()){
			sleep(100, 200);
		}
	}
	
	public int randomRange(int aFrom, int aTo){
    	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
    }
	
	public boolean randomSelectOptionAt(Point coord, String upText, String option, int leftChance, int rightChance, int maxWait){
    	int totalChance = leftChance + rightChance;
    	Mouse.move(coord);
    	if (Timing.waitUptext(upText, (maxWait/2))){
        	if ((new Random().nextInt(totalChance)) < leftChance){
        		Mouse.click(coord, 1);
        		return true;
        	}else{
        		Mouse.click(coord, 3);
        		if (Timing.waitChooseOption(option, (maxWait/2))){
        			ChooseOption.select(option);
        			return true;
        		}else{
        			return false;
        		}
        	}
    	}else{
    		return false;
    	}
    }
	
	public boolean rightClicktAt(Point coord, String option, int maxWait){
    	Mouse.click(coord, 3);
		if (Timing.waitChooseOption(option, (maxWait/2))){
			ChooseOption.select(option);
			return true;
		}

		return false;
    }
	
	public Point getNearestPoint(long id){
		Point MSc = new Point(259, 220);
        ScreenModel[] find = ScreenModels.find(id);
        ArrayList<Integer> distList = new ArrayList<Integer>();
        ArrayList<Point> coordList = new ArrayList<Point>();
        
        if (find.length > 0){
	        for (int i = 0; i < find.length; i++){
                distList.add((int) Math.sqrt(Math.pow(Math.abs(MSc.x - find[i].base_point.x), 2) + Math.pow(Math.abs(MSc.y - find[i].base_point.y), 2)));
                coordList.add(new Point(find[i].base_point.getLocation()));
	        }
	        
	        if (!distList.isEmpty() && !coordList.isEmpty()){
	            int nearest = 1000;
	            int t = 0;
	            
	            for (int i = 0; i < distList.size(); i++){
	                if (distList.get(i) < nearest){
	                    nearest = distList.get(i);
	                    t = i;
	                }
	            }
	
	            return coordList.get(t);
	        }
        }
        
        return null;
    }
	
	public long getNearestID(long[] ids){
		Point MSc = new Point(259, 220);
        ScreenModel[] find = ScreenModels.find(ids);
        ArrayList<Long> idList = new ArrayList<Long>();
        ArrayList<Integer> distList = new ArrayList<Integer>();
        
        if (find.length > 0){
	        for (int i = 0; i < find.length; i++){
                distList.add((int) Math.sqrt(Math.pow(Math.abs(MSc.x - find[i].base_point.x), 2) + Math.pow(Math.abs(MSc.y - find[i].base_point.y), 2)));
                idList.add(find[i].id);
	        }
	        
	        if (!idList.isEmpty() && !distList.isEmpty()){
	            int nearest = 1000;
	            int t = 0;
	            
	            for (int i = 0; i < distList.size(); i++){
	                if (distList.get(i) < nearest){
	                    nearest = distList.get(i);
	                    t = i;
	                }
	            }
	
	            return idList.get(t);
	        }
        }
        
        return -1;
    }
	
	public Point getNearestMM(Point[] pts){
		Point MMc = new Point(627, 135);
		int best = 0;
		double nearest = 1000;

        for (int i = 0; i < pts.length; i++){
        	double dist = Math.abs(MMc.x - pts[i].x) + Math.abs(MMc.y - pts[i].y);
        	if (dist < nearest){
        		nearest = dist;
        		best = i;
        	}
        }
        
        return pts[best];
    }
	
	public Point[] getRoom(){
		boolean[][] map = new boolean[151][151];
		int MMX1 = 551, MMY1 = 59, MMXL = 151, MMYL = 151;
		Color A = new Color(0, 0, 0);
		Color B = new Color(0, 0, 0);

		// Get all pixels that aren't black from the minimap, store in a 2d boolean array
		for (int i = 0; i < map.length; i++){
			for (int j = 0; j < map[i].length; j++){
				double dist = Math.pow(MMX1 + (MMXL/2) - (i + MMX1), 2) + Math.pow(MMY1 + (MMYL/2) - (j + MMY1), 2);
				if (dist < (Math.pow(MMXL/2, 2))){
					B = Screen.getColourAt(new Point(i + MMX1, j + MMY1));
					if (!Screen.coloursMatch(A, B, new Tolerance(10))){
						map[i][j] = true;
					}
				}
			}
		}
		
		// Flood-fill to get the current room
		ArrayList<Point> stack = new ArrayList<Point>();
		stack.add(new Point(627, 135));
		int[][] offset = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
		int i = 0;
		
		while (stack.size() > i){
			Point convert = new Point(stack.get(i).x-MMX1, stack.get(i).y-MMY1);

			for (int j = 0; j < offset.length; j++){
				Point PT = new Point(convert.x + offset[j][0], convert.y + offset[j][1]);
				if (map[PT.x][PT.y]){
					stack.add(new Point(PT.x + MMX1, PT.y + MMY1));
					map[PT.x][PT.y]= false; 
				}
			}
			
			i++;
		}
		
		Point[] coords = stack.toArray(new Point[stack.size()]); 

		return coords;
	}
	
	public Point middleOfRoom(){
		Point[] coords = getRoom();
		int totalX = 0,
		    totalY = 0,
		    length = coords.length;
		    
		for (int i = 0; i < length; i++){
			totalX = totalX + coords[i].x;
			totalY = totalY + coords[i].y;
		}
		
		Point P = (new Point(totalX/length, totalY/length));
		return P;
	}
	
	public void fixRightClick(){
		while (ChooseOption.getPosition() != null){
			Point MP = new Point(Mouse.getPos());
			Mouse.move(MP.x + randomRange(-50, 50), MP.y + randomRange(-50, 50));
		}
	}
	
	public void walkToMiddle(){
		Point middle = middleOfRoom();
		Mouse.move(middle);
		Mouse.click(1);
		waitUntilNotMoving();
	}
	
	public Point randomPoint(){
		Point[] coords = getRoom();
		int random = randomRange(0, coords.length);
		return coords[random];
	}
	
	public void walkToRandom(){
		Point random = randomPoint();
		Mouse.move(random);
		Mouse.click(1);
		waitUntilNotMoving();
	}

	public boolean waitFindID(long ID, long maxTimeInMS){
		long startTime = System.currentTimeMillis(),
			 maxTime = startTime + maxTimeInMS;
		
		while (System.currentTimeMillis() < maxTime){
			if (ScreenModels.find(ID).length > 0){
				return true;
			}
		}
		
		return false;
	}
	
	public int getTexture(String which){
		String[] options = {"Npc", "Item"};
		int choice = -1;
		for (int i = 0; i < options.length; i++){
			if (options[i].equals(which)){
				choice = i;
				break;
			}
		}
		
		Color color = new Color(0, 0, 0);		
		switch(choice){
			case 0: color = new Color(235, 235, 10); break;
			case 1: color = new Color(230, 0, 0); break;
		}
		
		Point[] coords = getRoom();
		Texture[] all = Textures.getAll();
		
		for (int i = 0; i < coords.length; i++) {
			int x = coords[i].x,
			    y = coords[i].y;
			ColourPoint[] find = Screen.findColours(color, x, y, x, y, new Tolerance(25));
			if (find.length > 0) {
				for (int j = 0; j < all.length; j++){
					double dist = Math.abs(all[j].x - x) + Math.abs(all[j].y - y);
					if (dist < 5){
						return all[j].crc16;
					}
				}
			}
		}
		
		return -1;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
