package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.api.ScreenModels;
import org.tribot.api.types.MinimapIcon;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.input.Mouse;
import org.tribot.api.Game;
import org.tribot.api.ChooseOption;
import org.tribot.api.Minimap;
import org.tribot.api.Player;

    @ScriptManifest(authors = { "J J" }, category = "Agility", name = "JJ's Gnome Runner")
    
    public class JJsGnome extends Script implements Painting {
    	final long logID = 4390934L, netID = 1107874667L, branchID = 852209111L, ropeID = 1493690341L,
    			    branchID2 = 1340392253L, pipeID = 347181813L, farmID = 22944L, agilID = 55915L;

    	public void onPaint(Graphics g){
            g.setColor(Color.CYAN);
            g.drawString("JJ's Gnome Runner", 5, 100);
            //g.setColor(Color.WHITE);
            //g.drawString("Time Running: " + Timing.msToString(System.currentTimeMillis() - StartTime), 5, 120);
            //g.drawString("Status: " + status, 5, 160);
        }
        
        public void clickMouse(int i) throws Exception{
        	Robot r = new Robot();
        	switch(i){
        	case 1: r.mousePress(InputEvent.BUTTON1_MASK);
        	case 2: r.mousePress(InputEvent.BUTTON2_MASK);
        	case 3: r.mousePress(InputEvent.BUTTON3_MASK);
        	}
        	
        	int humanWait = 0;
        	while (humanWait < 4){
        		sleep(20, 50);
        		humanWait++;
        	}
        	
        	switch(i){
        	case 1: r.mouseRelease(InputEvent.BUTTON1_MASK);
        	case 2: r.mouseRelease(InputEvent.BUTTON2_MASK);
        	case 3: r.mouseRelease(InputEvent.BUTTON3_MASK);
        	}
        }
        
        public int randomRange(int aFrom, int aTo){
        	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
        }
        
        public boolean waitUptext(String upText, int maxWait){
        	int waitDelay = 0;
        	while (!Game.isUptext(upText) && waitDelay < 10){
        		sleep(maxWait/40, maxWait/13);
        		waitDelay++;
        	}
        	if (waitDelay < 10)
        		return true;
        	return false;
        }
        
        public boolean waitOption(String option, int maxWait){
        	int waitDelay = 0;
        	while (!ChooseOption.isOptionValid(option) && waitDelay < 10){
        		sleep(maxWait/40, maxWait/13);
        		waitDelay++;
        	}
        	if (waitDelay < 10)
        		return true;
        	return false;
        }
        
        public boolean randomSelectOptionAt(Point coord, String upText, String option, int leftChance, int rightChance, int maxWait){
        	int totalChance = leftChance + rightChance;
        	Mouse.move(coord);
        	if (waitUptext(upText, (maxWait/2))){
	        	if ((new Random().nextInt(totalChance)) < leftChance){
	        		Mouse.click(coord, 1);
	        		return true;
	        	}else{
	        		Mouse.click(coord, 3);
	        		if (waitOption(option, (maxWait/2))){
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
        
        public void waitUntilNotMoving(){
	        sleep(750, 1250);
			while (Player.isMoving()){
				sleep(50, 150);
			}
			sleep(750, 1250);
        }
        
        private void logBalance() {
			ScreenModel[] log = ScreenModels.find(logID);
			
			if (log.length > 0){
				Point LP = new Point(log[0].base_point.x + randomRange(-3, 3), log[0].base_point.y + randomRange(-3, 3));
				randomSelectOptionAt(LP, "Walk", "Walk", 4, 1, 1000);
				waitUntilNotMoving();
			}
		}
        
        private void obstacleNet() {
			ScreenModel[] net = ScreenModels.find(netID);
			
			if (net.length >= 3){
				int nr = randomRange(0, 2);
				Point OP = new Point(net[nr].base_point.x + randomRange(-30, 30), net[nr].base_point.y + randomRange(0, 6));
				randomSelectOptionAt(OP, "Climb", "Climb", 1, 1, 1000);
				waitUntilNotMoving();
			}
		}
        
        private void treeBranch() {
			ScreenModel[] branch = ScreenModels.find(branchID);
			
			if (branch.length > 0){
				Point BP = new Point(branch[0].points[0].x + randomRange(-2, 2), branch[0].points[0].y + randomRange(-4, 4));
				randomSelectOptionAt(BP, "Climb", "Climb", 4, 1, 1000);
				waitUntilNotMoving();
			}
		}
        
        private void balancingRope() {
			ScreenModel[] rope = ScreenModels.find(ropeID);
			
			if (rope.length > 0){
				int near = 0;
				for(int i=0; i<rope.length; i++){
					int nearest = 500;
					if (rope[i].base_point.x < nearest){
						nearest = rope[i].base_point.x;
						near = i;
					}
				}
				Point RP = new Point(rope[near].points[0].x + randomRange(10, 16), rope[near].points[0].y + 7);
				randomSelectOptionAt(RP, "Walk", "Walk", 1, 1, 1000);
				waitUntilNotMoving();
			}
		}
        
        private void treeBranch2() {
			ScreenModel[] branch = ScreenModels.find(branchID2);
			
			if (branch.length > 0){
				Point BP = new Point(branch[0].points[0].x + randomRange(-4, 0), branch[0].points[0].y + randomRange(-4, 0));
				randomSelectOptionAt(BP, "Climb", "Climb", 4, 1, 1000);
				waitUntilNotMoving();
			}
		}
        
        private void toNet2() {
        	Point NP = new Point(randomRange(610, 630), randomRange(105, 115));
        	Mouse.move(NP);
        	Mouse.click(NP, 1);
        	waitUntilNotMoving();
        }
        
        private void obstacleNet2() {
			ScreenModel[] net = ScreenModels.find(netID);
			
			if (net.length >= 3){
				int nr = randomRange(0, 2);
				Point NP = new Point(net[nr].base_point.x + randomRange(-30, 30), net[nr].base_point.y - randomRange(5, 20));
				randomSelectOptionAt(NP, "Climb", "Climb", 1, 1, 1000);
				waitUntilNotMoving();
			}
		}
        
        private void obstaclePipe() {
			ScreenModel[] pipe = ScreenModels.find(pipeID);
			
			if (pipe.length >= 2){
				int nr = randomRange(0, 1);
				Point PP = new Point(pipe[nr].base_point.x + randomRange(-10, 10), pipe[nr].base_point.y + randomRange(-15, 10));
				randomSelectOptionAt(PP, "Squeeze", "Squeeze", 4, 1, 1000);
				waitUntilNotMoving();
			}
		}
        
        private void toLog() {
        	MinimapIcon[] agilSymbol = Minimap.findIcons(agilID);
        	if (agilSymbol.length > 0){
            	Point AP = new Point(agilSymbol[0].x + randomRange(10, 30), agilSymbol[0].y + randomRange(-5, 5));
            	Mouse.move(AP);
            	Mouse.click(AP, 1);
            }else{
            	MinimapIcon[] farmSymbol = Minimap.findIcons(farmID);
                if (farmSymbol.length > 0){
                	Point FP = new Point(farmSymbol[0].x - randomRange(5, 15), farmSymbol[0].y + randomRange(35, 50));
                	Mouse.move(FP);
                	Mouse.click(FP, 1);
                	
            	}else{
            		println("shutdown. error.");
            	}
            }
        	waitUntilNotMoving();
        }
        
        

        @Override
	    public void run() {
            println("Testing");
            logBalance();
            obstacleNet();
            treeBranch();
            balancingRope();
            treeBranch2();
            toNet2();
            obstacleNet2();
            obstaclePipe();
            toLog();
        }
    }