package com.xenoage.utils.document.command

/**
 * A history of [Command]s executed on the same [CommandPerformer].
 */
class CommandHistory {

	private val history = mutableListOf<Command>()
	private var historyPosition = -1

	/** The last executed [Command], or null if there is nothing to be undone */
	val lastCommand: Command?
		get() = if (historyPosition > -1) history[historyPosition] else null

	/** The last undone [Command], or null if there is nothing to be redone */
	val lastUndoneCommand: Command?
		get() = if (history.size > historyPosition + 1) history[historyPosition + 1] else null

	/** The list of the undoable commands. The first one is the command, that must be undone first */
	val undoableCommands: List<Command>
		get() = history.reversed()

	/** Resets the history. */
	fun clear() {
		history.clear()
		historyPosition = -1
	}

	/**
	 * Adds the given [Command] to the top of the stack and sets it as the
	 * last executed command.
	 */
	fun addCommand(command: Command) {
		history.add(command)
		forward()
	}

	/** Sets the next [Command] as the last executed command. */
	fun forward() {
		historyPosition++
	}

	/** Sets the last [Command] as the last executed command. */
	fun back() {
		historyPosition--
	}

	/** Deletes all commands that follow the current command. */
	fun removeFollowingCommands() {
		val numberToRemove = history.size - historyPosition - 1
		repeat(numberToRemove, { history.removeAt(history.size - 1) })
	}

}
