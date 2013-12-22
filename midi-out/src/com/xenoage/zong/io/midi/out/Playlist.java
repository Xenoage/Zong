package com.xenoage.zong.io.midi.out;

import java.util.List;

import com.xenoage.zong.core.position.MP;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class stores the repetitions in a score by
 * remembering the sequential ranges which have to be played,
 * defined by start and end {@link MP}s.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class Playlist {

	/**
	 * A range from a given {@link MP} to a given {@link MP}.
	 */
	@AllArgsConstructor
	public static final class PlayRange {

		public final MP from, to;
	}


	/** The list of ranges in chronological order. */
	private List<PlayRange> ranges;

}
