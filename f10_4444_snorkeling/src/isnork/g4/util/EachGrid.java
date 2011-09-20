package isnork.g4.util;

import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class EachGrid {

	private ArrayList<SeaLifePrototype> occupiedby;
	private Point2D location;

	public ArrayList<SeaLifePrototype> getOccupiedby() {
		return occupiedby;
	}
	public void setOccupiedby(ArrayList<SeaLifePrototype> occupiedby) {
		this.occupiedby = occupiedby;
	}
	
	public EachGrid(Point2D location) {
		this.location = location;
	}
	
	
}
