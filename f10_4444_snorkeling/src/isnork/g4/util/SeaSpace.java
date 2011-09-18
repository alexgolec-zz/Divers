package isnork.g4.util;

import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SeaSpace {

	private ArrayList<SeaLifePrototype> occupiedby;
	private int roundset;
	private Point2D location, center;
//	private Logger log;
	
	public SeaSpace(Point2D location) {
		this.location = location;
	}
	
	public ArrayList<SeaLifePrototype> getOccupiedby() {
		return occupiedby;
	}
	public void setOccupiedby(ArrayList<SeaLifePrototype> occupiedby) {
		this.occupiedby = occupiedby;
	}
	public int getRoundset() {
		return roundset;
	}
	public void setRoundset(int roundset) {
		this.roundset = roundset;
	}
	public Point2D getLocation() {
		return location;
	}
	public void setLocation(Point2D location) {
		this.location = location;
	}
	public Point2D getCenter() {
		return center;
	}
	public void setCenter(Point2D center) {
		this.center = center;
	}
	
	
	
}
