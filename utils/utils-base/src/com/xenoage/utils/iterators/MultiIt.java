package com.xenoage.utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator for multiple iterators.
 * They are iterated one after the other.
 * 
 * @author Andreas Wenger
 */
public class MultiIt<T>
	implements Iterator<T>, Iterable<T> {

	private final Iterator<? extends T>[] iterators;
	private int index = 0;


	@SafeVarargs public MultiIt(Iterator<? extends T>... iterators) {
		if (iterators.length == 0)
			throw new IllegalArgumentException("At least one iterator must be given");
		this.iterators = iterators;
	}

	@SafeVarargs public static <T> MultiIt<T> multiIt(Iterator<? extends T>... iterators) {
		return new MultiIt<>(iterators);
	}

	@Override public boolean hasNext() {
		if (!iterators[index].hasNext()) {
			if (index + 1 < iterators.length) {
				index++;
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
		if (!hasNext())
			throw new NoSuchElementException();
		return iterators[index].next();
	}

	@Override public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override public Iterator<T> iterator() {
		return this;
	}

}
