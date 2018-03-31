package com.xenoage.utils;

import java.util.Collection;

/**
 * Checks the integrity of data structures.
 * 
 * @author Andreas Wenger
 */
public class CheckUtils {

	/**
	 * Throws an {@link IllegalStateException} if the given object is null.
	 * For convenience, the object is returned.
	 */
	public static <T> T checkNotNull(T o)
		throws IllegalStateException {
		if (o == null)
			throw new IllegalStateException();
		return o;
	}

	/**
	 * Throws an {@link IllegalStateException} with the given message
	 * if the given object is null.
	 * For convenience, the object is returned.
	 */
	public static <T> T checkNotNull(T o, String message)
		throws IllegalStateException {
		if (o == null)
			throw new IllegalStateException(message);
		return o;
	}
	
	/**
	 * Throws an {@link IllegalStateException} if the given array is null
	 * or contains null.
	 * For convenience, the array is returned.
	 */
	public static <T> T[] checkNotNullIn(T... a)
		throws IllegalStateException {
		if (a == null)
			throw new IllegalStateException();
		for (T o : a)
			if (o == null)
				throw new IllegalStateException();
		return a;
	}

	/**
	 * Throws an {@link IllegalStateException} if the given collection is null
	 * or contains null.
	 * For convenience, the collection is returned.
	 */
	public static <T> Collection<T> checkNotNullIn(Collection<T> c)
		throws IllegalStateException {
		if (c == null)
			throw new IllegalStateException();
		for (T o : c)
			if (o == null)
				throw new IllegalStateException();
		return c;
	}

	/**
	 * Throws an {@link IllegalStateException} with the given message
	 * if the given collection is null or contains null.
	 * For convenience, the collection is returned.
	 */
	public static <T> Collection<T> checkNotNullIn(Collection<T> c, String message)
		throws IllegalStateException {
		if (c == null)
			throw new IllegalStateException(message);
		for (T o : c)
			if (o == null)
				throw new IllegalStateException(message);
		return c;
	}
	
	/**
	 * Throws an {@link IllegalArgumentException} if one of the given arguments is null.
	 */
	public static void checkArgsNotNull(Object... o) {
		for (int i = 0; i < o.length; i++) {
			if (o[i] == null) {
				throw new IllegalArgumentException(
					"Argument may not be null (checked argument with index " + i + ")");
			}
		}
	}

	/**
	 * Throws an {@link IllegalStateException} if the given collection is null
	 * or is empty.
	 * For convenience, the collection is returned.
	 */
	public static <T> Collection<T> checkNotEmpty(Collection<T> c)
		throws IllegalStateException {
		if (c == null || c.size() == 0)
			throw new IllegalStateException();
		return c;
	}

	/**
	 * Throws an {@link IllegalStateException} with the given message
	 * if the given collection is null or is empty.
	 * For convenience, the collection is returned.
	 */
	public static <T> Collection<T> checkNotEmpty(Collection<T> c, String message)
		throws IllegalStateException {
		if (c == null || c.size() == 0)
			throw new IllegalStateException(message);
		return c;
	}

}
