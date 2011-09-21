package isnork.g4;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashSet;

import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

public class DiverHeatmap {
	private int dimension;
	private double[][] danger;
	private HashSet<StationaryCreature> stationaryCreatures;
	
	private static Point[] dangerRadius;
	
	{
		dangerRadius = new Point[9];
		
		dangerRadius[0] = new Point(0, 0);
		dangerRadius[1] = new Point(-1, 1);
		dangerRadius[2] = new Point(0, 1);
		
		dangerRadius[3] = new Point(1, 1);
		dangerRadius[4] = new Point(-1, 0);
		dangerRadius[5] = new Point(1, 0);
		
		dangerRadius[6] = new Point(-1, -1);
		dangerRadius[7] = new Point(0, -1);
		dangerRadius[8] = new Point(1, -1);
	}
	
	/**
	 * Internal class to represent stationary creatures that were seen in person. 
	 * @author alexgolec
	 */
	private class StationaryCreature {
		public final Point pos;
		public final SeaLifePrototype proto;
		
		public StationaryCreature(int x, int y, Observation o) {
			pos = new Point(x, y);
			proto = G4Diver.getProtoFromName(o.getName());
		}
	}
	
	public DiverHeatmap(int dimension) {
		this.dimension = dimension;
		stationaryCreatures = new HashSet<DiverHeatmap.StationaryCreature>();
	}

	public void tick() {
		danger = new double[this.dimension][this.dimension];
	}
	
	public void registerStationary(Observation o) {
		Point2D pos = o.getLocation();
		stationaryCreatures.add(new StationaryCreature((int) pos.getX(), (int) pos.getY(), o));
	}
	
	private void putStationaryCreature(StationaryCreature c) {
		for (Point p: dangerRadius) {
			Point cur = new Point((int) c.pos.getX(), (int) c.pos.getY());
			cur.setLocation(cur.x + p.x, cur.y + cur.y);
			
			try {
				danger[cur.x][cur.y] += -2 * c.proto.getHappiness();
			} catch (IndexOutOfBoundsException e) {
				continue;
			}
		}
	}
	
	public double[][] getDanger() {
		for (StationaryCreature s: stationaryCreatures) {
			putStationaryCreature(s);
		}
		
		return danger;
	}
}
