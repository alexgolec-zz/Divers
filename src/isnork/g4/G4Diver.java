package isnork.g4;

import isnork.sim.GameConfig;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.Set;

public class G4Diver extends Player {

	GameConfig gameConfig = new GameConfig();
	
	int boardDimension = gameConfig.getD();
	int visibilityRadius = gameConfig.getR();
	int numDivers = gameConfig.getNumDivers();
	Set<SeaLifePrototype> seaLifePossibilites = null;
	int penalty = gameConfig.getPenalty();
	Point2D whereIAm = null;
	
	@Override
	public String getName() {
		return "G4 Diver";
	}

	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int d, int r, int n) {
//		System.out.println("newGame() called");
		this.seaLifePossibilites = seaLifePossibilites;
		this.penalty = penalty;
		this.boardDimension = d;
		this.visibilityRadius = r;
		this.numDivers = n;
	}

	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
		
//		System.out.println("tick() called");
		this.whereIAm = myPosition;
		if(myPosition == null){
			System.out.println("myPosition is NULL");
		}else{
			System.out.println("myPosition - " + this.whereIAm.getX() + " , " + this.whereIAm.getY());
		}
//		for(Observation o : whatYouSee){
//			System.out.println(" id = " + o.getId());
//			System.out.println(" name = " + o.getName());
//			System.out.println(o.getLocation().getX() + " , " + o.getLocation().getY());
//			System.out.println(" happiness = " + o.happiness());
//			System.out.println(" dangerous = " + o.isDangerous());
//			System.out.println("----------------------");
//		}
		return null;
	}

	@Override
	public Direction getMove() {
		System.out.println("getMove() called");
		Direction direction = Direction.NE;
		if( distanceFromBoat(this.whereIAm) > (boardDimension - visibilityRadius) ){
			 System.out.println("at NE Corner");
			 return Direction.W;
		}
		return direction;
	}
	
	private double distanceFromBoat(Point2D myPosition){
//		System.out.println("distanceFromBoat - " + myPosition.getX() + " , " + myPosition.getY());
//		double x = myPosition.getX();
//		double y = myPosition.getY();
//		return Math.sqrt(x*x + y*y);
		System.out.println("distanceFromBoat = " + (Point2D.distance(0, 0, myPosition.getX(), myPosition.getY())));
		return Point2D.distance(0, 0, myPosition.getX(), myPosition.getY());
	}
	
	private int getQuadrant(Point2D myPosition){
		return 0;
	}

}
