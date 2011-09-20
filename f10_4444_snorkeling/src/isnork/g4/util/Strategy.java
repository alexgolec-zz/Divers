package isnork.g4.util;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @author Lenovo
 *
 */
public class Strategy {
	
	public int n, r, d;
	private int currentDiverId = 0;
	private int currentRound = 0;
	private int maxRounds = 480;
	private Point2D myPosition = null;
	private boolean moveAwayFromBoat = true;
	private boolean moveSpirally = false;
	private DangerAvoidanceStrategy dangerAvoidanceStrategy;
	
	public int getCurrentRound() {
		return currentRound;
	}
	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}
	public int getMaxRounds() {
		return maxRounds;
	}
	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

	public Strategy(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int d, int r, int n, Random randomSeed) {
		this.d = d;
		this.r = r;
		this.n = n;
	}
	
	
	/**
	 * 
	 * called by G4Diver.tick after ever tick... 
	 * 
	 * @param myPosition
	 * @param whatYouSee
	 * @param incomingMessages
	 * @param playerLocations
	 * @param diverId
	 */
	public void updateAfterEachTick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages, Set<Observation> playerLocations, int diverId){
		currentRound++;
		currentDiverId = diverId;
		this.myPosition = myPosition;
		
		dangerAvoidanceStrategy = new DangerAvoidanceStrategy(myPosition, whatYouSee);
		
	}
	
	
	public Direction getMove(int diverId){ 
		currentDiverId = diverId;
		EndGameStrategy endGameStrategy = new EndGameStrategy();
		Direction nextMove;
		
		if(endGameStrategy.allowedReturnTimeRadius((double)30, this) <= endGameStrategy.fastestReturnTime(myPosition)){
			System.out.println(" -endGameStrategy.allowedReturnTimeRadius- ");
			nextMove = moveTowardBoat();
			System.out.println(nextMove + "nextMove1");
			nextMove = dangerAvoidanceStrategy.avoidDanger(nextMove);
			return nextMove;
		}
		
		nextMove = spiralMove();
		nextMove = dangerAvoidanceStrategy.avoidDanger(nextMove);
		
		return nextMove;
	}
	
	private Direction moveTowardBoat(){
		double x = myPosition.getX();
		double y = myPosition.getY();
		double new_x, new_y;
		
		// the absolute values of x and y are reduced by 1.
		new_x = x==0?0:(x/Math.abs(x))*(Math.abs(x)-1);
		new_y = y==0?0:(y/Math.abs(y))*(Math.abs(y)-1);
		
		System.out.println(new_x + " , " + new_y);
		//TODO try it with newPos as 0,0
		
		Point2D newPos = new Point2D.Double(new_x, new_y);
		
		// System.out.println(myPosition + "to" + newPos +  " ------ " + findDirection(myPosition, newPos));
		return findDirection(myPosition, newPos);
	}
	
	private Direction spiralMove(){
		
		double x = myPosition.getX();
		double y = myPosition.getY();
		
//		System.out.println(" --1->  " + moveAwayFromBoat + "," + moveSpirally);
		
		if(moveAwayFromBoat){
			
			// check if edge is visible 
			if(Math.abs(currentDiverId)%2 == 0){
				// check if r away
				if (d - Math.max(Math.abs(x), Math.abs(y)) < r){
					moveAwayFromBoat = false;
					moveSpirally = true;
				}
			}
			if(Math.abs(currentDiverId)%2 == 1){
				// check if 3r away
//				System.out.println(" 1=> " + x + "," + y + " = " + (Math.max(Math.abs(x), Math.abs(y))*Math.sqrt(2.0)));
//				System.out.println(" 2 => " + x + "," + y + " = " + (Point2D.distance(Math.abs(x), Math.abs(y), d, d)));
				if (d - Math.max(Math.abs(x), Math.abs(y))*Math.sqrt(2.0) <= 3*r){
					moveAwayFromBoat = false;
					moveSpirally = true;
				}
			}
		}
		
		if(moveAwayFromBoat){
			System.out.println(" currentDiverId%8 = " + Math.abs(currentDiverId)%8);
			switch(Math.abs(currentDiverId)%8){
				case 0: return Direction.N;
				case 1: return Direction.NW;
				case 2: return Direction.W;
				case 3: return Direction.SW;
				case 4: return Direction.S;
				case 5: return Direction.SE;
				case 6: return Direction.E;
				case 7: return Direction.NE;
//				default: System.out.println(" ret null in switch "); return null;
			}
		} else if(moveSpirally){
			if( (x > 0) && (Math.abs(x) >= Math.abs(y)) ){
				if ((Math.abs(x) == Math.abs(y)) && (x>0) && (y>0)) return Direction.W; 
				return Direction.S;
			}
			if( (y > 0) && (Math.abs(y) >= Math.abs(x)) ){
				if ((Math.abs(x) == Math.abs(y)) && (x<0) && (y>0)) return Direction.N;
				return Direction.W;
			}
			if( (x < 0) && (Math.abs(x) >= Math.abs(y)) ){
				if ((Math.abs(x) == Math.abs(y)) && (x<0) && (y<0)) return Direction.E;
				return Direction.N;
			}
			if( (y < 0) && (Math.abs(y) >= Math.abs(x)) ){
				if ((Math.abs(x) == Math.abs(y)) && (x<0) && (y>0)) return Direction.S;
				return Direction.E;
			}
		}
//		System.out.println(" ret null in end ");
		return null;
	}
	
	public static Direction findDirection(Point2D currentPos, Point2D newPos){
		double currX = currentPos.getX();
		double currY = currentPos.getY();
		double newX = newPos.getX();
		double newY = newPos.getY();
		
		// in a quadrant
		if (currX > newX && currY > newY) {
			return Direction.NW;
		}
		if (currX < newX && currY < newY) {
			return Direction.SE;
		}
		if (currX < newX && currY > newY) {
			return Direction.NE;
		}
		if (currX > newX && currY < newY) {
			return Direction.SW;
		}

		// on a line
		if (currX < newX && currY > newY || currX < newX
				&& currY == newY) {
			return Direction.E;
		}
		if (currX > newX && currY < newY || currX == newX
				&& currY < newY) {
			return Direction.S;
		}

		if (currX == newX && currY > newY) {
			return Direction.N;
		}
		if (currX > newX && currY == newY) {
			return Direction.W;
		}
		return Direction.N;
		
	}

}
