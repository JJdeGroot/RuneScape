package org.obduro.runecrafter.types;

public enum Pouch {
	SMALL(5511, -1, 3),
	MEDIUM(5510, -1, 6),
	LARGE(5513, -1, 9),
	GIANT(5515, -1, 12);
	
	private final int id, degradedID, size;
	
	private Pouch(int id, int degradedID, int size){
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
