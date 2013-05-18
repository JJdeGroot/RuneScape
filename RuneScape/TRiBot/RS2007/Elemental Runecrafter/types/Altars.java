package scripts.jjsrunecrafter.types;

public enum Altars {
	AIR(12726, 10047, 10060), 
	MIND(12727, 10048, 10061), 
	WATER(12728, 10049, 10062), 
	EARTH(12729, 10050, 10063), 
	FIRE(12730, 10051, 10064), 
	BODY(12731, 10052, 10065);
	
	private final int ruinsID, portalID, altarID;

	private Altars(int ruinsID, int portalID, int altarID){
		this.ruinsID = ruinsID;
		this.portalID = portalID;
		this.altarID = altarID;
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

	
}
