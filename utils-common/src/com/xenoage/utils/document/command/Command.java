package com.xenoage.utils.document.command;

import com.xenoage.utils.document.Document;

/**
 * Interface for commands. Based on the Command Design Pattern.
 * 
 * Examples for commands are actions for opening or printing a document,
 * inserting or deleting content and so on.
 * 
 * Commands may be undoable and redoable, which is defined in an {@link Undoability} value.
 * 
 * Commands should not be invoked directly, but always on the {@link Document} which they affect,
 * since the history for undoing the commands is saved there and not within this class.
 * 
 * The naming of the commands is suggested as follows (where appropriate):
 * [Object][Verb] (like "LineAdd" or "ImageRemove").
 * 
 * @author Andreas Wenger
 */
public interface Command {

	/**
	 * Executes or redoes the command.
	 */
	public void execute();

	/**
	 * Returns the undoability level of this command.
	 */
	public Undoability getUndoability();

	/**
	 * Undoes this command.
	 */
	public void undo();

}
