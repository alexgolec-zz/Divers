package isnork.sim;

import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SeaLife extends SeaLifePrototype implements Cloneable {
	private int id;
	private Point2D location;
	private Direction direction;
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public Point2D getLocation() {
		return location;
	}
	public void setLocation(Point2D location) {
		this.location = location;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public Direction getNewDirection() {
		int r = GameConfig.random.nextInt(100);
		if(r < 21 || direction == null)
		{
			ArrayList<Direction> directions = Direction.allBut(direction);
			return directions.get(GameConfig.random.nextInt(directions.size()));
		}
		else
		{
			//Keep same direction
			return direction;
		}
	}
	public SeaLife(SeaLifePrototype p)
	{
		this.happiness=p.happiness;
		this.dangerous=p.dangerous;
		this.name=p.name;
		this.speed=p.speed;
		this.filename=p.filename;
	}
	public Object clone()
	{
		SeaLife s = new SeaLife(this);
		s.id=this.id;
		s.location=this.location;
		s.direction= null;
		return s;
	}
	@Override
	public String toString() {
		return name+" (id "+id+", happy score = "+happiness+(dangerous?", dangerous" : "")+")";
	}
}
