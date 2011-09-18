/**
 * 
 */
package isnork.g4;

import java.awt.geom.Point2D;
import java.util.Set;

import isnork.g4.util.ClusteringStatergy;
import isnork.g4.util.SeaBoard;
import isnork.g4.util.Statergy;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;

/**
 * @author Lenovo
 *
 */
public class G4Diver extends Player {

	/* (non-Javadoc)
	 * @see isnork.sim.Player#getName()
	 */
	@Override
	public String getName() {
		return "G4 Diver";
	}

	/* (non-Javadoc)
	 * @see isnork.sim.Player#newGame(java.util.Set, int, int, int, int)
	 */
	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int d, int r, int n) {
		
		SeaBoard  seaBoard = new SeaBoard(seaLifePossibilites, d);
		
		Statergy statergy = new Statergy(seaLifePossibilites, penalty, d, r, n, random);
		
		ClusteringStatergy.getInstance().initialize(d, n, getId());
		
		
	}

	/* (non-Javadoc)
	 * @see isnork.sim.Player#tick(java.awt.geom.Point2D, java.util.Set, java.util.Set, java.util.Set)
	 */
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see isnork.sim.Player#getMove()
	 */
	@Override
	public Direction getMove() {
		// TODO Auto-generated method stub
		System.out.println(" ->>> " + ClusteringStatergy.getInstance().toString());
		return null;
	}

}
