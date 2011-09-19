package isnork.g4.util;

public class Globals {
	
	private static final Globals GLOBALS = new Globals();
	
	public int n;
	
	public int currentRound = 0;
	public int maxRounds = 480;
	
	private Globals() {
		// TODO Auto-generated constructor stub
	}
	
	public static Globals getInstance(){
		return GLOBALS;
	}

}
