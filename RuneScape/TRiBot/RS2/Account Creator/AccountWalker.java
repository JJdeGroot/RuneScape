package scripts;

import java.awt.Point;
import java.awt.Rectangle;

import org.tribot.api.Constants;
import org.tribot.api.EGW;
import org.tribot.api.GameTab;
import org.tribot.api.GameTab.TABS;
import org.tribot.api.Player;
import org.tribot.api.Textures;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.Texture;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = { "J J" }, category = "Private", name = "JJ's Account Walker")
public class AccountWalker extends Script{
	final Point[] pathToGE = {new Point(3234, 3221), new Point(3234, 3221), new Point(3238, 3223), new Point(3242, 3225), new Point(3248, 3225), new Point(3250, 3229), new Point(3249, 3235), new Point(3248, 3241), new Point(3251, 3248), new Point(3252, 3253), new Point(3252, 3260), new Point(3250, 3266), new Point(3246, 3272), new Point(3242, 3278), new Point(3240, 3285), new Point(3240, 3291), new Point(3239, 3298), new Point(3238, 3304), new Point(3238, 3311), new Point(3239, 3317), new Point(3239, 3324), new Point(3239, 3331), new Point(3237, 3334), new Point(3232, 3336), new Point(3227, 3341), new Point(3227, 3348), new Point(3226, 3354), new Point(3221, 3362), new Point(3217, 3369), new Point(3214, 3376), new Point(3211, 3381), new Point(3211, 3381), new Point(3211, 3382), new Point(3211, 3389), new Point(3211, 3395), new Point(3211, 3401), new Point(3209, 3406), new Point(3203, 3413), new Point(3198, 3420), new Point(3194, 3426), new Point(3190, 3428), new Point(3182, 3428), new Point(3178, 3434), new Point(3177, 3440), new Point(3177, 3446), new Point(3173, 3454), new Point(3168, 3461), new Point(3166, 3467)};
	final Point[] pathToSW = {new Point(3165, 3468), new Point(3158, 3469), new Point(3153, 3472), new Point(3149, 3476)};
	final Point[] pathToNW = {new Point(3165, 3467), new Point(3159, 3474), new Point(3156, 3481), new Point(3153, 3487), new Point(3150, 3493), new Point(3148, 3500), new Point(3147, 3505)};
	final Point[] pathToSE = {new Point(3165, 3468), new Point(3167, 3468), new Point(3173, 3471), new Point(3179, 3476)};
	
	String status = "Starting";

	private boolean openMagicTab(){
		TABS open = GameTab.getOpen();
		
		if(!open.equals(TABS.MAGIC)){
			if(GameTab.open(TABS.MAGIC)){
				sleep(500, 1000);
				return true;
			}
		}else{
			return true;
		}
		
		return false;
	}
	
	private Point ptInRect(Rectangle r){
		return new Point(r.x + Constants.RANDOM.nextInt(r.width), r.y + Constants.RANDOM.nextInt(r.height));
	}
	
	private boolean clickTexture(int id, int index, int clickType){
		Texture[] textures = Textures.find(id);
		if(textures.length > index){
			Rectangle r = new Rectangle(textures[index].x, textures[index].y, textures[index].width, textures[index].height);
			Point p = ptInRect(r);
			Mouse.move(p);
			Mouse.click(clickType);
			sleep(500, 1000);
			return true;
		}
		return false;
	}
	
	private boolean clickOnMagic(){
		status = "Clicking on magic spells";
		if(GameTab.getOpen().equals(TABS.MAGIC)){
			return clickTexture(9523, 0, 1);
		}
		return false;
	}
	
	private boolean clickOnTeleports(){
		status = "Clicking on teleport spells";
		if(GameTab.getOpen().equals(TABS.MAGIC)){
			return clickTexture(21468, 0, 1);
		}
		return false;
	}
	
	private boolean clickOnHomeTele(){
		status = "Clicking on lodestone teleport";
		if(GameTab.getOpen().equals(TABS.MAGIC)){
			return clickTexture(21468, 0, 1);
		}
		return false;
	}
	
	private boolean lodestoneScreen(){
		return Textures.find(34370).length > 0;
	}
	
	private boolean waitLodestoneScreen(int time){
		status = "Waiting for lodestone screen";
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < time){
			if(lodestoneScreen()){
				return true;
			}
			sleep(50, 100);
		}
		return false;
	}
	
	private boolean getToLumbridge(){
		if(openMagicTab()){
			if(clickOnMagic()){
				if(clickOnTeleports()){
					if(clickOnHomeTele()){
						if(waitLodestoneScreen(5000)){
							// Clicking on lumbridge in the screen
							Mouse.clickBox(319, 281, 330, 295, 1);
							
							// Waiting until we arrived
							status = "Waiting until we are in Lumbridge";
							Point lumby = new Point(3234, 3221);
							long t = System.currentTimeMillis();
							
							while(Timing.timeFromMark(t) < 25000){
								Point myPos = EGW.getPosition();
								
								double distance = Math.abs(myPos.x - lumby.x) + Math.abs(myPos.y - lumby.y);
								if(distance <= 5){
									status = "Arrived in Lumbridge";
									println("Distance < 5");
									sleep(5000, 6000);
									return true;
								}
								
								sleep(500, 1000);
							}
						}
					}
				}
			}
		}
		return false;
	}

	private void printPositions(){
		long t = System.currentTimeMillis();
		String print = "";
		while(Timing.timeFromMark(t) < 1000000){
			Point p = EGW.getPosition();
			print = print + "new Point(" + p.x + ", " + p.y + "), ";
			println(print);
			sleep(2000);
		}
	}
	
	private boolean walkToGE(){
		if(EGW.walkPath(pathToGE)){
			while(Player.isMoving()){
				sleep(100, 200);
			}
			return true;
		}
		return false;
	}
	
	private boolean walkToBooth(){
		int i = Constants.RANDOM.nextInt(3);
		switch(i){
			case 0: return EGW.walkPath(pathToNW);
			case 1: return EGW.walkPath(pathToSW);
			case 2: return EGW.walkPath(pathToSE);
		}
		return false;
	}
	
	private void logout(){
		Mouse.clickBox(750, 54, 760, 66, 1);
		sleep(500, 1000);
		Mouse.clickBox(574, 446, 712, 467, 1);
		sleep(2000, 3000);
	}
	
	public boolean mainloop(){
		long t = System.currentTimeMillis();
		while(Timing.timeFromMark(t) < 300000){ // 5 mins
			if(getToLumbridge()){
				if(walkToGE()){
					if(walkToBooth()){
						logout();
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
    public void run() {		
		//mainloop();
		printPositions();
    }
}