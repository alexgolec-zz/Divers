package isnork.sim;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Cell
{
	private Boolean Observed;
	private Double ObservedDistance;
	private Boolean Stepped;
	private int stepcount;
	private ArrayList<CellListener> listeners;
	

	/*Cell bounds*/
	private Point Location;
	private Point2D.Double Center;
	private Double Dimensions;
	
	
	/*Cell Properties*/
	private Boolean blocked;
	private Boolean startable;
	
	HashSet<SeaLife> sealife = new HashSet<SeaLife>();
	HashSet<Integer> players = new HashSet<Integer>();

	public Set<SeaLife> getSeaLife()
	{
		Set<SeaLife> ret = new HashSet<SeaLife>();
		for(SeaLife s : sealife)
		{
			ret.add((SeaLife) s.clone());
		}
		return sealife;
	}
	public Set<Integer> getPlayers()
	{
		return players;
	}
	public Cell(int x, int y, Double dimensions)
	{
		CellBounds(new Point(x, y), dimensions);
		Stepped = false;
		stepcount = 0;
		Observed = false;
		ObservedDistance = Double.MAX_VALUE;
		
		listeners = new ArrayList<CellListener>();
	}

	public Cell(Point location, Double dimensions)
	{
		CellBounds(location, dimensions);
		Stepped = false;
		stepcount = 0;
		Observed = false;
		ObservedDistance = Double.MAX_VALUE;

		listeners = new ArrayList<CellListener>();
	}

	public void CellBounds(Point location, Double dimensions)
	{
		this.setLocation(location.x, location.y);
		Dimensions = dimensions;
		blocked = false;
		startable = false;
		
		Center = new Point2D.Double(((double)Location.x + 0.5d)*Dimensions, ((double)Location.y + 0.5d)*Dimensions);
	}
	

	/**
	 * @return
	 */
	
	public Boolean isSteppedOn()
	{
		// Do something here
		return Stepped;
	}
	
	public Boolean getObserved()
	{
		return new Boolean(Observed);
	}

	/**
	 * @return
	 */
	public Double getObservedDistance()
	{
		return new Double(ObservedDistance);
	}
	

	/**
	 * @param c
	 * @return
	 */
	public Double distanceToCell(Cell c)
	{
		return Center.distance(c.Center);
	}
	
	/**
	 * @param c
	 * @return
	 */
	public Cell copy(Cell c)
	{
		return new Cell(c.Location, c.Dimensions);
	}

	/**
	 * @return
	 */
	public Point getLocation()
	{
		return new Point(Location);
	}

	/**
	 * @return
	 */
	public Point2D.Double getCenter()
	{
		return new Point2D.Double(Center.x,Center.y);
	}

	/**
	 * @return
	 */
	public Double getDimensions()
	{
		return new Double(Dimensions);
	}


	
	public void setLocation(int x, int y)
	{
		Location = new Point(x, y);
	}

	
	public Boolean isBlocked()
	{
		return new Boolean(blocked);
	}
	
	public Boolean isStartable()
	{
		return new Boolean(startable);
	}

	public void setStartable(Boolean startable)
	{
		this.startable = startable;
		notifyListeners();
	}
	
	public static interface CellListener{
		public void CellUpdated(Cell source);
	}

	public void addCellListener(CellListener l){
		listeners.add(l);
	}
	public void removeCellListener(CellListener l){
		listeners.remove(l);
	}
	private void notifyListeners(){
		Iterator<CellListener> it = listeners.iterator();
		while(it.hasNext())
			it.next().CellUpdated(this);
	}
	
	@Override
	public String toString()
	{
		return "(" + Location.x + "," + Location.y + ")";
	}
	
}
