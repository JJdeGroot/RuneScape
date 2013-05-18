package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;

import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.api.ScreenModels;
import org.tribot.api.types.ScreenModel;
import org.tribot.api.input.Mouse;
import org.tribot.api.ChooseOption;
import org.tribot.api.Player;
import org.tribot.api.Timing;


    @ScriptManifest(authors = { "J J" }, category = "Agility", name = "JJ's Gnome Runner")
    
    public class JJsGnome2 extends Script implements Painting {
    	final long logID = 4390934L, netID = 1107874667L, branchID = 852209111L, ropeID = 1493690341L,
    			    branchID2 = 1340392253L, pipeID = 347181813L, farmID = 22944L, agilID = 55915L;
    	final long[] notAnimation = {1417827556L, 4390934L, 2457391002L, 949177853L};
    	long startTime = 0, animationID = 0, playerID = 0;
    	String status;

    	public void onPaint(Graphics g){
            g.setColor(Color.YELLOW);
            g.drawString("JJ's Gnome Runner", 5, 100);
            g.setColor(Color.WHITE);
            g.drawString("Time Running: " + Timing.msToString(System.currentTimeMillis() - startTime), 5, 120);
            g.drawString("Player ID: " + playerID, 5, 140);
            g.drawString("Animation ID: " + animationID, 5, 160);
            g.drawString("Status: " + status, 5, 180);
        }
    	
    	private void onStart() {
	        println("JJ's Gnome Runner has been started!");
	        startTime  = System.currentTimeMillis();
	    }
    	
    	private void onStop() {
            println("Thanks for using JJ's Gnome Runner");
            println("Script has ran for: " + Timing.msToString(System.currentTimeMillis() - startTime));
        }
    	
    	private void grabID(String which){
    		if (which.equals("animationID") || which.equals("playerID")){
	    		ScreenModel[] ids = ScreenModels.getAll();
	    		for (int i=0; i<ids.length; i++){
	    			if (ids[i].points[0].x > 220 && ids[i].points[0].x < 310){
	    				if (ids[i].points[0].y > 190 && ids[i].points[0].y < 240){
	    					int k=0;
	    					for (int j=0; j<notAnimation.length; j++){
	    						if (ids[i].id != notAnimation[j]){
	    							k++;
	    		    			}
	    					}
							if (k == notAnimation.length){
								if (which.equals("animationID")){
									if (ids[i].id != playerID)
										animationID = ids[i].id;
								}else{
									playerID = ids[i].id;
								}
								break;
							}
	    				}
	    			}
	    		}
    		}
     	}
        
    	private int randomRange(int aFrom, int aTo){
        	return (aTo - new Random().nextInt((Math.abs(aTo - aFrom) + 1)));
        }
        
        private boolean randomSelectOptionAt(Point coord, String upText, String option, int leftChance, int rightChance, int maxWait){
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
        
        private void waitUntilNotMoving(){
	        sleep(500, 1000);
			while (Player.isMoving()){
				sleep(100, 200);
			}
        }
        
        private void waitUntilNotAnimating(){
        	if (playerID != 0 && animationID != 0){
            	if (ScreenModels.find(animationID).length > 0){
            		sleep(100, 200);
            		waitUntilNotAnimating();
            	}
        	}else{
        		println("PlayerID or AnimationID not defined, stopping script!");
        		stopScript();
        	}
        }
        
        private void sureWait(){
        	waitUntilNotMoving();
        	waitUntilNotAnimating();
        }
        
        private boolean tryToFix(String uptext, Point coord, int xOffsetFrom, int xOffsetTo, int yOffsetFrom, int yOffsetTo){
        	Point oldPos = new Point(Mouse.getPos());
        	while (ChooseOption.getPosition() != null){
        		Point MP = new Point(Mouse.getPos());
        		Mouse.move(MP.x + randomRange(-50, 50), MP.y + randomRange(-50, 50));
        	}
        	if (Mouse.getPos() != oldPos)
        		Mouse.move(oldPos);
        	
        	int attempt = 0;
			while (attempt < 9 && !Timing.waitUptext("Walk", 500)){
				Mouse.move(coord.x+randomRange(xOffsetFrom, xOffsetTo), coord.y+randomRange(yOffsetFrom, yOffsetTo));
				attempt++;
				println(Mouse.getPos());
			}
			if (attempt > 9){
				println("Unable to find uptext, stopping script");
				return false;
			}else{
				return true;
			}
        }
        
        private void logBalance() {
        	status = "Log balance";
			ScreenModel[] log = ScreenModels.find(logID);
			
			if (log.length > 0){
				if (playerID == 0)
					grabID("playerID");
				Point LP = new Point(log[0].base_point.x + randomRange(-3, 3), log[0].base_point.y + randomRange(-3, 3));
				if (!randomSelectOptionAt(LP, "Walk", "Walk", 4, 1, 1000)){
					if (!tryToFix("Walk", LP, -3, 3, -3, 3))
						stopScript();
					randomSelectOptionAt(LP, "Walk", "Walk", 4, 1, 1000);
				}
				if (animationID == 0){
					sleep(2000, 3000);
					grabID("animationID");
				}
				sureWait();
			}
		}
        
        private void obstacleNet() {
        	status = "Obstacle net";
			ScreenModel[] net = ScreenModels.find(netID);
			
			if (net.length >= 3){
				int nr = randomRange(0, 2);
				Point OP = new Point(net[nr].base_point.x + randomRange(-30, 30), net[nr].base_point.y + randomRange(0, 6));
				if (!randomSelectOptionAt(OP, "Climb", "Climb", 1, 1, 1000)){
					if (!tryToFix("Climb", OP, -10, 10, -5, 5))
						stopScript();
					randomSelectOptionAt(OP, "Climb", "Climb", 1, 1, 1000);
				}
				sureWait();
				sleep(500, 1000);
			}
		}
        
        private void treeBranch() {
        	status = "Tree branch";
			ScreenModel[] branch = ScreenModels.find(branchID);
			
			if (branch.length > 0){
				Point BP = new Point(branch[0].points[0].x + randomRange(-2, 2), branch[0].points[0].y + randomRange(-4, 4));
				if (!randomSelectOptionAt(BP, "Climb", "Climb", 4, 1, 1000)){
					if (!tryToFix("Climb", BP, -3, 3, -3, 3))
						stopScript();
					randomSelectOptionAt(BP, "Climb", "Climb", 4, 1, 1000);
				}
				sureWait();
			}
		}
        
        private void balancingRope() {
        	status = "Balancing rope";
			ScreenModel[] rope = ScreenModels.find(ropeID);
			
			if (rope.length > 0){
				int near = 0, nearest = 600;
				for(int i=0; i<rope.length; i++){
					if (rope[i].base_point.x < nearest){
						nearest = rope[i].base_point.x;
						near = i;
					}
				}

				Point RP = new Point(rope[near].points[0].x + randomRange(8, 12), rope[near].points[0].y + 7);
				if (!randomSelectOptionAt(RP, "Walk-on", "Walk-on", 1, 1, 1000)){
					if (!tryToFix("Walk-on", RP, -2, 2, -1, 1))
						stopScript();
					randomSelectOptionAt(RP, "Walk-on", "Walk-on", 1, 0, 1000);
				}
				sureWait();
			}
		}
        
        private void treeBranch2() {
        	status = "Tree branch";
			ScreenModel[] branch = ScreenModels.find(branchID2);
			
			if (branch.length > 0){
				Point BP = new Point(branch[0].points[0].x + randomRange(-4, 0), branch[0].points[0].y + randomRange(-4, 0));
				if (!randomSelectOptionAt(BP, "Climb", "Climb", 1, 1, 1000)){
					if (!tryToFix("Climb", BP, -3, 3, -3, 3))
						stopScript();
					randomSelectOptionAt(BP, "Climb", "Climb", 1, 1, 1000);
				}
				sureWait();
			}
		}
        
        private void toNet2() {
        	status = "Running to net";
        	Point NP = new Point(randomRange(610, 630), randomRange(105, 115));
        	Mouse.move(NP);
        	Mouse.click(NP, 1);
        	sureWait();
        }
        
        private void obstacleNet2() {
        	status = "Obstacle net";
			ScreenModel[] net = ScreenModels.find(netID);
			
			if (net.length >= 3){
				int nr;
				if (net.length > 3){
					ArrayList<Integer> netList = new ArrayList<Integer>();
					for (int i=0; i<net.length; i++){
						if (net[i].points[0].x > 150){
							netList.add(i);
						}
					}
					nr = randomRange(netList.get(0), netList.get(netList.size()-1));
				}else{
					nr = randomRange(0, 2);
				}

				Point NP = new Point(net[nr].base_point.x + randomRange(-30, 30), net[nr].base_point.y - randomRange(5, 20));
				if (!randomSelectOptionAt(NP, "Climb", "Climb", 1, 1, 1000)){
					if (!tryToFix("Climb", NP, -10, 10, -5, 5))
						stopScript();
					randomSelectOptionAt(NP, "Climb", "Climb", 1, 1, 1000);
				}
				sureWait();
				sleep(500, 1000);
			}
		}
        
        private void obstaclePipe() {
        	status = "Obstacle pipe";
			ScreenModel[] pipe = ScreenModels.find(pipeID);
			
			if (pipe.length >= 2){
				int nr = randomRange(0, 1);
				Point PP = new Point(pipe[nr].base_point.x + randomRange(-10, 10), pipe[nr].base_point.y + randomRange(-15, 10));
				if (!randomSelectOptionAt(PP, "Squeeze", "Squeeze", 4, 1, 1000)){
					if (!tryToFix("Squeeze", PP, -5, 5, -5, 5))
						stopScript();
					randomSelectOptionAt(PP, "Squeeze", "Squeeze", 4, 1, 1000);
				}
				sureWait();
			}
		}
        
        private void toLog() {
        	status = "Running to start";
        	Point SP = new Point(randomRange(575, 590), randomRange(125, 135));
        	Mouse.move(SP);
        	Mouse.click(SP, 1);
        	sureWait();
        }
        

        @Override
	    public void run() {
        	onStart();
            logBalance();
            obstacleNet();
            treeBranch();
            balancingRope();
            treeBranch2();
            toNet2();
            obstacleNet2();
            obstaclePipe();
            toLog();
            onStop();
        }
    }