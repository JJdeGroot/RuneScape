package scripts;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import org.tribot.api.Screen;
import org.tribot.api.ScreenModels;
import org.tribot.api.Textures;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.types.Texture;
import org.tribot.api.types.colour.ColourPoint;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.script.Script;

public class Monsters extends Script {
	General gen = new General();
	
	
	// FROZEN MONSTERS //
	final long[] FROZEN_FORGOTTEN_MAGE = {3561310148L, 1326036789L, 1597212118L, 1681796192L, 1969115135L, 3744374570L, 1969115135L,
										  3925667173L, 1688567927L};
	final long[] FROZEN_FORGOTTEN_RANGER = {3545007793L, 542885332L, 2843750077L, 831055358L, 3545007793L, 594794251L, 1167093962L,
											1085661053L};
	final long[] FROZEN_FORGOTTEN_WARRIOR = {1397758839L, 4007478432L, 1271086756L, 3364414828L, 3555221883L, 3202713522L, 674224873L, 
											 2958639544L, 3539948262L, 1802395580L, 3509779262L, 1185561219L, 3790085060L, 110017228L,
											 18653500308L, 2049146926L, 2422936825L, 3053080153L, 3367617953L, 616848619L, 57600827L, 
											 83473977L, 1250647707L, 1856568072L, 548461974L, 3990261294L, 1692443119L, 2768752823L,
											 105900281L, 1115415981L, 4175087512L, 3538258775L, 1863500308L, 4141903060L};
	final long[] FROZEN_GIANT_RAT = {2122423973L, 146161673L, 2958331695L, 1538366387L, 252909303L, 1541225756L, 2958331695L};
	final long FROZEN_HYDRA = 236354172L,
			   FROZEN_ICE_ELEMENTAL = 2846886962L,
			   FROZEN_ICE_GIANT = 2637943098L,
			   FROZEN_ICE_SPIDER = 1253199262L,
			   FROZEN_ICE_WARRIOR = 1589511840L,
			   FROZEN_ICE_ICEFIEND = 3463791966L,
			   FROZEN_MYSTERIOUS_SHADE = 4255391196L,
			   FROZEN_PROTOMASTYX = 2459750624L,
			   FROZEN_THROWER_TROLL = 699355667L;
	final long[][] FROZEN_MONSTERS = {FROZEN_FORGOTTEN_MAGE, FROZEN_FORGOTTEN_RANGER, FROZEN_FORGOTTEN_WARRIOR, FROZEN_GIANT_RAT,
									  {FROZEN_HYDRA, FROZEN_ICE_ELEMENTAL, FROZEN_ICE_GIANT, FROZEN_ICE_SPIDER, 
									   FROZEN_ICE_WARRIOR, FROZEN_ICE_ICEFIEND, FROZEN_MYSTERIOUS_SHADE, FROZEN_PROTOMASTYX, 
									   FROZEN_THROWER_TROLL}};
	
	// OTHERS //
	int npcID = -1;
	
	/*
	public Point[] findMonsters() {
		Color npc = new Color(235, 235, 10);
		ArrayList<Point> npcList = new ArrayList<Point>();
		Point[] coords = gen.getRoom();

		for (int i = 0; i < coords.length; i++) {
			int x = coords[i].x;
			int y = coords[i].y;
			ColourPoint[] find = Screen.findColours(npc, x, y, x, y, new Tolerance(15));
			if (find.length > 0) {
				npcList.add(new Point(x, y));
			}
		}

		if (!npcList.isEmpty()) {
			return npcList.toArray(new Point[npcList.size()]);
		}

		return new Point[0];
	}
	*/
	
	public boolean findMonsters(){
		Point[] coords = gen.getRoom();
    	Texture[] all = Textures.getAll();

    	for (int i = 0; i < coords.length; i++){
    		for (int j = 0; j < all.length; j++){
    			if (all[j].crc16 == npcID){
	    			Point P = new Point(all[j].x, all[j].y);
	    			if (coords[i].x == P.x && coords[i].y == P.y){
	    				println("Found npc");
	    				return true;
	    			}
    			}
    		}
    	}
    	
    	return false;
	}
	
	public boolean walkToMonsters(){
    	Point[] coords = gen.getRoom();
    	Texture[] all = Textures.getAll();
    	ArrayList<Point> npcList = new ArrayList<Point>();
    	Point MMc = new Point(627, 135);

    	for (int i = 0; i < coords.length; i++){
    		for (int j = 0; j < all.length; j++){
    			if (all[j].crc16 == npcID){
	    			Point P = new Point(all[j].x, all[j].y);
	    			if (coords[i].x == P.x && coords[i].y == P.y){
	    				println("Found npc");
	    				npcList.add(P);
	    				break;
	    			}
    			}
    		}
    	}
    	
    	if (!npcList.isEmpty()){
    		Point[] pts = npcList.toArray(new Point[npcList.size()]);
    		Point K = gen.getNearestMM(pts);
    		
    		double dist = Math.abs(K.x - MMc.x) + Math.abs(K.y - MMc.y);
    		if (dist > 15){
	    		Mouse.click(K, 1);
	    		gen.waitUntilNotMoving();
    		}
    		
    		return true;
    	}
    	
    	return false;
    }

	public void killMonsters() {
		Point MSc = new Point(259, 220);
		for (int i = 0; i < FROZEN_MONSTERS.length; i++) {
			ScreenModel[] monsters = ScreenModels.find(FROZEN_MONSTERS[i]);
			if (monsters.length > 0) {
				println("detected monsters");

				int nearest = 1000;
				int best = -1;
				for (int j = 0; j < monsters.length; j++) {
					int distance = Math.abs(monsters[j].base_point.x - MSc.x)
							+ Math.abs(monsters[j].base_point.y - MSc.y);
					if (distance < nearest) {
						nearest = distance;
						best = j;
					}
				}

				if (best != -1) {
					Point MP = new Point(monsters[best].base_point.x + gen.randomRange(-5, 5), monsters[best].base_point.y + gen.randomRange(-5, 5));
					Mouse.move(MP);
					if (Timing.waitUptext("Attack", 500)){
						Mouse.click(1);

						long temp = monsters[best].id;
						int length = ScreenModels.find(temp).length;
						while (ScreenModels.find(temp).length > (length - 1)) {
							sleep(50, 150);
							switch (gen.randomRange(0, 100)) {
							case 0:
								ScreenModel[] find = ScreenModels.find(temp);
								if (find.length > 0) {
									Point TP = find[0].base_point;
									Mouse.move(TP);
									if (Timing.waitUptext("Attack", 500)) {
										Mouse.click(1);
									}
								}
							}
						}
						break;
					}
				}
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
