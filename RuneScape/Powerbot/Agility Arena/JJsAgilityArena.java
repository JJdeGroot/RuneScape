package org.obduro.agilityarena;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;

import org.obduro.agilityarena.nodes.Bank;
import org.obduro.agilityarena.nodes.ClimbUp;
import org.obduro.agilityarena.nodes.Heal;
import org.obduro.agilityarena.nodes.Path;
import org.obduro.agilityarena.nodes.Restore;
import org.obduro.agilityarena.nodes.TagDispenser;
import org.obduro.agilityarena.nodes.Wander;
import org.obduro.agilityarena.utils.GUI;
import org.obduro.agilityarena.utils.General;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;

@Manifest(authors = { "RuneScapeJJ" }, name = "JJ's Agility Arena", description = "Gets tickets at the Agility Arena", version = 2.0)
public class JJsAgilityArena extends ActiveScript implements PaintListener {
	
	private final GUI gui = new GUI();
	private Tree container;

	private static boolean stopScript = false;

	// Sets up the jobs
	public void onStart() {
		System.out.println("JJ's Agility Arena has been started");
		System.out.println("Version: 2.1, released on 16 March 2013");
		
		// Setting up jobs
		Node[] jobs = {new Restore(), new Heal(),
					  new TagDispenser(), new ClimbUp(),
					  new Bank(), new Wander(), new Path()};
		container = new Tree(jobs);
		
		// Setting up GUI
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.setVisible(true);
			}
		});

		while(gui.isShowing()){
			sleep(100, 200);
		}
	}
	
	// Shows the message that the script has ended
	public void onStop() {
		System.out.println("JJ's Agility Arena has been ended");
		System.out.println("Ran for: " + Time.format(System.currentTimeMillis()-General.startTime));
		System.out.println("Gained: " + General.tags + " Agility Arena Tickets");
	}
	
	// Empty's the container to stop the script
	public static void forceStopScript(){
		stopScript = true;
	}
	
	@Override
    public void onRepaint(Graphics g) {
		g.setFont(new Font ("Tahoma", Font.BOLD, 10));
		g.setColor(Color.WHITE);
		g.drawString("JJ's Agility Arena v2.1", 5, 90);
		
		g.drawString("____________________________", 5, 100);
		
		g.drawString("Running for: " + Time.format(System.currentTimeMillis()-General.startTime), 5, 120);
		g.drawString("Agility tickets gained: " + General.tags, 5, 140);
		g.drawString("Per hour: " + General.toHour(General.tags), 5, 160);
		
		g.drawString("____________________________", 5, 175);
		
		g.drawString("My position: " + General.getPlayerTile(), 5, 200);
		g.drawString("My position in grid: " + General.formatToGrid(General.getPlatformGridLocation()), 5, 220);
		
		g.drawString("Arrow location: " + General.getArrowTile(), 5, 240);
		g.drawString("Arrow in grid: " + General.formatToGrid(General.getArrowGridLocation()), 5, 260);

		g.drawString("Path: " + General.getPath(), 5, 280);
		g.drawString("Dispenser: " + General.getDispenserTile(), 5, 300);
	}

	@Override
	public int loop() {
		System.out.println("Mainloop called.");

		while(gui.isShowing()){
			System.out.println("Waiting for the GUI!");
			return Random.nextInt(100, 200);
		}
		
		if(stopScript){
			System.out.println("Terminating script!");
			return -1;
		}

		if(container != null){
			Node job = container.state();
			if (job != null) {
                container.set(job);
                getContainer().submit(job);
                job.join();
			}
		}

		return Random.nextInt(50, 100);
	}
}