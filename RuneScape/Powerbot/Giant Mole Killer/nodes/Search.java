package org.obduro.mole.nodes;

import org.obduro.mole.General;
import org.obduro.mole.utils.LocalPlayer;
import org.obduro.mole.utils.Mole;
import org.obduro.mole.utils.Path;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.wrappers.Tile;

public class Search extends Node {

	@Override
	public boolean activate() {
		System.out.println("Search called");
		return System.currentTimeMillis()-General.getKillTime() >= 120000 && !Mole.canAttack();
	}

	@Override
	public void execute() {
		System.out.println("Need to search the mole");
		LocalPlayer.handleQuickPray();

		Tile moleTile;
		while((moleTile = Mole.getTile()) == null){
			Path.walk();
		}
	
		moleTile.clickOnMap();
		LocalPlayer.wait(moleTile);
		
		System.out.println("Found mole!");
		
	}
	
}
