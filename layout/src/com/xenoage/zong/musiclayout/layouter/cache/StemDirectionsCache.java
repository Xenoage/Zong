package com.xenoage.zong.musiclayout.layouter.cache;

import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.HashMap;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Cache for the computed {@link StemDirection} of {@link Chord}s.
 * 
 * @author Andreas Wenger
 */
public class StemDirectionsCache {
	
	private HashMap<Chord, StemDirection> cache = map();
	
	public StemDirection get(Chord chord) {
		return cache.get(chord);
	}
	
	public void set(Chord chord, StemDirection stemDir) {
		cache.put(chord, stemDir);
	}
	
	public void clear() {
		cache.clear();
	}

}
