package org.obduro.barbassault;

import java.awt.Graphics;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Equipment.Slot;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.Item;

import org.obduro.barbassault.attacker.ActionBar;
import org.obduro.barbassault.attacker.Kill;
import org.obduro.barbassault.collector.Hopper;
import org.obduro.barbassault.collector.Loot;
import org.obduro.barbassault.game.Call;
import org.obduro.barbassault.game.IngameTeam;
import org.obduro.barbassault.healer.Heal;
import org.obduro.barbassault.healer.Poison;


@Manifest(authors = { "RuneScapeJJ" }, name = "JJ's Barbarian Assault", description = "Plays Barbarian Assault", version = 1.0)
public class JJsBarbAssault extends ActiveScript implements PaintListener {

	private Tree container;
	
	@Override
	public void onRepaint(Graphics g) {
		g.drawString("JJ's Barbarian Assault", 10, 80);
	}
	
	public void onStart(){
		System.out.println("JJ's Barbarian Assault has been started");
		
		Node[] jobs = {new Call(), new Loot(), new Hopper()}; // COLLECTOR
		//Node[] jobs = {new Call(), new Poison()}; // HEALER
		//Node[] jobs = {new Call(), new Kill()}; // ATTACKER
		container = new Tree(jobs);
	}
	

	@Override
	public int loop() {
		System.out.println("Loop");

		//System.out.println("TeamMate #1: health " +  Heal.TeamMate.ONE.getCurrentHealth());
		//System.out.println("TeamMate #1: max health " +  Heal.TeamMate.ONE.getMaximumHealth());
		//System.out.println("TeamMate #1: name " +  Heal.TeamMate.ONE.getName());
		
		
		
		//System.out.println("Actionbar open: " + ActionBar.isOpen());
		
		//ActionBar.close();
		//ActionBar.open();
		
		//System.out.println("Slot for agressive: " + ActionBar.Ability.AGRESSIVE.getSlot());
		//ActionBar.Slot.ONE.activate(true);
		//Task.sleep(2000);
		
		/*
		String[] healerNames = Team.Roles.HEALER.getNames();
		for(String name : healerNames){
			System.out.println("Healer: " + name);
		}
		*/
		
		if(container != null){
			Node job = container.state();
			if (job != null) {
                container.set(job);
                getContainer().submit(job);
                job.join();
			}
		}		
		
		return Random.nextInt(100, 200);
	}

	

}
