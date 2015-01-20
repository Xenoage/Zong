package com.xenoage.zong.io.score;

import static com.xenoage.utils.math.Fraction.fr;
import lombok.Getter;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.io.selection.ScoreSelection;


/**
 * This class stores the selected options for the input engine.
 * 
 * There should be only one active instance per application, because the
 * options (like current note duration) is valid for all open scores.
 * 
 * For example, if the user selects half notes, and switches to another score,
 * still half notes should be selected.
 *
 * @author Andreas Wenger
 */
public class ScoreInputOptions {

	/**
	 * The error level: show and log errors, log errors, ignore.
	 */
	public enum ErrorLevel {
		ShowAndLog,
		Log,
		Ignore,
		Throw;
	}
	/** The currently selected error level used for the following operations. */
	@Getter private ErrorLevel errorLevel = ErrorLevel.ShowAndLog;
	/** The currently selected note duration used for the following operations. */
	@Getter private Fraction duration;
	/** The currently selected octave used for the following operations. */
	@Getter private int octave;


	/**
	 * The write mode: Where to write and how to deal with existing elements?
	 * Notice, that is is only a hint for the input classes.
	 * Each implementation may handle these modes in a different
	 * way. See the documentation of the {@link ScoreSelection} implementations.
	 */
	public enum WriteMode {
		/** Overwrite the element after the cursor. */
		Replace,
		/** Add the element to the chord before the cursor. */
		ChordBeforeCursor;
	}
	/** The currently write mode used for the following operations. */
	@Getter private WriteMode writeMode = WriteMode.Replace;


	/**
	 * Creates a new instance of ScoreInputOptions.
	 */
	public ScoreInputOptions() {
		//default duration are quarter notes
		duration = fr(1, 4);
		//default octave is 4
		octave = 4;
	}

	public void setDuration(Fraction duration) {
		this.duration = duration;
	}
	
	public void setErrorLevel(ErrorLevel errorLevel) {
		this.errorLevel = errorLevel;
	}

	public void setWriteMode(WriteMode writeMode) {
		this.writeMode = writeMode;
	}

}
