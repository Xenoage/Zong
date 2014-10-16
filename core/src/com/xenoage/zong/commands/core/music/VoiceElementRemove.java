package com.xenoage.zong.commands.core.music;

import static com.xenoage.utils.iterators.ReverseIterator.reverseIt;
import static com.xenoage.utils.kernel.Range.rangeReverse;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.annotations.Untested;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.commands.core.music.beam.BeamRemove;
import com.xenoage.zong.commands.core.music.slur.SlurRemove;
import com.xenoage.zong.commands.core.music.tuplet.TupletRemove;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.tuplet.Tuplet;


/**
 * Removes the given {@link VoiceElement}.
 * 
 * All {@link Slur}s, the {@link Beam} and the {@link Tuplet} belonging to this element
 * are removed, too.
 * 
 * @author Andreas Wenger
 */
@Untested public class VoiceElementRemove
	implements Command {
	
	//data
	private VoiceElement element;
	//backup data
	private int elementIndex = 0;
	private List<Command> backupCmds = null;
	
	
	public VoiceElementRemove(VoiceElement element) {
		this.element = element;
	}


	@Override public void execute() {
		Voice voice = element.getParent();
		if (voice == null)
			throw new IllegalStateException("element is not part of a voice");
		
		//remove slurs, beam and tuplet, if it is a chord
		if (element instanceof Chord) {
			Chord chord = (Chord) element;
			//remove slurs
			for (int i : rangeReverse(chord.getSlurs()))
				executeAndRemember(new SlurRemove(chord.getSlurs().get(i)));
			//remove beam
			if (chord.getBeam() != null)
				executeAndRemember(new BeamRemove(chord.getBeam()));
			//remove tuplet
			if (chord.getTuplet() != null)
				executeAndRemember(new TupletRemove(chord.getTuplet()));
		}
		
		//remove element, and remember its position
		elementIndex = voice.removeElement(element);
	}


	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}


	@Override public void undo() {
		Voice voice = element.getParent();
		//add element
		voice.addElement(elementIndex, element);
		//restore slurs, beam and tuplet
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
