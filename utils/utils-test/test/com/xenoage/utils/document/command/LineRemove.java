package com.xenoage.utils.document.command;

import com.xenoage.utils.document.Notes;

/**
 * Removes the given line from {@link Notes}.
 * 
 * @author Andreas Wenger
 */
public class LineRemove
	implements Command {

	//data
	private Notes notes;
	private int lineIndex;

	//backup
	private String line;


	public LineRemove(Notes notes, int lineIndex) {
		this.notes = notes;
		this.lineIndex = lineIndex;
	}

	@Override public void execute() {
		line = notes.getLines().remove(lineIndex);
	}

	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	@Override public void undo() {
		notes.getLines().add(lineIndex, line);
	}

}
