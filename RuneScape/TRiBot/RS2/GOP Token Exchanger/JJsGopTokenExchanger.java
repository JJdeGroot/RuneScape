package scripts;

import java.awt.Color;
import java.awt.Graphics;

import org.tribot.api.Chat;
import org.tribot.api.EGW;
import org.tribot.api.General;
import org.tribot.api.Inventory;
import org.tribot.api.ScreenModels;
import org.tribot.api.Textures;
import org.tribot.api.Timing;
import org.tribot.script.EnumScript;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.jjsgoptokenexchanger.Bank;
import scripts.jjsgoptokenexchanger.Exchange;
import scripts.jjsgoptokenexchanger.ExchangeState;
import scripts.jjsgoptokenexchanger.GUI;
import scripts.jjsgoptokenexchanger.Generic;
import scripts.jjsgoptokenexchanger.Portal;
import scripts.jjsgoptokenexchanger.Talisman;

@ScriptManifest(authors = { "J J" }, category = "Minigames", name = "JJ's Gop Token Exchanger")
public class JJsGopTokenExchanger extends EnumScript<ExchangeState> implements Painting {

	private final GUI gui = new GUI();
	private final long startTime = System.currentTimeMillis(),
					   depositBoxID = 2165220430L,
					   portalToBankID = 1220408498L,
					   elrissID = 903918791,
					   portalToGopID = 3689793898L;
	private final int shopScreenID = 62615;

	private Talisman chosenTalisman = Talisman.WATER;
	private int amountBought = 0;
	private ExchangeState scriptState = ExchangeState.BUY_TALISMANS;
	
	private boolean guiFinished = false;
	
	@Override
	public void onPaint(Graphics g) {
		g.setColor(new Color(60, 60, 60));
		g.fillRect(5, 70, 200, 110);
		
		g.setColor(Color.WHITE);
		g.drawString("JJ's Token Exchanger", 10, 90);
		g.drawString("Running for: " + Timing.msToString(System.currentTimeMillis()-startTime), 10, 110);
		g.drawString("State: " + scriptState, 10, 130);
		g.drawString("Buying: " + chosenTalisman + " TALISMAN", 10, 150);
		g.drawString("Amount bought: " + amountBought, 10, 170);
	}
	
	// Handles the GUI
	private void handleGUI(){
		if(!guiFinished){
			// Setting up GUI
			java.awt.EventQueue.invokeLater(new Runnable(){
				@Override
				public void run() {
					gui.create();
				}
	    	});
			
			while(!gui.isFinished()){
				General.sleep(250, 750);
			}
			
			chosenTalisman = gui.getChosenTalisman();
			println("Chosen talisman: " + chosenTalisman);
			guiFinished = true;
		}
	}
	
	private ExchangeState getState(){
		if(Inventory.isFull()){
			if(ScreenModels.find(depositBoxID).length > 0){
				return ExchangeState.DEPOSIT_ITEMS;
			}else{
				if(EGW.getFloor() != 3){
					if(ScreenModels.find(portalToBankID).length > 0){
						return ExchangeState.ENTER_GUILD_EXIT;
					}else{
						return ExchangeState.WALK_TO_GUILD_EXIT;
					}
				}else{
					return ExchangeState.WALK_TO_BANK;
				}
			}
		}else{
			if(Textures.find(shopScreenID).length > 0){
				return ExchangeState.BUY_TALISMANS;
			}else{
				if(EGW.getFloor() != 3){
					if(ScreenModels.find(elrissID).length > 0){
						return ExchangeState.INTERACT_WITH_ELRISS;
					}else{
						return ExchangeState.WALK_TO_ELRISS;
					}
				}else{
					if(ScreenModels.find(portalToGopID).length > 0){
						return ExchangeState.ENTER_GUILD_ENTRANCE;
					}else{
						return ExchangeState.WALK_TO_GUILD_ENTRANCE;
					}
				}
			}
		}
	}
	
	private void outOfTokensCheck(){
		String[] chatbox = Chat.getChatLines();
		for(String chatline : chatbox){
			if(chatline.contains("You do not have enough tokens to buy that item.")){
				System.out.println("Out of tokens, terminating script!");
				this.stopScript();
			}
		}
	}
	
	@Override
	public ExchangeState getInitialState() {
		return ExchangeState.GUI;
	}

	@Override
	public ExchangeState handleState(ExchangeState state) {
		scriptState = state;
		outOfTokensCheck();
		
		switch(state){
		
			case GUI:
				handleGUI();
				break;

			case BUY_TALISMANS:
				if(Exchange.buyTalismans(chosenTalisman)){
					amountBought += 27;
				}
				break;
				
			case DEPOSIT_ITEMS:
				Bank.depositItems();
				break;
				
			case ENTER_GUILD_ENTRANCE:
				Portal.enterPortalToGOP();
				break;
				
			case ENTER_GUILD_EXIT:
				Generic.closeInterfaces();
				Portal.enterPortalToBank();
				break;
				
			case INTERACT_WITH_ELRISS:
				Exchange.interactElriss();
				break;
				
			case WALK_TO_BANK:
				Bank.walkToBank();
				break;
				
			case WALK_TO_ELRISS:
				Exchange.walkToElriss();
				break;
				
			case WALK_TO_GUILD_ENTRANCE:
				Portal.walkToGopEntrancePortal();
				break;
				
			case WALK_TO_GUILD_EXIT:
				Portal.walkToGopExitPortal();
				break;
	
		}
		
		// Checking if gui is finished
		while(!guiFinished){
			General.sleep(250, 750);
		}
		
		sleep(100, 200);
		
		return getState();
	}

}
