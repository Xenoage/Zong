package com.xenoage.zong.musiclayout.notator.accidentals;

import static com.xenoage.utils.kernel.Range.range;

import java.util.List;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Strategy for displacing accidentals.
 * Not thread-safe.
 * 
 * @author Andreas Wenger
 */
public abstract class Strategy {
	
	List<Pitch> pitches;
	NoteDisplacement[] notes;
	ChordWidths chordWidths;
	MusicContext mc;
	
	//all accidentals, ascending LP
	Accidental[] accs;
	//all notes with accidentals, ascending LP
	NoteDisplacement[] accsNote;
	
	
	
	public AccidentalsDisplacement computeAccidentalsDisplacement(List<Pitch> pitches,
		NoteDisplacement[] notes, ChordWidths chordWidths, MusicContext mc) {
		this.pitches = pitches;
		this.notes = notes;
		this.chordWidths = chordWidths;
		this.mc = mc;
		computeAccsAndAccsNote();
		return compute();
	}
	
	abstract int getAccsCount();
	
	abstract AccidentalsDisplacement compute();
	
	private void computeAccsAndAccsNote() {
		accs = new Accidental[getAccsCount()];
		accsNote = new NoteDisplacement[accs.length];
		int lastIndex = -1;
		for (int i : range(accs)) {
			int index = getAccNoteIndex(lastIndex + 1);
			accs[i] = mc.getAccidental(pitches.get(index));
			accsNote[i] = notes[i];
			lastIndex = index;
		}
	}
	
	private int getAccNoteIndex(int startIndex) {
		for (int i : range(startIndex, pitches.size() - 1))
			if (mc.getAccidental(pitches.get(i)) != null)
				return i;
		throw new IllegalStateException();
	}
	
	AccidentalsDisplacement create(float widthIs, float... xIs) {
		AccidentalDisplacement[] a = new AccidentalDisplacement[xIs.length];
		for (int i : range(a))
			a[i] = new AccidentalDisplacement(accsNote[i].lp, xIs[i], accs[i]);
		return new AccidentalsDisplacement(a, widthIs);
	}

}
