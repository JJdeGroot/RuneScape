package org.obduro.agilityarena.nodes;

import java.awt.Point;

import org.obduro.agilityarena.utils.General;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;

public class TagDispenser extends Node {
	private final int[] dispenserIDs = {3608, 3581};
	
	@Override
	public boolean activate() {
		System.out.println("Settings: " + Settings.get(1613));
		System.out.println("arrow loc: " + General.getArrowGridLocation());
		System.out.println("my loc: " + General.getPlatformGridLocation());
		Point arrow = General.getArrowGridLocation();
		Point me = General.getPlatformGridLocation();
		return arrow != null && me != null && arrow.x == me.x && arrow.y == me.y && Settings.get(1613) != 6 && Settings.get(1613) != 15;
	}

	@Override
	public void execute() {
		System.out.println("TagDispenser node -> activated");

		System.out.println("Need to tag!");
		if(General.performAction(dispenserIDs, "Tag")){
			Timer timer = new Timer(Random.nextInt(1500, 2500));
			while(timer.isRunning()){
				if(Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1){
					timer.reset();
				}
				Task.sleep(50, 100);
				if(Settings.get(1613) == 6){
					General.tags++;
					break;
				}
			}
		}
		
	}
}