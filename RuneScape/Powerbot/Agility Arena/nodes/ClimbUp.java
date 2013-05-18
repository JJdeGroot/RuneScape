package org.obduro.agilityarena.nodes;

import org.obduro.agilityarena.utils.General;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;

public class ClimbUp extends Node{
	private final int climbingRopeID = 3610;

	@Override
	public boolean activate() {
		return Players.getLocal().getLocation().getPlane() == 0 && General.getArrowTile() != null;
	}

	@Override
	public void execute() {
		System.out.println("ClimbUp node -> activated");
		General.performAction(climbingRopeID, "Climb");
	}
}