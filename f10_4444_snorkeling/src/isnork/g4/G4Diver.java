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

	/** Mapping form species to communication code. */
	private MessageMap messageMap;
	/** Mapping from name to SeaLifePrototype. This field is meant to be read-only... */
	private static Hashtable<String, SeaLifePrototype> species = null;
	/** Set of IDs of stationary objects that were already reported. */
	private Set<Integer> reportedStationaries;
	/** Map from id to observation of the last observations of the last tick. */
	private Hashtable<Integer, Observation> lastCreatureObservations = null;
	/** The radius of visibility. */
	private int visibilityRadius;
	private DiverHeatmap heatmap;
	
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
		reportedStationaries = new HashSet<Integer>();
		
		seaBoard = new HeatMap(seaLifePossibilites, d);
		strategy = new Strategy(seaLifePossibilites, penalty, d, r, n, random);
		
		messageMap = new MessageMap(seaLifePossibilites);
		visibilityRadius = r;
		heatmap = new DiverHeatmap(d);

		ClusteringStrategy.getInstance().initialize(d, n, getId());
	}
	/**
	 * Convert a string name to a prototype
	 * @param name the name
	 * @return the prototype, or null if it wasn't found
	 */
	public static SeaLifePrototype getProtoFromName(String name) {
		assert(species != null);
		try {
			return species.get(name);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Get a set of observations and filter out the players, leaving just the 
	 * creatures. 
	 * @param observations the set of observations
	 * @return the set of observations without the players
	 */
	private Set<Observation> creaturesFilter(Set<Observation> observations) {
		HashSet<Observation> ret = new HashSet<Observation>();
		
		for (Observation o: observations) {
			if (o.getId() > 0) {
				ret.add(o);
			}
		}
		
		return ret;
	}
	
	private Observation moreValuableObservation(Observation o1, Observation o2) {
		if (messageMap.moreValuable(o1.getName(), o2.getName()) == o1.getName()) {
			return o1;
		} else {
			return o2;
		}
	}
	
	private Observation getMostValuable(Collection<Observation> observations) {
		Observation mostValuable = null;
		
		for (Observation s: observations) {
			if (mostValuable == null) {
				mostValuable = s;
				continue;
			}
			mostValuable = moreValuableObservation(mostValuable, s);
		}
		return mostValuable;
	}
	
	/**
	 * Return the name of the most valuable species. This is where the bulk of 
	 * the communication dispatching logic will want to live.
	 * TODO Put it here. 
	 * @param species the set of species
	 * @return the name of the most valuable species, or null if there is 
	 * nothing to report
	 */
	private String chooseSpeciesToReport(Set<Observation> observations) {
		HashSet<Observation> stationaries = new HashSet<Observation>();
		HashSet<Observation> mobiles = new HashSet<Observation>();
		
		// Partition the list into stationary and nonstationary lists. Also 
		// discard observations of stationaries we've already seen. 
		for (Observation s: observations) {
			if (reportedStationaries.contains(s.getId())) {
				continue;
			}
			if (G4Diver.getProtoFromName(s.getName()).getSpeed() == 0) {
				stationaries.add(s);
			} else {
				mobiles.add(s);
			}
		}
		
		Observation mostValuable = getMostValuable(stationaries);
		if (mostValuable == null) {
			mostValuable = getMostValuable(mobiles);
		}
		
		if (mostValuable == null) {
			return null;
		} else {
			return mostValuable.getName();
		}
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
	
	private Hashtable<Integer, Observation> archiveObservations(Set<Observation> observations) {
		Hashtable<Integer, Observation> ret = new Hashtable<Integer, Observation>();
		for (Observation o: observations) {
			ret.put(o.getId(), o);
		}
		return ret;
	}
	
	private void registerStationariesWithHeatmap(Collection<Observation> observations) {
		for (Observation o: observations) {
			if (getProtoFromName(o.getName()).getSpeed() == 0) {
				heatmap.registerStationary(o);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see isnork.sim.Player#tick(java.awt.geom.Point2D, java.util.Set, java.util.Set, java.util.Set)
	 */
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
		
		strategy.updateAfterEachTick(myPosition, whatYouSee, incomingMessages, playerLocations, getId());
		
		Set<Observation> justCreatures = creaturesFilter(whatYouSee);
		Set<SeaLifePrototype> visibleSpecies = getSpeciesFromObservations(justCreatures); 

		// Notify the heatmap of stationary creatures
		registerStationariesWithHeatmap(justCreatures);
		
		String speciesToReport = chooseSpeciesToReport(justCreatures);
		String message = messageMap.get(speciesToReport);
		if (message != null) {
			System.out.println("Reporting species " + speciesToReport + " (" + message + ")");
		}
		
		lastCreatureObservations = archiveObservations(whatYouSee);
		
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
