package com.xenoage.utils.document.command

import com.xenoage.utils.document.Document

/**
 * Interface for commands. Based on the Command Design Pattern.
 *
 * Examples for commands are actions for opening or printing a document,
 * inserting or deleting content and so on.
 *
 * Commands may be undoable and redoable, which is defined in an [Undoability] value.
 *
 * Commands should not be invoked directly, but always on the [Document] which they affect,
 * since the history for undoing the commands is saved there and not within this class.
 *
 * The naming of the commands is suggested as follows (where appropriate):
 * [Object][Verb] (like "LineAdd" or "ImageRemove").
 */
interface Command {

	/** The international name of the command */
	val name: String

	/** The undoability level of this command */
	val undoability: Undoability

	/** Executes or redoes the command */
	fun execute()

	/** Undoes this command */
	fun undo()

}
