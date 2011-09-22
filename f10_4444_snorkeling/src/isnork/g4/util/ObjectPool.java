package isnork.g4.util;

import java.util.Vector;

public class ObjectPool<T> {
	private Vector<T> objects;
	private int current;
	private Class<T> objectClass;
	
	public ObjectPool(Class<T> c) {
		objectClass = c;
		objects = new Vector<T>();
		reset();
	}
	
	public T get() {
		if (current >= objects.size()) {
			try {
				System.out.println("Creating object number "+current);
				objects.add(objectClass.newInstance());
			} catch (InstantiationException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			}
		}
		
		int index = current;
		current++;
		return objects.get(index);
	}
	
	public void reset() {
		current = 0;
	}
}
