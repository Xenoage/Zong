package com.xenoage.utils.iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterable iterator around a given iterator, that only delivers objects
 * assignment-compatible to a class (like instanceof), which must also be
 * given as parameter T. This iterator allows no modifications.
 * 
 * It can be used within a foreach statement.
 * There is also a method that returns the current index.
 * 
 * @author Andreas Wenger
 */
public final class ClassFilterIt<T>
	implements Iterator<T>, Iterable<T> {

	private final Class<?> classFilter;
	private final Iterator<?> iterator;

	private int currentIndex = -1;
	private T currentElement = null;
	private boolean currentElementAvailable = true;
	private int nextIndex = -1;


	/**
	 * Creates a new {@link ClassFilterIt} for the given {@link Collection}.
	 * If null is given, a valid iterator with no elements is returned.
	 */
	public ClassFilterIt(Collection<?> collection, Class<?> classFilter) {
		this.classFilter = classFilter;
		if (collection != null) {
			this.iterator = collection.iterator();
			forward();
		}
		else {
			iterator = null;
			currentElementAvailable = false;
		}
	}

	@Override public boolean hasNext() {
		return currentElementAvailable;
	}

	@Override public T next()
		throws NoSuchElementException {
		currentIndex = nextIndex;
		if (currentElementAvailable) {
			T ret = currentElement;
			forward();
			return ret;
		}
		else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Move forward to the next element.
	 */
	@SuppressWarnings("unchecked") private void forward() {
		while (iterator.hasNext()) {
			nextIndex++;
			Object e = iterator.next();
			if (classFilter.isInstance(e)) {
				currentElement = (T) e;
				return;
			}
		}
		currentElementAvailable = false;
	}

	@Override public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override public Iterator<T> iterator() {
		return this;
	}

	/**
	 * Returns the current index, which is the index of the original
	 * iterator, not the index counted by the number of matching objects.
	 */
	public int getIndex() {
		return currentIndex;
	}

}
