package scripts;

import java.awt.Color;
import java.awt.Graphics;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.EnumScript;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.jjsrunecrafter.methods.Bank;
import scripts.jjsrunecrafter.methods.GUI;
import scripts.jjsrunecrafter.methods.RuneCraft;
import scripts.jjsrunecrafter.methods.Walk;
import scripts.jjsrunecrafter.types.Altars;
import scripts.jjsrunecrafter.types.Banks;
import scripts.jjsrunecrafter.types.Essences;
import scripts.jjsrunecrafter.types.Pouches;
import scripts.jjsrunecrafter.types.Runes;
import scripts.jjsrunecrafter.types.States;

@ScriptManifest(authors = { "J J" }, category = "RuneCrafting", name = "JJ's Premium RuneCrafter ") 
public class JJsRuneCrafter07 extends EnumScript<States> implements Painting {

	// GUI COMPONENTS
	private GUI gui;
	private Runes chosenRunes;
	private Altars chosenAltar;
	private Banks chosenBank;
	private Pouches[] chosenPouches;
	private Essences chosenEssence;
	private boolean usingTiara;
	private boolean usingPouches;
	// OTHERS
	private long startTime;
	private int startXp;
	private int currentXp;
	private String runesName;
	// CUSTOM METHODS
	private Bank bank;
	private RuneCraft runecraft;
	private Walk walk;

	private States scriptState = States.GUI;
	private boolean guiInitialized = false;
	private int runesMade = 0;

	public void onPaint(Graphics g){
		// CALCULATIONS
		long timeRan = System.currentTimeMillis()-startTime;
		double multiplier = timeRan / 3600000D;
		
		int xpGained = currentXp-startXp;
		int xpPerHour = (int) (xpGained / multiplier);
		
		int runesPerHour = (int) (runesMade / multiplier);
		
		int lvl = Skills.getLevelByXP(currentXp);
		int percent = Skills.getPercentToNextLevel("RuneCrafting");
	
		// DRAWING
		g.setColor(new Color(60, 60, 60));
		g.fillRect(0, 295, 519, 75);
	
		g.setColor(Color.WHITE);
		g.drawString("JJ's Premium RuneCrafter", 5, 310);
		g.drawString("Running for: " + Timing.msToString(timeRan), 185, 310);
		g.drawString("State: " + scriptState, 335, 310);
		
		g.drawString("Xp gained: " + xpGained + " (" + xpPerHour + "/h)", 5, 330);
		g.drawString(runesName + " runes made: " + runesMade + " (" + runesPerHour + "/h)", 335, 330);

		g.setColor(new Color(0, 200, 0));
		g.fillRect(105, 345, percent*3, 20);

		g.setColor(new Color(200, 0, 0));
		g.fillRect(105+percent*3, 345, 3*(100-percent), 20);
		
		g.setColor(Color.WHITE);
		g.drawString("Currently lvl " + lvl, 5, 360);
		g.drawString("lvl " + (lvl+1), 420, 360);
		
		g.setColor(Color.BLACK);
		g.drawString("" + percent + "%", 238, 360);
	}
	
	private States getState(){
		if(!guiInitialized || gui.isVisible()){
			return States.GUI;
		}else{
			if(Inventory.isFull()){
				if(walk.insideAltar()){
					if(walk.nearAltar()){
						return States.CRAFT_RUNES;
					}else{
						return States.WALK_TO_ALTAR;
					}
				}else{
					if(walk.nearRuins()){
						return States.ENTER_RUINS;
					}else{
						return States.WALK_TO_RUINS;
					}
				}
			}else{
				if(walk.insideAltar()){
					if(!walk.nearPortal()){
						return States.WALK_TO_PORTAL;
					}else{
						return States.EXIT_ALTAR;
					}
				}else{
					if(walk.nearBank()){
						if(Banking.isBankScreenOpen()){
							return States.BANKING;
						}else{
							return States.OPEN_BANK;
						}
					}else{
						return States.WALK_TO_BANK;
					}
				}
			}
		}
	}
	
	private boolean wearingItem(int id){
        RSInterfaceChild equip = Interfaces.get(387, 28);
        if(equip != null){
            RSItem[] items = equip.getItems();
            for(RSItem item : items)
                if(item.getID() == id)
                	return true;
        }
        return false;
    }

	@Override
	public States getInitialState() {
		System.out.println("JJ's RuneCrafter started");
		startTime = System.currentTimeMillis();
		
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				gui = new GUI();
				guiInitialized = true;
				gui.setVisible(true);
			}
    	});

		while(true){
			if(guiInitialized && !gui.isVisible()){
				break;
			}
			sleep(100, 200);
		}
		System.out.println("Gui finished!");

		// Getting information from the GUI
		chosenRunes = gui.getChosenRunes();
		chosenAltar = gui.getChosenAltar();
		chosenBank = gui.getChosenBank();
		chosenPouches = gui.getChosenPouches();
		chosenEssence = gui.getChosenEssence();

		// Setting up instance variables
		startXp = Skills.getXP("RuneCrafting");
		currentXp = startXp;
		startTime = System.currentTimeMillis();
		
		// Creating objects containing methods
		bank = new Bank(chosenRunes, chosenEssence, chosenPouches);
		runecraft = new RuneCraft(chosenRunes, chosenAltar, chosenPouches, usingTiara);
		System.out.println("RuneCraft: " + runecraft);
		walk = new Walk(chosenRunes, chosenAltar, chosenBank);
		
		// Making booleans for pouches/tiara
		usingTiara = wearingItem(chosenRunes.getTiaraID());
		usingPouches = chosenPouches != null && chosenPouches.length > 0;
		String enumName = chosenRunes.name().toLowerCase();
		runesName = enumName.substring(0, 1).toUpperCase() + enumName.substring(1, enumName.length());
		
		// Setting camera angle, rotation, mouse speed, etc.
		Camera.setCameraAngle(100);
		Camera.setCameraRotation(0);
		Mouse.setSpeed(General.random(135, 145));
		Walking.control_click = true;
		Walking.walking_timeout = 5000L;

		return getState();
	}

	@Override
	public States handleState(States state) {
		scriptState = state;
		currentXp = Skills.getXP("RuneCrafting");
	
		switch(scriptState){
			case BANKING:
				bank.depositRunes(usingPouches);
				bank.withdrawEss();
				break;
				
			case CRAFT_RUNES:
				if(runecraft.craftRunes(usingPouches)){
					runesMade += runecraft.getCraftedRunes();
				}
				break;
				
			case ENTER_RUINS:
				runecraft.enterRuins();
				break;
				
			case EXIT_ALTAR:
				runecraft.exitPortal();
				break;
				
			case OPEN_BANK:
				if(!bank.openBank()){
					if(!walk.nearBank()){
						walk.toBankFailsafe();
					}
				}
				break;
				
			case WALK_TO_ALTAR:
				walk.toAltar();
				break;
				
			case WALK_TO_PORTAL:
				walk.toPortal();
				break;
				
			case WALK_TO_BANK:
				walk.toBank();
				break;
				
			case WALK_TO_RUINS:
				walk.toMysteriousRuins();
				break;
				
			case GUI:
				println("Waiting for GUI");
				break;
		}
		
		sleep(20, 60);
		return getState();
	}

}
