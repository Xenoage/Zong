package com.xenoage.utils.document.command

import com.xenoage.utils.document.Document

/**
 * Classes implementing this interface can be notified when a [Command] has been
 * executed or undone on a [Document].
 *
 * This is for example useful to keep the state of undo and redo buttons of the GUI up to date.
 */
interface CommandListener {

	/**
	 * This method is called, when the given [Command] was performed on
	 * the given [Document].
	 */
	fun commandExecuted(document: Document, command: Command)

	/**
	 * This method is called, when the given [Command] was undone on
	 * the given [Document]. When multiple steps are undone at the same time,
	 * the last undone command is given.
	 */
	fun commandUndone(document: Document, command: Command)

}
