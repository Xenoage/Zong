package com.xenoage.zong.musiclayout.layouter.cache;

import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.kernel.Tuple3.t3;

import java.util.HashMap;

import com.xenoage.utils.iterators.It;
import com.xenoage.utils.kernel.Tuple3;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.musiclayout.stampings.NoteheadStamping;
import com.xenoage.zong.musiclayout.stampings.StaffTextStamping;

/**
 * Cache for lyric elements which still have to be created.
 * Since normal lyrics and hyphens can be created immediately,
 * this cache is only needed for auxiliary elements like
 * underscore lines ("___") to indicate syllables
 * that span over several chords.
 * 
 * @author Andreas Wenger
 */
public class OpenLyricsCache {

	//cache for open underscore lyrics: left syllable (starting point) and currently
	//rightmost notehead (ending point)
	//TIDY: own class for Tuple3<StaffTextStamping, NoteheadStamping, Integer>
	private HashMap<Lyric, Tuple3<StaffTextStamping, NoteheadStamping, Integer>> openUnderscores = map();


	/**
	 * Adds or changes the given underscore line that startes at
	 * the given left syllable and ends at the given notehead.
	 */
	public void setUnderscore(Lyric start, StaffTextStamping leftSyllable,
		NoteheadStamping rightmostNotehead, int staffIndex) {
		openUnderscores.put(start, t3(leftSyllable, rightmostNotehead, staffIndex));
	}

	/**
	 * Gets the underscore line belonging to the given lyric, or null.
	 */
	public Tuple3<StaffTextStamping, NoteheadStamping, Integer> getUnderscore(Lyric lyric) {
		return openUnderscores.get(lyric);
	}

	/**
	 * Gets all open underscore lines.
	 */
	public It<Tuple3<StaffTextStamping, NoteheadStamping, Integer>> getUnderscores() {
		return new It<Tuple3<StaffTextStamping, NoteheadStamping, Integer>>(openUnderscores.values());
	}

}
