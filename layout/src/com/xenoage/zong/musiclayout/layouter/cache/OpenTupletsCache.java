package com.xenoage.zong.musiclayout.layouter.cache;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.ChordStampings;
import lombok.val;

import java.util.HashMap;
import java.util.Iterator;

import static com.xenoage.utils.backports.HashMapBackports.computeIfAbsent;
import static com.xenoage.utils.collections.CollectionUtils.map;

/**
 * Cache for tuplets which still have to be created.
 * 
 * @author Andreas Wenger
 */
public class OpenTupletsCache
	implements Iterable<Tuplet> {

	//tuplets, whose bracket/number are not stamped yet
	private HashMap<Tuplet, HashMap<Chord, ChordStampings>> openChords = map();


	/**
	 * Adds the given {@link ChordStampings} belonging to the given {@link Chord}
	 * with the given {@link Tuplet} to the cache.
	 */
	public void addChord(Chord chord, Tuplet tuplet, ChordStampings chordStampings) {
		val tupletData = computeIfAbsent(openChords, tuplet, k -> new HashMap<>());
		tupletData.put(chord, chordStampings);
	}

	/**
	 * Gets the {@link ChordStampings} for the given {@link Chord}
	 * with the given {@link Tuplet}, or null if unknown.chord.getTuplet()
	 */
	public ChordStampings getChord(Chord chord, Tuplet tuplet) {
		HashMap<Chord, ChordStampings> tupletData = openChords.get(tuplet);
		if (tupletData != null) {
			return tupletData.get(chord);
		}
		else {
			return null;
		}
	}

	/**
	 * Gets an iterator for all open tuplets.
	 */
	@Override public Iterator<Tuplet> iterator() {
		return openChords.keySet().iterator();
	}

}
