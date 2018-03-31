package com.xenoage.utils.collections;

import java.util.Collection;
import java.util.List;

import static com.xenoage.utils.kernel.Range.range;

/**
 * Some useful functions for arrays.
 *
 * @author Andreas Wenger
 */
public class ArrayUtils {

	/**
	 * Converts the given {@link Collection} of <code>int</code>s
	 * into an <code>int</code> array.
	 * @param a  the collection, which may not contain null.
	 *           if null, an empty array is returned.
	 */
	public static int[] toIntArray(Collection<Integer> a) {
		if (a == null)
			return new int[0];
		int[] ret = new int[a.size()];
		int i = 0;
		for (int t : a) {
			ret[i] = t;
			i++;
		}
		return ret;
	}

	/**
	 * Converts the given {@link Collection} of <code>String</code>s
	 * into an <code>String</code> array.
	 * @param l  the collection, which may not contain null.
	 *           if null, an empty array is returned.
	 */
	public static String[] toStringArray(List<String> l) {
		if (l == null)
			return new String[0];
		String[] ret = new String[l.size()];
		int i = 0;
		for (String o : l) {
			ret[i] = o;
			i++;
		}
		return ret;
	}

	/**
	 * Creates a copy of the given <code>float</code> array.
	 */
	public static float[] copy(float... a) {
		float[] ret = new float[a.length];
		for (int i = 0; i < a.length; i++)
			ret[i] = a[i];
		return ret;
	}

	/**
	 * Gets the index of the given element within the given array.
	 * @return  the index, or null if not found
	 */
	public static <T> int indexOf(T[] a, T e) {
		for (int i : range(a)) {
			if ((a[i] == null && e == null) || a[i].equals(e)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the sum of the given <code>int</code> array.
	 */
	public static int sum(int[] a) {
		int ret = 0;
		for (int v : a)
			ret += v;
		return ret;
	}

	/**
	 * Returns the sum of the given <code>float</code> array.
	 */
	public static float sum(float... a) {
		float ret = 0;
		for (float v : a)
			ret += v;
		return ret;
	}

	/**
	 * Returns true, if the given array contains the given object
	 * (same reference, not same contents).
	 */
	public static boolean containsRef(Object[] a, Object o) {
		for (Object e : a)
			if (e == o)
				return true;
		return false;
	}

	/**
	 * Returns true, if the given array contains a <code>null</code> element.
	 */
	public static boolean containsNull(Object... a) {
		return containsRef(a, null);
	}

	/**
	 * Returns true, if the given array contains only <code>null</code> elements.
	 */
	public static <T> boolean containsOnlyNull(T... c) {
		for (Object e : c)
			if (e != null)
				return false;
		return true;
	}
	
	/**
	 * Returns the entry with the lowest value.
	 * The array may not be empty.
	 */
	public static int min(int... a) {
		int ret = a[0];
		for (int v : a)
			if (v < ret)
				ret = v;
		return ret;
	}

	/**
	 * Returns the entry with the lowest value.
	 * The array may not be empty.
	 */
	public static <T extends Comparable<T>> T min(T[] c) {
		T ret = c[0];
		for (T t : c)
			if (t.compareTo(ret) < 0)
				ret = t;
		return ret;
	}
	
	/**
	 * Returns the entry with the highest value.
	 * The array may not be empty.
	 */
	public static int max(int... a) {
		int ret = a[0];
		for (int v : a)
			if (v > ret)
				ret = v;
		return ret;
	}

	/**
	 * Returns the entry with the highest value.
	 * The array may not be empty.
	 */
	public static <T extends Comparable<T>> T max(T[] c) {
		T ret = c[0];
		for (T t : c)
			if (t.compareTo(ret) > 0)
				ret = t;
		return ret;
	}
	
	/**
	 * Gets the index of the entry with the maximum value,
	 * or -1 if the array is empty.
	 */
	public static int getMaxIndex(float... a) {
		int ret = -1;
		float max = Float.MIN_VALUE;
		for (int i : range(a)) {
			if (a[i] > max) {
				max = a[i];
				ret = i;
			}
		}
		return ret;
	}

	/**
	 * Returns the given array as a human-readable comma-separated String,
	 * e.g. <code>[0,1,2]</code> as <code>"0, 1, 2"</code>.
	 */
	public static <T> String toString(T... c) {
		StringBuilder s = new StringBuilder();
		if (c.length > 0) {
			for (int i : range(0, c.length - 2))
				s.append(c[i] + ", ");
			s.append(c[c.length - 1]);
		}
		return s.toString();
	}
	
	public static <T> T getFirst(T... a) {
		return a[0];
	}
	
	public static <T> T getLast(T... a) {
		return a[a.length - 1];
	}
	
	public static int getFirst(int... a) {
		return a[0];
	}
	
	public static int getLast(int... a) {
		return a[a.length - 1];
	}

	public static float getFirst(float... a) {
		return a[0];
	}
	
	public static float getLast(float... a) {
		return a[a.length - 1];
	}
	
	/**
	 * Creates a new float array with the given size and default value.
	 */
	public static float[] arrayFloat(int size, float defaultValue) {
		float ret[] = new float[size];
		for (int i = 0; i < size; i++)
			ret[i] = defaultValue;
		return ret;
	}
	
	/**
	 * Creates a new double array with the given size and default value.
	 */
	public static double[] arrayDouble(int size, double defaultValue) {
		double ret[] = new double[size];
		for (int i = 0; i < size; i++)
			ret[i] = defaultValue;
		return ret;
	}
	
	/**
	 * Sets all elements of the given array to the given value.
	 */
	public static <T> void setValues(T[] array, T value) {
		for (int i : range(array))
			array[i] = value;
	}
	
}
