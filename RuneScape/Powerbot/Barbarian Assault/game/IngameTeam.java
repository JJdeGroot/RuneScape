package org.obduro.barbassault.game;

import java.util.ArrayList;

import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.interactive.Player;

public class IngameTeam {

	public enum Roles{
		ATTACKER(15445, 15446, 15447, 15448, 15449), 
		DEFENDER(15455, 15456, 15457, 15458, 15459), 
		COLLECTOR(15450, 15451, 15452, 15453, 15454), 
		HEALER(15460, 15461, 15462, 15463, 15464);
		
		private final int[] capeIDs;
		
		private Roles(final int... capeIDs){
			this.capeIDs = capeIDs;
		}
		
		public String[] getNames(){
			ArrayList<String> nameList = new ArrayList<String>();
			Player[] players = Players.getLoaded();
			
			if(players != null){
				for(Player player : players){
					String name = player.getName();
					System.out.println("Player name: " + name);
	
					int capeID = player.getAppearance()[1];
					for(int id : capeIDs)
						if(id == capeID)
							nameList.add(name);
				}
			}
			
			return nameList.toArray(new String[nameList.size()]);
		}
	}
	
}
