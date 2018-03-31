package com.xenoage.utils.iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator for multiple collections.
 * They are iterated one after the other.
 * 
 * @author Andreas Wenger
 */
public class MultiListIt<T>
	implements Iterator<T>, Iterable<T> {

	private final Collection<? extends T>[] collections;

	private Iterator<? extends T> currentIterator = null;
	private int index = 0;


	@SafeVarargs public MultiListIt(Collection<? extends T>... collections) {
		if (collections.length == 0)
			throw new IllegalArgumentException("At least one collection must be given");
		this.collections = collections;
		this.currentIterator = collections[0].iterator();
	}

	@SafeVarargs public static <T> MultiListIt<T> multiListIt(Collection<? extends T>... collections) {
		return new MultiListIt<>(collections);
	}

	@Override public boolean hasNext() {
		if (!currentIterator.hasNext()) {
			if (index + 1 < collections.length) {
				index++;
				currentIterator = collections[index].iterator();
				return hasNext();
			}
			else {
				return false;
			}
		}
		else {
			return true;
		}
	}

	@Override public T next() {
		if (!currentIterator.hasNext()) {
			if (index + 1 < collections.length) {
				index++;
				currentIterator = collections[index].iterator();
				return next();
			}
			else {
				throw new NoSuchElementException();
			}
		}
		else {
			return currentIterator.next();
		}
	}

	@Override public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override public Iterator<T> iterator() {
		return this;
	}

}
