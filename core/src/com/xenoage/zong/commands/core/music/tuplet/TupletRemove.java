package com.xenoage.zong.commands.core.music.tuplet;

import com.xenoage.utils.annotations.Untested;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.tuplet.Tuplet;


/**
 * Removes the given {@link Tuplet}.
 * 
 * @author Andreas Wenger
 */
@Untested public class TupletRemove
	implements Command {
	
	//data
	private Tuplet tuplet;
	
	
	public TupletRemove(Tuplet tuplet) {
		this.tuplet = tuplet;
	}


	@Override public void execute() {
		for (Chord chord : tuplet.getChords()) {
			chord.setTuplet(null);
		}
	}


	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}


	@Override public void undo() {
		for (Chord chord : tuplet.getChords()) {
			chord.setTuplet(tuplet);
		}
	}

}
