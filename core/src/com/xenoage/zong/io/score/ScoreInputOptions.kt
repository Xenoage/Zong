package com.xenoage.zong.io.score

import com.xenoage.utils.math._1_4
import com.xenoage.zong.core.music.util.Duration


/**
 * Options for the input engine.
 *
 * There should be only one active instance per application, because the
 * options (like current note duration) is valid for all open scores.
 *
 * For example, if the user selects half notes, and switches to another score,
 * still half notes should be selected.
 */
class ScoreInputOptions {

	/** The currently selected error level used for the following operations. */
	var errorLevel = ErrorLevel.ShowAndLog

	/** The currently selected note duration used for the following operations. */
	var duration: Duration = _1_4

	/** The currently selected octave used for the following operations. */
	var octave: Int = 4

	/** The currently write mode used for the following operations. */
	var writeMode = WriteMode.Replace

	/**
	 * The error level: show and log errors, log errors, ignore.
	 */
	enum class ErrorLevel {
		ShowAndLog,
		Log,
		Ignore,
		Throw
	}

	/**
	 * The write mode: Where to write and how to deal with existing elements?
	 * Notice, that is is only a hint for the input classes.
	 * Each implementation may handle these modes in a different
	 * way. See the documentation of the {@link ScoreSelection} implementations.
	 */
	enum class WriteMode  {
		/** Overwrite the element after the cursor. */
		Replace,
		/** Add the element to the chord before the cursor. */
		ChordBeforeCursor;
	}

}