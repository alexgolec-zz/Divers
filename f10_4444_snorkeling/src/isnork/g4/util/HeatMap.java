package isnork.g4.util;

import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.Set;

public class HeatMap {
	
	public EachGrid[][] eachGrid;
	
	public HeatMap(Set<SeaLifePrototype> seaLifePossibilites, int d) {
		eachGrid = new EachGrid[(2*d)+1][(2*d)+1];
		for(int i=0; i<=2*d; i++){
			for(int j=0; j<=2*d; j++){
				eachGrid[i][j] = new EachGrid(new Point2D.Double(i,j));
			}
		}
	}
	
	
	

}
