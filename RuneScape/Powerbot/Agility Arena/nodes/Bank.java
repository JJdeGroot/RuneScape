package org.obduro.agilityarena.nodes;

import java.awt.Point;

import org.obduro.agilityarena.JJsAgilityArena;
import org.obduro.agilityarena.utils.General;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class Bank extends Node {

	final Point exitPoint = new Point(4, 0);
	final int ladderID = 3618;
	
	@Override
	public boolean activate() {
		return General.bankingEnabled; // NEED TO ADD MORE CHECKS
	}
	
	// Returns the distance to the ladder
	private double distanceToLadder(){
		Tile myPos = General.getPlayerTile();
		SceneObject ladderObject = SceneEntities.getNearest(ladderID);
		if(ladderObject != null){
			return ladderObject.getLocation().distance(myPos);
		}
		return 100;
	}
	
	// Attempts to get out of the arena
	private boolean getOutOfArena(){
		long t = System.currentTimeMillis();
		
		while(General.getPlayerTile().getPlane() == 3){
			if(System.currentTimeMillis() - t > 120000){
				System.out.println("Failed to get out of arena within 2 minutes, terminating script!");
				Game.logout(true);
				JJsAgilityArena.forceStopScript();
			}
			
			if(distanceToLadder() <= 7){
				if(General.performAction(ladderID, "Climb-up")){
					return true;
				}
			}
			General.walkTowards(exitPoint);
		}
	
		return true;
	}

	@Override
	public void execute() {
		System.out.println("WE NEED TO BANK!!");
		getOutOfArena();
		
		System.out.println("Sorry, banking is not fully functional yet, terminating script!");
		Game.logout(true);
		JJsAgilityArena.forceStopScript();
	}

}
