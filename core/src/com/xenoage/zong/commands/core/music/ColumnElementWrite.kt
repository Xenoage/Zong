package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MeasureSide
import com.xenoage.zong.core.position.Beat


/**
 * Writes the given [ColumnElement] at the given beat and measure side in the given
 * [ColumnHeader].
 *
 * The beat and side is not needed for some [ColumnElement]s.
 * See [ColumnHeader.setColumnElement].
 */
class ColumnElementWrite(
		private val element: ColumnElement,
		private val column: ColumnHeader,
		private val beat: Beat? = null,
		private val side: MeasureSide? = null //TODO: get rid of the side. place it in the barline class itself
) : UndoableCommand() {

	//backup
	private var replacedElement: ColumnElement? = null

	override fun execute() {
		replacedElement = column.setColumnElement(element, beat, side)
	}

	override fun undo() {
		column.removeColumnElement(element)
		if (replacedElement != null)
			column.setColumnElement(replacedElement!!, beat, side)
	}

}
