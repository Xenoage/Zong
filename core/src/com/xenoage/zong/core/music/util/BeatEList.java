package com.xenoage.zong.core.music.util;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.iterators.ReverseIterator;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.MusicElementType;
import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.util.BeatE.beatE;
import static com.xenoage.zong.core.music.util.Interval.Result.True;

/**
 * This is a wrapper class to combine a list of objects with
 * the beats they belong to.
 * 
 * @author Andreas Wenger
 */
@Data public final class BeatEList<T>
	implements Iterable<BeatE<T>> {


	/** The list of elements, sorted in ascending beat order. */
	private List<BeatE<T>> elements;

	private static BeatEList<?> emptyList = null;


	/**
	 * Returns a new, empty and mutable {@link BeatEList}.
	 */
	public static <T> BeatEList<T> beatEList() {
		val ret = new BeatEList<T>();
		ret.elements = new ArrayList<>(0); //start with a capacity of 0 to save memory
		return ret;
	}

	/** Returns a shared empty, immutable list, that can be used instead of returning a new empty list or null. */
	public static <T> BeatEList<T> emptyBeatEList() {
		if (emptyList == null) {
			emptyList = new BeatEList<MusicElement>();
			emptyList.elements = ilist(); //immutable
		}
		return (BeatEList<T>) emptyList;
	}

	private BeatEList() {
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
	 * Gets the first element at the given beat with the given type, or null if there is none.
	 * Works only for {@link MusicElement} items.
	 */
	public T get(Fraction beat, MusicElementType type) {
		for (BeatE<T> e : elements) {
			if (e.getBeat().equals(beat) && type.is((MusicElement) e.getElement()))
				return e.getElement();
		}
		return null;
	}

	/**
	 * Gets all elements at the given beat in a new list, or an empty list if there are none.
	 */
	public List<T> getAll(Fraction beat) {
		List<T> ret = alist();
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
	 * Gets all used beats (each beat only one time).
	 */
	public List<Fraction> getBeats() {
		List<Fraction> ret = alist(elements.size());
		Fraction lastBeat = null;
		for (BeatE<T> e : elements) {
			if (lastBeat == null || false == lastBeat.equals(e.beat)) {
				ret.add(e.beat);
				lastBeat = e.beat;
			}
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
		for (BeatE<T> e : ReverseIterator.reverseIt(elements)) {
			if (endpoint.isInInterval(e.getBeat(), beat) == True)
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

	/**
	 * Gets an {@link Iterable} to iterate in reverse order, from highest to lowest beat.
	 */
	public Iterable<BeatE<T>> reverseIt() {
		return ReverseIterator.reverseIt(elements);
	}

	/**
	 * Returns a new {@link BeatEList} with only the elements which appear in the
	 * given interval relative to the given beat.
	 */
	public BeatEList<T> filter(Interval interval, Fraction beat) {
		BeatEList<T> ret = beatEList();
		for (val e : elements)
			if (interval.isInInterval(e.beat, beat) == True)
				ret.add(e);
		return ret;
	}

	/**
	 * Creates a mutable copy of this list, that can be further modified.
	 */
	public BeatEList<T> clone() {
		val ret = new BeatEList<T>();
		ret.elements = alist(elements);
		return ret;
	}

}
