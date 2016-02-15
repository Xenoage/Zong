package com.xenoage.zong.musiclayout.notation;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.HashMap;
import java.util.List;

import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;

import lombok.Data;

/**
 * Collection of already computed {@link Notation}s.
 * 
 * A {@link Notation} is queried by the {@link MusicElement} it belongs
 * to. Also a staff index may be given, because for {@link ColumnElement}s
 * the same element is found on all staves but with different notations.
 * 
 * When querying for an element, the staff index can also be provided. If
 * nothing can be found at the given staff, the staff-less cache is used.
 * But if an element is registered with a staff but searched for without
 * a staff, it won't be found.
 * 
 * @author Andreas Wenger
 */
public class Notations {

	@Data private static class StaffElement {
		final MusicElement element;
		final int staff;
	}

	private HashMap<Object, Notation> notations;

	public Notations() {
		this.notations = map();
	}

	/**
	 * Adds the given {@link Notation}.
	 * If already there, it is replaced. Use this method only for elements which appear
	 * only on a single staff (like chords or clefs). If they may appear on multiple
	 * staves (like time signatures), use {@link #add(Notation, int)} instead.
	 */
	public void add(Notation notation) {
		if (notation != null)
			notations.put(notation.getElement(), notation);
	}

	/**
	 * Adds the given {@link Notation}, that belongs to the given and staff.
	 * If already there, it is replaced.
	 */
	public void add(Notation notation, int staff) {
		if (notation != null)
			notations.put(new StaffElement(notation.getElement(), staff), notation);
	}

	@Deprecated
	public void merge(Notations cache) {
		this.notations.putAll(cache.notations);
	}

	/**
	 * Gets the {@link Notation}, that belongs to the given {@link MusicElement},
	 * or null if unknown. It will also return null if the element was registered
	 * with a staff before. See {@link #add(Notation, MusicElement)}.
	 */
	public Notation get(MusicElement element) {
		return notations.get(element);
	}

	/**
	 * Gets the {@link Notation}, that belongs to the given {@link MusicElement}
	 * and staff index. If it is not found there, the staff-less cache is searched.
	 * If still not found, null is returned.
	 */
	public Notation get(MusicElement element, int staff) {
		Notation ret = notations.get(new StaffElement(element, staff));
		if (ret == null)
			ret = notations.get(element);
		return ret;
	}

	/**
	 * Gets the {@link ChordNotation}, that belongs to the given {@link Chord}.
	 * If the notation does not exist yet, it is created.
	 */
	public ChordNotation getChord(Chord chord) {
		ChordNotation ret = (ChordNotation) get(chord);
		if (ret == null)
			add(ret = new ChordNotation(chord));
		return ret;
	}
	
	/**
	 * Gets the {@link ChordNotation}s of the given {@link Beam}.
	 */
	public List<ChordNotation> getBeamChords(Beam beam) {
		List<ChordNotation> ret = alist(beam.size());
		for (BeamWaypoint wp : beam.getWaypoints())
			ret.add(getChord(wp.getChord()));
		return ret;
	}

	public int size() {
		return notations.size();
	}

	@Override public String toString() {
		return "[" + getClass().getSimpleName() + " with " + notations.size() + " elements]";
	}

}
