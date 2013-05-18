package org.obduro.runecrafter.types;

public enum Essence {
	NORMAL(1437), 
	PURE(7937);

	private final int id;
	
	private Essence(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
}
