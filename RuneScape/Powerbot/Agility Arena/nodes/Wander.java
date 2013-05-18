package org.obduro.agilityarena.nodes;

import java.awt.Point;

import org.obduro.agilityarena.utils.General;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Settings;

public class Wander extends Node {
	final Point mp = new Point(2, 2); // middle point
	
	@Override
	public boolean activate() {
		return Settings.get(1613) == 6 || Settings.get(1613) == 15; // TAGGED!
	}

	@Override
	public void execute() {
		System.out.println("Wander activated!!");
		
		General.walkTowards(mp);
	}
}