package org.obduro.runecrafter.methods;

import org.obduro.runecrafter.Timer;
import org.obduro.runecrafter.types.Altar;
import org.obduro.runecrafter.types.Pouch;
import org.obduro.runecrafter.types.Essence;

import bot.script.methods.Bank;
import bot.script.methods.Inventory;
import bot.script.methods.Methods;
import bot.script.methods.Npcs;
import bot.script.methods.Objects;
import bot.script.methods.Players;
import bot.script.util.Random;
import bot.script.wrappers.GameObject;
import bot.script.wrappers.Item;
import bot.script.wrappers.NPC;

public class Banking {
	
	private final int[] BANK_NPCS = {495, 496, 497, 6200, 5913, 958},
					    BANK_CHESTS = {3194},
					    CLOSED_BANK_CHESTS = {3193},
					    BANK_BOOTHS = {2213, 11042, 11758};

	private Altar chosenAltar;
	private Essence chosenEssence;
	private Pouch[] pouches;

	public Banking(Altar altar, Essence essence, Pouch[] pouches){
		this.chosenAltar = altar;
		this.chosenEssence = essence;
		this.pouches = pouches;
	}
	
	private boolean fillPouches(){
		if(pouches != null && pouches.length > 0){
			for(Pouch p : pouches){
				if(Inventory.getCount(chosenEssence.getID(), false) < p.getSize()){
					if(!Bank.isOpen()){
						openBank();
					}
					withdrawEss(false);
					if(Bank.isOpen()){
						Bank.close();
					}
				}

				Item pouch = Inventory.getItem(p.getID());
				if(pouch != null && pouch.interact("Fill")){
					System.out.println("Filled " + p + " POUCH");
					Methods.sleep(100, 200);
				}
			}
		}
		return false;
	}
	
	private boolean waitBankscreen(int time){
		Timer timer = new Timer(time);
		while(timer.isRunning()) {
			if (Bank.isOpen()) {
				return true;
			}
			Methods.sleep(100, 200);
			if (Players.getLocal().getAnimation() != -1 || Players.getLocal().isMoving()) {
				timer.reset();
			}
		}
		
		return false;
	}
	
	private boolean openAllBanks() {
        GameObject object = Objects.getNearest(BANK_BOOTHS);
        if (object != null) {
        	System.out.println("Found bank booth");
            return object.interact("Use-quickly");
        }

        GameObject chest = Objects.getNearest(BANK_CHESTS);
        if (chest != null) {
        	System.out.println("Trying to open chest");
            return chest.click();
        }

        NPC npc = Npcs.getNearest(BANK_NPCS);
        if (npc != null) {
            return npc.interact("Bank");
        }

        return false;
    }
	
	private boolean handleClosedChest(){
		if(Objects.getNearest(BANK_CHESTS) == null){
			GameObject closedChest = Objects.getNearest(CLOSED_BANK_CHESTS);
			if(closedChest != null && closedChest.isVisible() && closedChest.interact("Open Closed chest")){
				System.out.println("Opened chest");
				Timer timer = new Timer(Random.nextInt(2000, 3000));
				while(timer.isRunning()) {
					if(Objects.getNearest(BANK_CHESTS) != null) {
						return true;
					}
					Methods.sleep(100, 200);
					if (Players.getLocal().getAnimation() != -1 || Players.getLocal().isMoving()) {
						timer.reset();
					}
				}
			}
		}
		return false;
	}

	public boolean openBank() {
		handleClosedChest();
		if(openAllBanks()){
			waitBankscreen(Random.nextInt(2000, 3000));
		}		

		return Bank.isOpen();
	}
	
	public boolean nearBank(){
		return Npcs.getNearest(BANK_NPCS) != null ||
			   Objects.getNearest(BANK_BOOTHS) != null ||
			   Objects.getNearest(BANK_CHESTS) != null ||
			   Objects.getNearest(CLOSED_BANK_CHESTS) != null;
	}

	public boolean depositRunes() {
		if (Bank.isOpen()) {
			Item runes = Inventory.getItem(chosenAltar.getRunesID());
			if (runes != null && runes.interact("Store All")) {
				System.out.println("Deposited runes");

				Timer timer = new Timer(Random.nextInt(2000, 3000));
				while (timer.isRunning()) {
					if (Inventory.getItem(chosenAltar.getRunesID()) == null) {
						return true;
					}
					Methods.sleep(100, 200);
				}
			}
		}
		return false;
	}

	public boolean withdrawEss(boolean usingPouches) {
		if(usingPouches){
			System.out.println("Using pouches, withdrawing ess & filling!");
			fillPouches();
		}
		
		if(Bank.isOpen()) {
			Item ess = Bank.getItem(chosenEssence.getID());
			if(ess != null && ess.interact("Withdraw All")) {
				System.out.println("Withdrawed essence");

				Timer timer = new Timer(Random.nextInt(2000, 3000));
				while (timer.isRunning()) {
					if (Inventory.isFull() || Bank.getItem(chosenEssence.getID()) == null) {
						return true;
					}
					Methods.sleep(100, 200);
				}
			}
		}

		return false;
	}
	
}
