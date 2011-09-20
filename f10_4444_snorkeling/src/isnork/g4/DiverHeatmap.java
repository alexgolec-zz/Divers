package isnork.g4;

import java.awt.geom.Point2D;
import java.util.HashSet;

import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

public class DiverHeatmap {
	private int dimension;
	private double[][] danger;
	private HashSet<StationaryCreature> stationaryCreatures;
	
	private static Point2D.Double[] dangerRadius;
	
	{
		dangerRadius = new Point2D.Double[9];
		
		dangerRadius[0] = new Point2D.Double(0, 0);
		dangerRadius[1] = new Point2D.Double(-1, 1);
		dangerRadius[2] = new Point2D.Double(0, 1);
		
		dangerRadius[3] = new Point2D.Double(1, 1);
		dangerRadius[4] = new Point2D.Double(-1, 0);
		dangerRadius[5] = new Point2D.Double(1, 0);
		
		dangerRadius[6] = new Point2D.Double(-1, -1);
		dangerRadius[7] = new Point2D.Double(0, -1);
		dangerRadius[8] = new Point2D.Double(1, -1);
	}
	
	/**
	 * Internal class to represent stationary creatures that were seen in person. 
	 * @author alexgolec
	 */
	private class StationaryCreature {
		public final Point2D.Double pos;
		public final SeaLifePrototype proto;
		
		public StationaryCreature(int x, int y, Observation o) {
			pos = new Point2D.Double(x, y);
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
		for (Point2D.Double p: dangerRadius) {
			Point2D.Double cur = new Point2D.Double(c.pos.getX(), c.pos.getY());
			cur.setLocation(cur.x + p.x, cur.y + cur.y);
			
			try {
				danger[(int) cur.x][(int) cur.y] += -2 * c.proto.getHappiness();
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
