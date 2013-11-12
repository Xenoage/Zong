package com.xenoage.zong.util.event;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.Score;


/**
 * This event is fired when the score has been
 * changed.
 * 
 * @author Andreas Wenger
 */
public final class ScoreChangedEvent
{
	
	/** The previous version of the score */
	public final Score oldScore;
	
	/** The modified version of the score */
	public final Score newScore;
	
	/** Indices of the measures, whose contents have been
	 * changed, or null, if there was a greater change */
	public final Range measures;
	
	
	/**
	 * Creates a new {@link ScoreChangedEvent}. If only the contents of some
	 * measures have been changed, use the <code>ScoreChangedEvent(Score, Score, Range)</code>
	 * constructor instead for better performance.
	 * This is the right constructor if measures were added, deleted, inserted
	 * or if the content was heavily changed.
	 * @param oldScore     the previous version of the score
	 * @param newScore     the modified version of the score
	 */
	public ScoreChangedEvent(Score oldScore, Score newScore)
	{
		this.oldScore = oldScore;
		this.newScore = newScore;
		this.measures = null;
	}
	
	
	/**
	 * Creates a new {@link ScoreChangedEvent}.
	 * @param oldScore     the previous version of the score
	 * @param newScore     the modified version of the score
	 * @param measures  the range of measures, whose contents have been changed.
	 *                  This parameter allows the layout engine only to update
	 *                  a small portion of the layout, but is only useful for
	 *                  small changes.
	 */
	public ScoreChangedEvent(Score oldScore, Score newScore, Range measures)
	{
		this.oldScore = oldScore;
		this.newScore = newScore;
		this.measures = measures;
	}
	

}
