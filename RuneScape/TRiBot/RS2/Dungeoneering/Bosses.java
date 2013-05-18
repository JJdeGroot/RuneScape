package scripts;

import java.awt.Color;
import java.awt.Point;

import org.tribot.api.Screen;
import org.tribot.api.TPS;
import org.tribot.api.Constants;
import org.tribot.api.DTMs;
import org.tribot.api.Minimap;
import org.tribot.api.ScreenModels;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.DTM;
import org.tribot.api.types.colour.DTMPoint;
import org.tribot.api.types.colour.DTMSubPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;

public class Bosses extends Script {
	General gen = new General();
	
	// FROZEN BOSSES //
	final long FROZEN_GLUTTONOUS_BEHEMOTH = 2081911231L,
			   FROZEN_BEHEMOTH_FOOD_SOURCE = 1561638623L,
			   FROZEN_ASTEA_FROSTWEB = 2889928543L,
			   FROZEN_ASTEA_ICE = 1711357207L,
			   FROZEN_ICY_BONES = 3076383209L,
			   FROZEN_LUMINESCENT_ICEFIEND = 4157604468L,
			   FROZEN_ICEFIEND_ICE_PARTICLE = 1360992063L,
			   FROZEN_ICEFIEND_IN_ICE = 3090192007L,
			   FROZEN_PLANEFREEZER_LAKHRAHNAZ = 16807696L,
			   FROZEN_TO_KASH_THE_BLOODCHILLER = 159919581L,
			   FROZEN_KASH_IN_ICE =  1969090180L;
	final long[] FROZEN_BOSSES = {FROZEN_GLUTTONOUS_BEHEMOTH, FROZEN_BEHEMOTH_FOOD_SOURCE, FROZEN_ASTEA_FROSTWEB, 
								  FROZEN_ICY_BONES, FROZEN_LUMINESCENT_ICEFIEND, FROZEN_ICEFIEND_ICE_PARTICLE, 
								  FROZEN_ICEFIEND_IN_ICE, FROZEN_PLANEFREEZER_LAKHRAHNAZ, FROZEN_TO_KASH_THE_BLOODCHILLER};
	public boolean detectBossRoom(){
		// only one door = boss room;
		return false;
	}
	
	private Point southOf(Point P){
		float angle = Minimap.getRotationAngle();
		println("Rotation angle: " + angle);
		
		double rad = (angle / 360 * 2 * Math.PI);
		println("Rad: " + rad);

		double x = Math.sin(rad) * 50;
		double y = Math.cos(rad) * 50;

		println("X: " + x);
		println("Y: " + y);
		
		Point PP = new Point(P.x + (int)x, P.y + (int)y);
		
		return PP;
	}
	
	public void behemoth(){
		long bossID = 2081911231L, foodSourceID = 1561638623L;
		Point MSC = new Point(259, 220);

		while (ScreenModels.find(bossID).length > 0){
			ScreenModel[] boss = ScreenModels.find(bossID);
			ScreenModel[] food = ScreenModels.find(foodSourceID);
			if (food.length > 0){
				int foodDist = Math.abs(MSC.x - food[0].base_point.x) + Math.abs(MSC.y - food[0].base_point.y);
				println("Food dist: " + foodDist);
				if (foodDist < 110){
					Point BP = new Point(boss[0].points[0]);
					Mouse.move(BP);
					if (Timing.waitUptext("behemoth", 500)){
						Mouse.click(BP, 1);
						sleep(5000, 10000);
					}
				}else{
					println("We need to move near the food source!");
					Point FP = new Point(food[0].base_point);
					Point FPS = southOf(FP);
					Mouse.move(FPS);
					Mouse.click(1);
					gen.waitUntilNotMoving();
				}
			}
		}
	}
	
	public boolean detectProtection(String which){
		int MSX1 = Constants.MSX1, MSY1 = Constants.MSY1, MSX2 = Constants.MSX2, MSY2 = Constants.MSY2;

		DTMPoint DTM_PT_0 = new DTMPoint(new Color(65, 64, 64), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_0 = { new DTMSubPoint(new ColourPoint(new Point(2, -8), new Color( 249, 247, 0)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(7, -3), new Color( 249, 247, 0)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(-8, 8), new Color( 189, 188, 191)), new Tolerance(10, 10, 10),1)};
		DTM protectMelee = new DTM(DTM_PT_0, DTM_PTS_0);
		
		DTMPoint DTM_PT_1 = new DTMPoint(new Color(63, 45, 10), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_1 = { new DTMSubPoint(new ColourPoint(new Point(5, -7), new Color( 36, 123, 9)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(-9, 9), new Color( 131, 45, 7)), new Tolerance(10, 10, 10),1)};
		DTM protectRange = new DTM(DTM_PT_1, DTM_PTS_1);
		
		DTMPoint DTM_PT_2 = new DTMPoint(new Color(131, 45, 7), new Tolerance(10, 10, 10));
		DTMSubPoint [] DTM_PTS_2 = { new DTMSubPoint(new ColourPoint(new Point(-7, 7), new Color( 249, 247, 0)), new Tolerance(10, 10, 10),1), new DTMSubPoint(new ColourPoint(new Point(7, -7), new Color( 249, 247, 0)), new Tolerance(10, 10, 10),1)};
		DTM protectMage = new DTM(DTM_PT_2, DTM_PTS_2);
		
		DTM[] protect = {protectMelee, protectRange, protectMage};
		
		String[] options = {"melee", "range", "mage"};		
		for (int i = 0; i < protect.length; i++){
			Point[] detect = DTMs.find_simple(protect[i], MSX1, MSY1, MSX2, MSY2);
			if (detect.length > 0){
				switch(i){
					case 0: println("Detected protect from melee");
							if (which.equals(options[i])){
								return true;
							}
							break;
					case 1: println("Detected protect from range");
							if (which.equals(options[i])){
								return true;
							}
							break;
					case 2: println("Detected protect from mage");
							if (which.equals(options[i])){
								return true;
							}
							break;
				}
			}
		}
		
		return false;
	}
	
	public void astea(){
		long bossID = FROZEN_ASTEA_FROSTWEB,
			 spider = 1253199262L,
			 ice = FROZEN_ASTEA_ICE;
		
		ScreenModel[] boss = ScreenModels.find(bossID);
		if (boss.length > 0){
			Point BP = new Point(boss[0].base_point);
			if (gen.randomSelectOptionAt(BP, "Astea", "Attack", 2, 1, 1000)){
				while(ScreenModels.find(bossID).length > 0){
					
					// Check if astea is praying melee
					while (!detectProtection("melee") && ScreenModels.find(ice).length == 0 && ScreenModels.find(bossID).length > 0){
						println("sleeping");
						sleep(50, 150);
					}
					
					ScreenModel[] iceblock = ScreenModels.find(ice);
					if (iceblock.length > 0){
						println("We need to attack astea again");
					}
					
					if (detectProtection("melee")){
						Point P = gen.getNearestPoint(spider);
						println("P: " + P);
						if (P != null){
							if (gen.randomSelectOptionAt(BP, "spider", "Attack", 1, 0, 1000)){
								int length = ScreenModels.find(spider).length;
								long max = System.currentTimeMillis() + gen.randomRange(4000, 6000);
								while (ScreenModels.find(spider).length == length && System.currentTimeMillis() < max && detectProtection("melee")){
									println("Astea is praying, killing spiders");
									sleep(50, 150);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void icyBones(){
		long bossID = FROZEN_ICY_BONES;
		
		//gen.walkToMiddle();
		
		ScreenModel[] boss = ScreenModels.find(bossID);
		if (boss.length > 0){
			Point BP = new Point(boss[0].base_point);
			if (gen.randomSelectOptionAt(BP, "Icy bones", "attack", 1, 1, 1000)){
				while(ScreenModels.find(bossID).length > 0){
					sleep(50, 100);
					switch(gen.randomRange(0, 50)){
						case 0: Point PP = ScreenModels.find(bossID)[0].base_point;
								Mouse.move(PP);
								break;
					}
					
				}
			}
		}
	}
	
	public boolean bossFlag(){
		Point[] coords = gen.getRoom();
		Color flag = new Color(225, 10, 30);
		int counted = 0;
		
		
		for (int i = 0; i < coords.length; i++){
			ColourPoint[] color = Screen.findColours(flag, coords[i].x, coords[i].y, coords[i].x, coords[i].y, new Tolerance(20));
			counted = counted + color.length;
		}
		if (counted > 5){
			return true;
		}
		return false;
	}
	
	public void particleRun(){
		Point[] coords = gen.getRoom();
		Point MMc = new Point(627, 135),
		      furthest = new Point(-1, -1);
		int distance = 0;
		
		
		for (int i = 0; i < coords.length; i++){
			int dist = Math.abs(coords[i].x - MMc.x + Math.abs(coords[i].y - MMc.y));
			if (dist > distance){
				distance = dist;
				furthest = coords[i];
			}
		}
		
		Mouse.move(furthest);
		Mouse.click(1);
		while(bossFlag()){
			sleep(10, 20);
		}
	}
	
	public void icefiend(){
		long bossID = FROZEN_LUMINESCENT_ICEFIEND,
			 particle = FROZEN_ICEFIEND_ICE_PARTICLE,
			 frozen = FROZEN_ICEFIEND_IN_ICE;
		
		//gen.walkToMiddle();
		
		ScreenModel[] boss = ScreenModels.find(bossID);
		if (boss.length > 0){
			Point BP = new Point(boss[0].base_point);
			if (gen.randomSelectOptionAt(BP, "icefiend", "Attack", 1, 1, 1000)){
				while(ScreenModels.find(particle).length < 3 && ScreenModels.find(frozen).length == 0){
					println("sleeping");
					sleep(50, 100);
				}
				
				while (gen.waitFindID(particle, 100) || gen.waitFindID(frozen, 100)){
					particleRun();
				}
			}
		}
	}
	
	private int distanceToNPC(){
		Point MMc = new Point(627, 135);
		int distance = 0; //Math.abs(NPC.x - MMc.x) + Math.abs(NPC.y - MMc.y);
		return distance;
	}
	
	public void planefreezer(){
		long bossID = FROZEN_PLANEFREEZER_LAKHRAHNAZ;
		
		
		ScreenModel[] boss = ScreenModels.find(bossID);
		if (boss.length > 0){
			Point BP = new Point(boss[0].base_point);
			if (gen.randomSelectOptionAt(BP, "freezer", "Attack", 1, 1, 1000)){
				while (distanceToNPC() < 10){
					sleep(50, 150);
				}
				
				
				
			}
			
		}
		
	}
	
	public void bloodchiller(){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
