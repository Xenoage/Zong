package com.xenoage.utils.pdlib;

import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Tuple2.t;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.pcollections.TreePVector;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Tuple2;

/**
 * Persistent list.
 * 
 * This list contains efficient producers to create modified
 * versions of this one.
 * 
 * If this is unneeded, because the list will never change, use
 * {@link CList} instead for performance reasons.
 * 
 * Currently logarithmic-time querying, setting, insertion and removal.
 * 
 * Deprecated annotations are used to warn the programmer of calling
 * unsupported methods.
 * 
 * @author Andreas Wenger
 */
public final class PList<T>
	implements IList<T> {

	private final TreePVector<T> data;


	public PList(Collection<T> data) {
		this.data = TreePVector.from(data);
	}

	@SafeVarargs public PList(T... data) {
		ArrayList<T> array = new ArrayList<T>(data.length);
		for (T o : data)
			array.add(o);
		this.data = TreePVector.from(array);
	}

	public PList() {
		this.data = TreePVector.empty();
	}

	public static <T2> PList<T2> plist() {
		return new PList<T2>();
	}

	public static <T2> PList<T2> plist(Collection<T2> data) {
		return new PList<T2>(data);
	}

	@SafeVarargs public static <T2> PList<T2> plist(T2... data) {
		return new PList<T2>(data);
	}

	private PList(TreePVector<T> data) {
		this.data = data;
	}

	@Override public T getFirst() {
		return data.get(0);
	}

	@Override public T getLast() {
		return data.get(data.size() - 1);
	}

	@Deprecated @Override public boolean add(T e) {
		throw new UnsupportedOperationException("Use plus method instead");
	}

	/**
	 * Adds the given element at the end of this list.
	 */
	public PList<T> plus(T e) {
		return new PList<T>(data.plus(e));
	}

	/**
	 * Adds the given element at the end of this list if it is not null.
	 */
	public PList<T> plusNotNull(T e) {
		if (e != null)
			return plus(e);
		else
			return this;
	}

	/**
	 * Adds the given element at index 0.
	 */
	public PList<T> plusFirst(T e) {
		return new PList<T>(data.plus(0, e));
	}

	@Deprecated @Override public void add(int index, T element) {
		throw new UnsupportedOperationException("Use plus method instead");
	}

	public PList<T> plus(int index, T element) {
		return new PList<T>(data.plus(index, element));
	}

	@Deprecated @Override public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException("Use plusAll method instead");
	}

	public PList<T> plusAll(Collection<? extends T> c) {
		return new PList<T>(data.plusAll(c));
	}

	@Deprecated @Override public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException("Use plusAll method instead");
	}

	public PList<T> plusAll(int index, Collection<? extends T> c) {
		return new PList<T>(data.plusAll(index, c));
	}

	@Deprecated @Override public void clear() {
		throw new UnsupportedOperationException("Create an empty instance instead");
	}

	@Override public boolean contains(Object o) {
		return data.contains(o);
	}

	@Override public boolean containsAll(Collection<?> c) {
		return data.containsAll(c);
	}

	@Override public T get(int index) {
		return data.get(index);
	}

	@Override public int indexOf(Object o) {
		return data.indexOf(o);
	}

	@Override public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override public Iterator<T> iterator() {
		return it(data);
	}

	@Override public int lastIndexOf(Object o) {
		return data.lastIndexOf(o);
	}

	@Override public ListIterator<T> listIterator() {
		return data.listIterator();
	}

	@Override public ListIterator<T> listIterator(int index) {
		return data.listIterator(index);
	}

	@Deprecated @Override public boolean remove(Object o) {
		throw new UnsupportedOperationException("Use minus method instead");
	}

	public PList<T> minus(Object o) {
		return new PList<T>(data.minus(o));
	}

	public PList<T> minusFirst() {
		return new PList<T>(data.minus(0));
	}

	@Deprecated @Override public T remove(int index) {
		throw new UnsupportedOperationException("Use minus method instead");
	}

	public PList<T> minus(int index) {
		return new PList<T>(data.minus(index));
	}

	@Deprecated @Override public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Use minusAll method instead");
	}

	public PList<T> minusAll(Collection<?> c) {
		return new PList<T>(data.minusAll(c));
	}

	@Deprecated @Override public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Use intersect method instead");
	}

	public PList<T> intersect(Collection<?> c) {
		LinkedList<T> ret = new LinkedList<T>();
		boolean modified = false;
		for (T e : data) {
			if (c.contains(e)) {
				ret.add(e);
				modified = true;
			}
		}
		if (modified)
			return new PList<T>(ret);
		else
			return this;
	}

	@Deprecated @Override public T set(int index, T element) {
		throw new UnsupportedOperationException("Use with method instead");
	}

	public PList<T> with(int index, T element) {
		return new PList<T>(data.with(index, element));
	}

	/**
	 * Sets the element at the given index. If the index is out of
	 * the bounds of this list, it is extended up to this index
	 * and the gaps are filled with the given fillElement.
	 */
	public PList<T> withExtend(int index, T element, T fillElement) {
		TreePVector<T> data = this.data;
		while (index >= data.size())
			data = data.plus(fillElement);
		return new PList<T>(data.with(index, element));
	}

	/**
	 * Replaces the given old element by the given new one,
	 * or does nothing if the old element was not found.
	 */
	public PList<T> replace(T oldElement, T newElement) {
		int i = indexOf(oldElement);
		if (i > -1)
			return with(i, newElement);
		else
			return this;
	}

	/**
	 * Replaces the given old element by the given new one,
	 * or does nothing if the old element was not found.
	 * If the new element is null, the entry is deleted from the list.
	 */
	public PList<T> replaceOrMinus(T oldElement, T newElement) {
		int i = indexOf(oldElement);
		if (i > -1) {
			if (newElement != null)
				return with(i, newElement);
			else
				return minus(i);
		}
		else
			return this;
	}

	@Override public int size() {
		return data.size();
	}

	/**
	 * Splits this list at the given position. The given index
	 * is the first element of the second part.
	 * This is a costly operation (linear runtime complexity, no memory reuse).
	 */
	public Tuple2<PList<T>, PList<T>> split(int index) {
		index = (index < 0 ? 0 : (index > size() ? size() : index));
		PList<T> p1 = plist();
		PList<T> p2 = plist();
		for (int i = 0; i < index; i++)
			p1 = p1.plus(get(i));
		for (int i = index; i < size(); i++)
			p2 = p2.plus(get(i));
		return t(p1, p2);
	}

	@Override public PList<T> subList(int fromIndex, int toIndex) {
		return new PList<T>(data.subList(fromIndex, toIndex));
	}

	@Override public Object[] toArray() {
		return data.toArray();
	}

	@Override public <T2> T2[] toArray(T2[] a) {
		return data.toArray(a);
	}

	/**
	 * Returns true, if the given collection has the same values as this one,
	 * otherwise false.
	 */
	@Override public boolean equals(Object o) {
		return data.equals(o);
	}

	@Override public int hashCode() {
		return data.hashCode();
	}

	@Override public String toString() {
		return "{size:" + data.size() + ", data:" + data.toString() + "}";
	}

}
