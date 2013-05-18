package org.obduro.mole.utils;

import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;

public class Mole {
	private static final int moleID = 3340;
	
	public static NPC getMole(){
		NPC[] moles = NPCs.getLoaded(moleID);
		if(moles.length > 0){
			return moles[0];
		}
		return null;
	}
	
	public static Tile getTile(){
		NPC mole = getMole();
		if(mole != null){
			return mole.getLocation();
		}
		return null;
	}
	
	public static int getHealthPercent(){
		NPC mole = getMole();
		if(mole != null){
			return mole.getHealthPercent();
		}
		return -1;
	}
	
	public static boolean canAttack(){
		NPC mole = getMole();
		if(mole != null){
			return mole.getLocation().distance(LocalPlayer.getTile()) <= 5;
		}
		return false;
	}
	
	public static boolean attack(){
		if(!LocalPlayer.inCombat()){
			NPC mole = getMole();
			if(mole != null){
				double distance = LocalPlayer.getTile().distance(mole);
				if(distance <= 5){
					if(mole.interact("Attack")){
						LocalPlayer.wait(mole.getLocation());
						return true;
					}else{
						Camera.turnTo(mole);
					}
				}
			}
		}
		return false;
	}
	
	
}
