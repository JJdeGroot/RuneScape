package org.obduro.runecrafter.types;

public enum Altar {

	AIR(2478, 2452, 2465, -1, 1439, 557), 
	MIND(2479, 2453, 2466, -1, 1449, 559), 
	WATER(2480, 2454, 2467, -1, 1445, 556), 
	EARTH(2481, 2455,  2468, -1, 1441, 558), 
	FIRE(2482, 2456, 2469, -1, 1443, 555), 
	BODY(2483, 2457,  2470, -1, 1447, 560),
	LAW(2485, 2459, 2472, -1, 1459, 564);

	private final int altarID, ruinsID, portalID, tiaraID, talismanID, runesID;

	private Altar(int altarID, int ruinsID, int portalID, int tiaraID, int talismanID, int runesID){
		this.altarID = altarID;
		this.ruinsID = ruinsID;
		this.portalID = portalID;
		this.tiaraID = tiaraID;
		this.talismanID = talismanID;
		this.runesID = runesID;
	}
	
	public int getAltarID(){
		return altarID;
	}

	public int getRuinsID(){
		return ruinsID;
	}

	public int getPortalID(){
		return portalID;
	}
	
	public int getTiaraID(){
		return tiaraID;
	}
	
	public int getTalismanID(){
		return talismanID;
	}
	
	public int getRunesID(){
		return runesID;
	}

	
}
