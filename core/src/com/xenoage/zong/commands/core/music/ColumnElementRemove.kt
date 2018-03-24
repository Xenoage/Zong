package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.Undoability
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MeasureSide


/**
 * Removes the given [ColumnElement] from the given [ColumnHeader].
 */
class ColumnElementRemove(
		private val column: ColumnHeader,
		private val element: ColumnElement
) : UndoableCommand() {

	//backup
	private var beat: Fraction? = null
	private var side: MeasureSide? = null

	override fun execute() {
		beat = column.getChildMP(element)!!.beat
		side = column.getSide(element)
		column.removeColumnElement(element)
	}

	override fun undo() {
		column.setColumnElement(element, beat, side)
	}

}
