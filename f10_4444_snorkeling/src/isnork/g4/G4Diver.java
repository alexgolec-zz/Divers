/**
 * 
 */
package isnork.g4;

import isnork.g4.util.ClusteringStrategy;
import isnork.g4.util.MessageMap;
import isnork.g4.util.HeatMap;
import isnork.g4.util.Strategy;
import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * @author Lenovo
 *
 */
public class G4Diver extends Player {
	
	Strategy strategy = null;
	HeatMap seaBoard = null;
	
	/* (non-Javadoc)
	 * @see isnork.sim.Player#getName()
	 */
	@Override
	public String getName() {
		return "G4 Diver";
	}

	/** Set of possible species we have seen. */
	private Set<SeaLifePrototype> seaLifePrototypes;
	/** Mapping form species to communication code. */
	private MessageMap messageMap;
	/** Mapping from name to SeaLifePrototype. This field is meant to be read-only... */
	private static Hashtable<String, SeaLifePrototype> species = null;
	
	/**
	 * Build the set of name to prototype mappings
	 * @param possibilities the possible species on the board
	 */
	private static void constructPossibilitiesMap(Collection<SeaLifePrototype> possibilities) {
		species = new Hashtable<String, SeaLifePrototype>();
		for (SeaLifePrototype p: possibilities) {
			species.put(p.getName(), p);
		}
	}
	
	/* (non-Javadoc)
	 * @see isnork.sim.Player#newGame(java.util.Set, int, int, int, int)
	 */
	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int d, int r, int n) {
		if (species == null) {
			constructPossibilitiesMap(seaLifePossibilites);
		}
		
		seaBoard = new HeatMap(seaLifePossibilites, d);
		strategy = new Strategy(seaLifePossibilites, penalty, d, r, n, random);
		
		seaLifePrototypes = seaLifePossibilites;
		messageMap = new MessageMap(seaLifePossibilites);

		ClusteringStrategy.getInstance().initialize(d, n, getId());
	}
	/**
	 * Convert a string name to a prototype
	 * @param name the name
	 * @return the prototype, or null if it wasn't found
	 */
	public static SeaLifePrototype getProtoFromName(String name) {
		try {
			return species.get(name);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Return the name of the most valuable species. This is where the bulk of 
	 * the communication dispatching logic will want to live.
	 * TODO Put it here. 
	 * @param species the set of species
	 * @return the name of the most valuable species, or null if there is 
	 * nothing to report
	 */
	private String chooseSpeciesToReport(Collection<SeaLifePrototype> species) {
		String mostValuable = null;
		
		for (SeaLifePrototype s: species) {
			if (mostValuable == null) {
				mostValuable = s.getName();
				continue;
			}
			mostValuable = messageMap.moreValuable(mostValuable, s.getName());
		}
		return mostValuable;
	}
	
	/**
	 * Convenience function that takes a collection of observations and returns a set of 
	 * species that those observations correspond to. 
	 * @param set the collection of observations
	 * @return the set of prototype objects for that collection
	 */
	private Set<SeaLifePrototype> getSpeciesFromObservations(Collection<Observation> set) {
		HashSet<SeaLifePrototype> ret = new HashSet<SeaLifePrototype>();
		for (Observation o: set) {
			SeaLifePrototype proto = getProtoFromName(o.getName());
			// There must be a prototype for that 
			assert(proto == null && o.getId() > 0);
			if (proto != null) {
				ret.add(proto);
			}
		}
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see isnork.sim.Player#tick(java.awt.geom.Point2D, java.util.Set, java.util.Set, java.util.Set)
	 */
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
		
		strategy.updateAfterEachTick(myPosition, whatYouSee, incomingMessages, playerLocations, getId());
		
		Set<SeaLifePrototype> visibleSpecies = getSpeciesFromObservations(whatYouSee); 
		
		String speciesToReport = chooseSpeciesToReport(visibleSpecies);
		String message = messageMap.get(speciesToReport);
		if (message != null) {
			System.out.println("Reporting species " + speciesToReport + " (" + message + ")");
		}
		return message;
	}

	/* (non-Javadoc)
	 * @see isnork.sim.Player#getMove()
	 */
	@Override
	public Direction getMove() {
//		System.out.println(ClusteringStrategy.getInstance().toString());
		System.out.println(" ------------------------- " + getId());
		Direction d = strategy.getMove(getId());
		System.out.println(" ------------------------- getMove - " + getId() + " -DIR- " + d);
		return d;
	}
}
