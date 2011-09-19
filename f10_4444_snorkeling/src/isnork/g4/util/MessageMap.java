package isnork.g4.util;

import isnork.g4.G4Diver;
import isnork.sim.SeaLifePrototype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

public class MessageMap {

	private Hashtable<String, String> table;
	
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
		table = new Hashtable<String, String>(c.size());
		
		ArrayList<SeaLifePrototype> lst = new ArrayList<SeaLifePrototype>(c);
		Collections.sort(lst, new SeaLifePrototypeComparator());
		char currentCharacter = 'a';
		for (SeaLifePrototype p: lst) {
			table.put(p.getName(), String.valueOf(currentCharacter));
			System.out.println("Species " + p.getName() + " (" + 
					p.getHappiness() + ") will be reported by " + currentCharacter);
			
			if (currentCharacter != 'z') {
				currentCharacter++;
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
