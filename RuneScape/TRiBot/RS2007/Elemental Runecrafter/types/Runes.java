package scripts.jjsrunecrafter.types;

public enum Runes {
	AIR(Banks.FALADOR_EAST, Altars.AIR, 1438, 556, 5527, 5.0), 
	MIND(Banks.FALADOR_WEST, Altars.MIND, 1448, 558, 5529, 5.5), 
	WATER(Banks.DRAYNOR, Altars.WATER, 1444, 555, 5531, 6.0), 
	EARTH(Banks.VARROCK_EAST, Altars.EARTH, 1440, 557, 5535, 6.5), 
	FIRE(Banks.AL_KHARID, Altars.FIRE, 1442, 554, 5537, 7.0), 
	BODY(Banks.EDGEVILLE, Altars.BODY, 1446, 559, 5533, 7.5);

	private final Banks bank;
	private final Altars altar;
	private final int talismanID, runesID, tiaraID;
	private final double xp;
	
	private Runes(Banks bank, Altars altar, int talismanID, int runesID, int tiaraID, double xp){
		this.bank = bank;
		this.altar = altar;
		this.talismanID = talismanID;
		this.runesID = runesID;
		this.tiaraID = tiaraID;
		this.xp = xp;
	}
	
	public int getTalismanID(){
		return talismanID;
	}
	
	public int getRunesID(){
		return runesID;
	}
	
	public int getTiaraID(){
		return tiaraID;
	}
	
	public double getXp(){
		return xp;
	}
	
	public Banks getBank(){
		return bank;
	}
	
	public Altars getAltar(){
		return altar;
	}
	
}
