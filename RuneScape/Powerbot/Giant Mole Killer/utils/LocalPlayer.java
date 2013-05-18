package org.obduro.mole.utils;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Prayer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;

public class LocalPlayer {

	private static boolean isResting(){
		return Widgets.get(750, 2).getTextureId() == 1794;
	}
	
	private static boolean setRest(){
		return Widgets.get(750, 5).interact("Rest");
	}
	
	public static boolean rest(int percent){
		while(Walking.getEnergy() < percent){
			if(!isResting()){
				setRest();
			}
			Task.sleep(50, 150);
		}

		return false;
	}
	
	public static boolean setRun(){
		if(!Walking.isRunEnabled()){
			Walking.setRun(true);
			return true;
		}else{
			return true;
		}
	}
	
	public static boolean lowPrayer(){
		return Prayer.getPoints() <= 150;
	}
	
	public static Tile getTile(){
		return Players.getLocal().getLocation();
	}
	
	public static boolean inCombat(){
		NPC mole = Mole.getMole();
		if(mole != null){
			return mole.isInCombat() && Players.getLocal().isInCombat();
		}
		return false;
	}
	
	public static boolean isMoving(){
		return Players.getLocal().isMoving();
	}
	
	public static void wait(Tile tile){
		Task.sleep(500, 1500);
		while(isMoving()){
			Tile myPos = getTile();
			double distance = myPos.distance(tile);
			if(distance <= 3){
				break;
			}

			if(Mole.canAttack()){
				break;
			}
			
			handleQuickPray();
			
			Task.sleep(50, 100);
		}
	}
	
	public static void handleQuickPray(){
		Tile tile = Mole.getTile();
		if(tile != null){
			double distance = LocalPlayer.getTile().distance(tile);
			if(distance <= 5){
				if(LocalPlayer.inCombat()){
					Prayer.toggleQuick(true);
				}
			}
		}else{
			Prayer.toggleQuick(false);
		}

		Task.sleep(500, 1000);
	}

}
