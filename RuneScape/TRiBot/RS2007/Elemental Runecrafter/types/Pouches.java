package scripts.jjsrunecrafter.types;

public enum Pouches {
	SMALL(5509, -1, 3),
	MEDIUM(5510, 5511, 6),
	LARGE(5512, 5513, 9),
	GIANT(5514, 5515, 12);
	
	private final int id, degradedID, size;
	
	private Pouches(int id, int degradedID, int size){
		this.id = id;
		this.degradedID = degradedID;
		this.size = size;
	}
	
	public int getID(){
		return id;
	}
	
	public int getDegradedID(){
		return degradedID;
	}
	
	public int getSize(){
		return size;
	}
}
