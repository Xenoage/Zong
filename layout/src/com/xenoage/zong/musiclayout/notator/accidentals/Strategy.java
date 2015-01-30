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
 * Thread-safe.
 * 
 * @author Andreas Wenger
 */
public abstract class Strategy {
	
	class Params {
		//all accidentals, ascending LP
		Accidental[] accs;
		//all notes with accidentals, ascending LP
		NoteDisplacement[] accsNote;
		//widths of musical symbols
		ChordWidths chordWidths;
	}
	
	
	public AccidentalsDisplacement computeAccidentalsDisplacement(List<Pitch> pitches,
		NoteDisplacement[] notes, int accsCount, ChordWidths chordWidths, MusicContext mc) {
		//find notes with accidentals
		Params params = new Params();
		params.accs = new Accidental[accsCount];
		params.accsNote = new NoteDisplacement[params.accs.length];
		int lastIndex = -1;
		for (int i : range(params.accs)) {
			int index = getAccNoteIndex(pitches, lastIndex + 1, mc);
			params.accs[i] = mc.getAccidental(pitches.get(index));
			params.accsNote[i] = notes[index];
			lastIndex = index;
		}
		params.chordWidths = chordWidths;
		//compute displacement
		return compute(params);
	}
	
	abstract AccidentalsDisplacement compute(Params params);
	
	//GOON: not public
	public static int getAccNoteIndex(List<Pitch> pitches, int startIndex, MusicContext mc) {
		for (int i : range(startIndex, pitches.size() - 1))
			if (mc.getAccidental(pitches.get(i)) != null)
				return i;
		throw new IllegalStateException();
	}
	
	static AccidentalsDisplacement create(Params params, float widthIs, float... xIs) {
		AccidentalDisplacement[] a = new AccidentalDisplacement[xIs.length];
		for (int i : range(a))
			a[i] = new AccidentalDisplacement(params.accsNote[i].lp, xIs[i], params.accs[i]);
		return new AccidentalsDisplacement(a, widthIs);
	}

}
