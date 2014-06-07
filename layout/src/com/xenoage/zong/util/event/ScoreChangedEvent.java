package com.xenoage.zong.util.event;

import lombok.Getter;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.Score;

/**
 * This event is fired when the score has been changed.
 * 
 * @author Andreas Wenger
 */
@Getter public final class ScoreChangedEvent {

	/** The score instance which has been modified. */
	private final Score score;

	/** Indices of the measures, whose contents have been
	 * changed, or null, if there was a greater change */
	private final Range measures;


	/**
	 * Creates a new {@link ScoreChangedEvent}. If only the contents of some
	 * measures have been changed, use the {@link #ScoreChangedEvent(Score, Range)}
	 * constructor instead for better performance.
	 * This is the right constructor if measures were added, deleted, inserted
	 * or if the content was heavily changed.
	 * @param score  the score instance which has been modified
	 */
	public ScoreChangedEvent(Score score) {
		this.score = score;
		this.measures = null;
	}

	/**
	 * Creates a new {@link ScoreChangedEvent}.
	 * @param score     the score instance which has been modified
	 * @param measures  the range of measures, whose contents have been changed.
	 *                  This parameter allows the layout engine only to update
	 *                  a small portion of the layout, but is only useful for
	 *                  small changes.
	 */
	public ScoreChangedEvent(Score score, Range measures) {
		this.score = score;
		this.measures = measures;
	}

}
