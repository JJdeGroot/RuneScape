package scripts.jjsgoptokenexchanger;

import java.awt.Point;

import org.tribot.api.Banking;
import org.tribot.api.Constants;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Inventory;
import org.tribot.api.Textures;
import org.tribot.api.Timing;
import org.tribot.api.types.BankItem;

public class Bank {
	
	private static final Point bankLoc = new Point(3109, 3160);
	private static final long depositBoxID = 2165220430L;
	private static final int gopTokensID = 1468557;
	private static final int depositBoxTexture = 22484;

	public static boolean walkToBank(){
		return Generic.walkTo(bankLoc);
	}
	
	private static boolean openDepositBox(){
		if(DynamicClicking.clickScreenModel(Generic.getScreenModel(depositBoxID), "Deposit")){
			Generic.waitUntilNotMoving();
			waitDepositBoxScreen(3000);
		}
		
		return depositBoxScreen();
	}
	
	private static boolean depositBoxScreen(){
		return Textures.find(depositBoxTexture).length > 0;
	}
	
	private static boolean waitDepositBoxScreen(int time){
		long t = System.currentTimeMillis();
		
		while(Timing.timeFromMark(t) < time){
			if(depositBoxScreen()){
				return true;
			}
			General.sleep(50, 100);
		}
		
		return false;
	}
	
	public static boolean depositItems(){
		if(openDepositBox()){
			BankItem[] items = Banking.getAll();
			for(BankItem item : items){
				if(item.id != gopTokensID){
					BankItem[] moreItems = Banking.find(item.id);
					if(moreItems.length > 1){
						int r = Constants.RANDOM.nextInt(moreItems.length);
						moreItems[r].click("Deposit-All");
					}else{
						item.click("Deposit-1");
					}
					General.sleep(1000, 1500);
					break;
				}
			}
			
			if(Inventory.getAll().length > 1){
				depositItems();
			}
			
			return true;
		}
		return false;
	}
	
}
