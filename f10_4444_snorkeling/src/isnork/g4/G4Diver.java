/**
 * 
 */
package isnork.g4;

import isnork.g4.util.ClusteringStrategy;
import isnork.g4.util.SeaBoard;
import isnork.g4.util.Strategy;
import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Set;

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
		
		Strategy statergy = new Strategy(seaLifePossibilites, penalty, d, r, n, random);
		
		ClusteringStrategy.getInstance().initialize(d, n, getId());
		
		
	}
	
	/* (non-Javadoc)
	 * @see isnork.sim.Player#tick(java.awt.geom.Point2D, java.util.Set, java.util.Set, java.util.Set)
	 */
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
		System.out.println(this);
		for (Observation o: whatYouSee) {
			int id = o.getId();
			if (id > 0) {
				System.out.println(id + " - " + o.getName() + " - " + o.happiness());
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see isnork.sim.Player#getMove()
	 */
	@Override
	public Direction getMove() {
		// TODO Auto-generated method stub
		System.out.println(ClusteringStrategy.getInstance().toString());
		return null;
	}

}
