package com.xenoage.utils.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Useful methods for working with collections.
 * 
 * @author Andreas Wenger
 */
public final class CollectionUtils {

	/**
	 * Creates a new empty {@link ArrayList} with the inferred type.
	 */
	public static <T> ArrayList<T> alist() {
		return new ArrayList<>();
	}

	/**
	 * Creates a new empty {@link ArrayList} with the inferred type
	 * using the given capacity.
	 */
	public static <T> ArrayList<T> alist(int initialCapacity) {
		return new ArrayList<>(initialCapacity);
	}
	
	/**
	 * Creates a new {@link ArrayList} with the inferred type
	 * using the given elements.
	 */
	public static <T> ArrayList<T> alist(Collection<T> vals) {
		ArrayList<T> ret = new ArrayList<>(vals.size());
		for (T v : vals)
			ret.add(v);
		return ret;
	}

	/**
	 * Creates a new {@link ArrayList} with the inferred type
	 * using the given elements.
	 */
	@SafeVarargs public static <T> ArrayList<T> alist(T... vals) {
		ArrayList<T> ret = new ArrayList<>(vals.length);
		for (T v : vals)
			ret.add(v);
		return ret;
	}

	/**
	 * Creates a new {@link ArrayList} with the inferred type
	 * using the given elements.
	 */
	public static <T> ArrayList<T> alist(Iterable<T> vals) {
		ArrayList<T> ret = new ArrayList<>();
		for (T v : vals)
			ret.add(v);
		return ret;
	}

	/**
	 * Creates a new {@link ArrayList} with the inferred type
	 * and size, using the given value for each element.
	 */
	public static <T> ArrayList<T> alistInit(T value, int size) {
		ArrayList<T> ret = new ArrayList<>(size);
		for (int i = 0; i < size; i++)
			ret.add(value);
		return ret;
	}
	
	/**
	 * Creates a new {@link ArrayList} with the inferred type
	 * using the given elements of all given Collections.
	 */
	@SafeVarargs
	public static <T> ArrayList<T> alistFromLists(Collection<T>... lists) {
		//compute size
		int count = 0;
		for (Collection<T> list : lists)
			count += list.size();
		//copy
		ArrayList<T> ret = new ArrayList<>(count);
		for (Collection<T> list : lists)
			ret.addAll(list);
		return ret;
	}

	/**
	 * Creates a new empty {@link HashMap} with the inferred type.
	 */
	public static <T1, T2> HashMap<T1, T2> map() {
		return new HashMap<>();
	}

	/**
	 * Creates a new empty {@link LinkedList} with the inferred type.
	 */
	public static <T> LinkedList<T> llist() {
		return new LinkedList<>();
	}

	/**
	 * Creates a new {@link LinkedList} with the inferred type
	 * using the given elements.
	 */
	@SafeVarargs public static <T> LinkedList<T> llist(T... vals) {
		LinkedList<T> ret = new LinkedList<>();
		for (T v : vals)
			ret.add(v);
		return ret;
	}

	/**
	 * Creates a new {@link Set} with the inferred type
	 * using the given elements.
	 */
	public static <T> Set<T> set(T... vals) {
		HashSet<T> ret = new HashSet<>();
		for (T v : vals)
			ret.add(v);
		return ret;
	}
	
	/**
	 * Creates a new {@link Set} with the inferred type
	 * using the given elements.
	 */
	public static <T> Set<T> set(Collection<T> vals) {
		return new HashSet<>(vals);
	}

	/**
	 * Returns the first maximum value of the given collection.
	 * If the collection is empty, null is returned.
	 */
	public static <T extends Comparable<T>> T getMax(Collection<T> vals) {
		return getExtremum(vals, 1);
	}

	/**
	 * Returns the first minimum value of the given collection.
	 * If the collection is empty, null is returned.
	 */
	public static <T extends Comparable<T>> T getMin(Collection<T> vals) {
		return getExtremum(vals, -1);
	}

	private static <T extends Comparable<T>> T getExtremum(Collection<T> vals, int comparator) {
		if (vals.size() == 0)
			return null;
		Iterator<T> it = vals.iterator();
		T ret = it.next();
		while (it.hasNext()) {
			T v = it.next();
			if (v.compareTo(ret) * comparator > 0)
				ret = v;
		}
		return ret;
	}

	/**
	 * Gets the element in the given list with the given index,
	 * or null if the list is null or if the index is outside the range
	 * or if the value at the given index is null.
	 */
	public static <T> T getOrNull(List<T> c, int index) {
		if (c == null)
			return null;
		if (index < 0 || index >= c.size())
			return null;
		return c.get(index);
	}
	
	/**
	 * Returns true, if the given collection contains the given object
	 * (same reference, not same contents).
	 */
	public static <T> boolean containsRef(Collection<T> c, T o) {
		for (Object e : c)
			if (e == o)
				return true;
		return false;
	}

	/**
	 * Returns true, if the given collection contains a <code>null</code> element.
	 */
	public static boolean containsNull(Collection<?> c) {
		return containsRef(c, null);
	}
	
	/**
	 * Returns true, if the given collection contains only <code>null</code> elements.
	 */
	public static boolean containsOnlyNull(Collection<?> c) {
		for (Object e : c)
			if (e != null)
				return false;
		return true;
	}

	/**
	 * Returns the sum of the items of the given collection.
	 */
	public static float sum(Collection<Float> c) {
		float ret = 0;
		for (float i : c)
			ret += i;
		return ret;
	}

	/**
	 * Finds the value that belongs to the given key in the given map.
	 * If the key is null or no value is found, an {@link IllegalStateException} is thrown.
	 */
	public static <T1, T2> T2 find(T1 key, Map<T1, T2> map)
		throws IllegalStateException {
		if (key == null)
			throw new IllegalStateException("Key is null");
		T2 ret = map.get(key);
		if (ret == null)
			throw new IllegalStateException("No value for key " + key);
		return ret;
	}

	/**
	 * In the given list, sets the element at the given index. If the index is out of
	 * the bounds of this list, it is extended up to this index
	 * and the gaps are filled with the given fillElement. If the given list is null,
	 * an {@link ArrayList} is created. The modified list is returned.
	 */
	public static <T> List<T> setExtend(List<T> list, int index, T element, T fillElement) {
		if (list == null)
			list = new ArrayList<>(index + 1);
		while (index >= list.size())
			list.add(fillElement);
		list.set(index, element);
		return list;
	}
	
	/**
	 * Merges the given two lists in a new list, but without duplicates.
	 */
	public static <T> List<T> mergeNoDuplicates(List<T> sourceList1, List<T> sourceList2) {
		List<T> ret = alist(sourceList1.size() + sourceList2.size());
		ret.addAll(sourceList1);
		for (T e : sourceList2) {
			if (false == ret.contains(e))
				ret.add(e);
		}
		return ret;
	}
	
	/**
	 * Adds the given element to the given list, if it is not null.
	 * The modified list is returned for convenience.
	 */
	public static <T> List<T> addNotNull(List<T> list, T element) {
		if (list != null && element != null)
			list.add(element);
		return list;
	}
	
	/**
	 * Returns the given list with the given element added at the end.
	 * If it is an empty list, a new list is created. Otherwise, the given list is reused. 
	 * This method is especially useful for dealing with immutable empty lists,
	 * which are used to save memory.
	 */
	public static <T> List<T> addOrNew(List<T> list, T element) {
		if (list.size() == 0)
			list = new ArrayList<>(1);
		list.add(element);
		return list;
	}
	
	/**
	 * Returns the given list, but without the given element.
	 * If the list is empty then, an immutable empty list is returned.
	 * This method is especially useful for dealing with immutable empty lists,
	 * which are used to save memory.
	 */
	public static <T> List<T> removeOrEmpty(List<T> list, T element) {
		if (list.size() == 0)
			return Collections.emptyList();
		list.remove(element);
		if (list.size() == 0)
			list = Collections.emptyList();
		return list;
	}
	
	/**
	 * Adds the given array elements to the given target list.
	 */
	public static <T> void addAll(List<T> target, T... elements) {
		for (T e : elements)
			target.add(e);
	}
	
	public static <T> T getFirst(List<T> list) {
		return list.get(0);
	}
	
	public static <T> T getLast(List<T> list) {
		return list.get(list.size() - 1);
	}

}
