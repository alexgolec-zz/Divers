package isnork.g4.util;

import java.awt.Point;
import java.util.Formatter;

public class MarkovSimulator {
	private static boolean isDiagonal(int dirx, int diry) {
		return ((dirx != 0) && (diry != 0));
	}
	
	static int count;
	
	private static void simulateRecursively(
			int currentTick,
			int totalTicks,
			Point pos,
			Point origin,
			int dirx,
			int diry,
			double prob,
			double [][] aggregator) {
		if (currentTick == totalTicks) {
			int i = pos.x - origin.x;
			int j = pos.y - origin.y;
			int size = (aggregator.length - 1) / 2;
			aggregator[i + size][j + size] += prob;
			return;
		}
		Point oldPos = new Point(pos);
		count += 1.03;
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				boolean diag = isDiagonal(x, y);
				//if ((x == 0 && y == 0) ||
				if (!((diag && currentTick % 3 == 0) || (!diag && currentTick % 2 == 0))) {
					simulateRecursively(currentTick + 1, totalTicks, pos, origin, dirx, diry, prob, aggregator);
					continue;
				}
				
				double newProb;
				if (x == dirx && y == diry) {
					newProb = 0.79;
				} else {
					newProb = .03;
				}
				
				Point newPos = new Point(oldPos.x + x, oldPos.y + y);
				simulateRecursively(currentTick + 1, totalTicks, newPos, origin, x, y, newProb, aggregator);
			}
		}
		
		if (currentTick == 0) {
			for (int i = 0; i < aggregator.length; i++) {
				for (int j = 0; j < aggregator[i].length; j++) {
					aggregator[i][j] /= count;
				}
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
	public static double[][] simulate(int ticks, Point pos, Point dir) {
		// One move immediately, then up to one every 2 ticks
		int size = 2 * (1 + ticks / 2) + 1;
		double [][] ret = new double[size][size];
		count = 0;
		simulateRecursively(0, ticks, pos, pos, dir.x, dir.y, 1, ret);
		
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[i].length; j++) {
				double d = ret[i][j];
				if (i == ticks && j == ticks) {
//					System.out.print("**");
				}
//				System.out.print(String.format("%1.2g ", d));
			}
//			System.out.println();
		}
		
		return ret;
	}
}
