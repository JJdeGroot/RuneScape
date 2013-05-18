package org.obduro.mole;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.util.Random;

import org.obduro.mole.nodes.Attack;
import org.obduro.mole.nodes.Heal;
import org.obduro.mole.nodes.Pray;
import org.obduro.mole.nodes.Search;
import org.obduro.mole.utils.LocalPlayer;
import org.obduro.mole.utils.Mole;

@Manifest(authors = { "RuneScapeJJ" }, name = "JJ's Giant Mole Killer", description = "Kills the Giant Mole")
public class JJsGiantMoleKiller extends ActiveScript implements PaintListener {
	private Tree container;
	private List<Node> jobs;

	public void onStart() {
		System.out.println("JJ's Giant Mole Killer has been started");
		
		jobs = new ArrayList<Node>();
		jobs.add(new Heal());
		jobs.add(new Pray());
		jobs.add(new Attack());
		jobs.add(new Search());
		
		container = new Tree(jobs.toArray(new Node[jobs.size()]));
	}
		
	public void onStop() {
		System.out.println("JJ's Giant Mole Killer has been ended");
	}

	@Override
	public void onRepaint(Graphics g) {
		g.drawString("My location: " + LocalPlayer.getTile(), 5, 90);
		g.drawString("Mole location: " + Mole.getTile(), 5, 110);
	}
	
	@Override
	public int loop() {
		System.out.println("Mainloop");

		
		//LocalPlayer.handleQuickPray();
		//Task.sleep(5000);
		//System.out.println("Done");
		
		if(container != null){
			final Node job = container.state();
			if (job != null) {
                container.set(job);
                getContainer().submit(job);
                job.join();
			}
		}

		return Random.nextInt(100, 200);
	}
}