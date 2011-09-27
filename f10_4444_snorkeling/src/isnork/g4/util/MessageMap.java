package isnork.g4.util;

import isnork.g4.G4Diver;
import isnork.sim.SeaLifePrototype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class MessageMap {

	/** Mapping from species to message. */
	private Hashtable<String, String> table;
	/** Mapping from message to species. */
	private Hashtable<String, HashSet<String> > reverse;
	
	private class SeaLifePrototypeComparator implements Comparator<SeaLifePrototype>{
		@Override
		public int compare(SeaLifePrototype o1, SeaLifePrototype o2) {
			return - (o1.getHappiness() - o2.getHappiness());
		}
	}
	
	/**
	 * Given a collection of prototypes, create a map such that 
	 * @param c
	 */
	public MessageMap(Collection<SeaLifePrototype> c) {
		table = new Hashtable<String, String>();
		reverse = new Hashtable<String, HashSet<String>>();
		
		ArrayList<SeaLifePrototype> lst = new ArrayList<SeaLifePrototype>(c);
		Collections.sort(lst, new SeaLifePrototypeComparator());
		char currentCharacter = 'a';
		for (SeaLifePrototype p: lst) {
			String message = String.valueOf(currentCharacter);
			table.put(p.getName(), message);
//			System.out.println("Species " + p.getName() + " (" + 
//					p.getHappiness() + ") will be reported by " + currentCharacter);
			
			if (currentCharacter != 'z') {
				currentCharacter++;
				HashSet<String> set = new HashSet<String>();
				set.add(p.getName());
				reverse.put(message, set);
			} else {
				HashSet<String> set;
//				System.out.println(" 1 ");
				try {
					set = reverse.get(message);
					if(set == null){
						set = new HashSet<String>(2);
					}
//					System.out.println(" 2 " + (set==null) + (reverse.get(message)==null));
				} catch (NullPointerException e) {
					set = new HashSet<String>(2);
					reverse.put(message, set);
//					System.out.println(" 3 ");
				}
//				System.out.println(" 4 " + (set==null));
				set.add(p.getName());
			}
		}
	}
	
	/**
	 * Given the name of a species, return the communication code associated with it.
	 * @param name the species name
	 * @return a one-character communication code
	 */
	public String get(String name) {
		// Because NullPointerException is thrown both for when the table was
		// accidentally left as null and when the element is not in the table,
		// we check for null explicitly here. 
		assert(table != null);
		try {
			String ret = table.get(name);
			// There is no reason why this should be any length besides one. 
			assert(ret.length() == 1);
			return ret;
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Take a message and resolve it to a set of possible creatures.
	 * @param message
	 * @return
	 */
	public Set<String> decode(String message) {
		assert(reverse != null);
		try {
			return reverse.get(message);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Return which of the two species yields more happiness
	 * @param species1 the first species
	 * @param species2 the second species
	 * @return the species which yields the greater happiness
	 */
	public String moreValuable(String species1, String species2) {
		if (G4Diver.getProtoFromName(species1).getHappiness() > 
			G4Diver.getProtoFromName(species2).getHappiness()) {
			return species1;
		} else {
			return species2;
		}
	}
}
