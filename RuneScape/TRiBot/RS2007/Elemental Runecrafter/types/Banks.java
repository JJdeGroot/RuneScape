package scripts.jjsrunecrafter.types;

public enum Banks {
	FALADOR_WEST(11758, 57), 
	FALADOR_EAST(11758, 73), 
	VARROCK_EAST(2453, 5912, 5913),
	AL_KHARID(2213, 65, 69),
	EDGEVILLE(2213, 56, 57),
	DRAYNOR(2213, 56, 57);
	
	private final int boothID;
	private final int[] bankerIDs;
	
	private Banks(int boothID, int... bankerIDs){
		this.boothID = boothID;
		this.bankerIDs = bankerIDs;
	}
	
	public int getBoothID(){
		return boothID;
	}
	
	public int[] getBankerID(){
		return bankerIDs;
	}
}
