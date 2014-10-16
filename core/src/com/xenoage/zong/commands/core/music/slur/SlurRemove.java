package com.xenoage.zong.commands.core.music.slur;

import static com.xenoage.utils.collections.CollectionUtils.removeOrEmpty;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurWaypoint;


/**
 * Removes the given {@link Slur}.
 * 
 * @author Andreas Wenger
 */
public class SlurRemove
	implements Command {

	//data
	private Slur slur;
	
	
	public SlurRemove(Slur slur) {
		this.slur = slur;
	}
	
	
	@Override public void execute() {
		//remove slur from chords
		for (SlurWaypoint wp : slur.getWaypoints()) {
			Chord chord = wp.getChord();
			chord.setSlurs(removeOrEmpty(chord.getSlurs(), slur));
		}
	}

	
	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		new SlurAdd(slur).execute();
	}
	
	

}
