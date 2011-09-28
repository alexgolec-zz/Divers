package isnork.g4;

import isnork.g4.util.MarkovSimulator;
import isnork.g4.util.MessageMap;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Hashtable;

public class DiverHeatmap {
	private int dimension;
	private double[][] danger = null;
	private double[][] happiness = null;
	/** Mapping from id of observation to creature. */
	private Hashtable<Integer, StationaryCreature> stationaryCreatures;
	private Hashtable<Integer, MobileCreature> movingCreatures;
	private Hashtable<String, HashSet<CreatureReport>> stationaryReports;
	private HashSet<Point> seenPoints;
	private int radius;
	
	/**
	 * Utility function to perform fetching from the danger matrix.
	 * @param x the x position
	 * @param y the y position
	 * @return the value of the danger matrix in this position
	 */
	public double dangerGet(int x, int y) {
		refreshDanger();
		return dangerGetPrivate(x, y);
	}
	
	private double dangerGetPrivate(int x, int y) {
		double ret = danger[x + dimension][y + dimension];
		return ret;
	}

	/**
	 * Utility function to set values in the danger matrix
	 * @param x the x position
	 * @param y the y position
	 * @param val the value to which to set the danger matrix entry
	 */
	private void dangerSet(int x, int y, double val) {
		danger[x + dimension][y + dimension] = val;
	}
	
	public double happyGet(int x, int y) {
		refreshHappiness();
		return happyGetPrivate(x, y);
	}
	
	private double happyGetPrivate(int x, int y) {
		return happiness[x + dimension][y + dimension];
	}
	
	private void happySet(int x, int y, double val) {
		happiness[x + dimension][y + dimension] = val;
	}
	
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
	
	private class CreatureReport {
		public Point reportPos;
		public final SeaLifePrototype proto;
		
		public CreatureReport(int x, int y, SeaLifePrototype proto) {
			reportPos = new Point(x, y);
			this.proto = proto;
		}
		
		public boolean equal(CreatureReport o1) {
			return o1.reportPos.equals(reportPos) && 
					o1.proto.equals(proto);
		}
	}
	
	/**
	 * Internal class to represent stationary creatures that were seen in person. 
	 * @author alexgolec
	 */
	private abstract class CreatureSighting {
		public Point pos;
		public final SeaLifePrototype proto;
		
		public CreatureSighting(int x, int y, Observation o) {
			pos = new Point(x, y);
			proto = G4Diver.getProtoFromName(o.getName());
		}
	}
	
	private class StationaryCreature extends CreatureSighting {
		public StationaryCreature(int x, int y, Observation o) {
			super(x, y, o);
		}
	}
	
	/**
	 * Internal class to represent mobile creatures seen in person
	 * @author alexgolec
	 */
	private class MobileCreature extends CreatureSighting {
		private Point lastPos = null;
		private Point lastDir = null;
		
		public MobileCreature(int x, int y, Observation o) {
			super(x, y, o);
		}
		
		public void setPos(Point pos) {
			lastPos = this.pos;
			this.pos = pos;
		}
		
		public Point getDirection() {
			if (lastPos == null) {
				return null;
			}
			int change_x = pos.x - lastPos.x;
			int change_y = pos.y - lastPos.y;
			
			if (change_x != 0 || change_y != 0) {
				lastDir = new Point(change_x, change_y);
			}
			
			return lastDir;
		}
	}
	
	/**
	 * Create a new empty heatmap
	 * @param dimension the dimension parameter of the board
	 */
	public DiverHeatmap(int dimension, int rad) {
		this.dimension = dimension;
		stationaryCreatures = new Hashtable<Integer, StationaryCreature>();
		movingCreatures = new Hashtable<Integer, MobileCreature>();
		stationaryReports = new Hashtable<String, HashSet<CreatureReport>>();
		seenPoints = new HashSet<Point>();
		radius = rad;
	}

	/**
	 * Notify the heapmap of a direct observation of a stationary creature
	 * @param o the observation to report
	 */
	public void registerStationary(Observation o) {
		if (stationaryCreatures.containsKey(o.getId())) {
			return;
		}
		
		invalidateDanger();
		
		Point2D pos = o.getLocation();
		stationaryCreatures.put(o.getId(), new StationaryCreature((int) pos.getX(), (int) pos.getY(), o));
//		System.out.println(" registered " + o.getId());
	}
	public void registerMoving(Observation o) {
		invalidateDanger();

		Point2D op = o.getLocation();
		Point p = new Point((int) op.getX(), (int) op.getY());

//		if (o.isDangerous()) {
//			System.out.println("Putting "+o.getName());
//		}
		
		if (movingCreatures.containsKey(o.getId())) {
			MobileCreature m = movingCreatures.get(o.getId());
			m.setPos(new Point((int) p.getX(), (int) p.getY()));
		} else {
			movingCreatures.put(o.getId(), new MobileCreature(p.x, p.y, o));
		}
	}
	
	public void reportStationaryMessage(MessageMap map, String message, Point location) {
		System.out.println("Received message "+message);
		
		if (message.toLowerCase().equals("z")) {
			return;
		}
		
		// Except for z, there is exactly one species per message
		String species = map.decode(message).iterator().next();
		CreatureReport r = new CreatureReport(location.x, location.y, G4Diver.getProtoFromName(species));
		
		HashSet<CreatureReport> reps = stationaryReports.get(r.proto.getName());
		if (reps == null) {
			reps = new HashSet<DiverHeatmap.CreatureReport>();
			stationaryReports.put(r.proto.getName(), reps);
		}

		if (reps.contains(r)) {
			return;
		} else {
			invalidateHappiness();
			reps.add(r);
		}
	}
	
	/**
	 * Increment the cells around the stationary creature with its danger values
	 * @param c the creature
	 */
	private void putStationaryCreature(StationaryCreature c) {
		if (!c.proto.isDangerous()) {
			return;
		}
		placeDangerousCreature(c.pos.x, c.pos.y, 1, c.proto.getHappinessD());
	}
	
	/**
	 * Place a dangerous creature onto the heatmap. This creature must definitely be dangerous,
	 * and there is no checking performed to make sure it is. 
	 * @param s the sighting of the creature itself
	 * @param prob the probability of the creature appearing on the square
	 */
	private void placeDangerousCreature(int x, int y, double prob, double happiness) {
		for (Point p: dangerRadius) {
			Point cur = new Point(x, y);
			cur.setLocation(cur.x + p.x, cur.y + p.y);
			try {
				double old = dangerGetPrivate(cur.x, cur.y);
				dangerSet(cur.x, cur.y, old - 2 * prob * happiness);
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
	}
	
	/**
	 * Put a mobile creature onto the map
	 * @param c the creature to place
	 */
	private void putMobileCreature(MobileCreature c) {
		if (!c.proto.isDangerous()) {
			return;
		}
		
		Point dir = c.getDirection();
		if (dir == null) {
			return;
		}
		
		double [][] probs = MarkovSimulator.simulate(6, c.pos, dir);
		int probsDim = (probs.length - 1) / 2;
		
		for (int i = -probsDim; i < probsDim; i++) {
			for (int j = -probsDim; j < probsDim; j++) {
				placeDangerousCreature(c.pos.x + i, c.pos.y + j, probs[i+probsDim][j+probsDim], c.proto.getHappinessD());
			}
		}
	}
	
	private void refreshDanger() {
		if (danger == null) {
			int size = 2 * dimension + 1;
			danger = new double[size][size];
		} else {
			return;
		}
		
		for (Integer s: stationaryCreatures.keySet()) {
			putStationaryCreature(stationaryCreatures.get(s));
		}
		for (Integer s: movingCreatures.keySet()) {
			putMobileCreature(movingCreatures.get(s));
		}
	}
	
	private void invalidateDanger() {
		danger = null;
	}
	
	private void invalidateHappiness() {
		happiness = null;
	}
	
	private void refreshHappiness() {
		if (happiness == null) {
			int size = 2 * dimension + 1;
			happiness = new double[size][size];
		} else {
			return;
		}
		
		for (String species: stationaryReports.keySet()) {
			HashSet<CreatureReport> reps = stationaryReports.get(species);
			if (reps == null) {
				continue;
			}
			for (CreatureReport r: reps) {
				placeStationaryHappiness(radius, r);
			}
		}
	}
	
	private void placeStationaryHappiness(int radius, CreatureReport c) {
		int squares = 0;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				if (Math.sqrt(i*i + j*j) < radius) {
					continue;
				}
				
				if (seenPoints.contains(new Point(c.reportPos.x + i, c.reportPos.y + j))) {
					continue;
				}
				
				squares++;
			}
		}
		double diffuseHappiness = c.proto.getHappinessD() / squares;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				if (Math.sqrt(i*i + j*j) < radius) {
					continue;
				}
				
				if (seenPoints.contains(new Point(c.reportPos.x + i, c.reportPos.y + j))) {
					continue;
				}
				
				try {
					happySet(c.reportPos.x + i, c.reportPos.y +j, diffuseHappiness);
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
	}
	
	public void reportLocation(Point2D m) {
		Point p = new Point((int) m.getX(), (int) m.getY());
		seenPoints.add(p);
		refreshHappiness();
		for (int i = 0; i < happiness.length; i++) {
			for (int j = 0; j < happiness[0].length; j++) {
				System.out.print(" "+happiness[i][j]);
			}
			System.out.println();
		}
		System.exit(1);
	}
	
//	public String toString() {
//		String ret = "";
//		
//		for (int i = -dimension; i <= dimension; i++) {
//			for (int j = -dimension; j <= dimension; j++) {
//				 double d = dangerGet(i, j);
//				 if (Math.abs(d) > 0.00001) {
//					 ret += ("("+i+","+j+") -> "+d+"\n");
//				 }
//			}
//		}
//		
//		return ret;
//	}
}
