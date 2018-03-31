package com.xenoage.utils.iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * Iterable iterator around a {@link List} with {@link RandomAccess},
 * that begins with the last element and ends with the first one,
 * allowing no modifications.
 * It can be used within a foreach statement.
 * 
 * @author Andreas Wenger
 */
public final class ReverseIterator<T>
	implements Iterator<T>, Iterable<T> {

	private final List<T> list;
	private int currentIndex;


	/**
	 * Creates a new {@link ReverseIterator} for the given {@link List}.
	 * If null is given, a valid iterator with no elements is returned.
	 */
	public ReverseIterator(List<T> list) {
		if (list != null) {
			if (!(list instanceof RandomAccess))
				throw new IllegalArgumentException("Use this iterator only for RandomAccess lists!");
			this.list = list;
		}
		else {
			this.list = new ArrayList<>(0);
		}
		this.currentIndex = this.list.size();
	}

	/**
	 * Creates a new {@link ReverseIterator} for the given {@link Collection}.
	 * If null is given, a valid iterator with no elements is returned.
	 */
	public static <T> ReverseIterator<T> reverseIt(List<T> list) {
		return new ReverseIterator<>(list);
	}

	@Override public boolean hasNext() {
		return (this.currentIndex > 0);
	}

	@Override public T next()
		throws NoSuchElementException {
		if (hasNext()) {
			this.currentIndex--;
			return list.get(this.currentIndex);
		}
		else {
			throw new NoSuchElementException();
		}
	}

	@Override public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override public Iterator<T> iterator() {
		return this;
	}

}
