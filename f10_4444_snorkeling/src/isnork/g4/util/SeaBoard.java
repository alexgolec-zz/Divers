package isnork.g4.util;

import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.Set;

public class SeaBoard {
	
	public SeaSpace[][] seaSpaceMap;
	
	public SeaBoard(Set<SeaLifePrototype> seaLifePossibilites, int d) {
		seaSpaceMap = new SeaSpace[(2*d)+1][(2*d)+1];
		for(int i=0; i<=2*d; i++){
			for(int j=0; j<=2*d; j++){
				seaSpaceMap[i][j] = new SeaSpace(new Point2D.Double(i,j));
			}
		}
	}
	
	

}
