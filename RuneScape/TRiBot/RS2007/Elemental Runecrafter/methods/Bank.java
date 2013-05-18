package scripts.jjsrunecrafter.methods;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;

import scripts.jjsrunecrafter.types.Pouches;
import scripts.jjsrunecrafter.types.Runes;
import scripts.jjsrunecrafter.types.Essences;

public class Bank {

	private Runes chosenRunes;
	private Essences chosenEssence;
	private Pouches[] pouches;

	public Bank(Runes runes, Essences essence, Pouches[] pouches){
		this.chosenRunes = runes;
		this.chosenEssence = essence;
		this.pouches = pouches;
	}
	
	private boolean fillPouches(){
		if(pouches != null && pouches.length > 0){
			for(Pouches pouch : pouches){
				if(Inventory.getCount(chosenEssence.getID()) < pouch.getSize()){
					if(!Banking.isBankScreenOpen()){
						Banking.openBankBanker();
					}
					withdrawEss();
					if(Banking.isBankScreenOpen()){
						Banking.close();
					}
				}

				RSItem[] inventPouch = Inventory.find(pouch.getID());
				if(inventPouch != null && inventPouch.length > 0 && inventPouch[0].click("Fill")){
					System.out.println("Filled " + pouch + " POUCH");
					General.sleep(200, 400);
				}
			}
		}
		return false;
	}

	public boolean openBank() {
		if(!Banking.openBankBanker()){
			Banking.openBankBooth();
		}
		return Banking.isBankScreenOpen();
	}

	public boolean depositRunes(boolean usingPouches) {
		if (Banking.isBankScreenOpen()) {
			RSItem[] runes = Inventory.find(chosenRunes.getRunesID());
			if (runes != null && runes.length > 0 && runes[0].click("Store All")) {
				System.out.println("Deposited runes");

				long t = System.currentTimeMillis();
				while (Timing.timeFromMark(t) < General.random(2000, 3000)) {
					runes = Inventory.find(chosenRunes.getRunesID());
					if(runes == null || runes.length == 0){
						if(usingPouches){
							fillPouches();
						}
						return true;
					}
					General.sleep(100, 200);
				}
			}
		}
		return false;
	}

	public boolean withdrawEss() {
		if(Banking.isBankScreenOpen()) {
			RSItem[] ess = Banking.find(chosenEssence.getID());
			if(ess != null && ess.length > 0 && ess[0].click("Withdraw All")) {
				System.out.println("Withdrawed essence");

				long t = System.currentTimeMillis();
				while(Timing.timeFromMark(t) < General.random(2000, 3000)){
					ess = Inventory.find(chosenEssence.getID());
					if (Inventory.isFull() || (ess != null && ess.length > 0)) {
						return true;
					}
					General.sleep(100, 200);
				}
			}
		}

		return false;
	}
	
}
