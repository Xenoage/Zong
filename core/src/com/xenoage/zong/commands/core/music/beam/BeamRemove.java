package com.xenoage.zong.commands.core.music.beam;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.tuplet.Tuplet;


/**
 * Removes the given {@link Tuplet}.
 * 
 * @author Andreas Wenger
 */
public class BeamRemove
	implements Command {
	
	//data
	private Beam beam;
	
	
	public BeamRemove(Beam beam) {
		this.beam = beam;
	}


	@Override public void execute() {
		for (BeamWaypoint wp : beam.getWaypoints()) {
			wp.getChord().setBeam(null);
		}
	}


	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}


	@Override public void undo() {
		for (BeamWaypoint wp : beam.getWaypoints()) {
			wp.getChord().setBeam(beam);
		}
	}

}
