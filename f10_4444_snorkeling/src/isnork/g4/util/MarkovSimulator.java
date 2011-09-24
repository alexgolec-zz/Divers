package isnork.g4.util;

import isnork.g4.G4Diver;

import java.awt.Point;
import java.awt.geom.Point2D;

public class MarkovSimulator {
	private static void simulateRecursively(
			int remainingTicks,
			Point pos,
			Point origin,
			Point dir,
			double[][] aggregate,
			double prob) {
		if (remainingTicks == 0) {
			return;
		} else {
			int dim = (aggregate.length - 1) / 2;
			Point nextPos = new Point();
			nextPos.setLocation(pos.getX() + dir.getX(), pos.getY() + dir.getY());
			for (Point2D p: G4Diver.neighbors.keySet()) {
				double moveProb;
				Point thisPos = new Point();
				thisPos.setLocation(p.getX() + origin.getX(), p.getY() + origin.getY());
				if (thisPos.equals(nextPos)) {
					moveProb = 0.79 * prob;
				} else {
					moveProb = 0.03 * prob;
				}
				aggregate[pos.x - thisPos.x + dim][pos.y - thisPos.y + dim] += moveProb;
				Point newDir = new Point();
				newDir.setLocation(p.getX(), p.getY());
				simulateRecursively(remainingTicks - 1, thisPos, origin, newDir, aggregate, moveProb);
			}
		}
	}
	
	/**
	 * Simulate some number of ticks ahead
	 * @param ticks the number of ticks ahead to simulate
	 * @param pos the position to start with
	 * @param dir the Point value of the Direction in which the creature is moving
	 * @return array of probabilities for the position of the creature
	 */
	public static float[][] simulate(int ticks, Point pos, Point dir) {
		pos = new Point();
		pos.setLocation(0, 0);
		dir = new Point();
		dir.setLocation(0, 1);
		double[][] f = new double[11][11];
		simulateRecursively(6, pos, pos, dir, f, 1);
		
		for (int i = 0; i < f.length; i++) {
			for (int j = 0; j < f[0].length; j++) {
				System.out.print(f[i][j]+" ");
			}
			System.out.println();
		}
		
		System.exit(0);
		
		return null;
	}
}
