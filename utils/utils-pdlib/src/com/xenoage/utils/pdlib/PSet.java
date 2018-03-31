package com.xenoage.utils.pdlib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.pcollections.HashTreePSet;
import org.pcollections.MapPSet;

import com.xenoage.utils.collections.ISet;

/**
 * Persistent hash set.
 * 
 * This map contains efficient producers to create modified
 * versions of this one.
 * 
 * Deprecated annotations are used to warn the programmer of calling
 * unsupported methods.
 * 
 * @author Andreas Wenger
 */
public class PSet<T>
	implements ISet<T> {

	private final MapPSet<T> data;


	public PSet() {
		this.data = HashTreePSet.empty();
	}

	private PSet(MapPSet<T> data) {
		this.data = data;
	}

	@Deprecated @Override public boolean add(T e) {
		throw new UnsupportedOperationException("Use plus method instead");
	}

	@Deprecated @Override public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException("Use plusAll method instead");
	}

	@Deprecated @Override public void clear() {
		throw new UnsupportedOperationException("Create an empty instance instead");
	}

	@Override public boolean contains(Object element) {
		return data.contains(element);
	}

	@Override public boolean containsAll(Collection<?> elements) {
		return data.containsAll(elements);
	}

	@Override public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override public Iterator<T> iterator() {
		return data.iterator();
	}

	public PSet<T> minus(T element) {
		return new PSet<T>(data.minus(element));
	}

	public PSet<T> minusAll(Collection<? extends T> elements) {
		return new PSet<T>(data.minusAll(elements));
	}

	public PSet<T> plus(T element) {
		return new PSet<T>(data.plus(element));
	}

	public PSet<T> plusAll(Collection<? extends T> elements) {
		return new PSet<T>(data.plusAll(elements));
	}

	@Deprecated @Override public boolean remove(Object o) {
		throw new UnsupportedOperationException("Use minus method instead");
	}

	@Deprecated @Override public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Use minusAll method instead");
	}

	@Deprecated @Override public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override public int size() {
		return data.size();
	}

	@Override public Object[] toArray() {
		return data.toArray();
	}

	@Override public <T2> T2[] toArray(T2[] a) {
		return data.toArray(a);
	}

	@Override public String toString() {
		return new HashSet<T>(data).toString(); //TIDY
	}

}
