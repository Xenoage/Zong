package com.xenoage.zong.io.midi.out;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.position.BMP;


/**
 * This class stores the repetitions in a score by
 * remembering the sequential ranges which have to be played,
 * defined by start and end {@link BMP}s.
 * 
 * @author Andreas Wenger
 */
public final class Playlist
{
	
	
	/**
	 * A range from a given {@link BMP} to a given {@link BMP}.
	 */
	public static final class PlayRange
	{
		public final BMP from, to;

		public PlayRange(BMP from, BMP to)
		{
			this.from = from;
			this.to = to;
		}
	}
	
	
	/** The list of ranges in chronological order. */
	public final PVector<PlayRange> ranges;
	
	
	public Playlist(PVector<PlayRange> ranges)
	{
		this.ranges = ranges;
	}

}
