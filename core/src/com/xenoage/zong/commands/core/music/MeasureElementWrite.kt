package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.music.Measure
import com.xenoage.zong.core.music.MeasureElement


/**
 * Writes the given measure element at the given beat
 * into the given measure (not into the whole measure column).
 * Dependent on its type, it may replace elements of the same type.
 */
class MeasureElementWrite(
		private val element: MeasureElement,
		private val measure: Measure,
		private val beat: Fraction
) : UndoableCommand() {

	//backup data
	private var replacedElement: MeasureElement? = null

	override fun execute() {
		replacedElement = measure.addMeasureElement(element, beat)
	}

	override fun undo() {
		if (replacedElement != null)
			measure.addMeasureElement(replacedElement!!, beat)
	}

}
