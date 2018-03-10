package com.xenoage.utils.document.command;

/**
 * Base class for a {@link Command}, which is a stopper in the command history
 * (see {@link Undoability#Stopper}).
 * 
 * @author Andreas Wenger
 */
public abstract class StopperCommand
	implements Command {

	@Override public Undoability getUndoability() {
		return Undoability.Stopper;
	}

	@Override public void undo() {
		throw new IllegalStateException("not undoable");
	}

}
