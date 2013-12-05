package com.xenoage.zong.musiclayout.layouter.cache;

import java.util.HashMap;
import java.util.Iterator;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.layouter.cache.util.BeamedStemStampings;

/**
 * Cache for beams which still have to be created.
 * 
 * @author Andreas Wenger
 */
public class OpenBeamsCache
	implements Iterable<BeamedStemStampings> {

	//beams, which are not stamped yet
	private HashMap<Beam, BeamedStemStampings> openBeams = new HashMap<Beam, BeamedStemStampings>();


	/**
	 * Gets the number of open beams.
	 */
	public int size() {
		return openBeams.size();
	}

	/**
	 * Gets the {@link BeamedStemStampings} instance for the given {@link Beam}.
	 * If not existing already, it is created.
	 */
	public BeamedStemStampings get(Beam beam) {
		BeamedStemStampings ret = openBeams.get(beam);
		if (ret == null) {
			ret = new BeamedStemStampings(beam);
			openBeams.put(beam, ret);
		}
		return ret;
	}

	/**
	 * Sets the {@link BeamedStemStampings} instance for the given {@link Beam}.
	 */
	public void set(Beam beam, BeamedStemStampings bss) {
		openBeams.put(beam, bss);
	}

	/**
	 * Gets an iterator for all open beams.
	 */
	@Override public Iterator<BeamedStemStampings> iterator() {
		return openBeams.values().iterator();
	}

}
