package com.xenoage.utils.kernel;

import java.util.Collection;
import java.util.Iterator;

/**
 * Class for a range of integers.
 * It can be used like this:
 * 
 * <pre>
 * Range r = new Range(0, 4);
 * for (int i : r)
 *   System.out.print(i + " "); // 0 1 2 3 4
 * </pre>
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public final class Range
	implements Iterable<Integer> {

	private final int start;
	private final int stop;
	private final int step;


	/**
	 * Creates a new range between the given two values (both inclusive).
	 */
	public static Range range(int start, int stop) {
		return new Range(start, (stop < start ? start - 1 : stop), 1);
	}

	/**
	 * Creates a new range between 0 and the given value (exclusive).
	 */
	public static Range range(int count) {
		return new Range(0, count - 1, 1);
	}

	/**
	 * Creates a new range for all indices within the given collection.
	 */
	public static <T> Range range(Collection<T> collection) {
		return new Range(0, collection.size() - 1, 1);
	}

	/**
	 * Creates a new range for all indices within the given {@link Countable}.
	 */
	public static <T> Range range(Countable countable) {
		return new Range(0, countable.getCount() - 1, 1);
	}

	/**
	 * Creates a new range for all indices within the given array.
	 */
	public static <T> Range range(int... a) {
		return new Range(0, a.length - 1, 1);
	}
	
	/**
	 * Creates a new range for all indices within the given array.
	 */
	public static <T> Range range(float... a) {
		return new Range(0, a.length - 1, 1);
	}

	/**
	 * Creates a new range for all indices within the given array.
	 */
	public static <T> Range range(T[] a) {
		return new Range(0, a.length - 1, 1);
	}

	/**
	 * Creates a new range for all indices within the given collection,
	 * but in reverse direction.
	 */
	public static <T> Range rangeReverse(Collection<T> collection) {
		return new Range(collection.size() - 1, 0, -1);
	}
	
	/**
	 * Creates a new range for all indices within the given array,
	 * but in reverse direction.
	 */
	public static <T> Range rangeReverse(int... a) {
		return new Range(a.length - 1, 0, -1);
	}
	
	/**
	 * Creates a new range for all indices within the given array,
	 * but in reverse direction.
	 */
	public static <T> Range rangeReverse(float... a) {
		return new Range(a.length - 1, 0, -1);
	}

	/**
	 * Creates a new range for all indices within the given array,
	 * but in reverse direction.
	 */
	public static <T> Range rangeReverse(T[] a) {
		return new Range(a.length - 1, 0, -1);
	}

	/**
	 * Creates a new range between the given two values (both inclusive),
	 * but in reverse direction.
	 */
	public static <T> Range rangeReverse(int start, int stop) {
		return new Range(start, stop, -1);
	}

	/**
	 * Creates a new reverse range between the given value (exclusive) and 0.
	 */
	public static Range rangeReverse(int count) {
		return new Range(count - 1, 0, -1);
	}

	/**
	 * Creates a new range between the given two values (both inclusive).
	 * In each iteration, the index is increased by the given step.
	 */
	private Range(int start, int stop, int step) {
		this.start = start;
		this.stop = stop;
		this.step = step;
	}

	/**
	 * Returns an iterator over this range.
	 */
	@Override public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private int i = start;


			@Override public boolean hasNext() {
				if (step > 0)
					return i <= stop;
				else
					return i >= stop;
			}

			@Override public Integer next() {
				int ret = i;
				i += step;
				return ret;
			}

			@Override public void remove() {
			}
		};
	}

	/**
	 * Gets the first number.
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Gets the last number.
	 */
	public int getStop() {
		return stop;
	}

	/**
	 * Gets the number of numbers.
	 */
	public int getCount() {
		if (step > 0)
			return stop - start + 1;
		else
			return start - stop + 1;
	}

	/**
	 * Returns whether the given number is in the range or not.
	 * The step is ignored.
	 */
	public boolean isInRange(int number) {
		if (number >= start && number <= stop) {
			return true;
		}
		return false;
	}

	@Override public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + start;
		result = prime * result + step;
		result = prime * result + stop;
		return result;
	}

	@Override public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Range other = (Range) obj;
		return (start == other.start && step == other.step && stop == other.stop);
	}

	@Override public String toString() {
		return "Range [start=" + start + ", stop=" + stop + ", step=" + step + "]";
	}

}
