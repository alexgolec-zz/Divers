package isnork.g4.util;

import isnork.sim.SeaLifePrototype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

public class MessageMap {

	private Hashtable<SeaLifePrototype, String> table;
	
	private class SeaLifePrototypeComparator implements Comparator<SeaLifePrototype>{
		@Override
		public int compare(SeaLifePrototype o1, SeaLifePrototype o2) {
			return o1.getHappiness() - o2.getHappiness();
		}		
	}
	
	public MessageMap(Collection<SeaLifePrototype> c) {
		ArrayList<SeaLifePrototype> lst = new ArrayList<SeaLifePrototype>(c);
		Collections.sort(lst, new SeaLifePrototypeComparator());
		char currentCharacter = 'a';
		for (SeaLifePrototype p: lst) {
			table.put(p, String.valueOf(currentCharacter));
			
			if (currentCharacter != 'z') {
				currentCharacter++;
			}
		}
	}
	
	public String get(SeaLifePrototype p) {
		return table.get(p);
	}
}
