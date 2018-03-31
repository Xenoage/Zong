package com.xenoage.utils.document.command;

/**
 * Base class for a {@link Command}, which is transparent in the command history
 * (see {@link Undoability#Transparent}).
 * 
 * @author Andreas Wenger
 */
public abstract class TransparentCommand
	implements Command {

	@Override public Undoability getUndoability() {
		return Undoability.Transparent;
	}

	@Override public void undo() {
		throw new IllegalStateException("not undoable");
	}

}
