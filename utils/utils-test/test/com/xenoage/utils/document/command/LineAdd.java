package com.xenoage.utils.document.command;

import com.xenoage.utils.document.Notes;


/**
 * Adds a line at the end of {@link Notes}.
 * 
 * @author Andreas Wenger
 */
public class LineAdd
	implements Command {
	
	//data
	private Notes notes;
	private String line;
	
	
	public LineAdd(Notes notes, String line) {
		this.notes = notes;
		this.line = line;
	}
	

	@Override public void execute() {
		notes.getLines().add(line);
	}

	
	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	@Override public void undo() {
		notes.getLines().remove(notes.getLines().size() - 1);
	}

}
