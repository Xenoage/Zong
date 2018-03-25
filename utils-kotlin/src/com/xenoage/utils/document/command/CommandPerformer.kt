package com.xenoage.utils.document.command

import com.xenoage.utils.document.Document
import com.xenoage.utils.document.CancelledException

import com.xenoage.utils.document.PropertyAlreadySetException
import com.xenoage.utils.document.UselessException
import com.xenoage.utils.document.command.Undoability.*
import com.xenoage.utils.log.log
import com.xenoage.utils.log.remark

import kotlin.math.log

/**
 * This class performs [Command]s on a [Document] and supports undoing and redoing them.
 *
 * [CommandListener] can be registered for monitoring the actions.
 */
class CommandPerformer(
		/** The [Document] this performer is working on.
		 *  May be null for an app-wide command performer. */
		val document: Document) {

	private val history = CommandHistory()
	private val listeners = mutableListOf<CommandListener>()

	/** True, if the last executed command can be undone */
	val isUndoPossible: Boolean
		get() = history.lastCommand != null

	/** True, if the last undone command can be executed again */
	val isRedoPossible: Boolean
		get() = history.lastUndoneCommand != null

	/**
	 * Executes the given [Command] and updates the command history according to
	 * its [Undoability].
	 */
	fun execute(command: Command) {
		log(remark("${command.name} is executed..."))
		try {
			command.execute()
			//non-transparent commands break the chain of redoable commands
			//(already undone commands can not be redone any more as soon as a new command was executed)
			val undo = command.undoability
			if (undo != Transparent)
				history.removeFollowingCommands()
			//add undoable command to history
			if (undo == Undoable)
				history.addCommand(command)
			//stopper commands clear the history
			if (undo == Stopper)
				history.clear()
			//notify listeners
			listeners.forEach { it.commandExecuted(document, command) }
		} catch (ex: CancelledException) {
			//user cancelled the action.
			log(remark("Command cancelled."))
		} catch (ex: UselessException) {
			//the action was cancelled because it has no effect.
			log(remark("Command was cancelled, because it has no effect."))
		} catch (ex: PropertyAlreadySetException) {
			//no problem. we just ignore that exception.
			log(remark("Command was cancelled, because property was already set."))
		}

	}

	/**
	 * Undoes the last command, when possible.
	 */
	fun undo() {
		val command = history.lastCommand
		if (isUndoPossible && command != null) {
			log(remark("${command.name} is undone..."))
			//undo the command
			command.undo()
			//go back in history
			history.back()
			//notify listeners
			listeners.forEach { it.commandUndone(document, command) }
		}
	}

	/**
	 * Undoes the given number of commands in the history, if possible.
	 */
	fun undoMultipleSteps(steps: Int) {
		if (steps < 1) return
		log(remark("Multiple undo steps ($steps)..."))
		var command: Command? = null
		var i = 0
		while (i < steps && isUndoPossible) {
			command = history.lastCommand!!
			log(remark("${command.name} is undone..."))
			//undo the command
			command.undo()
			//go back in history
			history.back()
			i++
		}
		//notify listeners
		if (command != null)
			listeners.forEach { it.commandUndone(document, command) }
	}

	/**
	 * Executes the last undone command again, when possible.
	 */
	fun redo() {
		if (false == isRedoPossible) return
		val command = history.lastUndoneCommand!!
		log(remark("${command.name} is redone..."))
		//execute the command
		command.execute()
		//go forward in history
		history.forward()
		//notify listeners
		listeners.forEach { it.commandExecuted(document, command) }
	}

	/** Registers the given [CommandListener]. */
	fun addCommandListener(listener: CommandListener) {
		listeners.add(listener)
	}

	/** Unregisters the given [CommandListener]. */
	fun removeCommandListener(listener: CommandListener) {
		listeners.remove(listener)
	}

}
