package isnork.sim;

import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;

public class Observation {
	Point2D location;
	int id;
	String name;
	public Point2D getLocation(){
		return location;
	}
	public int getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public boolean isDangerous()
	{
		return danger;
	}
	public double happinessD()
	{
		return happy;
	}
	public int happiness()
	{
		return (int) happy;
	}
	double happy;
	boolean danger;
	Direction dir;
	public Direction getDirection() {
		return dir;
	}
}
