package org.obduro.barbassault.lobby;

import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class LobbyTeam {

	private static String getText(int parent, int child){
		WidgetChild wc = Widgets.get(parent, child);
		if(wc.validate()){
			return wc.getText();
		}
		return null;
	}
	
	public enum Team {
		LEADER(6),
		ONE(7),
		TWO(8),
		THREE(9),
		FOUR(10);
		
		private final int index;
		
		private Team(int index){
			this.index = index;
		}
		
		
		
	}
	
	

	public String getHeader(){
		return getText(256, 5);
	}
	
	public String getLeader(){
		return getText(256, 6);
		
	}
	
	public String getPlayerOne(){
		return getText(256, 7);
	}
	
	public String getPlayerTwo(){
		return getText(256, 8);
	}
	
	public String getPlayerThree(){
		return getText(256, 9);
	}
	
	public String getPlayerFour(){
		return getText(256, 10);
	}
	
	
	
	
	
}
