package org.obduro.runecrafter.methods;

import bot.script.methods.Npcs;
import bot.script.wrappers.NPC;

public class AntiRandom {

	private final int DRUNKEN_DWARF_ID = 956,
					  NILES_ID = 2537,
					  PRINCESS_FROG = 2469,
					  FREAKY_FORESTER = 2548;
	private final int[] RANDOM_EVENT_NPCS = {DRUNKEN_DWARF_ID, NILES_ID, PRINCESS_FROG, FREAKY_FORESTER};
	
	//3063 = mystery box
	
	public boolean haveRandomEvent(){
		NPC[] npcs = Npcs.getLoaded();
		for(NPC npc : npcs){
			int id = npc.getId();
			for(int i : RANDOM_EVENT_NPCS){
				if(i == id){
					return true;
				}
			}
		}
		return false;
	}
	
}
