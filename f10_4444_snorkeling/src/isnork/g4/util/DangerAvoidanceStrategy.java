package isnork.g4.util;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DangerAvoidanceStrategy {
	
	Point2D myPosition;
	Set<Observation> whatYouSee;
	HashMap<Direction, Integer> directionMap = new HashMap<Direction, Integer>();
	
	
	public DangerAvoidanceStrategy(Point2D myPosition, Set<Observation> whatYouSee) {
		this.myPosition = myPosition;
		this.whatYouSee = whatYouSee;
		if(directionMap.isEmpty()){
			directionMap.put(Direction.N, 0);
			directionMap.put(Direction.NE, 1);
			directionMap.put(Direction.E, 2);
			directionMap.put(Direction.SE, 3);
			directionMap.put(Direction.S, 4);
			directionMap.put(Direction.SW, 5);
			directionMap.put(Direction.W, 6);
			directionMap.put(Direction.NW, 7);
		}
	}
	
	
	/**
	 * avoidDanger takes a direction and checks if this is a safe move
	 * @param nextMove is the proposed move
	 * @return safe direction closest to proposed direction
	 */
	public Direction avoidDanger(Direction nextMove){
		// for each dangerous creature, find direction from current location to that creature
		Iterator<Observation> iter = whatYouSee.iterator();
		Observation refObservation = null; 
		Direction refDirection;
		double refDistance;
		double [] possMoves = new double[]{0,0,0,0,0,0,0,0};
		
		while (iter.hasNext()){
			refObservation = iter.next();
			if (refObservation.isDangerous()) {
				refDirection = Strategy.findDirection(myPosition, refObservation.getLocation());
				refDistance = Point2D.distance(myPosition.getX(), myPosition.getY(),
						refObservation.getLocation().getX(), refObservation.getLocation().getY());
				possMoves[directionMap.get(refDirection).intValue()] += 2*refObservation.happiness()/(refDistance*refDistance+1);
			}
		}
		
		System.out.println(nextMove + " nextMove");
		if(nextMove == null) nextMove = Direction.N;
		int proposedMoveIndex = directionMap.get(nextMove);
		double best = possMoves[proposedMoveIndex];
		int bestNextMove = proposedMoveIndex;
		for (int i = 1; i <= 3; i++){
			if (possMoves[(proposedMoveIndex+i+8)%8] < best){
				bestNextMove = (proposedMoveIndex+i+8)%8;
				best = possMoves[(proposedMoveIndex+i+8)%8]/i;
			}
			if (possMoves[(proposedMoveIndex-i+8)%8] < best) {
				bestNextMove = (proposedMoveIndex+i+8)%8;
				best = possMoves[(proposedMoveIndex-i+8)%8]/i;
			}
		}
		
		Iterator<Direction> it = directionMap.keySet().iterator();
	    Direction refKey = null;
		while (it.hasNext()) {
			refKey = it.next();
	        if (directionMap.get(refKey).intValue() == bestNextMove){
	        	return refKey;
	        }
	    }
		
		return nextMove;
	}
}
