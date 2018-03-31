package com.xenoage.utils.document.command;

import com.xenoage.utils.document.Document;

/**
 * Classes implementing this interface can be notified when a {@link Command} has been
 * executed or undone on a {@link Document}.
 * 
 * This is for example useful to keep the state of undo and redo buttons of the GUI up to date.
 * 
 * @author Andreas Wenger
 */
public interface CommandListener {

	/**
	 * This method is called, when the given {@link Command} was performed on
	 * the given {@link Document}.
	 */
	public void commandExecuted(Document document, Command command);

	/**
	 * This method is called, when the given {@link Command} was undone on
	 * the given {@link Document}. When multiple steps are undone at the same time,
	 * the last undone command is given.
	 */
	public void commandUndone(Document document, Command command);

}
