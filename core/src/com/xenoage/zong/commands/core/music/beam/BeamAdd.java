package com.xenoage.zong.commands.core.music.beam;

import static com.xenoage.utils.iterators.ReverseIterator.reverseIt;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;


/**
 * Adds the given {@link Beam}.
 * 
 * If the contained chords already are beamed, those beams
 * are removed.
 * 
 * @author Andreas Wenger
 */
public class BeamAdd
	implements Command {
	
	//data
	private Beam beam;
	//backup
	private List<Command> backupCmds;

	
	public BeamAdd(Beam beam) {
		this.beam = beam;
	}
	
	
	@Override public void execute() {
		//remove existing beams
		for (BeamWaypoint wp : beam.getWaypoints()) {
			if (wp.getChord().getBeam() != null)
				executeAndRemember(new BeamRemove(wp.getChord().getBeam()));
		}
		//add beam to chords
		for (BeamWaypoint wp : beam.getWaypoints()) {
			wp.getChord().setBeam(beam);
		}
	}

	
	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		//remove beam
		new BeamRemove(beam).execute();
		//restore old beams
		if (backupCmds != null) {
			for (Command cmd : reverseIt(backupCmds))
				cmd.undo();
		}
	}
	
	
	private void executeAndRemember(Command cmd) {
		if (backupCmds == null)
			backupCmds = new ArrayList<Command>();
		cmd.execute();
		backupCmds.add(cmd);
	}

}
