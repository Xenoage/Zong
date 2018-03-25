package com.xenoage.utils.document.command

/**
 * Base class for a [Command], which is transparent in the command history
 * (see [Undoability.Transparent]).
 */
abstract class UndoableCommand : Command {

	override val name: String =
			this::class.simpleName ?: "Unnamed"

	override val undoability: Undoability
		get() = Undoability.Transparent

	override fun undo() {
		throw IllegalStateException("not undoable")
	}

}
