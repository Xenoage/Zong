package com.xenoage.zong.core.music.util;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.ReverseIterator.reverseIt;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.util.BeatE.beatE;

import java.util.ArrayList;
import java.util.Iterator;

import lombok.Data;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.math.Fraction;

/**
 * This is a wrapper class to combine a list of objects with
 * the beats they belong to.
 * 
 * @author Andreas Wenger
 */
@Data public final class BeatEList<T>
	implements Iterable<BeatE<T>> {

	/** The list of elements, sorted in ascending beat order. */
	private ArrayList<BeatE<T>> elements;


	public static <T> BeatEList<T> beatEList() {
		return new BeatEList<T>();
	}

	public BeatEList() {
		this.elements = new ArrayList<BeatE<T>>(0); //start with a capacity of 0 to save memory
	}

	private void checkNotNull(BeatE<? extends T> element) {
		if (element.getElement() == null)
			throw new IllegalArgumentException("Element must be assigned to a beat");
	}

	/**
	 * Gets the first element at the given beat, or null if there is none.
	 */
	public T get(Fraction beat) {
		for (BeatE<T> e : elements) {
			if (e.getBeat().equals(beat))
				return e.getElement();
		}
		return null;
	}

	/**
	 * Gets all elements at the given beat, or an empty list if there are none.
	 */
	public ArrayList<T> getAll(Fraction beat) {
		ArrayList<T> ret = alist();
		for (BeatE<T> e : elements) {
			int compare = e.getBeat().compareTo(beat);
			if (compare == 0)
				ret.add(e.getElement());
			else if (compare > 0)
				break;
		}
		return ret;
	}

	/**
	 * Adds the given positioned element.
	 * If there are already elements at this beat, it is added
	 * after the existing ones, but nothing is removed.
	 */
	public void add(BeatE<T> element) {
		checkNotNull(element);
		for (int i : range(elements)) {
			if (element.getBeat().compareTo(elements.get(i).getBeat()) < 0) {
				elements.add(i, element);
				return;
			}
		}
		elements.add(element);
	}

	/**
	 * Adds the given positioned element.
	 * If there are already elements at this beat, it is added
	 * after the existing ones, but nothing is removed.
	 */
	public void add(T element, Fraction beat) {
		add(beatE(element, beat));
	}

	/**
	 * Adds the given positioned elements.
	 * If there are already elements at the respective beat, the given elements are added
	 * after the existing ones, but nothing is removed.
	 */
	public void addAll(BeatEList<? extends T> list) {
		if (list != null)
			for (BeatE<? extends T> e : list)
				add(e.getElement(), e.getBeat());
	}

	/**
	 * Adds the given element at the given beat. If there is already
	 * a element, it is replaced by the given one and returned (otherwise null).
	 */
	public T set(BeatE<T> element) {
		checkNotNull(element);
		for (int i : range(elements)) {
			BeatE<T> e = elements.get(i);
			int compare = element.getBeat().compareTo(e.getBeat());
			if (compare == 0) {
				elements.set(i, element);
				return e.getElement();
			}
			else if (compare < 0) {
				elements.add(i, element);
				return null;
			}
		}
		elements.add(element);
		return null;
	}

	/**
	 * Adds the given element at the given beat. If there is already
	 * a element, it is replaced by the given one and returned (otherwise null).
	 */
	public T set(T element, Fraction beat) {
		return set(beatE(element, beat));
	}

	/**
	 * Removes the first occurrence of the given element.
	 * If found, it is returned, otherwise null.
	 */
	public T remove(T element) {
		for (int i : range(elements)) {
			if (elements.get(i).getElement() == element) {
				return elements.remove(i).getElement();
			}
		}
		return null;
	}

	/**
	 * Removes and returns the first element at the given beat (if there is any).
	 * If not found, null is returned.
	 */
	public T remove(Fraction beat) {
		T e = get(beat);
		if (e != null) {
			remove(e);
			return e;
		}
		return null;
	}

	/**
	 * Gets the first element, or null if the list is empty.
	 */
	public BeatE<T> getFirst() {
		if (elements.size() > 0)
			return elements.get(0);
		else
			return null;
	}

	/**
	 * Gets the last element, or null if the list is empty.
	 */
	public BeatE<T> getLast() {
		if (elements.size() > 0)
			return elements.get(elements.size() - 1);
		else
			return null;
	}

	/**
	 * Returns the last element at or before the given beat.
	 * If there is none, null is returned.
	 */
	public BeatE<T> getLastBefore(Interval endpoint, Fraction beat) {
		for (BeatE<T> e : reverseIt(elements)) {
			if (endpoint.isInInterval(e.getBeat(), beat) == Interval.Result.True)
				return e;
		}
		return null;
	}

	/**
	 * Gets the number of elements in this list.
	 */
	public int size() {
		return elements.size();
	}

	@Override public Iterator<BeatE<T>> iterator() {
		return elements.iterator();
	}

	/**
	 * Gets all data elements.
	 */
	public IList<T> getDataElements() {
		CList<T> ret = clist();
		for (BeatE<T> element : elements)
			ret.add(element.getElement());
		return ret.close();
	}

}
