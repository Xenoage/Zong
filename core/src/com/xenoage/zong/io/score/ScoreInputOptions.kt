package com.xenoage.zong.io.score

import com.xenoage.utils.math.Fraction.fr
import lombok.Getter

import com.xenoage.utils.math.Fraction
import com.xenoage.zong.io.selection.ScoreSelection


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
class ScoreInputOptions {


	/** The currently selected error level used for the following operations.  */
	@Getter
	private var errorLevel = ErrorLevel.ShowAndLog
	/** The currently selected note duration used for the following operations.  */
	@Getter
	private var duration: Fraction? = null
	/** The currently selected octave used for the following operations.  */
	@Getter
	private val octave: Int


	/** The currently write mode used for the following operations.  */
	@Getter
	private var writeMode = WriteMode.Replace

	/**
	 * Creates a new instance of ScoreInputOptions.
	 */
	init {
		//default duration are quarter notes
		duration = Companion.fr(1, 4)
		//default octave is 4
		octave = 4
	}

	fun setDuration(duration: Fraction) {
		this.duration = duration
	}

	fun setErrorLevel(errorLevel: ErrorLevel) {
		this.errorLevel = errorLevel
	}

	fun setWriteMode(writeMode: WriteMode) {
		this.writeMode = writeMode
	}

}