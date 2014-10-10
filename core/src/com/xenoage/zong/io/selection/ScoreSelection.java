package com.xenoage.zong.io.selection;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.score.ScoreInputOptions;
import com.xenoage.zong.utils.exceptions.IllegalMPException;
import com.xenoage.zong.utils.exceptions.MeasureFullException;


/**
 * Interface for selections within a score.
 * 
 * A simple cursor, a list of elements or staves and many
 * other things may form a selection.
 * 
 * A selection always contains a score. When one of the
 * <code>write</code> methods is called, the selection and the score is transformed in
 * some way, maybe dependent on the given {@link ScoreInputOptions}.
 *
 * @author Andreas Wenger
 */
public interface ScoreSelection
	extends Selection {


	/**
	 * Gets the score behind the selection.
	 */
	public Score getScore();


	/**
	 * Gets the {@link MP}, that fits best to describe this
	 * selection (most often, this will be the start position).
	 */
	public MP getMP();


	/**
	 * Writes the given pitches, e.g. as a chord.
	 * Each type of selection may have a different behavior.
	 */
	public void write(Pitch[] pitches, ScoreInputOptions options)
		throws IllegalMPException, MeasureFullException;


	/**
	 * Writes the given {@link ColumnElement}.
	 * Each type of selection may have a different behavior.
	 */
	public void write(ColumnElement element)
		throws IllegalMPException;


	/**
	 * Writes the given {@link ColumnElement}.
	 * Each type of selection may have a different behavior.
	 */
	public void write(ColumnElement element, ScoreInputOptions options)
		throws IllegalMPException, MeasureFullException;


	/**
	 * Writes the given {@link VoiceElement}.
	 * Each type of selection may have a different behavior.
	 */
	public void write(VoiceElement element)
		throws IllegalMPException, MeasureFullException;


	/**
	 * Writes the given {@link VoiceElement}.
	 * Each type of selection may have a different behavior.
	 */
	public void write(VoiceElement element, ScoreInputOptions options)
		throws IllegalMPException, MeasureFullException;


	/**
	 * Writes the given {@link MeasureElement} element.
	 * Dependent on its type, it may replace elements of the same type.
	 * Each type of selection may have a different
	 * behavior.
	 */
	public void write(MeasureElement element)
		throws IllegalMPException;


	/**
	 * Writes the given {@link MeasureElement} element.
	 * Dependent on its type, it may replace elements of the same type.
	 * Each type of selection may have a different
	 * behavior.
	 */
	public void write(MeasureElement element, ScoreInputOptions options)
		throws IllegalMPException;

}
