package scripts.jjsrunecrafter.types;

public enum Essences {
	NORMAL(1436), 
	PURE(7936);

	private final int id;
	
	private Essences(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
}
