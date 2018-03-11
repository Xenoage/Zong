package com.xenoage.utils.document.command

/**
 * Base class for a [Command], which is a stopper in the command history
 * (see [Undoability.Stopper]).
 */
abstract class StopperCommand(override val name: String) : Command {

	override val undoability: Undoability
		get() = Undoability.Stopper

	override fun undo() {
		throw IllegalStateException("not undoable")
	}

}
