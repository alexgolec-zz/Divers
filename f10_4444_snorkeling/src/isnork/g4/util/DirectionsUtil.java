package isnork.g4.util;

import isnork.sim.GameObject.Direction;

import java.util.ArrayList;
import java.util.List;

public class DirectionsUtil {
	
	public static List<Direction> getForwardDirections(Direction direction){
		List<Direction> fwdDiretions = new ArrayList<Direction>();
		if(direction == Direction.N){
			fwdDiretions.add(Direction.N);fwdDiretions.add(Direction.NE);fwdDiretions.add(Direction.NW);
			return fwdDiretions;
		}
		if(direction == Direction.S){
			fwdDiretions.add(Direction.S);fwdDiretions.add(Direction.SE);fwdDiretions.add(Direction.SW);
			return fwdDiretions;
		}
		if(direction == Direction.W){
			fwdDiretions.add(Direction.W);fwdDiretions.add(Direction.NW);fwdDiretions.add(Direction.SW);
			return fwdDiretions;
		}
		if(direction == Direction.E){
			fwdDiretions.add(Direction.E);fwdDiretions.add(Direction.NE);fwdDiretions.add(Direction.SE);
			return fwdDiretions;
		}
		if(direction == Direction.NE){
			fwdDiretions.add(Direction.N);fwdDiretions.add(Direction.NE);fwdDiretions.add(Direction.E);
			return fwdDiretions;
		}
		if(direction == Direction.NW){
			fwdDiretions.add(Direction.N);fwdDiretions.add(Direction.W);fwdDiretions.add(Direction.NW);
			return fwdDiretions;
		}
		if(direction == Direction.SE){
			fwdDiretions.add(Direction.S);fwdDiretions.add(Direction.E);fwdDiretions.add(Direction.SE);
			return fwdDiretions;
		}
		if(direction == Direction.SW){
			fwdDiretions.add(Direction.S);fwdDiretions.add(Direction.W);fwdDiretions.add(Direction.SW);
			return fwdDiretions;
		}
		return fwdDiretions;
	}
	
	public static List<Direction> getPerpendicularDirections(Direction direction){
		List<Direction> fwdDiretions = new ArrayList<Direction>();
		if(direction == Direction.N){
			fwdDiretions.add(Direction.W);fwdDiretions.add(Direction.E);fwdDiretions.add(Direction.NE);
			return fwdDiretions;
		}
		if(direction == Direction.S){
			fwdDiretions.add(Direction.W);fwdDiretions.add(Direction.E);fwdDiretions.add(Direction.SW);
			return fwdDiretions;
		}
		if(direction == Direction.W){
			fwdDiretions.add(Direction.W);fwdDiretions.add(Direction.NW);fwdDiretions.add(Direction.SW);
			return fwdDiretions;
		}
		if(direction == Direction.E){
			fwdDiretions.add(Direction.N);fwdDiretions.add(Direction.S);fwdDiretions.add(Direction.SE);
			return fwdDiretions;
		}
		if(direction == Direction.NE){
			fwdDiretions.add(Direction.S);fwdDiretions.add(Direction.W);fwdDiretions.add(Direction.SW);
			return fwdDiretions;
		}
		if(direction == Direction.NW){
			fwdDiretions.add(Direction.S);fwdDiretions.add(Direction.E);fwdDiretions.add(Direction.SE);
			return fwdDiretions;
		}
		if(direction == Direction.SE){
			fwdDiretions.add(Direction.N);fwdDiretions.add(Direction.W);fwdDiretions.add(Direction.NW);
			return fwdDiretions;
		}
		if(direction == Direction.SW){
			fwdDiretions.add(Direction.N);fwdDiretions.add(Direction.E);fwdDiretions.add(Direction.NE);
			return fwdDiretions;
		}
		return fwdDiretions;
	}
	
}
