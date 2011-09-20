package isnork.g4.util;

public class Globals {
	
	private static final Globals GLOBALS = new Globals();
	
//	public SeaBoard seaBoard = new SeaBoard(null, 0);
	
	private Globals() {
		// TODO Auto-generated constructor stub
	}
	
	public static Globals getInstance(){
		return GLOBALS;
	}

}
