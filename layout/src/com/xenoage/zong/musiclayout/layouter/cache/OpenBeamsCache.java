package com.xenoage.zong.musiclayout.layouter.cache;

import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.HashMap;
import java.util.Iterator;

import com.xenoage.zong.musiclayout.layouter.cache.util.BeamedStemStampings;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.stampings.StemStamping;

/**
 * Cache for beams which still have to be created.
 * 
 * @author Andreas Wenger
 */
public class OpenBeamsCache
	implements Iterable<BeamedStemStampings> {

	//beams, which are not stamped yet
	private HashMap<BeamNotation, BeamedStemStampings> openBeams = map();


	/**
	 * Gets the number of open beams.
	 */
	public int size() {
		return openBeams.size();
	}

	/**
	 * Gets the {@link BeamedStemStampings} instance for the given {@link BeamNotation}.
	 * If not existing already, it is created.
	 */
	public BeamedStemStampings get(BeamNotation beam) {
		BeamedStemStampings ret = openBeams.get(beam);
		if (ret == null) {
			ret = new BeamedStemStampings(beam, new StemStamping[beam.element.size()]);
			openBeams.put(beam, ret);
		}
		return ret;
	}

	/**
	 * Sets the {@link BeamedStemStampings} instance for the given {@link BeamNotation}.
	 */
	public void set(BeamNotation beam, BeamedStemStampings bss) {
		openBeams.put(beam, bss);
	}

	/**
	 * Gets an iterator for all open beams.
	 */
	@Override public Iterator<BeamedStemStampings> iterator() {
		return openBeams.values().iterator();
	}

}
