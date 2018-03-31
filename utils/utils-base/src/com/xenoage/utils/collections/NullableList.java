package com.xenoage.utils.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class to save memory when using {@link List}s.
 * 
 * When the list is empty, it is null. As soon as it has at least one
 * element, it is not null any more.
 * 
 * It can be used to modify nullable {@link List}s in a comforable way
 * without null checks, e.g.:
 * <code>object.setElements(NullableList.add(object.getElements(), newElement);</code>.
 * 
 * For looping over the list, use {@link #it(List)} for getting an {@link Iterable}
 * or {@link #it(List)} and {@link #get(List, int)}.
 * 
 * When a new list is created, the {@link ArrayList} class is used.
 * 
 * @deprecated: Nice hack, but we should prefer empty lists instead of null (e.g. Collections.emptyList()
 * 
 * @author Andreas Wenger
 */
@Deprecated
public class NullableList {

	private static final List<?> emptyList = Collections.emptyList();


	/**
	 * Adds the given element to the given list.
	 * If the list was null, it is instantiated now.
	 * The modified list is returned.
	 */
	public static <T> List<T> add(List<T> list, T e) {
		if (list == null)
			list = new ArrayList<>();
		list.add(e);
		return list;
	}

	/**
	 * Removes the given element from the given list.
	 * If the list is empty now, null is returned, otherwise
	 * the modified list is returned.
	 */
	public static <T> List<T> remove(List<T> list, T e) {
		if (list == null)
			return null;
		list.remove(e);
		if (list.size() == 0)
			return null;
		return list;
	}

	/**
	 * Removes the element with the given index from the given list.
	 * If the list is empty now, null is returned, otherwise
	 * the modified list is returned.
	 */
	public static <T> List<T> remove(List<T> list, int index) {
		if (list == null)
			return null;
		list.remove(index);
		if (list.size() == 0)
			return null;
		return list;
	}

	/**
	 * Gets an iterable over the given list, even when it is null.
	 */
	@SuppressWarnings("unchecked") public static <T> Iterable<T> it(List<T> list) {
		if (list == null)
			return (List<T>) emptyList; //this works, see Collections.emptyList()
		return list;
	}

	/**
	 * Gets the size of the given list, or 0 when it is null.
	 */
	public static <T> int size(List<T> list) {
		if (list == null)
			return 0;
		return list.size();
	}

	/**
	 * Gets the element at the given index, or throws an {@link IndexOutOfBoundsException}
	 * if the index is out of bounds or the list is null.
	 */
	public static <T> T get(List<T> list, int index) {
		if (list == null)
			throw new IndexOutOfBoundsException();
		return list.get(index);
	}

}
