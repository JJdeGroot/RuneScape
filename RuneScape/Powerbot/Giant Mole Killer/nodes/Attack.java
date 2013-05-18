package org.obduro.mole.nodes;

import org.obduro.mole.utils.LocalPlayer;
import org.obduro.mole.utils.Mole;
import org.powerbot.core.script.job.state.Node;

public class Attack extends Node {

	@Override
	public boolean activate() {
		System.out.println("Attack called");
		return Mole.canAttack();
	}

	@Override
	public void execute() {
		System.out.println("We can attack the Mole");

		Mole.attack();
		LocalPlayer.handleQuickPray();
		
		
	}

	
	
}
