package com.xenoage.utils.collections;

import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/**
 * Interface for immutable lists.
 * 
 * Deprecated annotations are used to warn the programmer of calling
 * unsupported methods.
 * 
 * @author Andreas Wenger
 */
public interface IList<T>
	extends List<T>, RandomAccess {

	@Deprecated @Override public boolean add(T e);

	@Deprecated @Override public void add(int index, T element);

	@Deprecated @Override public boolean addAll(Collection<? extends T> c);

	@Deprecated @Override public boolean addAll(int index, Collection<? extends T> c);

	@Deprecated @Override public void clear();

	public T getFirst();

	public T getLast();

	@Deprecated @Override public boolean remove(Object o);

	@Deprecated @Override public T remove(int index);

	@Deprecated @Override public boolean removeAll(Collection<?> c);

	@Deprecated @Override public boolean retainAll(Collection<?> c);

	@Deprecated @Override public T set(int index, T element);

	@Override public IList<T> subList(int fromIndex, int toIndex);
}
