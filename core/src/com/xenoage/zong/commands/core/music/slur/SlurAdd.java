package com.xenoage.zong.commands.core.music.slur;

import static com.xenoage.utils.collections.CollectionUtils.addOrNew;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurWaypoint;


/**
 * Adds the given {@link Slur}.
 * 
 * @author Andreas Wenger
 */
public class SlurAdd
	implements Command {
	
	//data
	private Slur slur;

	
	public SlurAdd(Slur slur) {
		this.slur = slur;
	}
	
	
	@Override public void execute() {
		//add slur to chords
		for (SlurWaypoint wp : slur.getWaypoints()) {
			Chord chord = wp.getChord();
			chord.setSlurs(addOrNew(chord.getSlurs(), slur));
		}
	}

	
	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		new SlurRemove(slur).execute();
	}

}
