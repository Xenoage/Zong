package com.xenoage.zong.musiclayout.layouter.cache;

import java.util.HashMap;
import java.util.Iterator;

import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.musiclayout.layouter.cache.util.SlurCache;

/**
 * Cache for {@link Slur}s which are still not stamped completely.
 * 
 * @author Andreas Wenger
 */
public class OpenSlursCache
	implements Iterable<SlurCache> {

	//slurs and ties, which are not stamped completely yet
	private HashMap<Slur, SlurCache> openCurvedLines = new HashMap<Slur, SlurCache>();


	/**
	 * Adds the given {@link SlurCache}.
	 */
	public void add(SlurCache tiedChords) {
		openCurvedLines.put(tiedChords.getSlur(), tiedChords);
	}

	/**
	 * Gets the {@link SlurCache} instance for the given {@link Slur} instance.
	 * If not existing, null is returned.
	 */
	public SlurCache get(Slur slur) {
		return openCurvedLines.get(slur);
	}

	/**
	 * Removes the given {@link Slur} instance.
	 * If not existing, nothing happens.
	 */
	public void remove(Slur slur) {
		openCurvedLines.remove(slur);
	}

	/**
	 * Gets an iterator for all open slurs.
	 */
	@Override public Iterator<SlurCache> iterator() {
		return openCurvedLines.values().iterator();
	}

}
