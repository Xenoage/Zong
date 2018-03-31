package com.xenoage.utils.iterators;

import java.util.ListIterator;

/**
 * {@link ListIterator} with allows no modifications.
 * 
 * @author Andreas Wenger
 */
public class ListIt<T>
	implements ListIterator<T> {

	private final ListIterator<T> iterator;


	private ListIt(ListIterator<T> iterator) {
		this.iterator = iterator;
	}

	/**
	 * Creates a new {@link ListIt} around the given {@link ListIterator}.
	 */
	public static <T> ListIt<T> listIt(ListIterator<T> iterator) {
		return new ListIt<>(iterator);
	}

	@Override public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override public T next() {
		return iterator.next();
	}

	@Override public boolean hasPrevious() {
		return iterator.hasPrevious();
	}

	@Override public T previous() {
		return iterator.previous();
	}

	@Override public int nextIndex() {
		return iterator.nextIndex();
	}

	@Override public int previousIndex() {
		return iterator.previousIndex();
	}

	@Override public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override public void set(T e) {
		throw new UnsupportedOperationException();
	}

	@Override public void add(T e) {
		throw new UnsupportedOperationException();
	}

}
