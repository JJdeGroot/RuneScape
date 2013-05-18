package org.obduro.runecrafter;

import java.awt.Color;
import java.awt.Graphics;

import org.obduro.runecrafter.gui.GUI;
import org.obduro.runecrafter.methods.Banking;
import org.obduro.runecrafter.methods.RuneCraft;
import org.obduro.runecrafter.methods.Walk;
import org.obduro.runecrafter.paths.AirPath;
import org.obduro.runecrafter.paths.BodyPath;
import org.obduro.runecrafter.paths.EarthPath;
import org.obduro.runecrafter.paths.FirePath;
import org.obduro.runecrafter.paths.MindPathEdge;
import org.obduro.runecrafter.paths.MindPathFally;
import org.obduro.runecrafter.paths.RcPath;
import org.obduro.runecrafter.paths.WaterPath;
import org.obduro.runecrafter.types.Altar;
import org.obduro.runecrafter.types.BankLoc;
import org.obduro.runecrafter.types.Pouch;
import org.obduro.runecrafter.types.Essence;
import org.obduro.runecrafter.types.State;

import bot.script.BotScript;
import bot.script.enums.Skill;
import bot.script.methods.Bank;
import bot.script.methods.Inventory;
import bot.script.methods.Methods;
import bot.script.methods.Skills;
import bot.script.util.Random;

public class JJsRuneCrafter extends BotScript {

	// GUI COMPONENTS
	private GUI gui;
	private Altar chosenAltar;
	private BankLoc chosenBank;
	private Pouch[] chosenPouches;
	private Essence chosenEssence;
	private boolean usingTiara;
	private boolean usingPouches;
	// OTHERS
	private long startTime;
	private int startXp;
	private int currentXp;
	// CUSTOM METHODS
	private Banking banking;
	private RuneCraft runecraft;
	private Walk walk;
	private RcPath path;

	private State scriptState = State.GUI;

	public void paint(Graphics g){
		g.setColor(new Color(60, 60, 60));
		if(usingPouches)
			g.fillRect(4, 45, 155, 200+chosenPouches.length*20);
		else
			g.fillRect(4, 45, 155, 200);

		g.setColor(Color.WHITE);
		g.drawString("JJ's RuneCrafter v1.3", 10, 65);
		
		// Calculations;
		int timeRan = (int) (System.currentTimeMillis()-startTime);
		int xpGained = currentXp-startXp;
		int xpPerHour = (int) (xpGained / (timeRan / 3600000D));
		
		g.drawString("Running for: " + Methods.timeToString(timeRan), 10, 95);
		g.drawString("State: " + scriptState, 10, 115);
		g.drawString("Xp gained: " + xpGained + " (" + xpPerHour + "/h)" , 10, 135);
		
		g.drawString("Chosen bank: " + chosenBank, 10, 170);
		g.drawString("Chosen essence: " + chosenEssence, 10, 190);
		g.drawString("Using tiara? " + usingTiara, 10, 210);
		
		g.drawString("Using pouches: " + usingPouches, 10, 240);
		if(usingPouches)
			for(int i = 0; i < chosenPouches.length; i++)
				g.drawString("Using " + chosenPouches[i].toString() + " pouch" , 10, 260 + i*20);
	}
	
	@Override
	public boolean onStart() {
		System.out.println("JJ's RuneCrafter started");
		startTime = System.currentTimeMillis();
		
		// Setting up GUI
		gui = new GUI();
		gui.setVisible(true);
		while(gui.isVisible()){
			sleep(100, 200);
		}
		chosenAltar = gui.getChosenAltar();
		chosenBank = gui.getChosenBank();
		chosenPouches = gui.getChosenPouches();
		chosenEssence = gui.getChosenEssence();
		usingTiara = gui.usingTiara();
		if(chosenPouches != null && chosenPouches.length > 0){
			usingPouches = true;
		}
		
		// Setting up instance variables
		startXp = Skills.getXp(Skill.RUNECRAFT);
		currentXp = startXp;
		startTime = System.currentTimeMillis();
		
		// Creating objects containing methods
		banking = new Banking(chosenAltar, chosenEssence, chosenPouches);
		runecraft = new RuneCraft(chosenAltar, chosenPouches, usingTiara);
		walk = new Walk(chosenAltar, chosenBank);
		
		switch(chosenAltar){
			case AIR:
				path = new AirPath();
				break;
				
				
			case BODY:
				path = new BodyPath();
				break;
				
				
			case EARTH:
				path = new EarthPath();
				break;
				
				
			case FIRE:
				path = new FirePath();
				break;
				
				
			case LAW:
				System.out.println("UNSUPPORTED!");
				this.stop();
				break;
				
				
			case MIND:
				if(chosenBank.equals(BankLoc.EDGEVILLE)){
					path = new MindPathEdge();
				}else{
					path = new MindPathFally();
				}
				break;
				
				
			case WATER:
				path = new WaterPath();
				break;
		}
		
		return true;
	}
	
	private State getState(){
		if(gui.isVisible()){
			return State.GUI;
		}else{
			if(Inventory.isFull()){
				if(walk.insideAltar()){
					if(walk.nearAltar()){
						return State.CRAFT_RUNES;
					}else{
						return State.WALK_TO_ALTAR;
					}
				}else{
					if(walk.nearRuins()){
						return State.ENTER_RUINS;
					}else{
						return State.WALK_TO_RUINS;
					}
				}
			}else{
				if(walk.insideAltar()){
					if(!walk.nearPortal()){
						return State.WALK_TO_PORTAL;
					}else{
						return State.EXIT_ALTAR;
					}
				}else{
					if(banking.nearBank()){
						if(Bank.isOpen()){
							return State.BANKING;
						}else{
							return State.OPEN_BANK;
						}
					}else{
						return State.WALK_TO_BANK;
					}
				}
			}
		}
	}
	
	@Override
	public int loop() {
		System.out.println("Inside loop");
		scriptState = getState();
		currentXp = Skills.getXp(Skill.RUNECRAFT);
		
		switch(scriptState){
			case BANKING:
				banking.depositRunes();
				banking.withdrawEss(usingPouches);
				break;
				
			case CRAFT_RUNES:
				runecraft.craftRunes(usingPouches);
				break;
				
			case ENTER_RUINS:
				runecraft.enterRuins();
				break;
				
			case EXIT_ALTAR:
				runecraft.exitPortal();
				break;
				
			case OPEN_BANK:
				if(!banking.openBank()){
					walk.toBankFailsafe();
				}
				break;
				
			case WALK_TO_ALTAR:
				path.toAltar();
				//walk.toAltar();
				break;
				
			case WALK_TO_PORTAL:
				path.toPortal();
				//walk.toPortal();
				break;
				
			case WALK_TO_BANK:
				path.toBank();
				//walk.toBank();
				break;
				
			case WALK_TO_RUINS:
				path.toRuins();
				//walk.toMysteriousRuins();
				break;
				
			case GUI:
				System.out.println("Waiting for GUI");
				break;
		}
		
		return Random.nextInt(50, 150);
	}

}
