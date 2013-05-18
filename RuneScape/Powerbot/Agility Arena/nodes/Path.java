package org.obduro.agilityarena.nodes;
import java.awt.Point;

import org.obduro.agilityarena.utils.General;
import org.obduro.agilityarena.utils.Obstacle;
import org.obduro.agilityarena.utils.Traverser;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;

public class Path extends Node {

	@Override
	public boolean activate() {
		return Players.getLocal().getPlane() == 3 && 
			   Players.getLocal().getHealthPercent() >= General.healPercentage;
	}

	@Override
	public void execute() {
		Point myPos = General.getPlatformGridLocation();
		Point arrow = General.getArrowGridLocation();
		System.out.println("Arrow in grid: " + arrow);
	
		Point p = General.generateDirection(arrow.x - myPos.x, arrow.y - myPos.y);
		Point myConvertedPos = General.convertLocation(myPos);
		Point onGrid = new Point(myConvertedPos.x+p.x, myConvertedPos.y+p.y);
		
		System.out.println("New Point on grid: " + onGrid);
		Obstacle obstacle = General.grid[onGrid.x][onGrid.y];
		System.out.println("Obstacle: " + obstacle);

		if(obstacle != Obstacle.PLATFORM && obstacle != Obstacle.EMPTY){
			Traverser traverser = new Traverser(obstacle);
			traverser.traverse();
		}else{
			System.out.println("Waiting for arrow to move...");
			Task.sleep(1000, 2000);
		}
	}
}
