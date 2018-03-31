package com.xenoage.utils.iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.xenoage.utils.collections.CList.ilist;

/**
 * Iterable iterator around a given iterator,
 * that allows no modifications.
 * 
 * It can be used within a foreach statement.
 * There is also a method that returns the current index.
 * 
 * @author Andreas Wenger
 */
public final class It<T>
	implements Iterator<T>, Iterable<T> {
	
	private static final List<Object> emptyList = ilist();

	private final Iterator<T> iterator;
	private int currentIndex = -1;


	/**
	 * Creates a new {@link It} for the given {@link Collection}.
	 * If null is given, a valid iterator with no elements is returned.
	 */
	@SuppressWarnings("unchecked")
	public It(Collection<T> collection) {
		if (collection != null)
			this.iterator = collection.iterator();
		else
			this.iterator = ((Collection<T>) emptyList).iterator();
	}

	/**
	 * Creates a new {@link It} for the given {@link Collection}.
	 * If null is given, a valid iterator with no elements is returned.
	 */
	public static <T> It<T> it(Collection<T> collection) {
		return new It<>(collection);
	}

	@Override public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override public T next()
		throws NoSuchElementException {
		currentIndex++;
		return iterator.next();
	}

	@Override public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override public Iterator<T> iterator() {
		return this;
	}

	public int getIndex() {
		return currentIndex;
	}

}
